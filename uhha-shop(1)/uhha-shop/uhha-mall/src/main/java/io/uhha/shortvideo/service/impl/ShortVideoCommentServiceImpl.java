package io.uhha.shortvideo.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.uhha.shortvideo.mapper.ShortVideoCommentMapper;
import io.uhha.shortvideo.domain.ShortVideoComment;
import io.uhha.shortvideo.service.IShortVideoCommentService;

/**
 * 短视频评论Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Service
public class ShortVideoCommentServiceImpl extends ServiceImpl<ShortVideoCommentMapper, ShortVideoComment> implements IShortVideoCommentService
{
    @Autowired
    private ShortVideoCommentMapper shortVideoCommentMapper;

    /**
     * 查询短视频评论
     * 
     * @param id 短视频评论主键
     * @return 短视频评论
     */
    @Override
    public ShortVideoComment selectShortVideoCommentById(String id)
    {
        return shortVideoCommentMapper.selectShortVideoCommentById(id);
    }

    /**
     * 查询短视频评论列表
     * 
     * @param shortVideoComment 短视频评论
     * @return 短视频评论
     */
    @Override
    public List<ShortVideoComment> selectShortVideoCommentList(ShortVideoComment shortVideoComment)
    {
        return shortVideoCommentMapper.selectShortVideoCommentList(shortVideoComment);
    }

    /**
     * 新增短视频评论
     * 
     * @param shortVideoComment 短视频评论
     * @return 结果
     */
    @Override
    public int insertShortVideoComment(ShortVideoComment shortVideoComment)
    {
        shortVideoComment.setCreateTime(DateUtils.getNowDate());
        return shortVideoCommentMapper.insertShortVideoComment(shortVideoComment);
    }

    /**
     * 修改短视频评论
     * 
     * @param shortVideoComment 短视频评论
     * @return 结果
     */
    @Override
    public int updateShortVideoComment(ShortVideoComment shortVideoComment)
    {
        return shortVideoCommentMapper.updateShortVideoComment(shortVideoComment);
    }

    /**
     * 批量删除短视频评论
     * 
     * @param ids 需要删除的短视频评论主键
     * @return 结果
     */
    @Override
    public int deleteShortVideoCommentByIds(String[] ids)
    {
        return shortVideoCommentMapper.deleteShortVideoCommentByIds(ids);
    }

    /**
     * 删除短视频评论信息
     * 
     * @param id 短视频评论主键
     * @return 结果
     */
    @Override
    public int deleteShortVideoCommentById(String id)
    {
        return shortVideoCommentMapper.deleteShortVideoCommentById(id);
    }

    @Override
    public int deleteShortVideoCommentByVideoId(String videoId) {
        QueryWrapper<ShortVideoComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId);
        return shortVideoCommentMapper.delete(queryWrapper);
    }

    @Override
    public List<ShortVideoComment> selectShortVideoCommentByVideoId(String vid) {
        return shortVideoCommentMapper.selectShortVideoCommentByVideoId(vid);
    }

    @Override
    public List<ShortVideoComment> selectShortVideoCommentByVideoIdAndParentId(String vid, Long parentId) {
        return shortVideoCommentMapper.selectShortVideoCommentByVideoIdAndParentId(vid, parentId);
    }
}
