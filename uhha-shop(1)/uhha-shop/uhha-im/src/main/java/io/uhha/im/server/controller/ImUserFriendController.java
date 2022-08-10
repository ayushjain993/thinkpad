package io.uhha.im.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.StringUtils;
import io.uhha.im.domain.ImUserFriend;
import io.uhha.im.model.ImUserFriendsVo;
import io.uhha.im.service.ImUserFriendService;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author uhha
 * @date 2022-01-18 21:05:05
 * @description 好友列表Controller
 */
@Api("好友列表管理")
@RestController
@RequestMapping("/im/imUserFriend")
public class ImUserFriendController {

    @Autowired
    private ImUserFriendService imUserFriendService;

    private QueryWrapper<ImUserFriend> getQueryWrapper(ImUserFriend imUserFriend) {
        return new QueryWrapper<ImUserFriend>()
                .eq(StringUtils.isNotBlank(imUserFriend.getUserId()), "user_id", imUserFriend.getUserId())
                .eq(StringUtils.isNotBlank(imUserFriend.getFriendId()), "friend_id", imUserFriend.getFriendId())
                .orderByDesc("create_time");
    }

    @ApiOperation("好友列表列表")
    @GetMapping("/list")
    public AjaxResult list(ImUserFriend imUserFriend) {
        List<ImUserFriendsVo> list = imUserFriendService.queryUserFriend(getQueryWrapper(imUserFriend));
        return AjaxResult.success(list);
    }

    @ApiOperation("好友列表删除")
    @DeleteMapping("/remove")
    public AjaxResult remove(ImUserFriend imUserFriend) {
        Asserts.notBlank(imUserFriend.getUserId(), "userid不能为空");
        Asserts.notBlank(imUserFriend.getFriendId(), "friendId不能为空");
        return AjaxResult.success(imUserFriendService.remove(
                new QueryWrapper<ImUserFriend>()
                        .eq("user_id", imUserFriend.getUserId())
                        .eq("friend_id", imUserFriend.getFriendId())));
    }


}
