package io.uhha.coin.capital.mapper;

import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 虚拟币充值提现记录 数据操作接口
 * @author ZKF
 */
@Mapper
public interface FVirtualCapitalOperationMapper {

    /**
     * 根据id查询记录(包括扩展字段)
     *
     * @param fid 操作id
     * @return 操作实体
     */
    FVirtualCapitalOperation selectAllById(@Param("fid") int fid);
	
	/**
	 * 新增记录
	 * @param record 实体对象
	 * @return 成功条数
	 */
    int insert(FVirtualCapitalOperation record);

    /**
     * 根据id查询记录
     * @param fid 主键ID
     * @return 实体对象
     */
    FVirtualCapitalOperation selectByPrimaryKey(Integer fid);

    /**
     * 更新记录
     * @param record 实体对象
     * @return 成功条数
     */
    int updateByPrimaryKey(FVirtualCapitalOperation record);
    
    /**
     * 分页查询记录
     * @param map 查询条件MAP
     * @return 实体对象列表
     */
    List<FVirtualCapitalOperation> getPageVirtualCapitalOperation(Map<String, Object> map);
    
    /**
     * 分页查询记录总条数
     * @param map 查询条件MAP
     * @return 记录总数
     */
    int countVirtualCapitalOperation(Map<String, Object> map);

    /**
     * 根据用户查询提现次数
     * @param fuid 用户ID
     * @return 提现次数
     */
	int getVirtualWalletWithdrawTimes(Long fuid);

    /**
     * 根据交易id查询记录
     * @param funiquenumber 交易ID
     * @return 实体对象
     */
    FVirtualCapitalOperation selectOneByTxid(@Param("funiquenumber") String funiquenumber);

    /**
     * 根据交易id查询记录
     * @param funiquenumber 交易ID
     * @return 实体对象
     */
    List<FVirtualCapitalOperation> selectMoreByTxid(@Param("funiquenumber") String funiquenumber);

    /**
     * 根据类型查询操作记录总数量
     *
     * @param map 参数map
     * @return 总数量
     */
    BigDecimal getTotalAmountByType(Map<String, Object> map);

    /**
     * 分页查询数据总条数
     *
     * @param map 参数map
     * @return 查询记录数
     */
    int countAdminPage(Map<String, Object> map);

    /**
     * 分页查询数据
     *
     * @param map 参数map
     * @return 操作列表
     */
    List<FVirtualCapitalOperation> getAdminPageList(Map<String, Object> map);

    /**
     * 查找未到账
     * @param fcoinid
     * @return
     */
    List<FVirtualCapitalOperation> seletcGoing(@Param("fcoinid") int fcoinid, @Param("fconfirmations") int fconfirmations);
}