package io.uhha.shortvideo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 短视频评论对象 short_video_comment
 * 
 * @author ruoyi
 * @date 2021-08-06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortVideoComment
{
    private static final long serialVersionUID = 1L;

    /** 评论id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父对象id， 0 对象为视频本身， 其余值为评论id */
    @Excel(name = "父对象id")
    private Long parentId;

    /** 视频id */
    @Excel(name = "视频id")
    @NotNull
    private String videoId;

    /** 发言人的uid */
    @Excel(name = "发言人的uid")
    private Long uid;

    /**
     * 作者名称
     */
    @TableField(exist = false)
    private String username;

    /**
     * 作者昵称
     */
    @TableField(exist = false)
    private String nickname;

    /**
     * 作者头像
     */
    @TableField(exist = false)
    private String avatarUrl;

    /** 发言内容 */
    @Excel(name = "发言内容")
    @NotNull
    private String content;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likedQty;

    /**
     * 是否已经点赞
     */
    @TableField(exist = false)
    private Integer like;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("videoId", getVideoId())
            .append("uid", getUid())
            .append("content", getContent())
            .append("likedQty", getLikedQty())
            .toString();
    }
}
