package io.uhha.login;


import io.swagger.annotations.*;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.enums.UmsMemberChangeEventEnum;
import io.uhha.member.service.IUmsMemberChangeHistoryService;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.member.service.RegisterService;
import io.uhha.member.service.RegisterServiceApi;
import io.uhha.util.CommonConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 注册控制器
 */
@RestController
@Api(tags = "注册接口")
public class RegisterController {

    /**
     * 注入用户服务
     */
    @Autowired
    private IUmsMemberService customerService;

    /**
     * 注入注册服务
     */
    @Autowired
    private RegisterService registerService;

    /**
     * 注入注册聚合服务
     */
    @Autowired
    private RegisterServiceApi registerServiceApi;

    /**
     * 注入redis 服务接口
     */
    @Autowired
    private RedisCache redisService;

    @Autowired
    private IUmsMemberChangeHistoryService umsMemberChangeHistoryService;


    /**
     * 校验手机号码是否存在
     *
     * @param mobile 手机号码
     * @return 存在返回>0  不存在返回0
     */
    @ResponseBody
    @RequestMapping(value = "/checkmobileexist", method = RequestMethod.POST)
    @UnAuth
    @ApiOperation(value = "校验手机号码是否存在", notes = "校验手机号码是否存在（不需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "mobile", value = "手机号码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "存在返回>0  不存在返回0", response = Integer.class)
    })
    public AjaxResult checkMobileExist(String mobile) {
        return AjaxResult.success(customerService.isMobileExist(mobile));
    }

    /**
     * 发送短信验证码
     * r
     *
     * @param mobile 手机号码
     * @return 0 成功 1 失败 -1 手机号码已经存在
     */
    @UnAuth
    @RequestMapping(value = "/sendmobilecode")
    @ResponseBody
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码（不需要认证）", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "mobile", value = "手机号码"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "0 成功 1 失败 -1 手机号码已经存在", response = Integer.class)
    })
    public AjaxResult sendSmsCode(String mobile) {
        redisService.setCacheObject(String.format("%s_%s", CommonConstant.APPLET_REGISTER_CODE_KEY, mobile),"1234", 5, TimeUnit.MINUTES);
        return AjaxResult.success("1234");
        //   return registerService.sendRegisterSmsCode(mobile, code -> redisService.putToRedis(String.format("%s_%s", CommonConstant.APPLET_REGISTER_CODE_KEY, mobile), code, 5, TimeUnit.MINUTES));
    }


    /**
     * 用户注册
     *
     * @return -1 手机验证码错误 -2 参数错误 0 失败  成功>0  -3 手机号码已存在 -10 推荐人不存在
     */
    @UnAuth
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "用户注册", notes = "用户注册（不需要认证）", httpMethod = "POST")

    @ApiResponses({
            @ApiResponse(code = 200, message = "-1 手机验证码错误 -2 参数错误 0 失败  成功>0  -3 手机号码已存在 -10 推荐人不存在", response = Integer.class)
    })
    public AjaxResult register(@RequestBody RegisterData registerData) {
        int res = 0;
        res = registerServiceApi.registerCustomer(registerData.getCountryCode(), registerData.getMobile(), registerData.getPassword(), registerData.getCode(), registerData.getRecommendCode());

        if(res<0){
            return AjaxResult.success(res);
        }
        //记录用户的注册事件
        umsMemberChangeHistoryService.logMemberEvent(UmsMemberChangeEventEnum.CREATED, registerData.getMobile(),"user");
        return AjaxResult.success(res);
    }

    /**
     * 用户注册实体
     */
    @Data
    @ApiModel(description = "用户注册实体")
    private static class RegisterData {

        /**
         * 国家码
         */
        @ApiModelProperty(value = "国家码")
        private String countryCode;

        /**
         * 手机号码
         */
        @ApiModelProperty(value = "手机号码")
        private String mobile;

        /**
         * 密码
         */
        @ApiModelProperty(value = "密码")
        private String password;

        /**
         * 手机验证码
         */
        @ApiModelProperty(value = "手机验证码")
        private String code;

        /**
         * 推荐码
         */
        @ApiModelProperty(value = "推荐码")
        private String recommendCode;

    }
}

