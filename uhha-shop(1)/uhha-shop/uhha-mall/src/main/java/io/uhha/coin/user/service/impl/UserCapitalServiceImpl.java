package io.uhha.coin.user.service.impl;

import io.uhha.coin.capital.mapper.FTokenCapitalOperationMapper;
import io.uhha.coin.capital.mapper.FVirtualCapitalOperationMapper;
import io.uhha.coin.capital.mapper.FWalletCapitalOperationMapper;
import io.uhha.coin.common.Enum.*;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.capital.domain.FTokenCapitalOperation;
import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.capital.domain.FWalletCapitalOperation;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.common.util.MQSend;
import io.uhha.coin.system.domain.SystemCoinSetting;
import io.uhha.coin.user.domain.FUserVirtualAddressWithdraw;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.framework.mq.ScoreHelper;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.common.utils.Utils;
import io.uhha.coin.user.mapper.*;
import io.uhha.coin.user.service.UserCapitalService;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.member.domain.UmsMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资产计算接口实现
 * @author ZKF
 */
@Service("userCapitalService")
@Slf4j
public class UserCapitalServiceImpl extends UserWalletBaseImpl implements UserCapitalService {

	@Autowired
	private UserCoinWalletMapper userCoinWalletMapper;
	@Autowired
	private RedisCryptoHelper redisCryptoHelper;
	@Autowired
	private FWalletCapitalOperationMapper walletCapitalOperationMapper;
	@Autowired
	private FTokenCapitalOperationMapper tokenCapitalOperationMapper;
	@Autowired
	private FVirtualCapitalOperationMapper virtualCapitalOperationMapper;
	@Autowired
	private FUserVirtualAddressMapper fUserVirtualAddressMapper;
	@Autowired
	private MQSend mqSend;
	@Autowired
	private ScoreHelper scoreHelper;


	/**
	 * 用户每日钱包提现次数
	 * @param fuid 用户ID
	 * @return 人民币每日提现次数
	 * @see UserCapitalService#selectWalletWithdrawTimes(Long)
	 */
	@Override
	public int selectWalletWithdrawTimes(Long fuid) {
		return walletCapitalOperationMapper.getWalletWithdrawTimes(fuid);
	}

	/**
	 * 用户每日虚拟币提现次数
	 * @param fuid 用户ID
	 * @return 虚拟币每日提现次数
	 * @see UserCapitalService#selectVirtualWalletWithdrawTimes
	 */
	@Override
	public int selectVirtualWalletWithdrawTimes(Long fuid) {
		return virtualCapitalOperationMapper.getVirtualWalletWithdrawTimes(fuid);
	}

	/**
	 * 淘币类型撤销提现
	 * */

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateTokenCancelOperationWithdraw(Long fuid, FTokenCapitalOperation operation, String ip) throws BCException {

		UserCoinWallet fwallet = userCoinWalletMapper.selectByUidAndCoinLock(fuid, operation.getFcoinid());
		BigDecimal totalToker = MathUtils.add(MathUtils.add(fwallet.getTotal(), operation.getFfees()), operation.getFamount());
		BigDecimal frozenToker = MathUtils.sub(MathUtils.sub(fwallet.getFrozen(), operation.getFfees()), operation.getFamount());
		fwallet.setFrozen(frozenToker);
		fwallet.setTotal(totalToker);

		operation.setFstatus(CapitalOperationOutStatus.Cancel);
		operation.setFupdatetime(Utils.getTimestamp());
		int result = tokenCapitalOperationMapper.updateByPrimaryKey(operation);
		if (result <= 0) {
			log.error("tokenCapitalOperationMapper.updateByPrimaryKey update database failed. check database situation.");
			return false;
		}
		int result2 = userCoinWalletMapper.updateByPrimaryKey(fwallet);
		if (result2 <= 0) {
			log.error("userCoinWalletMapper.updateByPrimaryKe update database failed. check database situation.");
			throw new BCException();
		}
		// MQ
		mqSend.SendUserAction(fuid, LogUserActionEnum.TOKER_WITHDRAW_CANCEL, operation.getFamount(), ip);
		return true;
	}


	/**
	 * 根据id查询人民币 充值记录
	 * @param fid 人民币充提记录表实体对象主键ID
	 * @return 人民币充提记录表实体对象
	 * @see UserCapitalService#selectWalletCapitalOperationById(int)
	 */
	@Override
	public FWalletCapitalOperation selectWalletCapitalOperationById(int fid){
		FWalletCapitalOperation operation = walletCapitalOperationMapper.selectByPrimaryKey(fid);
		if(operation != null){
			operation.setFamount(MathUtils.toScaleNum(operation.getFamount(), MathUtils.DEF_CNY_SCALE));
			operation.setFfees(MathUtils.toScaleNum(operation.getFfees(), MathUtils.DEF_CNY_SCALE));
		}
		return operation;
	}
	@Override
	public FTokenCapitalOperation selectTokenCapitalOperationById(int fid){
		FTokenCapitalOperation operation = tokenCapitalOperationMapper.selectByPrimaryKey(fid);
		if(operation != null){
			operation.setFamount(MathUtils.toScaleNum(operation.getFamount(), MathUtils.DEF_CNY_SCALE));
			operation.setFfees(MathUtils.toScaleNum(operation.getFfees(), MathUtils.DEF_CNY_SCALE));
		}
		return operation;
	}

	/**
	 * 查询人民币 充值记录
	 * @param param 分页实体对象
	 * @param operation 人民币充提记录表实体对象
	 * @return 分页实体对象
	 * @see UserCapitalService#selectWalletCapitalOperation
	 */
	@Override
	public Pagination<FWalletCapitalOperation> selectWalletCapitalOperation(Pagination<FWalletCapitalOperation> param,FWalletCapitalOperation operation){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", param.getOffset());
		map.put("limit", param.getPageSize());
		map.put("fuid", operation.getFuid());
		map.put("ftype", operation.getFtype());
		map.put("finouttype", operation.getFinouttype());
		map.put("begindate", param.getBegindate());
		map.put("enddate", param.getEnddate());
		int count  = walletCapitalOperationMapper.countWalletCapitalOperation(map);
		if(count > 0) {
			List<FWalletCapitalOperation> list = walletCapitalOperationMapper.getPageWalletCapitalOperation(map);
			for (FWalletCapitalOperation fWalletCapitalOperation : list) {
				fWalletCapitalOperation.setFamount(MathUtils.toScaleNum(fWalletCapitalOperation.getFamount(), MathUtils.DEF_CNY_SCALE));
				fWalletCapitalOperation.setFfees(MathUtils.toScaleNum(fWalletCapitalOperation.getFfees(), MathUtils.DEF_CNY_SCALE));
			}
			param.setData(list);
		}
	    param.setTotalRows(count);
	    param.generate();
	    return param;
	}

    @Override
    public Pagination<FTokenCapitalOperation> selectTokenCapitalOperation(Pagination<FTokenCapitalOperation> param, FTokenCapitalOperation operation){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", param.getOffset());
        map.put("limit", param.getPageSize());
        map.put("fuid", operation.getFuid());
        map.put("finouttype", operation.getFinouttype());
        map.put("begindate", param.getBegindate());
        map.put("enddate", param.getEnddate());
        int count  = tokenCapitalOperationMapper.countTokenCapitalOperation(map);
        if(count > 0) {
            List<FTokenCapitalOperation> list = tokenCapitalOperationMapper.getPageTokenCapitalOperation(map);
            for (FTokenCapitalOperation fTokenCapitalOperation : list) {
                fTokenCapitalOperation.setFamount(MathUtils.toScaleNum(fTokenCapitalOperation.getFamount(), MathUtils.DEF_CNY_SCALE));
                fTokenCapitalOperation.setFfees(MathUtils.toScaleNum(fTokenCapitalOperation.getFfees(), MathUtils.DEF_CNY_SCALE));
            }
            param.setData(list);
        }
        param.setTotalRows(count);
        param.generate();
        return param;
    }


	/**
	 * 新增虚拟币充值或提现记录
	 * @param user 用户实体对象
	 * @param coinid 虚拟币ID
	 * @param addressWithdraw 提现地址
	 * @param vwalletId 虚拟币钱包ID
	 * @param withdrawAmount 提现金额
	 * @param BTCFees 网络手续费
	 * @return true：成功，false：失败
	 * @throws BCException
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertVirtualCapitalOperation(
			UmsMember user, int coinid, FUserVirtualAddressWithdraw addressWithdraw, int vwalletId,
			BigDecimal withdrawAmount, BigDecimal BTCFees, DataSourceEnum sourceEnum, PlatformEnum platformEnum) throws BCException {
		FVirtualCapitalOperation fvirtualcaptualoperation = new FVirtualCapitalOperation();
		SystemCoinSetting fee = redisCryptoHelper.getCoinSetting(coinid, user.getCustomerLevel().getName());
		if(fee == null){
			log.error("coin setting was not found in Redis. coinid={},level={}",coinid, user.getCustomerLevel().getName());
			return false;
		}

		BigDecimal fees = MathUtils.mul(withdrawAmount, fee.getWithdrawFee());
		BigDecimal amt = MathUtils.toScaleNum(MathUtils.sub(MathUtils.sub(withdrawAmount, BTCFees), fees),
				MathUtils.ENTER_COIN_SCALE);
		fvirtualcaptualoperation.setFcoinid(coinid);
		fvirtualcaptualoperation.setFamount(amt);
		fvirtualcaptualoperation.setFcreatetime(Utils.getTimestamp());
		fvirtualcaptualoperation.setFfees(fees);
		fvirtualcaptualoperation.setFbtcfees(BTCFees);
		fvirtualcaptualoperation.setFupdatetime(Utils.getTimestamp());
		fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation);
		fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_OUT.getCode());
		fvirtualcaptualoperation.setFuid(user.getId());
		fvirtualcaptualoperation.setFwithdrawaddress(addressWithdraw.getFadderess());
		fvirtualcaptualoperation.setFaddressWithdrawId(addressWithdraw.getFid());
		fvirtualcaptualoperation.setFhasowner(false);
		fvirtualcaptualoperation.setVersion(0);
		fvirtualcaptualoperation.setFsource(sourceEnum.getCode());
		fvirtualcaptualoperation.setFplatform(platformEnum.getCode());
		int result = virtualCapitalOperationMapper.insert(fvirtualcaptualoperation);
		if (result <= 0) {
			log.error("virtualCapitalOperationMapper.insert update database failed. check database situation.");
			throw new BCException();
		}

		BigDecimal opAmount = MathUtils.add(MathUtils.add(amt, fees), BTCFees);
		//钱包操作
		boolean resultStatus = super.updateUserCoinWallet(user.getId(), coinid, MathUtils.positive2Negative(opAmount), opAmount);
		if(!resultStatus){
			log.error("updateUserCoinWallet failed. check database situation.");
			throw new BCException();
		}
		// MQ
		mqSend.SendUserAction(user.getId(), LogUserActionEnum.COIN_WITHDRAW_WAIT, coinid, amt, user.getIp());
		return true;
	}

	/**
	 * 根据id查询虚拟币提现记录
	 * @param fid 虚拟币充提记录表主键ID
	 * @return 虚拟币充提记录表实体对象
	 * @see UserCapitalService#selectVirtualCapitalOperationById(int)
	 */
	@Override
	public FVirtualCapitalOperation selectVirtualCapitalOperationById(int fid){
		FVirtualCapitalOperation operation = virtualCapitalOperationMapper.selectByPrimaryKey(fid);
		if(operation != null){
			operation.setFamount(MathUtils.toScaleNum(operation.getFamount(), MathUtils.DEF_COIN_SCALE));
			operation.setFfees(MathUtils.toScaleNum(operation.getFfees(), MathUtils.DEF_COIN_SCALE));
		}
		return operation;
	}

	/**
	 * 查询虚拟币 充值记录
	 * @param param 分页实体对象
	 * @param operation 虚拟币充提记录表实体对象
	 * @return 分页实体对象
	 * @see UserCapitalService#selectVirtualCapitalOperation(Pagination, FVirtualCapitalOperation)
	 */
	@Override
	public Pagination<FVirtualCapitalOperation> selectVirtualCapitalOperation(Pagination<FVirtualCapitalOperation> param,FVirtualCapitalOperation operation){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", param.getOffset());
		map.put("limit", param.getPageSize());
		map.put("fuid", operation.getFuid());
		if(operation.getFtype()!=null && !operation.getFtype().equals("")){
			map.put("ftype", operation.getFtype());
		}
		map.put("fcoinid", operation.getFcoinid());
		map.put("begindate", param.getBegindate());
		map.put("enddate", param.getEnddate());

		int count  = virtualCapitalOperationMapper.countVirtualCapitalOperation(map);
		if(count > 0) {
			List<FVirtualCapitalOperation> list = virtualCapitalOperationMapper.getPageVirtualCapitalOperation(map);
			for (FVirtualCapitalOperation fVirtualCapitalOperation : list) {
				if (fVirtualCapitalOperation.getFrechargeaddress() != null){
					if (fVirtualCapitalOperation.getFrechargeaddress().getBytes().length > 60){
						fVirtualCapitalOperation.setFrechargeaddress(fVirtualCapitalOperation.getFrechargeaddress().replace(fVirtualCapitalOperation.getFrechargeaddress().substring(17,51),"****"));
					}
				}
				fVirtualCapitalOperation.setFamount(MathUtils.toScaleNum(fVirtualCapitalOperation.getFamount(), MathUtils.DEF_COIN_SCALE));
				fVirtualCapitalOperation.setFfees(MathUtils.toScaleNum(fVirtualCapitalOperation.getFfees(), MathUtils.DEF_COIN_SCALE));
			}
			param.setData(list);
		}
	    param.setTotalRows(count);
	    param.generate();
	    return param;
	}

	/**
	 * 更新虚拟币提现撤单
	 * @param user 用户实体
	 * @param operation 虚拟币充提记录表实体对象
	 * @return true：成功，false：失败
	 * @throws BCException
	 * @see UserCapitalService#updateVirtualCancelOperationWithdraw
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateVirtualCancelOperationWithdraw(UmsMember user, FVirtualCapitalOperation operation) throws BCException {

		operation.setFstatus(VirtualCapitalOperationOutStatusEnum.Cancel);
		operation.setFupdatetime(Utils.getTimestamp());
		int result = virtualCapitalOperationMapper.updateByPrimaryKey(operation);
		if (result <= 0) {
			log.error("virtualCapitalOperationMapper.updateByPrimaryKey update database failed. check database situation.");
			return false;
		}

		BigDecimal amount = MathUtils.add(MathUtils.add(operation.getFamount(), operation.getFfees()), operation.getFbtcfees());

		boolean resultStatus = super.updateUserCoinWallet(user.getId(), operation.getFcoinid(), amount, MathUtils.positive2Negative(amount));
		if(!resultStatus){
			throw new BCException();
		}
		// MQ
		mqSend.SendUserAction(user.getId(), LogUserActionEnum.COIN_WITHDRAW_CANCEL, operation.getFcoinid(), operation.getFamount(), user.getIp());
		return true;
	}



	/**
	 * 获取用户虚拟币钱包
	 * @param fuid 用户ID
	 * @param fcoinId 虚拟币ID
	 * @return 虚拟币钱包实体对象
	 * @see UserCapitalService#getCoinWalletByUidAndId(Long, Integer)
	 */
	@Override
	public UserCoinWallet getCoinWalletByUidAndId(Long fuid, Integer fcoinId) {
		UserCoinWallet vwallet = userCoinWalletMapper.selectByUidAndCoin(fuid, fcoinId);
		if(vwallet != null){
			vwallet.setTotal(MathUtils.toScaleNum(vwallet.getTotal(), MathUtils.DEF_COIN_SCALE));
			vwallet.setFrozen(MathUtils.toScaleNum(vwallet.getFrozen(), MathUtils.DEF_COIN_SCALE));
			vwallet.setBorrow(MathUtils.toScaleNum(vwallet.getBorrow(), MathUtils.DEF_COIN_SCALE));
		}
		return vwallet;
	}

	/**
	 * 条件查询用户钱包
	 * @param captal
	 * @param userCoinWallet
	 * @return
	 */
	@Override
	public Pagination<UserCoinWallet> listVirtualWalletByParam(Pagination<UserCoinWallet> captal,UserCoinWallet userCoinWallet) {
		Map<String,Object> map = new HashMap<>();
		map.put("uid",userCoinWallet.getUid());
		map.put("keywords",userCoinWallet.getCoinName());
		map.put("offset",captal.getOffset());
		map.put("limit",captal.getPageSize());
		int count = userCoinWalletMapper.countByParam(map);
		if(count>0){
			List<UserCoinWallet> list = userCoinWalletMapper.selectByParam(map);
			if(list != null && list.size()>0){
				for (UserCoinWallet wallet : list) {
					wallet.setTotal(MathUtils.toScaleNum(wallet.getTotal(), MathUtils.DEF_COIN_SCALE));
					wallet.setFrozen(MathUtils.toScaleNum(wallet.getFrozen(), MathUtils.DEF_COIN_SCALE));
					wallet.setBorrow(MathUtils.toScaleNum(wallet.getBorrow(), MathUtils.DEF_COIN_SCALE));
				}
			}
			captal.setData(list);
		}
		captal.setTotalRows(count);
		return captal;
	}

	/**
	 * 获取用户虚拟币钱包
	 * @param fuid 用户ID
	 * @return 虚拟币钱包实体对象列表
	 * @see UserCapitalService#listVirtualWalletById(Long)
	 */
	@Override
	public List<UserCoinWallet> listVirtualWalletById(Long fuid) {
		List<UserCoinWallet> list = userCoinWalletMapper.selectByUid(fuid);
		if(list != null && list.size()>0){
			for (UserCoinWallet wallet : list) {
				wallet.setTotal(MathUtils.toScaleNum(wallet.getTotal(), MathUtils.DEF_COIN_SCALE));
				wallet.setFrozen(MathUtils.toScaleNum(wallet.getFrozen(), MathUtils.DEF_COIN_SCALE));
				wallet.setBorrow(MathUtils.toScaleNum(wallet.getBorrow(), MathUtils.DEF_COIN_SCALE));
			}
		}
		return list;
	}

	/**
	 * 判断是否首次充值
	 * @param fuid 用户ID
	 * @return true：是，false：否
	 * @see UserCapitalService#selectIsFirstCharge
	 */
	@Override
	public boolean selectIsFirstCharge(Long fuid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fuid", fuid);
		map.put("finouttype", CapitalOperationInOutTypeEnum.IN.getCode());
		map.put("fstatus", CapitalOperationInStatus.Come);
		int countCny = walletCapitalOperationMapper.countWalletCapitalOperation(map);

		map.clear();
		map.put("fuid", fuid);
		map.put("ftype", VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
		map.put("fstatus", VirtualCapitalOperationInStatusEnum.SUCCESS);
		int countCoin = virtualCapitalOperationMapper.countVirtualCapitalOperation(map);

		return (countCny + countCoin) > 0 ? false : true;
	}


	/**
	 * 更新用户钱包
	 * @param userCoinWallet
	 * @return
	 * @throws BCException
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateUserCoinWallet(UserCoinWallet userCoinWallet) throws Exception {
		int i = userCoinWalletMapper.updateByPrimaryKey(userCoinWallet);
		if(i<=0){
			throw new Exception();
		}
		return i > 0 ? true : false;
	}
}
