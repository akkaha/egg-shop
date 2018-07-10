alter table egg_shop_order add column `day_order` int(11) NOT NULL DEFAULT 0 COMMENT '每日序号' after id;

#2018-07-10
alter table egg_shop_user add column `ext` longtext COMMENT '扩展信息' after remark;