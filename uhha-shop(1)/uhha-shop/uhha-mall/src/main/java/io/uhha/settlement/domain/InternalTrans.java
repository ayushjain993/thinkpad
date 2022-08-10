package io.uhha.settlement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 转移记录对象 internal_trans
 *
 * @author uhha
 * @date 2021-12-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternalTrans {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 源账户
     */
    @Excel(name = "源账户")
    private String formAccountCode;

    /**
     * 目标账户
     */
    @Excel(name = "目标账户")
    private String toAccountCode;

    /**
     * 转移时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "转移时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transferTime;

    /**
     * 转移金额
     */
    @Excel(name = "转移金额")
    private BigDecimal transferAmount;

    /**
     * 进出账类型
     */
    @Excel(name = "进出账类型")
    private String transferType;

    /**
     * 操作类型
     */
    @Excel(name = "操作类型")
    private String operateType;

    /**
     * 删除标记  0未删除 1删除 默认0
     */
    private String delFlag;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @Excel(name = "备注")
    private String remark;
}
