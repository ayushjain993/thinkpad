package io.uhha.settlement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Assert;
import io.uhha.common.constant.Constant;
import io.uhha.common.enums.AccountTypeEnum;
import io.uhha.common.enums.SettlementTypeEnum;
import io.uhha.common.exception.CustomException;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.common.utils.uuid.IdUtils;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.settlement.domain.SettlementAccount;
import io.uhha.settlement.mapper.SettlementAccountMapper;
import io.uhha.settlement.model.AccountUser;
import io.uhha.settlement.service.IAccountCodeService;
import io.uhha.settlement.service.ISettlementAccountService;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.util.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 结算账户Service业务层处理
 *
 * @author uhha
 * @date 2021-12-31
 */
@Service
public class SettlementAccountServiceImpl extends ServiceImpl<SettlementAccountMapper, SettlementAccount> implements ISettlementAccountService {
    @Autowired
    private SettlementAccountMapper settlementAccountMapper;

    @Autowired
    private ITStoreInfoService storeInfoService;

    @Autowired
    private IUmsMemberService customerService;

    @Autowired
    private IAccountCodeService accountCodeService;

    @Override
    public List<AccountUser> selectAccountUserByAccountType(AccountTypeEnum accountType) {
        List<AccountUser> accountUsers = Collections.emptyList();
        switch (accountType) {
            case PERSONAL:
                List<io.uhha.member.vo.AccountUser> accounts = customerService.queryNotSettlementAccount();
                accountUsers = getAccountUsers(accounts);
                break;
            case STORE:
                List<io.uhha.member.vo.AccountUser> storeInfos = storeInfoService.queryNotSettlementAccount();
                accountUsers = getAccountUsers(storeInfos);
                break;
            default:
                break;
        }
        return accountUsers;
    }


    private List<AccountUser> getAccountUsers(List<io.uhha.member.vo.AccountUser> umsMembers) {
        return umsMembers.stream().map(umsMember -> {
            AccountUser accountUser = new AccountUser();
            accountUser.setId(umsMember.getId());
            accountUser.setName(umsMember.getName());
            return accountUser;
        }).collect(Collectors.toList());
    }

    /**
     * 查询结算账户
     *
     * @param id 结算账户主键
     * @return 结算账户
     */
    @Override
    public SettlementAccount selectSettlementAccountById(Long id) {
        return settlementAccountMapper.selectSettlementAccountById(id);
    }

    /**
     * 查询结算账户列表
     *
     * @param settlementAccount 结算账户
     * @return 结算账户
     */
    @Override
    public List<SettlementAccount> selectSettlementAccountList(SettlementAccount settlementAccount) {
        return settlementAccountMapper.selectSettlementAccountList(settlementAccount);
    }

    /**
     * 新增结算账户
     *
     * @param settlementAccount 结算账户
     * @return 结果
     */
    @Override
    public int insertSettlementAccount(SettlementAccount settlementAccount) {
        Assert.notNull(settlementAccount.getAccountType(), "系统账号类型不能为空");
        if (StringUtils.equals(settlementAccount.getAccountType(), AccountTypeEnum.SYSTEM.name())) {
            settlementAccount.setAssociateId(CommonConstant.ADMIN_STOREID);
            Assert.notNull(settlementAccount.getChildType(), "系统子账号类型不能为空");
        }
        //检查是否存在
        checkExist(settlementAccount);
        //填充数据
        String accountCode = accountCodeService.allocateAccountCode(settlementAccount.getAccountType());
        if(StringUtils.isEmpty(accountCode)){
            log.error("fail to get accountcode, please ensure it's generated.");
            return 0;
        }
        settlementAccount.setAccountCode(accountCode);
        settlementAccount.setCreateTime(DateUtils.getNowDate());
        settlementAccount.setCreateBy(Constant.SYSTEM);
        settlementAccount.setFrozenBalance(BigDecimal.ZERO);
        settlementAccount.setAvailableBalance(BigDecimal.ZERO);
        settlementAccount.setSettlementBeforeBalance(BigDecimal.ZERO);
        settlementAccount.setSettlementAfterBalance(BigDecimal.ZERO);

        settlementAccount.setSettlementTime(new Date());
        settlementAccount.setSettlementType(SettlementTypeEnum.AUTO.name());
        return settlementAccountMapper.insertSettlementAccount(settlementAccount);
    }

    private void checkExist(SettlementAccount settlementAccount) {
        Long count = lambdaQuery()
                .eq(SettlementAccount::getAccountType, settlementAccount.getAccountType())
                .eq(StringUtils.isNotBlank(settlementAccount.getChildType()), SettlementAccount::getChildType, settlementAccount.getChildType())
                .eq(SettlementAccount::getAssociateId, settlementAccount.getAssociateId())
                .count();
        if (count > 0) {
            throw new CustomException("账号已存在");
        }
    }


    /**
     * 修改结算账户
     *
     * @param settlementAccount 结算账户
     * @return 结果
     */
    @Override
    public int updateSettlementAccount(SettlementAccount settlementAccount) {
        return settlementAccountMapper.updateSettlementAccount(settlementAccount);
    }

    /**
     * 批量删除结算账户
     *
     * @param ids 需要删除的结算账户主键
     * @return 结果
     */
    @Override
    public int deleteSettlementAccountByIds(Long[] ids) {
        return settlementAccountMapper.deleteSettlementAccountByIds(ids);
    }

    /**
     * 删除结算账户信息
     *
     * @param id 结算账户主键
     * @return 结果
     */
    @Override
    public int deleteSettlementAccountById(Long id) {
        return settlementAccountMapper.deleteSettlementAccountById(id);
    }

    @Override
    public SettlementAccount querySettlementAccountByStoreId(String accountType, String childType, Long associateId) {
        return lambdaQuery().eq(SettlementAccount::getAccountType, accountType)
                .eq(StringUtils.isNotBlank(childType),SettlementAccount::getChildType,childType)
                .eq(SettlementAccount::getAssociateId, associateId).one();
    }

    @Override
    public SettlementAccount querySettlementAccountByAccountCode(String accountCode) {
        return lambdaQuery().eq(SettlementAccount::getAccountCode, accountCode).one();
    }


}
