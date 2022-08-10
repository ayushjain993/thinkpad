package io.uhha.order;


import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.order.domain.OmsOrder;
import io.uhha.order.service.OrderServiceApi;
import io.uhha.order.vo.Evaluation;
import io.uhha.order.vo.OrderItem;
import io.uhha.util.CommonConstant;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 评价控制器
 */
@Controller
@Api(tags = "评价接口")
public class EvaluationController {


    /**
     * 注入订单混合api
     */
    @Autowired
    private OrderServiceApi orderServiceApi;


    /**
     * 查询订单详情
     *
     * @return 返回订单详情(评价页面)
     */
    @RequestMapping("/queryorderforevaluation")
    @ResponseBody
    @ApiOperation(value = "查询订单详情", notes = "查询订单详情（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "orderId", value = "订单ID"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回订单详情(评价页面)", response = OmsOrder.class)
    })
    public AjaxResult queryOrderForEvaluation(HttpServletRequest request, Long orderId) {
        return AjaxResult.success(orderServiceApi.queryOrderDetailById(orderId, AppletsLoginUtils.getInstance().getCustomerId(request), CommonConstant.QUERY_WITH_NO_STORE, OrderItem.SKUS, OrderItem.STORE_INFO));
    }


    /**
     * 添加订单评论
     *
     * @param evaluationParams 订单评论
     * @return 成功返回1 失败返回0 -1 订单不存在  -2 订单状态错误
     */
    @RequestMapping("/addorderevaluation")
    @ResponseBody
    @ApiOperation(value = "添加订单评论", notes = "添加订单评论（需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功返回1 失败返回0 -1 订单不存在  -2 订单状态错误", response = Integer.class)
    })
    public AjaxResult addOrderEvaluation(HttpServletRequest request, @RequestBody Evaluation evaluationParams) {
        evaluationParams.setCustomerId(AppletsLoginUtils.getInstance().getCustomerId(request));
        return AjaxResult.success(orderServiceApi.addOrderEvaluation(evaluationParams));
    }

    /**
     * 查询订单评价详情
     *
     * @param orderId 订单id
     * @return 返回订单评价详情
     */
    @RequestMapping(value = "/evaluationdetail")
    @ResponseBody
    @ApiOperation(value = "查询订单评价详情", notes = "查询订单评价详情（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "orderId", value = "订单ID"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回订单评价详情", response = Evaluation.class)
    })
    public AjaxResult queryEvaluationDetail(HttpServletRequest request, Long orderId) {
        return AjaxResult.success(orderServiceApi.queryOrderEvaluation(orderId, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }


}
