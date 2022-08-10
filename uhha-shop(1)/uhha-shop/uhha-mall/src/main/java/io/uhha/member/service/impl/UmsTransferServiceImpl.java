package io.uhha.member.service.impl;

import io.uhha.coin.common.match.MathUtils;
import io.uhha.common.notify.MallNotifyHelper;
import io.uhha.common.utils.SnowflakeIdWorker;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.domain.UmsPreDepositRecord;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.service.IUmsPreDepositRecordService;
import io.uhha.member.service.IUmsTransferService;
import io.uhha.member.vo.UmsTransferVo;
import io.uhha.order.domain.OmsCommissionRecords;
import io.uhha.order.service.IOmsCommissionRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@Slf4j
public class UmsTransferServiceImpl implements IUmsTransferService {

    @Autowired
    private IUmsPreDepositRecordService preDepositRecordService;

    @Autowired
    private IUmsMemberService customerService;

    /**
     * 密码工具类
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注入随机数生成工具
     */
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;


    /**
     * 注入佣金记录
     */
    @Autowired
    private IOmsCommissionRecordsService commissionRecordService;

    @Autowired
    private MallNotifyHelper mallNotifyHelper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int umsTransfer(UmsTransferVo umsTransfer, Long userId) {
        log.debug("umsTransfer and umsTransfer:{}", umsTransfer);

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerInfoById(userId);

        if (Objects.isNull(customer)) {
            log.error("umsTransfer fail due to member is null ");
            return -1;
        }

        if (Objects.isNull(customer.getPaypassword())) {
            log.error("umsTransfer fail due to member payPassword is null ");
            return -2;
        }

        // 校验用户输入的支付密码是否正确
        if (!passwordEncoder.matches(umsTransfer.getPassword(), customer.getPaypassword())) {
            log.error("umsTransfer fail due to password is error...");
            return -3;
        }


        //校验佣金是否足够提现
        if (!MathUtils.checkBalance(customer.getCommission(),umsTransfer.getMoney())) {
            log.error("umsTransfer fail due to commission is not enough...");
            return -5;
        }
        //更改用户表中的佣金
        customerService.updateCustomerCommission(userId, umsTransfer.getMoney().negate());
        //添加佣金变更记录
        commissionRecordService.insertOmsCommissionRecords(OmsCommissionRecords.buildForExpend(userId, umsTransfer.getMoney(), OmsCommissionRecords.TRANSFER_REMARK));

        //更改用户可用余额
        preDepositRecordService.addPredepositRecord(UmsPreDepositRecord.buildCommissionTransfer(umsTransfer.getMoney(),userId, snowflakeIdWorker.nextId()));

        //发送通知
        mallNotifyHelper.commissionTransferToBalance(umsTransfer,userId);

        return 1;
    }

}
