package io.uhha.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.order.domain.OmsLogisticsCompany;
import io.uhha.order.vo.OmsLogisticsCompanyVo;

import java.util.List;

/**
 * 物流公司Mapper接口
 *
 * @author mj
 * @date 2020-07-24
 */
public interface OmsLogisticsCompanyMapper extends BaseMapper<OmsLogisticsCompany> {
    /**
     * 查询物流公司
     *
     * @param id 物流公司ID
     * @return 物流公司
     */
    public OmsLogisticsCompany selectOmsLogisticsCompanyById(Long id);

    /**
     * 查询物流公司列表
     *
     * @param omsLogisticsCompany 物流公司
     * @return 物流公司集合
     */
    public List<OmsLogisticsCompany> selectOmsLogisticsCompanyList(OmsLogisticsCompany omsLogisticsCompany);

    /**
     * 根据店铺id查询其使用的物流公司列表
     * @param storeId
     * @return
     */
    List<OmsLogisticsCompanyVo> selectOmsLogisticsCompanyForStore(long storeId);

    /**
     * 根据店铺id查询其使用的物流公司列表
     * @param storeId
     * @return
     */
    List<OmsLogisticsCompany> queryStoreUseCompanys(long storeId);

    /**
     * 新增物流公司
     *
     * @param omsLogisticsCompany 物流公司
     * @return 结果
     */
    public int insertOmsLogisticsCompany(OmsLogisticsCompany omsLogisticsCompany);

    /**
     * 修改物流公司
     *
     * @param omsLogisticsCompany 物流公司
     * @return 结果
     */
    public int updateOmsLogisticsCompany(OmsLogisticsCompany omsLogisticsCompany);

    /**
     * 删除物流公司
     *
     * @param id 物流公司ID
     * @return 结果
     */
    public int deleteOmsLogisticsCompanyById(Long id);

    /**
     * 批量删除物流公司
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOmsLogisticsCompanyByIds(Long[] ids);
}
