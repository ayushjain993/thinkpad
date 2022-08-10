package io.uhha.setting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.setting.bean.PlatformFeeSet;
import io.uhha.setting.domain.LsPlatformFeeSetting;
import io.uhha.setting.mapper.LsPlatformFeeSettingMapper;
import io.uhha.setting.service.ILsPlatformFeeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台费用设置Service业务层处理
 * 
 * @author peter
 * @date 2022-01-26
 */
@Service
public class LsPlatformFeeSettingServiceImpl extends ServiceImpl<LsPlatformFeeSettingMapper, LsPlatformFeeSetting> implements ILsPlatformFeeSettingService
{
    @Autowired
    private LsPlatformFeeSettingMapper lsPlatformFeeSettingMapper;

    /**
     * 查询平台费率配置设置
     *
     * @return 返回PqueryPlatformFeeSet
     */
    @Override
    public PlatformFeeSet queryPlatformFeeSet() {
        log.debug("queryPlatformFeeSet...");
        return PlatformFeeSet.getPlatformFeeSet(new PlatformFeeSet(), lsPlatformFeeSettingMapper.queryPlatformFeeSet());
    }

    /**
     * 修改平台费率配置设置
     *
     * @return 返回PqueryPlatformFeeSet
     */
    @Override
    public int editPlatformFeeSet(PlatformFeeSet platformFeeSet) {
        List<LsPlatformFeeSetting> list = new ArrayList<>();
        log.debug("editPlatformFeeSet...");
        lsPlatformFeeSettingMapper.deletePlatformFeeSet();

        list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultBasicStoreServiceRate() ==null? "0":platformFeeSet.getDefaultBasicStoreServiceRate().toString(), "DEFAULT_BASIC_STORE_SERVICE_RATE"));
        list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultPremiumStoreServiceRate()==null? "0":platformFeeSet.getDefaultPremiumStoreServiceRate().toString(), "DEFAULT_PREMIUM_STORE_SERVICE_RATE"));
        list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultWithdrawCommissionRate()==null? "0":platformFeeSet.getDefaultWithdrawCommissionRate().toString(), "DEFAULT_WITHDRAW_COMMISSION_RATE"));
         list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultDropshipRate()==null? "0":platformFeeSet.getDefaultDropshipRate().toString(), "DEFAULT_DROPSHIP_RATE"));
        list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultDropshipPlatformPart()==null? "0":platformFeeSet.getDefaultDropshipPlatformPart().toString(), "DEFAULT_DROPSHIP_PLATFORM_PART"));
        list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultDropshipMerchantPart()==null? "0":platformFeeSet.getDefaultDropshipMerchantPart().toString(), "DEFAULT_DROPSHIP_MERCHANT_PART"));
        list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultNftDropshipRate()==null? "0":platformFeeSet.getDefaultNftDropshipRate().toString(), "DEFAULT_NFT_DROPSHIP_RATE"));
        list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultNftDropshipPlatformPart()==null? "0":platformFeeSet.getDefaultNftDropshipPlatformPart().toString(), "DEFAULT_NFT_DROPSHIP_PLATFORM_PART"));
        list.add(LsPlatformFeeSetting.getPlatformFeeSetting(new LsPlatformFeeSetting(), platformFeeSet.getDefaultNftDropshipMerchantPart()==null? "0":platformFeeSet.getDefaultNftDropshipMerchantPart().toString(), "DEFAULT_NFT_DROPSHIP_MERCHANT_PART"));


        return lsPlatformFeeSettingMapper.addPlatformFeeSet(list);
    }
}
