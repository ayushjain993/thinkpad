package io.uhha.coin.common.dto.response;


import io.uhha.coin.common.util.ApiException;

import java.io.Serializable;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 11:56
 */

public class KlineResponse<T> implements Serializable {

    private static final long serialVersionUID = 6623109946970427260L;
    private String status;
    private String ch;
    private String ts;
    public String errCode;
    public String errMsg;
    public T data;

    public T checkAndReturn() {
        if ("ok".equals(status)) {
            return data;
        }
        throw new ApiException(errCode, errMsg);
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
