
-- datebase name zk_monitor  

CREATE TABLE `zk_cluster` (
  `id`  bigint(20) unsigned NOT NULL,
  `name` varchar(64) COMMENT '集群名称',
  `status`  smallint unsigned DEFAULT '0' COMMENT ' 0-已上线,1-已下线, 2-已停用, 3-已废弃',
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

CREATE TABLE `zk_item` (
  `id` bigint(20) unsigned NOT NULL,
  `cluster_id` bigint(20) unsigned NOT NULL COMMENT '引用zk_cluster.id',
  `zk_ip` char(15) COMMENT 'zkIP',
  `zk_port` int unsigned COMMENT '端口号',
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

CREATE TABLE `zk_inst_monitor` (
  `id` bigint(20) unsigned NOT NULL,
  `item_id` bigint(20) unsigned NOT NULL COMMENT '引用zk_item.id',
  `zk_ip` char(15) COMMENT 'zkIP',
  `zk_port` int unsigned COMMENT '端口号',
  `mode` varchar(255) COMMENT 'follower Or leader',
  `connections` int unsigned DEFAULT '0' COMMENT '客户端连接数',
  `node_count` int unsigned DEFAULT '0' COMMENT '节点数',
  `send_packet_qty` bigint(20) unsigned DEFAULT '0' COMMENT '发送包数量',
  `recv_packet_qty` bigint(20) unsigned DEFAULT '0' COMMENT '接收包数量',
  `min_latency` int unsigned DEFAULT '0' COMMENT '最小延迟',
  `avg_latency` int unsigned DEFAULT '0' COMMENT '平均延迟',
  `mzx_latency` int unsigned DEFAULT '0' COMMENT '最大延迟',
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

CREATE TABLE `zk_client_monitor` (
  `id` bigint(20) unsigned NOT NULL,
  `item_id` bigint(20) unsigned NOT NULL COMMENT '引用zk_item.id',
  `zk_monitor_id` bigint(20) unsigned NOT NULL COMMENT '引用zk_monitor.id',
  `client_ip` char(15) COMMENT 'zkIP',
  `client_port` int unsigned COMMENT '端口号',
  `send_packet_qty` bigint(20) unsigned DEFAULT '0' COMMENT '发送包数量',
  `recv_packet_qty` bigint(20) unsigned DEFAULT '0' COMMENT '接收包数量',
  `establish_time` datetime  COMMENT '连接建立时间',
  `last_resp_time` datetime  COMMENT '连接最后响应时间',
  `max_latency` int unsigned DEFAULT '0' COMMENT '最大延迟',
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

