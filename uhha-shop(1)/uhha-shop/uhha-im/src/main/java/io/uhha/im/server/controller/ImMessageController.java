package io.uhha.im.server.controller;

import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.bean.BeanUtils;
import io.uhha.im.domain.Message;
import io.uhha.im.model.ImMessageRequest;
import io.uhha.im.server.component.push.DefaultMessagePusher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.uhha.im.service.ImUserFriendService;
import io.uhha.im.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/im/message")
@Api(produces = "application/json", tags = "消息相关接口")
public class ImMessageController {

    @Resource
    private DefaultMessagePusher defaultMessagePusher;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ImUserFriendService userFriendService;

    @ApiOperation(httpMethod = "POST", value = "发送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sender", value = "发送者UID", paramType = "query", dataTypeClass = String.class, required = true, example = ""),
            @ApiImplicitParam(name = "receiver", value = "接收者UID", paramType = "query", dataTypeClass = String.class, required = true, example = ""),
            @ApiImplicitParam(name = "action", value = "消息动作", paramType = "query", dataTypeClass = String.class, required = true, example = ""),
            @ApiImplicitParam(name = "title", value = "消息标题", paramType = "query", dataTypeClass = String.class, example = ""),
            @ApiImplicitParam(name = "content", value = "消息内容", paramType = "query", dataTypeClass = String.class, example = ""),
            @ApiImplicitParam(name = "format", value = "消息格式", paramType = "query", dataTypeClass = String.class, example = ""),
            @ApiImplicitParam(name = "extra", value = "扩展字段", paramType = "query", dataTypeClass = String.class, example = ""),
    })
    @PostMapping(value = "/send")
    public AjaxResult send(@RequestParam String sender,
                           @RequestParam String receiver,
                           @RequestParam String action,
                           @RequestParam Integer type,
                           @RequestParam(required = false) String title,
                           @RequestParam(required = false) String content,
                           @RequestParam(required = false) String format,
                           @RequestParam(required = false) String extra) {

        io.uhha.im.server.model.Message message = new io.uhha.im.server.model.Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setAction(action);
        message.setType(type);
        message.setContent(content);
        message.setFormat(format);
        message.setTitle(title);
        message.setExtra(extra);
        message.setId(System.currentTimeMillis());
        userFriendService.bindRelationship(message.getSender(), message.getReceiver());
        io.uhha.im.domain.Message msg = new io.uhha.im.domain.Message();
        BeanUtils.copyBeanProp(msg, message);
        msg.setReadStatus(Boolean.FALSE);
        msg.setId(null);
        messageService.save(msg);
        defaultMessagePusher.push(message);
        return AjaxResult.success(message.getId());
    }

    @ApiOperation("im消息列表")
    @GetMapping("/list")
    public AjaxResult list(@Validated ImMessageRequest imMessageRequest) {
        List<Message> messages = messageService.listMessage(imMessageRequest);
        return AjaxResult.success(messages);
    }

}
