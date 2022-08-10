package io.uhha.shortvideo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.uhha.shortvideo.domain.UmsMemberLikes;
import org.web3j.abi.datatypes.Int;

import java.util.List;

public interface IUmsMemberLikesService extends IService<UmsMemberLikes> {

    /**
     * 查询资源的点赞数
     *
     * @param resourceId 资源记录主键
     * @return 点赞数
     */
    public Long selectLikesCountByResourceId(Long resourceId);

    /**
     * 查询用户被点赞总数
     *
     * @param uid 用户id
     * @return 点赞数
     */
    public Long selectLikesCountByUid(Long uid);

    /**
     * 查询用户点赞的视频
     *
     * @param uid 用户id
     * @return 点赞列表
     */
    public List<UmsMemberLikes> selectLikesByUid(Long uid);

    /**
     * 查询用户是否点赞过某内容
     *
     * @param uid 用户id
     * @return 点赞数
     */
    public Boolean isResourceLikedByUser(Long uid, String resoureId);

    /**
     * 查询短视频观看记录
     *
     * @param id 短视频观看记录主键
     * @return 短视频观看记录
     */
    public UmsMemberLikes selectUmsMemberLikesById(Long id);

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

    /**
     * 计算获得赞的视频或者评论的数目，并更新
     * @param umsMemberLikes
     */
    public void calculateAndUpdateLikesQty(List<UmsMemberLikes> umsMemberLikes);

    /**
     * 查询用户是否已经点过赞
     * @param uid
     * @param resourceId
     */
    public boolean checkUserLiked(Long uid, String resourceId);
}
