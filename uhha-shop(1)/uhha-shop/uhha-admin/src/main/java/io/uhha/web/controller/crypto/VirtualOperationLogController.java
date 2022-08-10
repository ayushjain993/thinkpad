package io.uhha.web.controller.crypto;


import io.swagger.annotations.Api;
import io.uhha.coin.capital.domain.FLogConsoleVirtualRecharge;
import io.uhha.coin.common.Enum.OperationlogEnum;
import io.uhha.coin.common.dto.common.Pagination;
import io.uhha.common.utils.Utils;
import io.uhha.coin.user.service.IAdminUserCapitalService;
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
import java.util.ArrayList;
import java.util.List;

@Api(tags = "加密货币手工充值操作日志Controller")
@RestController
@RequestMapping("/crypto/manualRecharge")
public class VirtualOperationLogController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(VirtualOperationLogController.class);

    @Autowired
    private IAdminUserCapitalService adminUserCapitalService;

    /**
     * 虚拟币手工充值列表
     */
    @GetMapping
    public Pagination<FLogConsoleVirtualRecharge>  list(
            @RequestParam(value = "logDate", required = false) String logDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "ftype", defaultValue = "0") Integer type,
            @RequestParam(value = "coinType",defaultValue = "-1")Integer coinType,
            @RequestParam(value = "keywords", required = false) String keyWord,
            @RequestParam(value = "orderField", defaultValue = "fcreatetime") String orderField,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection) throws Exception {

        Pagination<FLogConsoleVirtualRecharge> pageParam = new Pagination<FLogConsoleVirtualRecharge>(currentPage, 20);
        // 排序条件
        pageParam.setOrderField(orderField);
        pageParam.setOrderDirection(orderDirection);

        FLogConsoleVirtualRecharge filterParam = new FLogConsoleVirtualRecharge();
        List<Integer> status = new ArrayList<>();
        // 关键字
        if (!StringUtils.isEmpty(keyWord)) {
            pageParam.setKeyword(keyWord);
        }
        // 开始时间
        if (!StringUtils.isEmpty(logDate)) {
            pageParam.setBegindate(logDate);
        }
        // 结束时间
        if (!StringUtils.isEmpty(endDate)) {
            pageParam.setEnddate(endDate);
        }

        if (type != null && type > 0) {
            filterParam.setFtype(type);
        }
        if(coinType != -1){
            filterParam.setFcoinid(coinType);
        }

        return adminUserCapitalService.selectConsoleVirtualRechargeList(pageParam, filterParam, status);
    }

    /**
     * 新增
     */
    @PostMapping
    @ResponseBody
    public AjaxResult saveVirtualOperationLog(
            HttpServletRequest request,
            @RequestParam(value = "userLookup.id", required = true) Integer userId,
            @RequestParam(value = "vid", required = true) Integer vid,
            @RequestParam(value = "ftype", required = true, defaultValue = "1") Integer type,
            @RequestParam(value = "famount", required = true) BigDecimal famount,
            @RequestParam(value = "finfo", required = true) String finfo,
            @RequestParam(value = "fisSendMsg", required = false) String fisSendMsg) throws Exception {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        FLogConsoleVirtualRecharge virtualoperationlog = new FLogConsoleVirtualRecharge();

        virtualoperationlog.setFtype(type);
        virtualoperationlog.setFamount(famount);
        virtualoperationlog.setFcoinid(vid);
        virtualoperationlog.setFuid(admin.getUserId());
        virtualoperationlog.setFstatus(OperationlogEnum.SAVE);
        virtualoperationlog.setFcreatetime(Utils.getTimestamp());
        virtualoperationlog.setFinfo(finfo);
        if (fisSendMsg != null) {
            virtualoperationlog.setFissendmsg(1);
        } else {
            virtualoperationlog.setFissendmsg(0);
        }

        virtualoperationlog.setVersion(0);
        virtualoperationlog.setFcreatorid(admin.getUserId());
        adminUserCapitalService.insertConsoleVirtualRecharge(virtualoperationlog);

        return AjaxResult.success("新增成功!");
    }

    /**
     * 删除
     */
    @DeleteMapping
    @ResponseBody
    public AjaxResult deleteVirtualOperationLog(
            @RequestParam(value = "uid", required = true) Integer fid) throws Exception {
        FLogConsoleVirtualRecharge virtualoperationlog = adminUserCapitalService.selectConsoleVirtualRechargeById(fid);
        if (virtualoperationlog.getFstatus() != OperationlogEnum.SAVE) {
            return AjaxResult.error("删除失败，记录已审核");
        }
        adminUserCapitalService.deleteConsoleVirtualRechargeById(fid);
        return AjaxResult.success("删除成功");
    }

    /**
     * 审核
     */
    @PutMapping("/audit")
    @ResponseBody
    public AjaxResult auditVirtualOperationLog(
            @RequestParam(value = "uid", required = true) String fid) throws Exception {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        boolean flag = false;
        String [] fids=fid.split(",");
        StringBuilder sb=new StringBuilder();
        StringBuilder examine=new StringBuilder();
        for (String id: fids){
            FLogConsoleVirtualRecharge virtualoperationlog = adminUserCapitalService.selectConsoleVirtualRechargeById(Integer.valueOf(id));
            if (virtualoperationlog.getFstatus() != OperationlogEnum.SAVE) {
                examine.append(id).append(",");
                continue;
            }
            try {
                virtualoperationlog.setFstatus(OperationlogEnum.FFROZEN);
                virtualoperationlog.setFcreatorid(admin.getUserId());
                flag = adminUserCapitalService.updateConsoleVirtualRecharge(virtualoperationlog);
            } catch (Exception e) {
                logger.error(e.toString());
                e.printStackTrace();
                flag = false;
            }
            if (!flag) {
                sb.append(id).append(",");
            }
        }
        StringBuilder string=new StringBuilder();
        if (sb.toString().length() !=0 && !sb.toString().equals("")){
            string.append("ID为"+sb.substring(0,sb.length()-1)+"的信息审核失败，");
        }
        if (examine.toString().length() !=0 && !examine.toString().equals("")){
            string.append("ID为"+examine.substring(0,examine.length()-1)+"的信息已审核。");
        }
        if(string.toString().length() !=0 && !string.toString().equals("")){
            return AjaxResult.success(string.toString());
        }
        return AjaxResult.success("审核成功");
    }

    /**
     * 发放
     */
    @PostMapping("/send")
    @ResponseBody
    public AjaxResult sendVirtualOperationLog(
            @RequestParam(value = "uid", required = true) String fid) throws Exception {
        LoginUser admin = AdminLoginUtils.getInstance().getManager();

        boolean flag = false;
        String [] fids=fid.split(",");
        StringBuilder sb=new StringBuilder();
        StringBuilder grant=new StringBuilder();
        for (String id: fids){
            FLogConsoleVirtualRecharge virtualoperationlog = adminUserCapitalService.selectConsoleVirtualRechargeById(Integer.valueOf(id));
            if (virtualoperationlog.getFstatus() != OperationlogEnum.FFROZEN) {
                grant.append(id).append(",");
                continue;
            }
            try {
                virtualoperationlog.setFstatus(OperationlogEnum.AUDIT);
                virtualoperationlog.setFcreatorid(admin.getUserId());
                virtualoperationlog.setFcreatetime(Utils.getTimestamp());
                flag = adminUserCapitalService.updateConsoleVirtualRecharge(virtualoperationlog);

            } catch (Exception e) {
                logger.error(e.toString());
                e.printStackTrace();
                flag = false;
            }
            if (!flag) {
                sb.append(id).append(",");
            }
        }
        StringBuilder string=new StringBuilder();
        if (sb.toString().length() !=0 && !sb.toString().equals("")){
            string.append("ID为"+sb.substring(0,sb.length()-1)+"的信息发放失败，");
        }
        if (grant.toString().length() !=0 && !grant.toString().equals("")){
            string.append("ID为"+grant.substring(0,grant.length()-1)+"的信息状态不为冻结。");
        }
        if(string.toString().length() !=0 && !string.toString().equals("")){
            return AjaxResult.success(string.toString());
        }
        return AjaxResult.success("发放成功");
    }

}
