package io.uhha.store.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 门店支付类型对象 t_store_paytype
 *
 * @author mj
 * @date 2020-07-28
 */
public class TStorePaytype extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 支付名称
     */
    @Excel(name = "支付名称")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("createTime", getCreateTime())
                .toString();
    }
}
