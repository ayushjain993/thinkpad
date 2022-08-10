package io.uhha.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.order.domain.OmsLogisticsCompany;
import io.uhha.order.vo.OmsLogisticsCompanyVo;

import java.util.List;

/**
 * 物流公司Service接口
 *
 * @author mj
 * @date 2020-07-24
 */
public interface IOmsLogisticsCompanyService extends IService<OmsLogisticsCompany> {
    /**
     * 查询物流公司
     *
     * @param id 物流公司ID
     * @return 物流公司
     */
    public OmsLogisticsCompany selectOmsLogisticsCompanyById(Long id);

    List<OmsLogisticsCompany> queryStoreUseCompanys(long storeId);
    /**
     * 查询物流公司（包含店铺是否使用这个物流公司）
     *
     * @param storeId 店铺id
     * @return 返回物流公司（包含店铺是否使用这个物流公司）
     */
    List<OmsLogisticsCompany> queryLogisticsCompanysWithUse(long storeId);

    /**
     * 改变店铺物流的使用
     *
     * @param storeId    店铺id
     * @param companyId  物流公司id
     * @param actionType 操作类型 0 不使用1  使用
     * @return 成功返回1 失败返回0 -1 超过20个
     */
    int changeLogisticsCompanyUse(long storeId, long companyId, int actionType);

    /**
     * 查询物流公司列表
     *
     * @param omsLogisticsCompany 物流公司
     * @return 物流公司集合
     */
    public List<OmsLogisticsCompany> selectOmsLogisticsCompanyList(OmsLogisticsCompany omsLogisticsCompany);

    /**
     * 查询物流公司列表
     *
     * @param code 代码
     * @return 物流公司集合
     */
    public List<OmsLogisticsCompany> selectOmsLogisticsCompanyListByCode(String code);

    /**
     * 根据店铺id查询其使用的物流公司列表
     *
     * @param storeId 使用店铺id
     * @return 物流公司集合
     */
    public List<OmsLogisticsCompany> selectOmsLogisticsCompanyListByStoreId(Long storeId);

    /**
     * 根据店铺id查询其使用的物流公司列表
     *
     * @param storeId 使用店铺id
     * @return 物流公司集合
     */
    public List<OmsLogisticsCompanyVo> selectOmsLogisticsCompanyForStore(Long storeId);


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
     * 批量删除物流公司
     *
     * @param ids 需要删除的物流公司ID
     * @return 结果
     */
    public int deleteOmsLogisticsCompanyByIds(Long[] ids);

    /**
     * 删除物流公司信息
     *
     * @param id 物流公司ID
     * @return 结果
     */
    public int deleteOmsLogisticsCompanyById(Long id);
}
