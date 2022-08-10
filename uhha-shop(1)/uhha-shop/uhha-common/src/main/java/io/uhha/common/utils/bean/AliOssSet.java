package io.uhha.common.utils.bean;

import lombok.Data;

@Data
public class AliOssSet {
    /**
     * 授权ID(accessKey)
     */
    private String accessKey;

    /**
     * 授权密钥(secretKey)
     */
    private String secretKey;

    /**
     * 桶名(bucket)
     * @return
     */
    private String bucket;

    /**
     * 节点名(endpoint)
     */
    private String endPoint;

    /**
     * 是否启用
     */
    private String isUse;
}
