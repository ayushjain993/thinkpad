package io.uhha.settlement.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.enums.AccountTypeEnum;
import io.uhha.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.settlement.mapper.AccountCodeMapper;
import io.uhha.settlement.domain.AccountCode;
import io.uhha.settlement.service.IAccountCodeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户号码池Service业务层处理
 * 
 * @author uhha
 * @date 2022-02-23
 */
@Service
@Slf4j
public class AccountCodeServiceImpl extends ServiceImpl<AccountCodeMapper, AccountCode> implements IAccountCodeService
{
    @Autowired
    private AccountCodeMapper accountCodeMapper;

    /**
     * 查询账户号码池
     * 
     * @param id 账户号码池主键
     * @return 账户号码池
     */
    @Override
    public AccountCode selectAccountCodeById(Long id)
    {
        return accountCodeMapper.selectAccountCodeById(id);
    }

    /**
     * 查询账户号码池列表
     * 
     * @param accountCode 账户号码池
     * @return 账户号码池
     */
    @Override
    public List<AccountCode> selectAccountCodeList(AccountCode accountCode)
    {
        return accountCodeMapper.selectAccountCodeList(accountCode);
    }

    /**
     * 分配账户号码池
     *
     * @param accountType 账户类型
     * @return 结果accountNo
     */
    @Transactional
    public String allocateAccountCode(String accountType){
        AccountCode accountCode = accountCodeMapper.selectFreeAccountCode(accountType);
        accountCodeMapper.allocateAccountCode(accountCode.getId());
        return accountCode.getAccountNo();
    }

    @Override
    public boolean generateAccountCode(String accountType, Integer currency, Integer amount) {
        List<AccountCode> accountCodeList = new ArrayList<>();

        Long maxId = accountCodeMapper.selectMaxId();
        maxId = maxId==null? 0L:maxId;

        for(int i=0; i< amount;i++){
            String code = generateOneAccountCode(accountType, currency, maxId+i);
            AccountCode accountCode = AccountCode.builder()
                    .accountNo(code)
                    .status(0)
                    .accountType(accountType)
                    .currency(currency)
                    .build();
            accountCodeList.add(accountCode);
        }

        return this.saveBatch(accountCodeList);
    }

    private String generateOneAccountCode(String accountType, Integer currency, Long index){
        String prefix = "";

        if(AccountTypeEnum.PERSONAL.name().equals(accountType)){
            prefix = CommonConstant.PERSONAL_ACCOUNT_PREFIX;
        } else if(AccountTypeEnum.STORE.name().equals(accountType)){
            prefix = CommonConstant.STORE_ACCOUNT_PREFIX;
        }else if (AccountTypeEnum.SYSTEM.name().equals(accountType)){
            prefix = CommonConstant.SYSTEM_ACCOUNT_PREFIX;
        }else{
            log.error("not supported accountType: {}", accountType);
            return "";
        }
        String currencyPrefix = "";
        if(currency == 1){
            currencyPrefix = CommonConstant.USD_PREFIX;
        }else if(currency == 2){
            currencyPrefix = CommonConstant.CNY_PREFIX;
        }else{
            log.error("not supported currency: {}", currency);
            return "";
        }

        String no = String.format("%08d", index);
        if(index>99999999L){
            log.error("the number is out of storage!");
            return "";
        }
        return prefix+currencyPrefix+no;
    }

}
