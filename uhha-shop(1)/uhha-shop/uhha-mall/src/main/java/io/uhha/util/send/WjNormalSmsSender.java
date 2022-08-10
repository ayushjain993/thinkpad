package io.uhha.util.send;

import io.uhha.setting.domain.LsEmailSetting;
import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.validate.bean.ValidateSmsDO;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("wjNormalSmsSender")
public class WjNormalSmsSender implements Sender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 网建短信接口
     *
     * @param smsSetting 账号信息
     * @param vs 短信信息
     */
    @Override
    public boolean send(LsSmsSetting smsSetting, ValidateSmsDO vs) {
        Map maps = new HashMap();
        maps.put("Uid", smsSetting.getAppKey());
        maps.put("Key", smsSetting.getAppSecret());
        maps.put("smsMob", vs.getPhone());
        maps.put("smsText", vs.getContent());
        String result = sendHttpPost(smsSetting.getUrl(), maps, "utf-8");
        int resInt = Integer.parseInt(result);
        if (resInt > 0) {
            return true;
        }
        logger.error("短信发送失败，返回代码：" + result);
        return false;

    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     * @param type    字符编码格式
     */
    private String sendHttpPost(String httpUrl, Map<String, String> maps, String type) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, type);
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost, String reponseType) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(15000)
                .setConnectTimeout(15000)
                .setConnectionRequestTimeout(15000)
                .build();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity ;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, reponseType);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }
}
