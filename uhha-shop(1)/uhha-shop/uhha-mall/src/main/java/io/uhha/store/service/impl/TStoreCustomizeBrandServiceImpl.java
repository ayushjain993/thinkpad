package io.uhha.store.service.impl;

import io.uhha.common.enums.AuditStatusEnum;
import io.uhha.common.utils.DateUtils;
import io.uhha.goods.domain.PmsBrandApply;
import io.uhha.order.domain.OmsOrder;
import io.uhha.setting.service.BaseInfoSetService;
import io.uhha.store.domain.TStoreCustomizeBrand;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.mapper.TStoreCustomizeBrandMapper;
import io.uhha.store.service.ITStoreCustomizeBrandService;
import io.uhha.store.service.ITStoreInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 店铺自定义品牌列Service业务层处理
 *
 * @author mj
 * @date 2020-07-28
 */
@Service
public class TStoreCustomizeBrandServiceImpl implements ITStoreCustomizeBrandService {
    private static final Logger logger = LoggerFactory.getLogger(TStoreCustomizeBrandServiceImpl.class);
    @Autowired
    private TStoreCustomizeBrandMapper tStoreCustomizeBrandMapper;

    @Autowired
    private ITStoreInfoService storeInfoService;

    @Autowired
    private BaseInfoSetService baseInfoSetService;

    /**
     * 查询店铺自定义品牌列
     *
     * @param id 店铺自定义品牌列ID
     * @return 店铺自定义品牌列
     */
    @Override
    public TStoreCustomizeBrand selectTStoreCustomizeBrandById(Long id) {
        return tStoreCustomizeBrandMapper.selectTStoreCustomizeBrandById(id);
    }

    /**
     * 查询店铺自定义品牌列列表
     *
     * @param tStoreCustomizeBrand 店铺自定义品牌列
     * @return 店铺自定义品牌列
     */
    @Override
    public List<TStoreCustomizeBrand> selectTStoreCustomizeBrandList(TStoreCustomizeBrand tStoreCustomizeBrand) {
        List<TStoreCustomizeBrand> brands = tStoreCustomizeBrandMapper.selectTStoreCustomizeBrandList(tStoreCustomizeBrand);
        brands.forEach(this::setBrandStoreName);
        return brands;
    }

    /**
     * 新增店铺自定义品牌列
     *
     * @param tStoreCustomizeBrand 店铺自定义品牌列
     * @return 结果
     */
    @Override
    public int insertTStoreCustomizeBrand(TStoreCustomizeBrand tStoreCustomizeBrand) {
        //需要审批则状态为待审批，否则为审批通过
        boolean needsScreen = baseInfoSetService.isSkuNeedAudit();
        if(needsScreen){
            tStoreCustomizeBrand.setStatus(AuditStatusEnum.PASS.getCode());
        }else{
            tStoreCustomizeBrand.setStatus(AuditStatusEnum.UNDER_AUDIT.getCode());
        }

        tStoreCustomizeBrand.setCreateTime(DateUtils.getNowDate());
        return tStoreCustomizeBrandMapper.insertTStoreCustomizeBrand(tStoreCustomizeBrand);
    }

    /**
     * 修改店铺自定义品牌列
     *
     * @param tStoreCustomizeBrand 店铺自定义品牌列
     * @return 结果
     */
    @Override
    public int updateTStoreCustomizeBrand(TStoreCustomizeBrand tStoreCustomizeBrand) {
        //需要审批则状态为待审批，否则为审批通过
        boolean needsScreen = baseInfoSetService.isSkuNeedAudit();
        if(needsScreen){
            tStoreCustomizeBrand.setStatus(AuditStatusEnum.PASS.getCode());
        }else{
            tStoreCustomizeBrand.setStatus(AuditStatusEnum.UNDER_AUDIT.getCode());
        }
        return tStoreCustomizeBrandMapper.updateTStoreCustomizeBrand(tStoreCustomizeBrand);
    }

    /**
     * 批量删除店铺自定义品牌列
     *
     * @param ids 需要删除的店铺自定义品牌列ID
     * @return 结果
     */
    @Override
    public int deleteTStoreCustomizeBrandByIds(Long[] ids) {
        return tStoreCustomizeBrandMapper.deleteTStoreCustomizeBrandByIds(ids);
    }

    /**
     * 删除店铺自定义品牌列信息
     *
     * @param id 店铺自定义品牌列ID
     * @return 结果
     */
    @Override
    public int deleteTStoreCustomizeBrandById(Long id) {
        return tStoreCustomizeBrandMapper.deleteTStoreCustomizeBrandById(id);
    }

    @Override
    public TStoreCustomizeBrand queryBrandByNameAndStoreId(String name, long storeId) {
        logger.debug("queryBrandByNameAndStoreId \r\n brandname {} \r\n storeId:{}", name, storeId);
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("storeId", storeId);
        return tStoreCustomizeBrandMapper.queryBrandByNameAndStoreId(params);
    }

    @Override
    public int passBrandAudit(long id) {
        return tStoreCustomizeBrandMapper.passBrandAudit(id);
    }

    @Override
    public int batchPassBrandAudit(Long[] ids) {
        return tStoreCustomizeBrandMapper.batchPassBrandAudit(ids);
    }

    @Override
    public int refuseBrandAudit(TStoreCustomizeBrand brandApply) {
        return tStoreCustomizeBrandMapper.refuseBrandAudit(brandApply);
    }

    @Override
    public int batchRefuseBrandAudit(Long[] ids, String reason) {
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        params.put("reason", reason);
        return tStoreCustomizeBrandMapper.batchRefuseBrandAudit(params);
    }

    /**
     * 设置订单店铺的名称
     *
     * @return 返回订单信息
     */
    private TStoreCustomizeBrand setBrandStoreName(TStoreCustomizeBrand brand) {
        logger.debug("begin to setBrandStoreName....");
        TStoreInfo storeInfo = storeInfoService.queryStoreInfo(brand.getStoreId());
        if (Objects.isNull(storeInfo)) {
            logger.error("setOrderStoreName fail due to store is not exist....");
            return brand;
        }

        brand.setStoreName(storeInfo.getStoreName());
        return brand;
    }
}
