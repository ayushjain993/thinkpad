package io.uhha.member.domain;

import io.uhha.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;


/**
 * 用户修改历史记录对象 ums_member_change_history
 *
 * @author peter
 * @date 2021-12-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UmsMemberChangeHistory
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 用户id对应ums_member表中的id */
    @Excel(name = "用户手机号码")
    private String mobile;

    /** 事件名称
     * @see io.uhha.common.enums.UmsMemberChangeEventEnum
     * */
    @Excel(name = "事件名称")
    private String eventName;

    /** 事件内容 */
    @Excel(name = "事件内容")
    private String eventContent;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private Date createTime;

    /** 发起者 */
    @Excel(name = "发起者")
    private String createBy;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("mobile", getMobile())
                .append("eventName", getEventName())
                .append("eventContent", getEventContent())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .toString();
    }
}