package io.uhha.coin.user.mapper;

import io.uhha.coin.user.domain.FUserVirtualAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户虚拟币地址数据操作接口
 * @author ZKF
 */
@Mapper
public interface FUserVirtualAddressMapper {

	/**
	 * 根据用户和币种查询提现地址 
	 * @param fuid 用户ID
	 * @param fcoinid 币种ID
	 * @return 实体对象
	 */
	FUserVirtualAddress selectByUserAndCoin(@Param("fuid") Long fuid, @Param("fcoinid")Integer fcoinid);
	
	/**
	 * 根据地址和币种查询提现地址
	 * @param fcoinid 币种ID
	 * @param fadderess 地址
	 * @return 实体对象
	 */
	FUserVirtualAddress selectByCoinAndAddress(@Param("fcoinid")Integer fcoinid, @Param("fadderess") String fadderess);
	
	/**
	 * 根据用户查询用户的所有地址
	 * @param fuid 用户ID
	 * @return 实体对象列表
	 */
    List<FUserVirtualAddress> selectByUser(Long fuid);
    
    /**
     * 新增用户的所有地址
     * @param record 实体对象
     * @return 成功条数
     */
    int insert(FUserVirtualAddress record);

	/**
	 * 根据用户id和以太坊类的币种id查出充值地址
	 * @param fuid
	 * @param fcoinid
	 * @return
	 */
	List<FUserVirtualAddress>  selectETHsByUserAndCoin(@Param("fuid") Long fuid, @Param("fcoinid")Integer fcoinid);

	/**
	 * 根据用户id和LET的币种id查出充值地址
	 * @param fuid
	 * @param fcoinid
	 * @return
	 */
	List<FUserVirtualAddress>  selectLETsByUserAndCoin(@Param("fuid") Long fuid, @Param("fcoinid")Integer fcoinid);

	/**
	 * 检测提现地址是否平台地址
	 *
	 * @param address 地址
	 * @return 查询记录数
	 */
	int getAddressNum(@Param("address") String address);

	/**
	 * 根据充值地址查找用户
	 *
	 * @param address 地址
	 * @return 地址列表
	 */
	List<FUserVirtualAddress> getUserByAddress(@Param("address") String address,@Param("fcoinId") Integer fcoinId);

	List<FUserVirtualAddress> listUserEthAddresses(Integer fcoinid);

}