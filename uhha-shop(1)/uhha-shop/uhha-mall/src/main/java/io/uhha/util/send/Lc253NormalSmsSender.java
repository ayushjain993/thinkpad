package io.uhha.util.send;

import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.validate.bean.ValidateSmsDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component("lc253NormalSmsSender")
public class Lc253NormalSmsSender implements Sender {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创蓝短信接口
     *
     * @param smsSetting 账号信息
     * @param vs 短信信息
     */
    @Override
    public boolean send(LsSmsSetting smsSetting, ValidateSmsDO vs) {
        try {
            // 创建StringBuffer对象用来操作字符串
            StringBuilder sb = new StringBuilder();
            sb.append(smsSetting.getUrl());
            sb.append("?");
            // APIKEY
            sb.append("un=");
            sb.append(smsSetting.getAppKey());
            // 用户名
            sb.append("&pw=");
            sb.append(smsSetting.getAppSecret());
            // 向StringBuffer追加手机号码
            sb.append("&phone=");
            sb.append(vs.getPhone());
            // 向StringBuffer追加消息内容转URL标准码
            sb.append("&msg=");
            sb.append(URLEncoder.encode(vs.getContent(), "UTF-8"));
            // 是否状态报告
            sb.append("&rd=" + 1);
            // 创建url对象
            URL url = new URL(sb.toString());
            // 打开url连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置url请求方式 ‘get’ 或者 ‘post’
            connection.setRequestMethod("POST");
            // 发送
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            // 返回发送结果
            String inputline = in.readLine();
            // 输出结果
            System.out.println(inputline);
            connection.disconnect();
        } catch (Exception e) {
            logger.error("sendNew253NormalSms failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
