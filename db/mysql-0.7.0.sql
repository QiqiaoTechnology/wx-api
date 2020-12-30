/*
 V0.7.0 完整脚本，由于本次升级大量重构，不提供升级脚本，请备份重要数据后升级
 Author: Nifury
 Date: 19/06/2020
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `wx_account`;
CREATE TABLE `wx_account`  (
  `appid` char(20) CHARACTER SET utf8 NOT NULL COMMENT 'appid',
  `name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '公众号名称',
  `type` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '账号类型',
  `verified` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '认证状态',
  `secret` char(32) CHARACTER SET utf8 NOT NULL COMMENT 'appsecret',
  `token` varchar(32) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT 'token',
  `aes_key` varchar(43) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT 'aesKey',
  PRIMARY KEY (`appid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '公众号账号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_msg
-- ----------------------------
DROP TABLE IF EXISTS `wx_msg`;
CREATE TABLE `wx_msg`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `appid` char(20) CHARACTER SET utf8 NOT NULL COMMENT 'appid',
  `openid` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '微信用户ID',
  `in_out` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '消息方向',
  `msg_type` char(25) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT '消息类型',
  `detail` json NULL COMMENT '消息详情',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_appid`(`appid`) USING BTREE COMMENT 'appid'
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COMMENT = '微信消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_msg_reply_rule
-- ----------------------------
DROP TABLE IF EXISTS `msg_reply_rule`;
DROP TABLE IF EXISTS `wx_msg_reply_rule`;
CREATE TABLE `wx_msg_reply_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `appid` char(20) CHARACTER SET utf8 NULL DEFAULT '' COMMENT 'appid',
  `rule_name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '规则名称',
  `match_value` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '匹配的关键词、事件等',
  `exact_match` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否精确匹配',
  `reply_type` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '1' COMMENT '回复消息类型',
  `reply_content` varchar(1024) CHARACTER SET utf8 NOT NULL COMMENT '回复消息内容',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '规则是否有效',
  `desc` varchar(255) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT '备注说明',
  `effect_time_start` time(0) NULL DEFAULT '00:00:00' COMMENT '生效起始时间',
  `effect_time_end` time(0) NULL DEFAULT '23:59:59' COMMENT '生效结束时间',
  `priority` int(3) UNSIGNED NULL DEFAULT 0 COMMENT '规则优先级',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`rule_id`) USING BTREE,
  INDEX `idx_appid`(`appid`) USING BTREE COMMENT 'appid'
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COMMENT = '自动回复规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wx_msg_reply_rule
-- ----------------------------
INSERT INTO `wx_msg_reply_rule` VALUES (1, '', '关注回复', 'subscribe', 0, 'text', '你好，欢迎关注！', 1, '关注回复', '00:00:00', '23:59:59', 0, '2020-05-20 15:15:00');
INSERT INTO `wx_msg_reply_rule` VALUES (2, '', '默认回复', 'default', 0, 'text', '你好！', 1, '关注回复', '00:00:00', '23:59:59', 0, '2020-05-20 15:15:00');

-- ----------------------------
-- Table structure for wx_msg_template
-- ----------------------------
DROP TABLE IF EXISTS `msg_template`;
DROP TABLE IF EXISTS `wx_msg_template`;
CREATE TABLE `wx_msg_template`  (
  `id` bigint(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `appid` char(20) CHARACTER SET utf8 NOT NULL COMMENT 'appid',
  `template_id` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '公众号模板ID',
  `name` varchar(50) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT '模版名称',
  `title` varchar(20) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 NULL COMMENT '模板内容',
  `data` json NULL COMMENT '消息内容',
  `url` varchar(255) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT '链接',
  `miniprogram` json NULL COMMENT '小程序信息',
  `status` tinyint(1) UNSIGNED NOT NULL COMMENT '是否有效',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_name`(`name`) USING BTREE COMMENT '模板名称',
  INDEX `idx_status`(`status`) USING BTREE COMMENT '模板状态',
  INDEX `idx_appid`(`appid`) USING BTREE COMMENT 'appid'
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COMMENT = '消息模板' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for wx_qr_code
-- ----------------------------
DROP TABLE IF EXISTS `wx_qr_code`;
CREATE TABLE `wx_qr_code`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `appid` char(20) CHARACTER SET utf8 NOT NULL COMMENT 'appid',
  `is_temp` tinyint(1) NULL DEFAULT NULL COMMENT '是否为临时二维码',
  `scene_str` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '场景值ID',
  `ticket` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '二维码ticket',
  `url` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '二维码图片解析后的地址',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '该二维码失效时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '该二维码创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_appid`(`appid`) USING BTREE COMMENT 'appid'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COMMENT = '公众号带参二维码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_template_msg_log
-- ----------------------------
DROP TABLE IF EXISTS `template_msg_log`;
DROP TABLE IF EXISTS `wx_template_msg_log`;
CREATE TABLE `wx_template_msg_log`  (
  `log_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `appid` char(20) CHARACTER SET utf8 NOT NULL COMMENT 'appid',
  `touser` varchar(50) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT '用户openid',
  `template_id` varchar(50) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT 'templateid',
  `data` json NULL COMMENT '消息数据',
  `url` varchar(255) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT '消息链接',
  `miniprogram` json NULL COMMENT '小程序信息',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `send_result` varchar(255) CHARACTER SET utf8 NULL DEFAULT NULL COMMENT '发送结果',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_appid`(`appid`) USING BTREE COMMENT 'appid'
) ENGINE = InnoDB AUTO_INCREMENT = 116250 CHARACTER SET = utf8 COMMENT = '微信模版消息发送记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user`  (
  `openid` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '微信openid',
  `appid` char(20) CHARACTER SET utf8 NOT NULL COMMENT 'appid',
  `phone` char(11) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(50) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '昵称',
  `sex` tinyint(4) NULL DEFAULT NULL COMMENT '性别(0-未知、1-男、2-女)',
  `city` varchar(20) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '城市',
  `province` varchar(20) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '省份',
  `headimgurl` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '头像',
  `subscribe_time` datetime(0) NULL DEFAULT NULL COMMENT '订阅时间',
  `subscribe` tinyint(3) UNSIGNED NULL DEFAULT 1 COMMENT '是否关注',
  `unionid` varchar(50) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'unionid',
  `remark` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '备注',
  `tagid_list` json NULL COMMENT '标签ID列表',
  `subscribe_scene` varchar(50) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '关注场景',
  `qr_scene_str` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '扫码场景值',
  PRIMARY KEY (`openid`) USING BTREE,
  INDEX `idx_unionid`(`unionid`) USING BTREE COMMENT 'unionid',
  INDEX `idx_appid`(`appid`) USING BTREE COMMENT 'appid'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
