package io.uhha.settlement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.order.domain.OmsBillingRecords;
import io.uhha.settlement.domain.InternalTrans;

import java.util.List;

/**
 * 转移记录Service接口
 *
 * @author uhha
 * @date 2021-12-31
 */
public interface IInternalTransService extends IService<InternalTrans> {
    /**
     * 查询转移记录
     *
     * @param id 转移记录主键
     * @return 转移记录
     */
    public InternalTrans selectInternalTransById(Long id);

    /**
     * 查询转移记录列表
     *
     * @param internalTrans 转移记录
     * @return 转移记录集合
     */
    public List<InternalTrans> selectInternalTransList(InternalTrans internalTrans);

    /**
     * 新增转移记录
     *
     * @param internalTrans 转移记录
     * @return 结果
     */
    public int insertInternalTrans(InternalTrans internalTrans);

    /**
     * 修改转移记录
     *
     * @param internalTrans 转移记录
     * @return 结果
     */
    public int updateInternalTrans(InternalTrans internalTrans);

    /**
     * 批量删除转移记录
     *
     * @param ids 需要删除的转移记录主键集合
     * @return 结果
     */
    public int deleteInternalTransByIds(Long[] ids);

    /**
     * 删除转移记录信息
     *
     * @param id 转移记录主键
     * @return 结果
     */
    public int deleteInternalTransById(Long id);

    /**
     * 转移结算
     *
     * @param internalTrans
     */
    void settlement(InternalTrans internalTrans, OmsBillingRecords obs );
}
