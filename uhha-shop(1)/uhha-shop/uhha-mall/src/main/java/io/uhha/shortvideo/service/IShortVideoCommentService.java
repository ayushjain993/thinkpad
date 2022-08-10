package io.uhha.shortvideo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.shortvideo.domain.ShortVideoComment;

import java.util.List;

/**
 * 短视频评论Service接口
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
public interface IShortVideoCommentService extends IService<ShortVideoComment>
{
    /**
     * 查询短视频评论
     * 
     * @param id 短视频评论主键
     * @return 短视频评论
     */
    public ShortVideoComment selectShortVideoCommentById(String id);

    /**
     * 查询短视频评论列表
     * 
     * @param shortVideoComment 短视频评论
     * @return 短视频评论集合
     */
    public List<ShortVideoComment> selectShortVideoCommentList(ShortVideoComment shortVideoComment);

    /**
     * 新增短视频评论
     * 
     * @param shortVideoComment 短视频评论
     * @return 结果
     */
    public int insertShortVideoComment(ShortVideoComment shortVideoComment);

    /**
     * 修改短视频评论
     * 
     * @param shortVideoComment 短视频评论
     * @return 结果
     */
    public int updateShortVideoComment(ShortVideoComment shortVideoComment);

    /**
     * 批量删除短视频评论
     * 
     * @param ids 需要删除的短视频评论主键集合
     * @return 结果
     */
    public int deleteShortVideoCommentByIds(String[] ids);

    /**
     * 删除短视频评论信息
     * 
     * @param id 短视频评论主键
     * @return 结果
     */
    public int deleteShortVideoCommentById(String id);

    /**
     * 根据视频id删除短视频评论信息
     *
     * @param videoId 短视频评论主键
     * @return 结果
     */
    public int deleteShortVideoCommentByVideoId(String videoId);

    /**
     * 根据视频id获取评论列表
     * @param vid 视频id
     * @return 视频评论列表
     */
    public List<ShortVideoComment> selectShortVideoCommentByVideoId(String vid);

    public List<ShortVideoComment> selectShortVideoCommentByVideoIdAndParentId(String vid, Long parentId);
}
