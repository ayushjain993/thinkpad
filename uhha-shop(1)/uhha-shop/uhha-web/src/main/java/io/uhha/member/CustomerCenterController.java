package io.uhha.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.uhha.appletsutil.AppletsLoginUtils;
import io.uhha.common.annotation.UnAuth;
import io.uhha.common.core.domain.AjaxResult;
import io.uhha.member.domain.UmsMember;
import io.uhha.member.service.IUmsMemberService;
import io.uhha.shortvideo.service.IShortVideoService;
import io.uhha.shortvideo.service.IUmsMemberAttentionService;
import io.uhha.shortvideo.service.IUmsMemberLikesService;
import io.uhha.shortvideo.vo.ShortVideoVo;
import io.uhha.vo.UserHomepageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 用户中心控制器
 *
 * @author SK
 * @since 2018/6/13
 */
@Controller
@Api(tags = "用户中心接口")
@Slf4j
public class CustomerCenterController {

    /**
     * 注入会员接口
     */
    @Autowired
    private IUmsMemberService customerService;

    @Autowired
    private IShortVideoService shortVideoService;

    @Autowired
    private IUmsMemberAttentionService attentionService;

    @Autowired
    private IUmsMemberLikesService likesService;


    /**
     * 获得会员基本信息
     *
     * @return 返回会员基本信息
     */
    @RequestMapping("customerdetail")
    @ResponseBody
    @ApiOperation(value = "获得会员基本信息", notes = "获得会员基本信息（需要认证）", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回会员基本信息", response = UmsMember.class)
    })
    public AjaxResult customerDetail(HttpServletRequest request) {
        return AjaxResult.success(customerService.queryCustomerWithNoPasswordById(AppletsLoginUtils.getInstance().getCustomerId(request)));
    }

    /**
     * 获得用户主页基本信息（不需要登录）
     *
     * @return 返回主页基本信息
     */
    @UnAuth
    @RequestMapping("homepage1")
    @ResponseBody
    @ApiOperation(value = "获得用户主页基本信息", notes = "用户主页基本信息（不需要登录）", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户主页基本信息（不需要登录）", response = UserHomepageVo.class)
    })
    public AjaxResult homepageForNoLoginUser(@RequestParam("hostId") Long hostId) {
        UmsMember host = customerService.queryCustomerWithNoPasswordById(hostId);

        if(ObjectUtils.isEmpty(host)){
            log.error("homepageForNoLoginUser and hostId: {} doesn't exist", hostId);
            return AjaxResult.error("not found");
        }
        //填充基本信息
        UserHomepageVo homepageVo = UserHomepageVo.builder()
                .birthday(host.getBirthday())
                .nickname(host.getNickname())
                .uid(host.getId())
                .gender(host.getGender())
                .avatarUrl(host.getImage())
                .country(host.getCountry())
                .introduction(host.getIntroduction())
//                .withNewGoods()
                .isBlock(false)
                .build();

        //根据uid获取用户的视频列表
        List<ShortVideoVo> shortVideoVoList = shortVideoService.getShortVideoListByUID(hostId);
        homepageVo.setShortVideoVoList(shortVideoVoList);

        //获取用户点赞的视频列表
        List<ShortVideoVo> likedShortVideoVoList = shortVideoService.getLikedVideoListByUID(hostId);
        homepageVo.setLikeShortVideoList(likedShortVideoVoList);

        //统计数字
        homepageVo.setFollowerCount(attentionService.getFollowerCount(hostId));
        homepageVo.setFollowingCount(attentionService.getFollowingCount(hostId));
        homepageVo.setLikeCount(likesService.selectLikesCountByUid(hostId));

        return AjaxResult.success(homepageVo);
    }

    /**
     * 获得用户主页基本信息（需要登录）
     *
     * @return 返回主页基本信息（需要登录）
     */
    @RequestMapping("homepage2")
    @ResponseBody
    @ApiOperation(value = "获得用户主页基本信息（需要登录）", notes = "用户主页基本信息（需要登录）", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回用户主页基本信息（需要登录）", response = UserHomepageVo.class)
    })
    public AjaxResult homepage(HttpServletRequest request, @RequestParam("hostId") Long hostId) {
        UmsMember host = customerService.queryCustomerWithNoPasswordById(hostId);

        if(ObjectUtils.isEmpty(host)){
            log.error("homepageForNoLoginUser and hostId: {} doesn't exist", hostId);
            return AjaxResult.error("not found");
        }

        Long guestId = AppletsLoginUtils.getInstance().getCustomerId(request);

        //填充基本信息
        UserHomepageVo homepageVo = UserHomepageVo.builder()
                .birthday(host.getBirthday())
                .nickname(host.getNickname())
                .uid(host.getId())
                .gender(host.getGender())
                .avatarUrl(host.getImage())
                .country(host.getCountry())
                .introduction(host.getIntroduction())
//                .withNewGoods()
                .isBlock(false)
                .build();

        //根据uid获取用户的视频列表
        List<ShortVideoVo> shortVideoVoList = shortVideoService.getShortVideoListByUID(hostId);
        homepageVo.setShortVideoVoList(shortVideoVoList);

        //获取用户点赞的视频列表
        List<ShortVideoVo> likedShortVideoVoList = shortVideoService.getLikedVideoListByUID(hostId);
        homepageVo.setLikeShortVideoList(likedShortVideoVoList);

        attentionService.isUserfollowed(hostId, guestId);

        //统计数字
        homepageVo.setFollowerCount(attentionService.getFollowerCount(hostId));
        homepageVo.setFollowingCount(attentionService.getFollowingCount(hostId));
        homepageVo.setLikeCount(likesService.selectLikesCountByUid(hostId));

        return AjaxResult.success(homepageVo);
    }
}
