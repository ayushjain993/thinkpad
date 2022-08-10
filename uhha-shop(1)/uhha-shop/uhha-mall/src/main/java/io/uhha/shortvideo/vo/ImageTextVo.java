package io.uhha.shortvideo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ImageTextVo {


    /** 用户id */
    private Long uid;

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

}
