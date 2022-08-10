package io.uhha.im.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ImMessageRequest {
    //用户id
    @NotBlank(message = "用户id不能为空")
    private String userId;
    //用户friendId
    @NotBlank(message = "用户friendId不能为空")
    private String friendId;

    private Integer type;



}
