package io.uhha.integral.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.uhha.common.enums.BillingDirectionEnum;
import io.uhha.common.enums.PointRecordTypeEnum;
import io.uhha.common.utils.CustomLocalDateTimeDeserializer;
import io.uhha.common.utils.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by mj on 17/5/25.
 * 会员积分
 */
@Data
@ApiModel(description = "会员积分实体")
public class CustomerPoint {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private long id;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private long customerId;

    /**
     * 积分详情
     */
    @ApiModelProperty(value = "积分详情")
    private String detail;

    /**
     * 积分类型 1 收入 2支出
     * @see BillingDirectionEnum
     */
    @ApiModelProperty(value = "积分类型 0 收入 1支出")
    private String type;

    /**
     * 积分纪录来源类型 1 订单使用 2 订单取消 3 操作员修改 4 签到 5 积分商城使用 6 邮箱验证 7 评论 8 注册赠送
     */
    @ApiModelProperty(value = "积分纪录来源类型 1 订单使用 2 订单取消 3 操作员修改 4 签到 5 积分商城使用 6 邮箱验证 7 评论 8 注册赠送")
    private String sourceType;

    /**
     * 积分数量 正数表示增加，负数表示减少
     */
    @ApiModelProperty(value = "积分数量 正数表示增加，负数表示减少")
    private int point;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 扩展字段
     */
    private String extension;


    /**
     * 构造第一次验证邮箱时候的积分对象
     *
     * @param customerId 用户id
     * @param point      赠送的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForVerifyEmail(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.EMAIL_VERIFY.getCode();
        customerPoint.detail = PointRecordTypeEnum.EMAIL_VERIFY.getDescription();
        return customerPoint;
    }

    /**
     * 构造评论赠送的积分对象
     *
     * @param customerId 用户id
     * @param point      赠送的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForComment(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.ECOMMERCE_COMMENT.getCode();
        customerPoint.detail = PointRecordTypeEnum.ECOMMERCE_COMMENT.getDescription();
        return customerPoint;
    }

    /**
     * 构造评论赠送的积分对象
     *
     * @param customerId 用户id
     * @param point      赠送的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForVideoComment(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.VIDEO_COMMENT.getCode();
        customerPoint.detail = PointRecordTypeEnum.VIDEO_COMMENT.getDescription();
        return customerPoint;
    }

    /**
     * 构造发布视频赠送的积分对象
     *
     * @param customerId 用户id
     * @param point      赠送的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForVideo(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.POST_VIDEO.getCode();
        customerPoint.detail = PointRecordTypeEnum.POST_VIDEO.getDescription();
        return customerPoint;
    }

    /**
     * 构造视频点赞赠送的积分对象
     *
     * @param customerId 用户id
     * @param point      赠送的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForVideoLike(long customerId, int point, String videoId) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.VIDEO_LIKE.getCode();
        customerPoint.detail = PointRecordTypeEnum.VIDEO_LIKE.getDescription();
        customerPoint.extension = videoId;
        return customerPoint;
    }

    /**
     * 构造评论点赞赠送的积分对象
     *
     * @param customerId 用户id
     * @param point      赠送的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForCommentLike(long customerId, int point, String commentId) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.VIDEO_LIKE.getCode();
        customerPoint.detail = PointRecordTypeEnum.VIDEO_LIKE.getDescription();
        customerPoint.extension = commentId;
        return customerPoint;
    }

    /**
     * 构造注册时候的积分对象
     *
     * @param customerId 用户id
     * @param point      赠送的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForRegister(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.REGISTER.getCode();
        customerPoint.detail = PointRecordTypeEnum.REGISTER.getDescription();
        return customerPoint;
    }

    /**
     * 构造下订单时候的积分对象
     *
     * @param customerId 用户id
     * @param point      使用的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForOrder(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = -point;
        customerPoint.type = BillingDirectionEnum.OUTCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.ORDER_CONSUMED.getCode();
        customerPoint.detail = PointRecordTypeEnum.ORDER_CONSUMED.getDescription();
        return customerPoint;
    }

    /**
     * 构造取消订单时候的积分对象
     *
     * @param customerId 会员id
     * @param point      返回的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForCancelOrder(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.ORDER_CANCELED.getCode();
        customerPoint.detail = PointRecordTypeEnum.ORDER_CANCELED.getDescription();
        return customerPoint;
    }

    /**
     * 构造签到时候的积分对象
     *
     * @param customerId 会员id
     * @param point      赠送的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForSignIn(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = point;
        customerPoint.type = BillingDirectionEnum.INCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.SIGN.getCode();
        customerPoint.detail = PointRecordTypeEnum.SIGN.getDescription();
        return customerPoint;
    }

    /**
     * 构造积分商城购物时候的积分对象
     *
     * @param customerId 会员id
     * @param point      消费的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForPointMallOrder(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = -point;
        customerPoint.type = BillingDirectionEnum.OUTCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.POINT_STORE.getCode();
        customerPoint.detail = PointRecordTypeEnum.POINT_STORE.getDescription();
        return customerPoint;
    }

    /**
     * 构造积分兑换Token时候的积分对象
     *
     * @param customerId 会员id
     * @param point      消费的积分
     * @return 返回积分对象
     */
    public static CustomerPoint buildForRedeemOrder(long customerId, int point) {
        CustomerPoint customerPoint = new CustomerPoint();
        customerPoint.customerId = customerId;
        customerPoint.point = -point;
        customerPoint.type = BillingDirectionEnum.OUTCOME.getCode();
        customerPoint.sourceType = PointRecordTypeEnum.REDEEM_ORDER.getCode();
        customerPoint.detail = PointRecordTypeEnum.REDEEM_ORDER.getDescription();
        return customerPoint;
    }

    /**
     * 设置管理员新增积分时候的默认值
     *
     * @return 返回积分信息
     */
    public CustomerPoint setValuesForManagerAdd() {
        this.sourceType = PointRecordTypeEnum.OPERATOR_UPDATE.getCode();
        if (this.point >= 0) {
            this.type = BillingDirectionEnum.INCOME.getCode();
        } else {
            this.type = BillingDirectionEnum.OUTCOME.getCode();
        }
        return this;
    }

    /**
     * 检查更改积分
     *
     * @param point 当前总积分
     * @return true 可以更改 false 更改后总金额小于0，不可以更改
     */
    public boolean checkChangePoint(int point) {
        if (this.point >= 0) {
            return true;
        }
        if (Math.abs(this.point) > point) {
            return false;
        }
        return true;
    }
}
