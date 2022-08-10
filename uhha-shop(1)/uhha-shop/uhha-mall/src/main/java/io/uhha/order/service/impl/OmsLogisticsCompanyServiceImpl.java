package io.uhha.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.IteratorUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.order.domain.OmsLogisticsCompany;
import io.uhha.order.domain.OmsLogisticsCompanyUse;
import io.uhha.order.mapper.OmsLogisticsCompanyMapper;
import io.uhha.order.mapper.OmsLogisticsCompanyUseMapper;
import io.uhha.order.service.IOmsLogisticsCompanyService;
import io.uhha.order.vo.OmsLogisticsCompanyVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 物流公司Service业务层处理
 *
 * @author mj
 * @date 2020-07-24
 */
@Service
public class OmsLogisticsCompanyServiceImpl extends ServiceImpl<OmsLogisticsCompanyMapper, OmsLogisticsCompany> implements IOmsLogisticsCompanyService {
    @Autowired
    private OmsLogisticsCompanyMapper omsLogisticsCompanyMapper;
    /**
     * 调试日志
     */
    private Logger logger = LoggerFactory.getLogger(OmsLogisticsCompanyServiceImpl.class);

    /**
     * 注入店铺物流公司使用数据库接口
     */
    @Autowired
    private OmsLogisticsCompanyUseMapper logisticsCompanyUseMapper;

    @Autowired
    private RedisMallHelper redisMallHelper;

    @Override
    public List<OmsLogisticsCompany> queryStoreUseCompanys(long storeId) {
        logger.debug("queryStoreUseCompanys and storeId:{}");
        return omsLogisticsCompanyMapper.queryStoreUseCompanys(storeId);
    }
    /**
     * 查询物流公司
     *
     * @param id 物流公司ID
     * @return 物流公司
     */
    @Override
    public OmsLogisticsCompany selectOmsLogisticsCompanyById(Long id) {
        return omsLogisticsCompanyMapper.selectOmsLogisticsCompanyById(id);
    }
    @Override
    public List<OmsLogisticsCompany> queryLogisticsCompanysWithUse(long storeId) {

        List<OmsLogisticsCompany> logisticsCompanies = this.selectOmsLogisticsCompanyList(null);

        if (CollectionUtils.isEmpty(logisticsCompanies)) {
            logger.info("there is no LogisticsCompany.....");
            return logisticsCompanies;
        }

        // 查询店铺使用的物流公司

        List<OmsLogisticsCompanyUse> logisticsCompanyUses = logisticsCompanyUseMapper.queryLogisticsCompanyUses(storeId);

        // 如果店铺没有使用物流公司 则直接返回
        if (CollectionUtils.isEmpty(logisticsCompanyUses)) {
            logger.info("store no use LogisticsCompany");
            return logisticsCompanies;
        }

        // 设置店铺使用的物流公司
        IteratorUtils.zip(logisticsCompanies, logisticsCompanyUses,
                (logisticsCompany, logisticsCompanyUse) -> logisticsCompany.getId() == logisticsCompanyUse.getCompanyId(),
                (logisticsCompany1, logisticsCompanyUse1) -> logisticsCompany1.setStoreUseCompany());

        return logisticsCompanies;
    }

    @Override
    public int changeLogisticsCompanyUse(long storeId, long companyId, int actionType) {
        logger.debug("changeLogisticsCompanyUse and storeId:{} \r\n companyId:{} \r\n actionType:{}", storeId, companyId, actionType);

        Map<String, Object> params = new HashMap<>();
        params.put("storeId", storeId);
        params.put("companyId", companyId);
        // 使用
        if (actionType == 1) {
            if (logisticsCompanyUseMapper.queryLogisticsCompanyUseCount(storeId) >= 20) {
                logger.error("changeLogisticsCompanyUse fail due to store use company >=20");
                return -1;
            }
            logisticsCompanyUseMapper.addLogisticsCompanyUse(params);
        } else {
            // 不使用
            logisticsCompanyUseMapper.deleteLogisticsCompanyUse(params);
        }

        return 1;
    }

    /**
     * 查询物流公司列表
     *
     * @param omsLogisticsCompany 物流公司
     * @return 物流公司
     */
    @Override
    public List<OmsLogisticsCompany> selectOmsLogisticsCompanyList(OmsLogisticsCompany omsLogisticsCompany) {
        return omsLogisticsCompanyMapper.selectOmsLogisticsCompanyList(omsLogisticsCompany);
    }

    @Override
    public List<OmsLogisticsCompany> selectOmsLogisticsCompanyListByCode(String code) {
        if(StringUtils.isEmpty(code)){
            return new ArrayList<>();
        }

        List<OmsLogisticsCompany> list = redisMallHelper.getLogisticCompanies();

        List<OmsLogisticsCompany> filtered = list.stream().filter(new Predicate<OmsLogisticsCompany>() {
            @Override
            public boolean test(OmsLogisticsCompany omsLogisticsCompany) {
                return omsLogisticsCompany.getCode().startsWith(code);
            }
        }).collect(Collectors.toList());

        return filtered;
    }

    /**
     * 根据店铺id查询其使用的物流公司列表
     * @param storeId 使用店铺id
     * @return
     */
    @Override
    public List<OmsLogisticsCompany> selectOmsLogisticsCompanyListByStoreId(Long storeId) {
        return omsLogisticsCompanyMapper.queryStoreUseCompanys(storeId);
    }

    /**
     * 根据店铺id查询其使用的物流公司列表
     * @param storeId 使用店铺id
     * @return
     */
    @Override
    public List<OmsLogisticsCompanyVo> selectOmsLogisticsCompanyForStore(Long storeId) {
        return omsLogisticsCompanyMapper.selectOmsLogisticsCompanyForStore(storeId);
    }

    /**
     * 新增物流公司
     *
     * @param omsLogisticsCompany 物流公司
     * @return 结果
     */
    @Override
    public int insertOmsLogisticsCompany(OmsLogisticsCompany omsLogisticsCompany) {
        omsLogisticsCompany.setCreateTime(DateUtils.getNowDate());
        return omsLogisticsCompanyMapper.insertOmsLogisticsCompany(omsLogisticsCompany);
    }

    /**
     * 修改物流公司
     *
     * @param omsLogisticsCompany 物流公司
     * @return 结果
     */
    @Override
    public int updateOmsLogisticsCompany(OmsLogisticsCompany omsLogisticsCompany) {
        return omsLogisticsCompanyMapper.updateOmsLogisticsCompany(omsLogisticsCompany);
    }

    /**
     * 批量删除物流公司
     *
     * @param ids 需要删除的物流公司ID
     * @return 结果
     */
    @Override
    public int deleteOmsLogisticsCompanyByIds(Long[] ids) {
        return omsLogisticsCompanyMapper.deleteOmsLogisticsCompanyByIds(ids);
    }

    /**
     * 删除物流公司信息
     *
     * @param id 物流公司ID
     * @return 结果
     */
    @Override
    public int deleteOmsLogisticsCompanyById(Long id) {
        return omsLogisticsCompanyMapper.deleteOmsLogisticsCompanyById(id);
    }
}
