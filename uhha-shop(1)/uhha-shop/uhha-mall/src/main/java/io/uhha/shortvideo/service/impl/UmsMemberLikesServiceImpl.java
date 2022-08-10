package io.uhha.shortvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.enums.UmsLikeTypeEnum;
import io.uhha.shortvideo.domain.ShortVideo;
import io.uhha.shortvideo.domain.ShortVideoComment;
import io.uhha.shortvideo.domain.UmsMemberLikes;
import io.uhha.shortvideo.mapper.UmsMemberLikesMapper;
import io.uhha.shortvideo.service.IShortVideoCommentService;
import io.uhha.shortvideo.service.IShortVideoService;
import io.uhha.shortvideo.service.IUmsMemberLikesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UmsMemberLikesServiceImpl extends ServiceImpl<UmsMemberLikesMapper, UmsMemberLikes> implements IUmsMemberLikesService {
    @Autowired
    private UmsMemberLikesMapper umsMemberLikesMapper;

    @Autowired
    private IShortVideoService shortVideoService;

    @Autowired
    private IShortVideoCommentService shortVideoCommentService;

    @Override
    public Long selectLikesCountByResourceId(Long resourceId) {
        QueryWrapper<UmsMemberLikes> queryWrapper = new QueryWrapper();
        queryWrapper.eq("resource_id", resourceId);
        Long count = umsMemberLikesMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public Long selectLikesCountByUid(Long uid) {
        return umsMemberLikesMapper.selectLikesCountByUid(uid);
    }

    /**
     * 查询用户点赞的视频
     *
     * @param uid 用户id
     * @return 点赞列表
     */
    @Override
    public List<UmsMemberLikes> selectLikesByUid(Long uid) {
        QueryWrapper<UmsMemberLikes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        return umsMemberLikesMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean isResourceLikedByUser(Long uid, String resoureId) {
        QueryWrapper<UmsMemberLikes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid).eq("resource_id", resoureId);
        Long ret = umsMemberLikesMapper.selectCount(queryWrapper);

        return ret > 0;
    }

    @Override
    public UmsMemberLikes selectUmsMemberLikesById(Long id) {
        return umsMemberLikesMapper.selectUmsMemberLikesById(id);
    }

    @Override
    public List<UmsMemberLikes> selectUmsMemberLikesList(UmsMemberLikes umsMemberLikes) {
        return umsMemberLikesMapper.selectUmsMemberLikesList(umsMemberLikes);
    }

    @Override
    public int insertUmsMemberLikes(UmsMemberLikes umsMemberLikes) {
        return umsMemberLikesMapper.insertUmsMemberLikes(umsMemberLikes);
    }

    @Override
    public int updateUmsMemberLikes(UmsMemberLikes umsMemberLikes) {
        return umsMemberLikesMapper.updateUmsMemberLikes(umsMemberLikes);
    }

    @Override
    public int deleteUmsMemberLikesById(Long id) {
        return umsMemberLikesMapper.deleteUmsMemberLikesById(id);
    }

    @Override
    public int deleteUmsMemberLikesByIds(Long[] ids) {
        return umsMemberLikesMapper.deleteUmsMemberLikesByIds(ids);
    }

    private void calculateResourceLikesQty(String resourceType, List<String> resourceIds) {
        resourceIds.forEach(x -> {
            QueryWrapper<UmsMemberLikes> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("resource_id", x);
            Long likes = umsMemberLikesMapper.selectCount(queryWrapper);
            if (UmsLikeTypeEnum.VIDEO.getCode().equalsIgnoreCase(resourceType)) {
                ShortVideo shortVideo = shortVideoService.selectShortVideoById(x);
                if (shortVideo != null) {
                    shortVideo.setLikedqty(likes);
                    shortVideoService.updateShortVideo(shortVideo);
                }
            } else if (UmsLikeTypeEnum.COMMENT.getCode().equalsIgnoreCase(resourceType)) {
                ShortVideoComment shortVideoComment = shortVideoCommentService.selectShortVideoCommentById(x);
                if (shortVideoComment != null) {
                    shortVideoComment.setLikedQty(likes);
                    shortVideoCommentService.updateShortVideoComment(shortVideoComment);
                }
            } else {
                log.error("invalid resourceType: {} for resource: {}", resourceType, x);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateAndUpdateLikesQty(List<UmsMemberLikes> umsMemberLikes) {

        umsMemberLikes.forEach(x -> {
            String resourceId = x.getResourceId();
            String resourceType = x.getType();
            QueryWrapper<UmsMemberLikes> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("resource_id", resourceId);
            Long likes = umsMemberLikesMapper.selectCount(queryWrapper);

            if (UmsLikeTypeEnum.VIDEO.getCode().equalsIgnoreCase(resourceType)) {
                ShortVideo shortVideo = shortVideoService.selectShortVideoById(resourceId);
                if (shortVideo != null) {
                    shortVideo.setLikedqty(likes);
                    shortVideoService.updateShortVideo(shortVideo);
                }
            } else if (UmsLikeTypeEnum.COMMENT.getCode().equalsIgnoreCase(resourceType)) {
                ShortVideoComment shortVideoComment = shortVideoCommentService.selectShortVideoCommentById(resourceId);
                if (shortVideoComment != null) {
                    shortVideoComment.setLikedQty(likes);
                    shortVideoCommentService.updateShortVideoComment(shortVideoComment);
                }
            }
        });


    }

    @Override
    public boolean checkUserLiked(Long uid, String resourceId) {
        QueryWrapper<UmsMemberLikes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        queryWrapper.eq("resource_id", resourceId);
        return umsMemberLikesMapper.selectCount(queryWrapper) > 0L;
    }

}
