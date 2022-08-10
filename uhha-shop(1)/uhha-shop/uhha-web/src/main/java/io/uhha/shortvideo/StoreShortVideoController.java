package io.uhha.shortvideo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.appletsutil.UnAuthorizedException;
import io.uhha.common.annotation.RateLimiter;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.controller.BaseController;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.common.core.page.TableDataInfo;
import io.uhha.common.enums.LimitType;
import io.uhha.common.utils.bean.BeanUtils;
import io.uhha.integral.domain.CustomerPoint;
import io.uhha.integral.domain.PointSetting;
import io.uhha.integral.service.CustomerPointService;
import io.uhha.integral.service.PointSettingService;
import io.uhha.setting.bean.OssSetCommon;
import io.uhha.setting.bean.UploadData;
import io.uhha.setting.service.OssService;
import io.uhha.shortvideo.domain.ShortVideo;
import io.uhha.shortvideo.domain.ShortVideoComment;
import io.uhha.shortvideo.service.IShortVideoService;
import io.uhha.shortvideo.vo.ImageTextVo;
import io.uhha.shortvideo.vo.ShortVideoVo;
import io.uhha.shortvideo.vo.ShortVideoWatchVo;
import io.uhha.util.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@Api(tags = "短视频接口")
@RequestMapping("/video")
@Slf4j
public class StoreShortVideoController extends BaseController {
    @Autowired
    private IShortVideoService shortVideoService;

    @Autowired
    private OssService ossService;

    /**
     * 注入用户积分服务
     */
    @Autowired
    private CustomerPointService customerPointService;

    @Autowired
    private PointSettingService pointSettingService;

    /**
     * 查询用户推荐的视频列表
     */
    @UnAuth
    @ResponseBody
    @ApiOperation(value = "分页查询短视频列表", notes = "分页查询短视频列表（登陆不登陆均可以）", httpMethod = "GET")
    @GetMapping("/recommendList")
    @RateLimiter(key = "recommendList", time = 1, count = 100, limitType = LimitType.IP)
    @ApiResponses({
            @ApiResponse(code = 200, message = "短视频列表", response = ShortVideo.class)
    })
    public AjaxResult recommendList(HttpServletRequest request,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                    @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        startPage();

        Long uid;
        List<ShortVideoVo> list;

        try {
            uid = AppletsLoginUtils.getInstance().getCustomerId(request);
            if(uid!=0){
                list = shortVideoService.getRecommendedVideoListByUID(uid);
                return AjaxResult.success(list);
            }
        } catch (UnAuthorizedException ex) {
            log.warn("user not login.");
        }
        list = shortVideoService.getRecommendedVideoList();

        return AjaxResult.success(list);
    }

    /**
     * 查询用户推荐的视频列表
     */
    @ResponseBody
    @ApiOperation(value = "分页查询短视频列表", notes = "分页查询短视频列表（需要登陆）", httpMethod = "GET")
    @GetMapping("/recommendListByUid")
    @RateLimiter(key = "recommendListByUid", time = 1, count = 100, limitType = LimitType.IP)
    @ApiResponses({
            @ApiResponse(code = 200, message = "短视频列表", response = ShortVideo.class)
    })
    public AjaxResult recommendListByUid(HttpServletRequest request,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                         @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        startPage();
        List<ShortVideoVo> list = shortVideoService.getRecommendedVideoListByUID(uid);
        return AjaxResult.success(list);
    }

    /**
     * 查询用户的视频列表
     */
    @ResponseBody
    @ApiOperation(value = "分页查询短视频列表", notes = "分页查询短视频列表（需要登陆）", httpMethod = "GET")
    @GetMapping("/list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "短视频列表", response = ShortVideo.class)
    })
    public AjaxResult userVideoList(HttpServletRequest request,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                    @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        startPage();
        List<ShortVideoVo> list = shortVideoService.getRecommendedVideoListByUID(uid);
        return AjaxResult.success(list);
    }


    /**
     * 根据经纬度查询附近的视频
     */
    @UnAuth
    @ResponseBody
    @ApiOperation(value = "分页查询查询附近的短视频列表", notes = "分页查询查询附近的短视频列表（登陆不登陆均可以）", httpMethod = "GET")
    @GetMapping("/nearbyList")
    @ApiResponses({
            @ApiResponse(code = 200, message = "短视频列表", response = ShortVideo.class)
    })
    public AjaxResult nearbyList(@RequestParam("distance") long distance,
                                 @RequestParam("longitude") String longitude,
                                 @RequestParam("latitude") String latitude,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {

        startPage();
        List<ShortVideo> list = shortVideoService.getVideoListByLocation(distance, longitude, latitude);
        return AjaxResult.success(list);
    }

    /**
     * 根据经纬度查询附近的视频
     */
    @ApiOperation(value = "分页查询查询附近的短视频列表", notes = "分页查询查询附近的短视频列表（登陆不登陆均可以）", httpMethod = "GET")
    @GetMapping("/nearbyListByUid")
    @ApiResponses({
            @ApiResponse(code = 200, message = "短视频列表", response = ShortVideo.class)
    })
    public AjaxResult nearbyListByUid(HttpServletRequest request,
                                      @RequestParam("distance") long distance,
                                      @RequestParam("longitude") String longitude,
                                      @RequestParam("latitude") String latitude,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        startPage();
        List<ShortVideo> list = shortVideoService.getVideoListByUidAndLocation(uid, distance, longitude, latitude);
        return AjaxResult.success(list);
    }


    /**
     * 关注的视频列表
     */
    @ResponseBody
    @ApiOperation(value = "关注的视频列表", notes = "关注的视频列表（需登陆）", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "短视频列表", response = ShortVideo.class)
    })
    @GetMapping("/followList")
    public AjaxResult followList(HttpServletRequest request,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "4") Integer pageSize,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        startPage();
        List<ShortVideoVo> list = shortVideoService.getFollowedVideoListByUID(uid);
        return AjaxResult.success(list);
    }

    /**
     * 根据视频id获取视频信息
     */
    @UnAuth
    @ApiOperation(value = "根据视频id获取视频信息", notes = "根据视频id获取视频信息（不需登陆）", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回查看操作是否成功", response = AjaxResult.class)
    })
    @GetMapping("/{videoId}")
    public AjaxResult getVideoById(HttpServletRequest request, @PathVariable("videoId") String videoId) {
        long uid;
        ShortVideoVo shortVideoVo = null;

        try {
            uid = AppletsLoginUtils.getInstance().getCustomerId(request);
            if(uid!=0){
                shortVideoVo = shortVideoService.getShortVideoByIdAndUid(uid, videoId);
                return AjaxResult.success(shortVideoVo);
            }
        } catch (UnAuthorizedException ex) {
            log.warn("user not login.");
        }
        return AjaxResult.success(shortVideoService.getShortVideoById(videoId));
    }

    /**
     * 视频查看
     */
    @ApiOperation(value = "视频查看", notes = "视频查看（需登陆）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回查看操作是否成功", response = AjaxResult.class)
    })
    @PostMapping("/watch")
    public AjaxResult watchVideo(HttpServletRequest request, @RequestBody ShortVideoWatchVo shortVideoWatchVo) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        return AjaxResult.success(shortVideoService.watchVideo(uid, shortVideoWatchVo));
    }

    /**
     * 文件上传
     */
    @ApiOperation(value = "上传文件", notes = "上传视频（需登陆）", httpMethod = "POST")
    @Transactional
    @PostMapping("/fileUpload")
    public AjaxResult fileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile, @RequestParam("type") String type) throws IOException {

        //当前用户id
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        //获取referer头的值
        String referer = request.getHeader("referer");

        //上传文件
        if (Objects.isNull(multipartFile)) {
            return AjaxResult.error();
        }

        OssSetCommon ossSetCommon = ossService.queryOssSet();
        if (!CommonConstant.UPLOAD_VIDEO.equalsIgnoreCase(type) && !CommonConstant.UPLOAD_PIC.equalsIgnoreCase(type)) {
            logger.error("wrong param type. either 0 for picture or 1 for video");
            return AjaxResult.error("wrong param type. either 0 for picture or 1 for video");
        }

        String url = ossService.uploadToOss(Arrays.asList(UploadData.build(multipartFile.getInputStream(), multipartFile.getBytes(), multipartFile.getOriginalFilename(),
                type, multipartFile)), referer, Long.toString(uid)).stream().findFirst().orElse("");
        logger.debug("uploaded: [{}]", url);

        return AjaxResult.success(url);
    }

    /**
     * 视频发布
     */
    @ApiOperation(value = "上传视频", notes = "上传视频（需登陆）", httpMethod = "POST")
    @Transactional
    @PostMapping("/videoPublish")
    public AjaxResult videoPublish(@Validated @RequestBody ShortVideoVo shortVideo, HttpServletRequest request) throws IOException {

        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        shortVideo.setUid(uid);

        ShortVideo sv = new ShortVideo();
        BeanUtils.copyBeanProp(sv, shortVideo);
        sv.setFrontImageUrl(shortVideo.getVideoUrl()+"?x-oss-process=video/snapshot,t_7000,f_jpg,w_872,h_1634,m_fast");
        //保存记录
        int ret;
        ret = shortVideoService.insertShortVideo(sv);

        PointSetting pointSetting = pointSettingService.queryPointSetting();
        //如果积分赠送活动开启，则增加积分
        if (!ObjectUtils.isEmpty(pointSetting) && pointSetting.isOpen()) {
            customerPointService.addCustomerPoint(CustomerPoint.buildForVideo(uid, pointSetting.getVideoPoint()));
        }

        return AjaxResult.success(ret);
    }

    /**
     * 图文发布
     */
    @ApiOperation(value = "图文发布", notes = "图文发布（需登陆）", httpMethod = "POST")
    @Transactional
    @PostMapping("/textImagePublish")
    public AjaxResult textImagePublish(ImageTextVo imageTextVo) {
        ShortVideo sv = new ShortVideo();
        BeanUtils.copyBeanProp(sv, imageTextVo);
        //保存记录
        int ret;
        ret = shortVideoService.insertShortVideo(sv);
        return AjaxResult.success(ret);
    }

    /**
     * 视频删除
     */
    @PostMapping("/delete/{videoId}")
    public AjaxResult videoDelete(HttpServletRequest request, @RequestParam("videoId") String videoId) {
        //TODO 添加删除功能
        return AjaxResult.success();
    }

    /**
     * 视频点赞
     */
    @ApiOperation(value = "视频点赞", notes = "视频点赞（需登陆）", httpMethod = "POST")
    @PostMapping("/like")
    @RateLimiter(key = "like", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult videoLike(HttpServletRequest request, @RequestParam("videoId") String videoId) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);

        PointSetting pointSetting = pointSettingService.queryPointSetting();
        //如果积分赠送活动开启，则增加积分
        if (!ObjectUtils.isEmpty(pointSetting) && pointSetting.isOpen()) {
            customerPointService.addCustomerPoint(CustomerPoint.buildForVideoLike(uid, pointSetting.getLikePoint(), videoId));
        }

        return AjaxResult.success(shortVideoService.likeShortVideo(uid, videoId));
    }

    /**
     * 视频点踩
     */
    @ApiOperation(value = "视频点踩", notes = "视频点踩（需登陆）", httpMethod = "POST")
    @PostMapping("/unlike")
    @RateLimiter(key = "like", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult videoUnLike(HttpServletRequest request, @RequestParam("videoId") String videoId) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        return AjaxResult.success(shortVideoService.unlikeShortVideo(uid, videoId));
    }

    /**
     * 用户关注
     */
    @ApiOperation(value = "用户关注", notes = "用户关注（需登陆）", httpMethod = "POST")
    @PostMapping("/follow")
    @RateLimiter(key = "follow", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult umsFollow(HttpServletRequest request, @RequestParam("followedUid") Long followedUid) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        return AjaxResult.success(shortVideoService.followUser(uid, followedUid));
    }

    /**
     * 用户取消关注
     */
    @ApiOperation(value = "用户取消关注", notes = "用户取消关注（需登陆）", httpMethod = "POST")
    @PostMapping("/unfollow")
    @RateLimiter(key = "unfollow", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult umsUnfollow(HttpServletRequest request, @RequestParam("followedUid") Long followedUid) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        return AjaxResult.success(shortVideoService.unfollowUser(uid, followedUid));
    }

    /**
     * 查询视频的评论列表，默认取第一层的评论，点击更多可以查看本条下的所有评论
     */
    @ApiOperation(value = "查询视频的评论列表", notes = "查询视频的评论列表（需登陆）", httpMethod = "GET")
    @GetMapping("/comment")
    public TableDataInfo commentList(HttpServletRequest request, @RequestParam("videoId") String videoId,
                                     @RequestParam(value = "commentId", required = false) Long commentId) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        Long parentId = 0L;
        if(commentId ==null || commentId == 0L){
            parentId = 0L;
        }else{
            parentId = commentId;
        }
        startPage();
        List<ShortVideoComment> comments = shortVideoService.getCommentListByVideoIdAndParentId(uid, videoId, parentId);
        return getDataTable(comments);
    }

    /**
     * 评论
     */
    @ApiOperation(value = "发送评论", notes = "发送评论（需登陆）", httpMethod = "POST")
    @PostMapping("/comment/send")
    public AjaxResult commentSend(HttpServletRequest request,
                                  @RequestBody @Validated ShortVideoComment comment) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);

        comment.setUid(uid);

        PointSetting pointSetting = pointSettingService.queryPointSetting();
        //如果积分赠送活动开启，则增加积分
        if (!ObjectUtils.isEmpty(pointSetting) && pointSetting.isOpen()) {
            customerPointService.addCustomerPoint(CustomerPoint.buildForComment(uid, pointSetting.getCommentPoint()));
        }

        return AjaxResult.success(shortVideoService.sendComment(comment));
    }

    /**
     * 评论点赞
     */
    @ApiOperation(value = "评论点赞", notes = "评论点赞（需登陆）", httpMethod = "POST")
    @PostMapping("/comment/like")
    @RateLimiter(key = "like", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult commentLike(HttpServletRequest request,
                                  @RequestParam("videoId") String videoId,
                                  @RequestParam("commentId") String commentId) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        PointSetting pointSetting = pointSettingService.queryPointSetting();
        //如果积分赠送活动开启，则增加积分
        if (!ObjectUtils.isEmpty(pointSetting) && pointSetting.isOpen()) {
            customerPointService.addCustomerPoint(CustomerPoint.buildForCommentLike(uid, pointSetting.getLikePoint(), commentId));
        }
        return AjaxResult.success(shortVideoService.likeComment(uid, videoId, commentId));
    }


    /**
     * 评论点踩
     */
    @ApiOperation(value = "评论点踩", notes = "评论点踩（需登陆）", httpMethod = "POST")
    @PostMapping("/comment/unlike")
    @RateLimiter(key = "like", time = 1, count = 100, limitType = LimitType.IP)
    public AjaxResult commentUnLike(HttpServletRequest request,
                                    @RequestParam("videoId") String videoId,
                                    @RequestParam("commentId") String commentId) {
        long uid = AppletsLoginUtils.getInstance().getCustomerId(request);
        return AjaxResult.success(shortVideoService.unlikeComment(uid, videoId, commentId));
    }


}
