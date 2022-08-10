package io.uhha.member.service.impl;

import io.uhha.coin.common.Enum.BankCardTypeEnum;
import io.uhha.coin.user.domain.FUserBankinfo;
import io.uhha.coin.user.mapper.FUserBankinfoMapper;
import io.uhha.common.utils.DateUtils;
import io.uhha.member.service.UserBankInfoService;
import io.uhha.member.vo.UserBankInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserBankInfoServiceImpl implements UserBankInfoService {
    @Autowired
    private FUserBankinfoMapper userBankinfoMapper;

    @Override
    public int addUserBankInfo(UserBankInfoVo userBankinfo) {

        FUserBankinfo bankinfoList = userBankinfoMapper.getBankInfoListByUid(userBankinfo.getUserId());

        if(bankinfoList !=null){
            log.error("bank account exists!");
            return -1;
        }

        FUserBankinfo info = new FUserBankinfo();
        info.setVersion(1);
        info.setFstatus(0);
        info.setFbanktype(userBankinfo.getCardType());
        info.setFcreatetime(DateUtils.getNowDate());
        info.setFrealname(userBankinfo.getFirstname()+"-"+userBankinfo.getLastname());
        info.setFuid(userBankinfo.getUserId());
        info.setFcountry(userBankinfo.getCountry());
        info.setCountryid(userBankinfo.getCountryId());
        info.setFbankid(userBankinfo.getBankId());
        info.setFname(userBankinfo.getBankName());
        info.setFaddress(userBankinfo.getBankAddr());
        info.setFbanknumber(userBankinfo.getBankNo());
        info.setFbanktype(userBankinfo.getCardType());
        return userBankinfoMapper.insert(info);
    }

    @Override
    public int updateUserBankInfo(UserBankInfoVo userBankinfo) {
        FUserBankinfo info = new FUserBankinfo();
        info.setVersion(1);
        info.setFstatus(0);
        info.setFbanktype(userBankinfo.getCardType());
        info.setFcreatetime(DateUtils.getNowDate());
        info.setFrealname(userBankinfo.getFirstname()+"-"+userBankinfo.getLastname());
        info.setFuid(userBankinfo.getUserId());
        info.setFcountry(userBankinfo.getCountry());
        info.setCountryid(userBankinfo.getCountryId());
        info.setFbankid(userBankinfo.getBankId());
        info.setFname(userBankinfo.getBankName());
        info.setFaddress(userBankinfo.getBankAddr());
        info.setFbanknumber(userBankinfo.getBankNo());
        info.setFbanktype(userBankinfo.getCardType());

        return userBankinfoMapper.updateByUid(info);
    }

    @Override
    public UserBankInfoVo getUserBankInfoByUid(Long userId) {
        FUserBankinfo userBankinfo = userBankinfoMapper.getBankInfoListByUid(userId);
        if(userBankinfo==null){
            return null;
        }
        UserBankInfoVo vo = new UserBankInfoVo();
        vo.setUserId(userBankinfo.getFuid());
        vo.setBankId(userBankinfo.getFbankid());
        vo.setBankName(userBankinfo.getFname());
        vo.setCountry(userBankinfo.getFcountry());
        vo.setBankAddr(userBankinfo.getFaddress());
        vo.setBankNo(userBankinfo.getFbanknumber());
        vo.setCardType(userBankinfo.getFbanktype());
        String name = userBankinfo.getFrealname();
        String[] names = name.split("-");
        if(names.length==2){
            vo.setFirstname(names[0]);
            vo.setLastname(names[1]);
        }
        vo.setCountryId(userBankinfo.getCountryid());

        return vo;
    }
}

