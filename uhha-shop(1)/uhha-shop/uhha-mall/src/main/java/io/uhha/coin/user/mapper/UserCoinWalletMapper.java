package io.uhha.coin.user.mapper;



import io.uhha.coin.user.domain.UserCoinWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 用户钱包数据操作接口
 * @author ZKF
 */
@Mapper
public interface UserCoinWalletMapper {
	
	/**
	 * 插入数据
	 * @param record 实体对象
	 * @return 成功条数
	 */
    int insert(UserCoinWallet record);

    /**
     * 更新
     * @param record 实体对象
     * @return 成功条数
     */
    int updateByPrimaryKey(UserCoinWallet record);

    /**
     * @author zgy
     * 更新Frozen属性
     * @param record 实体对象
     * @return 成功条数
     */
    int updateFrozenByUidAndCoinId(UserCoinWallet record);

    /**
     * 获取用户所有钱包
     * @param uid 用户ID
     * @return 实体对象列表
     */
    List<UserCoinWallet> selectByUid(@Param("uid") Long uid);

	/**
	 * 根据参数查询用户符合条件的钱包
	 * @param map
	 * @return
	 */
	List<UserCoinWallet> selectByParam(Map<String,Object> map);

	/**
	 * 统计符合条件的用户钱包总数
	 * @param map
	 * @return
	 */
	Integer countByParam(Map<String,Object> map);
    /**
     * 修改时查询(无锁)
     * @param uid 用户ID
     * @param coinId 币种ID
     * @return 实体对象
     */
    UserCoinWallet selectByUidAndCoin(@Param("uid") Long uid, @Param("coinId") Integer coinId);
    
    /**
     * 修改时查询(带行级锁)
     * @param uid 用户ID
     * @param coinId 币种ID
     * @return 实体对象
     */
    UserCoinWallet selectByUidAndCoinLock(@Param("uid") Long uid, @Param("coinId") Integer coinId);

	/**
	 * 查询总量
	 *
	 * @param coinid 币种
	 * @return 总量列表
	 */
	Map<String, Object> selectSum(@Param("coinid") Integer coinid);

	/*********** 控台部分 *************/
	/**
	 * 分页查询数据
	 *
	 * @param map 参数map
	 * @return 查询记录列表
	 */
	List<UserCoinWallet> getAdminPageList(Map<String, Object> map);

	/**
	 * 分页查询数据总条数
	 *
	 * @param map 参数map
	 * @return 查询记录数
	 */
	int countAdminPage(Map<String, Object> map);

}
