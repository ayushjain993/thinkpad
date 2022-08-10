package io.uhha.web.controller.crypto;

import io.swagger.annotations.Api;
import io.uhha.coin.user.service.IAdminUserCapitalService;
import io.uhha.coin.log.domain.FLogModifyCapitalOperation;
import io.uhha.coin.capital.domain.FWalletCapitalOperation;
import io.uhha.coin.common.Enum.*;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.common.constant.Constant;
import io.uhha.common.utils.Utils;
import io.uhha.coin.user.domain.UserCoinWallet;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.domain.model.LoginUser;
import io.uhha.web.utils.AdminLoginUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Api(tags = "钱包资产操作Controller")
@RestController
@RequestMapping("/walletCapitalOperation")
public class WalletCapitalOperationController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(WalletCapitalOperationController.class);
	
	@Autowired
	private IAdminUserCapitalService adminUserCapitalService;

	// 每页显示多少条数据
	private int numPerPage = Constant.adminPageSize;

    /**
     * 待审核人民币充值列表
     */
    @GetMapping("/capitalInList")
    public Pagination<FWalletCapitalOperation> capitalInList(
            @RequestParam(value = "capitalId", defaultValue = "0") Integer capitalId,
            @RequestParam(value = "ftype", defaultValue = "0") Integer ftype,
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "orderField", defaultValue = "fupdatetime") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {
        // 定义查询条件
        Pagination<FWalletCapitalOperation> pageParam = new Pagination<FWalletCapitalOperation>(currentPage, numPerPage);
        // 排序条件
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

        FWalletCapitalOperation filterParam = new FWalletCapitalOperation();
        List<Integer> status = new ArrayList<>();
        filterParam.setFinouttype(CapitalOperationInOutTypeEnum.IN.getCode());
        // 充值状态
        status.add(CapitalOperationInStatus.WaitForComing);
        // 充值类型
        if (ftype > 0) {
            filterParam.setFtype(ftype);
        }
        // 关键字
        if (!StringUtils.isEmpty(keyWord)) {
            pageParam.setKeyword(keyWord);
        }
        // 充值ID
        if (capitalId > 0) {
            filterParam.setFid(Integer.valueOf(capitalId));
        }

        // 查询
        return adminUserCapitalService.selectWalletCapitalOperationList(pageParam, filterParam, status);
    }

    /**
     * 审核人民币充值
     */
    @PutMapping("/capitalInAudit")
    @ResponseBody
    public AjaxResult capitalInAudit(
            @RequestParam(value = "uid", required = true) Integer fid) throws Exception {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();
        
        FWalletCapitalOperation capitalOperation = adminUserCapitalService.selectById(fid);
        int status = capitalOperation.getFstatus();
        if (status != CapitalOperationInStatus.WaitForComing) {
            String status_s = CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.WaitForComing);
            return AjaxResult.error("审核失败,只有状态为:" + status_s + "的充值记录才允许审核!");
        }
        // 计算实际到账金额
        BigDecimal amount = capitalOperation.getFamount();
        // 充值操作
        capitalOperation.setFstatus(CapitalOperationInStatus.Come);
        capitalOperation.setFupdatetime(Utils.getTimestamp());
        capitalOperation.setFadminid(admin.getUserId());
        // 数据更新
        boolean flag = false;
        try {
            flag = adminUserCapitalService.updateWalletCapital(admin, capitalOperation, amount, true);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            flag = false;
        }
        if (!flag) {
            return AjaxResult.error("审核失败");
        }
//        // 增加积分
//        UmsMember fuser = umsMemberService.selectUmsMemberById(capitalOperation.getFuid());
//        scoreHelper.SendUserScore(fuser.getFid(), capitalOperation.getFamount(), ScoreTypeEnum.RECHARGE.getCode(), "充值RMB" + ":" + capitalOperation.getFamount() + "元");
//        //adminUserCapitalService.addUserScore(capitalOperation.getFagentid(), capitalOperation.getFuid(), fscore, ScoreTypeEnum.RECHARGE.getCode(), amount, 10d);
//        // 发送短信
//        if (fuser.getFistelephonebind()) {
//            validateHelper.smsUserRecharge(fuser.getFareacode(), fuser.getFtelephone(), capitalOperation.getFplatform(),
//                    BusinessTypeEnum.SMS_CNY_RECHARGE.getCode(), amount);
//        }
        return AjaxResult.success("审核成功");
    }


    /**
     * 修改充值金额
     */
    @PutMapping("/updateCapitalAmount")
    @ResponseBody
    public AjaxResult updateCapitalAmount(
            HttpServletRequest request,
            @RequestParam(value = "capitaloperation.famount", required = true) BigDecimal modifyMoney,
            @RequestParam(value = "capitaloperation.ftype", required = true) Integer ftype,
            @RequestParam(value = "capitaloperation.fid", required = true) Integer fid) throws Exception {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        FWalletCapitalOperation capitaloperation = adminUserCapitalService.selectById(fid);
        // 判断金额是否允许修改
        if (MathUtils.toScaleNum(capitaloperation.getFamount(), MathUtils.INTEGER_SCALE).compareTo(modifyMoney) <= 0
                && MathUtils.toScaleNumUp(capitaloperation.getFamount(), MathUtils.INTEGER_SCALE).compareTo(modifyMoney) > 0) {
            FLogModifyCapitalOperation transientInstance = new FLogModifyCapitalOperation();
            transientInstance.setFaccount(capitaloperation.getFaccount());
            transientInstance.setFamount(capitaloperation.getFamount());
            transientInstance.setFbank(capitaloperation.getFbank());
            transientInstance.setFmodifyamount(modifyMoney);
            transientInstance.setFupdatetime(Utils.getTimestamp());
            transientInstance.setFpayee(capitaloperation.getFpayee());
            transientInstance.setFphone(capitaloperation.getFphone());
            transientInstance.setFadminid(admin.getUserId());

            capitaloperation.setFamount(modifyMoney);
            capitaloperation.setFtype(ftype);
            adminUserCapitalService.updateModifyCapital(capitaloperation, transientInstance);
            // 修改成功
            return AjaxResult.success("更新成功");
        } else {
            return AjaxResult.error("修改失败，您修改的金额必须大于等于"
                    + MathUtils.toScaleNum(capitaloperation.getFamount(), MathUtils.INTEGER_SCALE) + "元，并且小于"
                    + MathUtils.toScaleNumUp(capitaloperation.getFamount(), MathUtils.INTEGER_SCALE) + "元");
        }
    }

    /**
     * 取消充值
     */
    @PutMapping("/capitalInCancel")
    @ResponseBody
    public AjaxResult capitalInCancel(
            @RequestParam(value = "uid", required = true) Integer fid) throws Exception {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();
        FWalletCapitalOperation capitalOperation = adminUserCapitalService.selectById(fid);
        int status = capitalOperation.getFstatus();
        if (status == CapitalOperationInStatus.Come || status == CapitalOperationInStatus.Invalidate) {
            return AjaxResult.error("取消失败,该笔充值已充值成功");
        }
        capitalOperation.setFstatus(CapitalOperationInStatus.Invalidate);
        capitalOperation.setFadminid(admin.getUserId());
        adminUserCapitalService.updateWalletCapital(capitalOperation);
        return AjaxResult.success("取消成功");
    }

    /**
     * 提现批量锁定
     */
    @PutMapping("/capitalOutAuditAll")
    @ResponseBody
    public AjaxResult capitalOutAuditAll(String[] ids) throws Exception {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        // 充值操作
        for (int i = 0; i < ids.length; i++) {
            int id = Integer.parseInt(ids[i]);
            FWalletCapitalOperation capitalOperation = adminUserCapitalService.selectById(id);
            int status = capitalOperation.getFstatus();
            if (status != CapitalOperationOutStatus.WaitForOperation) {
                String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation);
                return AjaxResult.error("锁定失败,只有状态为:‘" + status_s + "’的提现记录才允许锁定!");
            }
            capitalOperation.setFupdatetime(Utils.getTimestamp());
            capitalOperation.setFadminid(admin.getUserId());
            capitalOperation.setFstatus(CapitalOperationOutStatus.OperationLock);
            adminUserCapitalService.updateWalletCapital(capitalOperation);
        }
        return AjaxResult.success("批量锁定成功!");
    }

    /**
     * 查看会员钱包信息
     */
    @GetMapping("/viewUserWallet")
    public AjaxResult viewUserWallet(@RequestParam("fid") String fid) throws Exception {
        FWalletCapitalOperation capitalOperation = adminUserCapitalService.selectById(Integer.valueOf(fid));
        UserCoinWallet userWallet = adminUserCapitalService.selectUserWallet(capitalOperation.getFuid(), capitalOperation.getFcoinid());
        return AjaxResult.success(userWallet);
    }

    /**
     * 充值提现记录
     */
    @GetMapping("/capitaloperationList")
    public Pagination<FWalletCapitalOperation> Index(
            @RequestParam(value = "capitalId", defaultValue = "0") Integer capitalId,
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "fstatus", defaultValue = "0") String fstatus,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "orderField", defaultValue = "fupdatetime") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {

        // 定义查询条件
        Pagination<FWalletCapitalOperation> pageParam = new Pagination<FWalletCapitalOperation>(currentPage, numPerPage);
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

        FWalletCapitalOperation filterParam = new FWalletCapitalOperation();
        List<Integer> status = new ArrayList<>();
        // 关键字
        if (!StringUtils.isEmpty(keyWord)) {
            pageParam.setKeyword(keyWord);
        }
        // 类型-充值or提现
        if (type != null) {
            if (type != 0) {
                if (type == 1) {
                    filterParam.setFinouttype(CapitalOperationInOutTypeEnum.IN.getCode());
                } else {
                    filterParam.setFinouttype(CapitalOperationInOutTypeEnum.OUT.getCode());
                }
            }
        }
        // 充值ID
        if (capitalId > 0) {
            filterParam.setFid(Integer.valueOf(capitalId));
        }
        // 状态
        if (!fstatus.equals("0")) {
            if (fstatus.indexOf("充值") != -1) {
                status.add(Integer.valueOf(fstatus.replace("充值-", "")));
            } else if (fstatus.indexOf("提现") != -1) {
                status.add(Integer.valueOf(fstatus.replace("提现-", "")));
            }
        }

        // 查询
       return  adminUserCapitalService.selectWalletCapitalOperationList(pageParam, filterParam, status);

    }


}
