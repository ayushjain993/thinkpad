package io.uhha.setting.bean;

import io.uhha.coin.common.match.MathUtils;
import io.uhha.setting.domain.LsPlatformFeeSetting;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设置默认普通店铺平台服务费率，默认付费店铺平台服务费率；默认各支付渠道提现服务费率等；默认用户推荐费用费率（一级和二级），默认带货推广费用费率，默认NFT带货推广费用费率，默认推广费用分成，默认NFT推广费用分成等；
 */
@Data
@ToString
public class PlatformFeeSet {
    /**
     * 默认普通店铺平台服务费率
     */
    private BigDecimal defaultBasicStoreServiceRate;

    /**
     * 默认付费店铺平台服务费率
     */
    private BigDecimal defaultPremiumStoreServiceRate;

    /**
     * 默认支付渠道提现服务费率等
     */
    private BigDecimal defaultWithdrawCommissionRate;

    /**
     * 默认带货推广费用费率
     */
    private BigDecimal defaultDropshipRate;

    /**
     * 默认推广费用分成（平台）
     */
    private BigDecimal defaultDropshipPlatformPart;

    /**
     * 默认推广费用分成（商铺）
     */
    private BigDecimal defaultDropshipMerchantPart;

    /**
     * 默认NFT带货推广费用费率
     */
    private BigDecimal defaultNftDropshipRate;

    /**
     * 默认NFT推广费用分成（平台）
     */
    private BigDecimal defaultNftDropshipPlatformPart;

    /**
     * 默认NFT推广费用分成（商铺）
     */
    private BigDecimal defaultNftDropshipMerchantPart;

    /**
     * 用于组装支付设置对象在前端显示
     *
     * @return 支付设置对象
     */
    public static PlatformFeeSet getPlatformFeeSet(PlatformFeeSet platformFeeSet, List<LsPlatformFeeSetting> platformFeeSettings) {
        platformFeeSettings.forEach(setting->{
            if("DEFAULT_BASIC_STORE_SERVICE_RATE".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultBasicStoreServiceRate(new BigDecimal(setting.getColumnValue()));
            }
            if("DEFAULT_PREMIUM_STORE_SERVICE_RATE".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultPremiumStoreServiceRate(new BigDecimal(setting.getColumnValue()));
            }
            if("DEFAULT_WITHDRAW_COMMISSION_RATE".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultWithdrawCommissionRate(new BigDecimal(setting.getColumnValue()));
            }
            if("DEFAULT_DROPSHIP_RATE".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultDropshipRate(new BigDecimal(setting.getColumnValue()));
            }
            if("DEFAULT_DROPSHIP_PLATFORM_PART".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultDropshipPlatformPart(new BigDecimal(setting.getColumnValue()));
            }
            if("DEFAULT_DROPSHIP_MERCHANT_PART".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultDropshipMerchantPart(new BigDecimal(setting.getColumnValue()));
            }
            if("DEFAULT_NFT_DROPSHIP_RATE".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultNftDropshipRate(new BigDecimal(setting.getColumnValue()));
            }
            if("DEFAULT_NFT_DROPSHIP_PLATFORM_PART".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultNftDropshipPlatformPart(new BigDecimal(setting.getColumnValue()));
            }
            if("DEFAULT_NFT_DROPSHIP_MERCHANT_PART".equalsIgnoreCase(setting.getColumnName())){
                platformFeeSet.setDefaultNftDropshipMerchantPart(new BigDecimal(setting.getColumnValue()));
            }
        });
        return platformFeeSet;
    }

    public boolean isValidDropshipSetting(){

        int ret = BigDecimal.ONE.compareTo(MathUtils.add(defaultDropshipPlatformPart, defaultDropshipMerchantPart));
        return defaultDropshipRate !=null
                && !defaultDropshipRate.equals(BigDecimal.ZERO)
                && (ret == 0);
    }

    public boolean isValidNFTDropshipSetting(){
        int ret = BigDecimal.ONE.compareTo(MathUtils.add(defaultNftDropshipPlatformPart, defaultNftDropshipMerchantPart));
        return defaultNftDropshipRate !=null
                && !defaultNftDropshipRate.equals(BigDecimal.ZERO)
                && (ret == 0);
    }
}
