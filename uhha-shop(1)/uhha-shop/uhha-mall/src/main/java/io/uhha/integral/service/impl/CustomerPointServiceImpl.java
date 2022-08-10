package io.uhha.integral.service.impl;


import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.capital.mapper.FVirtualCapitalOperationMapper;
import io.uhha.coin.common.Enum.VirtualCapitalOperationOutStatusEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.coin.system.domain.FSysCoinOperation;
import io.uhha.coin.system.domain.FSysCoinWallet;
import io.uhha.coin.system.service.IFSysCoinOperationService;
import io.uhha.coin.system.service.IFSysCoinWalletService;
import io.uhha.coin.user.domain.FUserVirtualAddress;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.coin.user.mapper.FUserVirtualAddressMapper;
import io.uhha.coin.user.mapper.UserCoinWalletMapper;
import io.uhha.coin.user.service.IAdminUserCapitalService;
import io.uhha.coin.user.service.UserBankAddressService;
import io.uhha.common.enums.CryptoTypeEnum;
import io.uhha.common.exception.BCException;
import io.uhha.common.exception.ServiceException;
import io.uhha.common.notify.MallNotifyHelper;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.Utils;
import io.uhha.integral.domain.CustomerPoint;
import io.uhha.integral.domain.PointSetting;
import io.uhha.integral.domain.PointSignRule;
import io.uhha.integral.mapper.CustomerPointMapper;
import io.uhha.integral.service.CustomerPointService;
import io.uhha.integral.service.PointSettingService;
import io.uhha.integral.service.PointSignRuleService;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.domain.UmsPreDepositRecord;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.vo.UmsRedeemVo;
import io.uhha.order.domain.OmsCommissionRecords;
import io.uhha.util.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by mj on 17/5/25.
 * 会员积分服务接口实现
 */
@Service
public class CustomerPointServiceImpl implements CustomerPointService {

    /**
     * 调试日志
     */
    private Logger logger = LoggerFactory.getLogger(CustomerPointServiceImpl.class);

    /**
     * 注入会员积分数据库接口
     */
    @Autowired
    private CustomerPointMapper customerPointMapper;

    @Autowired
    private UserCoinWalletMapper userCoinWalletMapper;
    @Autowired
    private IFSysCoinOperationService  sysCoinOperationService;

    @Autowired
    private IFSysCoinWalletService fSysCoinWalletService;

    /**
     * 注入会员服务
     */
    @Autowired
    private IUmsMemberService customerService;

    /**
     * 注入签到积分规则服务
     */
    @Autowired
    private PointSignRuleService pointSignRuleService;

    @Autowired
    private MallNotifyHelper mallNotifyHelper;

    /**
     * 密码工具类
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PointSettingService pointSettingService;

    @Autowired
    private UserBankAddressService userBankAddressService;

    @Autowired
    private FVirtualCapitalOperationMapper virtualCapitalOperationMapper;

    @Override
    public PageHelper<CustomerPoint> queryCustomerPoints(PageHelper<CustomerPoint> pageHelper, long customerId, String sourceType,String type) {
        logger.debug("queryCustomerPoints and pageHelper:{} \r\n customerId:{}", pageHelper, customerId);
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("sourceType", sourceType);
        params.put("type", type);
        return pageHelper.setListDates(customerPointMapper.queryCustomerPoints(pageHelper.getQueryParams(params, customerPointMapper.queryCustomerPointCount(params))));
    }

    @Override
    public int queryCustomerPointCount(long customerId) {
        logger.debug("queryCustomerPointCount and customerId:{}", customerId);
        Integer point = customerPointMapper.queryCustomerPointAllCount(customerId);
        return Objects.isNull(point) ? 0 : point;
    }

    @Override
    public List<CustomerPoint> queryCustomerPointGroupByType(long customerId){
        return customerPointMapper.queryCustomerPointGroupByType(customerId);
    }
    @Override
    public int addCustomerPoint(CustomerPoint customerPoint) {
        logger.debug("addCustomerPoint and customerPoint:{}", customerPoint);

        if (Objects.isNull(customerPoint)) {
            logger.error("addCustomerPoint fail due to params is null...");
            return 0;
        }
        Integer point = customerPointMapper.queryCustomerPointAllCount(customerPoint.getCustomerId());
        if (!customerPoint.checkChangePoint(point == null ? 0 : point)) {
            logger.error("addCustomerPoint fail:will be minus");
            return -1;
        }
        return customerPointMapper.addCustomerPoint(customerPoint);
    }

    @Override
    public int isTodaySign(long customerId) {
        logger.debug("isTodaySign and customerId:{}", customerId);
        CustomerPoint customerPoint = customerPointMapper.queryLastSign(customerId);
        if (ObjectUtils.isEmpty(customerPoint)) {
            logger.info("isTodaySign : not sign");
            return 1;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (Integer.parseInt(customerPoint.getCreateTime().format(dateTimeFormatter)) >= Integer.parseInt(LocalDateTime.now().format(dateTimeFormatter))) {
            logger.error("isTodaySign : already sign");
            return -1;
        }
        return 1;
    }


    @Override
    @Transactional
    public int addSignRecord(long customerId, Consumer<Integer> consumer) {
        logger.debug("addSignRecord and customerId:{}", customerId);
        CustomerPoint lastSign = customerPointMapper.queryLastSign(customerId);
        PointSignRule pointSignRule = pointSignRuleService.queryPointSignRule();
        if (ObjectUtils.isEmpty(pointSignRule) || !pointSignRule.isOpen()) {
            logger.error("addSignRecord fail:pointSignRule is empty or not used");
            return -1;
        }
        //没有签到记录，开始签到
        if (ObjectUtils.isEmpty(lastSign)) {
            logger.info("addSignRecord:no signRecord");
            return signFirstDay(pointSignRule.getRuleInfos(), customerId, consumer);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //最后一次签到日期大于等于现在，标识已经签到过
        if (Integer.parseInt(lastSign.getCreateTime().format(dateTimeFormatter)) >= Integer.parseInt(LocalDateTime.now().format(dateTimeFormatter))) {
            logger.error("addSignRecord : already sign");
            return -2;
        }
        //最后一次签到日期是昨天，连续签到
        if (Integer.parseInt(lastSign.getCreateTime().format(dateTimeFormatter)) + 1 == Integer.parseInt(LocalDateTime.now().format(dateTimeFormatter))) {
            UmsMember customer = customerService.queryCustomerWithNoPasswordById(customerId);
            return signContinue(pointSignRule.getRuleInfos(), customerId, customer.getSignNum(), consumer);
        } else {
            //断签，重新开始签到
            logger.info("addSignRecord: not continue");
            return signFirstDay(pointSignRule.getRuleInfos(), customerId, consumer);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int redeemToken(long userId,UmsRedeemVo redeemVo) {
        logger.debug("redeemToken and UmsRedeemVo:{}", redeemVo);

        // 查询用户信息
        UmsMember customer = customerService.queryCustomerInfoById(userId);

        if (Objects.isNull(customer)) {
            logger.error("redeemToken fail due to member is null ");
            return -1;
        }

        if (Objects.isNull(customer.getPaypassword())) {
            logger.error("redeemToken fail due to member payPassword is null ");
            return -2;
        }

        // 校验用户输入的支付密码是否正确
        if (!passwordEncoder.matches(redeemVo.getPassword(), customer.getPaypassword())) {
            logger.error("redeemToken fail due to password is error...");
            return -3;
        }

        //查询当前的积分值
        BigDecimal pointBalance = new BigDecimal(queryCustomerPointCount(userId));

        //检查积分配置信息
        PointSetting pointSetting = pointSettingService.queryPointSetting();
        if(pointSetting==null ||!pointSetting.checkRedeemSetting()){
            logger.error("queryPointSetting fail due to no pointSetting in database... or invalid redeem setting");
            return -6;
        }
        BigDecimal maxRedeemToken = new BigDecimal( pointSetting.getMaxRedeemToken());
        BigDecimal minReemToken =  new BigDecimal(pointSetting.getMinRedeemToken());
        BigDecimal offsetToken = new BigDecimal(pointSetting.getOffsetToken());

        //所提额小于最小提取额
        if(redeemVo.getToken().compareTo(minReemToken)<0){
            logger.error("redeemToken redeem token number is less than min redeem setting");
            return -7;
        }
        //所提额大于于最大提取额
        if(redeemVo.getToken().compareTo(maxRedeemToken)>0){
            logger.error("redeemToken redeem token number is bigger than max redeem setting");
            return -8;
        }

        BigDecimal requiredPoints = MathUtils.mul(redeemVo.getToken(),offsetToken);
        //所需的积分数大于积分余额
        if(requiredPoints.compareTo(pointBalance)>0){
            logger.error("redeemToken required points is bigger than point balance. not enough balance to redeem.");
            return -5;
        }

        //减少积分数量
        if (addCustomerPoint(CustomerPoint.buildForRedeemOrder(userId, requiredPoints.intValue())) < 1) {
            logger.error("redeemToken fail :addCustomerPoint fail");
            return -9;
        }
        //增加UHHA余额
        FUserVirtualAddress address =userBankAddressService.selectVirtualAddressByUserAndCoin(userId,CryptoTypeEnum.UHHA.getCoinId());
        if(ObjectUtils.isEmpty(address)){
            logger.error("redeemToken fail :selectVirtualAddressByUserAndCoin fail, no address for UHHA");
            return -4;
        }
        UserCoinWallet virtualwalletTo = userCoinWalletMapper.selectByUidAndCoinLock(userId, CryptoTypeEnum.UHHA.getCoinId());
        if (virtualwalletTo == null) {
            return -4;
        }

        FVirtualCapitalOperation operation = FVirtualCapitalOperation.buildForRedeem(userId, CryptoTypeEnum.UHHA.getCoinId(),redeemVo.getToken(),address.getFadderess(), PlatformEnum.UHHA.getCode());
        if (virtualCapitalOperationMapper.insert(operation) <= 0) {
            logger.error("redeemToken fail :virtualCapitalOperationMapper insert fail");
            return -9;
        }
        // 更新用户UHHA钱包信息
        virtualwalletTo.setTotal(MathUtils.add(virtualwalletTo.getTotal(), operation.getFamount()));
        virtualwalletTo.setGmtModified(Utils.getTimestamp());
        if (userCoinWalletMapper.updateByPrimaryKey(virtualwalletTo) <= 0) {
            logger.error("redeemToken fail :updateByPrimaryKey update fail");
            return -9;
        }
        //判断系统余额时候够，并更改系统钱包余额
        FSysCoinWallet sysCoinWallet = fSysCoinWalletService.getSysCoinWalletByCoinId(CryptoTypeEnum.UHHA.getCoinId());
        if(sysCoinWallet.getTotal().compareTo(redeemVo.getToken())<0){
            logger.error("redeemToken fail :not enough balance in sys wallet");
            return -10;
        }
        sysCoinWallet.setTotal(MathUtils.sub(sysCoinWallet.getTotal(),redeemVo.getToken()));
        sysCoinWallet.setGmtCreate(DateUtils.getNowDate());
        //减少系统钱包UHHA余额
        fSysCoinWalletService.updateFSysCoinWallet(sysCoinWallet);
        //记录本次操作日志
        FSysCoinOperation sysCoinOperation = FSysCoinOperation.builder()
                .fadminid(0L)
                .famount(redeemVo.getToken())
                .ftoaddress(address.getFadderess())
                .fcreatetime(Utils.getTimestamp())
                .fcoinid(CryptoTypeEnum.UHHA.getCoinId())
                .fstatus(VirtualCapitalOperationOutStatusEnum.OperationSuccess)
                .version(1)
                .build();
        sysCoinOperationService.insertFSysCoinOperation(sysCoinOperation);

        //发送通知
        mallNotifyHelper.redeemPointsToToken(redeemVo,userId);

        return 1;
    }

    /**
     * 开始签到
     *
     * @param ruleInfos  规则详细
     * @param customerId 用户id
     * @return 1:成功
     */
    private int signFirstDay(List<PointSignRule.RuleInfo> ruleInfos, long customerId, Consumer<Integer> consumer) {
        if (!CollectionUtils.isEmpty(ruleInfos)) {
            customerService.updateSignNum(customerId, false);
            PointSignRule.RuleInfo fistDayRule = ruleInfos.stream().filter(ruleInfo -> ruleInfo.filterDay(1)).findFirst().orElse(null);
            return addSignByRule(fistDayRule, customerId, consumer);
        }
        return -1;
    }

    /**
     * 连续签到
     *
     * @param ruleInfos      规则详细
     * @param customerId     用户id
     * @param alreadySignNum 已经签到的天数
     * @return 1:成功
     */
    private int signContinue(List<PointSignRule.RuleInfo> ruleInfos, long customerId, int alreadySignNum, Consumer<Integer> consumer) {
        if (!CollectionUtils.isEmpty(ruleInfos)) {
            PointSignRule.RuleInfo lastDayRule = ruleInfos.stream().sorted(Comparator.reverseOrder()).findFirst().get();
            //如果已经签到的天数大于等于规则最大天数，则重新开始签到
            if (lastDayRule.getDay() <= alreadySignNum) {
                return signFirstDay(ruleInfos, customerId, consumer);
            } else {
                //连续签到
                customerService.updateSignNum(customerId, true);
                PointSignRule.RuleInfo continueDayRule = ruleInfos.stream().filter(ruleInfo -> ruleInfo.filterDay(alreadySignNum + 1)).findFirst().orElse(null);
                return addSignByRule(continueDayRule, customerId, consumer);
            }
        }
        return -1;
    }

    /**
     * 按规则增加积分记录
     *
     * @param ruleInfo   规则
     * @param customerId 用户id
     * @param consumer   回调
     * @return 1:成功
     */
    private int addSignByRule(PointSignRule.RuleInfo ruleInfo, long customerId, Consumer<Integer> consumer) {
        if (!ObjectUtils.isEmpty(ruleInfo)) {
            consumer.accept(ruleInfo.getPoint());
            return customerPointMapper.addCustomerPoint(CustomerPoint.buildForSignIn(customerId, ruleInfo.getPoint()));
        } else {
            consumer.accept(0);
            return customerPointMapper.addCustomerPoint(CustomerPoint.buildForSignIn(customerId, 0));
        }
    }
}


