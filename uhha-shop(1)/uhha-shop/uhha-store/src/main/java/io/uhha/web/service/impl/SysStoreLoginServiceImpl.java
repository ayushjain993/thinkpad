package io.uhha.web.service.impl;

import io.uhha.common.constant.Constants;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.core.domain.model.LoginUser;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.exception.user.CaptchaException;
import io.uhha.common.exception.user.CaptchaExpireException;
import io.uhha.common.exception.user.UserPasswordNotMatchException;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.MessageUtils;
import io.uhha.common.utils.SecurityUtils;
import io.uhha.common.utils.ServletUtils;
import io.uhha.common.utils.ip.IpUtils;
import io.uhha.framework.manager.AsyncManager;
import io.uhha.framework.manager.factory.AsyncFactory;
import io.uhha.framework.web.service.TokenService;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.system.service.ISysStoreUserService;
import io.uhha.web.service.SysStoreLoginService;
import io.uhha.web.service.SysStorePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class SysStoreLoginServiceImpl implements SysStoreLoginService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 会员服务接口
     */
    @Autowired
    private ISysStoreUserService customerService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private SysStorePermissionService permissionService;

    @Autowired
    private ITStoreInfoService storeInfoService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            log.error("Not able to fetch Captcha code.");
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            log.error("Captcha code doesn't match.");
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户验证
        SysUser iuser = customerService.selectUserByUserName(username);

        if (null == iuser) {
            log.error("user[{}] not found.", username);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        String ipassword = iuser.getPassword();
        if (!(SecurityUtils.matchesPassword(password, ipassword))) {
            log.error("user[{}] password doesn't match.", username);
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        //非管理员用户审核校验
        if(!iuser.isAdmin()) {
            storeInfoService.checkStore(iuser.getStoreId());
        }

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(iuser);
        loginUser.setPermissions(permissionService.getMenuPermission(iuser));

        // 用户验证
        Authentication authentication = null;
        authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        recordLoginInfo(loginUser.getUser());
        // 生成token
        return tokenService.createToken(loginUser);
    }


    public String storeVerifyCodelogin(String phoneNumber) {
        // 用户验证
        SysUser iuser = customerService.selectUserByUserName(phoneNumber);
        if (iuser == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(iuser);
        loginUser.setPermissions(permissionService.getMenuPermission(iuser));

        // 用户验证
        Authentication authentication = null;
        authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        recordLoginInfo(loginUser.getUser());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 记录登录信息
     */
    private void recordLoginInfo(SysUser user) {
        user.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        user.setLoginDate(DateUtils.getNowDate());
        customerService.updateUserProfile(user);
    }

}
