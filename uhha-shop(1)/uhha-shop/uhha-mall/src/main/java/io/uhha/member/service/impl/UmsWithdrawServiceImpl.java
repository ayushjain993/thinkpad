package io.uhha.member.service.impl;

import io.uhha.coin.common.Enum.validate.BusinessTypeEnum;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.common.notify.MallNotifyHelper;
import io.uhha.common.utils.AliPayUtils;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.SnowflakeIdWorker;
import io.uhha.common.utils.bean.AliPaySetting;
import io.uhha.common.utils.bean.WithdrawRequest;
import io.uhha.common.utils.bean.WithdrawResponse;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.domain.UmsPreDepositRecord;
import io.uhha.member.domain.UmsWithdraw;
import io.uhha.member.mapper.UmsWithdrawMapper;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.service.IUmsPreDepositRecordService;
import io.uhha.member.service.IUmsWithdrawService;
import io.uhha.member.service.UserBankInfoService;
import io.uhha.member.vo.UmsWithdrawVo;
import io.uhha.member.vo.UserBankInfoVo;
import io.uhha.order.domain.OmsCommissionRecords;
import io.uhha.order.service.IOmsCommissionRecordsService;
import io.uhha.setting.bean.AliPaySet;
import io.uhha.setting.service.ILsPaySettingService;
import io.uhha.util.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 提现记录Service业务层处理
 *
 * @author mj
 * @date 2020-07-25
 */
@Service
public class UmsWithdrawServiceImpl implements IUmsWithdrawService {
    @Autowired
    private UmsWithdrawMapper umsWithdrawMapper;

    /**
     * 注入提现记录数据库接口
     */
    @Autowired
    private UmsWithdrawMapper withdrawRecordMapper;

    /**
     * 注入会员服务
     */
    @Autowired
    private IUmsMemberService customerService;

    /**
     * 注入佣金记录
     */
    @Autowired
    private IOmsCommissionRecordsService commissionRecordService;

    /**
     * 注入随机数生成工具
     */
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 注入支付设置服务
     */
    @Autowired
    private ILsPaySettingService paySetService;

    /**
     * 注入预存款服务接口
     */
    @Autowired
    private IUmsPreDepositRecordService predepositRecordService;

    /**
     * 密码工具类
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUmsPreDepositRecordService preDepositRecordService;

    @Autowired
    private UserBankInfoService userBankInfoService;

    @Autowired
    private MallNotifyHelper mallNotifyHelper;

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(UmsWithdrawServiceImpl.class);

    @Override
    public PageHelper<UmsWithdraw> queryWithdrawRecords(PageHelper<UmsWithdraw> pageHelper, UmsWithdraw.QueryCriteria queryCriteria) {
        logger.debug("queryWithdrawRecords and pageHelper:{} \r\n queryCriteria:{}", pageHelper, queryCriteria);
        return pageHelper.setListDates(withdrawRecordMapper.queryWithdrawRecords(pageHelper.getQueryParams(queryCriteria.getQueryMap(), withdrawRecordMapper.queryWithdrawRecordsCount(queryCriteria.getQueryMap()))));
    }

    @Override
    public PageHelper<UmsWithdraw> queryWithdrawRecordsForAdmin(PageHelper<UmsWithdraw> pageHelper, UmsWithdraw.QueryCriteria queryCriteria) {
        logger.debug("queryWithdrawRecordsForAdmin and pageHelper:{} \r\n queryCriteria:{}", pageHelper, queryCriteria);
        return pageHelper.setListDates(withdrawRecordMapper.queryWithdrawRecordsForAdmin(pageHelper.getQueryParams(queryCriteria.getQueryMap(), withdrawRecordMapper.queryWithdrawRecordsCountForAdmin(queryCriteria.getQueryMap()))));
    }

    @Override
    @Transactional
    public int addWithdrawRecord(UmsWithdraw withdrawRecord, String password) {
        logger.debug("addWithdrawRecord and withdrawRecord:{}", withdrawRecord);
        if (!withdrawRecord.checkParams()) {
            logger.error("addWithdrawRecord fail due to checkParams fail ");
            return -4;
        }
        // 查询用户信息
        UmsMember customer = customerService.queryCustomerInfoById(withdrawRecord.getCustomerId());

        if (Objects.isNull(customer)) {
            logger.error("addWithdrawRecord fail due to member is null ");
            return -1;
        }

        if (Objects.isNull(customer.getPaypassword())) {
            logger.error("addWithdrawRecord fail due to member payPassword is null ");
            return -2;
        }

        // 校验用户输入的支付密码是否正确
        if (!passwordEncoder.matches(password, customer.getPaypassword())) {
            logger.error("addWithdrawRecord fail due to password is error...");
            return -3;
        }
        //校验佣金是否足够提现
        if (!customer.checkCommission(withdrawRecord.getMoney().negate())) {
            logger.error("addWithdrawRecord fail due to commission is not enough...");
            return -5;
        }
        //设置流水号
        withdrawRecord.setTradeNo(snowflakeIdWorker.nextId() + "");
        //添加提现记录
        withdrawRecordMapper.addWithdrawRecord(withdrawRecord);
        //更改用户表中的佣金
        customerService.updateCustomerCommission(withdrawRecord.getCustomerId(), withdrawRecord.getMoney().negate());
        //添加佣金变更记录
        commissionRecordService.insertOmsCommissionRecords(OmsCommissionRecords.buildForExpend(withdrawRecord.getCustomerId(), withdrawRecord.getMoney(), OmsCommissionRecords.WITHDRAW_REMARK));
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addWithdrawRecord(UmsWithdrawVo withdrawVo, Long userId) {

        logger.debug("addWithdrawRecord and withdrawRecord:{}", withdrawVo);
        if (!withdrawVo.checkParams()) {
            logger.error("addWithdrawRecord fail due to checkParams fail ");
            return -4;
        }
        // 查询用户信息
        UmsMember customer = customerService.queryCustomerInfoById(userId);

        if (Objects.isNull(customer)) {
            logger.error("addWithdrawRecord fail due to member is null ");
            return -1;
        }

        if (Objects.isNull(customer.getPaypassword())) {
            logger.error("addWithdrawRecord fail due to member payPassword is null ");
            return -2;
        }

        // 校验用户输入的支付密码是否正确
        if (!passwordEncoder.matches(withdrawVo.getPassword(), customer.getPaypassword())) {
            logger.error("addWithdrawRecord fail due to password is error...");
            return -3;
        }

        //检查用户配置的银行卡信息
        UserBankInfoVo userBankInfo = userBankInfoService.getUserBankInfoByUid(userId);
        if(userBankInfo==null){
            logger.error("addWithdrawRecord fail due to userBankInfo is empty...");
            return -4;
        }

        if(!withdrawVo.getAccount().equalsIgnoreCase(userBankInfo.getBankNo())){
            logger.error("addWithdrawRecord fail due to bank account info is different...");
            return -4;
        }

        //获取用户的可用余额
        BigDecimal avaliableDeposit = predepositRecordService.queryCutomerAllPredeposit(customer.getId());

        //校验佣金是否足够提现
        if (!MathUtils.checkBalance(avaliableDeposit,withdrawVo.getMoney())) {
            logger.error("addWithdrawRecord fail due to all deposit is not enough...");
            return -5;
        }

        //构造提款实体
        UmsWithdraw withdrawRecord = new UmsWithdraw();
        withdrawRecord.setAccount(withdrawVo.getAccount());
        withdrawRecord.setCustomerId(userId);
        withdrawRecord.setMoney(withdrawVo.getMoney());
        withdrawRecord.setName(withdrawVo.getName());

        //设置流水号
        withdrawRecord.setTradeNo(snowflakeIdWorker.nextId() + "");
        //添加提现记录
        withdrawRecordMapper.addWithdrawRecord(withdrawRecord);
        //添加余额变更记录
        preDepositRecordService.addPredepositRecord(UmsPreDepositRecord.buildWithdraw(withdrawRecord.getMoney(),userId, snowflakeIdWorker.nextId()));

        //发送通知
        mallNotifyHelper.applyWithdrawNotify(withdrawRecord);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWithdrawRecordStatus(long id, String status) {
        logger.debug("updateWithdrawRecordStatus and id:{} \r\n status:{}", id, status);
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("status", status);
        // 如果是拒绝提现 则将账户扣除的钱返回
        if ("2".equals(status)) {
            logger.debug("withdraw refuse and  begin to back money");
            UmsWithdraw withdrawRecord = withdrawRecordMapper.queryWithdrawRecordById(id);
            //添加记录
            preDepositRecordService.addPredepositRecord(UmsPreDepositRecord.buildWithdrawFail(withdrawRecord.getMoney(),withdrawRecord.getCustomerId(),withdrawRecord.getTradeNo()));
            //更新
            customerService.updateCustomerCommission(withdrawRecord.getCustomerId(), withdrawRecord.getMoney());
            //发送通知
            mallNotifyHelper.withdrawStatusUpdate(withdrawRecord.getMoney(),withdrawRecord.getCustomerId(), status);
        }else if("3".equals(status)){
            logger.debug("withdraw released");
            UmsWithdraw withdrawRecord = withdrawRecordMapper.queryWithdrawRecordById(id);
            preDepositRecordService.addPredepositRecord(UmsPreDepositRecord.buildWithdrawRelease(withdrawRecord.getMoney(),withdrawRecord.getCustomerId(),withdrawRecord.getTradeNo()));
        }

        return withdrawRecordMapper.updateWithdrawRecordStatus(params);
    }

    @Override
    public WithdrawResponse releaseMoney(long id) {
        logger.debug("releaseMoney and id:{}", id);
        //支付宝支付设置(数据库)
        AliPaySet aliPaySet = paySetService.queryPaySet().getAliPaySet();
        AliPaySetting aliPaySetting = new AliPaySetting();
        //支付宝支付设置
        BeanUtils.copyProperties(aliPaySet, aliPaySetting);
        // 校验参数
        if (!aliPaySetting.checkPayParams()) {
            logger.error("releaseMoney fail due to no checkPayParams fail....");
            return WithdrawResponse.buildSystemError();
        }
        // 查询提现记录
        UmsWithdraw withdraw = withdrawRecordMapper.queryWithdrawRecordById(id);
        //构建提现实体
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        BeanUtils.copyProperties(withdraw, withdrawRequest);

        if (Objects.isNull(withdraw)) {
            return WithdrawResponse.buildSystemError();
        }
        // 支付宝提现
        WithdrawResponse withdrawResponse = AliPayUtils.withdraw(aliPaySetting, withdrawRequest);
        // 如果成功则修改状态
        if (withdrawResponse.isSuccess()) {
            logger.debug("withdraw success....");
            updateWithdrawRecordStatus(id, "3");
        }
        return withdrawResponse;
    }

    /**
     * 查询提现记录
     *
     * @param id 提现记录ID
     * @return 提现记录
     */
    @Override
    public UmsWithdraw selectUmsWithdrawById(Long id) {
        return umsWithdrawMapper.selectUmsWithdrawById(id);
    }

    /**
     * 查询提现记录列表
     *
     * @param umsWithdraw 提现记录
     * @return 提现记录
     */
    @Override
    public List<UmsWithdraw> selectUmsWithdrawList(UmsWithdraw umsWithdraw) {
        return umsWithdrawMapper.selectUmsWithdrawList(umsWithdraw);
    }

    /**
     * 新增提现记录
     *
     * @param umsWithdraw 提现记录
     * @return 结果
     */
    @Override
    public int insertUmsWithdraw(UmsWithdraw umsWithdraw) {
        umsWithdraw.setCreateTime(DateUtils.getNowDate());
        return umsWithdrawMapper.insertUmsWithdraw(umsWithdraw);
    }

    /**
     * 修改提现记录
     *
     * @param umsWithdraw 提现记录
     * @return 结果
     */
    @Override
    public int updateUmsWithdraw(UmsWithdraw umsWithdraw) {
        return umsWithdrawMapper.updateUmsWithdraw(umsWithdraw);
    }

    /**
     * 批量删除提现记录
     *
     * @param ids 需要删除的提现记录ID
     * @return 结果
     */
    @Override
    public int deleteUmsWithdrawByIds(Long[] ids) {
        return umsWithdrawMapper.deleteUmsWithdrawByIds(ids);
    }

    /**
     * 删除提现记录信息
     *
     * @param id 提现记录ID
     * @return 结果
     */
    @Override
    public int deleteUmsWithdrawById(Long id) {
        return umsWithdrawMapper.deleteUmsWithdrawById(id);
    }

    @Override
    public String queryWithdrawMoney(Map map){
        return umsWithdrawMapper.queryWithdrawMoney(map);
    }

    @Override
    public String queryFrozenWithdrawMoney(Long customerId){
        return umsWithdrawMapper.queryFrozenWithdrawMoney(customerId);
    }
}
