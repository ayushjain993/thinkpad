package io.uhha.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.uhha.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 实名认证申请对象 realname_application_form
 *
 * @author uhha
 * @date 2022-03-13
 */
@Data
public class RealnameApplicationForm
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 用户银行卡id */
    @Excel(name = "用户银行卡id")
    private Long userBankId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long uid;

    /** 实名申请批表状态 */
    @Excel(name = "实名申请批表状态")
    private String status;

    /** 审批意见 */
    @Excel(name = "审批意见")
    private String opinion;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /** 修改者 */
    private String updateBy;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userBankId", getUserBankId())
                .append("uid", getUid())
                .append("status", getStatus())
                .append("opinion", getOpinion())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}
