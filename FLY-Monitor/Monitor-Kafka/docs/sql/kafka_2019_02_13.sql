/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : redis_monitor

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-14 14:54:57
*/

SET FOREIGN_KEY_CHECKS=0;


-- ----------------------------
-- Table structure for `redis_inst_monitor`
-- ----------------------------
DROP TABLE IF EXISTS `broker_inst_monitor`;
CREATE TABLE `broker_inst_monitor` (
`id`  bigint(20) UNSIGNED NOT NULL ,
`item_id`  int(10) UNSIGNED NOT NULL COMMENT '引用zk_item.id' ,
`broker_ip`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'brokerip' ,
`broker_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '' ,
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
DROP TABLE IF EXISTS `broker_item`;
CREATE TABLE `broker_item` (
`id`  int(10) UNSIGNED NOT NULL ,
`broker_ip`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'brokerip' ,
`broker_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '' ,
`jmx_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '' ,
`status`  smallint(5) UNSIGNED NULL DEFAULT 0 COMMENT ' 0-已上线,1-已下线, 2-已停用, 3-已废弃' ,
`status_update`  datetime NOT NULL COMMENT '对应状态操作时间' ,
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

DROP TABLE IF EXISTS `broker_item_detail`;
CREATE TABLE `broker_item_detail` (
`id`  int(10) UNSIGNED NOT NULL ,
`broker_ip`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'brokerip' ,
`broker_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '' ,
`broker_controller`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '1为正常' ,
`under_replicated_partitions`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '备份不足分区数，0最好，长时间大于0则broker有问题' ,
`leader_count`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT 'leader分区数' ,
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

DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
`id`  int(10) UNSIGNED NOT NULL ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'topic名字' ,
`partitions`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '分区数' ,
`replications`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '备份因子' ,
`log_size`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '消息数' ,
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

DROP TABLE IF EXISTS `topic_partition`;
CREATE TABLE `topic_partition` (
`id`  int(10) UNSIGNED NOT NULL ,
`topic_id`  int(10) UNSIGNED NOT NULL ,
`topic_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'topic名字' ,
`consumer_id`  int(10) UNSIGNED NOT NULL COMMENT '引用consumer表id' ,
`consumer_client_id`  int(10) UNSIGNED NOT NULL COMMENT '引用consumer表 标识符' ,
`partitions`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '分区号' ,
`log_size`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '消息数' ,
`consumer_offset`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '消费者offset' ,
`lag`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '消费者消费滞后数' ,
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

DROP TABLE IF EXISTS `consumer`;
CREATE TABLE `consumer` (
`id`  int(10) UNSIGNED NOT NULL ,
`group_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'consumer group 标识符' ,
`client_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'consumer 标识符' ,
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


