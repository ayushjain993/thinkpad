package io.uhha.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GoogleBindVo {
    @NotNull
    private String code;
    @NotNull
    private String totpkey;
    @NotNull
    private String qecode;
}
