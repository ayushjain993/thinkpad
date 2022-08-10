package io.uhha.coin.common.dto.response;

import io.uhha.coin.common.util.ApiException;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 5359461337414879743L;
    public String status;
    public String errCode;
    public String errMsg;
    public T data;

    public T checkAndReturn() {
        if ("ok".equals(status)) {
            return data;
        }
        throw new ApiException(errCode, errMsg);
    }
}
