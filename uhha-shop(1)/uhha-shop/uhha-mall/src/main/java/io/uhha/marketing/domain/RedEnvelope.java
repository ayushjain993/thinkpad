package io.uhha.marketing.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.uhha.common.utils.CustomLocalDateTimeDeserializer;
import io.uhha.common.utils.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by mj on 18/1/15
 * 红包实体类
 */
@Data
@ApiModel(description = "红包实体类")
public class RedEnvelope {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private long id;
    /**
     * 红包名称
     */
    @ApiModelProperty(value = "红包名称")
    private String name;
    /**
     * 生成红包的个数
     */
    @ApiModelProperty(value = "生成红包的个数")
    private int num;
    /**
     * 每人可以领取的红包数量
     */
    @ApiModelProperty(value = "每人可以领取的红包数量")
    private int limitNum;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String desc;
    /**
     * 店铺报名截止时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "店铺报名截止时间")
    private LocalDateTime signupTime;
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
     * 删除标记 0未删除 1删除 默认0
     */
    @ApiModelProperty(value = "删除标记 0未删除 1删除 默认0")
    private int delFlag;

    /**
     * 满多少钱
     */
    @ApiModelProperty(value = "满多少钱")
    private BigDecimal fullPrice;

    /**
     * 减多少钱
     */
    @ApiModelProperty(value = "减多少钱")
    private BigDecimal price;

    /**
     * 是否领完
     */
    @ApiModelProperty(value = "是否领完")
    private boolean runOut;

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private int canReceiveCount;

    /**
     * 店铺是否已经可以参加红包 false 不能参加(红包id或店铺id为空，红包已过期，已经参加过)  true 可以参加
     */
    @ApiModelProperty(value = "店铺是否已经可以参加红包 false 不能参加(红包id或店铺id为空，红包已过期，已经参加过)  true 可以参加")
    private boolean ifCanJoin;

    /**
     * 红包是否已领完 0 否  1 是  默认0
     */
    @ApiModelProperty(value = "红包是否已领完 0 否  1 是  默认0")
    private String status;

    /**
     * 构建是否可以参加
     *
     * @return 红包实体
     */
    public RedEnvelope buildIfCanJoin(boolean ifCanJoin) {
        this.ifCanJoin = ifCanJoin;
        return this;
    }

    /**
     * 构建剩余数量
     *
     * @return 红包实体
     */
    public RedEnvelope buildCanReceiveCount(int canReceiveCount) {
        this.canReceiveCount = canReceiveCount;
        return this;
    }

    /**
     * 构建是否领完
     *
     * @return 红包实体
     */
    public RedEnvelope buildIsRunOut() {
        this.runOut = this.checkRunOut();
        return this;
    }

    /**
     * 是否领完 true 已领完
     */
    @JsonIgnore
    public boolean checkRunOut() {
        return this.canReceiveCount <= 0;
    }

    /**
     * 判断开始时间是否大于结束时间
     *
     * @return 开始时间大于结束时间返回true, 小于false
     */
    public boolean toCompareStartTime() {
        return this.getStartTime().isAfter(this.getEndTime());
    }

    /**
     * 判断店铺报名截止时间是否大于开始时间
     *
     * @return 店铺报名截止时间是否大于开始时间返回true, 小于false
     */
    public boolean toCompareSignupTime() {
        return this.getSignupTime().isAfter(this.getStartTime());
    }

}
