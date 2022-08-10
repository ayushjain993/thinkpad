package io.uhha.setting.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.uhha.common.enums.StationLetterTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 站内信对象 ls_station_letter
 *
 * @author mj
 * @date 2020-07-29
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LsStationLetter extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 会员id
     */
    @Excel(name = "会员id")
    private Long customerId;

    /**
     * 站内信标题
     */
    @Excel(name = "站内信标题")
    private String title;

    /**
     * 站内信类型 0 公告 1 交易相关 2 私信
     * @see StationLetterTypeEnum
     */
    @Excel(name = "站内信类型")
    private String type;


    /**
     * 站内信内容
     */
    @Excel(name = "站内信内容")
    private String content;

    /**
     * 是否已读  0 未读  1 已读 默认0
     */
    @Excel(name = "是否已读  0 未读  1 已读 默认0 ")
    private String isRead;

    /**
     * 删除标记  0 未删除 1 删除 默认0
     */
    private int delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("customerId", getCustomerId())
                .append("title", getTitle())
                .append("content", getContent())
                .append("isRead", getIsRead())
                .append("delFlag", getDelFlag())
                .append("createTime", getCreateTime())
                .toString();
    }
}
