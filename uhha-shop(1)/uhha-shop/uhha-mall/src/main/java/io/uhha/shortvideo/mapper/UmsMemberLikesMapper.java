package io.uhha.shortvideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.uhha.shortvideo.domain.UmsMemberLikes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UmsMemberLikesMapper extends BaseMapper<UmsMemberLikes> {


    /**
     * 查询短视频观看记录
     *
     * @param id 短视频观看记录主键
     * @return 短视频观看记录
     */
    public UmsMemberLikes selectUmsMemberLikesById(Long id);

    /**
     * 查询用户被点赞总数
     *
     * @param resourceOwnerId 用户id
     * @return 短视频观看记录
     */
    public Long selectLikesCountByUid(@Param("resourceOwnerId") Long resourceOwnerId);


    /**
     * 查询短视频观看记录列表
     *
     * @param UmsMemberLikes 短视频观看记录
     * @return 短视频观看记录集合
     */
    public List<UmsMemberLikes> selectUmsMemberLikesList(UmsMemberLikes UmsMemberLikes);

    /**
     * 新增短视频观看记录
     *
     * @param UmsMemberLikes 短视频观看记录
     * @return 结果
     */
    public int insertUmsMemberLikes(UmsMemberLikes UmsMemberLikes);

    /**
     * 修改短视频观看记录
     *
     * @param UmsMemberLikes 短视频观看记录
     * @return 结果
     */
    public int updateUmsMemberLikes(UmsMemberLikes UmsMemberLikes);

    /**
     * 删除短视频观看记录
     *
     * @param id 短视频观看记录主键
     * @return 结果
     */
    public int deleteUmsMemberLikesById(Long id);

    /**
     * 批量删除短视频观看记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUmsMemberLikesByIds(Long[] ids);

}
