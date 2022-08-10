package io.uhha.web.controller.crypto;

import io.swagger.annotations.Api;
import io.uhha.coin.common.Enum.LogAdminActionEnum;
import io.uhha.coin.common.Enum.SystemCoinSortEnum;
import io.uhha.coin.common.Enum.SystemCoinStatusEnum;
import io.uhha.coin.common.coin.CoinDriver;
import io.uhha.coin.common.coin.CoinDriverFactory;
import io.uhha.coin.common.coin.driver.ETHDriver;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.common.framework.redis.RedisCryptoHelper;
import io.uhha.common.constant.Constant;
import io.uhha.coin.common.util.MQSend;
import io.uhha.coin.system.domain.SystemCoinSetting;
import io.uhha.coin.system.domain.SystemCoinType;
import io.uhha.coin.system.domain.SystemCoinTypeVO;
import io.uhha.coin.user.service.IAdminSystemCoinTypeService;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.domain.model.LoginUser;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.utils.ip.IpUtils;
import io.uhha.web.utils.AdminLoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Api(tags = "加密货币币种Controller")
@RestController
@RequestMapping("/sysCoinType")
@Slf4j
public class SystemCoinTypeController extends BaseController {

    @Autowired
    private IAdminSystemCoinTypeService adminSystemCoinTypeService;

    @Autowired
    private MQSend mqSend;

    @Autowired
    private RedisCryptoHelper redisCryptoHelper;

    @Value("${secret.ddn}")
    private String ddnSecret;
    @Value("${secret.btw}")
    private String btwSecret;
    @Value("${secret.uso}")
    private String usoSecret;
    //指定HLP
    @Value("${secret.hlp}")
    private String hlpSecret;
    @Value("${secret.nxt}")
    private String nxtSecret;
    @Value("${passWord.eth}")
    private String ethPassWord;
    @Value("${passWord.etf}")
    private String etfPassWord;

    /**
     * 管理后台使用获取虚拟币列表
     *
     * @param currentPage
     * @param keywords
     * @param orderField
     * @param orderDirection
     * @return
     * @throws Exception
     */
    @GetMapping
    public Pagination<SystemCoinTypeVO> selectVirtualCoinVOList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "coinType", defaultValue = "0") Integer coinType,
            @RequestParam(value = "orderField", defaultValue = "gmt_create") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection
    ) {
        Pagination<SystemCoinTypeVO> page = new Pagination<SystemCoinTypeVO>(currentPage, Constant.adminPageSize);
        page.setOrderDirection(orderDirection);
        page.setOrderField(orderField);
        page.setKeyword(keywords);

        SystemCoinType fVirtualCoinType = new SystemCoinType();
        fVirtualCoinType.setCoinType(coinType);
        return adminSystemCoinTypeService.selectVirtualCoinVOList(page, fVirtualCoinType);
    }


    /**
     * 加载币种列表
     *
     * @param coinid
     * @return
     * @throws Exception
     */
    @GetMapping("/{coinid}")
    public AjaxResult getVirtualCoinTypeById(
            @PathVariable(value = "coinid") Integer coinid
    ) {
        return AjaxResult.success(adminSystemCoinTypeService.selectVirtualCoinById(coinid));
    }


    /**
     * 保存新增的币种信息
     */
    @PostMapping
    @ResponseBody
    public AjaxResult saveVirtualCoinType(
            HttpServletRequest request,
            @RequestBody SystemCoinType coinType

    ) throws Exception {

        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        coinType.setStatus(SystemCoinStatusEnum.ABNORMAL.getCode());

        if (this.adminSystemCoinTypeService.insert(coinType)) {
            String ip = IpUtils.getIpAddr(request);
            mqSend.SendAdminAction(admin.getUserId(), LogAdminActionEnum.COIN_ADD, ip);
        }
        return AjaxResult.success("新增成功！");
    }

    /**
     * 修改的币种信息
     */
    @PutMapping
    @ResponseBody
    public AjaxResult updateCoinType(
            HttpServletRequest request,
            @RequestBody SystemCoinType coinType
    ) throws Exception {
        SystemCoinType coinTypeInDb = adminSystemCoinTypeService.selectVirtualCoinById(coinType.getId());
        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        if (coinTypeInDb == null) {
            return AjaxResult.error("币种不存在,请刷新重试！");
        }
        if (this.adminSystemCoinTypeService.updateVirtualCoin(coinType)) {
            String ip = IpUtils.getIpAddr(request);
            mqSend.SendAdminAction(admin.getUserId(), LogAdminActionEnum.COIN_ADD, ip);
        }
        return AjaxResult.success("修改成功");
    }

    /**
     * 虚拟币状态操作
     */
    @PutMapping("/updateCoinStatus")
    public AjaxResult updateCoinTypeStatus(@RequestBody SystemCoinTypeVO coinTypeVO) {
        SystemCoinType coinType = adminSystemCoinTypeService.selectVirtualCoinById(coinTypeVO.getId());
        if (coinType == null) {
            return AjaxResult.error("币种不存在，请刷新重试！");
        }
        try {
            coinType.setStatus(coinTypeVO.getStatus());
            coinType.setIsRecharge(coinTypeVO.getIsRecharge());
            coinType.setIsWithdraw(coinTypeVO.getIsWithdraw());
            adminSystemCoinTypeService.updateVirtualCoinByEnabled(coinType);
            return AjaxResult.success("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败！");
        }

    }

    /**
     * 钱包的测试
     */
    @GetMapping("/testWallet")
    @ResponseBody
    public AjaxResult testWallet(
            @RequestParam("fid") int fid,
            @RequestParam(value = "address", required = true) String address) throws Exception {
        SystemCoinType coinType = this.adminSystemCoinTypeService.selectVirtualCoinById(fid);
        String accesskey = coinType.getAccessKey();
        String secretkey = coinType.getSecrtKey();
        String ip = coinType.getIp();
        String port = coinType.getPort();
        if (accesskey == null || secretkey == null || ip == null || port == null) {
            return AjaxResult.error("钱包数据缺少，请检查配置信息！");
        }
        try {
            CoinDriver coinDriver = CoinDriverFactory.getDriver(coinType);
            BigDecimal balance = new BigDecimal("-1");

            if (coinType.getCoinType().equals(SystemCoinSortEnum.ETH.getCode())) {
                if ("ETH".equals(coinType.getShortName())) {
                    balance = ((ETHDriver) coinDriver).getEthBalance(address);//eth查询余额
                } else {
                    balance = ((ETHDriver) coinDriver).getEthTokenBalance(address, coinType.getContractAddress());//eth代币余额
                }
            } else {
                balance = coinDriver.getBalance();
            }
            if (balance == null) {
                return AjaxResult.error("钱包连接失败，请检查配置信息！");
            }
            return AjaxResult.success("测试成功，钱包余额:" + balance);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        return AjaxResult.error("钱包连接失败，请检查配置信息！");
    }

    /**
     * 获取币的手续费配置
     */
    @GetMapping("/setting")
    @ResponseBody
    public TableDataInfo getSysCoinSetting(
            @RequestParam("coinId") Integer coinId) throws Exception {
        List<SystemCoinSetting> coinSettingList = adminSystemCoinTypeService.selectSystemCoinSettingList(coinId);
        startPage();
        return getDataTable(coinSettingList);
    }

    /**
     * 修改币的手续费配置
     */
    @PutMapping("/setting")
    @ResponseBody
    public AjaxResult updateCoinFee(
            HttpServletRequest request,
            @RequestParam("id") Integer id,
            @RequestParam("coinId") Integer coinId,
            @RequestParam("levelVip") Integer levelVip,
            @RequestParam("withdrawMax") BigDecimal withdrawMax,
            @RequestParam("withdrawMin") BigDecimal withdrawMin,
            @RequestParam("withdrawFee") BigDecimal withdrawFee,
            @RequestParam("withdrawTimes") Integer withdrawTimes,
            @RequestParam("withdrawDayLimit") BigDecimal withdrawDayLimit) {

        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        try {
            SystemCoinSetting setting = SystemCoinSetting.builder()
                    .id(id)
                    .coinId(coinId)
                    .levelVip(levelVip)
                    .withdrawMax(withdrawMax)
                    .withdrawMin(withdrawMin)
                    .withdrawFee(withdrawFee)
                    .withdrawTimes(withdrawTimes)
                    .withdrawDayLimit(withdrawDayLimit)
                    .build();

            this.adminSystemCoinTypeService.updateSystemCoinSetting(setting);

            String ip = IpUtils.getIpAddr(request);
            mqSend.SendAdminAction(admin.getUserId(), LogAdminActionEnum.COIN_FREES, ip);
            return AjaxResult.success("更新成功！");
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        return AjaxResult.error("更新失败！");
    }

    /**
     * 创建虚拟币钱包地址
     *
     * @param fid      虚拟币ID
     * @param count
     * @param password
     * @return
     * @throws Exception
     */
    @GetMapping("/createWalletAddress")
    @ResponseBody
    public AjaxResult createWalletAddress(
            HttpServletRequest request,
            @RequestParam(value = "fid") int fid,
            @RequestParam(value = "count") int count,
            @RequestParam(value = "password") String password) throws Exception {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        if (count < 0 || count > 100000) {
            return AjaxResult.error("生成钱包地址数量错误！");
        }
        SystemCoinType coinType = adminSystemCoinTypeService.selectVirtualCoinById(fid);
        try {
            if ("ETH".equals(coinType.getShortName())) {
                password = ethPassWord;
                coinType.setSecrtKey(password);
            } else if ("ETF".equals(coinType.getShortName())) {
                password = etfPassWord;
                coinType.setSecrtKey(password);
            }
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        if (coinType.getCoinType().equals(SystemCoinSortEnum.ETH.getCode())) {
            if (!"ETH".equals(coinType.getShortName())) {
                return AjaxResult.error("以太坊类统一使用ETH的地址，请在简称为ETH的币种里生成地址！");
            }
        }
        adminSystemCoinTypeService.createVirtualCoinAddress(coinType, count, password);
        String ip0 = IpUtils.getIpAddr(request);
        mqSend.SendAdminAction(admin.getUserId(), LogAdminActionEnum.COIN_ADDRESS, ip0);
        return AjaxResult.success("地址异步生成中，请点击【虚拟币可用地址列表】查看生成详情！");
    }


    @GetMapping("/walletAddressList")
    public Pagination<Map<String, Object>> walletAddressList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false) String keywords
    ) {

        Pagination<Map<String, Object>> page = new Pagination<Map<String, Object>>(pageNum, Constant.adminPageSize);
        return adminSystemCoinTypeService.selectVirtualCoinAddressNumList(page);
    }

}
