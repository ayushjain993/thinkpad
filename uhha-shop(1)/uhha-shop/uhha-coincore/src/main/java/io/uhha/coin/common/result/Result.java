package io.uhha.coin.common.result;

import java.io.Serializable;

/**
 * 服务端返回消息
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 执行状态
     */
    private Boolean isSuccess;
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误消息
     */
    private String msg;
    /**
     * 时间
     */
    private Long time;


    /**
     * 附加消息
     */
    private Object data;

    /**
     * 成功
     */
    public static Result success() {
        Result result = new Result();
        result.setSuccess(true);
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /**
     * 成功
     */
    public static Result success(int code, String msg) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMsg(msg);
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /**
     * 成功
     */
    public static Result success(int code, Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setData(data);
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /**
     * 失败
     */
    public static Result failure() {
        Result result = new Result();
        result.setSuccess(false);
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /**
     * 失败
     *
     * @param code 错误码
     * @param msg  错误消息
     */
    public static Result failure(int code, String msg) {
        return failure(code, msg, null);
    }

    /**
     * 失败
     *
     * @param code 错误码
     * @param msg  错误消息
     * @param data 附加消息
     * @return
     */
    public static Result failure(int code, String msg, Object data) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        result.setTime(System.currentTimeMillis());
        return result;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
