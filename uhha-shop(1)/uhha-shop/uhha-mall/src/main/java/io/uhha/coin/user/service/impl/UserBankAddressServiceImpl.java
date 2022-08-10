package io.uhha.coin.user.service.impl;

import io.uhha.coin.capital.domain.FPool;
import io.uhha.coin.capital.mapper.FPoolMapper;
import io.uhha.coin.common.Enum.BankInfoStatusEnum;
import io.uhha.coin.common.Enum.LogUserActionEnum;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.util.MQSend;
import io.uhha.common.utils.Utils;
import io.uhha.coin.user.domain.FUserBankinfo;
import io.uhha.coin.user.domain.FUserVirtualAddress;
import io.uhha.coin.user.domain.FUserVirtualAddressWithdraw;
import io.uhha.coin.user.mapper.FUserBankinfoMapper;
import io.uhha.coin.user.mapper.FUserVirtualAddressMapper;
import io.uhha.coin.user.mapper.FUserVirtualAddressWithdrawMapper;
import io.uhha.coin.user.service.UserBankAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户银行卡、地址接口实现
 * @author ZKF
 */
@Service("userBandAddressService")
public class UserBankAddressServiceImpl implements UserBankAddressService {

	@Autowired
	private FUserBankinfoMapper userBankInfoMapper;
	@Autowired
	private FUserVirtualAddressWithdrawMapper userVirtualAddressWithdrawMapper;
	@Autowired
	private FUserVirtualAddressMapper userVirtualAddressMapper;
	@Autowired
	private FPoolMapper poolMapper;
	@Autowired
	private MQSend mqSend;

	/**
	 * 添加用户提现银行卡
	 * @param bankInfo 用户银行卡实体对象
	 * @param ip IP地址
	 * @return true：成功，false：失败
	 * @throws BCException
	 * @see UserBankAddressService#insertBankInfo
	 */
	@Override
	public boolean insertBankInfo(FUserBankinfo bankInfo, String ip) throws BCException {
		boolean b = selectIsExistBankInfo(bankInfo);
		if (b){
			return true;
		}
		int result = userBankInfoMapper.insert(bankInfo);
		if (result <= 0) {
			return false;
		}
		// MQ_USER_ACTION
		mqSend.SendUserAction(bankInfo.getFuid(), LogUserActionEnum.ADD_BANK, ip);
		return true;
	}

	/**
	 * 获取银行卡
	 * @param id 用户银行卡主键ID
	 * @return 用户银行卡实体对象
	 * @see UserBankAddressService#selectUserBankInfoById
	 */
	@Override
	public FUserBankinfo selectUserBankInfoById(int id) {
		return userBankInfoMapper.selectByPrimaryKey(id);
	}

	/**
	 * 查询用户提现银行卡
	 * @param fuid 用户ID
	 * @param ftype 类型
	 * @return 用户银行卡实体对象列表
	 * @see UserBankAddressService#selectUserBankInfolist
	 */
	@Override
	public List<FUserBankinfo> selectUserBankInfolist(Long fuid, Integer ftype) {
		List<FUserBankinfo> list = userBankInfoMapper.getBankInfoListByUser(fuid, ftype);
		/*for (FUserBankinfo fUserBankinfo : list) {
			FSystemBankinfoWithdraw bank = systemBankinfoWithdrawMapper.selectByPrimaryKey(fUserBankinfo.getFbanktype());
			if (bank != null) {
				fUserBankinfo.setFbanktype_s(bank.getFcnname());
			}
		}*/
		return list;
	}

	/**
	 * 查询用户提现银行卡
	 * @param fBankinfo 用户银行卡实体对象
	 * @return 用户银行卡实体对象列表
	 * @see UserBankAddressService#selectUserBankInfolist
	 */
	@Override
	public List<FUserBankinfo> selectUserBankInfolist(FUserBankinfo fBankinfo) {
		return userBankInfoMapper.getBankInfoListByBankInfo(fBankinfo);
	}

	/**
	 * 添加虚拟币提现地址
	 * @param address 虚拟币提现地址实体对象
	 * @param ip IP地址
	 * @return true：成功，false：失败
	 * @throws BCException
	 * @see UserBankAddressService#insertVirtualCoinWithdrawAddress
	 */

	@Override
	public boolean insertVirtualCoinWithdrawAddress(FUserVirtualAddressWithdraw address, String ip) throws BCException {
		int result = userVirtualAddressWithdrawMapper.insert(address);
		if (result <= 0) {
			return false;
		}
		// MQ_USER_ACTION
		mqSend.SendUserAction(address.getFuid(), LogUserActionEnum.ADD_ADDRESS_WITHDRAW, ip);
		return true;
	}

	/**
	 * 获取虚拟币提现地址
	 * @param address 虚拟币提现地址实体对象
	 * @return 虚拟币提现地址实体对象列表
	 * @see UserBankAddressService#selectVirtualCoinWithdrawAddressList
	 */
	@Override
	public List<FUserVirtualAddressWithdraw> selectVirtualCoinWithdrawAddressList(FUserVirtualAddressWithdraw address) {
		return userVirtualAddressWithdrawMapper.getVirtualCoinWithdrawAddressList(address);
	}

	/**
	 * 获取虚拟币提现地址
	 * @param id 虚拟币提现地址主键ID
	 * @return 虚拟币提现地址实体对象
	 * @see UserBankAddressService#selectVirtualCoinWithdrawAddressById(int)
	 */
	@Override
	public FUserVirtualAddressWithdraw selectVirtualCoinWithdrawAddressById(int id) {
		return userVirtualAddressWithdrawMapper.selectByPrimaryKey(id);
	}

	/**
	 * 添加虚拟币充值地址
	 * @param fuid		用户id
	 * @param fcoinid	虚拟币id
	 * @param ip		IP地址
	 * @return 			虚拟币充值地址实体对象
	 * @throws BCException
	 * @see UserBankAddressService#insertVirtualCoinAddress
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public FUserVirtualAddress insertVirtualCoinAddress(Long fuid, Integer fcoinid, String ip) throws BCException {
		FUserVirtualAddress virtualAddress = userVirtualAddressMapper.selectByUserAndCoin(fuid, fcoinid);
		if (virtualAddress == null) {
			FPool fpool = this.poolMapper.selectOneFpool(fcoinid);
			if (fpool == null) {
				return null;
			}
			String address = fpool.getFaddress();

			FUserVirtualAddress fvirtualaddress = new FUserVirtualAddress();
			fvirtualaddress.setFadderess(address);
			fvirtualaddress.setFcreatetime(Utils.getTimestamp());
			fvirtualaddress.setFuid(fuid);
			fvirtualaddress.setFcoinid(fcoinid);
			if (address == null || address.trim().equalsIgnoreCase("null") || address.trim().equals("")) {
				return null;
			}
			// 修改充值地址的使用状态标志位为已使用
			int result = this.poolMapper.updatePoolStatus(fpool);
			if (result > 0) {
				result = this.userVirtualAddressMapper.insert(fvirtualaddress);
			}
			if (result <= 0) {
				throw new BCException();
			}
			// MQ_USER_ACTION
			mqSend.SendUserAction(fuid, LogUserActionEnum.ADD_ADDRESS_RECHARGE, ip);
			return fvirtualaddress;
		} else {
			return virtualAddress;
		}
	}

	/**
	 * 添加ETH类虚拟币充值地址
	 * @param fuid		用户id
	 * @param fcoinid	虚拟币id
	 * @param ip		IP地址
	 * @param  ethid	以太坊id
	 * @return 			虚拟币充值地址实体对象
	 * @throws BCException
	 * @see UserBankAddressService#insertVirtualCoinAddress
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public FUserVirtualAddress insertETHVirtualCoinAddress(Long fuid,Integer fcoinid, String ip,Integer ethid) throws BCException {
		List<FUserVirtualAddress> virtualAddress = userVirtualAddressMapper.selectETHsByUserAndCoin(fuid, fcoinid);
		FUserVirtualAddress fvirtualaddress = new FUserVirtualAddress();
		if (virtualAddress.size()<=0) {
			FPool fpool = this.poolMapper.selectOneFpool(ethid);
			if (fpool == null) {
				return null;
			}
			String address = fpool.getFaddress();
			if (address == null || address.trim().equalsIgnoreCase("null") || address.trim().equals("")) {
				return null;
			}
			fvirtualaddress.setFadderess(address);
			// 修改充值地址的使用状态标志位为已使用
			this.poolMapper.updatePoolStatus(fpool);
		} else {
			fvirtualaddress.setFadderess(virtualAddress.get(0).getFadderess());
		}
		fvirtualaddress.setFcreatetime(Utils.getTimestamp());
		fvirtualaddress.setFuid(fuid);
		fvirtualaddress.setFcoinid(fcoinid);
		fvirtualaddress.setFcreatetime(Utils.getTimestamp());
		int	result = this.userVirtualAddressMapper.insert(fvirtualaddress);
		if (result <= 0) {
			throw new BCException();
		}
		// MQ_USER_ACTION
		mqSend.SendUserAction(fuid, LogUserActionEnum.ADD_ADDRESS_RECHARGE, ip);
		return fvirtualaddress;
	}
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public FUserVirtualAddress insertLETVirtualCoinAddress(Long fuid,Integer fcoinid, String ip,Integer letid) throws BCException {
		List<FUserVirtualAddress> virtualAddress = userVirtualAddressMapper.selectLETsByUserAndCoin(fuid, fcoinid);
		FUserVirtualAddress fvirtualaddress = new FUserVirtualAddress();
		if (virtualAddress.size()<=0) {
			FPool fpool = this.poolMapper.selectOneFpool(letid);
			if (fpool == null) {
				return null;
			}
			String address = fpool.getFaddress();
			if (address == null || address.trim().equalsIgnoreCase("null") || address.trim().equals("")) {
				return null;
			}
			fvirtualaddress.setFadderess(address);
			// 修改充值地址的使用状态标志位为已使用
			this.poolMapper.updatePoolStatus(fpool);
		} else {
			fvirtualaddress.setFadderess(virtualAddress.get(0).getFadderess());
		}
		fvirtualaddress.setFcreatetime(Utils.getTimestamp());
		fvirtualaddress.setFuid(fuid);
		fvirtualaddress.setFcoinid(fcoinid);
		fvirtualaddress.setFcreatetime(Utils.getTimestamp());
		int	result = this.userVirtualAddressMapper.insert(fvirtualaddress);
		if (result <= 0) {
			throw new BCException();
		}
		// MQ_USER_ACTION
		mqSend.SendUserAction(fuid, LogUserActionEnum.ADD_ADDRESS_RECHARGE, ip);
		return fvirtualaddress;
	}

	/**
	 * 根据用户和币种查询充值地址
	 * @param fuid 用户ID
	 * @param fcoinid 虚拟币ID
	 * @return 虚拟币充值地址实体对象
	 * @see UserBankAddressService#selectVirtualAddressByUserAndCoin
	 */
	@Override
	public FUserVirtualAddress selectVirtualAddressByUserAndCoin(Long fuid, Integer fcoinid) {
		return userVirtualAddressMapper.selectByUserAndCoin(fuid, fcoinid);
	}

	/**
	 * 根据用户和币种查询用户以太坊类钱包的充值地址
	 * @param fuid	用户id
	 * @param fcoinid 虚拟币id
	 * @return
	 */
	@Override
	public List<FUserVirtualAddress> selectETHsVirtualAddressByUserAndCoin(Long fuid, Integer fcoinid) {
		return userVirtualAddressMapper.selectETHsByUserAndCoin(fuid,fcoinid);
	}

	/**
	 * 根据用户和币种查询充值地址
	 * @param fuid 用户ID
	 * @return 虚拟币充值地址实体对象列表
	 * @see UserBankAddressService#selectVirtualAddressByUser
	 */
	@Override
	public List<FUserVirtualAddress> selectVirtualAddressByUser(Long fuid) {
		return userVirtualAddressMapper.selectByUser(fuid);
	}

	/**
	 * 根据用户ID和银行卡号查询银行卡信息是否存在
	 * @param bankInfo 用户银行卡实体对象
	 * @return true：成功，false：失败
	 * @throws BCException
	 * @see UserBankAddressService#selectIsExistBankInfo
	 */
	@Override
	public boolean selectIsExistBankInfo(FUserBankinfo bankInfo) throws BCException {
		bankInfo.setFstatus(BankInfoStatusEnum.NORMAL_VALUE);
		List<FUserBankinfo> list = userBankInfoMapper.getBankInfoListByBankInfo(bankInfo);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 更新提现银行卡信息
	 * @param record 用户银行卡实体对象
	 * @return true：成功，false：失败
	 * @throws BCException
	 * @see UserBankAddressService#updateBankInfo
	 */
	@Override
	public boolean updateBankInfo(FUserBankinfo record) throws BCException {
		int result = userBankInfoMapper.updateByPrimaryKey(record);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 更新虚拟币提现地址
	 * @param address 虚拟币提现地址实体对象
	 * @return true：成功，false：失败
	 * @see UserBankAddressService#updateVirtualCoinWithdrawAddress
	 */
	@Override
	public boolean updateVirtualCoinWithdrawAddress(FUserVirtualAddressWithdraw address) {
		int result = userVirtualAddressWithdrawMapper.updateByPrimaryKey(address);
		if (result <= 0) {
			return false;
		}
		return true;
	}

}
