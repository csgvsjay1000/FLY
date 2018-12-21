
DROP TABLE IF EXISTS `master_item`;
CREATE TABLE `master_item` (
`id`  bigint(20) UNSIGNED NOT NULL ,
`master_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点名称',
`master_redis_ip`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP' ,
`master_redis_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '端口号' ,
`total_slaves`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '计划从服务器总节点数' ,
`current_slaves`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '当前可用服务器节点数' ,
`total_sentinels`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '计划总sentinel节点数' ,
`current_sentinels`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '当前可用节点数' ,
`quorum`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '判断节点下线确定数' ,
`down_after_milliseconds`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '判断节点下线阀值' ,
`failover_timeout`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '故障转移超时时间' ,
`parallel_syncs`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '故障转移时并发复制数量' ,
`enable`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前集群是否可以, OK, NOT_OK',
`status`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前集群状态,是否停用,Normal ',
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


DROP TABLE IF EXISTS `master_item_monitor`;
CREATE TABLE `master_item_monitor` (
`id`  bigint(20) UNSIGNED NOT NULL ,
`master_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点名称',
`master_redis_ip`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP' ,
`master_redis_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '端口号' ,
`total_slaves`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '计划从服务器总节点数' ,
`current_slaves`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '当前可用服务器节点数' ,
`total_sentinels`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '计划总sentinel节点数' ,
`current_sentinels`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '当前可用节点数' ,
`quorum`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '判断节点下线确定数' ,
`down_after_milliseconds`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '判断节点下线阀值' ,
`failover_timeout`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '故障转移超时时间' ,
`parallel_syncs`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '故障转移时并发复制数量' ,
`enable`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前集群是否可以, OK, NOT_OK',
`status`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前集群状态,是否停用,Normal ',
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

DROP TABLE IF EXISTS `sentinel_redis_log`;
CREATE TABLE `sentinel_redis_log` (
`id`  bigint(20) UNSIGNED NOT NULL ,
`sentinel_ip`  char(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP' ,
`sentinel_port`  int(10) UNSIGNED NULL DEFAULT NULL COMMENT '端口号' ,
`channel`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '频道',
`ch_log`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中文日志',
`en_log`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文日志',
`content`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息内容',
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