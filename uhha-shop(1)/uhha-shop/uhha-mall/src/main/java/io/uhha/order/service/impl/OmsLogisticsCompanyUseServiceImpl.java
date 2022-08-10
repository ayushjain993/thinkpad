package io.uhha.order.service.impl;

import io.uhha.order.domain.OmsLogisticsCompanyUse;
import io.uhha.order.mapper.OmsLogisticsCompanyUseMapper;
import io.uhha.order.service.IOmsLogisticsCompanyUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺使用的物流公司Service业务层处理
 *
 * @author mj
 * @date 2020-07-24
 */
@Service
public class OmsLogisticsCompanyUseServiceImpl implements IOmsLogisticsCompanyUseService {
    @Autowired
    private OmsLogisticsCompanyUseMapper omsLogisticsCompanyUseMapper;

    /**
     * 查询店铺使用的物流公司
     *
     * @param id 店铺使用的物流公司ID
     * @return 店铺使用的物流公司
     */
    @Override
    public OmsLogisticsCompanyUse selectOmsLogisticsCompanyUseById(Long id) {
        return omsLogisticsCompanyUseMapper.selectOmsLogisticsCompanyUseById(id);
    }

    /**
     * 查询店铺使用的物流公司列表
     *
     * @param omsLogisticsCompanyUse 店铺使用的物流公司
     * @return 店铺使用的物流公司
     */
    @Override
    public List<OmsLogisticsCompanyUse> selectOmsLogisticsCompanyUseList(OmsLogisticsCompanyUse omsLogisticsCompanyUse) {
        return omsLogisticsCompanyUseMapper.selectOmsLogisticsCompanyUseList(omsLogisticsCompanyUse);
    }

    /**
     * 新增店铺使用的物流公司
     *
     * @param omsLogisticsCompanyUse 店铺使用的物流公司
     * @return 结果
     */
    @Override
    public int insertOmsLogisticsCompanyUse(OmsLogisticsCompanyUse omsLogisticsCompanyUse) {
        return omsLogisticsCompanyUseMapper.insertOmsLogisticsCompanyUse(omsLogisticsCompanyUse);
    }

    /**
     * 修改店铺使用的物流公司
     *
     * @param omsLogisticsCompanyUse 店铺使用的物流公司
     * @return 结果
     */
    @Override
    public int updateOmsLogisticsCompanyUse(OmsLogisticsCompanyUse omsLogisticsCompanyUse) {
        return omsLogisticsCompanyUseMapper.updateOmsLogisticsCompanyUse(omsLogisticsCompanyUse);
    }

    /**
     * 批量删除店铺使用的物流公司
     *
     * @param ids 需要删除的店铺使用的物流公司ID
     * @return 结果
     */
    @Override
    public int deleteOmsLogisticsCompanyUseByIds(Long[] ids) {
        return omsLogisticsCompanyUseMapper.deleteOmsLogisticsCompanyUseByIds(ids);
    }

    /**
     * 删除店铺使用的物流公司信息
     *
     * @param id 店铺使用的物流公司ID
     * @return 结果
     */
    @Override
    public int deleteOmsLogisticsCompanyUseById(Long id) {
        return omsLogisticsCompanyUseMapper.deleteOmsLogisticsCompanyUseById(id);
    }

    @Override
    public int deleteOmsLogisticsCompanyUse(Long storeId, Long companyId) {
        Map<String, Object> param = new HashMap<>();
        param.put("companyId", companyId);
        param.put("storeId", storeId);
        return omsLogisticsCompanyUseMapper.deleteLogisticsCompanyUse(param);
    }
}
