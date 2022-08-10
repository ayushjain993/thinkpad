package io.uhha.coin.user.service;

import io.uhha.coin.capital.domain.FTokenCapitalOperation;
import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.capital.domain.FWalletCapitalOperation;
import io.uhha.coin.common.Enum.DataSourceEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.common.exception.BCException;
import io.uhha.coin.user.domain.FUserVirtualAddressWithdraw;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.member.domain.UmsMember;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户资产计算接口
 * @author ZKF
 */
public interface UserCapitalService {

	/**
	 * 判断是否首次充值
	 * @param fuid 用户ID
	 * @return true：是，false：否
	 */
	public boolean selectIsFirstCharge(Long fuid);

	/**
	 * 获取用户虚拟币钱包
	 * @param fuid 用户IDID
	 * @param fcoinid 虚拟币ID
	 * @return 虚拟币钱包实体对象
	 */
	UserCoinWallet getCoinWalletByUidAndId(Long fuid, Integer fcoinid);

	/**
	 * 获取用户虚拟币钱包
	 * @param fuid 用户ID
	 * @return 虚拟币钱包实体对象列表
	 */
	List<UserCoinWallet> listVirtualWalletById(Long fuid);

	/**
	 * 根据参数查询符合条件的用户钱包
	 * @param
	 * @return
	 */
	Pagination<UserCoinWallet> listVirtualWalletByParam(Pagination<UserCoinWallet> captal,UserCoinWallet userCoinWallet);

	/**
	 * 用户每日钱包提现次数
	 * @param fuid 用户ID
	 * @return 人民币每日提现次数
	 */
	public int selectWalletWithdrawTimes(Long fuid);

	/**
	 * 用户每日虚拟币提现次数
	 * @param fuid 用户ID
	 * @return 虚拟币每日提现次数
	 */
	public int selectVirtualWalletWithdrawTimes(Long fuid);

	/**
	 * 根据id查询虚拟币提现记录	 
	 * @param id 虚拟币充提记录表主键ID
	 * @return 虚拟币充提记录表实体对象
	 */
	public FVirtualCapitalOperation selectVirtualCapitalOperationById(int id);

	/**
	 * 查询虚拟币 充值记录
	 * @param param 分页实体对象
	 * @param operation 虚拟币充提记录表实体对象
	 * @return 分页实体对象
	 */
	public Pagination<FVirtualCapitalOperation> selectVirtualCapitalOperation(Pagination<FVirtualCapitalOperation> param, FVirtualCapitalOperation operation);

	/**
	 * 更新虚拟币提现撤单
	 * @param user 用户实体
	 * @param operation 虚拟币充提记录表实体对象
	 * @return true：成功，false：失败
	 * @throws BCException 更新失败
	 */
	public boolean updateVirtualCancelOperationWithdraw(UmsMember user, FVirtualCapitalOperation operation) throws BCException;



	/**
	 * 更新淘币提现撤单
	 * @param fuid 用户ID
	 * @param operation 虚拟币充提记录表实体对象
	 * @return true：成功，false：失败
	 * @throws BCException 更新失败
	 */


	public boolean updateTokenCancelOperationWithdraw(Long fuid, FTokenCapitalOperation operation, String ip) throws BCException;




	/**
	 * 根据id查询人民币 充值记录
	 * @param fid 人民币充提记录表实体对象主键ID
	 * @return 人民币充提记录表实体对象
	 */
	public FWalletCapitalOperation selectWalletCapitalOperationById(int fid);

	public FTokenCapitalOperation selectTokenCapitalOperationById(int fid);

	/**
	 * 查询人民币 充值记录
	 * @param param 分页实体对象
	 * @param operation 人民币充提记录表实体对象
	 * @return 分页实体对象
	 */
	public Pagination<FWalletCapitalOperation> selectWalletCapitalOperation(Pagination<FWalletCapitalOperation> param, FWalletCapitalOperation operation);

	public Pagination<FTokenCapitalOperation> selectTokenCapitalOperation(Pagination<FTokenCapitalOperation> param, FTokenCapitalOperation operation);

	/**
	 * 新增虚拟币充值或提现记录
	 * @param user 用户实体对象
	 * @param coinid 虚拟币ID
	 * @param addressWithdraw 提现地址
	 * @param vwalletId 虚拟币钱包ID
	 * @param withdrawAmount 提现金额
	 * @param BTCFees 网络手续费
	 * @return true：成功，false：失败
	 * @throws BCException 更新失败
	 */
	boolean insertVirtualCapitalOperation(UmsMember user, int coinid, FUserVirtualAddressWithdraw addressWithdraw, int vwalletId
			, BigDecimal withdrawAmount, BigDecimal BTCFees, DataSourceEnum sourceEnum, PlatformEnum platformEnum) throws BCException;

	/**
	 * 更新钱包
	 * @param userCoinWallet
	 * @return
	 * @throws BCException
	 */
	boolean updateUserCoinWallet(UserCoinWallet userCoinWallet) throws Exception;

}
