package io.uhha.web.controller.marketing;

import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.marketing.domain.CrowdfundingProgress;
import io.uhha.marketing.service.CrowdfundingProgressService;
import io.uhha.order.vo.ConfirmOrderParams;
import io.uhha.order.vo.GroupOrder;
import io.uhha.util.CommonConstant;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by MJ on 2020/6/17.
 * 众筹订单控制器
 */
@RestController
@Api(tags = "众筹相关接口")
public class CrowdFundingController extends BaseController {
    @Autowired
    private CrowdfundingProgressService crowdfundingProgressService;

    /**
     * 分页查询众筹订单
     *
     * @return 返回众筹订单信息
     */
    @GetMapping("/crowdfunding")
    @ApiOperation(value = "分页查询众筹订单", notes = "分页查询众筹订单（需要认证）")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "marketingId", value = "活动id"),
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "storeId", value = "商户编号"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回众筹订单列表", response = CrowdfundingProgress.class)
    })
    public TableDataInfo list(long marketingId, long  storeId){
        startPage();
        List<CrowdfundingProgress> crowdfundingProgressList = crowdfundingProgressService.queryCrowdfundingProgressByMarketingId(marketingId,storeId);
        return getDataTable(crowdfundingProgressList);
    }

}
