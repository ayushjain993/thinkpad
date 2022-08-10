package io.uhha.common.utils.bean;

import lombok.Data;

/**
 * Created by mj on 17/10/14.
 * 邮箱配置类
 */
@Data
public class EmailConf {

    /**
     * 邮箱服务器的地址
     */
    private String host;

    /**
     * 发送邮件人的用户名
     */
    private String userName;

    /**
     * 发送邮件人的密码
     */
    private String password;


}
