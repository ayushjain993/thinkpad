package io.uhha.order.mapper;

import io.uhha.order.domain.OmsBillingRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 账单记录Mapper接口
 *
 * @author mj
 * @date 2020-07-24
 */
@Mapper
public interface OmsBillingRecordsMapper {
    /**
     * 查询账单记录
     *
     * @param id 账单记录ID
     * @return 账单记录
     */
    public OmsBillingRecords selectOmsBillingRecordsById(Long id);

    /**
     * 查询账单记录列表
     *
     * @param omsBillingRecords 账单记录
     * @return 账单记录集合
     */
    public List<OmsBillingRecords> selectOmsBillingRecordsList(OmsBillingRecords omsBillingRecords);

    /**
     * 新增账单记录
     *
     * @param omsBillingRecords 账单记录
     * @return 结果
     */
    public int insertOmsBillingRecords(OmsBillingRecords omsBillingRecords);

    /**
     * 修改账单记录
     *
     * @param omsBillingRecords 账单记录
     * @return 结果
     */
    public int updateOmsBillingRecords(OmsBillingRecords omsBillingRecords);

    /**
     * 删除账单记录
     *
     * @param id 账单记录ID
     * @return 结果
     */
    public int deleteOmsBillingRecordsById(Long id);

    /**
     * 批量删除账单记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOmsBillingRecordsByIds(Long[] ids);

    List<OmsBillingRecords> selectOmsBillingRecordsListByStatusAndCreateTime(@Param("status") String status, @Param("createTime") Date createTime);
}
