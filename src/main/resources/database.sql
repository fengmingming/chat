CREATE TABLE chat.`account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT not null comment '租户id',
  `account` VARCHAR(100)  not null comment '账号',
  `password` VARCHAR(255) null comment '密码',
  `nick_name` VARCHAR(100) null comment '昵称',
  `profile_picture` VARCHAR(255) null comment '头像',
  `create_time` DATETIME not null comment '创建时间',
  `update_time` DATETIME null comment '修改时间',
  PRIMARY KEY (`id`),
  unique key tenant_account (tenant_id, `account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment='账号';

CREATE TABLE chat.`group` (
  `groupId` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT not null comment '租户id',
  `group_name` VARCHAR(100) not null comment '组名称',
  `state` INT not null comment '组状态',
  `deleted` BIGINT not null default 0 comment '删除状态',
  `create_time` DATETIME not null comment '创建时间',
  `update_time` DATETIME null comment '修改时间',
  PRIMARY KEY (`groupId`),
  key tenant (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment='群组';

CREATE TABLE chat.`group_member` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT not null comment '租户id',
  `group_id` BIGINT not null comment '群组id',
  `account_id` BIGINT not null comment '账号id',
  `role` VARCHAR(255) null comment '角色',
  `deleted` BIGINT DEFAULT 0 comment '删除标识',
  `create_time` DATETIME not null comment '创建时间',
  PRIMARY KEY (`id`),
  key tenant_group (tenant_id, group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '群组成员';

CREATE TABLE chat.`message` (
  `msg_id` BIGINT NOT NULL AUTO_INCREMENT comment '',
  `tenant_id` BIGINT not null comment '租户id',
  `from` VARCHAR(100) not null comment '消息来源账号',
  `to` VARCHAR(100) not null comment '消息接收账号',
  `msg_type` VARCHAR(10) not null comment '消息类型',
  `message` TEXT null comment '消息内容',
  primary key (msg_id),
  key tenant_to (tenant_id, `to`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment='消息';

CREATE TABLE chat.`relationship` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_id` BIGINT not null comment '租户id',
  `account_id` BIGINT not null comment '账号id',
  `friend_id` BIGINT not null comment '友人账号id',
  `state` INT not null default 1 comment '状态 1 正常',
  `deleted` BIGINT DEFAULT 0 comment '删除标识',
  `create_time` DATETIME not null comment '创建时间',
  `update_time` DATETIME null comment '修改时间',
  PRIMARY KEY (`id`),
  key tenant_account (tenant_id, account_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment='好友关系表';

CREATE TABLE chat.`tenant` (
  `tenant_id` BIGINT NOT NULL AUTO_INCREMENT,
  `tenant_name` VARCHAR(100) not null comment '租户名称',
  `phone` VARCHAR(50) not null comment '联系方式' ,
  `password` VARCHAR(255) null comment '密码',
  `lease_deadline_date` DATETIME null comment '租期截至日期',
  `sign_secret` VARCHAR(100) not null comment '签名密钥',
  `public_key` varchar(2048) null comment '公钥',
  `private_key` varchar(2048) null comment '私钥',
  `state` INT not null DEFAULT 1 comment '状态 1 初始化',
  `create_time` DATETIME not null comment '创建时间',
  `update_time` DATETIME null comment '修改时间',
  PRIMARY KEY (`tenant_id`),
  unique key (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment='租户';