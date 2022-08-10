package io.uhha.crypto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.common.Enum.*;
import io.uhha.coin.common.Enum.validate.BusinessTypeEnum;
import io.uhha.coin.common.Enum.validate.PlatformEnum;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.common.result.Result;
import io.uhha.coin.common.util.ArgsConstant;
import io.uhha.common.constant.Constant;
import io.uhha.coin.system.domain.SystemCoinSetting;
import io.uhha.coin.user.domain.FUserVirtualAddressWithdraw;
import io.uhha.coin.user.service.UserBankAddressService;
import io.uhha.common.utils.Utils;
import io.uhha.coin.market.domain.TickerData;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.coin.market.service.impl.AutoMarket;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.domain.SystemTradeType;
import io.uhha.coin.user.domain.FUserVirtualAddress;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.coin.user.service.UserCapitalService;
import io.uhha.coin.user.vo.WithdrawVirtualCapitalVo;
import io.uhha.validate.service.IPreBaseValidateService;
import io.uhha.validate.service.IValidityCheckService;
import io.uhha.common.notify.ValidateNotifyHelper;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.MessageUtils;
import io.uhha.common.utils.ip.IpUtils;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.coin.user.vo.UserVWAddressVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


@RequestMapping("/crypto")
@RestController
@Api(tags = "加密货币接口")
@Slf4j
public class UserCapitalController {

    @Autowired
    private UserBankAddressService userBankAddressService;

    @Autowired
    private AutoMarket autoMarket;

    /**
     * BTC网络手续费
     */
    private static final BigDecimal BTC_FEES[] = {new BigDecimal("0.0001"), new BigDecimal("0.0002")
            , new BigDecimal("0.0003"), new BigDecimal("0.0004"), new BigDecimal("0.0005")
            , new BigDecimal("0.0006"), new BigDecimal("0.0007"), new BigDecimal("0.0008")
            , new BigDecimal("0.0009"), new BigDecimal("0.0010")};

    private static final int BTC_FEES_MAX = 10;
    @Autowired
    private ValidateNotifyHelper validateNotifyHelper;
    @Autowired
    private UserCapitalService userCapitalService;
    @Autowired
    private IPreBaseValidateService preBaseValidateService;
    /**
     * 注入会员接口
     */
    @Autowired
    private IUmsMemberService customerService;

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;
    @Autowired
    private IValidityCheckService validityCheckService;

    /**
     * 密码工具类
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 获取用户加密货币资产
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "获取用户资产", notes = "GetUserCapital", response = AjaxResult.class)
    @GetMapping(value = "/getUserCryptoCapital")
    public AjaxResult getUserCryptoCapital(HttpServletRequest request) {
        try {
            UmsMember user = customerService.queryCustomerWithNoPasswordById(AppletsLoginUtils.getInstance().getCustomerId(request));

            JSONArray vwallet = new JSONArray();

            //获取虚拟币账户
            List<UserCoinWallet> walletList = userCapitalService.listVirtualWalletById(user.getId());
            for (UserCoinWallet coinWallet : walletList) {
                SystemCoinType coin = redisCryptoHelper.getCoinType(coinWallet.getCoinId());
                if (coin == null) {
                    continue;
                }
                JSONObject walletTemp = new JSONObject();
                walletTemp.put("fid", coinWallet.getId());
                walletTemp.put("fuid", coinWallet.getUid());
                walletTemp.put("ftotal", coinWallet.getTotal());
                walletTemp.put("ffrozen", coinWallet.getFrozen());
                walletTemp.put("fborrow", coinWallet.getBorrow());
                walletTemp.put("fupdatetime", coinWallet.getGmtModified());
                walletTemp.put("fshortname", coin.getShortName());
                walletTemp.put("flogo", coin.getAppLogo());
                walletTemp.put("fcoinName", coin.getName());
                walletTemp.put("isWithdraw", coin.getIsWithdraw());
                walletTemp.put("isRecharge", coin.getIsRecharge());
                walletTemp.put("status", coin.getStatus());
                vwallet.add(walletTemp);
            }

            //计算总资产
            BigDecimal totalAsset = getGrossAsset(walletList);

            //计算净资产
            BigDecimal asset = getNetAsset(walletList);

            JSONObject json = new JSONObject();
            json.put("wallet", vwallet);
            json.put("totalAsset", totalAsset);
            json.put("asset", asset);
            return AjaxResult.success("获取用户资产", json);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }

        return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
    }

    /**
     * 描述：获取用户总资产（虚拟币)
     *
     * @param hide 是否隐藏小额资产，默认值是0表示不隐藏，1表示隐藏总资产为0的资产
     * @return
     * @Author csj
     * @Version 1.0.2   增加小额资产隐藏参数
     */
    @ApiOperation(value = "获取用户总资产", notes = "GetUserAllCapital", response = AjaxResult.class)
    @GetMapping(value = "/getUserAllCapital")
    public AjaxResult getUserAllCapital(HttpServletRequest request,
                                        @RequestParam(value = "hide", defaultValue = "0") Integer hide
    ) {
        try {
            UmsMember user = customerService.queryCustomerWithNoPasswordById(AppletsLoginUtils.getInstance().getCustomerId(request));
            List<UserCoinWallet> userWalletList = userCapitalService.listVirtualWalletById(user.getId());
            JSONArray array = new JSONArray();
            for (UserCoinWallet uWallet : userWalletList
            ) {
                //  隐藏总资产=可用+冻结+理财 为0的资产
                BigDecimal total = uWallet.getTotal();
                BigDecimal frozen = uWallet.getFrozen();
                BigDecimal brrow = uWallet.getBorrow();
                if (hide != 0 && BigDecimal.ZERO.compareTo(total) == 0
                        && BigDecimal.ZERO.compareTo(frozen) == 0
                        && BigDecimal.ZERO.compareTo(brrow) == 0) {
                    continue;
                }
                JSONObject object = new JSONObject();
                SystemCoinType coinType = redisCryptoHelper.getCoinType(uWallet.getCoinId());
                object.put("id", uWallet.getId());
                object.put("uid", uWallet.getUid());
                object.put("coinId", uWallet.getCoinId());
                object.put("total", Utils.rvZeroAndDot(uWallet.getTotal().toPlainString()));
                object.put("frozen", Utils.rvZeroAndDot(uWallet.getFrozen().toPlainString()));
                object.put("borrow", Utils.rvZeroAndDot(uWallet.getBorrow().toPlainString()));
                object.put("coinName", uWallet.getCoinName());
                object.put("shortName", uWallet.getShortName());
                object.put("logo", uWallet.getLogo());
                object.put("isWithdraw", coinType.getIsWithdraw());
                object.put("isRecharge", coinType.getIsRecharge());
//                object.put("toCNY",transCNY(uWallet.getTotal(),uWallet.getCoinId()));
                array.add(object);
            }
            return AjaxResult.success("获取用户总资产", array);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
    }

    @ApiOperation(value = "获取用户交易资产", notes = "GetUserTradeCapital", response = AjaxResult.class)
    @GetMapping(value = "/getUserTradeCapital")
    public AjaxResult getUserTradeCapital(
            HttpServletRequest request,
            @RequestParam(value = "symbol", required = true) int symbol) {
        try {
            SystemTradeType tradeType = redisCryptoHelper.getTradeType(symbol);
            if (tradeType == null) {
                log.error("Currency doesn't exist!");
                return AjaxResult.error(MessageUtils.message("app.com.err.coin.0"));
            }
            UmsMember user = customerService.queryCustomerWithNoPasswordById(AppletsLoginUtils.getInstance().getCustomerId(request));
            //卖钱包
            UserCoinWallet buyallet = userCapitalService.getCoinWalletByUidAndId(user.getId(), tradeType.getBuyCoinId());
            if (buyallet == null) {
                log.error("Buy wallet doesn't exist!");
                return AjaxResult.error("Buy wallet doesn't exist!");
            }
            buyallet.setShortName(tradeType.getSellShortName());
            //卖钱包
            UserCoinWallet sellWallet = userCapitalService.getCoinWalletByUidAndId(user.getId(), tradeType.getSellCoinId());
            if (sellWallet == null) {
                log.error("Sell wallet doesn't exist!");
                return AjaxResult.error("Sell wallet doesn't exist!");
            }
            sellWallet.setShortName(tradeType.getSellShortName());

            //获取虚拟币账户
            List<UserCoinWallet> vwalletList = userCapitalService.listVirtualWalletById(user.getId());

            //计算总资产
            BigDecimal totalAsset = getGrossAsset(vwalletList);

            //计算净资产
            BigDecimal asset = getNetAsset(vwalletList);

            // 小数位处理(默认价格2位，数量4位)
            String digit = org.springframework.util.StringUtils.isEmpty(tradeType.getDigit()) ? "2#4" : tradeType.getDigit();
            String[] digits = digit.split("#");

            JSONObject wallet = new JSONObject();
            JSONObject vwallet = new JSONObject();
            wallet.put("fid", buyallet.getId());
            wallet.put("fuid", buyallet.getUid());
            wallet.put("ftotal", MathUtils.toScaleNum(buyallet.getTotal(), Integer.valueOf(digits[0])));
            wallet.put("ffrozen", buyallet.getFrozen());
            wallet.put("fborrow", buyallet.getBorrow());
            wallet.put("fupdatetime", buyallet.getGmtModified());
            vwallet.put("fid", sellWallet.getId());
            vwallet.put("fuid", sellWallet.getUid());
            vwallet.put("fcoinid", sellWallet.getCoinId());
            vwallet.put("ftotal", MathUtils.toScaleNum(sellWallet.getTotal(), Integer.valueOf(digits[1])));
            vwallet.put("ffrozen", sellWallet.getFrozen());
            vwallet.put("fborrow", sellWallet.getBorrow());
            vwallet.put("fupdatetime", sellWallet.getGmtModified());

            JSONObject json = new JSONObject();
            json.put("wallet", wallet);
            json.put("vwallet", vwallet);
            json.put("totalAsset", totalAsset);
            json.put("asset", asset);
            json.put("rmblever", 0);
            json.put("coinlever", 0);

            return AjaxResult.success("获取用户资产", json);
        } catch (Exception e) {
            log.info("获取用户资产{}", e.getMessage());
        }
        return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
    }


    @ApiOperation(value = "获取虚拟币充值地址列表", notes = "GetVCAddressList", response = AjaxResult.class)
    @GetMapping(value = "/getVCAddressList")
    public AjaxResult getVCAddressList(HttpServletRequest request) {
        try {
            UmsMember user = customerService.queryCustomerWithNoPasswordById(AppletsLoginUtils.getInstance().getCustomerId(request));

            List<FUserVirtualAddress> list = userBankAddressService.selectVirtualAddressByUser(user.getId());
            List<SystemCoinType> coinList = redisCryptoHelper.getCoinTypeIsRechargeList(SystemCoinTypeEnum.COIN.getCode());
            List<FUserVirtualAddress> newlist = new ArrayList<>();
            for (FUserVirtualAddress va : list) {
                SystemCoinType coin = redisCryptoHelper.getCoinType(va.getFcoinid());
                va.setFshortname(coin.getShortName());
                newlist.add(va);
                if (coin.getCoinType().equals(SystemCoinSortEnum.ETH.getCode())) {
                    //查询所有能充值的币种
                    for (SystemCoinType coinType : coinList) {
                        //如果不是ETC链上的资产或者是ETC资产都跳过
                        if (!coinType.getCoinType().equals(SystemCoinSortEnum.ETH.getCode()) || coinType.getId().equals(va.getFcoinid())) {
                            continue;
                        }
                        FUserVirtualAddress virtualAddress = new FUserVirtualAddress();
                        virtualAddress.setFid(va.getFid());
                        virtualAddress.setFadderess(va.getFadderess());
                        virtualAddress.setFcoinid(coinType.getId());
                        virtualAddress.setFshortname(coinType.getShortName());
                        virtualAddress.setFcreatetime(va.getFcreatetime());
                        virtualAddress.setFuid(va.getFuid());
                        newlist.add(virtualAddress);
                    }
                }

            }
            return AjaxResult.success("获取虚拟币充值地址列表", newlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
    }


    @ApiOperation(value = "获取虚拟币充值地址", notes = "GetVCAddress", response = AjaxResult.class)
    @GetMapping(value = "/getVCAddress")
    public AjaxResult getVCAddress(HttpServletRequest request,
                                   @RequestParam(value = "coinid", required = true) int coinid) {
        UmsMember user = customerService.queryCustomerWithNoPasswordById(AppletsLoginUtils.getInstance().getCustomerId(request));

        if (user == null) {
            log.error("User was not found!");
            AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }

        SystemCoinType coin = redisCryptoHelper.getCoinType(coinid);
        if (coin == null || !coin.getIsRecharge()) {
            log.error("Currency doesn't exist in redis. check redis availability!");
            return AjaxResult.error("Currency doesn't exist!");
        }
        // 充值地址
        FUserVirtualAddress rechargeAddress;
        rechargeAddress = this.userBankAddressService.selectVirtualAddressByUserAndCoin(user.getId(), coin.getId());

        if (rechargeAddress == null) {
            log.error("rechargeAddress was not found!");
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
        JSONObject object = null;

        object = new JSONObject();
        object.put("fid", rechargeAddress.getFid());
        object.put("fcoinid", rechargeAddress.getFcoinid());
        object.put("fuid", rechargeAddress.getFuid());
        object.put("fadderess", rechargeAddress.getFadderess());
        object.put("fcreatetime", rechargeAddress.getFcreatetime());
        object.put("fshortname", rechargeAddress.getFshortname());
        return AjaxResult.success("获取虚拟币充值地址", object);
    }


    /**
     * 获取虚拟币充值地址
     *
     * @param coinid 虚拟币id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/withdraw/coin_address")
    public AjaxResult getVirtualAddress(
            HttpServletRequest request,
            @RequestParam("coinid") int coinid) throws Exception {
        //LoginUser
        UmsMember user = customerService.queryCustomerInfoById(AppletsLoginUtils.getInstance().getCustomerId(request));
        if (user == null) {
            log.error("User was not found!");
            AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }

        //IP
        String ip = IpUtils.getIpAddr(request);
        user.setIp(ip);

        SystemCoinType coinType = redisCryptoHelper.getCoinType(coinid);
        if (coinType == null || !coinType.getIsRecharge() || SystemCoinStatusEnum.ABNORMAL.getCode().equals(coinType.getStatus())) {
            log.error("Currency doesn't exist in redis. check redis availability!");
            return AjaxResult.error("Current currency suspended recharge!");
        }
        FUserVirtualAddress fuserVirtualaddress = null;
        //以太坊类地址获取
        if (coinType.getCoinType().equals(SystemCoinSortEnum.ETH.getCode())) {
            //获取到用户以太坊钱包地址
            SystemCoinType coin = redisCryptoHelper.getCoinTypeShortName("ETH");
            if (coin == null) {
                log.error("ETH doesn't exist in redis. check redis availability!");
                return AjaxResult.error(MessageUtils.message("com.capital.error.10014"));
            }
            fuserVirtualaddress = this.userBankAddressService.insertETHVirtualCoinAddress(user.getId(), coinType.getId(), ip, coin.getId());
        } else {
            fuserVirtualaddress = this.userBankAddressService.insertVirtualCoinAddress(user.getId(), coinType.getId(), ip);
        }
        if (fuserVirtualaddress == null) {
            log.error("getVirtualAddress failed. fuserVirtualaddress is null!");
            return AjaxResult.error(MessageUtils.message("com.capital.error.10014"));
        }
        return AjaxResult.success(fuserVirtualaddress.getFadderess());
    }

    @ApiOperation(value = "添加用户虚拟提现地址", notes = "AddUserVWAddress", response = AjaxResult.class)
    @PostMapping(value = "/addUserVWAddress")
    public AjaxResult addUserVWAddress(
            HttpServletRequest request,
            @RequestBody UserVWAddressVo userVWAddressVo) {
        try {
            //LoginUser
            UmsMember user = customerService.queryCustomerInfoById(AppletsLoginUtils.getInstance().getCustomerId(request));

            //IP
            String ip = IpUtils.getIpAddr(request);
            user.setIp(ip);

            String coinaddress = userVWAddressVo.getAddress();
            int coinid = userVWAddressVo.getCoinid();
            String remark = userVWAddressVo.getRemark();
            String pcode = userVWAddressVo.getPcode();
            String gcode = userVWAddressVo.getGcode();

            if (!(coinaddress.length() >= 20 && coinaddress.length() <= 34) && coinaddress.length() != 42) {
                return AjaxResult.error("请输入正确的提现地址");
            }
            if (coinaddress.length() == 42) {
                String withdarwheader = coinaddress.substring(0, 2);
                if (!withdarwheader.equals("0x")) {
                    return AjaxResult.error("请输入正确的提现地址");
                }
            }
            //验证币种
            SystemCoinType coin = redisCryptoHelper.getCoinType(coinid);
            if (coin == null) {
                log.error("Currency: {} doesn't exist!", coinid);
                return AjaxResult.error(MessageUtils.message("app.com.err.coin.0"));
            }

            //谷歌、手机验证
            if (preBaseValidateService.validatePhoneBindAndGoogleBind(user)) {
                log.error("You did not bind the phone or Google authentication!");
                return AjaxResult.error(MessageUtils.message("app.com.err.auth.0"));
            }

            //FIXME in production
//            Result validityResult = validityCheckService.getChangeCheck(user, pcode,
//                    BusinessTypeEnum.SMS_COIN_WITHDRAW_ACCOUNT.getCode(), gcode, ip, PlatformEnum.BC.getCode());
//            if (!validityResult.getSuccess()) {
//                log.error(MessageUtils.message("app.com.validate.err." + validityResult.getCode(), validityResult.getData()));
//                return AjaxResult.error(MessageUtils.message("app.com.validate.err." + validityResult.getCode(), validityResult.getData()));
//            }
            //
            FUserVirtualAddressWithdraw fvirtualaddress = new FUserVirtualAddressWithdraw();
            fvirtualaddress.setFcoinid(coinid);
            fvirtualaddress.setFuid(user.getId());
            fvirtualaddress.setFadderess(coinaddress);
            fvirtualaddress.setFremark(remark);
            fvirtualaddress.setFcreatetime(Utils.getTimestamp());
            fvirtualaddress.setInit(true);
            fvirtualaddress.setVersion(0);
            boolean insertResult = userBankAddressService.insertVirtualCoinWithdrawAddress(fvirtualaddress, ip);
            if (insertResult) {
                log.info("Successful operation!");
                return AjaxResult.success(MessageUtils.message("app.com.suc.0"));
            }
            log.error("Operation failed!");
            return AjaxResult.error(MessageUtils.message("app.com.err.1"));
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
    }


    @ApiOperation(value = "获取用户虚拟提现地址列表", notes = "GetUserVWAddress", response = AjaxResult.class)
    @GetMapping(value = "/getUserVWAddress")
    public AjaxResult getUserVWAddress(
            HttpServletRequest request,
            @RequestParam(value = "coinid", required = true) int coinid) {
        try {
            //LoginUser
            UmsMember user = customerService.queryCustomerInfoById(AppletsLoginUtils.getInstance().getCustomerId(request));

            //IP
            String ip = IpUtils.getIpAddr(request);
            user.setIp(ip);

            FUserVirtualAddressWithdraw address = new FUserVirtualAddressWithdraw();
            address.setFcoinid(coinid);
            address.setFuid(user.getId());
            address.setInit(true);
            List<FUserVirtualAddressWithdraw> list = userBankAddressService.selectVirtualCoinWithdrawAddressList(address);

            return AjaxResult.success("获取用户虚拟提现地址列表", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
    }

    @ApiOperation(value = "用户虚拟币提现", notes = "WithdrawUserVirtualWallet", response = AjaxResult.class)
    @PostMapping(value = "/withdrawVirtualCapital")
    public AjaxResult withdrawVirtualCapital(
            HttpServletRequest request,
            @RequestBody WithdrawVirtualCapitalVo withdrawVirtualCapitalVo) {

        Integer addressid = withdrawVirtualCapitalVo.getAddressid();
        BigDecimal withdrawAmount = withdrawVirtualCapitalVo.getWithdrawAmount();
        String tradePwd = withdrawVirtualCapitalVo.getTradePwd();
        String totpCode = withdrawVirtualCapitalVo.getGoogleCode();
        String phoneCode = withdrawVirtualCapitalVo.getPhoneCode();
        Integer coinid = withdrawVirtualCapitalVo.getCoinid();
        Integer btcfeesIndex = withdrawVirtualCapitalVo.getBtcfees();


        try {
            withdrawAmount = MathUtils.toScaleNum(withdrawAmount, MathUtils.ENTER_COIN_SCALE);
            //LoginUser
            long userId = AppletsLoginUtils.getInstance().getCustomerId(request);
            UmsMember user = customerService.queryCustomerWithCustomerLevel(userId);
            UmsMember user1 = customerService.queryCustomerInfoById(userId);
            //IP
            String ip = IpUtils.getIpAddr(request);
            user.setIp(ip);

            Result result = preBaseValidateService.validateCapital(user, "coin");
            if (!result.getSuccess()) {
                log.error("提现验证失败，code:{}--msg:{}", result.getCode(), result.getMsg());
                return AjaxResult.error(MessageUtils.message("app.com.validate.err." + result.getCode()));
            }

            //验证币种
            SystemCoinType coin = redisCryptoHelper.getCoinType(coinid);
            if (coin == null) {
                log.error("Currency doesn't exist!");
                return AjaxResult.error(MessageUtils.message("app.com.err.coin.0"));
            }
            if (!coin.getIsWithdraw()) {
                log.error("Current virtual currency is not allowed to withdraw cash operation!");
                return AjaxResult.error(MessageUtils.message("app.com.err.vwd.0"));
            }
            //非会员名称为0
            String level = "0".equalsIgnoreCase(user.getCustomerLevel().getName()) ? "0" : user.getCustomerLevel().getName();
            SystemCoinSetting fee = redisCryptoHelper.getCoinSetting(coin.getId(), level);
            if (null == fee) {
                log.error("Current virtual currency fee setting is wrong!");
                return AjaxResult.error(MessageUtils.message("app.com.err.coin.0"));
            }
            // 最大最小提现虚拟币
            if (withdrawAmount.compareTo(fee.getWithdrawMin()) < 0 && fee.getWithdrawMin().compareTo(BigDecimal.ZERO) > 0) {
                log.error("current withdraw volume is less than the allow volume {}", fee.getWithdrawMin());
                return AjaxResult.error("最小提现" + fee.getWithdrawMin());
            }
            if (withdrawAmount.compareTo(fee.getWithdrawMax()) > 0 && fee.getWithdrawMax().compareTo(BigDecimal.ZERO) > 0) {
                log.error("current withdraw volume is more than the allow volume {}", fee.getWithdrawMax());
                return AjaxResult.error("最大提现" + fee.getWithdrawMax());
            }

            FUserVirtualAddressWithdraw address = userBankAddressService.selectVirtualCoinWithdrawAddressById(addressid);
            if (address == null || !address.getInit()) {
                log.error("Illegal request addressId ={}!", addressid);
                return AjaxResult.error(MessageUtils.message("app.com.err.0"));
            }

            //密码不正确
            if (!passwordEncoder.matches(tradePwd, user1.getPaypassword())) {
                log.error("Invalid pay password");
                return AjaxResult.error(MessageUtils.message("app.com.err.trade.6"));
            }

            //google和短信验证
            Result validityResult = validityCheckService.getChangeCheck(user, phoneCode,
                    BusinessTypeEnum.SMS_COIN_WITHDRAW.getCode(), totpCode, ip, PlatformEnum.UHHA.getCode());
            if (!validityResult.getSuccess()) {
                log.info("短信或谷歌验证错误：code:{}----msg:{}", validityResult.getCode(), validityResult.getMsg());
                return AjaxResult.error(MessageUtils.message("app.com.validate.err." + validityResult.getCode(), validityResult.getData()));
            }

            int time = userCapitalService.selectVirtualWalletWithdrawTimes(user.getId());
            if (time >= Constant.VirtualCoinWithdrawTimes) {
                log.error("Daily virtual currency withdrawal most");
                return AjaxResult.error(MessageUtils.message("app.com.err.16") + Constant.VirtualCoinWithdrawTimes + MessageUtils.message("app.com.err.9"));
            }

            // 用户钱包
            UserCoinWallet fvirtualwallet = userCapitalService.getCoinWalletByUidAndId(user.getId(), coinid);
            // 余额不足
            if (fvirtualwallet == null || fvirtualwallet.getTotal().compareTo(withdrawAmount) < 0) {
                log.error("The withdrawal amount must not exceed the account balance!");
                return AjaxResult.error(MessageUtils.message("app.com.err.bkd.4"));
            }
            // BTC网络手续费
            BigDecimal BTCFees = coin.getNetworkFee();
            if (coin.getShortName().equals("BTC")) {
                if (btcfeesIndex >= 0 && btcfeesIndex <= BTC_FEES_MAX) {
                    BTCFees = BTC_FEES[btcfeesIndex];
                }
            }
            boolean i = userCapitalService.insertVirtualCapitalOperation(user, coinid, address,
                    fvirtualwallet.getId(), withdrawAmount, BTCFees, DataSourceEnum.APP, PlatformEnum.UHHA);

            if (i) {
                return AjaxResult.success(MessageUtils.message("app.com.err.vwd.6"));
            }
            return AjaxResult.error(MessageUtils.message("app.com.err.1"));
        } catch (Exception e) {
            log.info("用户虚拟币提现:{}", e.getMessage());
            return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
        }
    }


    @ApiOperation(value = "撤销虚拟币提现", notes = "CancelWithdrawCoin", response = AjaxResult.class)
    @PostMapping(value = "/cancelWithdrawVirtualCapital")
    public AjaxResult cancelWithdrawVirtualCapital(
            HttpServletRequest request,
            @RequestParam(value = "fid", required = true) int fid) throws Exception {
        UmsMember user = customerService.queryCustomerInfoById(AppletsLoginUtils.getInstance().getCustomerId(request));
        //获取IP
        String ip = IpUtils.getIpAddr(request);
        user.setIp(ip);
        FVirtualCapitalOperation fvirtualcaptualoperation = userCapitalService.selectVirtualCapitalOperationById(fid);
        if (fvirtualcaptualoperation != null && fvirtualcaptualoperation.getFuid().equals(user.getId()) &&
                fvirtualcaptualoperation.getFtype().equals(VirtualCapitalOperationTypeEnum.COIN_OUT.getCode()) &&
                fvirtualcaptualoperation.getFstatus().equals(VirtualCapitalOperationOutStatusEnum.WaitForOperation)) {
            try {
                fvirtualcaptualoperation.setFstatus(CapitalOperationOutStatus.Cancel);
                fvirtualcaptualoperation.setFupdatetime(new Date());
                this.userCapitalService.updateVirtualCancelOperationWithdraw(user, fvirtualcaptualoperation);
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
            }
        } else {
            return AjaxResult.error("操作失败");
        }
        return AjaxResult.success("撤销成功！");
    }


    @ApiOperation(value = "获取充值须知", notes = "getRechargeNotes", response = AjaxResult.class)
    @GetMapping(value = "/getRechargeNotes")
    public AjaxResult getRechargeNotes(@RequestParam("coinid") int coinid) {

        SystemCoinType coin = redisCryptoHelper.getCoinType(coinid);
        Map<String, Object> map = new HashMap<>();
        String phone = redisCryptoHelper.getSystemArgs(ArgsConstant.telephone);
        String minRechargeMount = "0.0001";
        try {
            String hlp_min_recharge_mount = redisCryptoHelper.getSystemArgs(coin.getShortName() + "_min_recharge_mount");
            if (hlp_min_recharge_mount != null) {
                minRechargeMount = hlp_min_recharge_mount;
            }
        } catch (Exception e) {
        }
        map.put("wt1", MessageUtils.message("financial.recharge.notice4").replace("{0}", phone));
        map.put("wt2", MessageUtils.message("financial.recharge.notice5").replace("{0}", coin.getShortName()));
        map.put("wt3", MessageUtils.message("financial.recharge.notice6").replace("{0}", coin.getShortName()).replace("{1}", minRechargeMount));
        map.put("wt4", "");
        return AjaxResult.success("充值须知", map);
    }

    @ApiOperation(value = "获取可充值币种信息", notes = "getIsRechargeCoinInfo", response = AjaxResult.class)
    @GetMapping(value = "/getIsRechargeCoinInfo")
    public AjaxResult getIsRechargeCoinInfo(@RequestParam("coinid") int coinid) {
        List<SystemCoinType> coinList = redisCryptoHelper.getCoinTypeIsRechargeList(SystemCoinTypeEnum.COIN.getCode());
        for (SystemCoinType coin : coinList) {
            if (coinid == coin.getId()) {
                JSONObject item = new JSONObject();
                item.put("id", coin.getId());
                item.put("name", coin.getName());
                item.put("shortname", coin.getShortName());
                item.put("miniCount", coin.getMinCount());
                item.put("networkFee", coin.getNetworkFee());
                return AjaxResult.success(item);
            }
        }
        return AjaxResult.error("not found");
    }

    /**
     * 获取用户虚拟币操作记录
     *
     * @param request
     * @param pageNum
     * @param pageSize
     * @param type
     * @param coinid
     * @return
     */
    @ApiOperation(value = "获取用户虚拟币操作记录", notes = "GetUserVWOperationRecord", response = AjaxResult.class)
    @GetMapping(value = "/getUserVWOperationRecord")
    public AjaxResult getUserVWOperationRecord(
            HttpServletRequest request,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "coinid") Integer coinid) {
        try {
            UmsMember user = customerService.queryCustomerInfoById(AppletsLoginUtils.getInstance().getCustomerId(request));

            FVirtualCapitalOperation operation = new FVirtualCapitalOperation();
            operation.setFuid(user.getId());
            operation.setFtype(type);
            operation.setFcoinid(coinid);

            Pagination<FVirtualCapitalOperation> page = new Pagination<FVirtualCapitalOperation>(pageNum, pageSize);

            page = userCapitalService.selectVirtualCapitalOperation(page, operation);

            return AjaxResult.success("获取用户虚拟币操作记录", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error(MessageUtils.message("app.com.err.net.0"));
    }


    /**
     * 获取总资产
     *
     * @param userCoinWallets
     * @return
     */
    private BigDecimal getGrossAsset(List<UserCoinWallet> userCoinWallets) {
        if (userCoinWallets == null || userCoinWallets.size() <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal last, vtotal, total = BigDecimal.ZERO;
        Map<Integer, Integer> coinIdToTradeId = redisCryptoHelper.getCoinIdToTradeId();
        for (UserCoinWallet userCoinWallet : userCoinWallets) {
            Object tradeId = coinIdToTradeId.get(userCoinWallet.getCoinId());
            vtotal = MathUtils.add(userCoinWallet.getTotal(), userCoinWallet.getFrozen());
            if (tradeId != null) {
                vtotal = MathUtils.add(vtotal, userCoinWallet.getBorrow());
                last = getLastPrice(Integer.valueOf(tradeId.toString()));
                vtotal = MathUtils.mul(vtotal, last);
            }
            total = MathUtils.add(total, vtotal);
        }
        return MathUtils.toScaleNum(total, MathUtils.DEF_USD_SCALE);
    }

    /**
     * 获取净资产
     *
     * @param userCoinWallets
     * @return
     */
    private BigDecimal getNetAsset(List<UserCoinWallet> userCoinWallets) {
        if (userCoinWallets == null || userCoinWallets.size() <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal last, vtotal;
        Map<Integer, Integer> coinIdToTradeId = redisCryptoHelper.getCoinIdToTradeId();
        for (UserCoinWallet userCoinWallet : userCoinWallets) {
            Object tradeId = coinIdToTradeId.get(userCoinWallet.getCoinId());
            vtotal = MathUtils.add(userCoinWallet.getTotal(), userCoinWallet.getFrozen());
            if (tradeId != null) {
                last = getLastPrice(Integer.valueOf(tradeId.toString()));
                vtotal = MathUtils.mul(vtotal, last);
            }
            total = MathUtils.add(vtotal, total);
        }
        return MathUtils.toScaleNum(total, MathUtils.DEF_USD_SCALE);
    }

    /**
     * 实时价格
     *
     * @param tradeId 交易ID
     * @return BigDecimal
     */
    private BigDecimal getLastPrice(int tradeId) {
        TickerData data = autoMarket.getTickerData(tradeId);
        return data.getLast();
    }

}
