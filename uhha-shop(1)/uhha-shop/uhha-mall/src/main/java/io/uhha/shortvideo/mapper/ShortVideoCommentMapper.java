package io.uhha.shortvideo.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.shortvideo.domain.ShortVideoComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 短视频评论Mapper接口
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Mapper
public interface ShortVideoCommentMapper extends BaseMapper<ShortVideoComment>
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
     * 删除短视频评论
     *
     * @param id 短视频评论主键
     * @return 结果
     */
    public int deleteShortVideoCommentById(String id);

    /**
     * 批量删除短视频评论
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShortVideoCommentByIds(String[] ids);

    public List<ShortVideoComment> selectShortVideoCommentByVideoId(@Param("videoId") String videoId);
    public List<ShortVideoComment> selectShortVideoCommentByVideoIdAndParentId(@Param("videoId")String videoId, @Param("parentId") Long parentId);
}
