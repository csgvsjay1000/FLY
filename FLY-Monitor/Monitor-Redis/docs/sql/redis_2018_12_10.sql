/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : redis_monitor

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-10 10:40:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `redis_cluster`
-- ----------------------------
DROP TABLE IF EXISTS `redis_cluster`;
CREATE TABLE `redis_cluster` (
`id`  bigint(20) UNSIGNED NOT NULL ,
`name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集群名称' ,
`status`  smallint(5) UNSIGNED NULL DEFAULT 0 COMMENT ' 0-已上线,1-已下线, 2-已停用, 3-已废弃' ,
`status_update`  datetime NOT NULL COMMENT '对应状态操作时间' ,
`node_count`  int(10) UNSIGNED NULL DEFAULT 0 COMMENT '节点总数' ,
`enable_node_count`  int(10) UNSIGNED NULL DEFAULT 0 COMMENT '可用节点总数' ,
`cluster_uptime`  datetime NOT NULL COMMENT '集群上线时间' ,
`remark`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr01`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr02`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr03`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_date`  datetime NOT NULL ,
`update_date`  datetime NOT NULL ,
`creater`  bigint(20) NOT NULL ,
`updater`  bigint(20) NOT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `redis_inst_monitor`
-- ----------------------------
DROP TABLE IF EXISTS `redis_inst_monitor`;
CREATE TABLE `redis_inst_monitor` (
`id`  bigint(20) UNSIGNED NOT NULL ,
`item_id`  int(10) UNSIGNED NOT NULL COMMENT '引用zk_item.id' ,
`master_item_id`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '主实例ID 引用zk_item.id' ,
`redis_ip`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'redisIP' ,
`redis_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '端口号' ,
`role`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'master Or slave' ,
`mode`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'follower Or leader' ,
`connections`  int(10) UNSIGNED NULL DEFAULT 0 COMMENT '客户端连接数' ,
`used_memory`  bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '已使用内存,单位byte' ,
`max_memory`  bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '总内存,单位byte' ,
`obj_count`  bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '对象数，即键值对数' ,
`keyspace_hits`  int(10) UNSIGNED NULL DEFAULT 0 COMMENT '命中次数' ,
`keyspace_misses`  int(10) UNSIGNED NULL DEFAULT 0 COMMENT '未命中次数' ,
`mem_fragmentation_ratio`  decimal(10,2) NULL DEFAULT NULL COMMENT '内存碎片率' ,
`qps`  decimal(8,2) NULL DEFAULT NULL COMMENT '每秒请求数' ,
`cpu_ratio`  decimal(6,2) NULL DEFAULT NULL ,
`remark`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr01`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr02`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr03`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_date`  datetime NOT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `redis_item`
-- ----------------------------
DROP TABLE IF EXISTS `redis_item`;
CREATE TABLE `redis_item` (
`id`  int(10) UNSIGNED NOT NULL ,
`cluster_id`  bigint(20) UNSIGNED NOT NULL COMMENT '引用zk_cluster.id' ,
`redis_ip`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'redisIP' ,
`redis_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '端口号' ,
`status`  smallint(5) UNSIGNED NULL DEFAULT 0 COMMENT ' 0-已上线,1-已下线, 2-已停用, 3-已废弃' ,
`status_update`  datetime NOT NULL COMMENT '对应状态操作时间' ,
`slowlog-log-slower-than`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '慢日志阀值' ,
`slowlog-max-len`  int(11) UNSIGNED NULL DEFAULT NULL COMMENT '保存最大长度' ,
`remark`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr01`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr02`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`attr03`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`create_date`  datetime NOT NULL ,
`update_date`  datetime NOT NULL ,
`creater`  bigint(20) NOT NULL ,
`updater`  bigint(20) NOT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;
