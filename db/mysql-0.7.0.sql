#grant all privileges on dbname.tablename to 'username'@'%';
# 比如想给用户nacos赋予数据库test所有的表的权限并且不限制root用户的连接地址，代码如下
grant all privileges on wx.* to 'wx'@'%';
# 刷新权限
flush privileges;

create table wx_account
(
  appid    char(20)                     not null
  comment 'appid'
    primary key,
  name     varchar(50)                  not null
  comment '公众号名称',
  type     tinyint unsigned default '1' null
  comment '账号类型',
  verified tinyint unsigned default '1' null
  comment '认证状态',
  secret   char(32)                     not null
  comment 'appsecret',
  token    varchar(32)                  null
  comment 'token',
  aes_key  varchar(43)                  null
  comment 'aesKey'
)
  comment '公众号账号'
  charset = utf8;

create table wx_msg
(
  id          bigint auto_increment
  comment '主键'
    primary key,
  appid       char(20) charset utf8              not null
  comment 'appid',
  openid      varchar(32) charset utf8           not null
  comment '微信用户ID',
  in_out      tinyint unsigned                   null
  comment '消息方向',
  msg_type    char(25) charset utf8              null
  comment '消息类型',
  detail      json                               null
  comment '消息详情',
  create_time datetime default CURRENT_TIMESTAMP null
  comment '创建时间'
)
  comment '微信消息'
  charset = utf8mb4;

create index idx_appid
  on wx_msg (appid)
  comment 'appid';

create table wx_msg_reply_rule
(
  rule_id           int auto_increment
    primary key,
  appid             char(20) default ''                null
  comment 'appid',
  rule_name         varchar(20)                        not null
  comment '规则名称',
  match_value       varchar(200)                       not null
  comment '匹配的关键词、事件等',
  exact_match       tinyint(1) default '0'             not null
  comment '是否精确匹配',
  reply_type        varchar(20) default '1'            not null
  comment '回复消息类型',
  reply_content     varchar(1024)                      not null
  comment '回复消息内容',
  status            tinyint(1) default '1'             not null
  comment '规则是否有效',
  `desc`            varchar(255)                       null
  comment '备注说明',
  effect_time_start time default '00:00:00'            null
  comment '生效起始时间',
  effect_time_end   time default '23:59:59'            null
  comment '生效结束时间',
  priority          int unsigned default '0'           null
  comment '规则优先级',
  update_time       datetime default CURRENT_TIMESTAMP not null
  on update CURRENT_TIMESTAMP
  comment '修改时间'
)
  comment '自动回复规则'
  charset = utf8;

create index idx_appid
  on wx_msg_reply_rule (appid)
  comment 'appid';

create table wx_user
(
  openid          varchar(50)                  not null
  comment '微信openid'
    primary key,
  appid           char(20) charset utf8        not null
  comment 'appid',
  phone           char(11)                     null
  comment '手机号',
  nickname        varchar(50)                  null
  comment '昵称',
  sex             tinyint                      null
  comment '性别(0-未知、1-男、2-女)',
  city            varchar(20)                  null
  comment '城市',
  province        varchar(20)                  null
  comment '省份',
  headimgurl      varchar(255)                 null
  comment '头像',
  subscribe_time  datetime                     null
  comment '订阅时间',
  subscribe       tinyint unsigned default '1' null
  comment '是否关注',
  unionid         varchar(50)                  null
  comment 'unionid',
  remark          varchar(255)                 null
  comment '备注',
  tagid_list      json                         null
  comment '标签ID列表',
  subscribe_scene varchar(50)                  null
  comment '关注场景',
  qr_scene_str    varchar(64)                  null
  comment '扫码场景值',
  is_blacklist    int default '0'              not null
  comment '是否黑名单用户，0是黑名单',
)
  comment '用户表'
  charset = utf8mb4;

create index idx_appid
  on wx_user (appid)
  comment 'appid';

create index idx_unionid
  on wx_user (unionid)
  comment 'unionid';

INSERT INTO `wx_msg_reply_rule` VALUES (1, '', '关注回复', 'subscribe', 0, 'text', '你好，欢迎关注！', 1, '关注回复', '00:00:00', '23:59:59', 0, '2020-05-20 15:15:00');
INSERT INTO `wx_msg_reply_rule` VALUES (2, '', '默认回复', 'default', 0, 'text', '你好！', 1, '关注回复', '00:00:00', '23:59:59', 0, '2020-05-20 15:15:00');



-- auto-generated definition
create table wx_date_cube_user
(
  appid         char(20) default ''                null
  comment '微信公众号appid',
  ref_date      datetime                           not null
  comment '数据记录时间',
  user_source   int default 0                      not null
  comment '用户的渠道',
  new_user      int default 0                      not null
  comment '新增的用户数量',
  cancel_user   int default 0                      not null
  comment '取消关注的用户数量',
  cumulate_user int default 0                      not null
  comment '总用户量',

  is_deleted    int default 0                      not null
  comment '是否删除（1是；0否）',
  create_time   datetime default CURRENT_TIMESTAMP not null
  comment '创建时间',
  update_time   datetime default CURRENT_TIMESTAMP not null
  on update CURRENT_TIMESTAMP
  comment '修改时间'
)
  comment '微信公众号用户数据分析表';

