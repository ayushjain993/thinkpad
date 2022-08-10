package io.uhha.web.controller.crypto;


import io.swagger.annotations.Api;
import io.uhha.coin.capital.domain.FVirtualCapitalOperation;
import io.uhha.coin.common.Enum.*;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinDriverFactory;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.coin.common.util.ArgsConstant;
import io.uhha.coin.common.util.CommonHelper;
import io.uhha.common.constant.Constant;
import io.uhha.common.utils.Utils;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.service.IFSysCoinWalletService;
import io.uhha.coin.user.service.IAdminUserCapitalService;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.domain.model.LoginUser;
import io.uhha.web.utils.AdminLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "加密货币资产操作Controller")
@RestController
@RequestMapping("/crypto/operationLog")
@Slf4j
public class VirtualCapitalOperationController extends BaseController {

    @Autowired
    private IAdminUserCapitalService adminUserCapitalService;

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    // 每页显示多少条数据
    private int numPerPage = Constant.adminPageSize;

    @Autowired
    private IFSysCoinWalletService sysCoinWalletService;

    /**
     * 加密货币充值提现列表
     */
    @GetMapping
    public Pagination<FVirtualCapitalOperation> list(
            @RequestParam(value = "fcoinid", required = false) Integer fcoinid,
            @RequestParam(value = "type", defaultValue = "0") Integer ftype,
            @RequestParam(value = "status", defaultValue = "-1") Integer status,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "orderField", defaultValue = "fupdatetime") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection,
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {

        Pagination<FVirtualCapitalOperation> pageParam = new Pagination<FVirtualCapitalOperation>(currentPage, numPerPage);
        // 排序条件
        pageParam.setOrderField(orderField);
        pageParam.setOrderDirection(orderDirection);

        FVirtualCapitalOperation filterParam = new FVirtualCapitalOperation();
        List<Integer> statusList = new ArrayList<>();
        // 关键字
        if (!StringUtils.isEmpty(keyWord)) {
            pageParam.setKeyword(keyWord);
        }
        // 加密货币ID
        if (fcoinid != null && fcoinid > 0) {
            filterParam.setFcoinid(fcoinid);
        }

        // 类型提现or充值
        if (ftype > 0) {
            filterParam.setFtype(ftype);
        }
        // 开始时间
        if (!StringUtils.isEmpty(logDate)) {
            pageParam.setBegindate(logDate);
        }
        // 结束时间
        if (!StringUtils.isEmpty(endDate)) {
            pageParam.setEnddate(endDate);
        }

        if (status >= 0) {
            statusList.add(status);
        }

        // 页面参数
        List<SystemCoinType> type = redisCryptoHelper.getCoinTypeList();
        Map<Integer, String> coinMap = new HashMap<Integer, String>();
        for (SystemCoinType coin : type) {
            coinMap.put(coin.getId(), coin.getName());
        }
        coinMap.put(0, "全部");

        // 查询
        return adminUserCapitalService.selectVirtualCapitalOperationList(
                pageParam, filterParam, statusList);
    }

    /**
     * 待审核加密货币提现列表
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/outList")
    public Pagination<FVirtualCapitalOperation> virtualCapitalOutList(
            @RequestParam(value = "fcoinid", defaultValue = "0") Integer fcoinid,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "orderField", defaultValue = "fupdatetime") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {

        Pagination<FVirtualCapitalOperation> pageParam = new Pagination<FVirtualCapitalOperation>(currentPage, numPerPage);
        pageParam.setOrderField(orderField);
        pageParam.setOrderDirection(orderDirection);
        // 开始时间
        if (!StringUtils.isEmpty(logDate)) {
            pageParam.setBegindate(logDate);
        }
        // 结束时间
        if (!StringUtils.isEmpty(endDate)) {
            pageParam.setEnddate(endDate);
        }
        FVirtualCapitalOperation filterParam = new FVirtualCapitalOperation();
        List<Integer> status = new ArrayList<>();
        filterParam.setFtype(VirtualCapitalOperationTypeEnum.COIN_OUT.getCode());
        status.add(VirtualCapitalOperationOutStatusEnum.WaitForOperation);
        status.add(VirtualCapitalOperationOutStatusEnum.OperationLock);
        status.add(VirtualCapitalOperationOutStatusEnum.LockOrder);

        // 关键字
        if (!StringUtils.isEmpty(keyWord)) {
            pageParam.setKeyword(keyWord);
        }
        // 加密货币ID
        if (fcoinid > 0) {
            filterParam.setFcoinid(fcoinid);
        }

        // 查询
        return adminUserCapitalService.selectVirtualCapitalOperationList(
                pageParam, filterParam, status);
    }

    /**
     * 生成授权码
     *
     * @param algorithm 算法
     * @param args      参数集
     * @return
     */
    private String newKey(String algorithm, Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuilder tmp = new StringBuilder(String.valueOf(args[0]));//.append(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));

        for (int i = 1; i < args.length; i++) {
            tmp.append("_").append(String.valueOf(args[i]));
        }
        byte[] result = CommonHelper.digest(algorithm, ByteBuffer.wrap(tmp.toString().getBytes()));
        return CommonHelper.toHexString(result, "");
    }

    /**
     * 审核加密货币提现订单-最后确定
     */
    @PutMapping("/outAudit")
    @ResponseBody
    public AjaxResult virtualCapitalOutAudit(HttpServletRequest request, @RequestParam("fid") Integer fid, @RequestParam("code") String code) throws Exception {

        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        FVirtualCapitalOperation virtualCapitaloperation = adminUserCapitalService.selectVirtualById(fid);
        // 检测状态
        int status = virtualCapitaloperation.getFstatus();
        // 验证码验证 SHA256([YYYY].[MM].[DD]_SHA256([YYYY].[MM].[DD]_[ID]_[UID])_uhhaisgood)
        String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        String key = newKey(CommonHelper.SHA256, date, virtualCapitaloperation.getFid(),
                virtualCapitaloperation.getFuid());
        key = newKey(CommonHelper.SHA256, date, key, "uhhaisgood");

        //查看系统是否默认开启提币审批验证码
        String requireApprove = redisCryptoHelper.getSystemArgs(ArgsConstant.ENABLE_COINOUT_APPROVE_CODE);
        if("1".equalsIgnoreCase(requireApprove)){
            if (!key.equals(code)) {
                return AjaxResult.error("审核失败,验证码或授权码错误,请重试!");
            }else{
                log.info("verification code verified. ");
            }
        }

        if (status == VirtualCapitalOperationOutStatusEnum.LockOrder) {
            return AjaxResult.error("审核失败,订单锁定中,请联系技术人员确定订单状态!");
        } else if (status != VirtualCapitalOperationOutStatusEnum.OperationLock) {
            String status_s = VirtualCapitalOperationOutStatusEnum.getEnumString(
                    VirtualCapitalOperationOutStatusEnum.OperationLock);
            return AjaxResult.error("审核失败,只有状态为:" + status_s + "的提现记录才允许审核!");
        }
        // 检测数量
        Long userId = virtualCapitaloperation.getFuid();
        int coinTypeId = virtualCapitaloperation.getFcoinid();
        UserCoinWallet virtualWalletInfo = adminUserCapitalService.selectUserVirtualWallet(userId, coinTypeId);
        BigDecimal amount = MathUtils.add(MathUtils.add(virtualCapitaloperation.getFamount(),
                virtualCapitaloperation.getFfees()), virtualCapitaloperation.getFbtcfees());
        // 冻结数量
        BigDecimal frozenRmb = virtualWalletInfo.getFrozen();

        if (MathUtils.sub(frozenRmb, amount).compareTo(BigDecimal.ZERO) < 0) {
            return AjaxResult.error("审核失败,冻结数量:" + frozenRmb + "小于提现数量:" + amount + "，操作异常!");
        }
        SystemCoinType virtualCoinType = redisCryptoHelper.getCoinType(coinTypeId);

        if (virtualCoinType == null || !virtualCoinType.getIsWithdraw()) {
            return AjaxResult.error("审核失败," + virtualCoinType.getName() + "已禁止提现!");
        }

        int addressNum = 0;//是否平台互转(判断地址是否是平台提供的地址)

        CoinDriver coinDriver = CoinDriverFactory.getDriver(virtualCoinType);
        // 查看是否存在地址:select count(*) from f_user_virtual_address where fadderess =
        addressNum = adminUserCapitalService.selectAddressNum(virtualCapitaloperation.getFwithdrawaddress());

        // 设置订单状态和时间
        virtualCapitaloperation.setFstatus(VirtualCapitalOperationOutStatusEnum.LockOrder);
        virtualCapitaloperation.setFupdatetime(Utils.getTimestamp());
        // 管理员ID
        virtualCapitaloperation.setFadminid(admin.getUserId());
        boolean flag = false;
        try {
            if (!adminUserCapitalService.updateVirtualCapital(admin, virtualCapitaloperation)) {
                return AjaxResult.error("审核失败,订单锁定失败,请稍后再试!");
            }
            // FIXME 提现并修改钱包信息
            flag = adminUserCapitalService.updateVirtualCapital(virtualCapitaloperation.getFid(), admin.getUserId(), amount, addressNum, coinDriver);
        } catch (Exception e) {
            log.error(e.toString(), e);
            return AjaxResult.error(e.getMessage());
        }
        return flag ? AjaxResult.success("审核成功!" + (addressNum > 0 ? "此订单为平台互转！请勿转币" : "")) : AjaxResult.error("更新审核失败");

    }

    /**
     * 修改订单状态
     * @param type (1 锁定 2 取消锁定 3 取消提现 4 恢复提现)
     */
    @RequestMapping("/changeStatus")
    @ResponseBody
    public AjaxResult virtualCapitalOperationChangeStatus(
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "uid", required = false) Integer uid
    ) throws Exception {

        LoginUser admin = AdminLoginUtils.getInstance().getManager();
        // FIXME 提现状态修改
        FVirtualCapitalOperation fvirtualCapitaloperation = adminUserCapitalService.selectVirtualById(uid);
        if(ObjectUtils.isEmpty(fvirtualCapitaloperation)){
            log.error("adminUserCapitalService.selectVirtualById({}) return null. check database status", uid);
            AjaxResult.error("审核失败,订单锁定中,请联系技术人员确定订单状态!");
        }
        Long fuid = fvirtualCapitaloperation.getFuid();
        Integer fcoinid = fvirtualCapitaloperation.getFcoinid();
        // String fcoinname=fvirtualCapitaloperation.getFcoinname();
        fvirtualCapitaloperation.setFupdatetime(Utils.getTimestamp());
        int status = fvirtualCapitaloperation.getFstatus();
        // 状态为锁定的才能恢复提现
        if (status == VirtualCapitalOperationOutStatusEnum.LockOrder && type != 4) {
            return AjaxResult.error("审核失败,订单锁定中,请联系技术人员确定订单状态!");
        }
        String tips = "";
        switch (type) {
            case 1:
                tips = "锁定";
                if (status != VirtualCapitalOperationOutStatusEnum.WaitForOperation) {
                    String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation);
                    return AjaxResult.error("锁定失败,只有状态为:‘" + status_s + "’的充值记录才允许锁定!");
                } else {
                    // FUser user = adminUserService.selectById(fuid);
                    //返回数据集
                    // JSONArray balance = new JSONArray();
                    //加密货币钱包列表 lgp:何用?
                    List<UserCoinWallet> vwalletList = new ArrayList<UserCoinWallet>();
                    //币种
                    List<SystemCoinType> coinList = redisCryptoHelper.getCoinTypeList();
                    SystemCoinType coin = null;
                    //循环币种
                    for (SystemCoinType coin1 : coinList) {
                        if (coin1.getId().equals(fcoinid)) {
                            coin = coin1;
                        }
                    }
                    //查询虚拟钱包
                    UserCoinWallet vwallet = adminUserCapitalService.selectUserVirtualWallet(fuid, coin.getId());
                    // lgp:何用?
                    vwallet.setCoinName(coin.getName());
                    vwalletList.add(vwallet);
                    // 充提统计
                    BigDecimal recharge = BigDecimal.ZERO;
                    BigDecimal withdraw = BigDecimal.ZERO;
                    // 买卖交易
                    BigDecimal buycount = BigDecimal.ZERO;
                    BigDecimal buyamount = BigDecimal.ZERO;
                    BigDecimal sellcount = BigDecimal.ZERO;
                    BigDecimal sellamount = BigDecimal.ZERO;
                    BigDecimal fee = BigDecimal.ZERO;

                    //加密货币充提
                    recharge = adminUserCapitalService.selectVirtualWalletTotalAmount(fuid, coin.getId(), VirtualCapitalOperationTypeEnum.COIN_IN.getCode());
                    withdraw = adminUserCapitalService.selectVirtualWalletTotalAmount(fuid, coin.getId(), VirtualCapitalOperationTypeEnum.COIN_OUT.getCode());
//                    boolean isTradeType = false;
//                    for (SystemTradeTypeEnum i : SystemTradeTypeEnum.values()) {
//                        if (i.toString().equals(coin.getShortName()) || "QCNY".equals(coin.getShortName())) {
//                            isTradeType = true;
//                            break;
//                        }
//                    }

                    //手工充值
                    BigDecimal rechargeWork = adminUserCapitalService.selectAdminRechargeVirtualWalletTotalAmount(fuid, coin.getId(), OperationlogEnum.AUDIT);
                    BigDecimal frozenWork = adminUserCapitalService.selectAdminRechargeVirtualWalletTotalAmount(fuid, coin.getId(), OperationlogEnum.FFROZEN);

                    BigDecimal rechargeCoin = recharge;
                    BigDecimal withdrawCoin = withdraw;

                    // 加密货币充值  + 手工充值审核
                    BigDecimal restplan = MathUtils.add(rechargeCoin, rechargeWork);
                    // + 手工充值冻结
                    restplan = MathUtils.add(restplan, frozenWork);
                    // - 加密货币提现
                    restplan = MathUtils.sub(restplan, withdrawCoin);
                    if (coin.getCoinType().equals(SystemCoinTypeEnum.CNY.getCode())) {
                        restplan = MathUtils.sub(restplan, buyamount);
                        restplan = MathUtils.add(restplan, sellamount);
                    } else {
                        // +总买单成交数据
                        restplan = MathUtils.add(restplan, buycount);
                        // -总卖单成交数据
                        restplan = MathUtils.sub(restplan, sellcount);
                    }
                    // -总买单手续费
                    restplan = MathUtils.sub(restplan, fee);

                    // 单个币种所有资产 = 可用 + 冻结 + 理财 + ico
                    BigDecimal rest = MathUtils.add(MathUtils.add(MathUtils.add(vwallet.getTotal(), vwallet.getFrozen()),
                            vwallet.getBorrow()), vwallet.getIco());
                    //判断restplan和rest是否相等  代码暂用
//                    boolean isbalance = restplan.compareTo(rest) == 0//暂用正负万分之1可以提现
                    boolean isbalance = Assets(restplan, rest);

                    if (isbalance) {
                        fvirtualCapitaloperation.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationLock);
                        break;
                    } else {
                        return AjaxResult.error("资产不平衡");
                    }
                }
            case 2:
                tips = "取消锁定";
                if (status != VirtualCapitalOperationOutStatusEnum.OperationLock) {
                    String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationLock);
                    return AjaxResult.error("取消锁定失败,只有状态为:‘" + status_s + "’的充值记录才允许取消锁定!");
                }
                fvirtualCapitaloperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation);
                break;
            case 3:
                tips = "取消提现";
                if (status == VirtualCapitalOperationOutStatusEnum.Cancel) {
                    return AjaxResult.error("取消提现失败,该记录已处于取消状态!");
                }
                fvirtualCapitaloperation.setFstatus(VirtualCapitalOperationOutStatusEnum.Cancel);
                break;
            case 4:
                tips = "恢复提现";
                if (status != VirtualCapitalOperationOutStatusEnum.LockOrder) {
                    return AjaxResult.error("恢复提现失败,只有状态为:锁定的充值记录才允许恢复提现!");
                }
                fvirtualCapitaloperation.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationLock);
                break;
            default:
                break;
        }

        boolean flag = false;
        try {
            fvirtualCapitaloperation.setFadminid(admin.getUserId());
            flag = adminUserCapitalService.updateVirtualCapital(admin, fvirtualCapitaloperation);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            return AjaxResult.error("未知错误，请刷新列表后重试！");
        }
        if (flag) {
            return AjaxResult.success(tips + "成功！");
        } else {
            return AjaxResult.error("未知错误，请刷新列表后重试！");
        }
    }

    private boolean Assets(BigDecimal x, BigDecimal y) {
        //x 代表 计算结果 y 代表 查询结果
        if (x.compareTo(y) == 0) {
            return true;
        }
        BigDecimal loat = BigDecimal.valueOf(0.0001);
        //用户计算资产小于查询资产的正万分之1&& 用户计算资产大于查询资产的负万分之一
        if (x.compareTo(MathUtils.add(y, MathUtils.mul(y, loat))) == -1 && x.compareTo(MathUtils.sub(y, MathUtils.mul(y, loat))) == 1) {
            return true;
        }
        return false;
    }

}
