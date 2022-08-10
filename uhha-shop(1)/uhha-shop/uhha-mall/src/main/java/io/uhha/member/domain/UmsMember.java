package io.uhha.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.uhha.common.annotation.Excel;
import io.uhha.common.core.domain.BaseEntity;
import io.uhha.common.enums.UserRegisterSourceEnum;
import io.uhha.common.utils.DateUtils;
import io.uhha.common.utils.ShareCodeUtils;
import io.swagger.annotations.ApiModelProperty;
import io.uhha.member.vo.UserBankInfoVo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * 会员对象 ums_member
 *
 * @author mj
 * @date 2020-07-25
 */
@Data
public class UmsMember extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String username;

    /**
     * 用户密码
     */
    @Excel(name = "用户密码")
    private String password;

    /**
     * 昵称
     */
    @Excel(name = "昵称")
    private String nickname;

    /**
     * 头像地址
     */
    @Excel(name = "头像地址")
    private String image;

    /**
     * 真实名
     */
    @Excel(name = "真实名")
    private String firstname;

    /**
     * 真实姓
     */
    @Excel(name = "真实姓")
    private String lastname;


    /**
     * 国籍
     */
    @Excel(name = "国籍")
    private String nationality;

    /**
     * 国籍id
     */
    @Excel(name = "国籍id")
    private Long nationalityId;

    /**
     * 实名认证类型
     * @see io.uhha.common.enums.IdentityTypeEnum
     */
    @Excel(name = "实名认证类型")
    private String idtype;

    /**
     * 实名认证证件号
     */
    @Excel(name = "身份证")
    private String cardid;

    /**
     * 实名认证证明材料
     */
    @Excel(name = "实名认证证明材料1")
    private String certificate1;

    /**
     * 实名认证证明材料
     */
    @Excel(name = "实名认证证明材料2")
    private String certificate2;

    /**
     * 实名认证证明材料
     */
    @Excel(name = "实名认证证明材料3")
    private String certificate3;

    /**
     * 实名认证证明材料
     */
    @Excel(name = "实名认证证明材料4")
    private String certificate4;

    /**
     * 性别  0 保密 1男 2女 默认0
     */
    @Excel(name = "性别  0 保密 1男 2女 默认0")
    private String gender;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 月收入  0 无收入 1 2000以下 2 2000-3999 3 4000-59994 6000－7999  5 8000以上
     */
    @Excel(name = "月收入  0 无收入 1 2000以下 2 2000-3999 3 4000-5999 4 6000－7999  5 8000以上")
    private String monthlyIncome;

    /**
     * 婚姻状况  0 保密 1未婚 2 已婚 默认0 保姆
     */
    @Excel(name = "婚姻状况  0 保密 1未婚 2 已婚 默认0 保姆")
    private String marriageStatus;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String mobile;

    /**
     * 邮箱地址
     */
    @Excel(name = "邮箱地址")
    private String email;

    /**
     * 更改中的邮箱（绑定新邮箱使用）
     */
    @Excel(name = "更改中的邮箱")
    private String modifiedEmail;

    /**
     * 校验码（绑定新邮箱时使用）
     */
    @Excel(name = "校验码")
    private String checkCode;

    /**
     * 国家
     */
    @Excel(name = "国家")
    private String country;

    /**
     * 省
     */
    @Excel(name = "省")
    private Long province;

    /**
     * 市
     */
    @Excel(name = "市")
    private Long city;

    /**
     * 区
     */
    @Excel(name = "区")
    private Long county;

    /**
     * 详细地址
     */
    @Excel(name = "详细地址")
    private String detailaddress;

    /**
     * 兴趣爱好
     */
    @Excel(name = "兴趣爱好")
    private String interest;

    /**
     * 个人介绍限制255字
     */
    @Excel(name = "个人介绍限制255字")
    private String introduction;

    /**
     * 店铺id  平台的为0  默认为平台
     */
    @Excel(name = "店铺id  平台的为0  默认为平台")
    private Long storeId;

    /**
     * 1 普通用户 2 商家店铺用户 3 店铺员工
     */
    @Excel(name = "1 普通用户 2 商家店铺用户 3 店铺员工")
    private String type;

    private String appletOpenId;

    private String appOpenId;

    private String h5OpenId;
    /**
     * 总的消费金额
     */
    @Excel(name = "总的消费金额")
    private BigDecimal consumptionAmount;

    /**
     * 支付密码
     */
    @Excel(name = "支付密码")
    private String paypassword;

    /**
     * 支付密码修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payPasswordModifyTime;

    /**
     * 用户注册来源 1 pc  2app  3 手机h5 4 管理员后台新增 5applet
     */
    @Excel(name = "用户注册来源 1 pc  2app  3 手机h5 4 管理员后台新增 ")
    private String source;

    /**
     * 用户状态 1 正常 2 冻结  3 未启用 默认1
     */
    @Excel(name = "用户状态 1 正常 2 冻结  3 未启用 默认1 ")
    private String status;

    /**
     * 会员签到次数
     */
    @Excel(name = "会员签到次数")
    private int signNum;

    /**
     * 手机是否验证  0 否 1 验证 默认0
     */
    @Excel(name = "手机是否验证  0 否 1 验证 默认0 ")
    private String isMobileVerification;

    /**
     * 邮箱是否验证   0 否 1 验证  默认0
     */
    @Excel(name = "邮箱是否验证   0 否 1 验证  默认0  ")
    private String isEmailVerification;

    /**
     * 实名认证状态 0 已提交 1 验证通过  2 已拒绝 默认0
     */
    @Excel(name = "实名认证状态   0 已提交 1 验证通过  2 已拒绝 默认0  ")
    private String realnameScreenStatus;

    /**
     * Google验证器是否验证   0 否 1 验证  默认0
     */
    @Excel(name = "Google验证器是否验证   0 否 1 验证  默认0  ")
    private String isGoogleVerification;

    /**
     * 谷歌验证密匙
     */
    private String googleAuthenticator;

    /**
     * 谷歌验证URL
     */
    private String googleUrl;

    /**
     * 手机区号（国家码）
     */
    private String areacode;

    /**
     * 是否允许法定货币提现 0 否 1 是  默认0
     */
    private String allowFiatWithdraw;

    /**
     * 是否允许虚拟币提现 0 否 1 是  默认0
     */
    private String allowCryptoWithdraw;

    /**
     * 错误登录的次数
     */
    @Excel(name = "错误登录的次数")
    private int loginErrorCount;

    /**
     * 删除标记 0 未删除 1删除 默认0
     */
    private int delFlag;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modifyTime;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date delTime;

    /**
     * 锁定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "锁定时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime lockTime;

    /**
     * 最近登陆时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "最近登陆时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastLoginTime;

    /**
     * 推荐人的会员id  如果没有 则为-1  默认为-1
     */
    @Excel(name = "推荐人的会员id  如果没有 则为-1  默认为-1")
    private Long recommended = -1L;

    /**
     * 二级推荐人的会员id （上级的上级） 如果没有 则为-1  默认为-1
     */
    @Excel(name = "二级推荐人的会员id ", readConverterExp = "上=级的上级")
    private Long sRecommended = -1L;

    /**
     * 会员自己的推荐码
     */
    @Excel(name = "会员自己的推荐码")
    private String selfRecommendCode;

    /**
     * 会员的佣金
     */
    @Excel(name = "会员的佣金")
    private BigDecimal commission;


    /**
     * 会员的佣金
     */
    @Excel(name = "会员冻结的佣金")
    private BigDecimal commissionFrozen;

    /**
     * 会员所属的店铺id
     */
    @Excel(name = "会员所属的店铺id")
    private Long beloneStoreId;
    /**
     * 用户积分
     */
    @ApiModelProperty(value = "用户积分")
    private int customerPoint;
    private int subSpreadCustomerCount;
    /**
     * 推荐人code 如果为空 则说明没有推荐人
     */
    private String recommondCode;
    /**
     * 自动注册渠道类型
     */
    @ApiModelProperty(value = "自动注册渠道类型")
    private String channelType;
    private UmsMemberLevel customerLevel;

    private BigDecimal allPredeposit;

    /**
     * 上次登录ip地址
     */
    private String ip;

    /**
     * 银行卡配置信息
     */
    private UserBankInfoVo userBankInfo;

    /**
     * 构造app用户注册对象
     * @param countryCode 国家
     * @param country 国家码
     * @param mobile        手机号码
     * @param password      密码
     * @param recommondCode 推荐吗
     * @return 返回app用户注册对象
     */
    public static UmsMember buildAppRegisterCustomer(String countryCode, String country, String mobile, String password, String recommondCode) {
        UmsMember customer = new UmsMember();
        customer.country = country;
        customer.areacode = countryCode;
        customer.username = mobile;
        customer.password = password;
        customer.mobile = mobile;
        customer.type = "1";
        customer.source = UserRegisterSourceEnum.H5.getCode();
        customer.isMobileVerification = "1";
        customer.setCreateTime(DateUtils.getNowDate());

        customer.recommondCode = recommondCode;
        return customer;
    }

    /**
     * 构造h5用户注册对象
     *
     * @param mobile        手机号码
     * @param recommondCode 推荐吗
     * @return 返回app用户注册对象
     */
    public static UmsMember buildH5RegisterCustomer(String mobile, String recommondCode) {
        UmsMember customer = new UmsMember();
        customer.username = mobile;
        customer.password = "123456";
        customer.mobile = mobile;
        customer.type = "1";
        customer.source = UserRegisterSourceEnum.APP.getCode();
        customer.isMobileVerification = "1";
        customer.setCreateTime(DateUtils.getNowDate());

        customer.recommondCode = recommondCode;
        return customer;
    }

    /**
     * 构造代客下单自动注册对象
     *
     * @param mobile 手机号码
     * @return 返回代客下单自动注册对象
     */
    public static UmsMember buildStoreShoppingRegisterCustomer(String mobile, long beloneStoreId) {
        UmsMember customer = new UmsMember();
        customer.username = mobile;
        customer.password = "123456";
        customer.mobile = mobile;
        customer.type = "1";
        customer.source = UserRegisterSourceEnum.BACKEND.getCode();
        customer.status = "1";
        customer.storeId = 0L;
        customer.isMobileVerification = "1";
        customer.setCreateTime(DateUtils.getNowDate());
        customer.beloneStoreId = beloneStoreId;
        return customer;

    }

    public int getSubSpreadCustomerCount() {
        return subSpreadCustomerCount;
    }

    public void setSubSpreadCustomerCount(int subSpreadCustomerCount) {
        this.subSpreadCustomerCount = subSpreadCustomerCount;
    }

    public String getRecommondCode() {
        return recommondCode;
    }

    public void setRecommondCode(String recommondCode) {
        this.recommondCode = recommondCode;
    }

    public UmsMemberLevel getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(UmsMemberLevel customerLevel) {
        this.customerLevel = customerLevel;
    }

    /**
     * 设置下级分销会员数量
     *
     * @param subSpreadCustomerCount 下级分销会员数量
     * @return 返回当前对象
     */
    public UmsMember addSubSpreadCustomerCount(int subSpreadCustomerCount) {
        this.subSpreadCustomerCount = subSpreadCustomerCount;
        return this;
    }

    /**
     * 设置自己的推荐吗
     *
     * @return 返回当前对象
     */
    public UmsMember addSelfRecommondCode() {
        this.selfRecommendCode = ShareCodeUtils.toSerialCode(Long.parseLong(this.mobile));
        return this;
    }

    /**
     * 判断是否有推荐人  有返回true  没有返回false
     *
     * @return 有返回true  没有返回false
     */
    @JsonIgnore
    public boolean hasRecommonded() {
        return !StringUtils.isEmpty(this.recommondCode) || this.recommended != -1;
    }

    /**
     * 后端添加用户设置默认值
     *
     * @return 返回用户信息
     */
    public UmsMember setDefaultValuesForAdminAdd() {
        this.storeId = 0L;
        this.type = "1";
        this.source = "4";
        this.status = "1";
        this.isMobileVerification = "1";
        if (!StringUtils.isEmpty(this.email)) {
            this.isEmailVerification = "1";
        }

        this.beloneStoreId = 0L;
        return this;
    }

    /**
     * 设置归属门店id
     *
     * @param beloneStoreId 归属门店id
     * @return 返回用户信息
     */
    public UmsMember setBelongStoreIdForStore(long beloneStoreId) {
        this.beloneStoreId = beloneStoreId;
        return this;
    }

    /**
     * 清除用户密码、google认证秘钥等敏感信息
     */
    public UmsMember clearSensitiveInfo() {
        this.password = "**********";
        if (!StringUtils.isEmpty(this.paypassword)) {
            this.paypassword = "**********";
        }
        this.googleAuthenticator = "**********";
        this.googleUrl = "**********";
        return this;
    }

    /**
     * 获取会员对象
     *
     * @param customerId 会员id
     * @return 会员对象
     */
    public UmsMember setCustomerId(long customerId) {
        this.id = customerId;
        return this;
    }

    /**
     * 获得会员的会员等级id
     *
     * @return 返回会员的会员等级id
     */
    @JsonIgnore
    public long getCustomerLevelId() {
        return Objects.isNull(this.customerLevel) ? -1 : this.customerLevel.getId();
    }

    /**
     * 获得会员等级名称
     *
     * @return 返回会员等级名称
     */
    @JsonIgnore
    public String getCustomerLevelName() {
        return Objects.isNull(this.customerLevel) ? "" : this.customerLevel.getName();
    }

    /**
     * 检查更改金额
     *
     * @param money 更改金额
     * @return true 可以更改 false 更改后总金额小于0，不可以更改
     */
    public boolean checkConsumptionAmount(BigDecimal money) {
        if (money.intValue() >= 0) {
            return true;
        }
        if (money.abs().compareTo(this.consumptionAmount) > 0) {
            return false;
        }
        return true;
    }

    /**
     * 检查更改佣金金额
     *
     * @param money 更改佣金金额
     * @return true 可以更改 false 更改后总金额小于0，不可以更改
     */
    public boolean checkCommission(BigDecimal money) {
        if (money.intValue() >= 0) {
            return true;
        }
        if (money.abs().compareTo(this.commission) > 0) {
            return false;
        }
        return true;
    }

    /**
     * 检查更改提款金额
     *
     * @param money 更改余额金额
     * @return true 可以更改 false 更改后总金额小于0，不可以更改
     */
    public boolean checkWithdrawBalance(BigDecimal money) {
        if (money.intValue() >= 0) {
            return true;
        }
        this.allPredeposit = this.allPredeposit == null? BigDecimal.ZERO:this.allPredeposit;
        if (money.abs().compareTo(this.allPredeposit) > 0) {
            return false;
        }
        return true;
    }


    /**
     * 为用户添加用户名
     *
     * @param username
     * @return
     */
    public UmsMember addUsername(String username) {
        this.username = username;
        return this;
    }

    public boolean isMobileVerified() {
        return "1".equalsIgnoreCase(this.isMobileVerification);
    }

    public boolean isEmailVerified() {
        return "1".equalsIgnoreCase(this.isEmailVerification);
    }

    public boolean isGoogleVerified() {
        return "1".equalsIgnoreCase(this.isGoogleVerification);
    }

    public boolean isRealNameScreened() {
        return "1".equalsIgnoreCase(this.realnameScreenStatus);
    }

}
