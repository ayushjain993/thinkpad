package io.uhha.shortvideo.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.shortvideo.domain.ShortVideo;
import io.uhha.shortvideo.vo.ShortVideoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 短视频Mapper接口
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Mapper
public interface ShortVideoMapper extends BaseMapper<ShortVideo>
{
    /**
     * 查询短视频
     * 
     * @param uid 短视频主键
     * @return 短视频
     */
    public List<ShortVideoVo> selectShortVideoByUid(Long uid);

    /**
     * 查询短视频
     *
     * @param videoId 短视频主键
     * @return 短视频
     */
    public ShortVideo selectShortVideoByVideoId(String videoId);

    /**
     * 查询单个短视频（未登录）
     *
     * @param videoId 短视频主键
     * @return 短视频
     */
    public ShortVideoVo getShortVideoById(@Param("videoId") String videoId);

    /**
     * 查询短视频列表
     * 
     * @param shortVideo 短视频
     * @return 短视频集合
     */
    public List<ShortVideo> selectShortVideoList(ShortVideo shortVideo);

    /**
     * 查询短视频列表
     *
     * @return 短视频集合
     */
    public List<ShortVideoVo> selectRecommendedVideoList();


    /**
     * 关注的用户列表的视频列表，按照点赞数量倒序排列
     * @param uid uid
     * @return 关注的用户列表的视频列表
     */
    public List<ShortVideoVo> getFollowedVideoListByUID(@Param("uid") long uid);



    /**
     * 用户点赞的大视频列表，按照点赞数量倒序排列
     * @param uid uid
     * @return 关注的用户列表的视频列表
     */
    public List<ShortVideoVo> getLikedVideoListByUID(@Param("uid") long uid);

    /**
     * 根据经纬度查询附近的视频
     * @param uid 用户id
     * @param latitudeSubtract 维度差
     * @param latitudeAdd 维度加
     * @param longitudeSubtract 经度差
     * @param longitudeAdd 经度加
     * @param latitude 维度
     * @param longitude 经度
     * @return 视频列表
     */
    public List<ShortVideo> selectVideoListByUidAndLocation(@Param("uid")long uid, @Param("latitudeSubtract") Double latitudeSubtract , @Param("latitudeAdd") Double latitudeAdd ,
                                                            @Param("longitudeSubtract") Double longitudeSubtract, @Param("longitudeAdd") Double longitudeAdd,
                                                            @Param("latitude") String latitude,@Param("longitude") String longitude);

    /**
     * 根据经纬度查询附近的视频
     * @param latitudeSubtract 维度差
     * @param latitudeAdd 维度加
     * @param longitudeSubtract 经度差
     * @param longitudeAdd 经度加
     * @param latitude 维度
     * @param longitude 经度
     * @return 视频列表
     */
    public List<ShortVideo> selectVideoListByLocation(@Param("latitudeSubtract") Double latitudeSubtract , @Param("latitudeAdd") Double latitudeAdd ,
                                                            @Param("longitudeSubtract") Double longitudeSubtract, @Param("longitudeAdd") Double longitudeAdd,
                                                            @Param("latitude") String latitude,@Param("longitude") String longitude);

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
     * 删除短视频
     * 
     * @param videoId 短视频主键
     * @return 结果
     */
    public int deleteShortVideoById(String videoId);

    /**
     * 批量删除短视频
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShortVideoByIds(String[] ids);

    public int rejectVideoApply(String videoId);

    public int passVideoApply(String videoId);
}
