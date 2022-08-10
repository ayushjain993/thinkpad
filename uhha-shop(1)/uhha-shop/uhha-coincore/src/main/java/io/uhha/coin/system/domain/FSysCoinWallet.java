package io.uhha.coin.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统钱包，主要做头寸
 * @author LY
 *
 */
@Data
public class FSysCoinWallet implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// 主键ID
	private Integer id;
	//名称
    private String name;
    // 币种ID
    private Integer coinId;
    // 可用
    private BigDecimal total;
    // 冻结
    private BigDecimal frozen;
    // 理财
    private BigDecimal borrow;
    //版本
    private Integer version;
    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;
    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModified;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("coinId", getCoinId())
                .append("total", getTotal())
                .append("frozen", getFrozen())
                .append("borrow", getBorrow())
                .append("gmtCreate", getGmtCreate())
                .append("gmtModified", getGmtModified())
                .toString();
    }

}