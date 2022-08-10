package io.uhha.coin.user.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserVWAddressVo {
    @NotNull
    private int coinid;
    @NotNull
    private String address;
    private String remark;
    private String pcode;
    private String gcode;
}
