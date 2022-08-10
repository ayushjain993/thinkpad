package io.uhha.member.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.uhha.member.domain.UmsMember;
import lombok.Data;

import java.util.function.Consumer;


/**
 * 获取验证码参数实体
 */
@Data
@ApiModel(description = "获取验证码参数实体")
public class ValidateCodeParams {

    /**
     * 国家码
     */
    @ApiModelProperty(value = "国家码")
    private String areacode;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    private Integer type;

    private Integer msgType;

    //价格
    private Double price;


}
