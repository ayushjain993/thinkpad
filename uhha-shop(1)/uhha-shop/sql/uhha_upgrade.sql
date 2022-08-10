-- UMS_MEMBER
ALTER TABLE `ums_member`
    ADD COLUMN `is_real_name_screened`   varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否通过实名认证   0 否 1 验证  默认0' after `is_email_verification`,
    ADD COLUMN `is_google_verification`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '手机是否验证  0 否 1 验证 默认0' AFTER `is_real_name_screened`,
    ADD COLUMN `google_authenticator`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '谷歌验证密匙' AFTER `is_google_verification`,
    ADD COLUMN `google_url` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '谷歌验证URL'  DEFAULT NULL AFTER `google_authenticator`,
    ADD COLUMN `allow_fiat_withdraw`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否允许法定货币提现 0 否 1 是  默认0'  DEFAULT NULL AFTER `google_url`,
    ADD COLUMN `allow_crypto_withdraw`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否允许虚拟币提现 0 否 1 是  默认0'  AFTER `allow_fiat_withdraw`;

-- bugfix 修改分类增加名字长度
ALTER TABLE `pms_category`
    MODIFY COLUMN `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名称';

-- 2021/10 pms_goods_import

ALTER TABLE `pms_goods_import`
    ADD COLUMN `channel`  varchar(20) NULL AFTER `del_flag`,
    ADD COLUMN `numIid`  varchar(16) NULL AFTER `channel`,
    ADD COLUMN `pic_url`  varchar(512) NULL AFTER `numIid`,
    ADD COLUMN `org_url`  varchar(512) NULL AFTER `pic_url`,
    ADD COLUMN `shop_id`  varchar(20) NULL AFTER `org_url`;

ALTER TABLE `pms_goods_import`
    add column `first_cate_id` bigint(20) comment '一级分类id' after `shop_id`,
    add column `second_cate_id` bigint(20) comment '二级分类id' after `first_cate_id`,
    add column `third_cate_id` bigint(20) comment '三级分类id' after `second_cate_id`;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品导入', '5', '1', 'importGoods', 'goods/goods/importspu', 1, 0, 'C', '0', '0', 'goods:goods:list', '#', 'admin', sysdate(), '', null, '商品导入菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品导入查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'goods:goods:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品导入新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'goods:goods:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品导入修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'goods:goods:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品导入删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'goods:goods:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品导入导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'goods:goods:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品源配置', '2169', '1', 'skuSourceSetting', 'setting/LsSkuSourceSetting/index.vue', 1, 0, 'C', '0', '0', 'system:skuSourceSetting:list', 'edit', 'admin', sysdate(), '', null, 'SPU源配置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品源配置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:skuSourceSetting:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品源配置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:skuSourceSetting:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品源配置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:skuSourceSetting:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品源配置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:skuSourceSetting:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('商品源配置导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:skuSourceSetting:export',       '#', 'admin', sysdate(), '', null, '');

-- 升级ruoyi框架升级脚本
ALTER TABLE sys_menu add query             varchar(255)    default ''               comment '路由参数' after component;


-- 添加用户修改历史记录
DROP TABLE IF EXISTS `ums_member_change_history`;
CREATE TABLE `ums_member_change_history`
(
    `id`             bigint(20)                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `mobile`    varchar(45)                              NOT NULL COMMENT '用户手机号码',
    `event_name`     varchar(255)                            DEFAULT '' NOT NULL COMMENT  '事件名称',
    `event_content`  varchar(255)                            DEFAULT '' NOT NULL COMMENT  '事件内容',
    `create_time`    datetime                                DEFAULT now() NOT NULl COMMENT '事件发生事件',
    `create_by`      varchar(45)                             NULl COMMENT '事件发起者',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `mobile` (`mobile`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1080
  CHARACTER SET = utf8mb4
  CHARSET=utf8mb4_general_ci COMMENT = '用户修改历史记录表'
  ROW_FORMAT = Dynamic;

-- 初始化积分配置
INSERT INTO sms_point_setting (id, is_open, email_point, comment_point, video_point, like_point, use_point, offset_money, modify_time)
VALUES (1, 0, 0, 0, 0, 0, 0, 0, now());

-- 新增积分配置菜单
INSERT INTO `sys_menu` (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('积分配置', 2182, 1, 'PointSetting', 'member/PointSetting/index', '', 1, 0,'C', '0', '0', 'member:PointSetting:list', 'documentation', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '积分配置菜单');
INSERT INTO `sys_menu` (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('配置查询', 2080, 1, '#', '', '', 1, 0, 'F', '0', '0', 'member:PointSetting:query', 'server', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');
INSERT INTO `sys_menu` (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('配置修改', 2080, 3, '#', '', '', 1, 0, 'F', '0', '0', 'member:PointSetting:edit', 'server', 'admin', '2018-03-01 00:00:00', 'ry', '2018-03-01 00:00:00', '');

-- 会员积分字段
alter table ums_member_point add `extension` varchar(64) null comment  '扩展字段' after point;


-- ----------------------------
-- Records of oms_logistics_company
-- ----------------------------
INSERT INTO `oms_logistics_company` VALUES ('1', '圆通速递', 'yuantong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('2', '韵达快递', 'yunda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('3', '中通快递', 'zhongtong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('4', '申通快递', 'shentong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('5', '邮政快递包裹', 'youzhengguonei', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('6', '百世快递', 'huitongkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('7', '顺丰速运', 'shunfeng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('8', 'EMS', 'ems', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('9', '极兔速递', 'jtexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('10', '京东物流', 'jd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('11', '丰网速运', 'fengwang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('12', '德邦', 'debangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('13', '德邦快递', 'debangkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('14', '中通国际', 'zhongtongguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('15', '百世快运', 'baishiwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('16', '中通快运', 'zhongtongkuaiyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('17', '韵达快运', 'yundakuaiyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('18', '安能快运', 'annengwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('19', '特急送', 'lntjs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('20', '速尔快递', 'suer', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('21', '宅急送', 'zhaijisong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('22', '优速快递', 'youshuwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('23', '顺丰快运', 'shunfengkuaiyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('24', '国际包裹', 'youzhengguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('25', 'UPS', 'ups', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('26', '跨越速运', 'kuayue', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('27', 'UPS-全球件', 'upsen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('28', 'DHL-中国件', 'dhl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('29', '京广速递', 'jinguangsudikuaijian', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('30', '壹米滴答', 'yimidida', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('31', 'D速快递', 'dsukuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('32', '日日顺物流', 'rrs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('33', '安得物流', 'annto', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('34', 'FedEx-国际件', 'fedex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('35', 'EWE全球快递', 'ewe', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('36', '顺心捷达', 'sxjdfreight', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('37', 'USPS', 'usps', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('38', '丹鸟', 'danniao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('39', '联昊通', 'lianhaowuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('40', 'YunExpress', 'yuntrack', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('41', 'DHL-全球件', 'dhlen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('42', '加运美', 'jiayunmeiwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('43', '信丰物流', 'xinfengwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('44', '递四方', 'disifang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('45', '汇森速运', 'huisenky', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('46', '同城快寄', 'shpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('47', '海信物流', 'savor', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('48', '众邮快递', 'zhongyouex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('49', 'EMS包裹', 'emsbg', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('50', '速腾快递', 'suteng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('51', 'Titan泰坦国际速递', 'timelytitan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('52', '威时沛运货运', 'wtdchina', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('53', '中远e环球', 'cosco', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('54', '宏递快运', 'hd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('55', '京东快运', 'jingdongkuaiyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('56', 'EMS-国际件', 'emsguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('57', '宇鑫物流', 'yuxinwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('58', '顺昌国际', 'shunchangguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('59', '天地华宇', 'tiandihuayu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('60', '申通国际', 'stosolution', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('61', '联邦快递', 'lianbangkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('62', '圆通国际', 'yuantongguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('63', '美快国际物流', 'meiquick', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('64', '九曳供应链', 'jiuyescm', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('65', 'Xlobo贝海国际', 'xlobo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('66', 'CJ物流', 'doortodoor', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('67', 'DPD', 'dpd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('68', '燕文物流', 'yw56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('69', '日本（Japan Post）', 'japanposten', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('70', '邦泰快运', 'btexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('71', '卓志速运', 'chinaicip', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('72', '苏宁物流', 'suning', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('73', '易客满', 'ecmscn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('74', '威盛快递', 'wherexpess', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('75', '拉火速运', 'lahuoex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('76', 'UBI Australia', 'gotoubi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('77', '三态速递', 'santaisudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('78', '澳邮中国快运', 'auexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('79', '德坤物流', 'dekuncn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('80', '天天快递', 'tiantian', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('81', '速必达', 'subida', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('82', '嘉里大通', 'jialidatong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('83', '荣庆物流', 'rokin', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('84', 'GLS', 'gls', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('85', '平安达腾飞', 'pingandatengfei', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('86', '中铁快运', 'ztky', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('87', '鑫正一快递', 'zhengyikuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('88', 'TNT', 'tnt', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('89', '四季安物流', 'sja56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('90', '出口易', 'chukou1', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('91', '安能快递', 'ane66', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('92', 'TNT-全球件', 'tnten', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('93', '优邦速运', 'ubonex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('94', '佐川急便', 'sagawa', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('95', '全一快递', 'quanyikuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('96', '叁虎物流', 'sanhuwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('97', 'Fedex-国际件-中文', 'fedexcn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('98', '时安达速递', 'goex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('99', '新顺丰（NSF）', 'nsf', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('100', '大马鹿', 'idamalu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('101', '雪域易购', 'qhxyyg', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('102', '盛丰物流', 'sfwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('103', '富吉速运', 'fujisuyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('104', '景光物流', 'jgwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('105', '速通物流', 'sut56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('106', '龙邦速递', 'longbanwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('107', '新元国际', 'xynyc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('108', '盛辉物流', 'shenghuiwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('109', '国通快递', 'guotongkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('110', 'Superb Express', 'superb', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('111', '金岸物流', 'jinan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('112', '澳洲飞跃物流', 'rlgaus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('113', '荷兰邮政-中文(PostNL international registered mail)', 'postnlcn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('114', '方舟速递', 'arkexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('115', '安迅物流', 'anxl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('116', '富腾达国际货运', 'ftd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('117', 'OCS', 'ocs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('118', 'SYNSHIP快递', 'synship', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('119', '澳天速运', 'aotsd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('120', '日本郵便', 'japanpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('121', '比利时（Bpost）', 'bpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('122', '微特派', 'weitepai', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('123', 'DPD UK', 'dpduk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('124', '泰进物流', 'taijin', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('125', 'DPD Germany', 'dpdgermany', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('126', '递四方美国', 'disifangus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('127', '耀飞同城快递', 'yaofeikuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('128', '天马迅达', 'tianma', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('129', '家家通快递', 'newsway', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('130', '疯狂快递', 'crazyexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('131', '海欣斯快递', 'highsince', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('132', '大田物流', 'datianwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('133', '中速快递', 'zhongsukuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('134', '货拉拉物流', 'huolalawuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('135', '商桥物流', 'shangqiao56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('136', '源安达', 'yuananda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('137', '芝麻开门', 'zhimakaimen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('138', '安信达', 'anxindakuaixi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('139', '斑马物流', 'banma', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('140', '澳大利亚(Australia Post)', 'auspost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('141', '韵达国际', 'udalogistic', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('142', '鼎润物流', 'la911', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('143', '龙邦物流', 'lbex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('144', '安达速递', 'adapost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('145', '中铁飞豹', 'zhongtiewuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('146', '转运四方', 'zhuanyunsifang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('147', 'AAE-中国件', 'aae', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('148', '极地快递', 'polarexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('149', 'Aramex', 'aramex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('150', '铁中快运', 'tzky', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('151', '中邮速递', 'wondersyd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('152', '泛捷国际速递', 'epanex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('153', '迅达速递', 'xdexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('154', '晟邦物流', 'nanjingshengbang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('155', 'Austa国际速递', 'austa', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('156', '新杰物流', 'sunjex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('157', '广东邮政', 'guangdongyouzhengwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('158', '中华邮政', 'postserv', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('159', 'Purolator', 'purolator', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('160', '加拿大龙行速运', 'longcps', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('161', '招金精炼', 'zhaojin', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('162', '黄马甲', 'huangmajia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('163', '达发物流', 'dfwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('164', '行云物流', 'xyb2b', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('165', '成都立即送', 'lijisong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('166', '转运中国', 'uszcn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('167', '澳德物流', 'auod', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('168', 'LUCFLOW EXPRESS', 'longfx', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('169', '三真驿道', 'zlink', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('170', '荷兰邮政(PostNL international registered mail)', 'postnl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('171', '快捷速递', 'kuaijiesudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('172', 'EMS-国际件-英文', 'emsinten', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('173', '科捷物流', 'kejie', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('174', 'COE', 'coe', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('175', '全联速运', 'guexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('176', '鸿泰物流', 'hnht56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('177', '联合速运', 'unitedex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('178', '程光快递', 'flyway', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('179', '快捷快物流', 'gdkjk56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('180', '百腾物流', 'baitengwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('181', '贰仟家物流', 'erqianjia56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('182', 'TransRush', 'transrush', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('183', '韩国邮政', 'koreapostcn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('184', '速递中国', 'sendtochina', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('185', '无忧物流', 'aliexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('186', '新西兰（New Zealand Post）', 'newzealand', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('187', '优优速递', 'youyou', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('188', '速达通', 'sdto', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('189', '中集冷云', 'cccc58', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('190', '三象速递', 'sxexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('191', '万家物流', 'wanjiawuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('192', 'MoreLink', 'morelink56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('193', '三志物流', 'sanzhi56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('194', '联合速递', 'lhexpressus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('195', '佳成快递 ', 'jiacheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('196', '亚洲顺物流', 'yzswuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('197', '百福东方', 'baifudongfang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('198', '时达通', 'jssdt56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('199', '小飞侠速递', 'cyxfx', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('200', '德国优拜物流', 'ubuy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('201', '速派快递', 'fastgoexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('202', '大韩通运', 'cjkoreaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('203', '顺丰-繁体', 'shunfenghk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('204', '秦远物流', 'qinyuan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('205', '合众速递(UCS）', 'ucs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('206', 'beiou express', 'beiou', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('207', '顺捷美中速递', 'passerbyaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('208', 'EMS物流', 'emswuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('209', 'TST速运通', 'tstexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('210', '聚盟共建', 'jumstc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('211', '佳怡物流', 'jiayiwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('212', '黑猫宅急便', 'tcat', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('213', '宅急便', 'zhaijibian', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('214', 'TNT Italy', 'tntitaly', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('215', '中环快递', 'zhonghuan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('216', '法国大包、EMS-英文(Chronopost France)', 'chronopostfren', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('217', '签收快递', 'signedexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('218', '星云速递', 'nebuex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('219', '直德邮', 'zdepost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('220', '长江国际速递', 'changjiang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('221', '美通', 'valueway', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('222', '中国邮政（CHINA POST）', 'chinapost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('223', '黑猫同城送', 'ynztsy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('224', '安鲜达', 'exfresh', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('225', '恒路物流', 'hengluwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('226', 'E2G速递', 'express2global', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('227', '明达国际速递', 'tmwexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('228', '安捷物流', 'anjie88', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('229', '中邮物流', 'zhongyouwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('230', '锦程快递', 'hrex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('231', '德国(Deutsche Post)', 'deutschepost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('232', '领送送', 'lingsong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('233', '加拿大(Canada Post)', 'canpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('234', 'airpak expresss', 'airpak', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('235', 'FedEx-美国件', 'fedexus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('236', 'Newgistics', 'newgistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('237', '宇佳物流', 'yujiawl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('238', 'DPEX', 'dpex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('239', '荷兰邮政-中国件', 'postnlchina', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('240', '城晓国际快递', 'ckeex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('241', '乌克兰邮政包裹', 'ukrpostcn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('242', '汇达物流', 'hdcexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('243', '洋包裹', 'yangbaoguo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('244', '皮牙子快递', 'bazirim', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('245', 'CNE', 'cnexps', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('246', '能达速递', 'ganzhongnengda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('247', '品骏快递', 'pjbest', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('248', '一正达速运', 'yizhengdasuyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('249', '永昌物流', 'yongchangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('250', '百事亨通', 'bsht', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('251', 'YDH', 'ydhex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('252', '御风速运', 'yufeng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('253', 'TRAKPAK', 'trakpak', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('254', '嘉贤物流', 'jiaxianwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('255', '邮邦国际', 'youban', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('256', '顺达快递', 'sundarexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('257', '佳吉快运', 'jiajiwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('258', '帮帮发', 'bangbangpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('259', '明通国际快递', 'tnjex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('260', '万象物流', 'wanxiangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('261', '商壹国际物流', 'com1express', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('262', '飞洋快递', 'shipgce', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('263', 'DHL-德国件（DHL Deutschland）', 'dhlde', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('264', 'EFS Post（平安快递）', 'efs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('265', '中外运', 'esinotrans', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('266', 'Hermes', 'hermes', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('267', '如家国际快递', 'homecourier', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('268', 'OC-Post', 'ocpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('269', '全速物流', 'quansu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('270', '法国小包（colissimo）', 'colissimo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('271', '浩博物流', 'njhaobo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('272', '原飞航', 'yuanfeihangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('273', '锋鸟物流', 'beebird', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('274', '飞远配送', 'feiyuanvipshop', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('275', '易境达国际物流', 'uscbexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('276', '友家速递', 'youjia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('277', '运通中港', 'yuntongkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('278', '亚风速递', 'yafengsudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('279', '海带宝', 'haidaibao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('280', '佰麒快递', 'beckygo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('281', '汇通天下物流', 'httx56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('282', '春风物流', 'spring56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('283', '英国大包、EMS（Parcel Force）', 'parcelforce', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('284', '中途速递', 'ztcce', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('285', '北京EMS', 'bjemstckj', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('286', '一速递', 'oneexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('287', 'USPSCN', 'uspscn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('288', '泛远国际物流', 'farlogistis', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('289', '昌宇国际', 'changwooair', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('290', '快达物流', 'kuaidawuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('291', '西班牙(Correos de Espa?a)', 'correosdees', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('292', '全峰快递', 'quanfengkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('293', '联运通物流', 'szuem', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('294', '迅速快递', 'xunsuexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('295', '优速通达', 'yousutongda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('296', '递五方云仓', 'di5pll', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('297', 'GTS快递', 'gts', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('298', '中国香港(HongKong Post)', 'hkpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('299', 'EASY EXPRESS', 'easyexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('300', 'TNT Australia', 'tntau', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('301', '一智通', '1ziton', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('302', '宜送物流', 'yiex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('303', '联合快递', 'gslhkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('304', '丰通快运', 'ftky365', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('305', '易达通快递', 'qexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('306', '华欣物流', 'chinastarlogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('307', '远盾物流', 'yuandun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('308', '集先锋快递', 'jxfex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('309', '武汉优进汇', 'yjhgo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('310', '法国(La Poste)', 'csuivi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('311', '环球速运', 'huanqiu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('312', '远成快运', 'ycgky', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('313', '明大快递', 'adaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('314', '上海缤纷物流', 'bflg', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('315', '韩国（Korea Post）', 'koreapost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('316', '西游寄', 'xiyoug', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('317', '传喜物流', 'chuanxiwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('318', '远成物流', 'yuanchengwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('319', '全球快运', 'abcglobal', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('320', 'E速达', 'exsuda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('321', 'wedepot物流', 'wedepot', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('322', '魔速达', 'mosuda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('323', 'dhl小包', 'dhlecommerce', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('324', 'EMS-英文', 'emsen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('325', '尚橙物流', 'shangcheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('326', '佳辰国际速递', 'jiachenexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('327', '天翼快递', 'tykd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('328', '万家康物流', 'wjkwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('329', '华通快运', 'htongexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('330', '联邦快递-英文', 'lianbangkuaidien', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('331', '世华通物流', 'szshihuatong56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('332', '奔腾物流', 'benteng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('333', '汇捷物流', 'hjwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('334', '瑞典（Sweden Post）', 'ruidianyouzheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('335', '承诺达', 'ytchengnuoda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('336', '泰国138国际物流', 'sd138', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('337', 'DPD Poland', 'dpdpoland', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('338', '聚鼎物流', 'juding', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('339', '中邮电商', 'chinapostcb', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('340', '上海航瑞货运', 'hangrui', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('341', '小熊物流', 'littlebearbear', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('342', '中国香港环球快运', 'huanqiuabc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('343', '上大物流', 'shangda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('344', '山西建华', 'shanxijianhua', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('345', '黑猫速运', 'heimao56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('346', '海派国际速递', 'hpexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('347', '顺捷达', 'shunjieda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('348', '增益速递', 'zengyisudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('349', '汇峰物流', 'huif56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('350', '人人转运', 'renrenex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('351', '湘达物流', 'xiangdawuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('352', '海联快递', 'hltop', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('353', '爱拜物流', 'ibuy8', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('354', 'Toll Priority(Toll Online)', 'tollpriority', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('355', '成都东骏物流', 'dongjun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('356', '颿达国际快递', 'fardarww', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('357', '意大利(Poste Italiane)', 'italiane', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('358', '全时速运', 'runhengfeng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('359', '泰国邮政（Thailand Thai Post）', 'thailand', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('360', '运通中港快递', 'ytkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('361', '八达通', 'bdatong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('362', '天天快物流', 'guoeryue', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('363', '全川物流', 'quanchuan56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('364', '中外运空运', 'sinoairinex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('365', '永邦国际物流', 'yongbangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('366', '欧亚专线', 'euasia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('367', '海红网送', 'haihongwangsong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('368', '创运物流', 'zjcy56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('369', '速舟物流', 'cnspeedster', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('370', '奉天物流', 'fengtianexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('371', '新西兰中通', 'nzzto', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('372', '申通新西兰', 'stonewzealand', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('373', '飞豹快递', 'feibaokuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('374', 'Gati-中文', 'gaticn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('375', '黑豹物流', 'heibaowuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('376', '银雁专送', 'cfss', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('377', '全网物流', 'gdqwwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('378', '久久物流', 'jiujiuwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('379', '洋口岸', 'ykouan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('380', '法国大包、EMS-法文（Chronopost France）', 'chronopostfra', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('381', '7号速递', 'express7th', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('382', '日日顺智慧物联', 'gooday365', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('383', '瀚朝物流', 'hac56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('384', '美国快递', 'meiguokuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('385', '美国云达', 'yundaexus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('386', '安世通快递', 'astexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('387', '广州安能聚创物流', 'gzanjcwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('388', '景顺物流', 'jingshun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('389', '赛澳递for买卖宝', 'saiaodimmb', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('390', '一起送', 'yiqisong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('391', '苏通快运', 'zjstky', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('392', '澳州顺风快递', 'emms', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('393', '红马甲物流', 'sxhongmajia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('394', '中外运速递', 'zhongwaiyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('395', 'CCES/国通快递', 'cces', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('396', '飞康达', 'feikangda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('397', '重庆星程快递', 'cqxingcheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('398', 'ECMS Express', 'ecmsglobal', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('399', '货运皇', 'kingfreight', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('400', '51跨境通', 'wykjt', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('401', '长吉物流', 'cjqy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('402', '海星桥快递', 'haixingqiao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('403', '中国香港(HongKong Post)英文', 'hkposten', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('404', '好来运', 'hlyex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('405', '秦邦快运', 'qbexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('406', '顺丰冷链', 'shunfenglengyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('407', '顺捷丰达', 'shunjiefengda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('408', '澳捷物流', 'ajlogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('409', '百世国际', 'baishiguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('410', 'Canpar', 'canpar', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('411', '诚和通', 'cht361', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('412', 'DHL-波兰（DHL Poland）', 'dhlpoland', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('413', 'Toll', 'dpexen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('414', '凡宇快递', 'fanyukuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('415', '飞力士物流', 'flysman', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('416', '立白宝凯物流', 'lbbk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('417', '南方传媒物流', 'ndwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('418', '荷兰包裹(PostNL International Parcels)', 'postnlpacle', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('419', '优联吉运', 'uluckex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('420', '越南小包(Vietnam Posts)', 'vietnam', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('421', '玥玛速运', 'yue777', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('422', '群航国际物流', 'cloudlogistics365', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('423', '邦通国际', 'comexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('424', '东瀚物流', 'donghanwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('425', '快卡', 'kuaika', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('426', '龙枫国际快递', 'lfexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('427', 'OnTrac', 'ontrac', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('428', '新速航', 'sunspeedy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('429', '泰实货运', 'tjkjwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('430', '宇捷通', 'yujtong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('431', 'DHL Benelux', 'dhlbenelux', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('432', '腾达速递', 'nntengda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('433', '葡萄牙（Portugal CTT）', 'portugalctt', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('434', '全速通', 'quansutong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('435', '瑞士(Swiss Post)', 'swisspost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('436', '万庚国际速递', 'vangenexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('437', '西安喜来快递', 'xilaikd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('438', '捷祥物流', 'cdjx56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('439', '新时速物流', 'csxss', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('440', '陪行物流', 'peixingwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('441', '智通物流', 'ztong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('442', 'CDEK', 'cdek', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('443', '城际快递', 'chengji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('444', '6LS EXPRESS', 'lsexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('445', '美西快递', 'meixi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('446', '丹麦(Post Denmark)', 'postdanmarken', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('447', '丰程物流', 'sccod', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('448', 'GPI', 'tcxbthai', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('449', '中融泰隆', 'zrtl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('450', '中翼国际物流', 'chnexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('451', '创一快递', 'chuangyi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('452', '中国翼', 'cnws', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('453', '全日通', 'quanritongkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('454', '赛澳递', 'saiaodi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('455', '天天欧洲物流', 'ttkeurope', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('456', '越南EMS(VNPost Express)', 'vnpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('457', '青岛安捷快递', 'anjiekuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('458', '澳世速递', 'ausexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('459', '贝业物流', 'boyol', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('460', '次晨达物流', 'ccd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('461', '拉脱维亚(Latvijas Pasts)', 'latvia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('462', '恒通快递', 'lqht', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('463', '鲁通快运', 'lutong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('464', '英国邮政大包EMS', 'parcelforcecn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('465', '美国申通', 'stoexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('466', '中宏物流', 'zhonghongwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('467', '泰捷达国际物流', 'ztjieda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('468', '中远快运', 'zy100', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('469', '泰国中通CTO', 'ctoexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('470', '大洋物流', 'dayangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('471', '高捷快运', 'goldjet', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('472', 'GSM', 'gsm', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('473', '云邮跨境快递', 'hkems', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('474', '皇家物流', 'pfcexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('475', '菲律宾（Philippine Postal）', 'phlpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('476', 'rpx', 'rpx', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('477', 'TNT Post', 'tntpostcn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('478', '中技物流', 'zhongjiwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('479', '全程快递', 'agopost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('480', '河北橙配', 'chengpei', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('481', '深圳德创物流', 'dechuangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('482', '安的快递', 'gda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('483', '华美快递', 'hmus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('484', '环东物流', 'huandonglg', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('485', '佳吉快递', 'jiajikuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('486', 'amazon-国际订单', 'amusorder', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('487', '比利时(Belgium Post)', 'belgiumpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('488', '比利时国际(Bpost international)', 'bpostinter', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('489', '澳通华人物流', 'cllexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('490', '丹递56', 'dande56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('491', 'FedRoad 联邦转运', 'fedroad', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('492', '全速快递', 'fsexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('493', '印度(India Post)', 'india', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('494', '骏丰国际速递', 'junfengguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('495', '吉捷国际速递', 'luckyfastex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('496', '民航快递', 'minghangkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('497', '林道国际快递', 'shlindao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('498', 'UEQ快递', 'ueq', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('499', '元智捷诚', 'yuanzhijiecheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('500', '远为快递', 'ywexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('501', 'A2U速递', 'a2u', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('502', '捷记方舟', 'ajexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('503', '阿森迪亚', 'asendia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('504', '蜜蜂速递', 'bee001', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('505', 'BHT', 'bht', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('506', '鑫宸物流', 'cdxinchen56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('507', '德中快递', 'decnlh', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('508', '恒瑞物流', 'hengrui56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('509', '马来西亚小包（Malaysia Post(Registered)）', 'malaysiapost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('510', '民邦速递', 'minbangsudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('511', '龙行天下', 'pmt0704be', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('512', '俄罗斯邮政(Russian Post)', 'pochta', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('513', '百米快运', '100mexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('514', '心怡物流', 'alog', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('515', 'CHS中环国际快递', 'chszhonghuanguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('516', 'globaltracktrace', 'globaltracktrace', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('517', '国顺达物流', 'guoshunda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('518', '华翰物流', 'huahanwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('519', '上海昊宏国际货物', 'hyk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('520', '嘉诚速达', 'jcsuda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('521', '急先达', 'jixianda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('522', '一号线', 'lineone', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('523', '美泰物流', 'meitai', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('524', '配思货运', 'peisihuoyunkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('525', '伍圆速递', 'wuyuansudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('526', '翔腾物流', 'xiangteng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('527', '亚马逊中国', 'yamaxunwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('528', '云达通', 'ydglobe', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('529', '中天万运', 'zhongtianwanyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('530', 'ADP国际快递', 'adp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('531', '亚马逊中国订单', 'amazoncnorder', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('532', '中联速递', 'auvanda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('533', '百世云配', 'baishiyp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('534', '加拿大邮政', 'canpostfr', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('535', '哥斯达黎加(Correos de Costa Rica)', 'correos', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('536', '大道物流', 'dadaoex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('537', 'e直运', 'edtexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('538', '凤凰快递', 'fenghuangkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('539', '汇强快递', 'huiqiangkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('540', 'LaserShip', 'lasership', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('541', '林道国际快递-英文', 'ldxpres', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('542', '中国澳门(Macau Post)', 'macao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('543', '银河物流', 'milkyway', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('544', '猛犸速递', 'mmlogi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('545', 'PCA Express', 'pcaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('546', '雪域快递', 'qhxykd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('547', '顺丰-荷兰', 'shunfengnl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('548', '新加坡小包(Singapore Post)', 'singpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('549', '中外运速递-中文', 'sinoex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('550', '淘布斯国际物流', 'taoplus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('551', '乌兹别克斯坦(Post of Uzbekistan)', 'uzbekistan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('552', '豌豆物流', 'wandougongzhu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('553', '祥龙运通物流', 'xianglongyuntong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('554', '雅澳物流', 'yourscm', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('555', 'ABF', 'abf', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('556', 'amazon-国内订单', 'amcnorder', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('557', '堡昕德速递', 'bosind', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('558', '东方汇', 'est365', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('559', '豪翔物流', 'haoxiangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('560', '猴急送', 'hjs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('561', '红远物流', 'hongywl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('562', '以色列(Israel Post)', 'israelpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('563', '佳家通货运', 'jiajiatong56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('564', '乐天速递', 'ltexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('565', 'PostNord(Posten AB)', 'postenab', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('566', '全信通快递', 'quanxintong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('567', '中加国际快递', 'scic', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('568', '圣安物流', 'shenganwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('569', '穗佳物流', 'suijiawuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('570', '老扬州物流', 'tjlyz56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('571', 'WTD海外通', 'wtdex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('572', '新宁物流', 'xinning', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('573', '一辉物流', 'yatfai', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('574', '韵丰物流', 'yunfeng56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('575', 'AOL澳通速递', 'aolau', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('576', 'Asendia USA', 'asendiausa', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('577', '邦送物流', 'bangsongwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('578', '百通物流', 'buytong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('579', '易达快运', 'edaeuexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('580', '飞邦快递', 'fbkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('581', '高铁快运', 'gaotieex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('582', '汉邦国际速递', 'handboy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('583', 'JDIEX', 'jdiex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('584', '捷安达', 'jieanda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('585', '锦程物流', 'jinchengwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('586', '柬埔寨中通', 'khzto', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('587', 'LWE', 'lwe', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('588', '一号仓', 'onehcang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('589', '顺通快递', 'stkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('590', '宇航通物流', 'yhtlogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('591', 'YODEL', 'yodel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('592', '优胜国际速递', 'yoseus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('593', '安家同城快运', 'anjiatongcheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('594', '爱尔兰(An Post)', 'anposten', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('595', 'BDC快递', 'bdcgcc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('596', '青云物流', 'bjqywl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('597', '同舟行物流', 'chinatzx', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('598', '中骅物流', 'chunghwa56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('599', 'City-Link', 'citylink', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('600', '华中快递', 'cpsair', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('601', '达方物流', 'dfpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('602', '英脉物流', 'gml', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('603', 'GTT EXPRESS快递', 'gttexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('604', '顺时达物流', 'hnssd56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('605', '克罗地亚（Hrvatska Posta）', 'hrvatska', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('606', '汇霖大货网', 'huilin56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('607', '金大物流', 'jindawuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('608', '考拉国际速递', 'kaolaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('609', '韩国邮政韩文', 'koreapostkr', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('610', 'Landmark Global', 'landmarkglobal', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('611', '云豹国际货运', 'leopard', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('612', '路遥物流', 'luyao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('613', '木春货运', 'mchy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('614', '明亮物流', 'mingliangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('615', '速风快递', 'sufengkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('616', '深圳DPEX', 'szdpex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('617', '一邦速递', 'yibangwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('618', '西安运逸快递', 'yyexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('619', '三三国际物流', 'zenzen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('620', '转瞬达集运', 'zsda56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('621', '安达信', 'advancing', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('622', 'apgecommerce', 'apgecommerce', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('623', '奥地利(Austrian Post)', 'austria', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('624', '彪记快递', 'biaojikuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('625', 'BRT', 'brt', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('626', '城市映急', 'city56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('627', 'CNAIR', 'cnair', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('628', 'CNPEX中邮快递', 'cnpex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('629', '龙象国际物流', 'edragon', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('630', '联众国际', 'epspost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('631', '共速达', 'gongsuda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('632', '光线速递', 'gxwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('633', '海外环球', 'haiwaihuanqiu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('634', '皇家云仓', 'hotwms', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('635', '匈牙利（Magyar Posta）', 'hungary', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('636', '恒宇运通', 'hyytes', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('637', 'jcex', 'jcex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('638', '加州猫速递', 'jiazhoumao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('639', '极光转运', 'jiguang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('640', '嘉里大荣物流', 'kerrytj', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('641', '马来西亚大包、EMS（Malaysia Post(parcel,EMS)）', 'malaysiaems', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('642', '诺尔国际物流', 'nuoer', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('643', '北极星快运', 'polarisexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('644', '全际通', 'quanjitong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('645', '卢旺达(Rwanda i-posita)', 'rwanda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('646', '速呈', 'sczpds', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('647', '十方通物流', 'sfift', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('648', '闪货极速达', 'shanhuodidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('649', '西翼物流', 'westwing', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('650', '五六快运', 'wuliuky', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('651', '蓝天物流', 'xflt56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('652', '越丰物流', 'yuefengwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('653', 'ZTE中兴物流', 'zteexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('654', '增速跨境 ', 'zyzoom', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('655', '德国雄鹰速递', 'adlerlogi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('656', '阿富汗(Afghan Post)', 'afghan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('657', '德方物流', 'ahdf', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('658', '伯利兹(Belize Postal)', 'belize', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('659', '博茨瓦纳', 'botspost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('660', '保加利亚（Bulgarian Posts）', 'bulgarian', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('661', '云南诚中物流', 'czwlyn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('662', '达速物流', 'dasu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('663', 'DCS', 'dcs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('664', '递达速运', 'didasuyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('665', 'E跨通', 'ecallturn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('666', '艾菲尔国际速递', 'eiffel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('667', 'Estes', 'estes', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('668', '加拿大联通快运', 'fastontime', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('669', 'Fastway Ireland', 'fastway', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('670', '飞快达', 'feikuaida', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('671', '芬兰(Itella Posti Oy)', 'finland', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('672', 'FOX国际快递', 'fox', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('673', '广东诚通物流', 'gdct56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('674', '贵州星程快递', 'gzxingcheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('675', '开心快递', 'happylink', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('676', '飞豹速递', 'hkeex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('677', '飞鹰物流', 'hnfy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('678', '中强物流', 'hnzqwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('679', '宏捷国际物流', 'hongjie', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('680', '冰岛(Iceland Post)', 'iceland', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('681', 'logen路坚', 'ilogen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('682', 'Italy SDA', 'italysad', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('683', '加佳物流', 'jiajiawl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('684', 'KCS', 'kcs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('685', '快服务', 'kfwnet', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('686', '跨境直邮通', 'kjde', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('687', '淘韩国际快递', 'krtao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('688', '楽道物流', 'ledaowuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('689', '新易泰', 'lnet', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('690', '陆本速递 LUBEN EXPRESS', 'luben', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('691', '美达快递', 'meidaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('692', '美龙快递', 'mjexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('693', 'Nova Poshta', 'novaposhta', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('694', '偌亚奥国际快递', 'nuoyaao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('695', '全球速递', 'pdstow', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('696', '秦岭智能速运', 'qinling', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('697', 'Quantium', 'quantium', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('698', '全通快运', 'quantwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('699', '叙利亚(Syrian Post)', 'republic', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('700', '睿和泰速运', 'rhtexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('701', '上海无疆for买卖宝', 'shanghaiwujiangmmb', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('702', '杰响物流', 'shbwch', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('703', '南非（South African Post Office）', 'southafrican', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('704', '行必达', 'speeda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('705', '新加坡EMS、大包(Singapore Speedpost)', 'speedpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('706', '智德物流', 'stzd56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('707', '苏丹（Sudapost）', 'sudapost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('708', '速品快递', 'supinexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('709', '中运全速', 'topspeedex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('710', '优海国际速递', 'uhi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('711', '速翼快递', 'usasueexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('712', '华夏国际速递', 'uschuaxia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('713', '鹰运国际速递', 'vipexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('714', '万邑通', 'winit', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('715', '微转运', 'wzhaunyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('716', '西邮寄', 'xipost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('717', '易达丰国际速递', 'ydfexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('718', '易达通', 'yidatong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('719', '银捷速递', 'yinjiesudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('720', '壹品速递', 'ypsd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('721', '运通速运', 'yuntong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('722', '志腾物流', 'zhitengwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('723', '振捷国际货运', 'zjgj56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('724', '中粮鲜到家物流', 'zlxdjwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('725', '准实快运', 'zsky123', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('726', '360 Lion Express', '360lion', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('727', 'AFL', 'afl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('728', '加拿大民航快递', 'airgtc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('729', '新干线快递', 'anlexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('730', '阿鲁巴[荷兰]（Post Aruba）', 'aruba', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('731', 'AUV国际快递', 'auvexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('732', '阿塞拜疆EMS(EMS AzerExpressPost)', 'azerbaijan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('733', '宝通快递', 'baotongkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('734', '巴巴多斯(Barbados Post)', 'barbados', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('735', 'BCWELT', 'bcwelt', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('736', 'BlueDart', 'bluedart', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('737', '玻利维亚', 'bolivia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('738', 'BorderGuru', 'borderguru', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('739', '速方(Sufast)', 'bphchina', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('740', '百千诚物流', 'bqcwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('741', '捷克（?eská po?ta）', 'ceskaposta', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('742', '城通物流', 'chengtong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('743', 'SQK国际速递', 'chinasqk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('744', '嘉荣物流', 'chllog', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('745', 'Chronopost Portugal', 'chronopostport', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('746', 'CE易欧通国际速递', 'cloudexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('747', 'CL日中速运', 'clsp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('748', 'C&C国际速递', 'cncexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('749', 'CNUP 中联邮', 'cnup', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('750', '哥伦比亚(4-72 La Red Postal de Colombia)', 'colombia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('751', '叮当同城', 'ddotbase', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('752', 'Deltec Courier', 'deltec', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('753', 'DHL HK', 'dhlhk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('754', 'DHL-荷兰（DHL Netherlands）', 'dhlnetherlands', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('755', 'Direct Link', 'directlink', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('756', 'DTDC India', 'dtdcindia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('757', 'EC-Firstclass', 'ecfirstclass', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('758', '希腊EMS（ELTA Courier）', 'eltahell', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('759', '波兰小包(Poczta Polska)', 'emonitoring', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('760', '高考通知书', 'emsluqu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('761', '俄顺达', 'eshunda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('762', 'Estafeta', 'estafeta', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('763', '易达国际速递', 'eta100', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('764', 'ETEEN专线', 'eteenlog', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('765', 'E通速递', 'etong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('766', 'EU-EXPRESS', 'euexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('767', '易邮国际', 'euguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('768', '易优包裹', 'eupackage', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('769', 'europeanecom', 'europeanecom', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('770', '优莎速运', 'eusacn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('771', '飛斯特運通', 'exbtr', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('772', '可可树美中速运', 'excocotree', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('773', '易转运', 'ezhuanyuan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('774', 'FedEx-英国件（FedEx UK)', 'fedexuk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('775', 'FedEx-英国件', 'fedexukcn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('776', '四方格', 'fourpxus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('777', '环创物流', 'ghl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('778', '直布罗陀[英国]( Royal Gibraltar Post)', 'gibraltar', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('779', '冠捷物流 ', 'gjwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('780', '冠庭国际物流', 'guanting', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('781', '国送快运', 'guosong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('782', '海盟速递', 'haimengsudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('783', '海中转运', 'haizhongzhuanyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('784', '航宇快递', 'hangyu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('785', '好又快物流', 'haoyoukuai', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('786', '亚美尼亚(Haypost-Armenian Postal)', 'haypost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('787', '环国运物流', 'hgy56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('788', '共联配', 'hlpgyl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('789', '宏品物流', 'hongpinwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('790', '卓烨快递', 'hrbzykd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('791', '华企快运', 'huaqikuaiyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('792', '兰州伙伴物流', 'huoban', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('793', '鸿远物流', 'hyeship', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('794', '泛太优达', 'iex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('795', '无限速递', 'igcaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('796', '印度尼西亚EMS(Pos Indonesia-EMS)', 'indonesia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('797', '捷运达快递', 'interjz', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('798', '伊朗（Iran Post）', 'iran', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('799', '驿扬国际速运', 'iyoungspeed', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('800', '骏达快递', 'jdexpressusa', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('801', '捷邦物流', 'jieborne', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('802', '晋越快递', 'jinyuekuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('803', '佳捷翔物流', 'jjx888', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('804', 'J&T Express 马来西亚', 'jtexpressmy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('805', '快速递', 'ksudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('806', '快8速运', 'kuai8', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('807', '快淘快递', 'kuaitao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('808', '四川快优达速递', 'kuaiyouda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('809', '凯信达', 'kxda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('810', '跨跃国际', 'kyue', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('811', '華信物流WTO', 'logistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('812', '卢森堡(Luxembourg Post)', 'luxembourg', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('813', '今枫国际快运', 'mapleexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('814', '尼泊尔（Nepal Postal Services）', 'nepalpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('815', '牛仔速运', 'niuzaiexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('816', 'NLE', 'nle', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('817', '亚欧专线', 'nlebv', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('818', '华赫物流', 'nmhuahe', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('819', '爱沙尼亚(Eesti Post)', 'omniva', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('820', 'OPEK', 'opek', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('821', '巴基斯坦(Pakistan Post)', 'pakistan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('822', '先锋国际快递', 'pioneer', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('823', '急顺通', 'pzhjst', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('824', 'ANTS EXPRESS', 'qdants', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('825', '7E速递', 'qesd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('826', '千里速递', 'qianli', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('827', '卡塔尔（Qatar Post）', 'qpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('828', '全之鑫物流', 'qzx56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('829', '日日顺快线', 'rrskx', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('830', '日日通国际', 'rrthk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('831', 'S2C', 's2c', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('832', '三盛快递', 'sanshengco', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('833', '塞尔维亚(PE Post of Serbia)', 'serbia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('834', '上海快通', 'shanghaikuaitong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('835', '阳光快递', 'shiningexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('836', '顺邦国际物流', 'shunbang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('837', '顺士达速运', 'shunshid', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('838', 'skynet', 'skynet', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('839', '嗖一下同城快递', 'sofast56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('840', '申必达', 'speedoex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('841', '速呈宅配', 'sucheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('842', '速配欧翼', 'superoz', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('843', '瑞士邮政', 'swisspostcn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('844', '深圳邮政', 'szyouzheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('845', '天翔快递', 'tianxiang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('846', 'TNT UK', 'tntuk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('847', '通达兴物流', 'tongdaxing', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('848', '通和天下', 'tonghetianxia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('849', 'UPS Mail Innovations', 'upsmailinno', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('850', '凡仕特物流', 'wlfast', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('851', '鑫通宝物流', 'xtb', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('852', '西游寄速递', 'xyjexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('853', 'YCG物流', 'ycgglobal', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('854', '亿领速运', 'yilingsuyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('855', '英超物流', 'yingchao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('856', '亿顺航', 'yishunhang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('857', '易邮速运', 'yiyou', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('858', '邮来速递', 'youlai', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('859', '远航国际快运', 'yuanhhk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('860', '众辉达物流', 'zhdwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('861', '众川国际', 'zhongchuan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('862', '众派速递', 'zhpex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('863', 'AAA Cooper Transportation', 'aaacooper', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('864', 'ABX Express', 'abxexpress_my', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('865', 'aCommerce', 'acommerce', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('866', 'ACS Courier', 'acscourier', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('867', '安达易国际速递', 'adiexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('868', 'ADSone', 'adsone', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('869', 'Agility Logistics', 'agility', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('870', '卡邦配送', 'ahkbps', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('871', '奥兰群岛', 'aland', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('872', '阿尔巴尼亚(Posta shqipatre)', 'albania', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('873', 'AlfaTrex', 'alfatrex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('874', 'Algeria', 'algeria', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('875', 'ALLIED', 'alliedexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('876', 'Swiship UK', 'amazon_fba_swiship', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('877', 'Anjun Logistics', 'anjun_logistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('878', 'Anteraja', 'anteraja', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('879', '澳速物流', 'aosu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('880', 'APC Postal Logistics', 'apc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('881', '美国汉邦快递', 'aplus100', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('882', 'ARC', 'arc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('883', '艾瑞斯远', 'ariesfar', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('884', 'Asendia HK', 'asendiahk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('885', '澳达国际物流', 'auadexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('886', '澳货通', 'auex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('887', '澳邦国际物流', 'ausbondexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('888', '澳新物流', 'axexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('889', '巴林(Bahrain Post)', 'bahrain', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('890', '孟加拉国(EMS)', 'bangladesh', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('891', '报通快递', 'baoxianda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('892', 'BEE express', 'beeexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('893', '白俄罗斯(Belpochta)', 'belpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('894', '贝宁', 'benin', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('895', '笨鸟国际', 'benniao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('896', '飛斯特', 'bester', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('897', '不丹邮政 Bhutan Post', 'bhutan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('898', '鑫锐达', 'bjxsrd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('899', '远通盛源', 'bjytsywl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('900', '佰乐捷通', 'bljt56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('901', '标杆物流', 'bmlchina', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('902', '波黑(JP BH Posta)', 'bohei', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('903', 'BOXC', 'boxc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('904', '巴西(Brazil Post/Correios)', 'brazilposten', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('905', '宏桥国际物流', 'briems', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('906', '文莱(Brunei Postal)', 'brunei', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('907', 'Buylogic', 'buylogic', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('908', '展勤快递', 'byht', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('909', '新喀里多尼亚[法国](New Caledonia)', 'caledonia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('910', '柬埔寨(Cambodia Post)', 'cambodia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('911', '到了港', 'camekong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('912', '喀麦隆(CAMPOST)', 'cameroon', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('913', 'Campbell’s Express', 'campbellsexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('914', '能装能送', 'canhold', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('915', '卢森堡航空', 'cargolux', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('916', 'CBL Logistica', 'cbl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('917', 'CBL Logistics', 'cbl_logistica', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('918', '广州信邦', 'cbllogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('919', '钏博物流', 'cbo56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('920', '中迅三方', 'cd3fwl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('921', 'CEVA Logistics', 'ceva', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('922', 'CEVA Logistic', 'cevalogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('923', '城铁速递', 'cex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('924', '成达国际速递', 'chengda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('925', '智利(Correos Chile)', 'chile', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('926', '荣通国际', 'chinaqingguan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('927', '中国香港骏辉物流', 'chunfai', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('928', 'citysprint', 'citysprint', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('929', '中澳速递', 'cnausu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('930', '中欧物流', 'cneulogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('931', 'Collect+', 'collectplus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('932', 'Corporate couriers logistics', 'corporatecouriers', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('933', '莫桑比克（Correios de Moçambique）', 'correios', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('934', '乌拉圭（Correo Uruguayo）', 'correo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('935', '阿根廷(Correo Argentina)', 'correoargentino', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('936', 'Correos Express', 'correosexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('937', 'CourierPost', 'courierpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('938', 'Couriers Please', 'couriersplease', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('939', '环旅快运', 'crossbox', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('940', '塞浦路斯(Cyprus Post)', 'cypruspost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('941', 'DACHSER', 'dachser', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('942', 'Delhivery', 'delhivery', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('943', '澳行快递', 'desworks', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('944', '东风全球速递', 'dfglobalex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('945', 'dhluk', 'dhluk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('946', '云南滇驿物流', 'dianyi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('947', '叮咚澳洲转运', 'dindon', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('948', '叮咚快递', 'dingdong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('949', '递四方澳洲', 'disifangau', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('950', '吉布提', 'djibouti', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('951', '天翔东捷运', 'djy56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('952', '东红物流', 'donghong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('953', 'Dotzot', 'dotzot', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('954', 'DPD Ireland', 'dpd_ireland', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('955', 'DPE Express', 'dpe_express', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('956', '老司机国际快递', 'driverfastgo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('957', 'DSV', 'dsv', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('958', '德淘邦', 'dt8ang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('959', 'DTD', 'dtd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('960', '多道供应链', 'duodao56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('961', '大亿快递', 'dyexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('962', '东方航空物流', 'ealceair', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('963', 'Echo', 'echo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('964', 'Ecom Express', 'ecomexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('965', '东西E全运', 'ecotransite', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('966', '厄瓜多尔(Correos del Ecuador)', 'ecuador', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('967', 'EFSPOST', 'efspost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('968', '埃及（Egypt Post）', 'egypt', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('969', 'Ekart', 'ekart', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('970', '易联通达', 'el56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('971', '希腊包裹（ELTA Hellenic Post）', 'elta', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('972', '阿联酋(Emirates Post)', 'emirates', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('973', '阿联酋(Emirates Post)', 'emiratesen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('974', '波兰小包(Poczta Polska)', 'emonitoringen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('975', '南非EMS', 'emssouthafrica', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('976', '乌克兰EMS(EMS Ukraine)', 'emsukraine', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('977', '乌克兰EMS-中文(EMS Ukraine)', 'emsukrainecn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('978', '英国(大包,EMS)', 'england', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('979', 'Equick China', 'equick_cn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('980', '厄立特里亚', 'eripostal', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('981', '埃塞俄比亚(Ethiopian postal)', 'ethiopia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('982', '埃塞俄比亚(Ethiopian Post)', 'ethiopian', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('983', '中欧国际物流', 'eucnrail', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('984', '德国 EUC POST', 'eucpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('985', '败欧洲', 'europe8', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('986', 'Expeditors', 'expeditors', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('987', '探路速运', 'explorer56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('988', '澳洲新干线快递', 'expressplus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('989', '颿达国际快递-英文', 'fandaguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('990', 'Fastway New Zealand', 'fastway_nz', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('991', 'Fastway South Africa', 'fastway_za', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('992', '正途供应链', 'fastzt', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('993', '飞狐快递', 'feihukuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('994', '丰羿', 'fengyee', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('995', '斐济(Fiji Post)', 'fiji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('996', 'First Flight', 'firstflight', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('997', 'First Logistics', 'firstlogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('998', 'Flash Express', 'flashexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('999', 'Flash Express-英文', 'flashexpressen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1000', '花瓣转运', 'flowerkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1001', 'FQ狂派速递', 'freakyquick', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1002', '法翔速运', 'ftlexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1003', '飞云快递系统', 'fyex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1004', 'Gati-英文', 'gatien', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1005', 'Gati-KWE', 'gatikwe', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1006', 'GDEX', 'gdex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1007', '容智快运', 'gdrz58', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1008', '新鹏快递', 'gdxp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1009', 'GE2D跨境物流', 'ge2d', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1010', '格鲁吉亚(Georgian Pos）', 'georgianpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1011', '加纳', 'ghanapost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1012', 'GHT物流', 'ghtexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1013', 'GIZTIX', 'giztix', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1014', '全球特快', 'global99', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1015', 'GLS Italy', 'gls_italy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1016', 'GOGOX', 'gogox', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1017', 'gojavas', 'gojavas', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1018', 'Grab', 'grab', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1019', '格陵兰[丹麦]（TELE Greenland A/S）', 'greenland', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1020', '潍鸿', 'grivertek', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1021', '哥士传奇速递', 'gscq365', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1022', '德尚国际速递', 'gslexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1023', '万通快递', 'gswtkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1024', 'GT国际快运', 'gtgogo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1025', '广东通路', 'guangdongtonglu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1026', '永邦快递', 'guangdongyongbang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1027', '根西岛', 'guernsey', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1028', '圭亚那', 'guyana', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1029', '宏观国际快递', 'gvpexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1030', '货六六', 'h66', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1031', '海红for买卖宝', 'haihongmmb', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1032', '翰丰快递', 'hanfengjl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1033', 'Hermes Germany', 'hermes_de', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1034', 'Hermesworld', 'hermesworld', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1035', '合心速递', 'hexinexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1036', '华瀚快递', 'hhair56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1037', 'Hi淘易快递', 'hitaoe', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1038', '互联快运', 'hlkytj', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1039', '河南全速通', 'hnqst', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1040', 'Holisol', 'holisollogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1041', '居家通', 'homexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1042', '洪都拉斯', 'honduras', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1043', '红背心', 'hongbeixin', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1044', '环球通达 ', 'hqtd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1045', '高铁速递', 'hre', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1046', '海硕高铁速递', 'hsgtsd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1047', '海淘物流', 'ht22', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1048', '华通务达物流', 'htwd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1049', '华达快运', 'huada', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1050', '华夏货运', 'huaxiahuoyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1051', '驼峰国际', 'humpline', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1052', 'hunter Express', 'hunterexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1053', '户通物流', 'hutongwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1054', '华航快递', 'hzpl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1055', '大达物流', 'idada', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1056', 'ID Express', 'idexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1057', 'iExpress', 'iexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1058', 'ILYANG', 'ilyang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1059', '艾姆勒', 'imlb2c', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1060', 'INDOPAKET', 'indopaket', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1061', '多米尼加（INPOSDOM – Instituto Postal Dominicano）', 'inposdom', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1062', 'InPost Paczkomaty', 'inpost_paczkomaty', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1063', 'Interlink Express', 'interlink', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1064', 'Interparcel', 'interparcel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1065', 'UPS i-parcel', 'iparcel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1066', 'IXPRESS', 'ixpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1067', '牙买加（Jamaica Post）', 'jamaicapost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1068', 'janio', 'janio', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1069', '急递', 'jdpplus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1070', '泽西岛', 'jerseypost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1071', '极兔国际', 'jet', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1072', '澳速通国际速递', 'jetexpressgroup', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1073', 'J&T Express', 'jetexpresszh', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1074', '捷仕', 'jetstarexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1075', '近端', 'jinduan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1076', '劲通快递', 'jintongkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1077', '冀速物流', 'jisu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1078', '九宫物流', 'jiugong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1079', '久易快递', 'jiuyicn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1080', '吉祥邮（澳洲）', 'jixiangyouau', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1081', 'JNE', 'jne', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1082', '约旦(Jordan Post)', 'jordan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1083', '骏绅物流', 'jsexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1084', 'J&T Express 泰国', 'jtexpressth', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1085', '聚物物流', 'juwu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1086', '聚中大', 'juzhongda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1087', 'Kaha Epress', 'kahaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1088', '哈萨克斯坦(Kazpost)', 'kazpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1089', '肯尼亚(POSTA KENYA)', 'kenya', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1090', 'Kerry Express', 'kerryexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1091', '启邦国际物流', 'keypon', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1092', 'Kuehne + Nagel', 'kn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1093', '考拉速递', 'koalaexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1094', '番薯国际货运', 'koali', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1095', 'Kuehne+Nagel', 'kuehnenagel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1096', 'KURASI', 'kurasi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1097', '吉尔吉斯斯坦(Kyrgyz Post)', 'kyrgyzpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1098', '蓝镖快递', 'lanbiaokuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1099', '蓝弧快递', 'lanhukuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1100', '老挝(Lao Express) ', 'lao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1101', '塞内加尔', 'laposte', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1102', '林安物流', 'lasy56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1103', '拉脱维亚(Latvijas Pasts)', 'latviaen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1104', '两点之间', 'ldzy168', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1105', '乐达全球速递', 'ledaexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1106', '乐递供应链', 'ledii', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1107', '莱索托(Lesotho Post)', 'lesotho', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1108', '美联快递', 'letseml', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1109', 'Laxship', 'lexship', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1110', 'lazada', 'lgs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1111', '联运快递', 'lianyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1112', '黎巴嫩(Liban Post)', 'libanpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1113', 'Linex', 'linex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1114', 'Lion Parcel', 'lionparcel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1115', '丽狮物流', 'lishi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1116', '立陶宛（Lietuvos pa?tas）', 'lithuania', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1117', '良藤国际速递', 'lmfex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1118', '隆浪快递', 'longlangkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1119', '长风物流', 'longvast', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1120', 'Loomis Express', 'loomisexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1121', '联通快递', 'ltparcel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1122', '路邦物流', 'lubang56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1123', '论道国际物流', 'lundao', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1124', '卢森堡(Luxembourg Post)', 'luxembourgde', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1125', '卢森堡(Luxembourg Post)', 'luxembourgfr', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1126', '马其顿(Macedonian Post)', 'macedonia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1127', 'Maersk', 'maersk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1128', 'mailamericas', 'mailamericas', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1129', '麦力快递', 'mailikuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1130', '迈隆递运', 'mailongdy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1131', 'Mainfreight', 'mainfreight', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1132', '马尔代夫(Maldives Post)', 'maldives', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1133', '马耳他（Malta Post）', 'malta', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1134', '芒果速递', 'mangguo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1135', 'Matkahuolto', 'matkahuolto', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1136', '毛里求斯(Mauritius Post)', 'mauritius', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1137', '澳洲迈速快递', 'maxeedexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1138', '美邦国际快递', 'meibang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1139', '墨西哥（Correos de Mexico）', 'mexico', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1140', 'Mexico Senda Express', 'mexicodenda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1141', '名航速运', 'mhsy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1142', '美乐维冷链物流', 'mlw', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1143', 'Mitsui OSK Lines', 'mol', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1144', '摩尔多瓦(Posta Moldovei)', 'moldova', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1145', '蒙古国(Mongol Post) ', 'mongolpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1146', '黑山(Posta Crne Gore)', 'montenegro', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1147', '摩洛哥 ( Morocco Post )', 'morocco', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1148', 'MRW', 'mrw', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1149', 'Mexico Multipack', 'multipack', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1150', '中俄速通（淼信）', 'mxe56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1151', 'MyHermes', 'myhermes', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1152', '新亚物流', 'nalexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1153', '纳米比亚(NamPost)', 'namibia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1154', 'NandanCourier', 'nandan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1155', 'Nationex', 'nationex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1156', '红马速递', 'nedahm', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1157', '荷兰速递(Nederland Post)', 'nederlandpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1158', '尼尔快递', 'nell', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1159', 'Network Courier', 'networkcourier', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1160', '尼日利亚(Nigerian Postal)', 'nigerianpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1161', 'Ninja Van ', 'ninjavan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1162', 'ninja xpress', 'ninjaxpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1163', 'Nippon Express', 'nipponexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1164', 'Norsk Global', 'norsk_global', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1165', 'NYK Line', 'nyk', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1166', 'OCA Argentina', 'ocaargen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1167', '﻿OCS ANA Group', 'ocsindia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1168', '阿曼(Oman Post)', 'oman', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1169', '无限配', 'omni2', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1170', 'Omni Parcel', 'omniparcel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1171', 'One World Express', 'oneworldexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1172', '昂威物流', 'onway', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1173', '波音速递', 'overseaex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1174', 'Packlink', 'packlink', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1175', '巴拿马', 'panama', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1176', 'Pandu Logistics', 'pandulogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1177', '巴拉圭(Correo Paraguayo)', 'paraguay', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1178', 'parcel2go', 'parcel2go', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1179', '诚一物流', 'parcelchina', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1180', 'Park N Pracel', 'parknparcel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1181', 'paxel', 'paxel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1182', 'paxelen', 'paxelen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1183', '普畅物流', 'pcwl56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1184', '派尔快递', 'peex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1185', '鹏程快递', 'pengcheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1186', '鹏远国际速递', 'pengyuanexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1187', '秘鲁(SERPOST)', 'peru', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1188', 'Parcel Freight Logistics', 'pflogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1189', 'pickupp', 'pickupp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1190', '品速心达快递', 'pinsuxinda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1191', '品信快递', 'pinxinkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1192', '葡萄牙（Portugal CTT）', 'portugalctten', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1193', 'Portugal Seur', 'portugalseur', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1194', 'POS INDONESIA', 'posindonesia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1195', '坦桑尼亚（Tanzania Posts Corporation）', 'posta', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1196', 'PostElbe', 'postelbe', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1197', '挪威（Posten Norge）', 'postennorge', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1198', 'PostNord Logistics', 'postnord', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1199', '巴布亚新几内亚(PNG Post)', 'postpng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1200', 'PT Prima Multi Cipta', 'primamulticipta', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1201', '土耳其', 'ptt', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1202', '启辰国际速递', 'qichen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1203', '千顺快递', 'qskdyxgs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1204', 'Qxpress', 'qxpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1205', 'RAM', 'ramgroup_za', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1206', 'Red Express', 'redexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1207', 'Redur', 'redur', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1208', 'Redur Spain', 'redur_es', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1209', '日昱物流', 'riyuwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1210', 'RL Carriers', 'rl_carriers', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1211', '罗马尼亚（Posta Romanian）', 'romanian', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1212', '皇家国际速运', 'royal', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1213', '瑞典（Sweden Post）', 'ruidianyouzhengen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1214', '润百特货', 'runbail', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1215', 'Safexpress', 'safexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1216', '佐川急便-英文', 'sagawaen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1217', 'Correo El Salvador', 'salvador', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1218', '萨摩亚(Samoa Post)', 'samoa', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1219', 'SAP EXPRESS', 'sapexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1220', '沙特阿拉伯(Saudi Post)', 'saudipost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1221', 'SCG', 'scglogistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1222', '速佳达快运', 'scsujiada', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1223', '四川星程快递', 'scxingcheng', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1224', '首达速运', 'sdsy888', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1225', 'Selektvracht', 'selektvracht', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1226', '森鼎国际物流', 'sendinglog', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1227', 'Sendle', 'sendle', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1228', 'International Seur', 'seur', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1229', '澳丰速递', 'sfau', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1230', 'SFC', 'sfc', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1231', 'SFC Service', 'sfcservice', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1232', '圣飞捷快递', 'sfjhd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1233', '曹操到', 'sfpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1234', 'Shadowfax', 'shadowfax', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1235', '衫达快运', 'shanda56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1236', '尚途国际货运', 'shangtuguoji', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1237', '捎客物流', 'shaoke', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1238', '商海德物流', 'shd56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1239', '盛通快递', 'shengtongscm', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1240', '神骏物流', 'shenjun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1241', '神马快递', 'shenma', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1242', '王牌快递', 'shipbyace', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1243', '苏豪快递', 'shipsoho', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1244', '世运快递', 'shiyunkuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1245', 'SHL畅灵国际物流', 'shlexp', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1246', 'wish邮', 'shpostwish', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1247', 'shreeanjanicourier', 'shreeanjanicourier', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1248', 'SiCepat Ekspres', 'sicepat', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1249', '四海快递', 'sihaiet', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1250', '四海捷运', 'sihiexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1251', 'Siodemka', 'siodemka', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1252', '易普递', 'sixroad', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1253', 'SkyNet Malaysia', 'skynetmalaysia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1254', 'skynetworldwide', 'skynetworldwide', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1255', '荷兰Sky Post', 'skypost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1256', 'Asendia HK (LATAM)', 'skypostal', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1257', '斯洛伐克(Slovenská Posta)', 'slovak', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1258', '斯洛文尼亚(Slovenia Post)', 'slovenia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1259', '斯里兰卡(Sri Lanka Post)', 'slpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1260', '所罗门群岛', 'solomon', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1261', '布基纳法索', 'sonapost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1262', 'Spoton', 'spoton', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1263', '速速达', 'ssd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1264', '首通快运', 'staky', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1265', '星速递', 'starex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1266', '星运快递', 'staryvr', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1267', '圣卢西亚', 'stlucia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1268', '速豹', 'subaoex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1269', '特急便物流', 'sucmj', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1270', '郑州速捷', 'sujievip', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1271', '苏里南', 'surpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1272', '圣文森特和格林纳丁斯', 'svgpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1273', '斯威士兰', 'swaziland', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1274', 'Amazon FBA Swiship', 'swiship', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1275', '顺友物流', 'sypost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1276', '天美快递', 'taimek', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1277', '坦桑尼亚(Tanzania Posts)', 'tanzania', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1278', '淘特物流快递', 'taote', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1279', 'TCI XPS', 'tcixps', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1280', 'TD Cargo', 'tdcargo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1281', 'The BlueBhell Couriers', 'thebluebhellcouriers', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1282', 'The Courier Guy', 'thecourierguy', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1283', '加拿大雷霆快递', 'thunderexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1284', '天纵物流', 'tianzong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1285', 'TiKi', 'tiki', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1286', '万家通快递', 'timedg', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1287', 'TIPSA', 'tipsa', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1288', 'Shree Tirupati', 'tirupati', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1289', '天联快运', 'tlky', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1290', '株式会社T.M.G', 'tmg', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1291', 'TNY物流', 'tny', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1292', '多哥', 'togo', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1293', '顶世国际物流', 'topshey', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1294', 'The Professional Couriers', 'tpcindia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1295', 'track-parcel', 'trackparcel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1296', 'Trans Kargo', 'transkargologistics', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1297', '突尼斯EMS(Rapid-Poste)', 'tunisia', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1298', '海龟国际快递', 'turtle', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1299', '天翼物流', 'tywl99', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1300', 'UBX', 'ubx', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1301', 'UEX国际物流', 'uex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1302', '欧洲UEX', 'uexiex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1303', '乌干达(Posta Uganda)', 'uganda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1304', '邮鸽速运', 'ugoexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1305', '乌克兰小包、大包(UkrPoshta)', 'ukraine', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1306', '乌克兰小包、大包(UkrPost)', 'ukrpost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1307', 'Uni Express', 'uniexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1308', 'UParcel', 'uparcel', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1309', 'UPS Freight', 'upsfreight', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1310', 'UTAO优到', 'utaoscm', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1311', '瓦努阿图(Vanuatu Post)', 'vanuatu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1312', '越中国际物流', 'vctrans', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1313', '维普恩物流', 'vps', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1314', 'Wahana', 'wahana', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1315', '万博快递', 'wanboex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1316', '宁夏万家通', 'wanjiatong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1317', '万达美', 'wdm', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1318', '文捷航空', 'wenjiesudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1319', '威速递', 'wexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1320', '香港伟豪国际物流', 'whgjkd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1321', 'Whistl', 'whistl', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1322', '万理诺物流', 'wln', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1323', '星空国际', 'wlwex', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1324', '渥途国际速运', 'wotu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1325', 'wowexpress', 'wowexpress', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1326', '沃埃家', 'wowvip', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1327', '臣邦同城', 'wto56kj', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1328', '万运国际快递', 'wygj168', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1329', '国晶物流', 'xdshipping', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1330', '鑫宏福物流', 'xhf56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1331', '西安城联速递', 'xianchengliansudi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1332', '新元快递', 'xingyuankuaidi', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1333', 'Xpert Delivery', 'xpertdelivery', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1334', 'XpressBees', 'xpressbees', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1335', '鑫世锐达', 'xsrd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1336', '迅选物流', 'xunxuan', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1337', '鑫远东速运', 'xyd666', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1338', '也门(Yemen Post)', 'yemen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1339', '驿递汇速递', 'yidihui', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1340', '艺凡快递', 'yifankd', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1341', '易航物流', 'yihangmall', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1342', 'yikonn', 'yikonn', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1343', '易欧洲国际物流', 'yiouzhou', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1344', '一柒国际物流', 'yiqiguojiwuliu', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1345', '宜送', 'yisong', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1346', '易通达', 'yitongda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1347', '益加盛快递', 'yjs', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1348', '邮驿帮高铁速运', 'youyibang', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1349', '运通快运', 'ytky168', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1350', '粤中国际货运代理（上海）有限公司', 'yuezhongsh', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1351', 'Yusen Logistics', 'yusen', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1352', '一运全成物流', 'yyqc56', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1353', '赞比亚', 'zampost', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1354', '珠峰速运', 'zf365', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1355', '智谷特货', 'zhiguil', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1356', '中环转运', 'zhonghuanus', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1357', '忠信达', 'zhongxinda', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1358', '卓实快运', 'zhuoshikuaiyun', '0', null, null, null);
INSERT INTO `oms_logistics_company` VALUES ('1359', '明辉物流', 'zsmhwl', '0', null, null, null);

ALTER table `oms_logistics_company` add column is_use tinyint after del_flag;

-- 添加部门id
ALTER TABLE `t_store_info`
    ADD COLUMN `dept_id`    bigint(20)  DEFAULT NULL COMMENT '部门id' after `status`;

INSERT INTO `sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (5, '店铺冻结', '5', 'storeStatus', NULL, 'danger', 'N', '0', 'admin', '2020-07-27 19:48:57', '', NULL, NULL);

-- 新增是否自定义标识字段
ALTER TABLE `pms_brand`
    ADD COLUMN `customize` int(11) DEFAULT 0 COMMENT '是否自定义0否，1是，默认0' AFTER REASON;

ALTER TABLE `pms_goods_import`
    ADD COLUMN `tmall` int(11) DEFAULT 0 COMMENT '是否自定义0否，1是，默认0' AFTER channel;

-- 规格关联对于平台的商品id
ALTER TABLE `pms_spec`
    ADD COLUMN `numIid`  varchar(32) NULL COMMENT '导入商品的平台id' AFTER `nick_name`,
    ADD COLUMN `spu_id`  bigint(20) NULL COMMENT 'spuid' AFTER `numIid`;

--
ALTER TABLE `ums_member_likes`
    ADD COLUMN `resource_owner_id` bigint(20) not null comment '作者id' after `resource_id`;


ALTER TABLE `ls_system_setting`
    ADD COLUMN  `privacy_protocol`       text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '隐私协议' after `register_protocol`,
    ADD COLUMN   `recharge_protocol`      text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '充值协议' after `privacy_protocol`;

-- 2022/1/7 添加结算功能 lyz
DROP TABLE IF EXISTS `settlement_record`;
CREATE TABLE `settlement_record` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `account_code` varchar(64) NOT NULL,
                                     `in_billing` decimal(10,2) NOT NULL,
                                     `out_billing` decimal(10,2) NOT NULL,
                                     `settlement_time` datetime NOT NULL,
                                     `settlement_staus` char(1) NOT NULL COMMENT '结算状态',
                                     `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记  0未删除 1删除 默认0',
                                     `create_time` datetime NOT NULL default now() COMMENT '创建时间',
                                     `create_by` varchar(30) NOT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1478363071839305731 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `internal_trans`;
CREATE TABLE `internal_trans` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `trans_type` varchar(255) NOT NULL COMMENT '操作类型',
                                  `form_account_code` varchar(64) NOT NULL,
                                  `to_account_code` varchar(64) NOT NULL,
                                  `transfer_time` datetime NOT NULL COMMENT '转移时间',
                                  `transfer_amount` decimal(10,2) NOT NULL,
                                  `transfer_type` varchar(20) NOT NULL COMMENT '进出账类型',
                                  `operate_type` varchar(255) NOT NULL COMMENT '操作类型',
                                  `create_by` varchar(255) DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记  0未删除 1删除 默认0',
                                  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `settlement_withdrawal`;
CREATE TABLE `settlement_withdrawal` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                         `store_name` varchar(64) NOT NULL COMMENT '门店名称',
                                         `withdrawal_code` varchar(64) NOT NULL COMMENT '提现单号',
                                         `withdrawal_amount` decimal(10,2) NOT NULL COMMENT '提现金额',
                                         `real_store_in_amount` decimal(10,2) NOT NULL COMMENT '商户实收',
                                         `store_id` bigint(20) NOT NULL,
                                         `status` varchar(255) NOT NULL COMMENT '状态',
                                         `create_by` varchar(255) NOT NULL,
                                         `fail_reason` varchar(255) DEFAULT NULL,
                                         `pay_time` datetime DEFAULT NULL COMMENT '付款时间',
                                         `create_time` datetime NOT NULL COMMENT '申请时间',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `settlement_account`;
CREATE TABLE `settlement_account` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                      `account_code` varchar(64) NOT NULL COMMENT '账户类型',
                                      `account_type` varchar(20) NOT NULL COMMENT '账户类型',
                                      `account_name` varchar(255) NOT NULL,
                                      `associate_id` bigint(20) NOT NULL COMMENT '关联id',
                                      `withdrawal_rate` decimal(10,2) NOT NULL DEFAULT '0.00',
                                      `settlement_time` datetime NULL COMMENT '结算时间',
                                      `settlement_before_balance` decimal(10,2) NOT NULL,
                                      `settlement_after_balance` decimal(10,2) NOT NULL,
                                      `frozen_balance` decimal(10,2) NOT NULL COMMENT '冻结余额',
                                      `available_balance` decimal(10,2) NOT NULL COMMENT '可用余额',
                                      `settlement_type` varchar(255) NOT NULL COMMENT '结算类型',
                                      `create_by` varchar(255) NOT NULL,
                                      `create_time` datetime NOT NULL default now(),
                                      `del_flag` char(1) DEFAULT '0' COMMENT '删除标记  0未删除 1删除 默认0',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_associate_id_settlement_type` (`associate_id`,`settlement_type`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('结算管理', '0', '3', 'settlement', '', 1, 0, 'M', '0', '0', null, 'cascader', 'admin', '2021-12-23 12:10:06', '', null, '');

-- 按钮父菜单ID
SELECT @pparentId := LAST_INSERT_ID();

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户', @pparentId, '1', 'settlementAccount', 'settlement/settlementAccount/index', 1, 0, 'C', '0', '0', 'settlement:settlementAccount:list', '#', 'admin', sysdate(), '', null, '结算账户菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementAccount:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementAccount:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementAccount:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementAccount:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementAccount:export',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录', @pparentId, '1', 'settlementRecord', 'settlement/settlementRecord/index', 1, 0, 'C', '0', '0', 'settlement:settlementRecord:list', '#', 'admin', sysdate(), '', null, '结算记录菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementRecord:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementRecord:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementRecord:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementRecord:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementRecord:export',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现', @pparentId, '1', 'settlementWithdrawal', 'settlement/settlementWithdrawal/index', 1, 0, 'C', '0', '0', 'settlement:settlementWithdrawal:list', '#', 'admin', sysdate(), '', null, '提现菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementWithdrawal:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementWithdrawal:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementWithdrawal:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementWithdrawal:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'settlement:settlementWithdrawal:export',       '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('结算管理', '0', '3', 'settlement', '', 1, 'M', '0', '0', null, 'cascader', 'admin', '2021-12-23 12:10:06', '', null, '');

-- 按钮父菜单ID
SELECT @pparentId := LAST_INSERT_ID();

-- 菜单 SQL
insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('店铺结算', @pparentId, '1', 'storeSettlement', 'settlement/storeSettlement/index', 1, 'C', '0', '0', 'settlement:storeSettlement:list', '#', 'admin', sysdate(), '', null, '结算账户菜单');


-- 菜单 SQL
insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户', @pparentId, '1', 'settlementAccount', 'settlement/settlementAccount/index', 1, 'C', '0', '0', 'settlement:settlementAccount:list', '#', 'admin', sysdate(), '', null, '结算账户菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户查询', @parentId, '1',  '#', '', 1,  'F', '0', '0', 'settlement:settlementAccount:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户新增', @parentId, '2',  '#', '', 1, 'F', '0', '0', 'settlement:settlementAccount:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户修改', @parentId, '3',  '#', '', 1, 'F', '0', '0', 'settlement:settlementAccount:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户删除', @parentId, '4',  '#', '', 1, 'F', '0', '0', 'settlement:settlementAccount:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算账户导出', @parentId, '5',  '#', '', 1, 'F', '0', '0', 'settlement:settlementAccount:export',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录', @pparentId, '1', 'settlementRecord', 'settlement/settlementRecord/index', 1, 'C', '0', '0', 'settlement:settlementRecord:list', '#', 'admin', sysdate(), '', null, '结算记录菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录查询', @parentId, '1',  '#', '', 1, 'F', '0', '0', 'settlement:settlementRecord:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录新增', @parentId, '2',  '#', '', 1, 'F', '0', '0', 'settlement:settlementRecord:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录修改', @parentId, '3',  '#', '', 1, 'F', '0', '0', 'settlement:settlementRecord:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录删除', @parentId, '4',  '#', '', 1, 'F', '0', '0', 'settlement:settlementRecord:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('结算记录导出', @parentId, '5',  '#', '', 1, 'F', '0', '0', 'settlement:settlementRecord:export',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现', @pparentId, '1', 'settlementWithdrawal', 'settlement/settlementWithdrawal/index', 1, 'C', '0', '0', 'settlement:settlementWithdrawal:list', '#', 'admin', sysdate(), '', null, '提现菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现查询', @parentId, '1',  '#', '', 1, 'F', '0', '0', 'settlement:settlementWithdrawal:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现新增', @parentId, '2',  '#', '', 1, 'F', '0', '0', 'settlement:settlementWithdrawal:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现修改', @parentId, '3',  '#', '', 1, 'F', '0', '0', 'settlement:settlementWithdrawal:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现删除', @parentId, '4',  '#', '', 1, 'F', '0', '0', 'settlement:settlementWithdrawal:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_store_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('提现导出', @parentId, '5',  '#', '', 1, 'F', '0', '0', 'settlement:settlementWithdrawal:export',       '#', 'admin', sysdate(), '', null, '');



INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('账户类型', 'account_type', '0', 'admin', '2022-01-07 12:03:10', '', null, '账户类型');
INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('结算类型', 'settlement_type', '0', 'admin', '2022-01-07 12:11:28', '', null, null);
INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('提现状态', 'withdrawal_status', '0', 'admin', '2022-01-07 12:42:10', '', null, null);


INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '个人', 'PERSONAL', 'account_type', null, 'warning', 'N', '0', 'admin', '2022-01-07 12:07:08', 'admin', '2022-01-07 12:07:42', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '系统', 'SYSTEM', 'account_type', null, 'success', 'N', '0', 'admin', '2022-01-07 12:07:26', 'admin', '2022-01-07 12:07:36', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '商户', 'STORE', 'account_type', null, 'primary', 'N', '0', 'admin', '2022-01-07 12:07:56', 'admin', '2022-01-07 12:08:26', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '自动', 'AUTO', 'settlement_type', null, 'default', 'N', '0', 'admin', '2022-01-07 12:12:10', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '手动', 'MANUAL', 'settlement_type', null, 'default', 'N', '0', 'admin', '2022-01-07 12:12:24', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '审核中', 'UNDER_AUDIT', 'withdrawal_status', null, 'primary', 'N', '0', 'admin', '2022-01-07 12:42:39', 'admin', '2022-01-07 12:44:01', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '审核通过', 'PASS', 'withdrawal_status', null, 'success', 'N', '0', 'admin', '2022-01-07 12:42:52', 'admin', '2022-01-07 12:44:35', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '审核未通过', 'REJECTED', 'withdrawal_status', null, 'info', 'N', '0', 'admin', '2022-01-07 12:43:05', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '提现中', 'WITHDRAWAL_ING', 'withdrawal_status', null, 'success', 'N', '0', 'admin', '2022-01-07 12:43:26', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '提现失败', 'WITHDRAWAL_FAIL', 'withdrawal_status', null, 'danger', 'N', '0', 'admin', '2022-01-07 12:43:44', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '提现成功', 'WITHDRAWAL_SUCCESS', 'withdrawal_status', null, 'success', 'N', '0', 'admin', '2022-01-07 12:44:24', '', null, null);


--
ALTER TABLE `sys_store_user`
    MODIFY COLUMN `phonenumber`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码' AFTER `email`;
-- 添加视频封面页
ALTER TABLE `short_video`
    ADD COLUMN front_image_url  varchar(255)  null comment '封面图片' after video_url;

-- v6.0.4 upgrade

ALTER TABLE `settlement_record`
    ADD COLUMN `oms_billing_records`  LONGTEXT NULL COMMENT '账单记录';

INSERT INTO `sys_job` VALUES ('108', '结算任务', 'DEFAULT', 'settlementTask.autoSettlementJob(\'\')', '0 0 0 * * ?', '1', '1', '1', 'admin', '2022-01-03 12:43:25', 'admin', '2022-01-03 12:44:29', '');

-- 优化第三方产品导入
ALTER TABLE `pms_goods`
    ADD COLUMN `spu_import_id` bigint(20) null comment '商品导入id';





ALTER TABLE `settlement_account`
    ADD COLUMN `child_type`  varchar(20) NULL COMMENT '子类型';

INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES (' 账户子类型', 'child_type', '0', 'admin', '2022-01-11 12:34:22', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '系统交易账户', 'SYSTEM_TRADING', 'child_type', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '系统提现账户', 'SYSTEM_WITHDRAWAL', 'child_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '系统手续费账户', 'SYSTEM_HANDLING_FEE', 'child_type', null, 'success', 'N', '0', 'admin', '2022-01-11 12:36:49', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('0', '系统结算账户', 'SYSTEM_SETTLEMENT', 'child_type', null, 'success', 'N', '0', 'admin', '2022-01-11 12:36:49', '', null, null);

DROP TABLE IF EXISTS `im_session`;
CREATE TABLE `im_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(255) DEFAULT NULL,
  `nid` varchar(255) DEFAULT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  `device_name` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `channel` varchar(255) DEFAULT NULL,
  `app_version` varchar(255) DEFAULT NULL,
  `os_version` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `bind_time` bigint(20) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `im_user_friends`;
CREATE TABLE `im_user_friends` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) NOT NULL,
  `friend_id` varchar(64) NOT NULL,
  `create_time` datetime NOT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `del_flag` varchar(255) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `im_message`;
CREATE TABLE `im_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `action` varchar(255) NOT NULL,
  `type` int(11) DEFAULT '0',
  `title` varchar(255) DEFAULT NULL COMMENT '消息标题',
  `content` longtext NOT NULL COMMENT '内容',
  `sender` varchar(64) NOT NULL COMMENT '消息发送者账号',
  `receiver` varchar(64) NOT NULL,
  `format` varchar(255) NOT NULL COMMENT '格式',
  `read_status` tinyint(1) DEFAULT '0',
  `extra` varchar(255) DEFAULT NULL COMMENT '附件内容',
  `timestamp` bigint(20) NOT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1642862839896 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `im_user`;
CREATE TABLE `im_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nick_name` varchar(64) DEFAULT NULL COMMENT '昵称',
  `user_id` bigint(20) NOT NULL,
  `user_code` varchar(64) NOT NULL COMMENT '用户code',
  `type` varchar(64) NOT NULL COMMENT '类型',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` varchar(255) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1484782341221515267 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='im用户';



INSERT INTO `sys_menu` (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('聊天', '2282', '11', 'im', 'im/chat', '', '1', '1', 'C', '0', '0', null, 'message', 'admin', '2022-01-22 14:46:21', '', null, '');
INSERT INTO `sys_store_menu` (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('聊天', '0', '11', 'im', 'im/chat', '1', 'C', '0', '0', null, 'message', 'admin', '2022-01-22 14:46:21', '', null, '');

-- 优化导入第三方带货逻辑
ALTER table `pms_goods`
    add column  `is_dropship` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci               DEFAULT '0' COMMENT '是否支持第三方带货 0 否 1 是默认0' after is_virtual;

ALTER table `pms_sku`
    add column  `is_dropship` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci               DEFAULT '0' COMMENT '是否支持第三方带货 0 否 1 是默认0' after is_virtual;

ALTER table `oms_order_sku`
    add column  `is_dropship` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci               DEFAULT '0' COMMENT '是否第三方带货订单 0 否 1 是默认0' after s_commission_rate;

ALTER table `oms_order`
    add column  `is_dropship` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci               DEFAULT '0' COMMENT '是否第三方带货订单 0 否 1 是默认0' after order_type;


-- 6.0.5
-- 添加结算信息 2022-1-26
ALTER table `t_store_info`
    add column merchant_type char(1) CHARACTER SET utf8 COLLATE utf8_general_ci default '0' comment '账户类型 0 普通店铺 1 付费店铺',
    add column platform_fee_rate float default 0 comment '平台服务费率';

DROP TABLE IF EXISTS `t_store_info_change_history`;
create table `t_store_info_change_history`
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `store_id` bigint(20) NOT NULL COMMENT '店铺id',
    `event_name`     varchar(255)                            DEFAULT '' NOT NULL COMMENT  '事件名称',
    `event_content`  varchar(255)                            DEFAULT '' NOT NULL COMMENT  '事件内容',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `create_by`      varchar(45)    NULl COMMENT '事件发起者',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='店铺更改日志';

-- ----------------------------
-- Table structure for ls_platform_fee_setting
-- ----------------------------
DROP TABLE IF EXISTS `ls_platform_fee_setting`;
CREATE TABLE `ls_platform_fee_setting`
(
    `id`           bigint(20)                                               NOT NULL AUTO_INCREMENT COMMENT '主键id	',
    `column_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '字段名称',
    `column_value` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段值',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 562
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '平台费用设置'
  ROW_FORMAT = Dynamic;


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台费用设置', '2135', '1', 'platFeeSetting', 'setting/platFeeSetting/index', 1, 0, 'C', '0', '0', 'setting:platFeeSetting:list', 'documentation', 'admin', sysdate(), '', null, '平台费用设置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台费用设置查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'setting:platFeeSetting:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台费用设置新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'setting:platFeeSetting:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台费用设置修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'setting:platFeeSetting:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台费用设置删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'setting:platFeeSetting:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('平台费用设置导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'setting:platFeeSetting:export',       '#', 'admin', sysdate(), '', null, '');



--
-- Table structure for table `validate_email`
--

DROP TABLE IF EXISTS `validate_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `validate_email` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `uid` int(11) DEFAULT NULL,
                                  `email` varchar(255) DEFAULT NULL,
                                  `platform` int(1) DEFAULT NULL,
                                  `template_id` int(11) DEFAULT NULL,
                                  `title` varchar(255) DEFAULT NULL,
                                  `content` text,
                                  `code` varchar(255) DEFAULT NULL,
                                  `uuid` varchar(255) DEFAULT NULL,
                                  `status` int(1) DEFAULT NULL,
                                  `gmt_send` datetime DEFAULT NULL,
                                  `gmt_create` datetime DEFAULT NULL,
                                  `version` int(11) unsigned DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `validate_email`
--



--
-- Table structure for table `validate_sms`
--

DROP TABLE IF EXISTS `validate_sms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `validate_sms` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `uid` int(11) DEFAULT NULL,
                                `phone` varchar(30) DEFAULT NULL,
                                `content` text,
                                `send_type` int(1) DEFAULT NULL,
                                `platform` int(11) DEFAULT NULL,
                                `template_id` int(11) DEFAULT NULL,
                                `status` int(1) DEFAULT NULL,
                                `gmt_create` datetime DEFAULT NULL,
                                `gmt_send` datetime DEFAULT NULL,
                                `version` int(11) unsigned DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `validate_sms`
--
INSERT INTO `validate_sms` VALUES (4,NULL,'18982832970','您的注册验证码是549815，用于验证您的帐号注册。',1,1,1,2,'2018-09-18 16:15:00','2018-09-18 16:15:00',0),(5,NULL,'18982832970','手机登录验证码 244010',1,1,43,2,'2018-09-18 16:16:40','2018-09-18 16:16:40',0),(6,NULL,'18140203069','您的注册验证码是824115，用于验证您的帐号注册。',1,1,1,2,'2018-09-18 16:29:14','2018-09-18 16:29:14',0),(7,NULL,'18140203069','手机登录验证码 664593',1,1,43,2,'2018-09-18 16:31:23','2018-09-18 16:31:23',0),(8,NULL,'18153889220','您的注册验证码是571196，用于验证您的帐号注册。',1,1,1,2,'2018-09-18 16:45:37','2018-09-18 16:45:37',0),(9,NULL,'17348008022','您的注册验证码是317539，用于验证您的帐号注册。',1,1,1,2,'2018-09-19 10:51:44','2018-09-19 10:51:44',0),(10,NULL,'17348008022','手机登录验证码 240710',1,1,43,2,'2018-09-19 10:52:40','2018-09-19 10:52:40',0);

--
-- Table structure for table `validate_statistics`
--

DROP TABLE IF EXISTS `validate_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `validate_statistics` (
                                       `id` int(11) NOT NULL AUTO_INCREMENT,
                                       `platform` int(11) DEFAULT NULL COMMENT '平台标志 1：富比特网 2：富比特ICO',
                                       `send_type` int(11) DEFAULT NULL COMMENT '发送类型 1:普通短信 2:语音短信 3:国际短信 4:邮件',
                                       `times` int(11) DEFAULT NULL,
                                       `gmt_create` datetime DEFAULT NULL,
                                       `gmt_modified` datetime DEFAULT NULL,
                                       `version` int(11) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `validate_statistics`
--


--
-- Table structure for table `validate_template`
--

DROP TABLE IF EXISTS `validate_template`;
CREATE TABLE `validate_template` (
                                     `id` int(11) NOT NULL AUTO_INCREMENT,
                                     `send_type` int(2) DEFAULT NULL COMMENT '1-国内短信;2-语音短信;3-国际短信;4-邮件',
                                     `business_type` varchar(255) DEFAULT NULL,
                                     `platform` int(1) DEFAULT NULL,
                                     `language` int(1) DEFAULT NULL,
                                     `template` text,
                                     `params` varchar(128) DEFAULT NULL,
                                     `gmt_create` datetime DEFAULT NULL,
                                     `gmt_modified` datetime DEFAULT NULL,
                                     `version` int(11) unsigned DEFAULT '1',
                                     `system_version` tinyint(1) DEFAULT '0' COMMENT '系统使用版本:0代表公用，1代表1.0使用，2代表2.0使用',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `validate_template`
--
INSERT INTO `validate_template` VALUES ('1', '1', '112', '1', '3', 'You are regestering UHHA. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('2', '1', '112', '1', '1', '您正在注册UHHA，注册验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('3', '1', '123', '1', '1', '您正在使用UHHA进行验证登录，登录验证码：#code#。请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('4', '1', '123', '1', '3', 'You are loginning to UHHA with verification code. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('5', '1', '109', '1', '1', '您正在找回密码，验证码：#code#。请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('6', '1', '109', '1', '3', 'You are recovering login password, verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('7', '1', '106', '1', '1', '您正在修改密码，验证码：#code#。请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('8', '1', '106', '1', '3', 'You are changing login password, verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('9', '1', '107', '1', '3', 'You are changing payment password, verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('10', '1', '107', '1', '1', '您正在修改支付密码，验证码：#code#。请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('11', '1', '301', '1', '1', '您有新的订单#orderNo#，金额#price#', '16#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('12', '1', '301', '1', '3', 'You have a new order: #orderNo#, volume :#price#', '16#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('13', '4', '301', '1', '1', '您有新的订单#orderNo#，金额#price#', '16#7', null, '2022-01-28 18:13:51', null, null);
INSERT INTO `validate_template` VALUES ('14', '4', '301', '1', '3', 'You have a new order: #orderNo#, volume :#price#', '16#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('15', '1', '302', '1', '1', '订单#orderNo#支付成功', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('16', '1', '302', '1', '3', 'Order: #orderNo# paid successfully', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('17', '4', '302', '1', '1', '订单#orderNo#支付成功', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('18', '4', '302', '1', '3', 'Order: #orderNo# paid successfully', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('19', '1', '303', '1', '1', '订单#orderNo#，通过#logisticCompanyName#运单号#wayBillCode#发货成功。', '16#17#18', null, null, null, null);
INSERT INTO `validate_template` VALUES ('20', '1', '303', '1', '3', 'Order: #orderNo# was sent to logistic company: #logisticCompanyName#, code: #wayBillCode#', '16#17#18', null, null, null, null);
INSERT INTO `validate_template` VALUES ('21', '4', '303', '1', '1', '订单#orderNo#，通过#logisticCompanyName#运单号#wayBillCode#发货成功', '16#17#18', null, null, null, null);
INSERT INTO `validate_template` VALUES ('22', '4', '303', '1', '3', 'Order: #orderNo# was sent to logistic company: #logisticCompanyName#, code: #wayBillCode#', '16#17#18', null, null, null, null);
INSERT INTO `validate_template` VALUES ('23', '1', '304', '1', '1', '订单#orderNo#，收货成功', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('24', '1', '304', '1', '3', 'Order: #orderNo# received', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('25', '4', '304', '1', '1', '订单#orderNo#，收货成功', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('26', '4', '304', '1', '3', 'Order: #orderNo# received', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('27', '1', '305', '1', '1', '您的订单#orderNo#，有退款申请', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('28', '1', '305', '1', '3', 'You order: #orderNo# has a new refund request', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('29', '4', '305', '1', '1', '您的订单#orderNo#，有退款申请', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('30', '4', '305', '1', '3', 'You order: #orderNo# has a new refund request', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('31', '1', '306', '1', '1', '您的订单#orderNo#，退款成功', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('32', '1', '306', '1', '3', 'You order: #orderNo# refund request finished', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('33', '4', '306', '1', '1', '您的订单#orderNo#，退款成功', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('34', '4', '306', '1', '3', 'You order: #orderNo# refund request finished', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('35', '1', '307', '1', '1', '您的订单#orderNo#，有退货申请', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('36', '1', '307', '1', '3', 'You order: #orderNo# has a new return request', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('37', '4', '307', '1', '1', '您的订单#orderNo#，有退货申请', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('38', '4', '307', '1', '3', 'You order: #orderNo# has a new return request', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('39', '1', '308', '1', '1', '您的订单#orderNo#，退货成功', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('40', '1', '308', '1', '3', 'You order: #orderNo# return request finished', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('41', '4', '308', '1', '1', '您的订单#orderNo#，退货成功', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('42', '4', '308', '1', '3', 'You order: #orderNo# return request finished', '16', null, null, null, null);
INSERT INTO `validate_template` VALUES ('43', '5', '301', '1', '1', '您有新的订单#orderNo#，金额#price#', '16#7', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('44', '5', '301', '1', '3', 'You have a new order: #orderNo#, volume :#price#', '16#7', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('45', '5', '302', '1', '1', '订单#orderNo#支付成功', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('46', '5', '302', '1', '3', 'Order: #orderNo# paid successfully', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('47', '5', '303', '1', '1', '订单#orderNo#，发货成功', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('48', '5', '303', '1', '3', 'Order: #orderNo# was sent to logistic', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('49', '5', '304', '1', '1', '订单#orderNo#，收货成功', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('50', '5', '304', '1', '3', 'Order: #orderNo# received', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('51', '5', '305', '1', '1', '您的订单#orderNo#，有退款申请', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('52', '5', '305', '1', '3', 'You order: #orderNo# has a new refund request', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('53', '5', '306', '1', '1', '您的订单#orderNo#，退款成功', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('54', '5', '306', '1', '3', 'You order: #orderNo# refund request finished', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('55', '5', '307', '1', '1', '您的订单#orderNo#，有退货申请', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('56', '5', '307', '1', '3', 'You order: #orderNo# has a new return request', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('57', '5', '308', '1', '1', '您的订单#orderNo#，退货成功', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('58', '5', '308', '1', '3', 'You order: #orderNo# return request finished', '16', null, null, '1', '0');
INSERT INTO `validate_template` VALUES ('59', '1', '131', '2', '3', 'You are regestering UHHA Store. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('60', '1', '131', '2', '1', '您正在注册UHHA商户平台，注册验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('61', '4', '131', '2', '3', 'You are regestering UHHA Store. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('62', '4', '131', '2', '1', '您正在注册UHHA商户平台，注册验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('63', '5', '131', '2', '3', 'You are regestering UHHA Store. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('64', '5', '131', '2', '1', '您正在注册UHHA商户平台，注册验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);

-- 国际短信
INSERT INTO `validate_template` VALUES ('101', '3', '112', '1', '3', 'You are regestering UHHA. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('102', '3', '123', '1', '3', 'You are loginning to UHHA with verification code. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('103', '3', '109', '1', '3', 'You are recovering login password, verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('104', '3', '106', '1', '3', 'You are changing login password, verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('105', '3', '107', '1', '3', 'You are changing payment password, verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('106', '3', '131', '2', '3', 'You are regestering UHHA Store. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);

INSERT INTO `validate_template` VALUES ('107', '1', '104', '1', '3', 'You are withdrawing #price#$. Verification code: #code#. Please DON\"T share the code to anyone else!', '1#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('108', '1', '104', '1', '1', '您正在提现 #price#美元，验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('109', '3', '104', '1', '3', 'You are withdrawing #price#$. Verification code: #code#. Please DON\"T share the code to anyone else!', '1#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('110', '3', '104', '1', '1', '您正在提现 #price#美元，验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('111', '4', '104', '1', '3', 'You are withdrawing #price#$. Verification code: #code#. Please DON\"T share the code to anyone else!', '1#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('112', '4', '104', '1', '1', '您正在提现 #price#美元，验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('113', '5', '104', '1', '3', 'You are withdrawing #price#$. Verification code: #code#. Please DON\"T share the code to anyone else!', '1#7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('114', '5', '104', '1', '1', '您正在提现 #price#美元，验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1#7', null, null, null, null);

INSERT INTO `validate_template` VALUES ('115', '1', '132', '1', '3', 'You withdrawing request of #price#$ submitted successfully!', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('116', '1', '132', '1', '1', '您的#price#美元提现请求成功提交!', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('117', '3', '132', '1', '3', 'You withdrawing request of #price#$ submitted successfully!', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('118', '3', '132', '1', '1', '您的#price#美元提现请求成功提交!', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('119', '4', '132', '1', '3', 'You withdrawing request of #price#$ submitted successfully!', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('120', '4', '132', '1', '1', '您的#price#美元提现请求成功提交!', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('121', '5', '132', '1', '3', 'You withdrawing request of #price#$ submitted successfully!', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('122', '5', '132', '1', '1', '您的#price#美元提现请求成功提交!', '7', null, null, null, null);

-- 视频审核
INSERT INTO `validate_template` VALUES ('123', '4', '400', '1', '3', 'Your video: #videoTitle# was submitted successfully!', '30', null, null, null, null);
INSERT INTO `validate_template` VALUES ('124', '4', '400', '1', '1', '您的视频#videoTitle#提交成功!', '30', null, null, null, null);
INSERT INTO `validate_template` VALUES ('125', '5', '400', '1', '3', 'Your video: #videoTitle# was submitted successfully!', '30', null, null, null, null);
INSERT INTO `validate_template` VALUES ('126', '5', '400', '1', '1', '您的视频#videoTitle#提交成功!', '30', null, null, null, null);

INSERT INTO `validate_template` VALUES ('127', '4', '401', '1', '3', 'Your video: #videoTitle# submission failed due to: #reason#', '30#31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('128', '4', '401', '1', '1', '您的视频#videoTitle#提交失败!原因为：#reason#', '30', null, null, null, null);
INSERT INTO `validate_template` VALUES ('129', '5', '401', '1', '3', 'Your video: #videoTitle# submission failed due to: #reason#', '30#31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('130', '5', '401', '1', '1', '您的视频#videoTitle#提交失败!原因为：#reason#', '30', null, null, null, null);

-- 实名认证
INSERT INTO `validate_template` VALUES ('131', '1', '402', '1', '3', 'Your passed realname screenning!', '', null, null, null, null);
INSERT INTO `validate_template` VALUES ('132', '1', '402', '1', '1', '您的实名认证审核成功!', '', null, null, null, null);
INSERT INTO `validate_template` VALUES ('133', '3', '402', '1', '3', 'Your passed realname screening!', '', null, null, null, null);
INSERT INTO `validate_template` VALUES ('134', '3', '402', '1', '1', '您的实名认证审核成功!', '', null, null, null, null);
INSERT INTO `validate_template` VALUES ('135', '4', '402', '1', '3', 'Your passed realname screening!', '', null, null, null, null);
INSERT INTO `validate_template` VALUES ('136', '4', '402', '1', '1', '您的实名认证审核成功!', '', null, null, null, null);
INSERT INTO `validate_template` VALUES ('137', '5', '402', '1', '3', 'Your passed realname screening!', '', null, null, null, null);
INSERT INTO `validate_template` VALUES ('138', '5', '402', '1', '1', '您的实名认证审核成功!', '', null, null, null, null);

INSERT INTO `validate_template` VALUES ('139', '1', '403', '1', '3', 'Your realname screening failed due to #reason#', '31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('140', '1', '403', '1', '1', '您的实名认证申请审核失败。原因为#reason#', '31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('141', '3', '403', '1', '3', 'Your realname screening failed due to #reason#', '31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('142', '3', '403', '1', '1', '您的实名认证申请审核失败。原因为#reason#', '31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('143', '4', '403', '1', '3', 'Your realname screening failed  due to #reason#', '31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('144', '4', '403', '1', '1', '您的实名认证申请审核失败。原因为#reason#', '31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('145', '5', '403', '1', '3', 'Your realname screening failed  due to #reason#', '31', null, null, null, null);
INSERT INTO `validate_template` VALUES ('146', '5', '403', '1', '1', '您的实名认证申请审核失败。原因为#reason#', '31', null, null, null, null);

-- 修改商铺的登录密码
INSERT INTO `validate_template` VALUES ('147', '1', '133', '2', '3', 'You are recovering login password, verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('148', '1', '133', '2', '1', '您正在修改密码，验证码：#code#。请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('149', '3', '133', '2', '3', 'You are recovering login password, verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('150', '3', '133', '2', '1', '您正在修改密码，验证码：#code#。请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
-- 视频收到评论
INSERT INTO `validate_template` VALUES ('151', '5', '404', '1', '3', 'Your video #videoTitle# got comments: #comment# from #user#', '30#32#5', null, null, null, null);
INSERT INTO `validate_template` VALUES ('152', '5', '404', '1', '1', '您的视频#videoTitle#被#user#评论：#comment#', '30#5#32', null, null, null, null);

-- 兑换token
INSERT INTO `validate_template` VALUES ('153', '1', '315', '1', '3', 'You are redeeming #price# UHHA token with points', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('154', '1', '315', '1', '1', '您兑换了#price# UHHA Token', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('155', '3', '315', '1', '3', 'You are redeeming #price# UHHA token with points', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('156', '3', '315', '1', '1', '您兑换了#price# UHHA Token', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('157', '4', '315', '1', '3', 'You are redeeming #price# UHHA token with points', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('158', '4', '315', '1', '1', '您兑换了#price# UHHA Token', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('159', '5', '315', '1', '3', 'You are redeeming #price# UHHA token with points', '7', null, null, null, null);
INSERT INTO `validate_template` VALUES ('160', '5', '315', '1', '1', '您兑换了#price# UHHA Token', '7', null, null, null, null);

-- 提币操作
INSERT INTO `validate_template` VALUES ('161', '1', '105', '1', '3', 'You are withdrawing token. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('162', '1', '105', '1', '1', '您正在提币，注册验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('163', '3', '105', '1', '3', 'You are withdrawing token. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('164', '3', '105', '1', '1', '您正在提币，注册验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('165', '4', '105', '1', '3', 'You are withdrawing token. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('166', '4', '105', '1', '1', '您正在提币，注册验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('167', '5', '105', '1', '3', 'You are withdrawing token. Verification code: #code#. Please DON\"T share the code to anyone else!', '1', null, null, null, null);
INSERT INTO `validate_template` VALUES ('168', '5', '105', '1', '1', '您正在提币，注册验证码：#code#，请勿将本验证码分享给任何人，谨防受骗。', '1', null, null, null, null);

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('消息模板', '2135', '1', 'validateTemplate', 'setting/validateTemplate/index', 1, 0, 'C', '0', '0', 'setting:validateTemplate:list', '#', 'admin', sysdate(), '', null, '校验菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('消息模板查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'setting:validateTemplate:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('消息模板新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'setting:validateTemplate:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('消息模板修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'setting:validateTemplate:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('消息模板删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'setting:validateTemplate:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('消息模板导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'setting:validateTemplate:export',       '#', 'admin', sysdate(), '', null, '');

-- 添加字典数据
INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('系统语言', 'language_type', '0', 'admin', '2022-01-07 12:03:10', '', null, '系统语言');

INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('1', '中文简体', '1', 'language_type', null, '', 'N', '0', 'admin', '2022-01-07 12:07:08', 'admin', '2022-01-07 12:07:42', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('2', '中文繁体', '2', 'language_type', null, '', 'N', '0', 'admin', '2022-01-07 12:07:08', 'admin', '2022-01-07 12:07:42', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('3', '英语', '3', 'language_type', null, '', 'N', '0', 'admin', '2022-01-07 12:07:08', 'admin', '2022-01-07 12:07:42', null);

INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('发送类型', 'send_type', '0', 'admin', '2022-01-07 12:03:10', '', null, '发送类型');

INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('1', '普通短信', '1', 'send_type', null, '', 'N', '0', 'admin', '2022-01-07 12:07:08', 'admin', '2022-01-07 12:07:42', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('2', '语音短信', '2', 'send_type', null, '', 'N', '0', 'admin', '2022-01-07 12:07:08', 'admin', '2022-01-07 12:07:42', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('3', '国际短信', '3', 'send_type', null, '', 'N', '0', 'admin', '2022-01-07 12:07:08', 'admin', '2022-01-07 12:07:42', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('4', '邮件', '4', 'send_type', null, '', 'N', '0', 'admin', '2022-01-07 12:07:08', 'admin', '2022-01-07 12:07:42', null);

-- 是否发送消息判断条件
INSERT into `f_system_args` (fid, fkey, ftype, fvalue, fdescription, version, furl)
values (84,'isLiveEnabled',2,'0','是否线上环境 0不是，1是',0,'');

alter  table internal_trans DROP  trans_type;


ALTER TABLE `oms_billing_records`
    ADD COLUMN `settlement_time`        datetime                                            DEFAULT NULL COMMENT '结算时间';

INSERT INTO cms_article_column (id, name, parentId, sort, is_show, del_flag, create_time, modify_time, del_time) VALUES (1, 'Root', 0, 1, '0', '0', '2022-01-11 14:47:09', '2022-01-11 14:47:19', null);
INSERT INTO cms_article_column (id, name, parentId, sort, is_show, del_flag, create_time, modify_time, del_time) VALUES (2, '新手必看', 1, 1, '0', '0', '2022-01-11 14:47:24', '2022-02-06 10:35:40', null);
INSERT INTO cms_article_column (id, name, parentId, sort, is_show, del_flag, create_time, modify_time, del_time) VALUES (3, '我要流量', 1, 2, '0', '0', '2022-02-06 10:35:49', '2022-02-06 10:37:09', null);
INSERT INTO cms_article_column (id, name, parentId, sort, is_show, del_flag, create_time, modify_time, del_time) VALUES (4, '我要赚钱', 1, 3, '0', '0', '2022-02-06 10:36:14', '2022-02-06 10:37:17', null);

truncate f_country;

INSERT INTO `f_country` VALUES (1,'AD','AND','20','Andorra','安道尔','376','0'),(2,'AX','ALA','248','?aland Island','奥兰群岛',NULL,'1'),(3,'AF','AFG','4','Afghanistan','阿富汗','93','0'),(4,'AL','ALB','8','Albania','阿尔巴尼亚','355','0'),(5,'DZ','DZA','12','Algeria','阿尔及利亚','213','0'),(6,'AS','ASM','16','American Samoa','美属萨摩亚',NULL,'1'),(7,'AO','AGO','24','Angola','安哥拉','244','0'),(8,'AI','AIA','660','Anguilla','安圭拉','1264','0'),(9,'AQ','ATA','10','Antarctica','南极洲',NULL,'1'),(10,'AG','ATG','28','Antigua & Barbuda','安提瓜和巴布达','1268','0'),(11,'AR','ARG','32','Argentina','阿根廷','54','0'),(12,'AM','ARM','51','Armenia','亚美尼亚','374','0'),(13,'AW','ABW','533','Aruba','阿鲁巴',NULL,'1'),(14,'AU','AUS','36','Australia','澳大利亚','61','0'),(15,'AT','AUT','40','Austria','奥地利','43','0'),(16,'AZ','AZE','31','Azerbaijan','阿塞拜疆','994','0'),(17,'BH','BHR','48','Bahrain','巴林','973','0'),(18,'BD','BGD','50','Bangladesh','孟加拉','880','0'),(19,'BB','BRB','52','Barbados','巴巴多斯','1246','0'),(20,'BY','BLR','112','Belarus','白俄罗斯','375','0'),(21,'BE','BEL','56','Belgium','比利时','32','0'),(22,'BZ','BLZ','84','Belize','伯利兹','501','0'),(23,'BJ','BEN','204','Benin','贝宁','229','0'),(24,'BM','BMU','60','Bermuda','百慕大','1441','0'),(25,'BT','BTN','64','Bhutan','不丹',NULL,'1'),(26,'BO','BOL','68','Bolivia','玻利维亚','591','0'),(27,'BA','BIH','70','Bosnia & Herzegovina','波黑',NULL,'1'),(28,'BW','BWA','72','Botswana','博茨瓦纳','267','0'),(29,'BV','BVT','74','Bouvet Island','布韦岛',NULL,'1'),(30,'BR','BRA','76','Brazil','巴西','55','0'),(31,'IO','IOT','86','British Indian Ocean Territory','英属印度洋领地',NULL,'1'),(32,'VG','VGB','92','British Virgin Islands','英属维尔京群岛',NULL,'1'),(33,'BN','BRN','96','Brunei','文莱','673','0'),(34,'BG','BGR','100','Bulgaria','保加利亚','359','0'),(35,'BF','BFA','854','Burkina','布基纳法索','226','0'),(36,'BI','BDI','108','Burundi','布隆迪','257','0'),(37,'CI','CIV','384','C?te d’Ivoire','科特迪瓦',NULL,'1'),(38,'KH','KHM','116','Cambodia','柬埔寨','855','0'),(39,'CM','CMR','120','Cameroon','喀麦隆','237','0'),(40,'CA','CAN','124','Canada','加拿大','1','0'),(41,'CV','CPV','132','Cape Verde','佛得角',NULL,'1'),(42,'BQ','BES','535','Caribbean Netherlands','荷兰加勒比区',NULL,'1'),(43,'KY','CYM','136','Cayman Islands','开曼群岛',NULL,'1'),(44,'CF','CAF','140','Central African Republic','中非','236','0'),(45,'TD','TCD','148','Chad','乍得','235','0'),(46,'CL','CHL','152','Chile','智利','56','0'),(47,'CN','CHN','156','China','中国','86','0'),(48,'CX','CXR','162','Christmas Island','圣诞岛',NULL,'1'),(49,'CC','CCK','166','Cocos (Keeling) Islands','科科斯群岛',NULL,'1'),(50,'CO','COL','170','Colombia','哥伦比亚','57','0'),(51,'CK','COK','184','Cook Islands','库克群岛','682','0'),(52,'CR','CRI','188','Costa Rica','哥斯达黎加','506','0'),(53,'HR','HRV','191','Croatia','克罗地亚',NULL,'1'),(54,'CU','CUB','192','Cuba','古巴','53','0'),(55,'CY','CYP','196','Cyprus','塞浦路斯','357','0'),(56,'CZ','CZE','203','Czech Republic','捷克','420','0'),(57,'CD','COD','180','Democratic Republic of the Congo','刚果（金）',NULL,'1'),(58,'DK','DNK','208','Denmark','丹麦','45','0'),(59,'DJ','DJI','262','Djibouti','吉布提','253','0'),(60,'DM','DMA','212','Dominica','多米尼克',NULL,'1'),(61,'DO','DOM','214','Dominican Republic','多米尼加','1890','0'),(62,'EC','ECU','218','Ecuador','厄瓜多尔','593','0'),(63,'EG','EGY','818','Egypt','埃及','20','0'),(64,'SV','SLV','222','El Salvador','萨尔瓦多','503','0'),(65,'GQ','GNQ','226','Equatorial Guinea','赤道几内亚',NULL,'1'),(66,'ER','ERI','232','Eritrea','厄立特里亚',NULL,'1'),(67,'EE','EST','233','Estonia','爱沙尼亚','372','0'),(68,'ET','ETH','231','Ethiopia','埃塞俄比亚','251','0'),(69,'FK','FLK','238','Falkland Islands','马尔维纳斯群岛（ 福克兰）',NULL,'1'),(70,'FO','FRO','234','Faroe Islands','法罗群岛',NULL,'1'),(71,'FM','FSM','583','Federated States of Micronesia','密克罗尼西亚联邦',NULL,'1'),(72,'FJ','FJI','242','Fiji','斐济群岛','679','0'),(73,'FI','FIN','246','Finland','芬兰','358','0'),(74,'FR','FRA','250','France','法国','33','0'),(75,'GF','GUF','254','French Guiana','法属圭亚那','594','0'),(76,'PF','PYF','258','French polynesia','法属波利尼西亚','689','0'),(77,'TF','ATF','260','French Southern Territories','法属南部领地',NULL,'1'),(78,'GA','GAB','266','Gabon','加蓬','241','0'),(79,'GM','GMB','270','Gambia','冈比亚','220','0'),(80,'GE','GEO','268','Georgia','格鲁吉亚','995','0'),(81,'DE','DEU','276','Germany','德国','49','0'),(82,'GH','GHA','288','Ghana','加纳','233','0'),(83,'GI','GIB','292','Gibraltar','直布罗陀','350','0'),(84,'GB','GBR','826','Great Britain (United Kingdom; England)','英国','44','0'),(85,'GR','GRC','300','Greece','希腊','30','0'),(86,'GL','GRL','304','Greenland','格陵兰',NULL,'1'),(87,'GD','GRD','308','Grenada','格林纳达','1809','0'),(88,'GP','GLP','312','Guadeloupe','瓜德罗普',NULL,'1'),(89,'GU','GUM','316','Guam','关岛','1671','0'),(90,'GT','GTM','320','Guatemala','危地马拉','502','0'),(91,'GG','GGY','831','Guernsey','根西岛',NULL,'1'),(92,'GN','GIN','324','Guinea','几内亚','224','0'),(93,'GW','GNB','624','Guinea-Bissau','几内亚比绍',NULL,'1'),(94,'GY','GUY','328','Guyana','圭亚那','592','0'),(95,'HT','HTI','332','Haiti','海地','509','0'),(96,'HM','HMD','334','Heard Island and McDonald Islands','赫德岛和麦克唐纳群岛',NULL,'1'),(97,'HN','HND','340','Honduras','洪都拉斯','504','0'),(98,'HK','HKG','344','Hong Kong','香港','852','0'),(99,'HU','HUN','348','Hungary','匈牙利','36','0'),(100,'IS','ISL','352','Iceland','冰岛','354','0'),(101,'IN','IND','356','India','印度','91','0'),(102,'ID','IDN','360','Indonesia','印尼','62','0'),(103,'IR','IRN','364','Iran','伊朗','98','0'),(104,'IQ','IRQ','368','Iraq','伊拉克','964','0'),(105,'IE','IRL','372','Ireland','爱尔兰','353','0'),(106,'IM','IMN','833','Isle of Man','马恩岛',NULL,'1'),(107,'IL','ISR','376','Israel','以色列','972','0'),(108,'IT','ITA','380','Italy','意大利','39','0'),(109,'JM','JAM','388','Jamaica','牙买加','1876','0'),(110,'JP','JPN','392','Japan','日本','81','0'),(111,'JE','JEY','832','Jersey','泽西岛',NULL,'1'),(112,'JO','JOR','400','Jordan','约旦','962','0'),(113,'KZ','KAZ','398','Kazakhstan','哈萨克斯坦','327','0'),(114,'KE','KEN','404','Kenya','肯尼亚','254','0'),(115,'KI','KIR','296','Kiribati','基里巴斯',NULL,'1'),(116,'KW','KWT','414','Kuwait','科威特','965','0'),(117,'KG','KGZ','417','Kyrgyzstan','吉尔吉斯斯坦','331','0'),(118,'LA','LAO','418','Laos','老挝','856','0'),(119,'LV','LVA','428','Latvia','拉脱维亚','371','0'),(120,'LB','LBN','422','Lebanon','黎巴嫩','961','0'),(121,'LS','LSO','426','Lesotho','莱索托','266','0'),(122,'LR','LBR','430','Liberia','利比里亚','231','0'),(123,'LY','LBY','434','Libya','利比亚','218','0'),(124,'LI','LIE','438','Liechtenstein','列支敦士登','423','0'),(125,'LT','LTU','440','Lithuania','立陶宛','370','0'),(126,'LU','LUX','442','Luxembourg','卢森堡','352','0'),(127,'MO','MAC','446','Macao','澳门','853','0'),(128,'MG','MDG','450','Madagascar','马达加斯加','261','0'),(129,'MW','MWI','454','Malawi','马拉维','265','0'),(130,'MY','MYS','458','Malaysia','马来西亚','60','0'),(131,'MV','MDV','462','Maldives','马尔代夫','960','0'),(132,'ML','MLI','466','Mali','马里','223','0'),(133,'MT','MLT','470','Malta','马耳他','356','0'),(134,'MH','MHL','584','Marshall islands','马绍尔群岛',NULL,'1'),(135,'MQ','MTQ','474','Martinique','马提尼克',NULL,'1'),(136,'MR','MRT','478','Mauritania','毛里塔尼亚',NULL,'1'),(137,'MU','MUS','480','Mauritius','毛里求斯','230','0'),(138,'YT','MYT','175','Mayotte','马约特',NULL,'1'),(139,'MX','MEX','484','Mexico','墨西哥','52','0'),(140,'MD','MDA','498','Moldova','摩尔多瓦','373','0'),(141,'MC','MCO','492','Monaco','摩纳哥','377','0'),(142,'MN','MNG','496','Mongolia','蒙古国 蒙古','976','0'),(143,'ME','MNE','499','Montenegro','黑山',NULL,'1'),(144,'MS','MSR','500','Montserrat','蒙塞拉特岛','1664','0'),(145,'MA','MAR','504','Morocco','摩洛哥','212','0'),(146,'MZ','MOZ','508','Mozambique','莫桑比克','258','0'),(147,'MM','MMR','104','Myanmar (Burma)','缅甸','95','0'),(148,'NA','NAM','516','Namibia','纳米比亚','264','0'),(149,'NR','NRU','520','Nauru','瑙鲁','674','0'),(150,'NP','NPL','524','Nepal','尼泊尔','977','0'),(151,'NL','NLD','528','Netherlands','荷兰','31','0'),(152,'NC','NCL','540','New Caledonia','新喀里多尼亚',NULL,'1'),(153,'NZ','NZL','554','New Zealand','新西兰','64','0'),(154,'NI','NIC','558','Nicaragua','尼加拉瓜','505','0'),(155,'NE','NER','562','Niger','尼日尔','227','0'),(156,'NG','NGA','566','Nigeria','尼日利亚','234','0'),(157,'NU','NIU','570','Niue','纽埃',NULL,'1'),(158,'NF','NFK','574','Norfolk Island','诺福克岛',NULL,'1'),(159,'KP','PRK','408','North Korea','朝鲜 北朝鲜','850','0'),(160,'MP','MNP','580','Northern Mariana Islands','北马里亚纳群岛',NULL,'1'),(161,'NO','NOR','578','Norway','挪威','47','0'),(162,'OM','OMN','512','Oman','阿曼','968','0'),(163,'PK','PAK','586','Pakistan','巴基斯坦','92','0'),(164,'PW','PLW','585','Palau','帕劳',NULL,'1'),(165,'PS','PSE','275','Palestinian territories','巴勒斯坦',NULL,'1'),(166,'PA','PAN','591','Panama','巴拿马','507','0'),(167,'PG','PNG','598','Papua New Guinea','巴布亚新几内亚','675','0'),(168,'PY','PRY','600','Paraguay','巴拉圭','595','0'),(169,'PE','PER','604','Peru','秘鲁','51','0'),(170,'PN','PCN','612','Pitcairn Islands','皮特凯恩群岛',NULL,'1'),(171,'PL','POL','616','Poland','波兰','48','0'),(172,'PT','PRT','620','Portugal','葡萄牙','351','0'),(173,'PR','PRI','630','Puerto Rico','波多黎各','1787','0'),(174,'QA','QAT','634','Qatar','卡塔尔','974','0'),(175,'MK','MKD','807','Republic of Macedonia (FYROM)','马其顿',NULL,'1'),(176,'CG','COG','178','Republic of the Congo','刚果（布）','242','0'),(177,'RE','REU','638','Réunion','留尼汪',NULL,'1'),(178,'RO','ROU','642','Romania','罗马尼亚','40','0'),(179,'RU','RUS','643','Russian Federation','俄罗斯','7','0'),(180,'RW','RWA','646','Rwanda','卢旺达',NULL,'1'),(181,'BL','BLM','652','Saint Barthélemy','圣巴泰勒米岛',NULL,'1'),(182,'MF','MAF','663','Saint Martin (France)','法属圣马丁',NULL,'1'),(183,'PM','SPM','666','Saint-Pierre and Miquelon','圣皮埃尔和密克隆',NULL,'1'),(184,'WS','WSM','882','Samoa','萨摩亚',NULL,'1'),(185,'SM','SMR','674','San Marino','圣马力诺','378','0'),(186,'ST','STP','678','Sao Tome & Principe','圣多美和普林西比','239','0'),(187,'SA','SAU','682','Saudi Arabia','沙特阿拉伯','966','0'),(188,'SN','SEN','686','Senegal','塞内加尔','221','0'),(189,'RS','SRB','688','Serbia','塞尔维亚',NULL,'1'),(190,'SC','SYC','690','Seychelles','塞舌尔','248','0'),(191,'SL','SLE','694','Sierra Leone','塞拉利昂','232','0'),(192,'SG','SGP','702','Singapore','新加坡','65','0'),(193,'SK','SVK','703','Slovakia','斯洛伐克','421','0'),(194,'SI','SVN','705','Slovenia','斯洛文尼亚','386','0'),(195,'SB','SLB','90','Solomon Islands','所罗门群岛','677','0'),(196,'SO','SOM','706','Somalia','索马里','252','0'),(197,'ZA','ZAF','710','South Africa','南非','27','0'),(198,'GS','SGS','239','South Georgia and the South Sandwich Islands','南乔治亚岛和南桑威奇群岛',NULL,'1'),(199,'KR','KOR','410','South Korea','韩国 南朝鲜','82','0'),(200,'SS','SSD','728','South Sudan','南苏丹',NULL,'1'),(201,'ES','ESP','724','Spain','西班牙','34','0'),(202,'LK','LKA','144','Sri Lanka','斯里兰卡','94','0'),(203,'SH','SHN','654','St. Helena & Dependencies','圣赫勒拿',NULL,'1'),(204,'KN','KNA','659','St. Kitts & Nevis','圣基茨和尼维斯',NULL,'1'),(205,'LC','LCA','662','St. Lucia','圣卢西亚','1758','0'),(206,'VC','VCT','670','St. Vincent & the Grenadines','圣文森特和格林纳丁斯','1784','0'),(207,'SD','SDN','729','Sudan','苏丹','249','0'),(208,'SR','SUR','740','Suriname','苏里南','597','0'),(209,'SZ','SWZ','748','Swaziland','斯威士兰','268','0'),(210,'SE','SWE','752','Sweden','瑞典','46','0'),(211,'CH','CHE','756','Switzerland','瑞士','41','0'),(212,'SY','SYR','760','Syria','叙利亚','963','0'),(213,'TW','TWN','158','Taiwan','中华民国（台湾）','886','0'),(214,'TJ','TJK','762','Tajikistan','塔吉克斯坦','992','0'),(215,'TZ','TZA','834','Tanzania','坦桑尼亚','255','0'),(216,'SJ','SJM','744','Template:Country data SJM Svalbard','斯瓦尔巴群岛和 扬马延岛',NULL,'1'),(217,'TH','THA','764','Thailand','泰国','66','0'),(218,'BS','BHS','44','The Bahamas','巴哈马','1242','0'),(219,'KM','COM','174','The Comoros','科摩罗',NULL,'1'),(220,'PH','PHL','608','The Philippines','菲律宾','63','0'),(221,'TL','TLS','626','Timor-Leste (East Timor)','东帝汶',NULL,'1'),(222,'TG','TGO','768','Togo','多哥','228','0'),(223,'TK','TKL','772','Tokelau','托克劳',NULL,'1'),(224,'TO','TON','776','Tonga','汤加','676','0'),(225,'TT','TTO','780','Trinidad & Tobago','特立尼达和多巴哥','1809','0'),(226,'TN','TUN','788','Tunisia','突尼斯','216','0'),(227,'TR','TUR','792','Turkey','土耳其','90','0'),(228,'TM','TKM','795','Turkmenistan','土库曼斯坦','993','0'),(229,'TC','TCA','796','Turks & Caicos Islands','特克斯和凯科斯群岛',NULL,'1'),(230,'TV','TUV','798','Tuvalu','图瓦卢',NULL,'1'),(231,'UG','UGA','800','Uganda','乌干达','256','0'),(232,'UA','UKR','804','Ukraine','乌克兰','380','0'),(233,'AE','ARE','784','United Arab Emirates','阿联酋','971','0'),(234,'UM','UMI','581','United States Minor Outlying Islands','美国本土外小岛屿',NULL,'1'),(235,'US','USA','840','United States of America (USA)','美国','1','0'),(236,'VI','VIR','850','United States Virgin Islands','美属维尔京群岛',NULL,'1'),(237,'UY','URY','858','Uruguay','乌拉圭','598','0'),(238,'UZ','UZB','860','Uzbekistan','乌兹别克斯坦','233','0'),(239,'VU','VUT','548','Vanuatu','瓦努阿图',NULL,'1'),(240,'VA','VAT','336','Vatican City (The Holy See)','梵蒂冈',NULL,'1'),(241,'VE','VEN','862','Venezuela','委内瑞拉','58','0'),(242,'VN','VNM','704','Vietnam','越南','84','0'),(243,'WF','WLF','876','Wallis and Futuna','瓦利斯和富图纳',NULL,'1'),(244,'EH','ESH','732','Western Sahara','西撒哈拉',NULL,'1'),(245,'YE','YEM','887','Yemen','也门','967','0'),(246,'ZM','ZMB','894','Zambia','赞比亚','260','0'),(247,'ZW','ZWE','716','Zimbabwe','津巴布韦','263','0');


DROP TABLE IF EXISTS `ls_email_setting`;
CREATE TABLE `ls_email_setting`
(
    `id`          bigint(20)                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `sender_mail` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发信邮箱',
    `sender_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '发信人',
    `smtp_server` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'SMTP的服务器地址',
    `smtp_port`   int(11)                                                 NOT NULL COMMENT 'SMTP 的端口',
    `username`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '邮箱帐号',
    `password`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '邮箱密码',
    `access_key`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'access key',
    `secret_key`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'secret key',
    `url`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'URL',
    `is_use`    char(1) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '是否启用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '邮箱设置表'
  ROW_FORMAT = Dynamic;

INSERT INTO `ls_email_setting` VALUES (1,'sender@mail.com','sender','smtp.server.com','789','user','pass','','','','0');

DROP TABLE IF EXISTS `ls_sms_setting`;
CREATE TABLE `ls_sms_setting`
(
    `id`                        bigint(20)                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `app_secret`                    varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'AppSecret',
    `url`                       varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT '短信接口地址',
    `sign`                      varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '短信签名',
    `template_id`               varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '模板id',
    `writeoff_template_id`      varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '核销门店订单的模版id',
    `virtual_order_template_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '虚拟订单核销的模版id',
    `app_key`                       varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT NULL COMMENT 'AppKey',
    `audit_template_id`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '社区团购审核结果通知模版id',
    `settlement_template_id`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '社区团购佣金结算模版id',
    `withdraw_template_id`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci           DEFAULT NULL COMMENT '社区团购提现打款模版id',
    `is_use`                    char(1)  default '0'                                             DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '短信接口设置'
  ROW_FORMAT = Dynamic;

INSERT INTO `ls_sms_setting` VALUES (1, '','','','','','','','','','','0');
INSERT INTO `ls_sms_setting` VALUES (2, '','','','','','','','','','','0');

-- 修正flyway的权限问题
GRANT SELECT ON  `performance_schema`.user_variables_by_thread TO uhha@'%';

-- 官方客户数据
update sys_store_user set store_id = 0 where user_id = 1;
INSERT INTO `im_user` VALUES (1, 'Official', 1, '0000001', 'STORE', 'Admin', NULL, NULL, NULL, NULL, '0');

-- 增加用户简介和国家
alter table ums_member
    add column  introduction varchar(255) default null comment '用户介绍' after interest,
    add column  country varchar(32) default null comment '国家' after check_code;

-- 用户地址中增加地址相关自动
alter table ums_member_address
    add column county_id     bigint           null comment '区县id' after city_id;
alter table ums_member_address
    modify column country_id  bigint   comment '国家id' after detail_address;

alter table ls_province
    add country_id bigint           not null comment '该省所属的国家id  对应f_country 中的id';


-- 增加视频带货记录推广人用户id
alter table oms_order
    add column distribute_user_id bigint(20) default null comment '视频带货记录推广人用户id' after logistics_code;

alter table oms_shopping_cart
    add column distribute_user_id bigint(20) default null comment '视频带货记录推广人用户id' after spu_id;

INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('佣金类型', 'commission_type', '0', 'admin', '2022-01-07 12:42:10', '', null, '0 收入 1支出');
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (1, '收入', '0', 'commission_type', NULL, NULL, 'N', '0', 'admin', '2020-07-25 08:44:18', '', NULL, NULL);
INSERT `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (2, '支出', '1', 'commission_type', NULL, NULL, 'N', '0', 'admin', '2020-07-25 08:44:29', '', NULL, NULL);

-- 添加带货佣金比例
alter table pms_goods
    add column `dropship_commission_rate` decimal(3,2) DEFAULT '0.00' COMMENT '带货佣金比例';

alter table pms_sku
    add column `dropship_commission_rate` decimal(3,2) DEFAULT '0.00' COMMENT '带货佣金比例';

alter table oms_order_sku
    add column `dropship_commission_rate` decimal(3,2) DEFAULT '0.00' COMMENT '带货佣金比例';

-- 账户池
DROP TABLE IF EXISTS `account_code`;
CREATE TABLE `account_code` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `status` int(11) DEFAULT NULL comment '占用状态 0 未被占用 1已占用',
                                `currency` int(11) NOT NULL DEFAULT 1 comment '1 USD 2 CNY',
                                `account_no` varchar(32) NOT NULL comment '账号',
                                `account_type` varchar(16) NOT NULL comment '账号类型',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `mobile` (`status`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 0
  CHARACTER SET = utf8mb4
  CHARSET=utf8mb4_general_ci COMMENT = '账户号码池'
  ROW_FORMAT = Dynamic;


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账户号码池', '2439', '1', 'code', 'settlement/code/index', 1, 0, 'C', '0', '0', 'settlement:code:list', '#', 'admin', sysdate(), '', null, '账户号码池菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账户号码池查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'settlement:code:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账户号码池新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'settlement:code:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账户号码池修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'settlement:code:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账户号码池删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'settlement:code:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('账户号码池导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'settlement:code:export',       '#', 'admin', sysdate(), '', null, '');

-- 初始用户数据
-- 店铺
INSERT INTO t_store_info (id, store_name, status, dept_id, company_name, company_address, company_phone, company_email, contact_person, contact_phone, legal_person, card_no, card_pic, bus_licenec, bus_licenec_pic, business_scope, org_pic, tax_pic, bank_user_name, bank_account, bank_name, bank_number, bank_address, bank_pic, billing_cycle, ismerge, del_flag, service_qq, reason, ave_score, effective_time, create_time, modify_time, del_time, type, longitude, latitude, business_time, bus_routes, avatar_picture, province_id, city_id, district_id, company_detail_address, merchant_type, platform_fee_rate)
VALUES (0, '商城自营', '2', null, '商城自营', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', '0', null, null, null, null, null, null, null, '0', null, null, null, null, null, 32, 32, 2995, null, '0', 0);

-- 店铺管理员用户
INSERT INTO sys_store_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, areacode, is_mobile_verification, is_email_verification, is_real_name_screened, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark, store_id)
VALUES (1, 103, 'admin', 'Uhha', '00', 'ry@163.com', '15888888888', null, null, null, null, '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '182.138.169.116', '2022-02-23 20:52:28', 'admin', '2018-03-16 11:33:00', '耐克（NIKE）京东自营专区', '2022-02-23 12:52:27', '管理员', 0);

-- 自营店铺的及时通讯用户
INSERT INTO im_user (id, nick_name, user_id, user_code, type, create_by, create_time, update_by, update_time, remarks, del_flag)
VALUES (1492672392160501762, 'Uhha', 1, '9119ad1c59644139ad110a0f51af62f6', 'SYSTEM', 'Uhha', '2022-02-13 09:30:05', null, '2022-02-13 09:30:05', null, '0');
INSERT INTO im_user (id, nick_name, user_id, user_code, type, create_by, create_time, update_by, update_time, remarks, del_flag)
VALUES (1492675845091926017, 'Uhha', 1, '35ccf327358a489da88e740ed4d7fbc9', 'STORE', 'Uhha', '2022-02-13 09:43:49', null, '2022-02-13 09:43:49', null, '0');

-- 结算记录
alter table settlement_record
    add `record_type`        char(2) CHARACTER SET utf8 COLLATE utf8_general_ci      DEFAULT NULL COMMENT '账单记录类型',
    add `related_account_code`        varchar(64)          not null;

alter table oms_billing_records
    modify `record_type`      char(2) CHARACTER SET utf8 COLLATE utf8_general_ci      DEFAULT NULL COMMENT '账单记录类型';

--
delete from `sys_dict_data` where dict_type ='bill_records_type';

INSERT INTO `sys_dict_data`
VALUES (124, 0, '确认收货', '1', 'bill_records_type', NULL, NULL, 'N', '0', 'admin', '2020-07-24 11:15:13', '', NULL,
        '账单记录类型 1 确认收货 2 退款订单 3 退货订单佣金  4 订单关闭（只支付定金） 5 推广订单提成 6 退货订单');
INSERT INTO `sys_dict_data`
VALUES (125, 1, '退款订单', '2', 'bill_records_type', NULL, NULL, 'N', '0', 'admin', '2020-07-24 11:15:13', '', NULL,
        '账单记录类型 1 确认收货 2 退款订单 3 退货订单佣金  4 订单关闭（只支付定金） 5 推广订单提成 6 退货订单');
INSERT INTO `sys_dict_data`
VALUES (126, 2, '退货订单佣金', '3', 'bill_records_type', NULL, NULL, 'N', '0', 'admin', '2020-07-24 11:15:13', '', NULL,
        '账单记录类型 1 确认收货 2 退款订单 3 退货订单佣金  4 订单关闭（只支付定金） 5 推广订单提成 6 退货订单');
INSERT INTO `sys_dict_data`
VALUES (127, 3, '订单关闭（只支付定金）', '4', 'bill_records_type', NULL, NULL, 'N', '0', 'admin', '2020-07-24 11:15:13', '', NULL,
        '账单记录类型 1 确认收货 2 退款订单 3 退货订单佣金  4 订单关闭（只支付定金） 5 推广订单提成 6 退货订单');
INSERT INTO `sys_dict_data`
VALUES (128, 4, '推广订单提成L1', '5', 'bill_records_type', NULL, NULL, 'N', '0', 'admin', '2020-07-24 11:15:13', '', NULL,
           '账单记录类型 1 确认收货 2 退款订单 3 退货订单佣金  4 订单关闭（只支付定金） 5 推广订单提成 6 退货订单');
INSERT INTO `sys_dict_data`
VALUES (129, 5, '推广订单提成L2', '6', 'bill_records_type', NULL, NULL, 'N', '0', 'admin', '2020-07-24 11:15:13', '', NULL,
        '账单记录类型 1 确认收货 2 退款订单 3 退货订单佣金  4 订单关闭（只支付定金） 5 推广订单提成 6 退货订单');
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('6', '退货订单', '7', 'bill_records_type', null, 'success', 'N', '0', 'admin', '2022-01-07 12:44:24', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('7', '带货佣金用户部分', '8', 'bill_records_type', null, 'success', 'N', '0', 'admin', '2022-01-07 12:44:24', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('8', '带货佣金店铺部分', '9', 'bill_records_type', null, 'success', 'N', '0', 'admin', '2022-01-07 12:44:24', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('9', '带货佣金退货', '10', 'bill_records_type', null, 'success', 'N', '0', 'admin', '2022-01-07 12:44:24', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('10', '确认收货平台收取手续费', '11', 'bill_records_type', null, 'success', 'N', '0', 'admin', '2022-01-07 12:44:24', '', null, null);

-- system menu
UPDATE sys_menu t SET t.menu_name = 'system' WHERE t.menu_id = 1;

UPDATE sys_menu t SET t.menu_name = 'returnAudit' WHERE t.menu_id = 2344;

UPDATE sys_menu t SET t.menu_name = 'orderConfig' WHERE t.menu_id = 2155;

UPDATE sys_menu t SET t.menu_name = 'specMgn' WHERE t.menu_id = 2019;

UPDATE sys_menu t SET t.menu_name = 'modifyLogisticTemplate' WHERE t.menu_id = 2299;

UPDATE sys_menu t SET t.menu_name = 'storeData' WHERE t.menu_id = 2181;

UPDATE sys_menu t SET t.menu_name = 'refundAudit' WHERE t.menu_id = 2339;

UPDATE sys_menu t SET t.menu_name = 'logisticMgn' WHERE t.menu_id = 2205;

UPDATE sys_menu t SET t.menu_name = 'sysMonitor' WHERE t.menu_id = 2;

UPDATE sys_menu t SET t.menu_name = 'storeAudit' WHERE t.menu_id = 2129;

UPDATE sys_menu t SET t.menu_name = 'storeCommunityOrderList' WHERE t.menu_id = 2167;

UPDATE sys_menu t SET t.menu_name = 'refundAudit' WHERE t.menu_id = 2343;

UPDATE sys_menu t SET t.menu_name = 'storeAudit' WHERE t.menu_id = 2180;

UPDATE sys_menu t SET t.menu_name = 'helpList' WHERE t.menu_id = 2310;

UPDATE sys_menu t SET t.menu_name = 'country' WHERE t.menu_id = 2458;

UPDATE sys_menu t SET t.menu_name = 'storeProduct' WHERE t.menu_id = 2168;

UPDATE sys_menu t SET t.menu_name = 'orderData' WHERE t.menu_id = 2203;

UPDATE sys_menu t SET t.menu_name = 'billing' WHERE t.menu_id = 2044;

UPDATE sys_menu t SET t.menu_name = 'pointConfig' WHERE t.menu_id = 2436;

UPDATE sys_menu t SET t.menu_name = 'nftItem' WHERE t.menu_id = 2482;

UPDATE sys_menu t SET t.menu_name = 'addLogisticTemplate' WHERE t.menu_id = 2298;

UPDATE sys_menu t SET t.menu_name = 'storeProductMgn' WHERE t.menu_id = 2179;

UPDATE sys_menu t SET t.menu_name = 'quartz' WHERE t.menu_id = 110;

UPDATE sys_menu t SET t.menu_name = 'helpCenter' WHERE t.menu_id = 2309;

UPDATE sys_menu t SET t.menu_name = 'cryptSysWallet' WHERE t.menu_id = 2416;

UPDATE sys_menu t SET t.menu_name = 'formCreator' WHERE t.menu_id = 113;

UPDATE sys_menu t SET t.menu_name = 'onlineUser' WHERE t.menu_id = 109;

UPDATE sys_menu t SET t.menu_name = 'nftOrder' WHERE t.menu_id = 2494;

UPDATE sys_menu t SET t.menu_name = 'dept' WHERE t.menu_id = 103;

UPDATE sys_menu t SET t.menu_name = 'commission' WHERE t.menu_id = 2050;

UPDATE sys_menu t SET t.menu_name = 'productMgn' WHERE t.menu_id = 2006;

UPDATE sys_menu t SET t.menu_name = 'memberMgn' WHERE t.menu_id = 2079;

UPDATE sys_menu t SET t.menu_name = 'productImport' WHERE t.menu_id = 2424;

UPDATE sys_menu t SET t.menu_name = 'settlementRecord' WHERE t.menu_id = 2446;

UPDATE sys_menu t SET t.menu_name = 'diagnostic' WHERE t.menu_id = 2422;

UPDATE sys_menu t SET t.menu_name = 'smsInterface' WHERE t.menu_id = 2149;

UPDATE sys_menu t SET t.menu_name = 'modifyArticle' WHERE t.menu_id = 2331;

UPDATE sys_menu t SET t.menu_name = 'nextLevelMember' WHERE t.menu_id = 2294;

UPDATE sys_menu t SET t.menu_name = 'logisticTemplate' WHERE t.menu_id = 2062;

UPDATE sys_menu t SET t.menu_name = 'official' WHERE t.menu_id = 4;

UPDATE sys_menu t SET t.menu_name = 'articleList' WHERE t.menu_id = 2320;

UPDATE sys_menu t SET t.menu_name = 'storeReturnRefundOrderList' WHERE t.menu_id = 2166;

UPDATE sys_menu t SET t.menu_name = 'auditSwitch' WHERE t.menu_id = 2164;

UPDATE sys_menu t SET t.menu_name = 'helpCate' WHERE t.menu_id = 2315;

UPDATE sys_menu t SET t.menu_name = 'menu' WHERE t.menu_id = 102;

UPDATE sys_menu t SET t.menu_name = 'withdrawMgn' WHERE t.menu_id = 2292;

UPDATE sys_menu t SET t.menu_name = 'recommendBrand' WHERE t.menu_id = 2273;

UPDATE sys_menu t SET t.menu_name = 'memberAddress' WHERE t.menu_id = 2098;

UPDATE sys_menu t SET t.menu_name = 'orderMgn' WHERE t.menu_id = 2204;

UPDATE sys_menu t SET t.menu_name = 'dict' WHERE t.menu_id = 105;

UPDATE sys_menu t SET t.menu_name = 'user' WHERE t.menu_id = 100;

UPDATE sys_menu t SET t.menu_name = 'serviceMonitor' WHERE t.menu_id = 112;

UPDATE sys_menu t SET t.menu_name = 'productAttr' WHERE t.menu_id = 2025;

UPDATE sys_menu t SET t.menu_name = 'accountPool' WHERE t.menu_id = 2513;

UPDATE sys_menu t SET t.menu_name = 'productAudit' WHERE t.menu_id = 2306;

UPDATE sys_menu t SET t.menu_name = 'cryptoDeposit' WHERE t.menu_id = 2410;

UPDATE sys_menu t SET t.menu_name = 'memberWithdraw' WHERE t.menu_id = 2452;

UPDATE sys_menu t SET t.menu_name = 'pointMall' WHERE t.menu_id = 2212;

UPDATE sys_menu t SET t.menu_name = 'orderList' WHERE t.menu_id = 2032;

UPDATE sys_menu t SET t.menu_name = 'opLog' WHERE t.menu_id = 500;

UPDATE sys_menu t SET t.menu_name = 'basicConfig' WHERE t.menu_id = 2178;

UPDATE sys_menu t SET t.menu_name = 'returnRefund' WHERE t.menu_id = 2038;

UPDATE sys_menu t SET t.menu_name = 'memberList' WHERE t.menu_id = 2080;

UPDATE sys_menu t SET t.menu_name = 'hotProduct' WHERE t.menu_id = 2270;

UPDATE sys_menu t SET t.menu_name = 'OSSConfig' WHERE t.menu_id = 2177;

UPDATE sys_menu t SET t.menu_name = 'storeMgn' WHERE t.menu_id = 2128;

UPDATE sys_menu t SET t.menu_name = 'crypto' WHERE t.menu_id = 2391;

UPDATE sys_menu t SET t.menu_name = 'videoWatchRecord' WHERE t.menu_id = 2368;

UPDATE sys_menu t SET t.menu_name = 'memberWithdraw' WHERE t.menu_id = 2122;

UPDATE sys_menu t SET t.menu_name = 'ossRecords' WHERE t.menu_id = 2379;

UPDATE sys_menu t SET t.menu_name = 'customerSupport' WHERE t.menu_id = 2283;

UPDATE sys_menu t SET t.menu_name = 'modifyHelp' WHERE t.menu_id = 2333;

UPDATE sys_menu t SET t.menu_name = 'contract' WHERE t.menu_id = 2464;

UPDATE sys_menu t SET t.menu_name = 'commissionConfig' WHERE t.menu_id = 2290;

UPDATE sys_menu t SET t.menu_name = 'commissionConfig' WHERE t.menu_id = 2295;

UPDATE sys_menu t SET t.menu_name = 'nftContract' WHERE t.menu_id = 2476;

UPDATE sys_menu t SET t.menu_name = 'nftAttention' WHERE t.menu_id = 2488;

UPDATE sys_menu t SET t.menu_name = 'memberBrowseHistory' WHERE t.menu_id = 2092;

UPDATE sys_menu t SET t.menu_name = 'marketingMgn' WHERE t.menu_id = 2207;

UPDATE sys_menu t SET t.menu_name = 'recommendTopic' WHERE t.menu_id = 2264;

UPDATE sys_menu t SET t.menu_name = 'validateTemplate' WHERE t.menu_id = 2507;

UPDATE sys_menu t SET t.menu_name = 'productAdd' WHERE t.menu_id = 2015;

UPDATE sys_menu t SET t.menu_name = 'cacheMonitor' WHERE t.menu_id = 116;

UPDATE sys_menu t SET t.menu_name = 'storeMemberData' WHERE t.menu_id = 2183;

UPDATE sys_menu t SET t.menu_name = 'settlement' WHERE t.menu_id = 2439;

UPDATE sys_menu t SET t.menu_name = 'platformFeeConfig' WHERE t.menu_id = 2501;

UPDATE sys_menu t SET t.menu_name = 'codeGenerator' WHERE t.menu_id = 114;

UPDATE sys_menu t SET t.menu_name = 'categoryMgn' WHERE t.menu_id = 2218;

UPDATE sys_menu t SET t.menu_name = 'cryptoWithdraw' WHERE t.menu_id = 2404;

UPDATE sys_menu t SET t.menu_name = 'settlementDetail' WHERE t.menu_id = 2349;

UPDATE sys_menu t SET t.menu_name = 'customizeBrand' WHERE t.menu_id = 2161;

UPDATE sys_menu t SET t.menu_name = 'businessParams' WHERE t.menu_id = 2385;

UPDATE sys_menu t SET t.menu_name = 'productMarketing' WHERE t.menu_id = 2211;

UPDATE sys_menu t SET t.menu_name = 'message' WHERE t.menu_id = 2374;

UPDATE sys_menu t SET t.menu_name = 'brandAudit' WHERE t.menu_id = 2160;

UPDATE sys_menu t SET t.menu_name = 'orderMgn' WHERE t.menu_id = 2031;

UPDATE sys_menu t SET t.menu_name = 'settlement' WHERE t.menu_id = 2347;

UPDATE sys_menu t SET t.menu_name = 'dataMonitor' WHERE t.menu_id = 111;

UPDATE sys_menu t SET t.menu_name = 'log' WHERE t.menu_id = 108;

UPDATE sys_menu t SET t.menu_name = 'videoComment' WHERE t.menu_id = 2362;

UPDATE sys_menu t SET t.menu_name = 'productAtt' WHERE t.menu_id = 2196;

UPDATE sys_menu t SET t.menu_name = 'distributionOrder' WHERE t.menu_id = 2291;

UPDATE sys_menu t SET t.menu_name = 'distributionOrder' WHERE t.menu_id = 2296;

UPDATE sys_menu t SET t.menu_name = 'chat' WHERE t.menu_id = 2500;

UPDATE sys_menu t SET t.menu_name = 'logisticCompany' WHERE t.menu_id = 2056;

UPDATE sys_menu t SET t.menu_name = 'memberDeposit' WHERE t.menu_id = 2116;

UPDATE sys_menu t SET t.menu_name = 'distributionMgn' WHERE t.menu_id = 2289;

UPDATE sys_menu t SET t.menu_name = 'addArticle' WHERE t.menu_id = 2330;

UPDATE sys_menu t SET t.menu_name = 'memberPoints' WHERE t.menu_id = 2110;

UPDATE sys_menu t SET t.menu_name = 'distributionMember' WHERE t.menu_id = 2293;

UPDATE sys_menu t SET t.menu_name = 'position' WHERE t.menu_id = 104;

UPDATE sys_menu t SET t.menu_name = 'productData' WHERE t.menu_id = 2202;

UPDATE sys_menu t SET t.menu_name = 'notice' WHERE t.menu_id = 107;

UPDATE sys_menu t SET t.menu_name = 'skuComment' WHERE t.menu_id = 2190;

UPDATE sys_menu t SET t.menu_name = 'returnAudit' WHERE t.menu_id = 2340;

UPDATE sys_menu t SET t.menu_name = 'productAudit' WHERE t.menu_id = 2163;

UPDATE sys_menu t SET t.menu_name = 'tools' WHERE t.menu_id = 3;

UPDATE sys_menu t SET t.menu_name = 'video' WHERE t.menu_id = 5;

UPDATE sys_menu t SET t.menu_name = 'memberLevel' WHERE t.menu_id = 2086;

UPDATE sys_menu t SET t.menu_name = 'payslipt' WHERE t.menu_id = 2073;

UPDATE sys_menu t SET t.menu_name = 'articleMgn' WHERE t.menu_id = 2308;

UPDATE sys_menu t SET t.menu_name = 'marketingConfig' WHERE t.menu_id = 2208;

UPDATE sys_menu t SET t.menu_name = 'productSrcConfig' WHERE t.menu_id = 2430;

UPDATE sys_menu t SET t.menu_name = 'video' WHERE t.menu_id = 2356;

UPDATE sys_menu t SET t.menu_name = 'storeMarketing' WHERE t.menu_id = 2209;

UPDATE sys_menu t SET t.menu_name = 'role' WHERE t.menu_id = 101;

UPDATE sys_menu t SET t.menu_name = 'addHelp' WHERE t.menu_id = 2332;

UPDATE sys_menu t SET t.menu_name = 'cryptoUserWallet' WHERE t.menu_id = 2398;

UPDATE sys_menu t SET t.menu_name = 'frontPageConfig' WHERE t.menu_id = 2206;

UPDATE sys_menu t SET t.menu_name = 'storeOrderDetail' WHERE t.menu_id = 2345;

UPDATE sys_menu t SET t.menu_name = 'storeMemberMgn' WHERE t.menu_id = 2182;

UPDATE sys_menu t SET t.menu_name = 'articleCate' WHERE t.menu_id = 2325;

UPDATE sys_menu t SET t.menu_name = 'ad' WHERE t.menu_id = 2276;

UPDATE sys_menu t SET t.menu_name = 'operation' WHERE t.menu_id = 2282;

UPDATE sys_menu t SET t.menu_name = 'settlementAccount' WHERE t.menu_id = 2440;

UPDATE sys_menu t SET t.menu_name = 'emailConfig' WHERE t.menu_id = 2138;

UPDATE sys_menu t SET t.menu_name = 'payConfig' WHERE t.menu_id = 2143;

UPDATE sys_menu t SET t.menu_name = 'storeProductConfig' WHERE t.menu_id = 2169;

UPDATE sys_menu t SET t.menu_name = 'shoppingcart' WHERE t.menu_id = 2184;

UPDATE sys_menu t SET t.menu_name = 'category' WHERE t.menu_id = 2171;

UPDATE sys_menu t SET t.menu_name = 'memberSeckillList' WHERE t.menu_id = 2104;

UPDATE sys_menu t SET t.menu_name = 'cryptoTypes' WHERE t.menu_id = 2392;

UPDATE sys_menu t SET t.menu_name = 'orderDetail' WHERE t.menu_id = 2334;

UPDATE sys_menu t SET t.menu_name = 'platformMarketing' WHERE t.menu_id = 2210;

UPDATE sys_menu t SET t.menu_name = 'productList' WHERE t.menu_id = 2013;

UPDATE sys_menu t SET t.menu_name = 'brand' WHERE t.menu_id = 2007;

UPDATE sys_menu t SET t.menu_name = 'storeConfig' WHERE t.menu_id = 2135;

UPDATE sys_menu t SET t.menu_name = 'storeOrderList' WHERE t.menu_id = 2165;

UPDATE sys_menu t SET t.menu_name = 'distributionMember' WHERE t.menu_id = 2297;

UPDATE sys_menu t SET t.menu_name = 'recommendProduct' WHERE t.menu_id = 2267;

UPDATE sys_menu t SET t.menu_name = 'params' WHERE t.menu_id = 106;

UPDATE sys_menu t SET t.menu_name = 'loginLog' WHERE t.menu_id = 501;

UPDATE sys_menu t SET t.menu_name = 'storeSettlement' WHERE t.menu_id = 2348;

UPDATE sys_menu t SET t.menu_name = 'interfaces' WHERE t.menu_id = 115;

-- store menu
UPDATE sys_store_menu t
SET t.menu_name = 'logisticCompany'
WHERE t.menu_id = 2056;

UPDATE sys_store_menu t
SET t.menu_name = 'memberPoints'
WHERE t.menu_id = 2110;

UPDATE sys_store_menu t
SET t.menu_name = 'payslipt'
WHERE t.menu_id = 2073;

UPDATE sys_store_menu t
SET t.menu_name = 'dept'
WHERE t.menu_id = 103;

UPDATE sys_store_menu t
SET t.menu_name = 'memberAddress'
WHERE t.menu_id = 2098;

UPDATE sys_store_menu t
SET t.menu_name = 'position'
WHERE t.menu_id = 104;

UPDATE sys_store_menu t
SET t.menu_name = 'system'
WHERE t.menu_id = 1;

UPDATE sys_store_menu t
SET t.menu_name = 'billing'
WHERE t.menu_id = 2044;

UPDATE sys_store_menu t
SET t.menu_name = 'memberBrowseHistory'
WHERE t.menu_id = 2092;

UPDATE sys_store_menu t
SET t.menu_name = 'params'
WHERE t.menu_id = 106;

UPDATE sys_store_menu t
SET t.menu_name = 'productMgn'
WHERE t.menu_id = 2006;

UPDATE sys_store_menu t
SET t.menu_name = 'memberDeposit'
WHERE t.menu_id = 2116;

UPDATE sys_store_menu t
SET t.menu_name = 'orderList'
WHERE t.menu_id = 2032;

UPDATE sys_store_menu t
SET t.menu_name = 'memberMgn'
WHERE t.menu_id = 2080;

UPDATE sys_store_menu t
SET t.menu_name = 'brand'
WHERE t.menu_id = 2007;

UPDATE sys_store_menu t
SET t.menu_name = 'commission'
WHERE t.menu_id = 2050;

UPDATE sys_store_menu t
SET t.menu_name = 'memberPanicRecord'
WHERE t.menu_id = 2104;

UPDATE sys_store_menu t
SET t.menu_name = 'returnRefund'
WHERE t.menu_id = 2038;

UPDATE sys_store_menu t
SET t.menu_name = 'logisticTemplate'
WHERE t.menu_id = 2062;

UPDATE sys_store_menu t
SET t.menu_name = 'orderMgn'
WHERE t.menu_id = 2031;

UPDATE sys_store_menu t
SET t.menu_name = 'user'
WHERE t.menu_id = 100;

UPDATE sys_store_menu t
SET t.menu_name = 'dict'
WHERE t.menu_id = 105;

UPDATE sys_store_menu t
SET t.menu_name = 'productAdd'
WHERE t.menu_id = 2015;

UPDATE sys_store_menu t
SET t.menu_name = 'productList'
WHERE t.menu_id = 2013;

UPDATE sys_store_menu t
SET t.menu_name = 'memberWithdraw'
WHERE t.menu_id = 2122;

UPDATE sys_store_menu t
SET t.menu_name = 'role'
WHERE t.menu_id = 101;

UPDATE sys_store_menu t
SET t.menu_name = 'menu'
WHERE t.menu_id = 102;

UPDATE sys_store_menu t
SET t.menu_name = 'recommendProduct'
WHERE t.menu_id = 2267;

UPDATE sys_store_menu t
SET t.menu_name = 'recommendBrand'
WHERE t.menu_id = 2273;

UPDATE sys_store_menu t
SET t.menu_name = 'ad'
WHERE t.menu_id = 2276;

UPDATE sys_store_menu t
SET t.menu_name = 'operation'
WHERE t.menu_id = 2282;

UPDATE sys_store_menu t
SET t.menu_name = 'hotProduct'
WHERE t.menu_id = 2270;

UPDATE sys_store_menu t
SET t.menu_name = 'storeConfig1'
WHERE t.menu_id = 2206;

UPDATE sys_store_menu t
SET t.menu_name = 'orderMgn'
WHERE t.menu_id = 2204;

UPDATE sys_store_menu t
SET t.menu_name = 'orderList'
WHERE t.menu_id = 2165;

UPDATE sys_store_menu t
SET t.menu_name = 'orderData'
WHERE t.menu_id = 2203;

UPDATE sys_store_menu t
SET t.menu_name = 'productList'
WHERE t.menu_id = 2168;

UPDATE sys_store_menu t
SET t.menu_name = 'storeReturnRefundOrderList'
WHERE t.menu_id = 2166;

UPDATE sys_store_menu t
SET t.menu_name = 'productMgn'
WHERE t.menu_id = 2179;

UPDATE sys_store_menu t
SET t.menu_name = 'logisticMgn'
WHERE t.menu_id = 2205;

UPDATE sys_store_menu t
SET t.menu_name = 'productImport'
WHERE t.menu_id = 2362;

UPDATE sys_store_menu t
SET t.menu_name = 'storeProductConfig'
WHERE t.menu_id = 2169;

UPDATE sys_store_menu t
SET t.menu_name = 'productData'
WHERE t.menu_id = 2202;

UPDATE sys_store_menu t
SET t.menu_name = 'skuComment'
WHERE t.menu_id = 2190;

UPDATE sys_store_menu t
SET t.menu_name = 'productAtt'
WHERE t.menu_id = 2196;



-- ----------------------------
-- Table structure for pv
-- ----------------------------
CREATE TABLE `pv` (
                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                      `uid` varchar(50) DEFAULT NULL COMMENT '用户ID',
                      `module` varchar(50) DEFAULT NULL COMMENT '模块',
                      `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
                      `referer` varchar(255) DEFAULT NULL COMMENT 'referer',
                      `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
                      `page_id` varchar(255) DEFAULT NULL COMMENT '页面内容ID',
                      `url` varchar(255) DEFAULT NULL,
                      `device_type` varchar(50) DEFAULT NULL,
                      `time_zone` varchar(10) DEFAULT NULL COMMENT '时区',
                      `ip` varchar(20) DEFAULT NULL COMMENT 'ip地址',
                      `location` varchar(255) DEFAULT NULL COMMENT '地址',
                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                      `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=utf8mb4;

alter table pv
    comment 'PV表';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PV', '5', '8', 'pv', 'system/pv/index', 1, 0, 'C', '0', '0', 'system:pv:list', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', 'PV菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PV查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:pv:query',        '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PV新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:pv:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PV修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:pv:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PV删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:pv:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('PV导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:pv:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 投诉部分
INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('投诉状态', 'complaint_status', '0', 'admin', '2022-01-11 12:34:22', '', null, '状态 0未处理 1已处理 默认为0');
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('1', '未处理', '0', 'complaint_status', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('2', '已处理', '1', 'complaint_status', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);

-- 1功能建议 2BUG反馈 3业务咨询

INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('投诉类型', 'complaint_type', '0', 'admin', '2022-01-11 12:34:22', '', null, '1功能建议 2BUG反馈 3业务咨询');
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('1', '功能建议', '1', 'complaint_type', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('2', 'BUG反馈', '2', 'complaint_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('3', '已处理', '3', 'complaint_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);

-- 订单投诉类型  1 产品相关 2价格相关 3服务相关 4物流相关 5售后相关 6财务相关 7活动相关 8网站相关 9 预约相关 10其他方面
INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('订单投诉状态', 'complaint_order_type', '0', 'admin', '2022-01-11 12:34:22', '', null, '');
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('1', '产品相关', '1', 'complaint_order_type', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('2', '价格相关', '2', 'complaint_order_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('3', '服务相关', '3', 'complaint_order_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('4', '物流相关', '4', 'complaint_order_type', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('5', '售后相关', '5', 'complaint_order_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('6', '财务相关', '6', 'complaint_order_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('7', '活动相关', '7', 'complaint_order_type', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('8', '网站相关', '8', 'complaint_order_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('9', '预约相关', '9', 'complaint_order_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('10', '其他方面', '10', 'complaint_order_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('投诉', '2282', '8', 'complaint', 'operate/complaint/index', 1, 0, 'C', '0', '0', 'operate:complaint:list', 'documentation', 'admin', '2018-03-01', 'ry', '2018-03-01', '投诉菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('投诉查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'operate:complaint:query',        '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('投诉修改', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'operate:complaint:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('投诉导出', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'operate:complaint:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单投诉', '2282', '9', 'complaintorder', 'operate/complaintorder/index', 1, 0, 'C', '0', '0', 'operate:complaintOrder:list', 'documentation', 'admin', '2018-03-01', 'ry', '2018-03-01', '投诉菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单投诉查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'operate:complaintOrder:query',        '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单投诉修改', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'operate:complaintOrder:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('订单投诉导出', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'operate:complaintOrder:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 实名认证

-- 实名认证类型
INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('实名认证类型', 'relename_type', '0', 'admin', '2022-01-11 12:34:22', '', null, '');
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('1', '身份证', '0', 'relename_type', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('2', '护照', '1', 'relename_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('3', '通行证', '2', 'relename_type', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);

-- 实名认证
alter table ums_member
    drop relename;
alter table ums_member
    add column `firstname` varchar(64)  CHARACTER SET utf8 COLLATE utf8_general_ci      DEFAULT NULL COMMENT '名',
    add column `lastname`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci      DEFAULT NULL COMMENT '姓',
    add column `nationality`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci      DEFAULT NULL COMMENT '国籍',
    add column `nationality_id`      int COMMENT '国家id',
    add column `idtype`        varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci      DEFAULT NULL COMMENT '实名认证类型',
    add column  `certificate1`                  varchar(512) charset utf8             null comment '实名认证证明材料1地址',
    add column  `certificate2`                  varchar(512) charset utf8             null comment '实名认证证明材料2地址',
    add column  `certificate3`                  varchar(512) charset utf8             null comment '实名认证证明材料3地址',
    add column  `certificate4`                  varchar(512) charset utf8             null comment '实名认证证明材料4地址'
    ;
alter table ums_member
    drop column `is_real_name_screened`;
alter table ums_member
    add column `realname_screen_status` varchar(2) charset utf8 default '0'  null comment '实名认证状态   0 已提交 1 验证通过  2 已拒绝 默认0';


--
-- Table structure for table `f_user_bankinfo`
--

DROP TABLE IF EXISTS `f_user_bankinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `f_user_bankinfo` (
                                   `fId` int(11) NOT NULL AUTO_INCREMENT,
                                   `fuid` bigint(20) DEFAULT NULL,
                                   `fname` varchar(128) DEFAULT NULL,
                                   `fbanknumber` varchar(128) DEFAULT NULL,
                                   `fbankid` bigint(20) DEFAULT NULL,
                                   `fbanktype` int(11) DEFAULT NULL,
                                   `fcreatetime` datetime DEFAULT NULL,
                                   `fstatus` int(11) DEFAULT NULL,
                                   `version` int(11) DEFAULT NULL,
                                   `init` tinyint(1) DEFAULT NULL,
                                   `faddress` varchar(200) DEFAULT NULL,
                                   `frealname` varchar(64) DEFAULT NULL,
                                   `countryid` bigint(20) DEFAULT NULL,
                                   `fcountry` varchar(64) DEFAULT NULL,
                                   `fprov` varchar(64) DEFAULT NULL,
                                   `fcity` varchar(64) DEFAULT NULL,
                                   `ftype` int(11) DEFAULT NULL,
                                   `fdist` varchar(255) DEFAULT NULL,
                                   PRIMARY KEY (`fId`),
                                   KEY `FK_Relationship_19` (`fuid`),
                                   KEY `fBankType` (`fbanktype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


-- 添加银行配置信息
DROP TABLE IF EXISTS `ls_bank_setting`;
CREATE TABLE `ls_bank_setting`
(
    `id`             bigint(20)                             NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `country_id`             bigint(20)                     NOT NULL  COMMENT '服务国家id',
    `country`       varchar(64)                    NOT NULL  COMMENT '服务国家',
    `bank_name`    varchar(64)                              NOT NULL COMMENT '银行名称',
    `swift_code`    varchar(64)                              NOT NULL COMMENT 'swift代码',
    `status`     varchar(1)                                 DEFAULT '' NOT NULL COMMENT  '状态',
    `bank_address`  varchar(255)                            DEFAULT '' NOT NULL COMMENT  '银行地址',
    `create_time`    datetime                                DEFAULT now() NOT NULl COMMENT '创建时间',
    `create_by`      varchar(45)                             NULl COMMENT '创建者',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `country_id` (`country_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1080
  CHARACTER SET = utf8mb4
  CHARSET=utf8mb4_general_ci COMMENT = '银行配置信息'
  ROW_FORMAT = Dynamic;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('bankConfig', '2282', '1', 'LsBankSetting', 'setting/LsBankSetting/index', 1, 0, 'C', '0', '0', 'setting:LsBankSetting:list', '#', 'admin', sysdate(), '', null, '银行配置信息菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('银行配置信息查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'setting:LsBankSetting:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('银行配置信息新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'setting:LsBankSetting:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('银行配置信息修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'setting:LsBankSetting:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('银行配置信息删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'setting:LsBankSetting:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('银行配置信息导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'setting:LsBankSetting:export',       '#', 'admin', sysdate(), '', null, '');


-- 添加实名认证申请表
DROP TABLE IF EXISTS `realname_application_form`;
CREATE TABLE `realname_application_form`
(
    `id`             bigint(20)                             NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_bank_id`             int(11)                     NOT NULL  COMMENT '用户银行卡id',
    `uid`       bigint(20)                    NOT NULL  COMMENT '用户id',
    `status`    varchar(2)  not null default '0'    comment '实名申请批表状态',
    `opinion`   varchar(64)   null  comment '审批意见',
    `create_time`    datetime                                DEFAULT now() NOT NULl COMMENT '创建时间',
    `create_by`      varchar(45)                             NULl COMMENT '创建者',
    `update_time`    datetime                                DEFAULT now() NOT NULl COMMENT '修改时间',
    `update_by`      varchar(45)                             NULl COMMENT '修改者',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `uid` (`uid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1080
  CHARACTER SET = utf8mb4
  CHARSET=utf8mb4_general_ci COMMENT = '实名认证申请表'
  ROW_FORMAT = Dynamic;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('实名认证申请', '3', '1', 'realnameAppForm', 'member/realnameAppForm/index', 1, 0, 'C', '0', '0', 'member:realnameAppForm:list', '#', 'admin', sysdate(), '', null, '实名认证申请菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('实名认证申请查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'member:realnameAppForm:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('实名认证申请新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'member:realnameAppForm:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('实名认证申请修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'member:realnameAppForm:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('实名认证申请删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'member:realnameAppForm:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('实名认证申请导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'member:realnameAppForm:export',       '#', 'admin', sysdate(), '', null, '');


INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('实名申请批表状态', 'relename_status', '0', 'admin', '2022-01-11 12:34:22', '', null, '');
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('1', '申请', '0', 'relename_status', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('2', '已验证', '1', 'relename_status', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('3', '已拒绝', '2', 'relename_status', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);

-- 新增充值表中的交易类型
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (6, '手续费转余额', '6', 'blance_trans_type', NULL, NULL, 'N', '0', 'admin', '2020-07-25 08:53:12', '', NULL, NULL);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (7, '余额提款', '7', 'blance_trans_type', NULL, NULL, 'N', '0', 'admin', '2020-07-25 08:53:12', '', NULL, NULL);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (8, '余额提款失败', '8', 'blance_trans_type', NULL, NULL, 'N', '0', 'admin', '2020-07-25 08:53:12', '', NULL, NULL);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (9, '提款结束', '9', 'blance_trans_type', NULL, NULL, 'N', '0', 'admin', '2020-07-25 08:53:12', '', NULL, NULL);


alter table ums_member
    add column commission_frozen             decimal(20, 2)           default 0.00 null comment '会员冻结的佣金' after commission;

alter table oms_commission_records
    add order_code                varchar(45)                null comment '订单code';

alter table ums_pre_deposit_record
    add frozen  decimal(20, 2)           default 0.00 null comment '冻结额' after current_money;

-- 国家信息
alter table t_store_info
    add column `country_id`            bigint(20)                                               DEFAULT NULL COMMENT '国家id' after avatar_picture;

-- 更改codeType字段为2字节
alter table ls_pay_setting modify codeType char(2);

-- 修改自定义品牌菜单
update sys_store_menu set parent_id=2169,path='customizebrandlist',component='store/TStoreCustomizeBrand/index' where menu_id = 2161;

-- 新增自定义品牌支持
alter table pms_goods
    add column `customize_brand`    int(11)  default NULL comment '是否为自定义品牌' after `brand_id`;


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('省表', '1', '1', 'province', 'setting/LsProvince/index', 1, 0, 'C', '0', '0', 'system:LsProvince:list', '#', 'admin', sysdate(), '', null, '省表菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('省查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:LsProvince:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('省新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:LsProvince:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('省修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:LsProvince:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('省删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:LsProvince:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('省导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:LsProvince:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市表', '1', '1', 'city', 'setting/LsCity/index', 1, 0, 'C', '0', '0', 'system:LsCity:list', '#', 'admin', sysdate(), '', null, '市表菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:LsCity:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:LsCity:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:LsCity:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:LsCity:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('市导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:LsCity:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('区表', '1', '1', 'district', 'setting/LsDistrict/index', 1, 0, 'C', '0', '0', 'system:LsDistrict:list', '#', 'admin', sysdate(), '', null, '区表菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('区查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:LsDistrict:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('区新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:LsDistrict:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('区修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:LsDistrict:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('区删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:LsDistrict:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('区导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:LsDistrict:export',       '#', 'admin', sysdate(), '', null, '');


-- 积分兑换配置
alter table sms_point_setting
    add column `offset_token`  int(11)                                                     DEFAULT NULL COMMENT '兑换一个Token需要多少积分',
    add column `min_redeem_token`  int(11)                                                     DEFAULT NULL COMMENT '最小可兑换token数',
    add column `max_redeem_token`  int(11)                                                     DEFAULT NULL COMMENT '单次最大可兑换token数';


-- 提币状态
INSERT INTO `sys_dict_type`(dict_name, dict_type, status, create_by, create_time, update_by, update_time, remark)
VALUES ('提币状态', 'crypto_op_out_status', '0', 'admin', '2022-01-11 12:34:22', '', null, '');
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('1', '等待提现', '1', 'crypto_op_out_status', null, 'primary', 'N', '0', 'admin', '2022-01-11 12:35:38', '', null, null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('2', '锁定，正在处理', '2', 'crypto_op_out_status', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('3', '提现成功', '3', 'crypto_op_out_status', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('4', '用户取消', '4', 'crypto_op_out_status', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);
INSERT INTO `sys_dict_data`( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES ('5', '锁定订单', '5', 'crypto_op_out_status', null, 'info', 'N', '0', 'admin', '2022-01-11 12:35:58', 'admin', '2022-01-11 12:36:54', null);

