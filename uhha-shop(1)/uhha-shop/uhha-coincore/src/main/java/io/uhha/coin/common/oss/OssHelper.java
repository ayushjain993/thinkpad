package io.uhha.coin.common.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.properties.OSSProperties;
import io.uhha.coin.common.util.GUIDUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * OSS公共接口
 * 
 * @author ZKF PS: 新调用在spring中增加以下配置
 *         <bean id="ossHelper" class="io.uhha.coin.common.oss.OssHelper"></bean>
 */

@EnableConfigurationProperties(OSSProperties.class)
public class OssHelper {

	private RedisCryptoHelper redisCryptoHelper;
	private OSSProperties properties;

	public void setRedisHelper(RedisCryptoHelper redisCryptoHelper) {
		this.redisCryptoHelper = redisCryptoHelper;
	}

	public void setProperties(OSSProperties properties) {
		this.properties = properties;
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param filePath
	 * @return
	 */
	public String uploadFile(MultipartFile file, String filePath) {
		// file.getContentType() 获取文件MIME类型
		// file.getOriginalFilename() 获取上传文件的原名
		String result;
		try {
			// result = uploadOSS(OSSConstant.BUCKET_BASE,
			// file.getContentType(), new ByteArrayInputStream(file.getBytes()),
			// file.getBytes().length, file.getOriginalFilename(), filePath);
			result = uploadOSS(properties.getBucketBase(), file.getContentType(),
					new ByteArrayInputStream(file.getBytes()), file.getBytes().length, file.getOriginalFilename(),
					filePath);
		} catch (Exception e) {
			return null;
		}
		if (result == null) {
			return null;
		}
		return redisCryptoHelper.getSystemArgs("imgUploadUrl") + result;
	}

	/**
	 * OSS上传文件
	 * 
	 * @param Bucket
	 * @param contentType
	 * @param fileSize
	 * @param input
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	private String uploadOSS(String Bucket, String contentType, InputStream input, long fileSize, String fileName,
			String filePath) throws Exception {
		// 参数校验
		if (StringUtils.isEmpty(contentType) || StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileName)) {
			return null;
		}
		String key = filePath + GUIDUtils.getGUIDString().toLowerCase() + fileName;
		// OSSClient client = new OSSClient(OSSConstant.OSS_ENDPOINT,
		// OSSConstant.ACCESS_ID, OSSConstant.ACCESS_KEY);
		OSSClient client = new OSSClient(properties.getOssEndpoint(), properties.getAccessId(),
				properties.getAccessKey());
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(fileSize);

		try {
			if (!client.doesBucketExist(Bucket)) {
				// 创建bucket
				client.createBucket(Bucket);
				client.setBucketAcl(Bucket, CannedAccessControlList.PublicRead);
			}
		} catch (ServiceException e) {
			// 如果Bucket已经存在，则忽略
			if (!OSSErrorCode.BUCKET_ALREADY_EXISTS.equals(e.getErrorCode())) {
				throw e;
			}
		}

		objectMeta.setContentType(contentType);
		client.putObject(Bucket, key, input, objectMeta);
		client.shutdown();
		return key;
	}

}
