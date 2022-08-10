package io.uhha.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class UpdateStorePwdBean {

    /**
     * 国家码
     */
    @ApiModelProperty(value = "国家码")
    private String areacode;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 用户输入的手机验证码
     */
    @ApiModelProperty(value = "用户输入的手机验证码")
    private String code;


    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
}
