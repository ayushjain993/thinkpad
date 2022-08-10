package io.uhha.setting.bean;

import java.util.List;

public class SkuSourceCommon {
    private OneBoundSourceSet oneBoundSourceSet = new OneBoundSourceSet();

    /**
     * 用于组装Oss设置对象在前端显示
     *
     * @param skuSourceCommon oss设置对象
     * @param skuSourceSettings      数据库映射对象
     * @return oss设置对象
     */
    public static SkuSourceCommon getSkuSourceSetCommon(SkuSourceCommon skuSourceCommon, List<SkuSourceSetting> skuSourceSettings) {
        skuSourceSettings.forEach(x -> {
            String value = x.getColumnValue();
            //万邦OneBound
            if ("1".equals(x.getCodeType())) {
                if ("accessKey".equals(x.getColumnName())) {
                    skuSourceCommon.oneBoundSourceSet.setAccessKey(value);
                }
                if ("apiSecret".equals(x.getColumnName())) {
                    skuSourceCommon.oneBoundSourceSet.setApiSecret(value);
                }
                if ("isUse".equals(x.getColumnName())) {
                    skuSourceCommon.oneBoundSourceSet.setIsUse(value);
                }
            }
        });
        return skuSourceCommon;
    }

    public OneBoundSourceSet getOneBoundSourceSet() {
        return oneBoundSourceSet;
    }

    public void setOneBoundSourceSet(OneBoundSourceSet oneBoundSourceSet) {
        this.oneBoundSourceSet = oneBoundSourceSet;
    }
}
