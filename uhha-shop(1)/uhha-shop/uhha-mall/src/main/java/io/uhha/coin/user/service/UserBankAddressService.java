package io.uhha.coin.user.service;

import java.util.List;

import io.uhha.coin.user.domain.FUserBankinfo;
import io.uhha.coin.user.domain.FUserVirtualAddress;
import io.uhha.coin.user.domain.FUserVirtualAddressWithdraw;
import io.uhha.common.exception.BCException;

/**
 * 用户银行卡、地址接口
 *
 * @author ZKF
 */
public interface UserBankAddressService {

	/**
	 * 添加用户提现银行卡
	 *
	 * @param bankInfo 用户银行卡实体对象
	 * @param ip       IP地址
	 * @return true：成功，false：失败
	 * @throws BCException 执行失败
	 */
	boolean insertBankInfo(FUserBankinfo bankInfo, String ip) throws BCException;

	/**
	 * 根据用户ID和银行卡号查询银行卡信息是否存在
	 *
	 * @param bankInfo 用户银行卡实体对象
	 * @return true：成功，false：失败
	 * @throws BCException 执行失败
	 */
	boolean selectIsExistBankInfo(FUserBankinfo bankInfo) throws BCException;

	/**
	 * 更新提现银行卡信息
	 *
	 * @param record 用户银行卡实体对象
	 * @return true：成功，false：失败
	 * @throws BCException 执行失败
	 */
	boolean updateBankInfo(FUserBankinfo record) throws BCException;

	/**
	 * 获取银行卡
	 *
	 * @param id 用户银行卡主键ID
	 * @return 用户银行卡实体对象
	 */
	FUserBankinfo selectUserBankInfoById(int id);

	/**
	 * 查询用户提现银行卡
	 *
	 * @param fuid  用户ID
	 * @param ftype 类型
	 * @return 用户银行卡实体对象列表
	 */
	List<FUserBankinfo> selectUserBankInfolist(Long fuid, Integer ftype);

	/**
	 * 查询用户提现银行卡
	 *
	 * @param fBankinfo 用户银行卡实体对象
	 * @return 用户银行卡实体对象列表
	 */
	List<FUserBankinfo> selectUserBankInfolist(FUserBankinfo fBankinfo);

	/**
	 * 添加虚拟币提现地址
	 *
	 * @param address 虚拟币提现地址实体对象
	 * @param ip      IP地址
	 * @return true：成功，false：失败
	 * @throws BCException 执行异常
	 */
	boolean insertVirtualCoinWithdrawAddress(FUserVirtualAddressWithdraw address, String ip) throws BCException;


	/**
	 * 添加ETH类虚拟币充值地址
	 *
	 * @param fuid    用户id
	 * @param fcoinid 虚拟币id
	 * @param ip      IP地址
	 * @param ethid   eth币种id
	 * @return 虚拟币充值地址实体对象
	 * @throws BCException 执行失败
	 */
	FUserVirtualAddress insertETHVirtualCoinAddress(Long fuid, Integer fcoinid, String ip, Integer ethid) throws BCException;

	/**
	 * 添加LET类虚拟币充值地址
	 *
	 * @param fuid    用户id
	 * @param fcoinid 虚拟币id
	 * @param ip      IP地址
	 * @param faddr   eth币种id
	 * @return 虚拟币充值地址实体对象
	 * @throws BCException 执行失败
	 */
	FUserVirtualAddress insertLETVirtualCoinAddress(Long fuid, Integer fcoinid, String ip, Integer faddr) throws BCException;

	/**
	 * 获取虚拟币提现地址
	 *
	 * @param address 虚拟币提现地址实体对象
	 * @return 虚拟币提现地址实体对象列表
	 */
	List<FUserVirtualAddressWithdraw> selectVirtualCoinWithdrawAddressList(FUserVirtualAddressWithdraw address);

	/**
	 * 获取虚拟币提现地址
	 *
	 * @param id 虚拟币提现地址主键ID
	 * @return 虚拟币提现地址实体对象
	 */
	FUserVirtualAddressWithdraw selectVirtualCoinWithdrawAddressById(int id);

	/**
	 * 更新虚拟币提现地址
	 *
	 * @param address 虚拟币提现地址实体对象
	 * @return true：成功，false：失败
	 */
	boolean updateVirtualCoinWithdrawAddress(FUserVirtualAddressWithdraw address);

	/**
	 * 添加虚拟币充值地址
	 *
	 * @param fuid    用户id
	 * @param fcoinid 虚拟币id
	 * @param ip      IP地址
	 * @return 虚拟币充值地址实体对象
	 * @throws BCException 执行失败
	 */
	FUserVirtualAddress insertVirtualCoinAddress(Long fuid, Integer fcoinid, String ip) throws BCException;

	/**
	 * 根据用户和币种查询充值地址
	 *
	 * @param fuid    用户ID
	 * @param fcoinid 虚拟币ID
	 * @return 虚拟币充值地址实体对象
	 */
	FUserVirtualAddress selectVirtualAddressByUserAndCoin(Long fuid, Integer fcoinid);

	/**
	 * 根据用户和币种查询充值地址
	 *
	 * @param fuid 用户ID
	 * @return 虚拟币充值地址实体对象列表
	 */
	List<FUserVirtualAddress> selectVirtualAddressByUser(Long fuid);

	/**
	 * 根据用户和币名查询ETH类的钱包地址
	 *
	 * @param fuid    用户id
	 * @param fcoinid 虚拟币id
	 * @return ETH类虚拟充值地址集合
	 */
	List<FUserVirtualAddress> selectETHsVirtualAddressByUserAndCoin(Long fuid, Integer fcoinid);
}
