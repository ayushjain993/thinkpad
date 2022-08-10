package io.uhha.coin.user.service.impl;

import io.uhha.coin.capital.domain.FLogConsoleVirtualRecharge;
import io.uhha.coin.capital.mapper.FLogConsoleVirtualRechargeMapper;
import io.uhha.coin.common.coin.driver.ETHDriver;
import io.uhha.coin.log.domain.FLogModifyCapitalOperation;
import io.uhha.coin.capital.domain.FTokenCapitalOperation;
import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.capital.domain.FWalletCapitalOperation;
import io.uhha.coin.log.mapper.FLogModifyCapitalOperationMapper;
import io.uhha.coin.capital.mapper.FTokenCapitalOperationMapper;
import io.uhha.coin.capital.mapper.FVirtualCapitalOperationMapper;
import io.uhha.coin.capital.mapper.FWalletCapitalOperationMapper;
import io.uhha.coin.common.Enum.*;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.system.domain.FSysCoinOperation;
import io.uhha.coin.system.service.IFSysCoinWalletService;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.common.exception.BCException;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.coin.common.util.MQSend;
import io.uhha.common.utils.Utils;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.user.domain.FUserVirtualAddress;
import io.uhha.coin.user.mapper.*;
import io.uhha.coin.user.service.IAdminUserCapitalService;
import io.uhha.common.core.domain.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户资金操作
 *
 * @author ZKF
 */
@Service("adminUserCapitalService")
public class AdminUserCapitalServiceImpl implements IAdminUserCapitalService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FWalletCapitalOperationMapper walletCapitalOperationMapper;
    @Autowired
    private FVirtualCapitalOperationMapper virtualCapitalOperationMapper;
    @Autowired
    private FLogModifyCapitalOperationMapper logModifyCapitalOperationMapper;
    @Autowired
    private UserCoinWalletMapper userCoinWalletMapper;
    @Autowired
    private FUserVirtualAddressMapper userVirtualAddressMapper;
    @Autowired
    private FLogConsoleVirtualRechargeMapper logConsoleVirtualRechargeMapper;
    @Autowired
    private RedisCryptoHelper redisCryptoHelper;
    @Autowired
    private MQSend mqSend;
    @Autowired
    private FUserVirtualAddressWithdrawMapper userVirtualAddressWithdrawMapper;
    @Autowired
    private FTokenCapitalOperationMapper tokerCapitalOperationMapper;
    @Autowired
    private IFSysCoinWalletService sysCoinWalletService;

    /**
     * 分页查询钱包操作记录
     *
     * @param pageParam 分页参数
     * @param record    实体参数
     * @param status    状态列表
     * @return 分页查询记录列表
     * @see IAdminUserCapitalService#selectWalletCapitalOperationList(Pagination,
     * FWalletCapitalOperation, List)
     */
    @Override
    public Pagination<FWalletCapitalOperation> selectWalletCapitalOperationList(
            Pagination<FWalletCapitalOperation> pageParam, FWalletCapitalOperation record, List<Integer> status) {
        // 组装查询条件数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageParam.getOffset());
        map.put("limit", pageParam.getPageSize());
        map.put("orderField", pageParam.getOrderField());
        map.put("orderDirection", pageParam.getOrderDirection());
        map.put("keyword", pageParam.getKeyword());
        map.put("finouttype", record.getFinouttype());
        map.put("fstatus", status.size() > 0 ? status : null);
        map.put("fid", record.getFid());
        map.put("finouttype", record.getFinouttype());
        map.put("start", pageParam.getBegindate());
        map.put("end", pageParam.getEnddate());
        // 查询总数
        int count = walletCapitalOperationMapper.countAdminPage(map);
        if (count > 0) {
            // 查询数据
            List<FWalletCapitalOperation> list = walletCapitalOperationMapper.getAdminPageList(map);
            // 设置返回数据
            pageParam.setData(list);
        }
        pageParam.setTotalRows(count);
        return pageParam;
    }

    /**
     * 分页查询淘币钱包操作记录
     *
     * @param pageParam 分页参数
     * @param record    实体参数
     * @param status    状态列表
     * @return 分页查询记录列表
     * @author ZGY
     */
    @Override
    public Pagination<FTokenCapitalOperation> selectTokenCapitalOperationList(
            Pagination<FTokenCapitalOperation> pageParam, FTokenCapitalOperation record, List<Integer> status) {
        // 组装查询条件数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageParam.getOffset());
        map.put("limit", pageParam.getPageSize());
        map.put("orderField", pageParam.getOrderField());
        map.put("orderDirection", pageParam.getOrderDirection());
        map.put("keyword", pageParam.getKeyword());
        map.put("finouttype", record.getFinouttype());
        map.put("faccount", record.getFaccount());
        map.put("fstatus", status.size() > 0 ? status : null);
        map.put("start", pageParam.getBegindate());
        map.put("end", pageParam.getEnddate());
        // 查询总数
        int count = tokerCapitalOperationMapper.countAdminPage(map);
        if (count > 0) {
            // 查询数据
            List<FTokenCapitalOperation> list = tokerCapitalOperationMapper.getAdminPageList(map);
            // 设置返回数据
            pageParam.setData(list);
        }
        pageParam.setTotalRows(count);
        return pageParam;
    }

    /**
     * 根据id查询钱包操作记录
     *
     * @param fid 操作id
     * @return 钱包操作实体
     * @see IAdminUserCapitalService#selectById(int)
     */
    @Override
    public FWalletCapitalOperation selectById(int fid) {
        return walletCapitalOperationMapper.selectByPrimaryKey(fid);
    }

    /**
     * 根据id查询淘币操作记录
     *
     * @param fid 操作id
     * @return 淘币操作实体
     * @author ZGY
     */
    @Override
    public FTokenCapitalOperation selectTokenById(Integer fid) {
        return tokerCapitalOperationMapper.selectTokenById(fid);
    }

    /**
     * 获取用户钱包
     *
     * @param fuid 用户id
     * @return 用户钱包实体
     */
    @Override
    public UserCoinWallet selectUserWallet(Long fuid, int fcoinid) {
        return userCoinWalletMapper.selectByUidAndCoin(fuid, fcoinid);
    }

    /**
     * 更新钱包操作
     *
     * @param admin      管理员
     * @param capital    钱包操作记录
     * @param amount     总量
     * @param isRecharge 是否充值
     * @return 是否执行成功
     * @throws BCException 更新异常
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateWalletCapital(LoginUser admin, FWalletCapitalOperation capital,
                                       BigDecimal amount, boolean isRecharge) throws Exception {
        UserCoinWallet userWallet = userCoinWalletMapper.selectByUidAndCoinLock(capital.getFuid(), capital.getFcoinid());
        if (isRecharge) {
            // 审核充值
            userWallet.setTotal(MathUtils.add(userWallet.getTotal(), amount));
        } else {
            // 审核提现
            if (capital.getFstatus() == CapitalOperationOutStatus.OperationSuccess) {
                userWallet.setFrozen(MathUtils.sub(userWallet.getFrozen(), amount));
            }
            // 取消提现
            if (capital.getFstatus() == CapitalOperationOutStatus.Cancel) {
                userWallet.setFrozen(MathUtils.sub(userWallet.getFrozen(), amount));
                userWallet.setTotal(MathUtils.add(userWallet.getTotal(), amount));
            }
        }
        userWallet.setGmtModified(Utils.getTimestamp());
        if (walletCapitalOperationMapper.updateByPrimaryKey(capital) <= 0) {
            throw new Exception("update Capital err");
        }
        if (userCoinWalletMapper.updateByPrimaryKey(userWallet) <= 0) {
            throw new Exception("update userWallet err");
        }
        // MQ
        if (isRecharge) {
            // RMB充值
            mqSend.SendUserAction(capital.getFuid(), LogUserActionEnum.RMB_RECHARGE, capital.getFtype(), amount);
            mqSend.SendAdminAction(admin.getUserId(), capital.getFuid(), LogAdminActionEnum.SYSTEM_RMB_RECHARGE, capital.getFtype(), amount);
        } else {
            // RMB提现
            if (capital.getFstatus() == CapitalOperationOutStatus.OperationSuccess) {
                mqSend.SendUserAction(capital.getFuid(), LogUserActionEnum.RMB_WITHDRAW, CapitalOperationTypeEnum.RMB_OUT, amount, capital.getFfees());
                mqSend.SendAdminAction(admin.getUserId(), capital.getFuid(), LogAdminActionEnum.SYSTEM_COIN_WITHDRAW, capital.getFtype(), amount);
            }
            // RMB取消提现
            if (capital.getFstatus() == CapitalOperationOutStatus.Cancel) {
                mqSend.SendUserAction(capital.getFuid(), LogUserActionEnum.RMB_WITHDRAW_CANCEL, CapitalOperationTypeEnum.RMB_OUT, amount, "admin_" + admin.getUserId());
                mqSend.SendAdminAction(admin.getUserId(), capital.getFuid(), LogAdminActionEnum.SYSTEM_RMB_WITHDRAW, capital.getFtype(), amount);
            }
        }
        return true;
    }

    /**
     * 更新钱包操作记录
     *
     * @param capital 操作记录
     * @return 是否执行成功
     * @throws BCException 更新异常
     * @see IAdminUserCapitalService#updateWalletCapital(FWalletCapitalOperation)
     */
    @Override
    public boolean updateWalletCapital(FWalletCapitalOperation capital) throws BCException {
        int result = walletCapitalOperationMapper.updateByPrimaryKey(capital);
        if (result <= 0) {
            return false;
        }
        mqSend.SendAdminAction(capital.getFadminid(), capital.getFuid(), LogAdminActionEnum.CANCEL_RMB_RECHARGE, capital.getFtype(), capital.getFamount());
        return true;
    }

    @Override
    public boolean updateTokenCapital(FTokenCapitalOperation capital) throws Exception {
        int result = tokerCapitalOperationMapper.updateByPrimaryKey(capital);
        if (result <= 0) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateTokenCapital(LoginUser admin, FTokenCapitalOperation capital,
                                      BigDecimal amount) throws Exception {
        UserCoinWallet userWallet = userCoinWalletMapper.selectByUidAndCoinLock(capital.getFuid(), capital.getFcoinid());
        if (capital.getFinouttype() == CapitalOperationTypeEnum.TOKEN_IN) {
            // 审核充值
            userWallet.setTotal(MathUtils.add(userWallet.getTotal(), amount));
        } else if (capital.getFinouttype() == CapitalOperationTypeEnum.TOKEN_OUT) {
            // 审核提现
            if (capital.getFstatus() == CapitalOperationOutStatus.OperationSuccess) {
                userWallet.setFrozen(MathUtils.sub(userWallet.getFrozen(), amount));
            }
            // 取消提现
            if (capital.getFstatus() == CapitalOperationOutStatus.Cancel) {
                userWallet.setFrozen(MathUtils.sub(userWallet.getFrozen(), amount));
                userWallet.setTotal(MathUtils.add(userWallet.getTotal(), amount));
            }
        }
        userWallet.setGmtModified(Utils.getTimestamp());
        if (tokerCapitalOperationMapper.updateByPrimaryKey(capital) <= 0) {
            throw new Exception("update Capital err");
        }
        if (userCoinWalletMapper.updateByPrimaryKey(userWallet) <= 0) {
            throw new Exception("update userWallet err");
        }
        // MQ
        if (capital.getFinouttype() == CapitalOperationTypeEnum.TOKEN_IN) {
            // TOKER充值
            mqSend.SendUserAction(capital.getFuid(), LogUserActionEnum.TOKER_RECHARGE, CapitalOperationTypeEnum.TOKEN_IN, amount);
            mqSend.SendAdminAction(admin.getUserId(), capital.getFuid(), LogAdminActionEnum.SYSTEM_TOKEN_RECHARGE, CapitalOperationTypeEnum.TOKEN_IN, amount);
        } else {
            // TOKER提现
            if (capital.getFstatus() == CapitalOperationOutStatus.OperationSuccess) {
                mqSend.SendUserAction(capital.getFuid(), LogUserActionEnum.TOKER_WITHDRAW, CapitalOperationTypeEnum.TOKEN_OUT, amount, capital.getFfees());
                mqSend.SendAdminAction(admin.getUserId(), capital.getFuid(), LogAdminActionEnum.SYSTEM_TOKER_WITHDRAW, CapitalOperationTypeEnum.TOKEN_OUT, amount);
            }
            // TOKER取消提现
            if (capital.getFstatus() == CapitalOperationOutStatus.Cancel) {
                mqSend.SendUserAction(capital.getFuid(), LogUserActionEnum.TOKER_WITHDRAW_CANCEL, CapitalOperationTypeEnum.TOKEN_OUT, amount, "admin_" + admin.getUserId());
                mqSend.SendAdminAction(admin.getUserId(), capital.getFuid(), LogAdminActionEnum.SYSTEM_TOKER_WITHDRAW, CapitalOperationTypeEnum.TOKEN_OUT, amount);
            }
        }
        return true;
    }

    /**
     * 是否第一次充值
     *
     * @param fuid 用户id
     * @return 是否第一次充值
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

        return countCny + countCoin > 0 ? false : true;
    }

    /**
     * 更新充值记录
     *
     * @param capital    钱包操作记录
     * @param capitalLog 修改日志
     * @return 是否修改成功
     * @throws BCException 更新失败
     * @see IAdminUserCapitalService#updateModifyCapital(FWalletCapitalOperation,
     * FLogModifyCapitalOperation)
     */
    @Override
    public boolean updateModifyCapital(FWalletCapitalOperation capital, FLogModifyCapitalOperation capitalLog) throws BCException {
        int result = walletCapitalOperationMapper.updateByPrimaryKey(capital);
        if (result <= 0) {
            return false;
        }
        result = logModifyCapitalOperationMapper.insert(capitalLog);
        if (result <= 0) {
            throw new BCException();
        }

        mqSend.SendAdminAction(capitalLog.getFadminid(), capital.getFuid(), LogAdminActionEnum.SYSTEM_MODIFY_RMB_RECHARGE, capital.getFtype(), capitalLog.getFamount());
        return true;
    }

    /**
     * 分页查询虚拟币操作记录
     *
     * @param pageParam 分页参数
     * @param record    实体参数
     * @param status    状态列表
     * @return 分页查询记录列表
     * @see IAdminUserCapitalService#selectVirtualCapitalOperationList(Pagination,
     * FVirtualCapitalOperation, List)
     */
    @Override
    public Pagination<FVirtualCapitalOperation> selectVirtualCapitalOperationList(Pagination<FVirtualCapitalOperation> pageParam, FVirtualCapitalOperation record, List<Integer> status) {
        // 组装查询条件数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageParam.getOffset());
        map.put("limit", pageParam.getPageSize());
        map.put("orderField", pageParam.getOrderField());
        map.put("orderDirection", pageParam.getOrderDirection());
        map.put("keyword", pageParam.getKeyword());
        map.put("ftype", record.getFtype());
        map.put("fcoinid", record.getFcoinid());
        map.put("fstatus", status.size() > 0 ? status : null);

        map.put("start", pageParam.getBegindate());
        map.put("end", pageParam.getEnddate());
        // 查询总数
        int count = virtualCapitalOperationMapper.countAdminPage(map);
        if (count > 0) {
            // 查询数据
            List<FVirtualCapitalOperation> list = virtualCapitalOperationMapper.getAdminPageList(map);
            for (FVirtualCapitalOperation fv : list) {
                //默认外部提币
                int isFromOurself = 0;
                SystemCoinType virtualCoinType = redisCryptoHelper.getCoinType(fv.getFcoinid());

                if (fv.getFwithdrawaddress() != null) {
                    isFromOurself = userVirtualAddressMapper.getAddressNum(fv.getFwithdrawaddress());
                }
                fv.setIsFromOurself(isFromOurself);
            }
            // 设置返回数据
            pageParam.setData(list);
        }
        pageParam.setTotalRows(count);
        return pageParam;
    }

    /**
     * 根据id查询虚拟币操作记录
     *
     * @param fid 操作id
     * @return 操作记录实体
     * @see IAdminUserCapitalService#selectVirtualById(int)
     */
    @Override
    public FVirtualCapitalOperation selectVirtualById(int fid) {
        return virtualCapitalOperationMapper.selectAllById(fid);
    }

    /**
     * 获取用户虚拟币钱包
     *
     * @param fuid    用户id
     * @param fcoinid 币种id
     * @return 虚拟币钱包实体
     * @see IAdminUserCapitalService#selectUserVirtualWallet
     */
    @Override
    public UserCoinWallet selectUserVirtualWallet(Long fuid, int fcoinid) {
        return userCoinWalletMapper.selectByUidAndCoin(fuid, fcoinid);
    }

    /**
     * 获取地址数量
     *
     * @param address 地址
     * @return 数量
     * @see IAdminUserCapitalService#selectAddressNum(String)
     */
    @Override
    public int selectAddressNum(String address) {
        return userVirtualAddressMapper.getAddressNum(address);
    }

    /**
     * 更新用户虚拟币操作
     *
     * @param amount     总量
     * @param addressNum 地址数量
     * @param coinDriver 钱包工具
     * @return 是否执行成功
     * @throws BCException 更新异常
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateVirtualCapital(Integer recordId, Long adminId, BigDecimal amount, int addressNum,
                                        CoinDriver coinDriver) throws BCException {

        FVirtualCapitalOperation record = virtualCapitalOperationMapper.selectAllById(recordId);
        if (!record.getFstatus().equals(VirtualCapitalOperationOutStatusEnum.LockOrder)) {
            throw new BCException("订单锁定中，不允许重复操作！");
        }

        Long fuid = record.getFuid();
        int fcoinid = record.getFcoinid();
        UserCoinWallet userVirtualWallet = userCoinWalletMapper.selectByUidAndCoinLock(fuid, fcoinid);
        if (userVirtualWallet == null) {
            throw new BCException("虚拟币钱包为空");
        }
        if (MathUtils.sub(userVirtualWallet.getFrozen(), amount).compareTo(BigDecimal.ZERO) == -1) {
            throw new BCException("虚拟币钱包冻结余额不足");
        }
        // 提现人钱包
        userVirtualWallet.setFrozen(MathUtils.sub(userVirtualWallet.getFrozen(), amount));
        userVirtualWallet.setGmtModified(Utils.getTimestamp());
        // [钱包异地操作]提现地址无用
         String address = record.getFwithdrawaddress();

        record.setFadminid(adminId);
        // [钱包异地操作]修改状态为LockOrder
        record.setFstatus(VirtualCapitalOperationOutStatusEnum.LockOrder);
        // 平台互转
        if (addressNum > 0) {
            UserCoinWallet virtualwalletTo = getUserCoinWallet(record, fcoinid, coinDriver.getCoinSort());
            if (virtualwalletTo == null) {
                throw new BCException("平台互转转入虚拟币钱包为空");
            }
            // 写记录
            FVirtualCapitalOperation tovirtualcaptualoperation = new FVirtualCapitalOperation();
            tovirtualcaptualoperation.setFuid(virtualwalletTo.getUid());
            tovirtualcaptualoperation.setFcoinid(fcoinid);
            tovirtualcaptualoperation.setFamount(record.getFamount());
            tovirtualcaptualoperation.setFfees(BigDecimal.ZERO);
            tovirtualcaptualoperation.setFbtcfees(BigDecimal.ZERO);
            tovirtualcaptualoperation.setFhasowner(true);
            tovirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS);// 收款成功
            tovirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN.getCode());// 收款
            tovirtualcaptualoperation.setFuniquenumber("[平台互转]" + Utils.UUID());
            tovirtualcaptualoperation.setFrechargeaddress(record.getFwithdrawaddress());
            tovirtualcaptualoperation.setFupdatetime(Utils.getTimestamp());
            tovirtualcaptualoperation.setFcreatetime(Utils.getTimestamp());
            tovirtualcaptualoperation.setVersion(0);
            tovirtualcaptualoperation.setFplatform(record.getFplatform());
            if (virtualCapitalOperationMapper.insert(tovirtualcaptualoperation) <= 0) {
                throw new BCException("平台互转记录写入出错");
            }
            // 更新钱包信息
            virtualwalletTo.setTotal(MathUtils.add(virtualwalletTo.getTotal(), record.getFamount()));
            virtualwalletTo.setGmtModified(Utils.getTimestamp());
            if (userCoinWalletMapper.updateByPrimaryKey(virtualwalletTo) <= 0) {
                throw new BCException("平台互转对方钱包被锁定");
            }
            // 更新提现信息
            record.setFuniquenumber("[平台互转]" + Utils.UUID());
            // [钱包异地操作]站内操作直接成功
            record.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationSuccess);
        }
        // [钱包异地操作]通过本地ETH节点发送交易
        else {
            //生成系统头寸钱包操作记录
            FSysCoinOperation sysCoinOperation = FSysCoinOperation.builder()
                    .fadminid(adminId)
                    .famount(amount)
                    .ftoaddress(address)
                    .fcreatetime(Utils.getTimestamp())
                    .fcoinid(coinDriver.getCoinSort())
                    .fstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation)
                    .version(1)
                    .build();
            // 返回TXID
            String resultTX = null;
            Integer nonceTmp = null;
            try {
                if (coinDriver.getCoinSort().equals(SystemCoinSortEnum.ETH.getCode())) {
                    if("ETH".equalsIgnoreCase(record.getFcoinname())){
                        resultTX = coinDriver.sendToAddress(address, record.getFamount().toString());
                    }else {
                        resultTX=((ETHDriver)coinDriver).sendToAddress(address,record.getFamount().toString(),((ETHDriver)coinDriver).getAccount());
                    }
                }
//                else if(coinDriver.getCoinSort().equals(SystemCoinSortEnum.BTS.getCode()) || coinDriver.getCoinSort().equals(SystemCoinSortEnum.GXC.getCode())
//                        ||coinDriver.getCoinSort().equals(SystemCoinSortEnum.TXB.getCode()) || coinDriver.getCoinSort().equals(SystemCoinSortEnum.ETP.getCode())){
//                    FUserVirtualAddressWithdraw addressWithdraw = userVirtualAddressWithdrawMapper.selectByPrimaryKey(record.getFaddressWithdrawId());
//                    resultTX = coinDriver.sendToAddress(address,record.getFamount(), addressWithdraw.getFremark(), record.getFbtcfees());
//                }
                else{
                	// 向地址发送指定数量的币
                    resultTX = coinDriver.sendToAddress(address, record.getFamount(), record.getFid().toString(), record.getFbtcfees());
                }
            } catch (Exception e) {
                logger.error(e.toString());
                e.printStackTrace();
                throw new BCException("打币链接钱包出现错误");
            }//eth返回结果处理
            if(coinDriver.getCoinSort().equals(SystemCoinSortEnum.ETH.getCode())){
                if("ETH".equalsIgnoreCase(record.getFcoinname())) {
                    if (!resultTX.startsWith("0x")){
                        if("password error".equals(resultTX)){
                            throw  new BCException("输入密码错误");
                        }
                        throw new BCException("请求失败！请联系技术人员处理！"+resultTX);
                    }
                }else {
                    if (!resultTX.startsWith("0x")){
                        if("password error".equals(resultTX)){
                            throw  new BCException("输入密码错误");
                        }else if("wallet donnot have enough token".equals(resultTX)){
                            throw new BCException("没有足够的此种代币");
                        }
                        throw new BCException("请求失败！请联系技术人员处理！"+resultTX);
                    }
                }
            }
            if (resultTX == null || "".equals(resultTX)) {
                throw new BCException("钱包连接错误  : " + resultTX + "_" + nonceTmp);
            }
            // 打币记录返回TXID
            record.setFuniquenumber(resultTX);
            record.setFnonce(nonceTmp);
            // 打币记录返回TXID
            sysCoinOperation.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationSuccess);
            sysCoinOperation.setTxid(resultTX);
            sysCoinOperation.setFnonce(nonceTmp);
        }
        // 更新数据
        if (virtualCapitalOperationMapper.updateByPrimaryKey(record) <= 0) {
            throw new BCException("更新订单失败");
        }
        if (userCoinWalletMapper.updateByPrimaryKey(userVirtualWallet) <= 0) {
            throw new BCException("更新用户钱包失败");
        }
        // MQ
        mqSend.SendUserAction(record.getFuid(), LogUserActionEnum.COIN_WITHDRAW,
                record.getFcoinid(), amount, record.getFfees(), record.getFbtcfees());

        // MQ
        mqSend.SendAdminAction(record.getFadminid(), record.getFuid(),
                LogAdminActionEnum.SYSTEM_COIN_WITHDRAW, record.getFcoinid(), record.getFamount());
        return true;
    }

    private UserCoinWallet getUserCoinWallet(FVirtualCapitalOperation record, int fcoinId, int coinType) throws BCException {
        UserCoinWallet userCoinWallet;
        List<FUserVirtualAddress> userVirtualAddresses = userVirtualAddressMapper.getUserByAddress(record.getFwithdrawaddress(), fcoinId);
        if (userVirtualAddresses == null || userVirtualAddresses.size() != 1) {
            throw new BCException("平台互转转入地址为空");
        }
        // 转入地址信息
        FUserVirtualAddress userVirtualAddress = userVirtualAddresses.get(0);
        // 转入钱包信息
        userCoinWallet = userCoinWalletMapper.selectByUidAndCoinLock(userVirtualAddress.getFuid(), fcoinId);
        return userCoinWallet;
    }

    /**
     * 更新虚拟币操作记录
     *
     * @param fAdmin 管理员
     * @param record 记录
     * @return 是否执行成功
     * @throws BCException 更新异常
     * @see IAdminUserCapitalService#updateVirtualCapital
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateVirtualCapital(LoginUser fAdmin, FVirtualCapitalOperation record) throws BCException {
        if (record.getFstatus() == VirtualCapitalOperationOutStatusEnum.Cancel) {
            UserCoinWallet userVirtualWallet = userCoinWalletMapper.selectByUidAndCoinLock(record.getFuid(), record.getFcoinid());
            BigDecimal amountfees = MathUtils.add(record.getFamount(), record.getFfees());
            BigDecimal amount = MathUtils.add(amountfees, record.getFbtcfees());
            BigDecimal frozenRmb = userVirtualWallet.getFrozen();
            if (MathUtils.sub(frozenRmb, amount).compareTo(BigDecimal.ZERO) < 0) {
                logger.error("虚拟币冻结金额低于提现金额，恢复冻结金额");
            }
            amount = frozenRmb;
            userVirtualWallet.setTotal(MathUtils.add(userVirtualWallet.getTotal(), amount));
            userVirtualWallet.setFrozen(MathUtils.sub(userVirtualWallet.getFrozen(), amount));
            userVirtualWallet.setGmtModified(Utils.getTimestamp());
            if (userCoinWalletMapper.updateByPrimaryKey(userVirtualWallet) <= 0) {
                logger.error("userCoinWalletMapper.updateByPrimaryKey record not found.");
                throw new BCException("更新虚拟钱包失败");
            }
        }
        if (virtualCapitalOperationMapper.updateByPrimaryKey(record) <= 0) {
            logger.error("virtualCapitalOperationMapper.updateByPrimaryKey record not found.");
            throw new BCException("更新订单失败");
        }
        // MQ
        if (record.getFstatus() == VirtualCapitalOperationOutStatusEnum.Cancel) {
            mqSend.SendUserAction(record.getFuid(), LogUserActionEnum.COIN_WITHDRAW_CANCEL, record.getFcoinid(), record.getFamount(), "admin_" + fAdmin.getUserId());

            mqSend.SendAdminAction(record.getFadminid(), record.getFuid(), LogAdminActionEnum.CANCEL_COIN_WITHDRAW, record.getFcoinid(), record.getFamount());
        }
        return true;
    }

    /**
     * 新增虚拟币充值记录
     *
     * @param record 充值实体
     * @return 是否新增成功
     * @see IAdminUserCapitalService#insertConsoleVirtualRecharge(FLogConsoleVirtualRecharge)
     */
    @Override
    public boolean insertConsoleVirtualRecharge(FLogConsoleVirtualRecharge record) {
        int i = logConsoleVirtualRechargeMapper.insert(record);
        return i > 0;
    }

    /**
     * 根据id查询虚拟币充值
     *
     * @param fid 充值id
     * @return 虚拟币充值实体
     * @see IAdminUserCapitalService#selectConsoleVirtualRechargeById(int)
     */
    @Override
    public FLogConsoleVirtualRecharge selectConsoleVirtualRechargeById(int fid) {
        return logConsoleVirtualRechargeMapper.selectByPrimaryKey(fid);
    }

    /**
     * 删除虚拟币充值
     *
     * @param fid 充值id
     * @return 是否删除成功
     * @see IAdminUserCapitalService#deleteConsoleVirtualRechargeById(int)
     */
    @Override
    public boolean deleteConsoleVirtualRechargeById(int fid) {
        int i = logConsoleVirtualRechargeMapper.deleteByPrimaryKey(fid);
        return i > 0;
    }

    /**
     * 更新虚拟币充值
     *
     * @param record 虚拟币充值
     * @return 是否更新成功
     * @throws BCException 更新异常
     * @see IAdminUserCapitalService#updateConsoleVirtualRecharge(FLogConsoleVirtualRecharge)
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateConsoleVirtualRecharge(FLogConsoleVirtualRecharge record) throws BCException {
        if (record.getFstatus() == OperationlogEnum.FFROZEN) {
            Long fuid = record.getFuid();
            int fcoinId = record.getFcoinid();
            BigDecimal qty = record.getFamount();
            UserCoinWallet userVirtualWallet = userCoinWalletMapper.selectByUidAndCoinLock(fuid, fcoinId);
            if (userVirtualWallet == null) {
                return false;
            }
            //c2c操作
            if (record.getFinfo().startsWith(ArtificialRechargeTypeEnum.C2CRecharge.getValue()) &&
                    record.getFinfo().split(",").length > 1 &&
                    Integer.valueOf(record.getFinfo().split(",")[1]) == C2COrderTypeEnum.SELL.getCode()) {
            } else {
                userVirtualWallet.setFrozen(MathUtils.add(userVirtualWallet.getFrozen(), qty));
            }
            userVirtualWallet.setGmtModified(Utils.getTimestamp());
            userCoinWalletMapper.updateByPrimaryKey(userVirtualWallet);
        }
        if (record.getFstatus() == OperationlogEnum.AUDIT) {
            Long fuid = record.getFuid();
            int fcoinId = record.getFcoinid();
            BigDecimal qty = record.getFamount();
            UserCoinWallet userVirtualWallet = userCoinWalletMapper.selectByUidAndCoinLock(fuid, fcoinId);
            if (userVirtualWallet == null) {
                return false;
            }
            //c2c操作
            if (record.getFinfo().startsWith(ArtificialRechargeTypeEnum.C2CRecharge.getValue()) &&
                    record.getFinfo().split(",").length > 1 &&
                    Integer.valueOf(record.getFinfo().split(",")[1]) == C2COrderTypeEnum.SELL.getCode()) {
                userVirtualWallet.setFrozen(MathUtils.sub(userVirtualWallet.getFrozen(), MathUtils.negative2Positive(qty)));
            } else {
                userVirtualWallet.setFrozen(MathUtils.sub(userVirtualWallet.getFrozen(), qty));
                userVirtualWallet.setTotal(MathUtils.add(userVirtualWallet.getTotal(), qty));
            }
            userVirtualWallet.setGmtModified(Utils.getTimestamp());
            userCoinWalletMapper.updateByPrimaryKey(userVirtualWallet);
        }
        int result = logConsoleVirtualRechargeMapper.updateByPrimaryKey(record);
        if (result <= 0) {
            throw new BCException();
        }
        mqSend.SendAdminAction(record.getFcreatorid(), record.getFuid(), LogAdminActionEnum.ADMIN_COIN_RECHARGE, record.getFcoinid(), record.getFamount());
        return true;
    }

    /**
     * 分页查询虚拟币充值
     *
     * @param pageParam 分页参数
     * @param record    实体参数
     * @param status    状态列表
     * @return 分页查询记录列表
     * @see IAdminUserCapitalService#selectConsoleVirtualRechargeList(Pagination,
     * FLogConsoleVirtualRecharge, List)
     */
    @Override
    public Pagination<FLogConsoleVirtualRecharge> selectConsoleVirtualRechargeList(Pagination<FLogConsoleVirtualRecharge> pageParam, FLogConsoleVirtualRecharge record, List<Integer> status) {
        // 组装查询条件数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageParam.getOffset());
        map.put("limit", pageParam.getPageSize());
        map.put("orderField", pageParam.getOrderField());
        map.put("orderDirection", pageParam.getOrderDirection());
        map.put("keyword", pageParam.getKeyword());
        map.put("fstatus", status.size() > 0 ? status : null);
        map.put("ftype", record.getFtype());

        map.put("start", pageParam.getBegindate());
        map.put("end", pageParam.getEnddate());
        map.put("fcoinid", record.getFcoinid());
        // 查询总数
        int count = logConsoleVirtualRechargeMapper.countAdminPage(map);
        if (count > 0) {
            // 查询数据
            List<FLogConsoleVirtualRecharge> list = logConsoleVirtualRechargeMapper.getAdminPageList(map);
            // 设置返回数据
            pageParam.setData(list);
        }
        pageParam.setTotalRows(count);
        pageParam.generate();
        return pageParam;
    }

    @Override
    public List<FLogConsoleVirtualRecharge> selectConsoleVirtualRechargeListByParam(FLogConsoleVirtualRecharge recharge) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ftype", recharge.getFtype());
        map.put("fuid", recharge.getFuid());
        map.put("famount", recharge.getFamount());
        map.put("finfo", recharge.getFinfo());
        map.put("start", recharge.getFcreatetime());
        map.put("end", recharge.getFcreatetime());
        List<FLogConsoleVirtualRecharge> list = logConsoleVirtualRechargeMapper.selectConsoleVirtualRechargeListByParam(map);
        return list;
    }

    /**
     * 分页查询用户钱包
     *
     * @param pageParam 分页参数
     * @param fuids     用户ids
     * @return 分页查询记录列表
     * @see IAdminUserCapitalService#selectUserWalletList(Pagination,
     * List)
     */
    @Override
    public Pagination<UserCoinWallet> selectUserWalletList(Pagination<UserCoinWallet> pageParam, List<Integer> fuids) {
        // 组装查询条件数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageParam.getOffset());
        map.put("limit", pageParam.getPageSize());
        map.put("orderField", pageParam.getOrderField());
        map.put("orderDirection", pageParam.getOrderDirection());
        map.put("keyword", pageParam.getKeyword());
        map.put("fuids", fuids);
        // 查询总数
        int count = userCoinWalletMapper.countAdminPage(map);
        if (count > 0) {
            // 查询数据
            List<UserCoinWallet> list = userCoinWalletMapper.getAdminPageList(map);
            // 设置返回数据
            pageParam.setData(list);
        }
        pageParam.setTotalRows(count);
        return pageParam;
    }

    /**
     * 分页查询用户钱包
     *
     * @param pageParam 分页参数
     * @return 分页查询记录列表
     * @see IAdminUserCapitalService#selectUserWalletList(Pagination)
     */
    @Override
    public Pagination<UserCoinWallet> selectUserWalletList(Pagination<UserCoinWallet> pageParam) {
        // 组装查询条件数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageParam.getOffset());
        map.put("limit", pageParam.getPageSize());
        map.put("orderField", pageParam.getOrderField());
        map.put("orderDirection", pageParam.getOrderDirection());
        // 查询总数
        int count = userCoinWalletMapper.countAdminPage(map);
        if (count > 0) {
            // 查询数据
            List<UserCoinWallet> list = userCoinWalletMapper.getAdminPageList(map);
            // 设置返回数据
            pageParam.setData(list);
        }
        pageParam.setTotalRows(count);
        return pageParam;
    }

    /**
     * 分页查询虚拟币钱包
     *
     * @param pageParam   分页参数
     * @param filterParam 实体参数
     * @param fuids       用户ids
     * @return 分页查询记录列表
     * @see IAdminUserCapitalService#selectUserVirtualWalletList(Pagination,
     * UserCoinWallet, List)
     */
    @Override
    public Pagination<UserCoinWallet> selectUserVirtualWalletList(Pagination<UserCoinWallet> pageParam, UserCoinWallet filterParam, List<Integer> fuids) {
        // 组装查询条件数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageParam.getOffset());
        map.put("limit", pageParam.getPageSize());
        map.put("orderField", pageParam.getOrderField());
        map.put("orderDirection", pageParam.getOrderDirection());
        map.put("keyword", pageParam.getKeyword());
        map.put("coinId", filterParam.getCoinId());
        map.put("fuids", fuids);
        // 查询总数
        int count = userCoinWalletMapper.countAdminPage(map);
        if (count > 0) {
            // 查询数据
            List<UserCoinWallet> list = userCoinWalletMapper.getAdminPageList(map);
            // 设置返回数据
            pageParam.setData(list);
        }
        pageParam.setTotalRows(count);
        return pageParam;
    }


    /**
     * 分页查询虚拟币钱包
     *
     * @param pageParam   分页参数
     * @param filterParam 实体参数
     * @return 分页查询记录列表
     * @see IAdminUserCapitalService#selectUserVirtualWalletListByCoin(Pagination,
     * UserCoinWallet)
     */
    @Override
    public Pagination<UserCoinWallet> selectUserVirtualWalletListByCoin(Pagination<UserCoinWallet> pageParam, UserCoinWallet filterParam) {
        // 组装查询条件数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", pageParam.getOffset());
        map.put("limit", pageParam.getPageSize());
        map.put("orderField", pageParam.getOrderField());
        map.put("orderDirection", pageParam.getOrderDirection());
        map.put("coinId", filterParam.getCoinId());


        // 查询总数
        int count = userCoinWalletMapper.countAdminPage(map);
        if (count > 0) {
            // 查询数据
            List<UserCoinWallet> list = userCoinWalletMapper.getAdminPageList(map);
            // 设置返回数据
            pageParam.setData(list);
        }
        pageParam.setTotalRows(count);
        return pageParam;
    }

    /**
     * 根据类型查询人民币操作总金额
     *
     * @param fuid  用户id
     * @param type  类型
     * @param start 开始时间
     * @param end   结束时间
     * @return 总金额
     * @see IAdminUserCapitalService#selectWalletTotalAmount
     */
    @Override
    public BigDecimal selectWalletTotalAmount(Long fuid, Integer type, Date start, Date end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fuid", fuid);
        map.put("type", type);
        map.put("start", start);
        map.put("end", end);

        BigDecimal total = walletCapitalOperationMapper.getTotalAmountByType(map);

        return total;
    }

    /**
     * 根据类型查询虚拟币操作总金额
     *
     * @param fuid   用户id
     * @param coinid 币种id
     * @param type   类型
     * @param start  开始时间
     * @param end    结束时间
     * @return 总金额
     * @see IAdminUserCapitalService#selectVirtualWalletTotalAmount
     */
    @Override
    public BigDecimal selectVirtualWalletTotalAmount(Long fuid, Integer coinid, Integer type, Date start, Date end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fuid", fuid);
        map.put("coinid", coinid);
        map.put("type", type);
        map.put("start", start);
        map.put("end", end);

        BigDecimal total = virtualCapitalOperationMapper.getTotalAmountByType(map);

        return total;
    }

    /**
     * 根据类型查询虚拟币操作总金额
     *
     * @param fuid   用户id
     * @param coinid 币种id
     * @param type   类型
     * @return 总金额
     * @see IAdminUserCapitalService#selectVirtualWalletTotalAmount
     */
    @Override
    public BigDecimal selectVirtualWalletTotalAmount(Long fuid, Integer coinid, Integer type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fuid", fuid);
        map.put("coinid", coinid);
        map.put("type", type);

        BigDecimal total = virtualCapitalOperationMapper.getTotalAmountByType(map);

        return total;
    }


    /**
     * 根据类型查询手工充值虚拟币操作总金额
     *
     * @param fuid   用户id
     * @param coinid 币种id
     * @param status 状态
     * @param start  开始时间
     * @param end    结束时间
     * @return 总金额
     * @see IAdminUserCapitalService#selectAdminRechargeVirtualWalletTotalAmount
     */
    @Override
    public BigDecimal selectAdminRechargeVirtualWalletTotalAmount(Long fuid, Integer coinid, Integer status, Date start, Date end) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fuid", fuid);
        map.put("coinid", coinid);
        map.put("status", status);
        map.put("start", start);
        map.put("end", end);

        BigDecimal total = logConsoleVirtualRechargeMapper.getTotalAmountByStatus(map);

        return total;
    }

    /**
     * 根据类型查询手工充值虚拟币操作总金额
     *
     * @param fuid   用户id
     * @param coinid 币种id
     * @param status 状态
     * @return 总金额
     * @see IAdminUserCapitalService#selectAdminRechargeVirtualWalletTotalAmount
     */
    @Override
    public BigDecimal selectAdminRechargeVirtualWalletTotalAmount(Long fuid, Integer coinid, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fuid", fuid);
        map.put("coinid", coinid);
        map.put("status", status);

        BigDecimal total = logConsoleVirtualRechargeMapper.getTotalAmountByStatus(map);

        return total;
    }
}
