package io.uhha.member.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserBankInfoVo {
    private Long userId;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private Long countryId;
    @NotNull
    private String country;
    private String bankName;
    private String bankNo;
    private Integer cardType;
    @NotNull
    private Long bankId;
    private String bankAddr;
    private String swiftCode;
}
