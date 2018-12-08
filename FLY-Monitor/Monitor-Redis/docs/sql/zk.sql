
-- datebase name redis_monitor  

CREATE TABLE `redis_cluster` (
  `id`  bigint(20) unsigned NOT NULL,
  `name` varchar(64) COMMENT '集群名称',
  `status` smallint unsigned DEFAULT '0' COMMENT ' 0-已上线,1-已下线, 2-已停用, 3-已废弃',
  `status_update` datetime NOT NULL COMMENT '对应状态操作时间',
  `node_count` int unsigned DEFAULT '0' COMMENT '节点总数',
  `enable_node_count` int unsigned DEFAULT '0' COMMENT '可用节点总数',
  `cluster_uptime` datetime NOT NULL COMMENT '集群上线时间',
  `remark` varchar(255) DEFAULT NULL,
  `attr01` varchar(255) DEFAULT NULL,
  `attr02` varchar(255) DEFAULT NULL,
  `attr03` varchar(255) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `creater` bigint(20) NOT NULL,
  `updater` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `redis_item` (
  `id` int unsigned NOT NULL,
  `cluster_id` bigint(20) unsigned NOT NULL COMMENT '引用zk_cluster.id',
  `redis_ip` char(15) COMMENT 'redisIP',
  `redis_port` int unsigned COMMENT '端口号',
  `status`  smallint unsigned DEFAULT '0' COMMENT ' 0-已上线,1-已下线, 2-已停用, 3-已废弃',
  `status_update` datetime NOT NULL COMMENT '对应状态操作时间',
  `remark` varchar(255) DEFAULT NULL,
  `attr01` varchar(255) DEFAULT NULL,
  `attr02` varchar(255) DEFAULT NULL,
  `attr03` varchar(255) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `creater` bigint(20) NOT NULL,
  `updater` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `redis_inst_monitor` (
  `id` bigint(20) unsigned NOT NULL,
  `item_id` int unsigned NOT NULL COMMENT '引用zk_item.id',
  `master_item_id` int unsigned DEFAULT NULL COMMENT '主实例ID 引用zk_item.id',
  `redis_ip` char(15) COMMENT 'redisIP',
  `redis_port` int unsigned COMMENT '端口号',
  `role` varchar(255) COMMENT 'master Or slave',
  `mode` varchar(255) COMMENT 'follower Or leader',
  `connections` int unsigned DEFAULT '0' COMMENT '客户端连接数',
  `used_memory` bigint(20) unsigned DEFAULT '0' COMMENT '已使用内存,单位byte',
  `max_memory` bigint(20) unsigned DEFAULT '0' COMMENT '总内存,单位byte',
  `obj_count` bigint(20) unsigned DEFAULT '0' COMMENT '对象数，即键值对数',
  `keyspace_hits` int unsigned DEFAULT '0' COMMENT '命中次数',
  `keyspace_misses` int unsigned DEFAULT '0' COMMENT '未命中次数',
  `mem_fragmentation_ratio`  DECIMAL(4,2) COMMENT '内存碎片率',
  `qps`  DECIMAL(6,2) COMMENT '每秒请求数',
  `remark` varchar(255) DEFAULT NULL,
  `attr01` varchar(255) DEFAULT NULL,
  `attr02` varchar(255) DEFAULT NULL,
  `attr03` varchar(255) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


