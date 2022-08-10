package io.uhha.web.service;

import io.uhha.common.core.domain.entity.SysUser;

import java.util.Set;

public interface SysStorePermissionService {
    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user);


    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user);
}
