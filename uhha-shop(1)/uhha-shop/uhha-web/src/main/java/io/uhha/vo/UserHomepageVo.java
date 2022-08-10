package io.uhha.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import io.uhha.shortvideo.vo.ShortVideoVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 短视频首页的bean
 * @author peter
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHomepageVo implements Serializable {
    /**
     * 会员昵称
     */
    @SerializedName("nickname")
    private String nickname;

    /**
     * uid
     */
    @SerializedName("uid")
    private Long uid;

    /**
     * 性别
     */
    @SerializedName("gender")
    private String gender;

    /**
     * 生日
     */
    @SerializedName("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 介绍
     */
    @SerializedName("introduction")
    private String introduction;


    /**
     * 会员头像
     */
    @SerializedName("avatar")
    private String avatarUrl;

    /**
     * 是否带货
     */
    @SerializedName("with_new_goods")
    private boolean withNewGoods;

    /**
     * 国家
     */
    @SerializedName("country")
    private String country;
    /**
     * 城市
     */
    @SerializedName("city")
    private String city;

    /**
     * 登陆用户的关注状态
     */
    @SerializedName("follower_status")
    private Long followerStatus;

    /**
     * 关注数
     */
    @SerializedName("following_count")
    private Long followingCount;

    /**
     * 被关注数
     */
    @SerializedName("follower_count")
    private Long followerCount;

    /**
     * 被点赞数
     */
    @SerializedName("like_count")
    private Long likeCount;

    /**
     * 当前用户是否屏蔽对方
     */
    @SerializedName("is_block")
    private Boolean isBlock;

    /**
     * 用户的个人背景图片
     */
    @SerializedName("cover_url")
    private String coverUrl;

    /**
     * 用户发布的短视频列表
     */
    private List<ShortVideoVo> shortVideoVoList;

    /**
     * 用户喜欢的视频列表
     */
    private List<ShortVideoVo> likeShortVideoList;
}
