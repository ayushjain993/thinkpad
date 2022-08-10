package io.uhha.im.model;

import io.uhha.common.enums.ImUserTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ImUserRequest {
    //用户类型
    @NotNull(message = "用户类型不能为空")
    private ImUserTypeEnum  type;

    //用户id
    @NotBlank(message = "用户id不能为空")
    private String userId;

}
