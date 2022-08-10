package io.uhha.coin.common.dto.response;

import java.io.Serializable;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 17:15
 */

public class SubmitcancelResponse implements Serializable {


    private static final long serialVersionUID = 6870944208126318527L;
    /**
     * status : ok
     * data : 59378
     */

    private String status;
    public String errCode;
    public String errMsg;
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
