package io.uhha.im.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.utils.StringUtils;
import io.uhha.im.domain.ImUser;
import io.uhha.im.model.ImUserRequest;
import io.uhha.im.service.ImUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author uhha
 * @date 2022-01-18 21:02:36
 * @description im用户Controller
 */
@Api("im用户管理")
@RestController
@RequestMapping("/im/imUser")
public class ImUserController {

    @Autowired
    private ImUserService imUserService;

    private QueryWrapper<ImUser> getQueryWrapper(ImUser imUser) {
        return new QueryWrapper<ImUser>()
                .like(StringUtils.isNotBlank(imUser.getNickName()), "nick_name", imUser.getNickName())
                .eq(StringUtils.isNotBlank(imUser.getUserCode()), "user_code", imUser.getUserCode())
                .eq(StringUtils.isNotBlank(imUser.getType()), "type", imUser.getType())
                .orderByDesc("create_time");
    }

    @ApiOperation("im用户列表")
    @GetMapping("/list")
    public AjaxResult list(ImUser imUser) {
        List<ImUser> list = imUserService.list(getQueryWrapper(imUser));
        return AjaxResult.success(list);
    }

    @ApiOperation("im用户查询userCode")
    @PostMapping("/queryImUserInfo")
    public AjaxResult queryImUserInfo(@Validated @RequestBody ImUserRequest request) {
        return AjaxResult.success(imUserService.queryImUserInfo(request.getType(), request.getUserId()));
    }

    @ApiOperation("im用户查询userCode")
    @GetMapping("/queryImUserInfoByUserCode")
    public AjaxResult queryImUserInfoByUserCode(@RequestParam("userCode") String userCode) {
        return AjaxResult.success(imUserService.queryImUserInfoByUserCode(userCode));
    }

}
