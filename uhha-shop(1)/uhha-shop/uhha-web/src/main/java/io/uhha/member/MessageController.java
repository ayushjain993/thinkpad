package io.uhha.member;

import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.setting.domain.LsStationLetter;
import io.uhha.setting.service.ILsStationLetterService;
import io.uhha.util.PageHelper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mj on 18/6/14.
 * 消息控制器
 */
@Api(tags = "通知消息接口")
@RestController
@Slf4j
public class MessageController {

    /**
     * 注入消息服务接口
     */
    @Autowired
    private ILsStationLetterService stationLetterService;

    /**
     * 查询用户消息
     *
     * @param pageHelper 分页帮助类
     * @return 返回用户消息
     */
    @RequestMapping(value = "/querymessages", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户消息", notes = "查询用户消息（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageNum", value = "当前页"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "pageSize", value = "每页显示的记录数"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户消息", response = LsStationLetter.class)
    })
    public AjaxResult queryMessages(HttpServletRequest request, @ApiIgnore PageHelper<LsStationLetter> pageHelper) {
        return AjaxResult.success(stationLetterService.queryStationLettersByCustomerId(pageHelper, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 查询用户消息
     *
     * @return 返回用户消息
     */
    @GetMapping("/querymessage")
    @ApiOperation(value = "查询用户消息", notes = "查询用户消息（需要认证）", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户消息", response = LsStationLetter.class)
    })
    public AjaxResult querymessage(@RequestParam("id") Long id) {
        return AjaxResult.success(stationLetterService.selectLsStationLetterById(id));
    }

    /**
     * 查询用户未读的站内信
     *
     * @return 返回用户未读的站内信
     */
    @RequestMapping(value = "/message/total")
    @ResponseBody
    @ApiOperation(value = "查询用户未读的站内信数量", notes = "查询用户未读的站内信数量（需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户未读的站内信数量", response = Integer.class)
    })
    public AjaxResult queryUnReadMessagecount(HttpServletRequest request) {
        return AjaxResult.success(stationLetterService.unReadNum(AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 更新消息阅读状态
     *
     * @param id 消息id
     * @return 返回成功1 失败0
     */
    @PostMapping("/updatemessageisread")
    @ResponseBody
    @ApiOperation(value = "更新消息阅读状态", notes = "更新消息阅读状态（需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "long", name = "id", value = "消息id"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回成功1 失败0", response = Integer.class)
    })
    public AjaxResult updateMessageIsRead(@RequestParam("id") Long id, HttpServletRequest request) {
        return AjaxResult.success(stationLetterService.updateStationLettersIsRead(id, AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 删除消息
     *
     * @param id 消息ids
     * @return 返回成功1 失败0
     */
    @PostMapping("/deletemessage/{id}")
    public AjaxResult deletemessage(@PathVariable("id") Long id) {
        return AjaxResult.success(stationLetterService.deleteStationLetter(id));
    }

    /**
     * 删除用户全部的消息
     */
    @PostMapping("/cleanallmessages")
    public AjaxResult cleanallmessages(HttpServletRequest request){
        return AjaxResult.success(stationLetterService.cleanStationLetters(AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 删除用户全部的消息
     */
    @PostMapping("/readallmessages")
    public AjaxResult readallmessages(HttpServletRequest request){
        return AjaxResult.success(stationLetterService.readAllStationLetters(AppletsLoginUtils.getInstance().getCustomerId(request)));
    }


}
