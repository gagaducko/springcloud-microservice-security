Drop DATABASE IF EXISTS auth_user;
CREATE DATABASE IF NOT EXISTS auth_user;

USE auth_user;

drop table if exists `sys_user`;
create table `sys_user` (
    `id` bigint(20) not null auto_increment comment '用户 id',
    `username` varchar(50) not null comment '用户名',
    `password` varchar(64) not null comment '用户密码',
    `is_account_non_expired` int(2) default '1' comment '帐户是否过期(1 未过期，0已过期)',
    `is_account_non_locked` int(2) default '1' comment '帐户是否被锁定(1 未过期，0已过期)',
    `is_credentials_non_expired` int(2) default '1' comment '密码是否过期(1 未过期，0已过期)',
    `is_enabled` int(2) default '1' comment '帐户是否可用(1 可用，0 删除用户)',
    `nick_name` varchar(64) default null comment '昵称',
    `mobile` varchar(20) default null comment '注册手机号',
    `email` varchar(50) default null comment '注册邮箱',
    `create_date` timestamp not null default current_timestamp,
    `update_date` timestamp not null default current_timestamp,
    primary key (`id`),
    unique key `username` (`username`) using btree,
    unique key `mobile` (`mobile`) using btree,
    unique key `email` (`email`) using btree
) engine=innodb auto_increment=11 default charset=utf8 comment='用户信息表';

drop table if exists `sys_role`;
create table `sys_role` (
    `id` bigint(20) not null auto_increment comment '角色 id',
    `name` varchar(64) not null comment '角色名称',
    `remark` varchar(200) default null comment '角色说明',
    `create_date` timestamp not null default current_timestamp,
    `update_date` timestamp not null default current_timestamp,
    primary key (`id`)
) engine=innodb auto_increment=12 default charset=utf8 comment='角色信息表';


drop table if exists `sys_permission`;
create table `sys_permission` (
    `id` bigint(20) not null auto_increment comment '权限 id',
    `parent_id` bigint(20) default null comment '父权限 id (0为顶级菜单)',
    `name` varchar(64) not null comment '权限名称',
    `code` varchar(64) default null comment '授权标识符',
    `url` varchar(255) default null comment '授权路径',
    `type` int(2) not null default '1' comment '类型(1菜单，2按钮)',
    `icon` varchar(200) default null comment '图标',
    `remark` varchar(200) default null comment '备注',
    `create_date` timestamp not null default current_timestamp,
    `update_date` timestamp not null default current_timestamp,
    primary key (`id`)
) engine=innodb auto_increment=33 default charset=utf8 comment='权限信息表';


drop table if exists `sys_role_permission`;
create table `sys_role_permission` (
    `id` bigint(20) not null auto_increment comment '主键 id',
    `role_id` bigint(20) not null comment '角色 id',
    `permission_id` bigint(20) not null comment '权限 id',
    primary key (`id`)
) engine=innodb auto_increment=26 default charset=utf8 comment='角色权限关联表';


drop table if exists `sys_user_role`;
create table `sys_user_role` (
    `id` bigint(20) not null auto_increment comment '主键 id',
    `user_id` bigint(20) not null comment '用户 id',
    `role_id` bigint(20) not null comment '角色 id',
    primary key (`id`)
) engine=innodb auto_increment=3 default charset=utf8 comment='用户角色关联表';