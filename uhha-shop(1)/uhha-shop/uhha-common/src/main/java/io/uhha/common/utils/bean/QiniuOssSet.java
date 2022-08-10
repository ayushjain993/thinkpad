package io.uhha.common.utils.bean;

import lombok.Data;

@Data
public class QiniuOssSet {
    /**
     * 授权ID(accessKey)
     */
    private String accessKey;

    /**
     * 授权密钥(secretKey)
     */
    private String secretKey;

    /**
     * 识别符(bucket)
     * @return
     */
    private String bucket;

    /**
     * 外链域名(endpoint)
     */
    private String host;

    /**
     * 是否启用
     */
    private String isUse;
}
