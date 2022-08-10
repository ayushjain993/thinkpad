package io.uhha.coin.common.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * https网站一般情况下使用了安全系数较低的SHA-1签名，因此首先我们在调用SSL之前需要重写验证方法，取消检测SSL。
 * @author ZKF
 */
public class MyTrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}