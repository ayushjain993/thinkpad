package io.uhha.util.send;


import io.uhha.setting.domain.LsSmsSetting;
import io.uhha.validate.bean.ValidateSmsDO;

public interface Sender {

    /**
     * 发送短信
     *
     * @param smsSetting 账号信息
     * @param vs 短信信息
     */
    boolean send(LsSmsSetting smsSetting, ValidateSmsDO vs);
}
