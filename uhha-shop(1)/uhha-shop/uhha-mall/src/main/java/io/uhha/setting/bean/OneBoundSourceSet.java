package io.uhha.setting.bean;

import lombok.Data;

@Data
public class OneBoundSourceSet {
    /**
     * 授权key
     */
    private String accessKey;

    /**
     * 秘钥(secret)
     */
    private String apiSecret;

    /**
     * 是否启用
     */
    private String isUse;
}
