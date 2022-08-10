package io.uhha.store.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺信息更改日志对象 t_store_info_change_history
 *
 * @author uhha
 * @date 2022-01-26
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TStoreInfoChangeHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 事件名称
     * @see io.uhha.common.enums.StoreChangeEventEnum
     */
    @Excel(name = "事件名称")
    private String eventName;

    /**
     * 事件内容
     */
    @Excel(name = "事件内容")
    private String eventContent;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建者
     */
    private String createBy;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeId", getStoreId())
                .append("eventName", getEventName())
                .append("eventContent", getEventContent())
                .append("createTime", getCreateTime())
                .toString();
    }
}
