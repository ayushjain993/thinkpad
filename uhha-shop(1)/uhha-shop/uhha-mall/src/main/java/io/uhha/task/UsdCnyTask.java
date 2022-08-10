package io.uhha.task;

import io.uhha.common.redis.RedisMallHelper;
import io.uhha.common.utils.http.OKHttp3ClientUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component("usdCnyTask")
@Slf4j
public class UsdCnyTask {
    public static final String url = "https://srh.bankofchina.com/search/whpj/search_cn.jsp";

    @Autowired
    private RedisMallHelper redisMallHelper;

    public void getRate() {
//        Map<String, String> params = new HashMap<>();
//        params.put("pjname", "美元");
//        params.put("page", "1");
//        params.put("head", "head_620.js");
//        params.put("bottom", "bottom_591.js");
//        try {
//            Response response = OKHttp3ClientUtils.getInstance().postData(url, params);
//            if (response.isSuccessful()) {
//                ResponseBody body = response.body();
//                String strBody = body.string();
//                log.debug(strBody);
//
//                SAXReader reader = new SAXReader ();
//                Document doc = reader.read(new InputSource(new StringReader(strBody)));
//
//                String rateXPath = "/html/body/div/div[4]/table/tbody/tr[2]/td[5]";
//                String timeXPath = "/html/body/div/div[4]/table/tbody/tr[2]/td[7]";
//                Node rateNode = doc.selectSingleNode(rateXPath);
//                Node timeNode = doc.selectSingleNode(timeXPath);
//                log.debug(rateNode.getText());
//                log.debug(timeNode.getText());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
        redisMallHelper.setUSDCNYRate(new BigDecimal("6.330"));

    }

//    public static void main(String[] args) {
//        UsdCnyTask task = new UsdCnyTask();
//        task.getRate();
//    }

}
