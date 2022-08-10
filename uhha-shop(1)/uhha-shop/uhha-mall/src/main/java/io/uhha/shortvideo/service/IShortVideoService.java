package io.uhha.shortvideo.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.shortvideo.domain.ShortVideo;
import io.uhha.shortvideo.domain.ShortVideoComment;
import io.uhha.shortvideo.vo.ShortVideoApplyVo;
import io.uhha.shortvideo.vo.ShortVideoVo;
import io.uhha.shortvideo.vo.ShortVideoWatchVo;

/**
 * 短视频Service接口
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
public interface IShortVideoService extends IService<ShortVideo>
{
    /**
     * 查询短视频
     *
     * @param videoId 短视频主键
     * @return 短视频
     */
    public ShortVideo selectShortVideoById(String videoId) ;

    /**
     * 查询短视频列表
     * 
     * @param shortVideo 短视频
     * @return 短视频集合
     */
    public List<ShortVideo> selectShortVideoList(ShortVideo shortVideo);

    /**
     * 查询短视频（已登录）
     *
     * @param uid 用户id
     * @param videoId 短视频主键
     * @return 短视频
     */
    public ShortVideoVo getShortVideoByIdAndUid(Long uid, String videoId) ;

    /**
     * 查询短视频（未登录）
     *
     * @param videoId 短视频主键
     * @return 短视频
     */
    public ShortVideoVo getShortVideoById(String videoId) ;

    /**
     * 根据uid查询用户的视频列表
     *
     * @param uid 用户id
     * @return 短视频集合
     */
    public List<ShortVideoVo> getShortVideoListByUID(long uid);

    /**
     * 关注的视频列表
     *
     * @param uid 用户id
     * @return 短视频集合
     */
    public List<ShortVideoVo> getFollowedVideoListByUID(long uid);

    /**
     * 用户喜欢的视频列表
     *
     * @param uid 用户id
     * @return 短视频集合
     */
    public List<ShortVideoVo> getLikedVideoListByUID(long uid);

    /**
     * 查询用户推荐的视频列表
     *
     * @param uid 用户id
     * @return 短视频集合
     */
    public List<ShortVideoVo> getRecommendedVideoListByUID(long uid);

    /**
     * 查询用户推荐的视频列表
     *
     * @return 短视频集合
     */
    public List<ShortVideoVo> getRecommendedVideoList();

    /**
     * 根据经纬度查询附近的视频
     *
     * @param uid 用户id
     * @param latitude 维度
     * @param longitude 经度
     * @return 短视频集合
     */
    public List<ShortVideo> getVideoListByUidAndLocation(long uid, long range, String longitude, String latitude);

    /**
     * 根据经纬度和Uid查询附近的视频
     *
     * @param latitude 维度
     * @param longitude 经度
     * @return 短视频集合
     */
    public List<ShortVideo> getVideoListByLocation(long range, String longitude, String latitude);

    /**
     * 新增短视频
     * 
     * @param shortVideo 短视频
     * @return 结果
     */
    public int insertShortVideo(ShortVideo shortVideo);

    /**
     * 修改短视频
     * 
     * @param shortVideo 短视频
     * @return 结果
     */
    public int updateShortVideo(ShortVideo shortVideo);

    /**
     * 批量删除短视频
     * 
     * @param ids 需要删除的短视频主键集合
     * @return 结果
     */
    public int deleteShortVideoByIds(String[] ids);

    /**
     * 删除短视频信息
     * 
     * @param videoId 短视频主键
     * @return 结果
     */
    public int deleteShortVideoById(String videoId);

    /**
     * 保存并更新视频的评论统计数量
     * @param comments
     */
    public void calculateAndUpdateRepliedqty(List<ShortVideoComment> comments);

    public boolean sendComment(ShortVideoComment comment);

    public List<ShortVideoComment> getCommentListByVideoId(Long uid, String videoId);

    public List<ShortVideoComment> getCommentListByVideoIdAndParentId(Long uid, String videoId, Long parentId);

    public boolean likeShortVideo(Long uid, String videoId);

    public boolean unlikeShortVideo(Long uid,  String videoId);

    public boolean likeComment(Long uid, String videoId, String commentId);

    public boolean unlikeComment(Long uid,  String videoId, String commentId);

    public int followUser(Long uid, Long followedUid);

    public int unfollowUser(Long uid, Long followedUid);

    public int watchVideo(Long uid, ShortVideoWatchVo watchRecord);

    public int refuseVideoAudit(ShortVideoApplyVo applyVo);

    public int passVideoAudit(String id);


}
