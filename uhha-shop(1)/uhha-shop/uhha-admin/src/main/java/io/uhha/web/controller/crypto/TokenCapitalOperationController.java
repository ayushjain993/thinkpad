package io.uhha.web.controller.crypto;

import io.swagger.annotations.Api;
import io.uhha.coin.user.service.IAdminUserCapitalService;
import io.uhha.coin.capital.domain.FTokenCapitalOperation;
import io.uhha.coin.common.Enum.CapitalOperationInStatus;
import io.uhha.coin.common.Enum.CapitalOperationOutStatus;
import io.uhha.coin.common.Enum.CapitalOperationTypeEnum;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.coin.common.match.MathUtils;
import io.uhha.common.constant.Constant;
import io.uhha.common.utils.Utils;
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

/**
 * Token资产Controller
 *
 * @author ZGY
 */
@Api(tags = "Token资产Controller")
@RestController
@RequestMapping("/tokenCapitalOperation")
public class TokenCapitalOperationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TokenCapitalOperationController.class);

    @Autowired
    private IAdminUserCapitalService adminUserCapitalService;

    // 每页显示多少条数据
    private int numPerPage = Constant.adminPageSize;

    /**
     * 待审核Token币列表
     */
    @GetMapping("/toBeAuditIn")
    public Pagination<FTokenCapitalOperation> capitalTokerInList(
            @RequestParam(value = "faccount", required = false) String faccount,
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "orderField", defaultValue = "fupdatetime") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {

        // 定义查询条件
        Pagination<FTokenCapitalOperation> pageParam = new Pagination<FTokenCapitalOperation>(currentPage, numPerPage);
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

        FTokenCapitalOperation filterParam = new FTokenCapitalOperation();
        List<Integer> status = new ArrayList<>();
        filterParam.setFinouttype(CapitalOperationTypeEnum.TOKEN_IN);
        // 充值状态
        status.add(CapitalOperationInStatus.WaitForComing);
        // 查询
        return adminUserCapitalService.selectTokenCapitalOperationList(pageParam, filterParam, status);
    }

    /**
     * 审核Token充值
     */
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult capitalTokenInAudit(
            HttpServletRequest request,
            @RequestParam(value = "uid", required = true) Integer fid) throws Exception {

        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        FTokenCapitalOperation tokerCapitalOperation = adminUserCapitalService.selectTokenById(fid);
        int status = tokerCapitalOperation.getFstatus();
        if (status != CapitalOperationInStatus.WaitForComing) {
            String status_s = CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.WaitForComing);
            return AjaxResult.error("审核失败,只有状态为:" + status_s + "的充值记录才允许审核!");
        }
        // 计算实际到账金额
        BigDecimal amount = tokerCapitalOperation.getFamount();
        // 充值操作

        tokerCapitalOperation.setFstatus(CapitalOperationInStatus.Come);
        tokerCapitalOperation.setFupdatetime(Utils.getTimestamp());
        tokerCapitalOperation.setFadminid(admin.getUserId());
        // 数据更新
        boolean flag;
        try {
            flag = adminUserCapitalService.updateTokenCapital(admin, tokerCapitalOperation, amount);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            flag = false;
        }
        if (!flag) {
            return AjaxResult.error("审核失败");
        }
        return AjaxResult.error("审核成功");
    }

    /**
     * 待审核淘币提现列表
     */
    @GetMapping("/toBeAuditOut")
    public  Pagination<FTokenCapitalOperation> capitalTokerOutList(
            @RequestParam(value = "capitalId", defaultValue = "0") Integer capitalId,
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "ftype", required = false) Integer type,
            @RequestParam(value = "orderField", defaultValue = "fupdatetime") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {
        // 定义查询条件
        Pagination<FTokenCapitalOperation> pageParam = new Pagination<FTokenCapitalOperation>(currentPage, numPerPage);
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

        FTokenCapitalOperation filterParam = new FTokenCapitalOperation();
        List<Integer> status = new ArrayList<>();
        status.add(CapitalOperationOutStatus.WaitForOperation);
        status.add(CapitalOperationOutStatus.OperationLock);
        filterParam.setFinouttype(CapitalOperationTypeEnum.TOKEN_OUT);
        // 关键字
        if (keyWord != null && keyWord.trim().length() > 0) {
            pageParam.setKeyword(keyWord);
        }

        // 充值ID
        if (capitalId > 0) {
            filterParam.setFid(capitalId);
        }
        //充值卡类型
        if (type != null && type > 0) {
            filterParam.setFtype(type);
        }

        // 查询
        return adminUserCapitalService.selectTokenCapitalOperationList(pageParam, filterParam, status);
    }

    /**
     * 淘币提现操作
     */
    @RequestMapping("/outAudit")
    @ResponseBody
    public AjaxResult capitalTokenOutAudit(
            @RequestParam(value = "uid", required = true) Integer fid,
            @RequestParam(value = "type", required = true) Integer type,
            @RequestParam(value = "online", required = true, defaultValue = "0") Integer online) throws Exception {

        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        FTokenCapitalOperation capitalOperation = adminUserCapitalService.selectTokenById(fid);
        int status = capitalOperation.getFstatus();
        switch (type) {
            case 1:
                if (status != CapitalOperationOutStatus.OperationLock) {
                    String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationLock);
                    return AjaxResult.error("审核失败,只有状态为:‘" + status_s + "’的提现记录才允许审核!");
                }
                break;
            case 2:
                if (status != CapitalOperationOutStatus.WaitForOperation) {
                    String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation);
                    return AjaxResult.error("锁定失败,只有状态为:‘" + status_s + "’的提现记录才允许锁定!");
                }
                break;
            case 3:
                if (status != CapitalOperationOutStatus.OperationLock) {
                    String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationLock);
                    return AjaxResult.error("取消锁定失败,只有状态为:‘" + status_s + "’的提现记录才允许取消锁定!");
                }
                break;
            case 4:
                if (status == CapitalOperationOutStatus.Cancel || status == CapitalOperationOutStatus.OperationSuccess) {
                    return AjaxResult.error("取消失败!");
                }
                break;
            default:
                return AjaxResult.error("非法提交！");
        }
        BigDecimal amount = capitalOperation.getFamount();
        BigDecimal frees = capitalOperation.getFfees();
        BigDecimal totalAmt = MathUtils.add(amount, frees);
        // 充值操作
        capitalOperation.setFupdatetime(Utils.getTimestamp());
        capitalOperation.setFadminid(admin.getUserId());
        // 钱包操作//1审核,2锁定,3取消锁定,4取消提现
        String tips = "";

        switch (type) {
            case 1:
                capitalOperation.setFstatus(CapitalOperationOutStatus.OperationSuccess);
                tips = "审核";
                break;
            case 2:
                capitalOperation.setFstatus(CapitalOperationOutStatus.OperationLock);
                tips = "锁定";
                break;
            case 3:
                capitalOperation.setFstatus(CapitalOperationOutStatus.WaitForOperation);
                tips = "取消锁定";
                break;
            case 4:
                capitalOperation.setFstatus(CapitalOperationOutStatus.Cancel);
                tips = "取消";
                break;
        }
        boolean flag = false;
        try {
            adminUserCapitalService.updateTokenCapital(admin, capitalOperation, totalAmt);
            flag = true;
        } catch (Exception e) {
            tips = e.getMessage() + "," + tips;
            logger.error(e.toString());
            e.printStackTrace();
            flag = false;
        }
        if (!flag) {
            return AjaxResult.error(tips + "失败");
        }
        return AjaxResult.success(tips + "成功");
    }


    /**
     * 提现批量锁定
     */
    @PostMapping("/outAuditAll")
    @ResponseBody
    public AjaxResult capitalOutAuditAll(String[] ids) throws Exception {
        // 充值操作
        LoginUser admin = AdminLoginUtils.getInstance().getManager();
        for (int i = 0; i < ids.length; i++) {
            int id = Integer.parseInt(ids[i]);
            FTokenCapitalOperation  FTokenCapitalOperation = adminUserCapitalService.selectTokenById(id);
            int status = FTokenCapitalOperation.getFstatus();
            if (status != CapitalOperationOutStatus.WaitForOperation) {
                String status_s = CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation);
                return AjaxResult.error("锁定失败,只有状态为:‘" + status_s + "’的提现记录才允许锁定!");
            }
            FTokenCapitalOperation.setFupdatetime(Utils.getTimestamp());
            FTokenCapitalOperation.setFadminid(admin.getUserId());
            FTokenCapitalOperation.setFstatus(CapitalOperationOutStatus.OperationLock);
            adminUserCapitalService.updateTokenCapital(FTokenCapitalOperation);
        }
        return AjaxResult.success("批量锁定成功!");
    }

    /**
     * 淘币充值提现记录
     */
    @GetMapping("/operationList")
    public  Pagination<FTokenCapitalOperation> Index(
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "fstatus", defaultValue = "0") String fstatus,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "orderField", defaultValue = "fupdatetime") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {

        // 定义查询条件
        Pagination<FTokenCapitalOperation> pageParam = new Pagination<FTokenCapitalOperation>(currentPage, numPerPage);
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
        FTokenCapitalOperation filterParam = new FTokenCapitalOperation();
        List<Integer> status = new ArrayList<>();
        // 关键字
        if (!StringUtils.isEmpty(keyWord)) {
            pageParam.setKeyword(keyWord);
        }
        // 类型-充值or提现
        if (type != null) {
            if (type != 0) {
                if (type == 1) {
                    filterParam.setFinouttype(9);
                } else {
                    filterParam.setFinouttype(10);
                }
            }
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
        return adminUserCapitalService.selectTokenCapitalOperationList(pageParam, filterParam, status);
    }
//    // 导出列名
//    private static enum ExportFiledRecharge {
//        UID, 会员登录名, 会员昵称, 真实姓名, 金额, 手续费, 转出账号, 状态, 创建时间, 最后修改时间, 审核人;
//    }

//    /**
//     * 淘币充值提现列表导出
//     */
//    @RequestMapping("admin/tokercapitaloperationExport")
//    @ResponseBody
//    public AjaxResult capitaloperationExport(
//            @RequestParam(value = "logDate", required = false) String logDate,
//            @RequestParam(value = "endDate", required = false) String endDate,
//            @RequestParam(value = "type", required = false) Integer type,
//            @RequestParam(value = "fstatus", defaultValue = "0") String fstatus,
//            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
//            @RequestParam(value = "keywords", required = false) String keyWord,
//            @RequestParam(value = "orderField", defaultValue = "fupdatetime") String orderField,
//            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {
//        ModelAndView modelAndView = new ModelAndView();
//        HttpServletResponse response = sessionContextUtils.getContextResponse();
//        response.setContentType("Application/excel");
//        response.addHeader("Content-Disposition", "attachment;filename=capitaloperationList.xls");
//        XlsExport e = new XlsExport();
//        int rowIndex = 0;
//        // header
//        e.createRow(rowIndex++);
//        for (ExportFiledRecharge filed : ExportFiledRecharge.values()) {
//            e.setCell(filed.ordinal(), filed.toString());
//        }
//        // 定义查询条件
//        Pagination<FTokenCapitalOperation> pageParam = new Pagination<FTokenCapitalOperation>(currentPage, numPerPage);
//        pageParam.setOrderField(orderField);
//        pageParam.setOrderDirection(orderDirection);
//        // 开始时间
//        if (!StringUtils.isEmpty(logDate)) {
//            modelAndView.addObject("logDate", logDate);
//            pageParam.setBegindate(logDate);
//        }
//        // 结束时间
//        if (!StringUtils.isEmpty(endDate)) {
//            modelAndView.addObject("endDate", endDate);
//            pageParam.setEnddate(endDate);
//        }
//        FTokenCapitalOperation filterParam = new FTokenCapitalOperation();
//        List<Integer> status = new ArrayList<>();
//        // 关键字
//        if (!StringUtils.isEmpty(keyWord)) {
//            pageParam.setKeyword(keyWord);
//        }
//        // 类型-充值or提现
//        if (type != null) {
//            if (type != 0) {
//                if (type == 1) {
//                    filterParam.setFinouttype(9);
//                } else {
//                    filterParam.setFinouttype(10);
//                }
//            }
//        }
//        // 状态
//        if (!fstatus.equals("0")) {
//            if (fstatus.indexOf("充值") != -1) {
//                status.add(Integer.valueOf(fstatus.replace("充值-", "")));
//            } else if (fstatus.indexOf("提现") != -1) {
//                status.add(Integer.valueOf(fstatus.replace("提现-", "")));
//            }
//        }
//        // 页面参数
//        Map<String, String> statusMap = new HashMap<String, String>();
//        statusMap.put("0", "全部");
//        if (type != null) {
//            if (type == 1) {
//                statusMap.put("充值-" + CapitalOperationInStatus.Come, "充值-" + CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.Come));
//                statusMap.put("充值-" + CapitalOperationInStatus.Invalidate, "充值-" + CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.Invalidate));
//                statusMap.put("充值-" + CapitalOperationInStatus.NoGiven, "充值-" + CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.NoGiven));
//                statusMap.put("充值-" + CapitalOperationInStatus.WaitForComing, "充值-" + CapitalOperationInStatus.getEnumString(CapitalOperationInStatus.WaitForComing));
//            } else {
//                statusMap.put("提现-" + CapitalOperationOutStatus.Cancel, "提现-" + CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.Cancel));
//                statusMap.put("提现-" + CapitalOperationOutStatus.OperationLock, "提现-" + CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationLock));
//                statusMap.put("提现-" + CapitalOperationOutStatus.OperationSuccess, "提现-" + CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.OperationSuccess));
//                statusMap.put("提现-" + CapitalOperationOutStatus.WaitForOperation, "提现-" + CapitalOperationOutStatus.getEnumString(CapitalOperationOutStatus.WaitForOperation));
//            }
//        }
//        // 查询
//        Pagination<FTokenCapitalOperation> pagination = adminUserCapitalService.selectTokenCapitalOperationList(pageParam, filterParam, status);
//        Collection<FTokenCapitalOperation> capitalOperationList = pagination.getData();
//        for (FTokenCapitalOperation capitalOperation : capitalOperationList) {
//            e.createRow(rowIndex++);
//            for (ExportFiledRecharge filed : ExportFiledRecharge.values()) {
//                switch (filed) {
//                    case UID:
//                        e.setCell(filed.ordinal(), capitalOperation.getFuid());
//                        break;
//                    case 会员登录名:
//                        e.setCell(filed.ordinal(), capitalOperation.getFloginname());
//                        break;
//                    case 会员昵称:
//                        e.setCell(filed.ordinal(), capitalOperation.getFnickname());
//                        break;
//                    case 真实姓名:
//                        e.setCell(filed.ordinal(), capitalOperation.getFrealname());
//                        break;
//                    case 金额:
//                        e.setCell(filed.ordinal(), capitalOperation.getFamount().doubleValue());
//                        break;
//                    case 手续费:
//                        e.setCell(filed.ordinal(), capitalOperation.getFfees().doubleValue());
//                        break;
//                    case 状态:
//                        e.setCell(filed.ordinal(), capitalOperation.getFstatus_s());
//                        break;
//                    case 创建时间:
//                        e.setCell(filed.ordinal(), capitalOperation.getFcreatetime());
//                        break;
//                    case 最后修改时间:
//                        e.setCell(filed.ordinal(), capitalOperation.getFupdatetime());
//                        break;
//                    case 审核人:
//                        e.setCell(filed.ordinal(), capitalOperation.getFadminname());
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//        e.exportXls(response);
//        return AjaxResult.success("导出成功");
//    }
}
