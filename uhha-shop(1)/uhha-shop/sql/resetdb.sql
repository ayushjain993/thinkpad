-- 店铺相关信息
delete from sys_store_user where user_id!=1; #删除商铺用户
delete FROM sys_store_user_role where user_id!=1; #删除用户角色
delete from sys_store_user where user_id !=1;

-- 物流
truncate t_store_info ;
truncate oms_logistics_template;
truncate oms_logistics_company_use;

-- 日志
truncate sys_oper_log;

-- 文章
truncate cms_article;
truncate cms_help;

-- IM
truncate im_session;
truncate im_message;
truncate im_user;
truncate im_user_friends;

-- 验证信息
truncate validate_email;
truncate validate_sms;
truncate validate_statistics;
-- 站内信
truncate ls_station_letter;


-- --------------------------------------------------------------------------
-- 订单部分
-- --------------------------------------------------------------------------
truncate oms_order;
truncate oms_order_attr;
truncate oms_order_sku;
truncate oms_order_operation_log;
truncate oms_back_order;
truncate oms_back_order_log;

truncate t_store_order;
truncate t_store_order_attr;
truncate t_store_order_sku;
truncate t_store_order_operation_log;

-- --------------------------------------------------------------------------
-- 财务部分
-- --------------------------------------------------------------------------
truncate oms_trans_records;
truncate oms_billing_records;
truncate oms_commission_records;
truncate settlement_account;
delete from settlement_account where account_type!='SYSTEM';
truncate internal_trans;
truncate settlement_record;
truncate settlement_withdrawal;
truncate account_code;

-- --------------------------------------------------------------------------
-- 用户部分
-- --------------------------------------------------------------------------
truncate ums_member; -- 会员信息
truncate ums_browse_record; -- 用户访问记录
truncate ums_member_address; -- 用户地址信息
truncate ums_member_attention; -- 用户关注商品
truncate ums_pre_deposit_record; -- 充值记录

-- 首页推荐商品、广告、推荐品牌、推荐话题等
truncate sms_home_advertise;
truncate sms_home_brand;
truncate sms_home_new_product;
truncate sms_home_recommend_product;
truncate sms_home_recommend_subject;

-- --------------------------------------------------------------------------
-- 商品信息部分
-- --------------------------------------------------------------------------
truncate pms_goods;	#删除商铺商品信息
truncate pms_goods_attribute_value; #删除商品属性信息
truncate pms_sku; #删除单品信息；
truncate pms_sku_spec_value; #删除单品规格信息
truncate pms_spec ; #删除商品的规格模板；
truncate pms_spec_value; #删除商品规格值信息
truncate pms_attention; # 关注
truncate pms_brand; # 品牌
truncate pms_brand_apply; # 品牌申请
truncate pms_category; # 产品种类表
truncate pms_category_spec; # 产品类别规格表
truncate pms_attribute; # 产品属性表
truncate pms_attribute_value;
truncate pms_type; # 产品类型
truncate pms_comment;
truncate pms_comment_picture;
truncate pms_comment_replay;

-- --------------------------------------------------------------------------
-- 视频部分
-- --------------------------------------------------------------------------
truncate short_video; # 视频
truncate short_video_comment; # 视频评论
truncate short_video_watch_record; # 视频观看记录

-- --------------------------------------------------------------------------
-- 加密货币部分
-- --------------------------------------------------------------------------
truncate f_user_virtual_address;
truncate f_user_virtual_address_withdraw;
truncate f_virtual_capital_operation;
truncate f_wallet_capital_operation;
truncate user_coin_wallet;
