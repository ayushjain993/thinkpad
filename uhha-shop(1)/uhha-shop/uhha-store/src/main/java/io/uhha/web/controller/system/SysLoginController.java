package io.uhha.web.controller.system;

import io.swagger.annotations.ApiOperation;
import io.uhha.common.constant.Constants;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.domain.entity.SysMenu;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.common.core.domain.model.LoginBody;
import io.uhha.common.core.domain.model.LoginUser;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.utils.SecurityUtils;
import io.uhha.common.utils.ServletUtils;
import io.uhha.common.utils.StringUtils;
import io.uhha.framework.web.service.SysLoginService;
import io.uhha.framework.web.service.SysPermissionService;
import io.uhha.framework.web.service.TokenService;
import io.uhha.store.domain.TStoreInfo;
import io.uhha.store.service.ITStoreInfoService;
import io.uhha.system.service.ISysStoreMenuService;
import io.uhha.system.service.ISysStoreUserService;
import io.uhha.web.service.SysStoreLoginService;
import io.uhha.web.service.SysStorePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private SysStoreLoginService storeLoginService;

    @Autowired
    private ISysStoreMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private SysStorePermissionService sysStorePermissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ITStoreInfoService storeInfoService;

    @Autowired
    private ISysStoreUserService storeUserService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/store/login")
    public AjaxResult storeLogin(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = storeLoginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success();

        if(user.getStoreId()==0) {
            // 角色集合
            Set<String> roles = permissionService.getRolePermission(user);
            // 权限集合
            Set<String> permissions = permissionService.getMenuPermission(user);
            ajax.put("roles", roles);
            ajax.put("permissions", permissions);
        }
        else {
            // 角色集合
            Set<String> roles = sysStorePermissionService.getRolePermission(user);
            // 权限集合
            Set<String> permissions = sysStorePermissionService.getMenuPermission(user);
            ajax.put("roles", roles);
            ajax.put("permissions", permissions);
            TStoreInfo storeInfo=storeInfoService.selectTStoreInfoById(user.getStoreId());
            ajax.put("storeInfo", storeInfo);
        }
        ajax.put("user", user);

        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     *
     * @param phoneNumber
     * @param password
     * @param storeName
     * @param verifyCode
     * @return
     */
    @ApiOperation(value = "用户注册",tags = {"用户注册"}, notes = "用户注册")
    @PostMapping("/storegister")
    public AjaxResult useregister(String phoneNumber, String password, String storeName, String verifyCode)
    {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\W]{6,12}$";
        SysUser user=new SysUser();
        user.setPhonenumber(phoneNumber);
        user.setUserName(phoneNumber);
        user.setPassword(password);
        if (!password.matches(regex)){
            return AjaxResult.error("密码不符合");
        }
        String redisverifyCode=(String) redisCache.getCacheObject(phoneNumber);
        if(StringUtils.isBlank(redisverifyCode)) {
            return AjaxResult.error("验证码失效");
        }
        if (!(redisverifyCode.equals(verifyCode))) {
            return AjaxResult.error("验证码验证错误");
        }
        user.setCreateBy("");
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        Long[] roleIds=new Long[] {new Long(3)};   //农资店用户
        user.setRoleIds(roleIds);

        TStoreInfo istroeInfo=new TStoreInfo();
        istroeInfo.setStoreName(storeName);
        istroeInfo.setStatus("0"); //新注册商铺状态修改为0
        //TODO
//        Long storeId=storeInfoService.insertTStoreInfo(istroeInfo);
//        user.setStoreId(storeId);
        storeUserService.insertUser(user);
        return AjaxResult.success("用户申请完成，审核通过后可登录系统！");
    }
}
