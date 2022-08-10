package io.uhha.shortvideo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import io.uhha.common.enums.AuditStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 短视频对象 short_video
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortVideo
{
    private static final long serialVersionUID = 1L;

    /** 作者用户id */
    private Long uid;

    /** 商品id */
    private Long spuId;

    /** 视频id */
    @Excel(name = "视频id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String videoId;

    /** 视频文件名称 */
    @Excel(name = "视频文件名称")
    private String videoFileName;

    /** 视频链接 */
    @Excel(name = "视频链接")
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
    @Excel(name = "时长小时")
    private Long hours;

    /** 时长分钟 */
    @Excel(name = "时长分钟")
    private Long minutes;

    /** 时长秒 */
    @Excel(name = "时长秒")
    private Long seconds;

    /** 位置名称 */
    @Excel(name = "位置名称")
    private String location;

    /** 距离 */
    @Excel(name = "位置名称")
    @TableField(exist = false)
    private String distance;

    /** 维度 */
    private float latitude;

    /** 经度 */
    private float longitude;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 文本内容 */
    @Excel(name = "文本内容")
    private String content;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String frontPage;

    /** 话题列表 */
    @Excel(name = "话题列表")
    private String topicList;

    /** 产品列表 */
    @Excel(name = "产品列表")
    private String productList;

    /** 是否推销产品 */
    @Excel(name = "是否推销产品")
    private String havingProduct;

    /** 播放量 */
    @Excel(name = "播放量")
    private Long pv;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likedqty;

    /** 收藏数 */
    @Excel(name = "收藏数")
    private Long collectedqty;

    /** 回复数 */
    @Excel(name = "回复数")
    private Long repliedqty;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String createBy;

}
