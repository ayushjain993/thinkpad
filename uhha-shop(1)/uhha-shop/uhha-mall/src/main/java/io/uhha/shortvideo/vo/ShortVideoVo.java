package io.uhha.shortvideo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import io.uhha.common.enums.AuditStatusEnum;
import io.uhha.shortvideo.domain.ShortVideoComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortVideoVo {


    /** 视频id */
    private String videoId;

    /** 视频文件名称 */
    @Excel(name = "视频文件名称")
    private String videoFileName;

    /** 视频链接 */
    @NotNull
    private String videoUrl;

    /**
     * 视频封面图片
     */
    private String frontImageUrl;

    /**
     * 状态，状态 0 审核中 1 审核通过 2 审核未通过
     * @see AuditStatusEnum
     */
    @Excel(name = "状态")
    private String status;

    /** 时长小时 */
    private Long hours;

    /** 时长分钟 */
    private Long minutes;

    /** 时长秒 */
    private Long seconds;

    /** 位置名称 */
    private String location;

    /** 距离 */
    private String distance;

    /** 维度 */
    private float latitude;

    /** 经度 */
    private float longitude;

    /** 标题 */
    private String title;

    /** 文本内容 */
    @NotNull
    private String content;

    /** 封面图片 */
    private String frontPage;

    /** 话题列表 */
    private String topicList;

    /** 产品列表 */
    private String productList;

    /** 是否推销产品 */
    private String havingProduct;

    /** 播放量 */
    private Long pv;

    /** 点赞数 */
    private Long likedqty;

    /** 收藏数 */
    private Long collectedqty;

    /** 回复数 */
    private Long repliedqty;

    /**
     * 排序
     */
    private int order;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String createBy;

    /**
     * 作者uid信息
     */
    private Long uid;

    /**
     * 作者姓名
     */
    private String username;

    /**
     * 作者昵称
     */
    private String nickname;

    /**
     * 作者头像
     */
    private String avatarUrl;

    /**
     * 带货产品信息
     */
    private Long spuId;

    /**
     * 是否已经点赞
     */
    private Integer like;

    /**
     * 是否已经关注
     */
    private Integer followed;

    /**
     * 视频的评论
     */
    private List<ShortVideoComment> commentList;


}

