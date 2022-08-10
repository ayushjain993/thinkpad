package io.uhha.im.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.common.core.domain.entity.SysUser;
import io.uhha.im.domain.ImUser;
import io.uhha.im.domain.ImUserFriend;
import io.uhha.im.model.ImUserFriendsVo;

import java.util.List;

/**
 * @author uhha
 * @date 2022-01-18 21:05:05
 * @description 好友列表Service接口
 */
public interface ImUserFriendService extends IService<ImUserFriend> {


    /**
     * 根据用户的ID 获取 用户好友(双向用户关系)
     *
     * @param userId 用户ID
     * @return 好友分组的列表
     */
    List<SysUser> getUserFriends(String userId);

    /**
     * 添加好友
     *
     * @param customerId
     * @param friendId
     * @return
     */
    boolean addUserFriend(String customerId, String friendId);

    /**
     * 绑定好友关系
     *
     * @param sender
     * @param receiver
     */
    void bindRelationship(String sender, String receiver);

    List<ImUserFriendsVo> queryUserFriend(QueryWrapper<ImUserFriend> queryWrapper);
}
