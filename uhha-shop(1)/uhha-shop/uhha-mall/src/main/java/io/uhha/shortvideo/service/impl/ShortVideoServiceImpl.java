package io.uhha.shortvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.uhha.common.core.redis.RedisCache;
import io.uhha.common.core.redis.RedisConstant;
import io.uhha.common.enums.AuditStatusEnum;
import io.uhha.common.enums.StatusEnum;
import io.uhha.common.enums.UmsLikeTypeEnum;
import io.uhha.common.notify.MallNotifyHelper;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.SnowflakeIdWorker;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.shortvideo.domain.*;
import io.uhha.shortvideo.mapper.ShortVideoCommentMapper;
import io.uhha.shortvideo.mapper.ShortVideoMapper;
import io.uhha.shortvideo.service.*;
import io.uhha.shortvideo.vo.ShortVideoApplyVo;
import io.uhha.shortvideo.vo.ShortVideoVo;
import io.uhha.shortvideo.vo.ShortVideoWatchVo;
import io.uhha.util.LocationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 短视频Service业务层处理
 *
 * @author ruoyi
 * @date 2021-08-06
 */
@Service
@Slf4j
public class ShortVideoServiceImpl extends ServiceImpl<ShortVideoMapper, ShortVideo> implements IShortVideoService {
    @Autowired
    private ShortVideoMapper shortVideoMapper;

    @Autowired
    private ShortVideoCommentMapper shortVideoCommentMapper;

    @Autowired
    private IUmsMemberAttentionService umsMemberAttentionService;

    @Autowired
    private IUmsMemberLikesService umsMemberLikesService;

    @Autowired
    private IShortVideoWatchRecordService watchRecordService;

    @Resource
    private IUmsMemberService memberService;

    @Autowired
    private CommentQueueService commentQueueService;

    @Autowired
    private LikesQueueService likesQueueService;

    @Autowired
    private IShortVideoCommentService videoCommentService;

    @Autowired
    private IUmsMemberLikesService likesService;


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SnowflakeIdWorker idWorker;


    @Autowired
    private MallNotifyHelper mallNotifyHelper;

    /**
     * 查询短视频
     *
     * @param videoId 短视频主键
     * @return 短视频
     */
    @Override
    public ShortVideo selectShortVideoById(String videoId) {
        return shortVideoMapper.selectShortVideoByVideoId(videoId);
    }

    /**
     * 查询短视频列表
     *
     * @param shortVideo 短视频参数
     * @return 短视频
     */
    @Override
    public List<ShortVideo> selectShortVideoList(ShortVideo shortVideo) {
        return shortVideoMapper.selectShortVideoList(shortVideo);
    }

    @Override
    public ShortVideoVo getShortVideoByIdAndUid(Long uid, String videoId) {
        ShortVideoVo shortVideoVo = shortVideoMapper.getShortVideoById(videoId);
        setMemberFollow(shortVideoVo,uid);
        setMemberVideoLike(shortVideoVo,uid);
        setShortVideoComment(shortVideoVo,uid);
        return shortVideoVo;
    }

    @Override
    public ShortVideoVo getShortVideoById(String videoId) {
        ShortVideoVo shortVideoVo = shortVideoMapper.getShortVideoById(videoId);
        setShortVideoComment(shortVideoVo);
        return shortVideoVo;
    }

    @Override
    public List<ShortVideoVo> getShortVideoListByUID(long uid) {
        return shortVideoMapper.selectShortVideoByUid(uid);
    }

    /**
     * 填充单视频的评论
     * @param shortVideoVo
     * @return
     */
    private ShortVideoVo setShortVideoComment(ShortVideoVo shortVideoVo, long uid){
        //填充评论信息
        final List<ShortVideoComment> commentList = videoCommentService.selectShortVideoCommentByVideoId(shortVideoVo.getVideoId());
        if(CollectionUtils.isNotEmpty(commentList)){
            shortVideoVo.setCommentList(commentList);
            setMemberCommentLikes(commentList,uid);
        }
        return shortVideoVo;
    }
    /**
     * 填充单视频的评论
     * @param shortVideoVo
     * @return
     */
    private ShortVideoVo setShortVideoComment(ShortVideoVo shortVideoVo){
        //填充评论信息
        final List<ShortVideoComment> commentList = videoCommentService.selectShortVideoCommentByVideoId(shortVideoVo.getVideoId());
        if(CollectionUtils.isNotEmpty(commentList)){
            shortVideoVo.setCommentList(commentList);
        }
        return shortVideoVo;
    }

    private ShortVideoVo setMemberVideoLike(ShortVideoVo shortVideoVo, long uid){
        final List<UmsMemberLikes> memberLikes = likesService.selectLikesByUid(uid);

        boolean like = memberLikes.stream().anyMatch(new Predicate<UmsMemberLikes>() {
            @Override
            public boolean test(UmsMemberLikes umsMemberLikes) {
                return umsMemberLikes.getResourceId().equalsIgnoreCase(shortVideoVo.getVideoId());
            }
        });
        shortVideoVo.setLike(like? 1:0);
        return shortVideoVo;
    }

    private ShortVideoVo setMemberFollow(ShortVideoVo shortVideoVo, long uid){
        final List<UmsMemberAttention> attentions = umsMemberAttentionService.getUserAttentions(uid);

            boolean followed = attentions.stream().anyMatch(new Predicate<UmsMemberAttention>() {
                @Override
                public boolean test(UmsMemberAttention attention) {
                    return attention.getFollowedUid().equals(shortVideoVo.getUid());
                }
            });
        shortVideoVo.setFollowed(followed? 1:0);
        return shortVideoVo;
    }


    /**
     * 填充评论信息
     * @param shortVideoVos
     * @return
     */
    private List<ShortVideoVo> setShortVideoComments(List<ShortVideoVo> shortVideoVos){
        shortVideoVos.forEach(x->{
            //填充评论信息
            final List<ShortVideoComment> commentList = videoCommentService.selectShortVideoCommentByVideoId(x.getVideoId());
            if(CollectionUtils.isNotEmpty(commentList)){
                x.setCommentList(commentList);
            }
        });
        return shortVideoVos;
    }

    /**
     * 填充用户对视频是否点赞信息
     * @param shortVideoVos
     * @param uid
     * @return
     */
    private List<ShortVideoVo> setMemberVideoLikes(List<ShortVideoVo> shortVideoVos, long uid){
        final List<UmsMemberLikes> memberLikes = likesService.selectLikesByUid(uid);

        shortVideoVos.forEach(x->{
            boolean like = memberLikes.stream().anyMatch(new Predicate<UmsMemberLikes>() {
                @Override
                public boolean test(UmsMemberLikes umsMemberLikes) {
                    return umsMemberLikes.getResourceId().equalsIgnoreCase(x.getVideoId());
                }
            });
            x.setLike(like? 1:0);
        });
        return shortVideoVos;
    }

    /**
     * 填充用户对评论是否点赞信息
     * @param comments
     * @param uid
     * @return
     */
    private List<ShortVideoComment> setMemberCommentLikes(List<ShortVideoComment> comments, long uid){
        //填充点赞信息
        final List<UmsMemberLikes> memberLikes = likesService.selectLikesByUid(uid);
        comments.forEach(x->{
            boolean like = memberLikes.stream().anyMatch(new Predicate<UmsMemberLikes>() {
                @Override
                public boolean test(UmsMemberLikes umsMemberLikes) {
                    return umsMemberLikes.getResourceId().equalsIgnoreCase(x.getVideoId());
                }
            });
            x.setLike(like? 1:0);
        });
        return comments;
    }

    /**
     * 填充用户是否关注
     * @param shortVideoVos
     * @return
     */
    private List<ShortVideoVo> setMemberFollows(List<ShortVideoVo> shortVideoVos, long uid){
        final List<UmsMemberAttention> attentions = umsMemberAttentionService.getUserAttentions(uid);

        shortVideoVos.forEach(x->{
            boolean followed = attentions.stream().anyMatch(new Predicate<UmsMemberAttention>() {
                @Override
                public boolean test(UmsMemberAttention attention) {
                    return attention.getFollowedUid().equals(x.getUid());
                }
            });
            x.setFollowed(followed? 1:0);
        });
        return shortVideoVos;
    }

    @Override
    public List<ShortVideoVo> getFollowedVideoListByUID(long uid) {
        List<ShortVideoVo> shortVideoVos = shortVideoMapper.getFollowedVideoListByUID(uid);

        //填充评论信息
        shortVideoVos = setShortVideoComments(shortVideoVos);

        //填充用户是否点赞信息
        shortVideoVos = setMemberVideoLikes(shortVideoVos, uid);

        //填充用户是否关注
        shortVideoVos = setMemberFollows(shortVideoVos, uid);

        return shortVideoVos;
    }

    @Override
    public List<ShortVideoVo> getLikedVideoListByUID(long uid) {
        List<ShortVideoVo> shortVideoVos = shortVideoMapper.getLikedVideoListByUID(uid);

        //填充评论信息
        shortVideoVos = setShortVideoComments(shortVideoVos);

        //填充用户是否点赞信息
        shortVideoVos = setMemberVideoLikes(shortVideoVos, uid);

        //填充用户是否关注
        shortVideoVos = setMemberFollows(shortVideoVos, uid);

        return shortVideoVos;

    }

    @Override
    public List<ShortVideoVo> getRecommendedVideoListByUID(long uid) {
        List<ShortVideoVo> shortVideoVos = shortVideoMapper.selectRecommendedVideoList();

        //填充评论信息
        shortVideoVos = setShortVideoComments(shortVideoVos);

        //填充用户是否点赞信息
        shortVideoVos = setMemberVideoLikes(shortVideoVos, uid);

        //填充用户是否关注
        shortVideoVos = setMemberFollows(shortVideoVos, uid);

        return shortVideoVos;
    }

    @Override
    public List<ShortVideoVo> getRecommendedVideoList() {
        List<ShortVideoVo> shortVideoVos = shortVideoMapper.selectRecommendedVideoList();

        shortVideoVos = setShortVideoComments(shortVideoVos);

        return shortVideoVos;
    }

    @Override
    public List<ShortVideo> getVideoListByUidAndLocation(long uid, long range, String longitude, String latitude) {
        DecimalFormat df = new DecimalFormat("0.00");
        String value = String.valueOf(range);
        String degees = "0.5";
        if (StringUtils.isNotEmpty(value) && isDigst(value)) {
            degees = String.valueOf(value);
        }
        BigDecimal deggesVar = new BigDecimal(degees);
        BigDecimal longitudeVar = new BigDecimal(longitude);
        BigDecimal latitudeVar = new BigDecimal(latitude);
        BigDecimal longitudeAddVar = longitudeVar.add(deggesVar);
        BigDecimal longitudeSubtractVar = longitudeVar.subtract(deggesVar);
        BigDecimal latitudeAddVar = latitudeVar.add(deggesVar);
        BigDecimal latitudeSubtractVar = latitudeVar.subtract(deggesVar);
        final Double longitudeAdd = longitudeAddVar.doubleValue();
        final Double longitudeSubtract = longitudeSubtractVar.doubleValue();
        final Double latitudeAdd = latitudeAddVar.doubleValue();
        final Double latitudeSubtract = latitudeSubtractVar.doubleValue();

        List<ShortVideo> list = shortVideoMapper.selectVideoListByUidAndLocation(uid, latitudeSubtract, latitudeAdd, longitudeSubtract, longitudeAdd, latitude, longitude);

        list.parallelStream().forEach((shortVideo) -> {
            shortVideo.setDistance(df.format(LocationUtils.getDistance(Float.toString(shortVideo.getLongitude()), Float.toString(shortVideo.getLatitude()), latitude, longitude) / 1000));
        });
        return list;
    }

    /**
     * 根据经纬度和Uid查询附近的视频
     *
     * @param latitude 维度
     * @param longitude 经度
     * @return 短视频集合
     */
    @Override
    public List<ShortVideo> getVideoListByLocation(long range, String longitude, String latitude){
        DecimalFormat df = new DecimalFormat("0.00");
        String value = String.valueOf(range);
        String degees = "0.5";
        if (StringUtils.isNotEmpty(value) && isDigst(value)) {
            degees = String.valueOf(value);
        }
        BigDecimal deggesVar = new BigDecimal(degees);
        BigDecimal longitudeVar = new BigDecimal(longitude);
        BigDecimal latitudeVar = new BigDecimal(latitude);
        BigDecimal longitudeAddVar = longitudeVar.add(deggesVar);
        BigDecimal longitudeSubtractVar = longitudeVar.subtract(deggesVar);
        BigDecimal latitudeAddVar = latitudeVar.add(deggesVar);
        BigDecimal latitudeSubtractVar = latitudeVar.subtract(deggesVar);
        final Double longitudeAdd = longitudeAddVar.doubleValue();
        final Double longitudeSubtract = longitudeSubtractVar.doubleValue();
        final Double latitudeAdd = latitudeAddVar.doubleValue();
        final Double latitudeSubtract = latitudeSubtractVar.doubleValue();

        List<ShortVideo> list = shortVideoMapper.selectVideoListByLocation(latitudeSubtract, latitudeAdd, longitudeSubtract, longitudeAdd, latitude, longitude);

        list.parallelStream().forEach((shortVideo) -> {
            shortVideo.setDistance(df.format(LocationUtils.getDistance(Float.toString(shortVideo.getLongitude()), Float.toString(shortVideo.getLatitude()), latitude, longitude) / 1000));
        });
        return list;
    }

    /**
     * 新增短视频
     *
     * @param shortVideo 短视频
     * @return 结果
     */
    @Override
    public int insertShortVideo(ShortVideo shortVideo) {
        int ret = 0;

        if(shortVideo.getSpuId()!=null){
            shortVideo.setHavingProduct(StatusEnum.YesNoType.YES.code().toString());
        }else{
            shortVideo.setHavingProduct(StatusEnum.YesNoType.NO.code().toString());
        }
        shortVideo.setStatus(AuditStatusEnum.UNDER_AUDIT.getCode());

        shortVideo.setCreateTime(DateUtils.getNowDate());
        shortVideo.setVideoId(idWorker.nextUuid());
        ret = shortVideoMapper.insert(shortVideo);

        //缓存话题
        String topicList = shortVideo.getTopicList();
        if (topicList != null) {
            String[] topics = topicList.split(",");
            for (String topic : topics
            ) {
                List<ShortVideo> topicVideos = redisCache.getCacheList(RedisConstant.TOPIC_TAG + topic);
                if (null == topicVideos) {
                    topicVideos = new ArrayList<>();
                }
                topicVideos.add(shortVideo);
            }
        }
        return ret;
    }

    /**
     * 修改短视频
     *
     * @param shortVideo 短视频
     * @return 结果
     */
    @Override
    public int updateShortVideo(ShortVideo shortVideo) {
        int ret = 0;
        ShortVideo oldVideo = shortVideoMapper.selectShortVideoByVideoId(shortVideo.getVideoId());

        if (oldVideo == null) {
            ret = shortVideoMapper.insertShortVideo(shortVideo);

            //缓存新话题
            String topicList = shortVideo.getTopicList();
            if (topicList != null) {
                String[] topics = topicList.split(",");
                for (String topic : topics
                ) {
                    List<ShortVideo> topicVideos = redisCache.getCacheList(RedisConstant.TOPIC_TAG + topic);
                    if (null == topicVideos) {
                        topicVideos = new ArrayList<>();
                    }
                    topicVideos.add(shortVideo);
                }
            }
            return ret;
        }

        ret = shortVideoMapper.updateShortVideo(shortVideo);

        String oldTopic = oldVideo.getTopicList();
        String newTopic = shortVideo.getTopicList();
        //话题无变化则不做什么
        if (oldTopic == null || oldTopic.equals(newTopic)) {
            return ret;
        }
        //话题有变化
        if (newTopic != null && !oldTopic.equalsIgnoreCase(newTopic)) {
            List<String> oldTopics = new ArrayList<String>(Arrays.asList(oldVideo.getTopicList().split(",")));
            List<String> newTopics = new ArrayList<String>(Arrays.asList(shortVideo.getTopicList().split(",")));

            //oldTopics - newTopics
            List<String> delta1 = oldTopics.stream().filter(item -> !newTopics.contains(item)).collect(Collectors.toList());
            //newTopics - oldTopics
            List<String> delta2 = newTopics.stream().filter(item -> !oldTopics.contains(item)).collect(Collectors.toList());

            //从redis中删除该话题的视频
            delta1.stream().forEach(x -> {
                List<ShortVideo> topicVideos = redisCache.getCacheList(RedisConstant.TOPIC_TAG + x);
                if (null == topicVideos) {
                    topicVideos = new ArrayList<>();
                }
                topicVideos = topicVideos.stream().filter(y -> y.getVideoId().equalsIgnoreCase(shortVideo.getVideoId())).collect(Collectors.toList());
                redisCache.deleteObject(RedisConstant.TOPIC_TAG + x);
                redisCache.setCacheList(RedisConstant.TOPIC_TAG + x, topicVideos);
            });

            //在redis中添加该话题的视频
            delta2.stream().forEach(x -> {
                List<ShortVideo> topicVideos = redisCache.getCacheList(RedisConstant.TOPIC_TAG + x);
                if (null == topicVideos) {
                    topicVideos = new ArrayList<>();
                }
                topicVideos.add(shortVideo);
                redisCache.deleteObject(RedisConstant.TOPIC_TAG + x);
                redisCache.setCacheList(RedisConstant.TOPIC_TAG + x, topicVideos);
            });
        }
        return ret;
    }

    /**
     * 批量删除短视频
     *
     * @param ids 需要删除的短视频主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteShortVideoByIds(String[] ids) {
        shortVideoMapper.deleteShortVideoByIds(ids);

        List<String> videos = Arrays.asList(ids);
        videos.forEach(x->{
            videoCommentService.deleteShortVideoCommentByVideoId(x);
        });
        return 1;
    }

    /**
     * 删除短视频信息
     *
     * @param videoId 短视频主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteShortVideoById(String videoId) {
        shortVideoMapper.deleteShortVideoById(videoId);
        videoCommentService.deleteShortVideoCommentByVideoId(videoId);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateAndUpdateRepliedqty(List<ShortVideoComment> comments) {
        comments.forEach(x->{
            ShortVideo shortVideo = selectShortVideoById(x.getVideoId());
            if(shortVideo ==null){
                log.error("invalid input paramenters. videoId:{} not found.", x.getVideoId());
                return;
            }
            QueryWrapper<ShortVideoComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("video_id", x.getVideoId());
            Long commented = shortVideoCommentMapper.selectCount(queryWrapper);
            shortVideo.setRepliedqty(commented);
            shortVideoMapper.updateShortVideo(shortVideo);
        });
    }

    @Override
    public boolean sendComment(ShortVideoComment comment) {
        comment.setCreateTime(DateUtils.getNowDate());
        if(comment.getParentId() == null){
            comment.setParentId(0L);
        }
        return commentQueueService.pushQueue(comment);
    }

    @Override
    public List<ShortVideoComment> getCommentListByVideoId(Long uid, String videoId) {
        List<ShortVideoComment> comments = videoCommentService.selectShortVideoCommentByVideoId(videoId);

        //填充用户对评论是否点赞信息
        comments = setMemberCommentLikes(comments, uid);

        return comments;
    }


    @Override
    public List<ShortVideoComment> getCommentListByVideoIdAndParentId(Long uid, String videoId, Long parentId) {
        List<ShortVideoComment> comments = videoCommentService.selectShortVideoCommentByVideoIdAndParentId(videoId, parentId);

        //填充用户对评论是否点赞信息
        comments = setMemberCommentLikes(comments, uid);

        return comments;
    }

    @Override
    @Transactional
    public boolean likeShortVideo(Long uid, String videoId) {

        //看用户是否已经点过赞了
        if(likesService.checkUserLiked(uid,videoId)){
            log.warn("User: {} Already liked resource: {}", uid, videoId);
            return false;
        }

        ShortVideo shortVideo = selectShortVideoById(videoId);

        UmsMemberLikes umsMemberLikes = UmsMemberLikes.builder()
                .uid(uid)
                .resourceId(videoId)
                .resourceOwnerId(shortVideo.getUid())
                .createTime(DateUtils.getNowDate())
                .type(UmsLikeTypeEnum.VIDEO.getCode())
                .build();
        return likesQueueService.pushLikeQueue(umsMemberLikes);
    }

    @Override
    @Transactional
    public boolean unlikeShortVideo(Long uid, String videoId) {
        UmsMemberLikes umsMemberLikes = UmsMemberLikes.builder()
                .uid(uid)
                .resourceId(videoId)
                .type(UmsLikeTypeEnum.VIDEO.getCode())
                .build();
        List<UmsMemberLikes> likesList = umsMemberLikesService.selectUmsMemberLikesList(umsMemberLikes);

        if (likesList == null || likesList.size() == 0) {
            return false;
        }
        if (likesList.size() == 1) {
            return likesQueueService.pushUnlikeQueue(likesList.get(0));
        } else {
            return false;
        }

    }

    @Override
    @Transactional
    public boolean likeComment(Long uid, String videoId, String commentId) {

        //看用户是否已经点过赞了
        if(likesService.checkUserLiked(uid,commentId)){
            log.warn("User: {} Already liked resource: {}", uid, commentId);
            return false;
        }

        //评论点赞更新
        ShortVideoComment comment = videoCommentService.selectShortVideoCommentById(commentId);
        if(comment==null){
            log.error("likeComment: commentId:{} for videoId:{} does not exists.", commentId, videoId);
            return false;
        }
        //获取shortVideo
        ShortVideo shortVideo = selectShortVideoById(videoId);

        UmsMemberLikes umsMemberLikes = UmsMemberLikes.builder()
                .uid(uid)
                .resourceId(commentId)
                .resourceOwnerId(shortVideo.getUid())
                .createTime(DateUtils.getNowDate())
                .type(UmsLikeTypeEnum.COMMENT.getCode())
                .build();
        return likesQueueService.pushLikeQueue(umsMemberLikes);
    }

    @Override
    @Transactional
    public boolean unlikeComment(Long uid, String videoId, String commentId) {
        ShortVideoComment comment = videoCommentService.selectShortVideoCommentById(commentId);
        if(comment==null){
            log.error("unlikeComment: Comment commentId:{} for videoId:{} does not exists.", commentId, videoId);
            return false;
        }

        QueryWrapper<UmsMemberLikes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid)
                .eq("resource_id", commentId)
                .eq("type",UmsLikeTypeEnum.COMMENT.getCode());

        UmsMemberLikes inDb = umsMemberLikesService.getOne(queryWrapper);
        if (inDb == null) {
            return false;
        }else{
            UmsMemberLikes umsMemberLikes = UmsMemberLikes.builder()
                    .id(inDb.getId())
                    .uid(uid)
                    .resourceId(commentId)
                    .type(UmsLikeTypeEnum.COMMENT.getCode())
                    .build();
            return likesQueueService.pushUnlikeQueue(umsMemberLikes);
        }
    }

    @Override
    public int followUser(Long uid, Long followedUid) {
        return umsMemberAttentionService.followUser(uid, followedUid);
    }

    @Override
    public int unfollowUser(Long uid, Long followedUid) {
        return umsMemberAttentionService.unfollowUser(uid, followedUid);
    }

    @Override
    @Transactional
    public int watchVideo(Long uid, ShortVideoWatchVo videoViewVo) {
        UmsMember umsMember = memberService.selectUmsMemberById(uid);

        ShortVideo shortVideo = shortVideoMapper.selectShortVideoByVideoId(videoViewVo.getVideoId());
        if (shortVideo == null) {
            log.warn("观看用户UID：{}, 视频ID:{}未找到.", videoViewVo.getUid(), videoViewVo.getVideoId());
            return 0;
        }
        //播放数+1
        shortVideo.setPv(shortVideo.getPv() + 1);
        shortVideoMapper.updateShortVideo(shortVideo);

        //记录访问历史
        ShortVideoWatchRecord watchRecord = ShortVideoWatchRecord.builder()
                //视频属性
                .videoId(shortVideo.getVideoId())
                .videoTags(shortVideo.getTopicList())
                .producerUid(shortVideo.getUid())
                .hours(shortVideo.getHours())
                .minutes(shortVideo.getMinutes())
                .seconds(shortVideo.getSeconds())
                //用户属性
                .personGender(umsMember.getGender())
                .personInterest(umsMember.getInterest())
                //设备属性
                .personPhoneModel(videoViewVo.getPersonPhoneModel())
                .envLocation(videoViewVo.getEnvLocation())
                .envNetwork(videoViewVo.getEnvNetwork())
                .build();

        return watchRecordService.insertShortVideoWatchRecord(watchRecord);
    }

    @Override
    public int refuseVideoAudit(ShortVideoApplyVo applyVo) {
        ShortVideo shortVideo = shortVideoMapper.selectById(applyVo.getVideoId());
        if(null == shortVideo){
            log.warn("Not found");
            return 0;
        }
        //发送审批通过还是失败通知
        mallNotifyHelper.adminApproveRejectVideo(shortVideo,applyVo.getReason(),AuditStatusEnum.REJECTED.getCode());

        return shortVideoMapper.rejectVideoApply(applyVo.getVideoId());
    }

    @Override
    public int passVideoAudit(String id) {
        ShortVideo shortVideo = shortVideoMapper.selectById(id);
        if(null == shortVideo){
            log.warn("Not found");
            return 0;
        }

        //发送审批通过还是失败通知
        mallNotifyHelper.adminApproveRejectVideo(shortVideo,"",AuditStatusEnum.PASS.getCode());

        return shortVideoMapper.passVideoApply(id);
    }

    private static boolean isDigst(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
        return pattern.matcher(str).matches();
    }
}
