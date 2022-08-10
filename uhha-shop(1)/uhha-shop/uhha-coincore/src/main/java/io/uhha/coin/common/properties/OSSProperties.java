package io.uhha.coin.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author lgp
 * @version 1.0.190104
 */
@ConfigurationProperties(prefix = "aliyun.oss")
public class OSSProperties {

	private String accessId;
	private String accessKey;
	private String ossEndpoint;
	private String bucketBase;

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getOssEndpoint() {
		return ossEndpoint;
	}

	public void setOssEndpoint(String ossEndpoint) {
		this.ossEndpoint = ossEndpoint;
	}

	public String getBucketBase() {
		return bucketBase;
	}

	public void setBucketBase(String bucketBase) {
		this.bucketBase = bucketBase;
	}
}