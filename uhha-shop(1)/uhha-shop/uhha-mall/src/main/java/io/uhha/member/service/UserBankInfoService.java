package io.uhha.member.service;

import io.uhha.member.vo.UserBankInfoVo;

public interface UserBankInfoService {
    public int addUserBankInfo(UserBankInfoVo userBankinfo);
    public int updateUserBankInfo(UserBankInfoVo userBankinfo);
    public UserBankInfoVo getUserBankInfoByUid(Long userId);
}
