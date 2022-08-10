package io.uhha.marketing.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.uhha.common.utils.CustomLocalDateTimeDeserializer;
import io.uhha.common.utils.CustomLocalDateTimeSerializer;
import io.uhha.goods.domain.PmsSku;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 拼团活动实体用于c端的展示
 *
 * @author mj created on 2020/5/28
 */
@Data
@ApiModel(description = "拼团活动实体用于c端的展示")
public class GroupMarketingShow {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private long id;

    /**
     * 团购id对应sms_marketing_group表中的id
     */
    @ApiModelProperty(value = "团购id对应sms_marketing_group表中的id")
    private long groupId;

    /**
     * 店铺id（平台设置都为0，店铺设置为对应店铺id）
     */
    @ApiModelProperty(value = "店铺id（平台设置都为0，店铺设置为对应店铺id）")
    private long storeId;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private int sort;

    /**
     * 分类id对应ls_marleting_cate表中的id
     */
    @ApiModelProperty(value = "分类id对应ls_marleting_cate表中的id")
    private long cateId;

    /**
     * 删除标记  0 未删除 1 已删除 默认0
     */
    @ApiModelProperty(value = "删除标记  0 未删除 1 已删除 默认0")
    private int delFlag;

    /**
     * 拼团促销信息
     */
    @ApiModelProperty(value = "拼团促销信息")
    private GroupMarketing groupMarketing;

    /**
     * 单品id
     */
    @ApiModelProperty(value = "单品id")
    private String skuId;

    /**
     * 单品信息
     */
    @ApiModelProperty(value = "单品信息")
    private PmsSku sku;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String marketingCateName;

    /**
     * 原始店铺id
     */
    @ApiModelProperty(value = "原始店铺id")
    private long oldStoreId;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    /**
     * 获取促销活动状态
     *
     * @return 1 未开始 2 进行中 3 已结束
     */
    @ApiModelProperty(value = "促销活动状态")
    public String getStatus() {
        if (Objects.isNull(this.startTime) || Objects.isNull(this.endTime)) {
            return "-1";
        } else {
            if (this.startTime.compareTo(LocalDateTime.now()) > 0) {
                return "1";
            } else if (this.endTime.compareTo(LocalDateTime.now()) <= 0) {
                return "3";
            } else {
                return "2";
            }
        }
    }

    /**
     * 添加店铺id
     *
     * @param storeId 店铺id
     * @return 当前实体
     */
    public GroupMarketingShow addStoreId(long storeId) {
        this.storeId = storeId;
        return this;
    }

}
