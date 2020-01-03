/*
 Navicat Premium Data Transfer

 Source Server         : 腾讯云服务器
 Source Server Type    : MySQL
 Source Server Version : 50646
 Source Host           : 118.89.36.191:3306
 Source Schema         : classmanager

 Target Server Type    : MySQL
 Target Server Version : 50646
 File Encoding         : 65001

 Date: 03/01/2020 18:17:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for database_source
-- ----------------------------
DROP TABLE IF EXISTS `database_source`;
CREATE TABLE `database_source`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_source_name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源的别名',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '连接信息',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `pass_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `driver_class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库驱动',
  `database_type` tinyint(10) NOT NULL DEFAULT 0 COMMENT '数据库类型(0-mysql,1-oracle)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
