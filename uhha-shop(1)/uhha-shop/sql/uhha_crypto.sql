

--
-- Table structure for table `f_system_lan`
--

DROP TABLE IF EXISTS `f_system_lan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_system_lan` (
                                `fid` int(11) NOT NULL AUTO_INCREMENT,
                                `fsortid` int(11) DEFAULT NULL,
                                `fstatus` int(11) DEFAULT NULL,
                                `fname` varchar(64) DEFAULT NULL,
                                `ftype` varchar(32) DEFAULT NULL,
                                `fshortname` varchar(32) DEFAULT NULL,
                                `fpackagename` varchar(128) DEFAULT NULL,
                                `fdescription` varchar(128) DEFAULT NULL,
                                `fisrealname` tinyint(11) DEFAULT '0',
                                PRIMARY KEY (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_system_lan`
--

LOCK TABLES `f_system_lan` WRITE;
/*!40000 ALTER TABLE `f_system_lan` DISABLE KEYS */;
INSERT INTO `f_system_lan` VALUES (1,1,1,'zh_CN',NULL,'zh_CN',NULL,NULL,0),(2,2,1,'en_US',NULL,'en_US',NULL,NULL,0);
/*!40000 ALTER TABLE `f_system_lan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_coin_type`
--

DROP TABLE IF EXISTS `system_coin_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_coin_type` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `sort_id` int(11) DEFAULT NULL,
                                    `name` varchar(32) DEFAULT NULL,
                                    `short_name` varchar(16) DEFAULT NULL,
                                    `symbol` varchar(5) DEFAULT NULL,
                                    `type` int(11) unsigned DEFAULT NULL,
                                    `coin_type` int(11) unsigned DEFAULT NULL,
                                    `status` int(11) unsigned DEFAULT NULL,
                                    `is_withdraw` tinyint(1) unsigned DEFAULT NULL,
                                    `is_recharge` tinyint(1) unsigned DEFAULT NULL,
                                    `risk_num` decimal(24,4) DEFAULT NULL,
                                    `is_push` tinyint(1) unsigned DEFAULT '0',
                                    `is_finances` tinyint(1) unsigned DEFAULT '0',
                                    `ip` varchar(32) DEFAULT NULL,
                                    `port` varchar(16) DEFAULT NULL,
                                    `access_key` varchar(64) DEFAULT NULL,
                                    `secrt_key` varchar(64) DEFAULT NULL,
                                    `asset_id` bigint(20) unsigned DEFAULT NULL,
                                    `network_fee` decimal(24,10) DEFAULT NULL,
                                    `min_count` decimal(24,10) DEFAULT NULL,
                                    `confirmations` int(11) unsigned DEFAULT NULL,
                                    `eth_account` varchar(128) DEFAULT NULL,
                                    `web_logo` varchar(128) DEFAULT NULL,
                                    `app_logo` varchar(128) DEFAULT NULL,
                                    `gmt_create` datetime DEFAULT NULL,
                                    `gmt_modified` datetime DEFAULT NULL,
                                    `coin_introduce` text,
                                    `version` int(11) unsigned DEFAULT NULL,
                                    `fadminid` int(11) DEFAULT NULL,
                                    `coin_advantage` text COMMENT '币种优势',
                                    `full_name` varchar(32) DEFAULT NULL,
                                    `last_collect_time` datetime DEFAULT NULL COMMENT '最后汇总时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_coin_type`
--

LOCK TABLES `system_coin_type` WRITE;
/*!40000 ALTER TABLE `system_coin_type` DISABLE KEYS */;
INSERT INTO `system_coin_type` VALUES (1,1,'UHHA','UHHA','UHHA',2,3,1,0,0,50.0000,0,0,'127.0.0.1','12345','123','123',1,0.1000000000,0.1000000000,6,'','https://www.uhha.io/uhha.png','https://www.uhha.io/uhha.png','2018-09-17 23:32:16','2018-09-18 00:20:02','UHHA',2,30,'UHHA','UHHA',NULL),
                                      (2,2,'BTC','BTC','BTC',2,2,1,1,1,50.0000,0,0,'127.0.0.1','12346','123','123',2,0.0100000000,0.1000000000,3,'','https://www.uhha.io/btc.png','https://www.uhha.io/btc.png','2018-09-17 23:45:46','2018-09-18 00:19:53','BTC',4,30,'BTC','BTC',NULL),
                                      (3,3,'ETH','ETH','ETH',2,3,1,1,1,50.0000,0,0,'127.0.0.1','8545','123','123',2,0.0100000000,0.1000000000,3,'','https://www.uhha.io/eth.png','https://www.uhha.io/eth.png','2018-09-17 23:45:46','2018-09-18 00:19:53','ETH',4,30,'ETH','ETH',NULL),
                                      (4,4,'USDT','USDT','USDT',1,3,1,1,1,50.0000,0,0,'127.0.0.1','8545','123','123',2,0.0100000000,0.1000000000,3,'','https://www.uhha.io/usdt.png','https://www.uhha.io/usdt.png','2018-09-17 23:45:46','2018-09-18 00:19:53','USDT',4,30,'USDT','USDT',NULL);
/*!40000 ALTER TABLE `system_coin_type` ENABLE KEYS */;
UNLOCK TABLES;

-- add contract address
ALTER TABLE system_coin_type add column `contract_address` varchar(128) DEFAULT NULL after eth_account;


--
-- Table structure for table `system_coin_setting`
--

DROP TABLE IF EXISTS `system_coin_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_coin_setting` (
                                       `id` int(11) NOT NULL AUTO_INCREMENT,
                                       `coin_id` int(11) DEFAULT NULL,
                                       `level_vip` int(11) DEFAULT NULL,
                                       `withdraw_max` decimal(24,10) DEFAULT NULL,
                                       `withdraw_min` decimal(24,10) DEFAULT NULL,
                                       `withdraw_fee` decimal(24,10) DEFAULT NULL,
                                       `withdraw_times` int(11) DEFAULT NULL,
                                       `withdraw_single_limit` decimal(24,10) DEFAULT NULL,
                                       `withdraw_day_limit` decimal(24,10) DEFAULT NULL,
                                       `gmt_create` datetime DEFAULT NULL,
                                       `gmt_modified` datetime DEFAULT NULL,
                                       `version` int(11) DEFAULT NULL,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_coin_setting`
--

LOCK TABLES `system_coin_setting` WRITE;
/*!40000 ALTER TABLE `system_coin_setting` DISABLE KEYS */;
INSERT INTO `system_coin_setting` VALUES (1,1,0,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:32:17','2018-09-17 23:32:17',0),
                                         (2,1,1,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:32:17','2018-09-17 23:32:17',0),
                                         (3,1,2,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:32:17','2018-09-17 23:32:17',0),
                                         (4,1,3,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:32:17','2018-09-17 23:32:17',0),
                                         (5,1,4,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:32:17','2018-09-17 23:32:17',0),
                                         (6,1,5,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:32:17','2018-09-17 23:32:17',0),
                                         (7,1,6,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:32:17','2018-09-17 23:32:17',0),
                                         (8,2,0,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (9,2,1,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (10,2,2,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (11,2,3,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (12,2,4,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (13,2,5,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (14,2,6,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (15,3,0,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (16,3,1,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (17,3,2,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (18,3,3,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (19,3,4,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (20,3,5,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (21,3,6,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (22,4,0,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (23,4,1,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (24,4,2,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (25,4,3,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (26,4,4,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (27,4,5,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0),
                                         (28,4,6,0.0000000000,0.0000000000,0.0000000000,0,NULL,0.0000000000,'2018-09-17 23:45:46','2018-09-17 23:45:46',0);
/*!40000 ALTER TABLE `system_coin_setting` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Table structure for table `system_trade_type`
--

DROP TABLE IF EXISTS `system_trade_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_trade_type` (
                                     `id` int(11) NOT NULL AUTO_INCREMENT,
                                     `sort_id` int(11) unsigned DEFAULT NULL,
                                     `type` int(11) unsigned DEFAULT NULL,
                                     `status` int(11) unsigned DEFAULT NULL,
                                     `buy_coin_id` int(11) unsigned DEFAULT NULL,
                                     `b_coin_nickname` varchar(30) DEFAULT NULL,
                                     `sell_coin_id` int(11) unsigned DEFAULT NULL,
                                     `s_coin_nickname` varchar(30) DEFAULT NULL,
                                     `is_share` tinyint(1) unsigned DEFAULT NULL,
                                     `is_stop` tinyint(1) unsigned DEFAULT '1',
                                     `open_time` time DEFAULT NULL,
                                     `stop_time` time DEFAULT NULL,
                                     `buy_fee` decimal(24,10) DEFAULT NULL,
                                     `sell_fee` decimal(24,10) DEFAULT NULL,
                                     `remote_id` int(11) unsigned DEFAULT NULL,
                                     `price_wave` decimal(24,4) DEFAULT NULL,
                                     `min_count` decimal(24,4) DEFAULT NULL,
                                     `max_count` decimal(24,4) DEFAULT NULL,
                                     `min_price` decimal(24,10) DEFAULT '0.0000000000',
                                     `max_price` decimal(24,4) DEFAULT '0.0000',
                                     `amount_offset` varchar(255) DEFAULT NULL,
                                     `price_offset` varchar(255) DEFAULT NULL,
                                     `digit` varchar(255) DEFAULT NULL,
                                     `open_price` decimal(24,8) DEFAULT NULL,
                                     `gmt_create` datetime DEFAULT NULL,
                                     `gmt_modified` datetime DEFAULT NULL,
                                     `version` int(11) unsigned DEFAULT NULL,
                                     `fadminid` int(11) DEFAULT NULL,
                                     `buy_coin_display` varchar(128) DEFAULT NULL COMMENT '买方币展示名称',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_trade_type`
--

LOCK TABLES `system_trade_type` WRITE;
/*!40000 ALTER TABLE `system_trade_type` DISABLE KEYS */;
INSERT INTO `system_trade_type` VALUES (1,1,2,2,1,'UHHA',2,'BTC',1,0,NULL,NULL,0.0020000000,0.0020000000,1,0.5000,0.0100,100.0000,0.0000100000,10000.0000,'2#2','2#2','2#4',0.00100000,'2018-09-18 00:50:26','2018-09-19 15:13:04',0,30,NULL),
                                       (2,2,1,2,2,'BTC',1,'UHHA',1,0,NULL,NULL,0.0020000000,0.0020000000,2,0.5000,0.0100,100.0000,0.0000100000,10000.0000,'2#2','2#2','2#4',0.00100000,'2018-09-19 15:14:19','2018-09-19 15:27:38',0,30,NULL);
/*!40000 ALTER TABLE `system_trade_type` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `f_entrust`
--

DROP TABLE IF EXISTS `f_entrust`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_entrust` (
                             `fid` int(11) NOT NULL AUTO_INCREMENT,
                             `fuid` bigint(20) DEFAULT NULL,
                             `ftradeid` int(11) unsigned DEFAULT NULL,
                             `fbuycoinid` int(11) unsigned DEFAULT NULL,
                             `fsellcoinid` int(11) unsigned DEFAULT NULL,
                             `fstatus` int(11) DEFAULT NULL COMMENT '1:未成交 2:部分成交 3:完全成交 4:撤单处理中 5:已撤销',
                             `ftype` int(11) DEFAULT NULL,
                             `fmatchtype` int(11) DEFAULT NULL COMMENT '1：平台撮合，2:禁用 3:火币撮合 4：中币撮合 5：币安撮合',
                             `flast` decimal(24,10) DEFAULT '0.0000000000',
                             `flastamount` decimal(24,10) DEFAULT '0.0000000000' COMMENT '最后一次成交数量',
                             `flastcount` int(11) DEFAULT '0',
                             `fprize` decimal(24,10) DEFAULT NULL,
                             `fcount` decimal(24,10) DEFAULT NULL,
                             `famount` decimal(24,10) DEFAULT NULL,
                             `fsuccessamount` decimal(24,10) DEFAULT NULL COMMENT '最后一次成交总价',
                             `fleftcount` decimal(24,10) DEFAULT NULL,
                             `ffees` decimal(24,10) DEFAULT NULL COMMENT '手续费',
                             `fleftfees` decimal(24,10) DEFAULT NULL,
                             `fsource` int(11) DEFAULT NULL,
                             `fhuobientrustid` bigint(11) DEFAULT NULL,
                             `fhuobiaccountid` varchar(50) DEFAULT NULL COMMENT '第三方ID',
                             `flastupdattime` datetime DEFAULT NULL,
                             `fcreatetime` datetime DEFAULT NULL,
                             PRIMARY KEY (`fid`),
                             KEY `fentrust_fuid` (`fuid`) USING BTREE,
                             KEY `fentrust_fstatus` (`fstatus`) USING BTREE,
                             KEY `fentrust_ftype` (`ftype`) USING BTREE,
                             KEY `fentrust_fmatchtype` (`fmatchtype`) USING BTREE,
                             KEY `fentrust_fsource` (`fsource`) USING BTREE,
                             KEY `fentrust_fcreateTime` (`fcreatetime`) USING BTREE,
                             KEY `fentrust_ftradeid` (`ftradeid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='委托订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_entrust`
--

LOCK TABLES `f_entrust` WRITE;
/*!40000 ALTER TABLE `f_entrust` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_entrust` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `f_system_args`
--

DROP TABLE IF EXISTS `f_system_args`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_system_args` (
                                 `fid` int(11) NOT NULL AUTO_INCREMENT,
                                 `fkey` varchar(100) DEFAULT NULL,
                                 `ftype` int(11) DEFAULT NULL,
                                 `fvalue` varchar(2048) DEFAULT NULL,
                                 `fdescription` varchar(1024) DEFAULT NULL,
                                 `version` int(11) DEFAULT 1,
                                 `furl` varchar(100) DEFAULT NULL,
                                 PRIMARY KEY (`fid`),
                                 UNIQUE KEY `fKey_seq` (`fkey`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_system_args`
--

LOCK TABLES `f_system_args` WRITE;
/*!40000 ALTER TABLE `f_system_args` DISABLE KEYS */;
INSERT INTO `f_system_args` VALUES (1,'webName',2,'bit.com','网站名称简称',13,''),
                                   (2,'email',2,'x','首页EAML',12,NULL),
                                   (3,'firstLevelDomain',1,'x','一级域名',11,NULL),
                                   (4,'englishName',1,'x','英文简称',10,NULL),
                                   (5,'fulldomain',2,'http://bit.com','网站首页',21,''),
                                   (6,'tradePasswordHour',1,'1','超过多少小时需要输入交易密码',3,''),
                                   (7,'logoImage',2,'/front/images1/logo1.png','网站LOGO',32,''),
                                   (8,'bigImage1',2,'/front/images/index/n.jpg','首页大图1',58,''),
                                   (9,'bigImage2',2,'/front/images/index/c.jpg','首页大图2',46,''),
                                   (10,'bigImage3',2,'/front/images/index/m.jpg','首页大图3',25,''),
                                   (12,'isCanlogin',1,'x','是否允许登陆，1允许，0暂停登陆',1,NULL),
                                   (13,'canLoginUsers',1,'x','可以登陆的会员ID，用#号隔开',1,NULL),
                                   (14,'AndroidVersion',1,'4','安卓APP版本号',47,''),
                                   (15,'AndroidIsUpdate',1,'0','是否强制更新安卓APP，0否，1是。',23,''),
                                   (16,'AppDownloadQR',2,'x','app下载的二维码',25,''),
                                   (17,'WeiBoQR',2,'x','新浪微博二维码',12,''),
                                   (18,'WeChatQR',2,'x','微信二维码',14,''),
                                   (19,'staticurl',2,'http://www.bit.com/','静态文件url',33,''),
                                   (21,'buyVipPrice',2,'x','购买vip6的价格',6,NULL),
                                   (22,'dowloadlogo',2,'x','下载页面logo',0,NULL),
                                   (23,'alipaycode',2,'x','支付宝转账二维码',22,''),
                                   (24,'wechatcode',2,'x','微信转账二维码',9,''),
                                   (25,'googleVerifyDownUrl',2,'x','google验证器下载二维码',3,''),
                                   (26,'IosVersion',1,'x','苹果APP版本号',37,NULL),
                                   (27,'IosIsUpdate',1,'x','苹果APP是否强制升级，0否，1是。',7,NULL),
                                   (28,'IosDownloadUrl',2,'x','IOS下载地址',8,NULL),
                                   (29,'AndroidDownloadUrl',2,'x','安卓app下载地址',25,NULL),
                                   (30,'GoogleAuthName',1,'x','谷歌身份验证器前缀',0,NULL),
                                   (31,'ETCAccountBC',1,'x','平台ETC提现地址',1,NULL),
                                   (32,'riskamount',1,'10000','风控发送短信人民币金额限制',4,''),
                                   (33,'AndroidUpdateUrl',2,'x','安卓热更新下载链接',2,NULL),
                                   (34,'alipayaccount',2,'81067','支付宝官方账号',10,''),
                                   (35,'wechataccount',2,'x','微信官方账号',1,NULL),
                                   (38,'riskphone',1,'x','风控通知手机号',12,NULL),
                                   (39,'basepath',2,'/','网站页面相对路径默认地址',6,''),
                                   (40,'OverseasDownloadUrl',2,'x','海外版APP下载地址',4,NULL),
                                   (41,'bigImage1url',2,'#','首页大图1跳转链接，不跳转填写javascript:void(0)',38,''),
                                   (42,'bigImage2url',2,'#','首页大图2跳转链接，不跳转填写javascript:void(0)',16,''),
                                   (43,'bigImage3url',2,'#','首页大图3跳转链接，不跳转填写javascript:void(0)',2,''),
                                   (44,'indexLlogoImage',2,'x','首页LOGO',11,NULL),
                                   (45,'alipayName',2,'x','支付宝官方账号开户姓名',3,NULL),
                                   (46,'wechatName',2,'x','微信官方账号开户姓名',1,NULL),
                                   (49,'imgUploadUrl',2,'https://fubt.oss-ap-southeast-1.aliyuncs.com/','上传图片访问路径',8,''),
                                   (50,'ChainVersion',1,'x','chain版本',1,NULL),
                                   (51,'ip_limt_count',1,'2','注册IP限制',4,''),
                                   (56,'official_wechat',2,'x','官方微信',4,NULL),
                                   (57,'serviceQQ',2,'x','官方QQ',3,NULL),
                                   (58,'telephone',2,'x','官方电话',2,NULL),
                                   (59,'IdentityAppKey',1,'x','身份证验证密钥',0,NULL),
                                   (60,'serviceQQGroup',2,'x','QQGroup',6,NULL),
                                   (61,'official_weibo',2,'x','官方微博',1,NULL),
                                   (62,'bigImage4',2,'/front/images/index/TKC.jpg','首页大图4',6,''),
                                   (63,'registerReward',1,'1#10','注册奖励（币种ID#奖励数量）',3,''),
                                   (64,'isOpenReg',2,'1','是否开放注册',0,''),
                                   (65,'isMustIntrol',2,'0','是否强制填写推荐人0否1是',5,''),
                                   (68,'bigImage5',2,'x','首页大图5',2,NULL),
                                   (69,'bigImage6',2,'x','首页大图6',4,NULL),
                                   (70,'bigImage5url',2,'x','首页大图5跳转链接，不跳转填写javascript:void(0)',6,NULL),
                                   (71,'bigImage6url',2,'x','首页大图6跳转链接，不跳转填写javascript:void(0)',4,NULL),
                                   (72,'bigImage4url',2,'x','首页大图4跳转链接，不跳转填写javascript:void(0)',4,NULL),
                                   (73,'recommendReward',1,'1#10','推荐人奖励，(币种id#币种数目)',1,''),
                                   (74,'coinVoteId',2,'1','新币投票消耗的币种id',7,''),
                                   (75,'sellOrderMax',2,'10','当天卖出订单达到上限',1,''),
                                   (76,'buyAndSellNumberMax',2,'1000000','交易数量上限',1,''),
                                   (77,'unpaidOrderMax',2,'6','当天未支付订单上限',0,''),
                                   (78,'orderCancelMax',2,'3','当天取消订单上限',0,''),
                                   (79,'etherscanApiUrl',2,'https://api-ropsten.etherscan.io','',0,''),
                                   (80,'etherscanApiKey',2,'R3FWYGW8RBAKCS4M9YDBA57FF6M5MRJ1VK','etherscanApiKey',0,''),
                                   (81,'isMQEnabled',2,'0','是否開啟MQ 0不開啟，1開啟',0,''),
                                   (82,'enableCoinOutCode',2,'0','是否启用提币审批验证码 0不開啟，1開啟',0,''),
                                   (83,'ethRequestUrl',2,'http://localhost:8545/','以太坊的节点链接',0,'')
                                   ;
/*!40000 ALTER TABLE `f_system_args` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Table structure for table `f_user`
--

DROP TABLE IF EXISTS `f_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_user` (
                          `fid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                          `fshowid` int(11) DEFAULT NULL COMMENT '显示ID',
                          `floginname` varchar(32) DEFAULT NULL COMMENT '登录名',
                          `fnickname` varchar(32) DEFAULT NULL COMMENT '昵称',
                          `floginpassword` varchar(32) DEFAULT NULL COMMENT '登录密码',
                          `ftradepassword` varchar(32) DEFAULT NULL COMMENT '交易密码',
                          `ftelephone` varchar(32) DEFAULT NULL COMMENT '绑定手机',
                          `femail` varchar(128) DEFAULT NULL COMMENT '绑定邮箱',
                          `frealname` varchar(32) DEFAULT NULL COMMENT '真实姓名',
                          `fidentityno` varchar(128) DEFAULT NULL COMMENT '身份证号',
                          `fidentitytype` int(11) DEFAULT NULL COMMENT '实名认证类型',
                          `fgoogleauthenticator` varchar(128) DEFAULT NULL COMMENT '谷歌验证密码',
                          `fgoogleurl` varchar(128) DEFAULT NULL COMMENT '谷歌验证URL',
                          `fstatus` int(11) DEFAULT NULL COMMENT '用户状态',
                          `fhasrealvalidate` tinyint(1) DEFAULT NULL COMMENT '是否实名认证',
                          `fhasrealvalidatetime` datetime DEFAULT NULL COMMENT '实名认证时间',
                          `fistelephonebind` tinyint(1) DEFAULT NULL COMMENT '是否绑定手机',
                          `fismailbind` tinyint(1) DEFAULT NULL COMMENT '是否绑定邮箱',
                          `fgooglebind` tinyint(1) DEFAULT '0' COMMENT '是否绑定谷歌',
                          `fupdatetime` datetime DEFAULT NULL COMMENT '最后更新时间',
                          `ftradepwdtime` datetime DEFAULT NULL COMMENT '修改交易密码的时间',
                          `fareacode` varchar(32) DEFAULT NULL COMMENT '手机区号',
                          `version` int(11) DEFAULT NULL COMMENT '乐观锁',
                          `fintrouid` int(11) DEFAULT NULL COMMENT '推荐人ID',
                          `finvalidateintrocount` int(11) DEFAULT NULL COMMENT '推荐人数',
                          `fiscny` tinyint(1) DEFAULT NULL COMMENT '是否允许人民币提现',
                          `fiscoin` tinyint(1) DEFAULT NULL COMMENT '是否允许虚拟币提现',
                          `fbirth` date DEFAULT NULL COMMENT '生日',
                          `flastlogintime` datetime DEFAULT NULL COMMENT '最后登录时间',
                          `fregistertime` datetime DEFAULT NULL COMMENT '注册时间',
                          `flastip` bigint(20) DEFAULT NULL COMMENT '最后登录IP',
                          `fplatform` int(11) DEFAULT '1' COMMENT '账号来源平台',
                          `is_video` tinyint(1) DEFAULT NULL COMMENT '是否视频认证',
                          `video_time` datetime DEFAULT NULL COMMENT '视频认证时间',
                          `identitytype` tinyint(2) DEFAULT '0' COMMENT '身份类型,1为公司员工，0为普通用户',
                          `agency` int(11) DEFAULT NULL COMMENT '代理商',
                          `otcidentity` tinyint(2) unsigned DEFAULT '0' COMMENT 'otc身份：0为普通客户，1为商户',
                          `fgooglestatus` int(2) DEFAULT '0' COMMENT '谷歌验证器状态：0未绑定状态，1开启状态，2关闭状态',
                          PRIMARY KEY (`fid`),
                          UNIQUE KEY `flogin_name` (`floginname`) USING BTREE,
                          KEY `fIntroUser_id_fk` (`fintrouid`),
                          KEY `fId` (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_user`
--


--
-- Table structure for table `f_user_virtual_address`
--

DROP TABLE IF EXISTS `f_user_virtual_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_user_virtual_address` (
                                          `fid` int(11) NOT NULL AUTO_INCREMENT,
                                          `fcoinid` int(11) DEFAULT NULL,
                                          `fadderess` varchar(180) DEFAULT NULL,
                                          `fuid` bigint(20) DEFAULT NULL,
                                          `fcreatetime` datetime DEFAULT NULL,
                                          `version` int(11) DEFAULT NULL,
                                          PRIMARY KEY (`fid`),
                                          UNIQUE KEY `uni_coin_uid_address` (`fuid`,`fcoinid`),
                                          KEY `FK_Relationship_22` (`fcoinid`),
                                          KEY `fuid_fks` (`fuid`),
                                          KEY `v_f_address` (`fadderess`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_user_virtual_address`
--

LOCK TABLES `f_user_virtual_address` WRITE;
/*!40000 ALTER TABLE `f_user_virtual_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_user_virtual_address` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `f_virtual_capital_operation`
--

DROP TABLE IF EXISTS `f_virtual_capital_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_virtual_capital_operation` (
                                               `fid` int(11) NOT NULL AUTO_INCREMENT,
                                               `fuid` bigint(20) DEFAULT NULL,
                                               `fcoinid` int(11) DEFAULT NULL,
                                               `famount` decimal(24,10) DEFAULT NULL,
                                               `ffees` decimal(24,10) DEFAULT NULL,
                                               `ftype` int(11) DEFAULT NULL,
                                               `fstatus` int(11) DEFAULT NULL,
                                               `fcreatetime` datetime DEFAULT NULL,
                                               `fupdatetime` datetime DEFAULT NULL,
                                               `tx_time` datetime DEFAULT NULL,
                                               `fwithdrawaddress` varchar(128) DEFAULT NULL,
                                               `frechargeaddress` varchar(128) DEFAULT NULL,
                                               `funiquenumber` varchar(180) DEFAULT NULL,
                                               `fconfirmations` int(11) DEFAULT NULL,
                                               `fhasowner` tinyint(1) DEFAULT NULL,
                                               `fblocknumber` int(11) DEFAULT '0',
                                               `fbtcfees` decimal(24,10) DEFAULT '0.0000000000',
                                               `fadminid` int(11) DEFAULT NULL,
                                               `version` int(11) DEFAULT NULL,
                                               `fsource` int(11) DEFAULT '1',
                                               `fnonce` int(11) DEFAULT '0',
                                               `fplatform` int(11) DEFAULT NULL,
                                               `f_address_withdraw_id` int(11) DEFAULT NULL,
                                               PRIMARY KEY (`fid`),
                                               UNIQUE KEY `ftradeUniqueNumber` (`funiquenumber`),
                                               KEY `FK_Relationship_15` (`fuid`),
                                               KEY `FK_Relationship_13` (`fcoinid`),
                                               KEY `int_add_fk` (`frechargeaddress`) USING BTREE,
                                               KEY `out_add_fk` (`fwithdrawaddress`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_virtual_capital_operation`
--

LOCK TABLES `f_virtual_capital_operation` WRITE;
/*!40000 ALTER TABLE `f_virtual_capital_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_virtual_capital_operation` ENABLE KEYS */;
UNLOCK TABLES;


/*Table structure for table `user_coin_wallet` */

DROP TABLE IF EXISTS `user_coin_wallet`;

CREATE TABLE `user_coin_wallet` (
                                    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                    `uid` int(15) DEFAULT NULL,
                                    `coin_id` int(11) DEFAULT NULL,
                                    `total` decimal(24,10) DEFAULT NULL,
                                    `frozen` decimal(24,10) DEFAULT NULL,
                                    `borrow` decimal(24,10) DEFAULT NULL,
                                    `ico` decimal(24,10) DEFAULT NULL,
                                    `gmt_create` datetime DEFAULT NULL,
                                    `gmt_modified` datetime DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `fuservirtualwallet_fuid` (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9595141 DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `f_token_capital_operation`
--

DROP TABLE IF EXISTS `f_token_capital_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_token_capital_operation` (
                                             `fid` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                             `fcoinid` int(11) DEFAULT NULL,
                                             `fuid` bigint(20) DEFAULT NULL,
                                             `fpayee` int(11) DEFAULT NULL,
                                             `finouttype` int(11) DEFAULT NULL,
                                             `famount` decimal(24,10) DEFAULT NULL,
                                             `fstatus` tinyint(1) DEFAULT NULL,
                                             `faccount` varchar(256) DEFAULT NULL,
                                             `fadminid` int(11) DEFAULT NULL,
                                             `ffees` decimal(24,10) DEFAULT NULL,
                                             `fcreatetime` datetime DEFAULT NULL,
                                             `fupdatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                             `version` int(11) DEFAULT NULL,
                                             PRIMARY KEY (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_token_capital_operation`
--

LOCK TABLES `f_token_capital_operation` WRITE;
/*!40000 ALTER TABLE `f_token_capital_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_token_capital_operation` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `f_user_virtual_address`
--

DROP TABLE IF EXISTS `f_user_virtual_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_user_virtual_address` (
                                          `fid` int(11) NOT NULL AUTO_INCREMENT,
                                          `fcoinid` int(11) DEFAULT NULL,
                                          `fadderess` varchar(180) DEFAULT NULL,
                                          `fuid` bigint(20) DEFAULT NULL,
                                          `fcreatetime` datetime DEFAULT NULL,
                                          `version` int(11) DEFAULT NULL,
                                          PRIMARY KEY (`fid`),
                                          UNIQUE KEY `uni_coin_uid_address` (`fuid`,`fcoinid`),
                                          KEY `FK_Relationship_22` (`fcoinid`),
                                          KEY `fuid_fks` (`fuid`),
                                          KEY `v_f_address` (`fadderess`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_user_virtual_address`
--

LOCK TABLES `f_user_virtual_address` WRITE;
/*!40000 ALTER TABLE `f_user_virtual_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_user_virtual_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f_user_virtual_address_withdraw`
--

DROP TABLE IF EXISTS `f_user_virtual_address_withdraw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_user_virtual_address_withdraw` (
                                                   `fId` int(11) NOT NULL AUTO_INCREMENT,
                                                   `fcoinid` int(11) DEFAULT NULL,
                                                   `fadderess` varchar(128) DEFAULT NULL,
                                                   `fuid` bigint(20) DEFAULT NULL,
                                                   `fcreatetime` datetime DEFAULT NULL,
                                                   `version` int(11) DEFAULT NULL,
                                                   `init` tinyint(1) DEFAULT NULL,
                                                   `fremark` varchar(128) DEFAULT NULL,
                                                   PRIMARY KEY (`fId`),
                                                   KEY `FK_Relationship_22` (`fcoinid`),
                                                   KEY `fuid_fk` (`fuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_user_virtual_address_withdraw`
--

LOCK TABLES `f_user_virtual_address_withdraw` WRITE;
/*!40000 ALTER TABLE `f_user_virtual_address_withdraw` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_user_virtual_address_withdraw` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f_virtual_capital_operation`
--

DROP TABLE IF EXISTS `f_virtual_capital_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_virtual_capital_operation` (
                                               `fid` int(11) NOT NULL AUTO_INCREMENT,
                                               `fuid` bigint(20) DEFAULT NULL,
                                               `fcoinid` int(11) DEFAULT NULL,
                                               `famount` decimal(24,10) DEFAULT NULL,
                                               `ffees` decimal(24,10) DEFAULT NULL,
                                               `ftype` int(11) DEFAULT NULL,
                                               `fstatus` int(11) DEFAULT NULL,
                                               `fcreatetime` datetime DEFAULT NULL,
                                               `fupdatetime` datetime DEFAULT NULL,
                                               `tx_time` datetime DEFAULT NULL,
                                               `fwithdrawaddress` varchar(128) DEFAULT NULL,
                                               `frechargeaddress` varchar(128) DEFAULT NULL,
                                               `funiquenumber` varchar(180) DEFAULT NULL,
                                               `fconfirmations` int(11) DEFAULT NULL,
                                               `fhasowner` tinyint(1) DEFAULT NULL,
                                               `fblocknumber` int(11) DEFAULT '0',
                                               `fbtcfees` decimal(24,10) DEFAULT '0.0000000000',
                                               `fadminid` int(11) DEFAULT NULL,
                                               `version` int(11) DEFAULT NULL,
                                               `fsource` int(11) DEFAULT '1',
                                               `fnonce` int(11) DEFAULT '0',
                                               `fplatform` int(11) DEFAULT NULL,
                                               `f_address_withdraw_id` int(11) DEFAULT NULL,
                                               PRIMARY KEY (`fid`),
                                               UNIQUE KEY `ftradeUniqueNumber` (`funiquenumber`),
                                               KEY `FK_Relationship_15` (`fuid`),
                                               KEY `FK_Relationship_13` (`fcoinid`),
                                               KEY `int_add_fk` (`frechargeaddress`) USING BTREE,
                                               KEY `out_add_fk` (`fwithdrawaddress`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_virtual_capital_operation`
--

LOCK TABLES `f_virtual_capital_operation` WRITE;
/*!40000 ALTER TABLE `f_virtual_capital_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_virtual_capital_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f_wallet_capital_operation`
--

DROP TABLE IF EXISTS `f_wallet_capital_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_wallet_capital_operation` (
                                              `fId` int(11) NOT NULL AUTO_INCREMENT,
                                              `fcoinid` int(11) DEFAULT NULL COMMENT '币种ID',
                                              `fsysbankid` int(11) DEFAULT NULL,
                                              `fuid` bigint(20) DEFAULT NULL,
                                              `fcreatetime` datetime DEFAULT NULL,
                                              `famount` decimal(24,10) DEFAULT NULL,
                                              `finouttype` int(11) DEFAULT NULL,
                                              `ftype` int(11) DEFAULT NULL,
                                              `fstatus` int(11) DEFAULT NULL,
                                              `fremittancetype` int(11) DEFAULT NULL,
                                              `fremark` varchar(256) DEFAULT NULL,
                                              `fbank` varchar(256) DEFAULT NULL,
                                              `faccount` varchar(256) DEFAULT NULL,
                                              `fpayee` varchar(256) DEFAULT NULL,
                                              `fphone` varchar(256) DEFAULT NULL,
                                              `fupdatetime` datetime DEFAULT NULL,
                                              `fadminid` int(9) DEFAULT NULL,
                                              `ffees` decimal(24,10) DEFAULT NULL,
                                              `version` int(11) DEFAULT NULL,
                                              `fischarge` tinyint(4) DEFAULT NULL,
                                              `faddress` varchar(128) DEFAULT NULL,
                                              `fsource` int(11) DEFAULT '1',
                                              `fplatform` int(11) unsigned DEFAULT NULL,
                                              PRIMARY KEY (`fId`),
                                              KEY `FK_Relationship_14` (`fuid`),
                                              KEY `FK_Relationship_24` (`fsysbankid`),
                                              KEY `fAuditee_id` (`fadminid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_wallet_capital_operation`
--

LOCK TABLES `f_wallet_capital_operation` WRITE;
/*!40000 ALTER TABLE `f_wallet_capital_operation` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_wallet_capital_operation` ENABLE KEYS */;
UNLOCK TABLES;




--
-- Table structure for table `f_day_capital_coin`
--

DROP TABLE IF EXISTS `f_day_capital_coin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_day_capital_coin` (
                                      `fid` int(11) NOT NULL AUTO_INCREMENT,
                                      `fcoinid` int(11) DEFAULT NULL,
                                      `frecharge` decimal(24,10) DEFAULT '0.0000000000',
                                      `fwithdraw` decimal(24,10) DEFAULT '0.0000000000',
                                      `fwithdrawwait` decimal(24,10) DEFAULT '0.0000000000',
                                      `ffees` decimal(24,10) DEFAULT '0.0000000000',
                                      `fnetfees` decimal(24,10) DEFAULT '0.0000000000',
                                      `fleverborrow` decimal(24,10) DEFAULT '0.0000000000',
                                      `fleverrepay` decimal(24,10) DEFAULT '0.0000000000',
                                      `fupdatetime` datetime DEFAULT NULL,
                                      `fcreatetime` datetime DEFAULT NULL,
                                      PRIMARY KEY (`fid`),
                                      KEY `fdaycoinid` (`fcoinid`) USING BTREE,
                                      KEY `fdaycointime` (`fcreatetime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_day_capital_coin`
--

LOCK TABLES `f_day_capital_coin` WRITE;
/*!40000 ALTER TABLE `f_day_capital_coin` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_day_capital_coin` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `f_day_operat`
--

DROP TABLE IF EXISTS `f_day_operat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_day_operat` (
                                `fid` int(11) NOT NULL AUTO_INCREMENT,
                                `flogin` int(11) DEFAULT NULL,
                                `fregister` int(11) DEFAULT NULL,
                                `frealname` int(11) DEFAULT NULL,
                                `fsms` int(11) DEFAULT NULL,
                                `fmail` int(11) DEFAULT NULL,
                                `fvip6` int(11) DEFAULT NULL,
                                `fcode` int(11) DEFAULT NULL COMMENT '充值码',
                                `fscore` int(11) DEFAULT NULL,
                                `fsubmitquestion` int(11) DEFAULT NULL,
                                `freplyquestion` int(11) DEFAULT NULL,
                                `fupdatetime` datetime DEFAULT NULL,
                                `fcreatetime` datetime DEFAULT NULL,
                                PRIMARY KEY (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_day_operat`
--

LOCK TABLES `f_day_operat` WRITE;
/*!40000 ALTER TABLE `f_day_operat` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_day_operat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f_day_sum`
--

DROP TABLE IF EXISTS `f_day_sum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_day_sum` (
                             `fid` int(11) NOT NULL AUTO_INCREMENT,
                             `fcoinid` int(11) DEFAULT NULL,
                             `ftotle` decimal(24,10) DEFAULT '0.0000000000',
                             `frozen` decimal(24,10) DEFAULT '0.0000000000',
                             `fcreatetime` datetime DEFAULT NULL,
                             PRIMARY KEY (`fid`),
                             KEY `fdaysumcoinid` (`fcoinid`) USING BTREE,
                             KEY `fdaysumcointime` (`fcreatetime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_day_sum`
--

LOCK TABLES `f_day_sum` WRITE;
/*!40000 ALTER TABLE `f_day_sum` DISABLE KEYS */;
INSERT INTO `f_day_sum` VALUES (1,1,0.0000000000,0.0000000000,'2018-09-19 00:00:00'),(2,2,0.0000000000,0.0000000000,'2018-09-19 00:00:00'),(3,1,0.0000000000,0.0000000000,'2018-09-20 00:00:00'),(4,2,0.0000000000,0.0000000000,'2018-09-20 00:00:00');
/*!40000 ALTER TABLE `f_day_sum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f_day_trade_coin`
--

DROP TABLE IF EXISTS `f_day_trade_coin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_day_trade_coin` (
                                    `fid` int(11) NOT NULL AUTO_INCREMENT,
                                    `fcoinid` int(11) DEFAULT NULL,
                                    `fbuy` decimal(24,10) DEFAULT '0.0000000000',
                                    `fsell` decimal(24,10) DEFAULT '0.0000000000',
                                    `fbuyfees` decimal(24,10) DEFAULT '0.0000000000',
                                    `fsellfees` decimal(24,10) DEFAULT '0.0000000000',
                                    `fbuyperson` int(11) DEFAULT NULL,
                                    `fsellperson` int(11) DEFAULT NULL,
                                    `fbuyentrust` int(11) DEFAULT NULL,
                                    `fsellentrust` int(11) DEFAULT NULL,
                                    `fupdatetime` datetime DEFAULT NULL,
                                    `fcreatetime` datetime DEFAULT NULL,
                                    PRIMARY KEY (`fid`),
                                    KEY `fdaytradecoinid` (`fcoinid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_day_trade_coin`
--

LOCK TABLES `f_day_trade_coin` WRITE;
/*!40000 ALTER TABLE `f_day_trade_coin` DISABLE KEYS */;
INSERT INTO `f_day_trade_coin` VALUES (1,1,0.0000000000,0.0000000000,0.0000000000,0.0000000000,0,0,0,0,'2018-09-19 00:00:00','2018-09-18 00:00:01');
/*!40000 ALTER TABLE `f_day_trade_coin` ENABLE KEYS */;
UNLOCK TABLES;




--
-- Table structure for table `f_coin_balance`
--

DROP TABLE IF EXISTS `f_coin_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_coin_balance` (
                                  `fid` int(11) NOT NULL AUTO_INCREMENT COMMENT '币种余额主键',
                                  `fdate` datetime DEFAULT NULL COMMENT '当日余额',
                                  `fcoinid` int(6) DEFAULT NULL COMMENT '币种id',
                                  `fbalance` decimal(24,10) DEFAULT NULL COMMENT '币种余额',
                                  `fcreatedate` datetime DEFAULT NULL COMMENT '创建时间',
                                  `fremark` varchar(255) DEFAULT NULL COMMENT '备注',
                                  `version` int(11) DEFAULT NULL COMMENT '版本号',
                                  PRIMARY KEY (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_coin_balance`
--

LOCK TABLES `f_coin_balance` WRITE;
/*!40000 ALTER TABLE `f_coin_balance` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_coin_balance` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `f_log_console_virtual_recharge`
--

DROP TABLE IF EXISTS `f_log_console_virtual_recharge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_log_console_virtual_recharge` (
                                                  `fid` int(11) NOT NULL AUTO_INCREMENT,
                                                  `famount` decimal(25,10) DEFAULT NULL,
                                                  `fcoinid` int(11) DEFAULT NULL,
                                                  `ftype` int(11) DEFAULT NULL,
                                                  `fstatus` int(1) DEFAULT NULL,
                                                  `fuid` bigint(20) DEFAULT NULL,
                                                  `fissendmsg` int(1) DEFAULT NULL,
                                                  `fcreatorid` int(11) DEFAULT NULL,
                                                  `finfo` varchar(512) DEFAULT NULL,
                                                  `fcreatetime` datetime DEFAULT NULL,
                                                  `version` int(11) DEFAULT NULL COMMENT '版本号',
                                                  `order_id` int(11) DEFAULT NULL COMMENT 'c2c_order_fid',
                                                  PRIMARY KEY (`fid`),
                                                  KEY `fk_voperationLog_VCoinTypeId` (`fcoinid`),
                                                  KEY `fk_voperationLog_UserId` (`fuid`),
                                                  KEY `fk_voperationLog_CreatorId` (`fcreatorid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_log_console_virtual_recharge`
--

LOCK TABLES `f_log_console_virtual_recharge` WRITE;
/*!40000 ALTER TABLE `f_log_console_virtual_recharge` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_log_console_virtual_recharge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `f_log_console_wallet_recharge`
--

DROP TABLE IF EXISTS `f_log_console_wallet_recharge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_log_console_wallet_recharge` (
                                                 `fid` int(11) NOT NULL AUTO_INCREMENT,
                                                 `ftype` int(11) DEFAULT NULL,
                                                 `fstatus` int(11) DEFAULT NULL,
                                                 `famount` decimal(24,10) DEFAULT NULL,
                                                 `fcreatetime` datetime DEFAULT NULL,
                                                 `fupdatetime` datetime DEFAULT NULL,
                                                 `fdescription` varchar(256) DEFAULT NULL,
                                                 `fuid` bigint(20) DEFAULT NULL,
                                                 `fadminid` int(11) DEFAULT NULL,
                                                 `version` int(11) DEFAULT NULL,
                                                 PRIMARY KEY (`fid`),
                                                 KEY `foreign_fuid` (`fuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_log_console_wallet_recharge`
--

LOCK TABLES `f_log_console_wallet_recharge` WRITE;
/*!40000 ALTER TABLE `f_log_console_wallet_recharge` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_log_console_wallet_recharge` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `f_pool`
--

DROP TABLE IF EXISTS `f_pool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_pool` (
                          `fid` int(11) NOT NULL AUTO_INCREMENT,
                          `fcoinid` int(11) DEFAULT NULL,
                          `faddress` varchar(256) DEFAULT NULL,
                          `fstatus` int(11) DEFAULT NULL COMMENT '1为已用,0为空闲',
                          `version` int(11) DEFAULT NULL,
                          PRIMARY KEY (`fid`),
                          KEY `fvir_fk` (`fcoinid`),
                          KEY `status_fk` (`fstatus`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_pool`
--

LOCK TABLES `f_pool` WRITE;
/*!40000 ALTER TABLE `f_pool` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_pool` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Table structure for table `f_log_modify_capital_operation`
--

DROP TABLE IF EXISTS `f_log_modify_capital_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_log_modify_capital_operation` (
                                                  `fid` int(11) NOT NULL AUTO_INCREMENT,
                                                  `faccount` varchar(256) DEFAULT NULL,
                                                  `fadminid` int(11) DEFAULT NULL,
                                                  `fbank` varchar(256) DEFAULT NULL,
                                                  `fpayee` varchar(256) DEFAULT NULL,
                                                  `fphone` varchar(64) DEFAULT NULL,
                                                  `famount` decimal(24,10) DEFAULT NULL COMMENT '修改前金额',
                                                  `fmodifyamount` decimal(24,10) DEFAULT NULL COMMENT '修改后金额',
                                                  `fupdatetime` datetime DEFAULT NULL,
                                                  PRIMARY KEY (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `f_log_countlimit`
--

DROP TABLE IF EXISTS `f_log_countlimit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_log_countlimit` (
                                    `fid` int(11) NOT NULL AUTO_INCREMENT,
                                    `fip` varchar(64) DEFAULT NULL,
                                    `fcreatetime` datetime DEFAULT NULL,
                                    `fcount` int(11) DEFAULT NULL,
                                    `ftype` int(11) DEFAULT NULL,
                                    `version` int(11) DEFAULT NULL,
                                    PRIMARY KEY (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_log_countlimit`
--

LOCK TABLES `f_log_countlimit` WRITE;
/*!40000 ALTER TABLE `f_log_countlimit` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_log_countlimit` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `f_user_loginlimit`
--

DROP TABLE IF EXISTS `f_user_loginlimit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `f_user_loginlimit` (
                                     `fid` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `fuid` bigint(20) DEFAULT NULL COMMENT '用户id',
                                     `fip` varchar(64) DEFAULT NULL COMMENT '登陆ip',
                                     `fcreatetime` datetime DEFAULT NULL COMMENT '创建时间',
                                     `fcount` int(4) DEFAULT NULL COMMENT '登陆错误次数',
                                     `version` tinyint(4) DEFAULT NULL COMMENT '版本号',
                                     PRIMARY KEY (`fid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `f_user_loginlimit`
--

LOCK TABLES `f_user_loginlimit` WRITE;
/*!40000 ALTER TABLE `f_user_loginlimit` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_user_loginlimit` ENABLE KEYS */;
UNLOCK TABLES;


-- ----------------------------
-- Table structure for `q_virtual_capital_small`
-- ----------------------------
DROP TABLE IF EXISTS `q_virtual_capital_small`;
CREATE TABLE `q_virtual_capital_small` (
                                           `qid` int(11) NOT NULL AUTO_INCREMENT,
                                           `qcoinid` int(11) DEFAULT NULL,
                                           `qamount` decimal(24,10) DEFAULT NULL,
                                           `qtype` int(11) DEFAULT NULL,
                                           `qcreatetime` datetime DEFAULT NULL,
                                           `tx_time` datetime DEFAULT NULL,
                                           `qrechargeaddress` varchar(128) DEFAULT NULL,
                                           `quniquenumber` varchar(180) DEFAULT NULL,
                                           `qsource` int(11) DEFAULT NULL,
                                           `qnonce` int(11) DEFAULT NULL,
                                           `qplatform` int(11) DEFAULT NULL,
                                           PRIMARY KEY (`qid`),
                                           UNIQUE KEY `idx_v_c_s_quniquenumber` (`quniquenumber`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of q_virtual_capital_small
-- ----------------------------

-- ----------------------------
-- Table structure for f_sys_coin_wallet
-- ----------------------------
DROP TABLE IF EXISTS `f_sys_coin_wallet`;
CREATE TABLE `f_sys_coin_wallet` (
                                     `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                                     `name` varchar(32) DEFAULT NULL COMMENT '钱包名称',
                                     `coin_id` int(11) DEFAULT NULL,
                                     `total` decimal(24,10) DEFAULT NULL COMMENT '可用余额',
                                     `frozen` decimal(24,10) DEFAULT NULL COMMENT '冻结',
                                     `borrow` decimal(24,10) DEFAULT NULL,
                                     `version` int(11) DEFAULT NULL,
                                     `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
                                     `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
                                     PRIMARY KEY (`id`),
                                     KEY `fuservirtualwallet_fuid` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of f_sys_coin_wallet
-- ----------------------------
INSERT INTO `f_sys_coin_wallet` VALUES ('1', 'UHHA头寸', '1', '0.0000000000', '0.0000000000', '0.0000000000', '0', null, null);
INSERT INTO `f_sys_coin_wallet` VALUES ('2', 'BTC头寸', '2', '0.0000000000', '0.0000000000', '0.0000000000', '0', null, null);
INSERT INTO `f_sys_coin_wallet` VALUES ('3', 'ETH头寸', '3', '0.0000000000', '0.0000000000', '0.0000000000', '2', null, null);
INSERT INTO `f_sys_coin_wallet` VALUES ('4', 'USDT头寸', '4', '0.0000000000', '0.0000000000', '0.0000000000', '0', null, null);


-- ----------------------------
-- Table structure for f_sys_coin_operation
-- ----------------------------
DROP TABLE IF EXISTS `f_sys_coin_operation`;
CREATE TABLE `f_sys_coin_operation` (
                                        `fid` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作编号',
                                        `fcoinid` int(11) DEFAULT NULL COMMENT '币种id',
                                        `ftoaddress` varchar(128) DEFAULT NULL COMMENT '源地址',
                                        `ffromaddress` varchar(128) DEFAULT NULL COMMENT '目标地址',
                                        `famount` decimal(24,10) DEFAULT NULL COMMENT '发生额',
                                        `ffees` decimal(24,10) DEFAULT NULL COMMENT '手续费',
                                        `ftype` int(11) DEFAULT NULL COMMENT '交易类型',
                                        `fstatus` int(11) DEFAULT NULL COMMENT '状态',
                                        `fadminid` int(11) DEFAULT NULL COMMENT '管理员id',
                                        `version` int(11) DEFAULT NULL COMMENT '版本',
                                        `fcreatetime` datetime DEFAULT NULL COMMENT '创建时间',
                                        `fupdatetime` datetime DEFAULT NULL COMMENT '更新时间',
                                        `txtime` datetime DEFAULT NULL,
                                        `txid` varchar(180) DEFAULT NULL,
                                        `fnonce` int(11) DEFAULT '0',
                                        `fconfirmations` int(11) DEFAULT NULL COMMENT '确认数',
                                        `fblocknumber` int(11) DEFAULT '0' COMMENT '区块号',
                                        PRIMARY KEY (`fid`),
                                        UNIQUE KEY `ftradeUniqueNumber` (`txid`),
                                        KEY `FK_Relationship_13` (`fcoinid`),
                                        KEY `int_add_fk` (`ftoaddress`) USING BTREE,
                                        KEY `out_add_fk` (`ffromaddress`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='系统钱包操作';

-- ----------------------------
-- Records of f_sys_coin_operation
-- ----------------------------