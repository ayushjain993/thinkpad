package io.uhha.shortvideo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.shortvideo.domain.UmsMemberAttention;

import java.util.List;

public interface IUmsMemberAttentionService extends IService<UmsMemberAttention> {
    public int followUser( Long uid,  Long followedUid);

    public int unfollowUser(Long uid,  Long followedUid);

    public Long getFollowerCount(Long uid);

    /**
     * 用户所关注的用户总和
     * @param uid host用户id
     * @return 用户所关注的用户总和
     */
    public Long getFollowingCount(Long uid);

    /**
     * guest用户是否关注过host
     * @param hostId
     * @param guestId
     * @return
     */
    public Boolean isUserfollowed(Long hostId, Long guestId);

    /**
     * 查询用户所关注的用户列表
     * @param guestId
     * @return
     */
    public List<UmsMemberAttention> getUserAttentions(Long guestId);
}
