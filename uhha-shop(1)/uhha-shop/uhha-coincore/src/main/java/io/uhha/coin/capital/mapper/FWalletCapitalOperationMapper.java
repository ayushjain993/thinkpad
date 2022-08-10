package io.uhha.coin.capital.mapper;


import io.uhha.coin.capital.domain.FWalletCapitalOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 钱包充值提现记录数据操作接口
 * @author ZKF
 */
@Mapper
public interface FWalletCapitalOperationMapper {

    /**
     * 新增记录
     * @param record 实体对象
     * @return 成功条数
     */
    int insert(FWalletCapitalOperation record);

    /**
     * 根据id查询记录
     * @param fid 主键ID
     * @return 实体对象
     */
    FWalletCapitalOperation selectByPrimaryKey(Integer fid);

    /**
     * 更新记录
     * @param record 实体对象
     * @return 成功条数
     */
    int updateByPrimaryKey(FWalletCapitalOperation record);
    
    /**
     * 分页查询记录
     * @param map 查询条件MAP
     * @return 实体对象列表
     */
    List<FWalletCapitalOperation> getPageWalletCapitalOperation(Map<String,Object> map);
    
    /**
     * 分页查询记录的记录数
     * @param map 查询条件MAP
     * @return 记录总数
     */
    int countWalletCapitalOperation(Map<String,Object> map);

    /**
     * 根据用户查询人民币提现次数
     * @param fuid 用户ID
     * @return 提现次数
     */
	int getWalletWithdrawTimes(Long fuid);
	
	/**
	 * 查询用户今日提现金额
	 * @param fuid 用户ID
	 * @return 今日提现金额
	 */
    BigDecimal getDayWithdrawCny(@Param("fuid") Long fuid);

    /*********** 控台部分 *************/
    /**
     * 分页查询数据
     *
     * @param map 参数map
     * @return 操作列表
     */
    List<FWalletCapitalOperation> getAdminPageList(Map<String, Object> map);

    /**
     * 分页查询数据总条数
     *
     * @param map 参数map
     * @return 查询记录数
     */
    int countAdminPage(Map<String, Object> map);

    /**
     * 根据类型查询操作记录总金额
     *
     * @param map 参数map
     * @return 总金额
     */
    BigDecimal getTotalAmountByType(Map<String, Object> map);
}