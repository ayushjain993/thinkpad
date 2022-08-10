package io.uhha.appletsutil;

import io.uhha.common.exception.ServiceException;
import io.uhha.common.md5.MessageSourceUtil;
import lombok.Data;

/**
 * Created by mj on 2018/6/13.
 * 程序未授权异常
 */
@Data
public class UnAuthorizedException extends ServiceException {

    /**
     * 错误code
     */
    private String errorCode;

    public UnAuthorizedException(String errorCode) {
        this(errorCode, MessageSourceUtil.getMessage(errorCode));
    }

    public UnAuthorizedException(String errorCode, String message) {
        super(message == null ? errorCode : message);
        this.errorCode = errorCode;
    }
}
