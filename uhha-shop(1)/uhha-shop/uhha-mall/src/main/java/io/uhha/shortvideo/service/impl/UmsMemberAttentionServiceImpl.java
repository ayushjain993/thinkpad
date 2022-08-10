package io.uhha.shortvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.utils.DateUtils;
import io.uhha.shortvideo.domain.UmsMemberAttention;
import io.uhha.shortvideo.mapper.UmsMemberAttentionMapper;
import io.uhha.shortvideo.service.IUmsMemberAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsMemberAttentionServiceImpl extends ServiceImpl<UmsMemberAttentionMapper, UmsMemberAttention> implements IUmsMemberAttentionService {

    @Autowired
    private UmsMemberAttentionMapper umsMemberAttentionMapper;


    @Override
    public int followUser(Long uid, Long followedUid) {
        UmsMemberAttention attention = UmsMemberAttention.builder()
                .createTime(DateUtils.getNowDate())
                .uid(uid)
                .followedUid(followedUid)
                .build();
       return umsMemberAttentionMapper.insert(attention);
    }

    @Override
    public int unfollowUser(Long uid, Long followedUid) {
        QueryWrapper<UmsMemberAttention> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid)
                .eq("followed_uid", followedUid);
        return umsMemberAttentionMapper.delete(queryWrapper);
    }

    @Override
    public Long getFollowerCount(Long uid) {
        QueryWrapper<UmsMemberAttention> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        return umsMemberAttentionMapper.selectCount(queryWrapper);
    }

    @Override
    public Long getFollowingCount(Long uid) {
        QueryWrapper<UmsMemberAttention> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("followed_uid", uid);
        return umsMemberAttentionMapper.selectCount(queryWrapper);
    }

    @Override
    public Boolean isUserfollowed(Long hostId, Long guestId) {
        QueryWrapper<UmsMemberAttention> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("followed_uid", hostId)
        .eq("uid", guestId);
        Long ret = umsMemberAttentionMapper.selectCount(queryWrapper);
        return ret >0;
    }

    @Override
    public List<UmsMemberAttention> getUserAttentions(Long guestId) {
        QueryWrapper<UmsMemberAttention> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", guestId);
        return umsMemberAttentionMapper.selectList(queryWrapper);
    }
}
