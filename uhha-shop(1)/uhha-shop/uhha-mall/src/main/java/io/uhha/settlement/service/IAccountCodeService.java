package io.uhha.settlement.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.settlement.domain.AccountCode;

/**
 * 账户号码池Service接口
 * 
 * @author uhha
 * @date 2022-02-23
 */
public interface IAccountCodeService extends IService<AccountCode>
{
    /**
     * 查询账户号码池
     * 
     * @param id 账户号码池主键
     * @return 账户号码池
     */
    public AccountCode selectAccountCodeById(Long id);

    /**
     * 查询账户号码池列表
     * 
     * @param accountCode 账户号码池
     * @return 账户号码池集合
     */
    public List<AccountCode> selectAccountCodeList(AccountCode accountCode);

    public String allocateAccountCode(String accountType);

    public boolean generateAccountCode(String accountType, Integer currency, Integer amount);
}
