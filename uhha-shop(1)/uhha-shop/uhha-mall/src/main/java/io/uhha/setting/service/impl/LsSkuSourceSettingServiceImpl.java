package io.uhha.setting.service.impl;

import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.bean.OssSetting;
import io.uhha.setting.bean.SkuSourceCommon;
import io.uhha.setting.bean.SkuSourceSetting;
import io.uhha.setting.domain.LsOssSetting;
import io.uhha.setting.domain.LsSkuSourceSetting;
import io.uhha.setting.mapper.LsOssSettingMapper;
import io.uhha.setting.mapper.LsSkuSourceSettingMapper;
import io.uhha.setting.service.ILsOssSettingService;
import io.uhha.setting.service.ILsSkuSourceSettingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sku来源Service业务层处理
 *
 * @author mj
 * @date 2020-07-28
 */
@Service
@Slf4j
public class LsSkuSourceSettingServiceImpl implements ILsSkuSourceSettingService {
    /**
     * Oss类型列表
     */
    private final static List<String> codeTypeList = Arrays.asList("1");

    @Autowired
    private LsSkuSourceSettingMapper lsSkuSourceSettingMapper;

    /**
     * 编辑Sku来源接口设置
     *
     * @param skuSourceCommon 实体类参数
     * @param codeType        Sku来源： 1 onebound
     * @return -1编辑出错 >=1成功
     */
    @Override
    @Transactional
    public int editSkuSourceSet(SkuSourceCommon skuSourceCommon, String codeType) {
        List<SkuSourceSetting> list = new ArrayList<>();
        if (codeTypeList.indexOf(codeType) < 0) {
            log.error("editSkuSourceSet error codeType is illegal");
            return -1;
        }
        //先删后增
        lsSkuSourceSettingMapper.deleteSkuSettingSet(codeType);
        if ("1".equals(codeType)) {
            log.debug("editSkuSourceSet oneBound...");
            list.add(SkuSourceSetting.getSkuSourceSet(new SkuSourceSetting(), codeType, skuSourceCommon.getOneBoundSourceSet().getAccessKey(), "accessKey"));
            list.add(SkuSourceSetting.getSkuSourceSet(new SkuSourceSetting(), codeType, skuSourceCommon.getOneBoundSourceSet().getApiSecret(), "apiSecret"));
            list.add(SkuSourceSetting.getSkuSourceSet(new SkuSourceSetting(), codeType, skuSourceCommon.getOneBoundSourceSet().getIsUse(), "isUse"));
        } else {
            log.debug("Aws Oss setting not done");
            return 0;
        }
        return lsSkuSourceSettingMapper.addSkuSettingSet(list);
    }

    @Override
    public SkuSourceCommon querySkuSourceSet() {
        return SkuSourceCommon.getSkuSourceSetCommon(new SkuSourceCommon(), lsSkuSourceSettingMapper.querySkuSettingSet());
    }

    @Override
    public LsSkuSourceSetting selectLsSkuSourceSettingById(Long id) {
        return lsSkuSourceSettingMapper.selectLsSkuSourceSettingById(id);
    }

    @Override
    public List<LsSkuSourceSetting> selectLsSkuSourceSettingList(LsSkuSourceSetting skuSourceSetting) {
        return lsSkuSourceSettingMapper.selectLsSkuSourceSettingList(skuSourceSetting);
    }

    @Override
    public int insertLsSkuSourceSetting(LsSkuSourceSetting lsSkuSourceSetting) {
        return lsSkuSourceSettingMapper.insertLsSkuSourceSetting(lsSkuSourceSetting);
    }

    @Override
    public int updateLsSkuSourceSetting(LsSkuSourceSetting skuSourceSetting) {
        return lsSkuSourceSettingMapper.updateLsSkuSourceSetting(skuSourceSetting);
    }

    @Override
    public int deleteLsSkuSourceSettingByIds(Long[] ids) {
        return lsSkuSourceSettingMapper.deleteLsSkuSourceSettingByIds(ids);
    }

    @Override
    public int deleteLsSkuSourceSettingById(Long id) {
        return lsSkuSourceSettingMapper.deleteLsSkuSourceSettingById(id);
    }
}
