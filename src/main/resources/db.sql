#  seckill 项目建表语句

#秒杀商品表
create database seckill;
use seckill;
create table seckill_item (
	id int(8),
    name varchar(30),
    price float(8,2),
    no int(8),
    create_time datetime,
    start_time datetime,
    end_time datetime
)character set utf8;
alter table seckill_item add primary key(id);
alter table seckill_item modify id int(8) auto_increment;

#订单表
create table seckill_order(
	id int(8),
    item_id int(8),
    user_id int(8),
    state tinyint(4),
    create_time datetime
)character set utf8;
alter table seckill_order add primary key(id);
alter table seckill_order modify id int(8) auto_increment;

#用户表
create table user(
	uid int(8),
    uname varchar(10),
    uphone varchar(20),
    upassword varchar(32)
)character set utf8;
alter table user add primary key(uid);
alter table user modify uid int(8) auto_increment;

select * from seckill_item;
select * from seckill_order;
alter table seckill_order modify order_code varchar(45) not null;



