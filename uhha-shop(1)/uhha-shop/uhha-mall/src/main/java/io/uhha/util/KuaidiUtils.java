package io.uhha.util;

import com.google.gson.Gson;
import com.kuaidi100.sdk.api.QueryTrack;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.kuaidi100.sdk.request.QueryTrackParam;
import com.kuaidi100.sdk.request.QueryTrackReq;
import com.kuaidi100.sdk.response.QueryTrackResp;
import com.kuaidi100.sdk.utils.SignUtils;
import io.uhha.util.bean.KuaidiProperties;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * https://github.com/kuaidi100-api/java-demo/tree/master/src
 */
@Configuration
public class KuaidiUtils {

    private static KuaidiProperties kuaidiProperties;

    @Autowired
    public KuaidiUtils(KuaidiProperties kuaidiProperties){
        this.kuaidiProperties = kuaidiProperties;
    }

    /**
     * 获得快递查询信息
     *
     * @param express100Code 快递代号
     * @param waybillCode    运单号
     * @param phone 电话号码
     */
    public static QueryTrackResp getKuaidiInfo(String express100Code, String waybillCode, String phone) throws Exception {

        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(express100Code);
        queryTrackParam.setNum(waybillCode);
        queryTrackParam.setPhone(phone);
        String param = new Gson().toJson(queryTrackParam);

        queryTrackReq.setParam(param);
        queryTrackReq.setCustomer(kuaidiProperties.getCustomer());
        queryTrackReq.setSign(SignUtils.querySign(param ,kuaidiProperties.getKey(),kuaidiProperties.getCustomer()));

        IBaseClient baseClient = new QueryTrack();
        HttpResult result = baseClient.execute(queryTrackReq);
        if(200 == result.getStatus()){
            QueryTrackResp resp = new Gson().fromJson(result.getBody(), QueryTrackResp.class);
            return resp;
        }
        return null;
    }
}
