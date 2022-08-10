package io.uhha.store.vo;

import io.uhha.common.core.domain.entity.SysUser;

public class StoreBusinessUser extends SysUser {
    private String verificationCode;

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
