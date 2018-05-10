DROP TABLE IF EXISTS `egg_shop_user`;
DROP TABLE IF EXISTS `egg_shop_order`;
DROP TABLE IF EXISTS `egg_order_item`;
DROP TABLE IF EXISTS `egg_price`;
DROP TABLE IF EXISTS `egg_price_extra`;

CREATE TABLE `egg_shop_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '姓名',
  `country` varchar(128) NOT NULL DEFAULT '' COMMENT '村庄',
  `phone` varchar(16) NOT NULL DEFAULT '' COMMENT '手机号',
  `remark` longtext COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_country` (`country`),
  KEY `idx_phone` (`phone`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息';

CREATE TABLE `egg_shop_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user` int(11) NOT NULL COMMENT '用户外键',
  `bill` longtext COMMENT '账单',
  `status` varchar(16) NOT NULL DEFAULT '' COMMENT '状态',
  `remark` longtext COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息';

CREATE TABLE `egg_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `weight` decimal(5,1) NOT NULL COMMENT '重量,单位斤',
  `count` int(11) NOT NULL DEFAULT 0 COMMENT '数量',
  `level` int(11) NOT NULL DEFAULT 6 COMMENT '层数',
  `user` int(11) DEFAULT NULL COMMENT '用户外键',
  `order` int(11) DEFAULT NULL COMMENT '订单外键',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_weight` (`weight`),
  KEY `fk_user` (`user`),
  KEY `fk_level` (`level`),
  CONSTRAINT `fk_order` FOREIGN KEY (`order`) REFERENCES `egg_shop_order` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单子每斤';

CREATE TABLE `egg_price` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `day` varchar(10) NOT NULL DEFAULT '' COMMENT '日期:天 yyyy-MM-dd',
  `weight` decimal(11,1) NOT NULL COMMENT '斤',
  `price` decimal(11,1) NOT NULL COMMENT '价格',
  `level` int(11) NOT NULL DEFAULT 6 COMMENT '层数',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_day_weight` (`day`,`weight`),
  KEY `idx_day` (`day`),
  KEY `idx_weight` (`weight`),
  KEY `idx_price` (`price`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='价格';

CREATE TABLE `egg_price_extra` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `day` varchar(10) NOT NULL DEFAULT '' COMMENT '日期: yyyy-MM-dd',
  `weight_adjust` decimal(5,1) NOT NULL COMMENT '重量调整,参加计价',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_day` (`day`),
  KEY `idx_weight_adjust` (`weight_adjust`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='价格其他';