package io.uhha.setting.domain;

import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 短信接口设置对象 ls_sms_setting
 *
 * @author mj
 * @date 2020-07-28
 */
@Data
public class LsSmsSetting extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * AppKey
     */
    @Excel(name = "AppKey")
    private String appKey;

    /**
     * AppSecret
     */
    @Excel(name = "AppSecret")
    private String appSecret;

    /**
     * 短信接口地址
     */
    @Excel(name = "短信接口地址")
    private String url;

    /**
     * 短信签名
     */
    @Excel(name = "短信签名")
    private String sign;

    /**
     * 模板id
     */
    @Excel(name = "模板id")
    private String templateId;

    /**
     * 核销门店订单的模版id
     */
    @Excel(name = "核销门店订单的模版id")
    private String writeoffTemplateId;

    /**
     * 虚拟订单核销的模版id
     */
    @Excel(name = "虚拟订单核销的模版id")
    private String virtualOrderTemplateId;



    /**
     * 社区团购审核结果通知模版id
     */
    @Excel(name = "社区团购审核结果通知模版id")
    private String auditTemplateId;

    /**
     * 社区团购佣金结算模版id
     */
    @Excel(name = "社区团购佣金结算模版id")
    private String settlementTemplateId;

    /**
     * 社区团购提现打款模版id
     */
    @Excel(name = "社区团购提现打款模版id")
    private String withdrawTemplateId;
    @ApiModelProperty("启用状态")
    private String isUse;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("secret", getAppSecret())
                .append("url", getUrl())
                .append("sign", getSign())
                .append("isUse", getIsUse())
                .append("templateId", getTemplateId())
                .append("writeoffTemplateId", getWriteoffTemplateId())
                .append("virtualOrderTemplateId", getVirtualOrderTemplateId())
                .append("key", getAppKey())
                .append("auditTemplateId", getAuditTemplateId())
                .append("settlementTemplateId", getSettlementTemplateId())
                .append("withdrawTemplateId", getWithdrawTemplateId())
                .toString();
    }
}
