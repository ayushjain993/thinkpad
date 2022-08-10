package io.uhha.member.service.impl;


import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.common.enums.AccountTypeEnum;
import io.uhha.common.enums.ImUserTypeEnum;
import io.uhha.common.redis.RedisMallHelper;
import io.uhha.common.utils.Utils;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.coin.user.mapper.UserCoinWalletMapper;
import io.uhha.common.utils.uuid.IdUtils;
import io.uhha.im.domain.ImUser;
import io.uhha.im.service.ImUserService;
import io.uhha.integral.domain.CustomerPoint;
import io.uhha.integral.service.CustomerPointService;
import io.uhha.marketing.domain.RegisterMarketing;
import io.uhha.marketing.service.CouponService;
import io.uhha.marketing.service.RegisterMarketingService;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.service.RegisterService;
import io.uhha.member.service.RegisterServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 注册服务聚合接口实现
 */
@Service
public class RegisterServiceApiImpl implements RegisterServiceApi {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(RegisterServiceApiImpl.class);

    /**
     * 注入注册服务
     */
    @Autowired
    private RegisterService registerService;

    /**
     * 注入注册营销服务
     */
    @Autowired
    private RegisterMarketingService registerMarketingService;

    /**
     * 注入会员积分服务
     */
    @Autowired
    private CustomerPointService customerPointService;

    /**
     * 注入优惠券服务
     */
    @Autowired
    private CouponService couponService;

    /**
     * 注入会员服务
     */
    @Autowired
    private IUmsMemberService customerService;

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    @Autowired
    private UserCoinWalletMapper userCoinWalletMapper;

    @Autowired
    private ImUserService imUserService;

    @Override
    @Transactional
    public int registerCustomer(String countryCode, String mobile, String password, String code, String recommondCode) {
        logger.debug("registerCustomer and\r\n mobile :{} \r\n code:{} \r\n recommondCode:{}", mobile, code, recommondCode);
        //注册用户
        int res = registerService.registerCustomer(countryCode, mobile, password, code, recommondCode);
        //注册成功后执行注册营销
        if (res <= 0) {
            return res;
        }
        //根据手机号查找用户
        UmsMember customer = customerService.queryCustomerByNameInWriteDataSource(mobile);
        //查找注册营销
        RegisterMarketing registerMarketing = registerMarketingService.queryRegisterMarketing();
        if (!ObjectUtils.isEmpty(registerMarketing)) {
            if (registerMarketing.isEffective()) {
                //赠送积分
                logger.info("registerCustomer success : present point");
                customerPointService.addCustomerPoint(CustomerPoint.buildForRegister(customer.getId(), registerMarketing.getPointNum()));
            }
            if (registerMarketing.isCanReceiveCoupon()) {
                //赠送优惠券
                logger.info("registerCustomer success : present coupon");
                couponService.receiveCoupon(customer.getId(), registerMarketing.getCouponId(), 2);
            }
        }

        // 初始化钱包
        List<SystemCoinType> fvirtualcointypes = redisCryptoHelper.getCoinTypeList();
        for (SystemCoinType fvirtualcointype : fvirtualcointypes) {
            UserCoinWallet coinWallet = new UserCoinWallet();
            coinWallet.setTotal(BigDecimal.ZERO);
            coinWallet.setFrozen(BigDecimal.ZERO);
            coinWallet.setBorrow(BigDecimal.ZERO);
            coinWallet.setIco(BigDecimal.ZERO);
            coinWallet.setCoinId(fvirtualcointype.getId());
            coinWallet.setUid(customer.getId());
            coinWallet.setGmtCreate(Utils.getTimestamp());
            coinWallet.setGmtModified(Utils.getTimestamp());
            if (userCoinWalletMapper.insert(coinWallet) <= 0) {
                logger.error("registerCustomer coinid={},coinname={} wallet creation failed.", fvirtualcointype.getId(), fvirtualcointype.getShortName());
            } else {
                logger.info("registerCustomer user coin wallet creation succeed.");
            }
        }
        logger.info("registerCustomer success.");

        //创建im用户
        boolean isExist = imUserService.checkExist(ImUserTypeEnum.PERSONAL, customer.getId());
        if (!isExist) {
            ImUser imUser = new ImUser();
            imUser.setUserCode(IdUtils.fastSimpleUUID());
            imUser.setUserId(customer.getId());
            imUser.setType(ImUserTypeEnum.PERSONAL.name());
            imUser.setNickName(customer.getNickname());
            imUser.setCreateBy(customer.getUsername());
            imUser.setCreateTime(new Date());
            imUser.setUpdateTime(new Date());
            imUserService.save(imUser);
        }
        return res;
    }
}
