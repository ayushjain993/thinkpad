package io.uhha.coin.user.service;

import io.uhha.coin.log.domain.FLogModifyCapitalOperation;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.capital.domain.FTokenCapitalOperation;
import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.capital.domain.FWalletCapitalOperation;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.capital.domain.FLogConsoleVirtualRecharge;
import io.uhha.common.exception.BCException;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.common.core.domain.model.LoginUser;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 后台用户资金管理
 *
 * @author LY
 */
public interface IAdminUserCapitalService {

    /**
     * 分页查询钱包操作记录
     *
     * @param pageParam 分页参数
     * @param record    实体参数
     * @param status    状态列表
     * @return 分页查询记录列表
     */
    Pagination<FWalletCapitalOperation> selectWalletCapitalOperationList(Pagination<FWalletCapitalOperation> pageParam, FWalletCapitalOperation record, List<Integer> status);

    /**
     * 分页查询淘币钱包操作记录
     *
     * @param pageParam 分页参数
     * @param record    实体参数
     * @param status    状态列表
     * @return 分页查询记录列表
     * @author ZGY
     */
    Pagination<FTokenCapitalOperation> selectTokenCapitalOperationList(Pagination<FTokenCapitalOperation> pageParam, FTokenCapitalOperation record, List<Integer> status);

    /**
     * 根据id查询钱包操作记录
     *
     * @param fid 操作id
     * @return 钱包操作实体
     */
    FWalletCapitalOperation selectById(int fid);

    /**
     * 根据id查询淘币操作记录
     *
     * @param fid 操作id
     * @return 淘币操作实体
     */
    FTokenCapitalOperation selectTokenById(Integer fid);


    boolean updateWalletCapital(LoginUser admin, FWalletCapitalOperation capital, BigDecimal amount, boolean isRecharge) throws Exception;

    /**
     * 更新钱包操作
     *
     * @param admin      管理员
     * @param capital    钱包操作记录
     * @param amount     总量
     * @return 是否执行成功
     * @throws BCException 更新异常
     */
    boolean updateTokenCapital(LoginUser admin, FTokenCapitalOperation capital, BigDecimal amount) throws Exception;


    /**
     * 获取用户钱包
     *
     * @param fuid 用户id
     * @return 用户钱包实体
     */
    UserCoinWallet selectUserWallet(Long fuid, int fcoinid);






    /**
     * 更新钱包操作记录
     *
     * @param capital 操作记录
     * @return 是否执行成功
     * @throws BCException 更新异常
     */
    boolean updateWalletCapital(FWalletCapitalOperation capital) throws BCException;
    /**
     * 更新淘币操作记录
     *
     * @param capital 操作记录
     * @return 是否执行成功
     * @throws BCException 更新异常
     */
    boolean updateTokenCapital(FTokenCapitalOperation capital) throws Exception;




    /**
     * 是否第一次充值
     *
     * @param fuid 用户id
     * @return 是否第一次充值
     */
    boolean selectIsFirstCharge(Long fuid);

    /**
     * 更新充值记录
     *
     * @param capital    钱包操作记录
     * @param capitalLog 修改日志
     * @return 是否修改成功
     * @throws BCException 更新失败
     */
    boolean updateModifyCapital(FWalletCapitalOperation capital, FLogModifyCapitalOperation capitalLog) throws BCException;

    /**
     * 分页查询虚拟币操作记录
     *
     * @param pageParam 分页参数
     * @param record    实体参数
     * @param status    状态列表
     * @return 分页查询记录列表
     */
    Pagination<FVirtualCapitalOperation> selectVirtualCapitalOperationList(Pagination<FVirtualCapitalOperation> pageParam, FVirtualCapitalOperation record, List<Integer> status);

    /**
     * 根据id查询虚拟币操作记录
     *
     * @param fid 操作id
     * @return 操作记录实体
     */
    FVirtualCapitalOperation selectVirtualById(int fid);

    /**
     * 获取用户虚拟币钱包
     *
     * @param fuid    用户id
     * @param fcoinid 币种id
     * @return 虚拟币钱包实体
     */
    UserCoinWallet selectUserVirtualWallet(Long fuid, int fcoinid);

    /**
     * 获取地址数量
     *
     * @param address 地址
     * @return 数量
     */
    int selectAddressNum(String address);

    /**
     * 更新用户虚拟币操作
     *
     * @param recordId   虚拟币操作
     * @param amount     总量
     * @param addressNum 地址数量
     * @param coinDriver 钱包工具
     * @return 是否执行成功
     * @throws BCException 更新异常
     */
    boolean updateVirtualCapital(Integer recordId, Long adminId, BigDecimal amount, int addressNum,
                                 CoinDriver coinDriver) throws BCException;

    /**
     * 更新虚拟币操作记录
     *
     * @param fAdmin 管理员
     * @param record 记录
     * @return 是否执行成功
     * @throws BCException 更新异常
     */
    boolean updateVirtualCapital(LoginUser fAdmin, FVirtualCapitalOperation record) throws BCException;

    /**
     * 新增虚拟币充值记录
     *
     * @param record 充值实体
     * @return 是否新增成功
     */
    boolean insertConsoleVirtualRecharge(FLogConsoleVirtualRecharge record);

    /**
     * 根据id查询虚拟币充值
     *
     * @param fid 充值id
     * @return 虚拟币充值实体
     */
    FLogConsoleVirtualRecharge selectConsoleVirtualRechargeById(int fid);

    /**
     * 删除虚拟币充值
     *
     * @param fid 充值id
     * @return 是否删除成功
     */
    boolean deleteConsoleVirtualRechargeById(int fid);

    /**
     * 更新虚拟币充值
     *
     * @param record 虚拟币充值
     * @return 是否更新成功
     * @throws BCException 更新异常
     */
    boolean updateConsoleVirtualRecharge(FLogConsoleVirtualRecharge record) throws BCException;

    /**
     * 分页查询虚拟币充值
     *
     * @param pageParam 分页参数
     * @param record    实体参数
     * @param status    状态列表
     * @return 分页查询记录列表
     */
    Pagination<FLogConsoleVirtualRecharge> selectConsoleVirtualRechargeList(Pagination<FLogConsoleVirtualRecharge> pageParam, FLogConsoleVirtualRecharge record, List<Integer> status);

    /**
     * create by hf
     * 根据条件查询充值记录
     * @return
     */
    List<FLogConsoleVirtualRecharge> selectConsoleVirtualRechargeListByParam(FLogConsoleVirtualRecharge recharge);
    /**
     * 分页查询用户钱包
     *
     * @param pageParam 分页参数
     * @param fuids     用户ids
     * @return 分页查询记录列表
     */
    Pagination<UserCoinWallet> selectUserWalletList(Pagination<UserCoinWallet> pageParam, List<Integer> fuids);

    /**
     * 分页查询用户钱包
     *
     * @param pageParam 分页参数
     * @return 分页查询记录列表
     */
    Pagination<UserCoinWallet> selectUserWalletList(Pagination<UserCoinWallet> pageParam);

    /**
     * 分页查询虚拟币钱包
     *
     * @param pageParam   分页参数
     * @param filterParam 实体参数
     * @param fuids       用户ids
     * @return 分页查询记录列表
     */
    Pagination<UserCoinWallet> selectUserVirtualWalletList(Pagination<UserCoinWallet> pageParam, UserCoinWallet filterParam, List<Integer> fuids);

    /**
     * 分页查询虚拟币钱包
     *
     * @param pageParam   分页参数
     * @param filterParam 实体参数
     * @return 分页查询记录列表
     */
    Pagination<UserCoinWallet> selectUserVirtualWalletListByCoin(Pagination<UserCoinWallet> pageParam, UserCoinWallet filterParam);

    /**
     * 根据类型查询虚拟币操作总金额
     *
     * @param fuid   用户id
     * @param coinid 币种id
     * @param type   类型
     * @param start  开始时间
     * @param end    结束时间
     * @return 总金额
     */
    BigDecimal selectVirtualWalletTotalAmount(Long fuid, Integer coinid, Integer type, Date start, Date end);

    /**
     * 根据类型查询虚拟币操作总金额
     *
     * @param fuid   用户id
     * @param coinid 币种id
     * @param type   类型
     * @return 总金额
     */
    BigDecimal selectVirtualWalletTotalAmount(Long fuid, Integer coinid, Integer type);

    /**
     * 根据类型查询人民币操作总金额
     *
     * @param fuid  用户id
     * @param type  类型
     * @param start 开始时间
     * @param end   结束时间
     * @return 总金额
     */
    BigDecimal selectWalletTotalAmount(Long fuid, Integer type, Date start, Date end);

    /**
     * 根据类型查询手工充值虚拟币操作总金额
     *
     * @param fuid   用户id
     * @param coinid 币种id
     * @param status 状态
     * @param start  开始时间
     * @param end    结束时间
     * @return 总金额
     */
    BigDecimal selectAdminRechargeVirtualWalletTotalAmount(Long fuid, Integer coinid, Integer status, Date start, Date end);

    /**
     * 根据类型查询手工充值虚拟币操作总金额
     *
     * @param fuid   用户id
     * @param coinid 币种id
     * @param status 状态
     * @return 总金额
     */
    BigDecimal selectAdminRechargeVirtualWalletTotalAmount(Long fuid, Integer coinid, Integer status);
}
