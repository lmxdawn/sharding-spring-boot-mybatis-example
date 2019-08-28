# 前言

项目初期没有强大的 DBA, 随着用户增加, 订单表越来越大, MySQL 的主机负载一直下不来, 导致吞吐降低

- GitHub 地址: https://github.com/lmxdawn/sharding-spring-boot-mybatis-example

# 订单业务分析

表结构:
```
CREATE TABLE `order_0` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned NOT NULL,
  `uid` bigint(20) unsigned NOT NULL,
  `money` int(11) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `modified_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4
```
## 问题分析:
 > 由于是单 `key` 业务, 只需要通过 `uid` 来查询, 则按照 `uid` 来做分表因子

# 分库中间件

> 经过一些权衡, 还是准备使用 [shardingsphere](https://shardingsphere.apache.org/index_zh.html) ,社区活跃度也比较不错

# 开始使用
jdk 8 , shardingsphere 版本 4.0.0-RC2
## 路由规则
- *分 2 个库, 一个库 4 张表*
- 路由计算, 库: uid % 2个库
- 路由计算, 表: uid / 2个库 % 4张表 (取商后再取余, 这里要注意取商有小数需要强转为整型后再取余)

| uid | uid % 2 (库) | uid / 2 % 4 (表) |
|---|---|---|
| 1 | 1 | 0 |
| 2 | 0 | 1 |
| 3 | 1 | 1 |
| 4 | 0 | 2 |
| 5 | 1 | 2 |
| 6 | 0 | 3 |
| 7 | 1 | 3 |
| 8 | 0 | 0 |

## MySQL 结构
- 先创建两个库, db0 和 db1
- 把下面的结构分别导入两个库中
```
CREATE TABLE `order_0` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned NOT NULL,
  `uid` bigint(20) unsigned NOT NULL,
  `money` int(11) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `modified_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4
CREATE TABLE `order_1` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned NOT NULL,
  `uid` bigint(20) unsigned NOT NULL,
  `money` int(11) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `modified_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4
CREATE TABLE `order_2` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned NOT NULL,
  `uid` bigint(20) unsigned NOT NULL,
  `money` int(11) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `modified_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4
CREATE TABLE `order_3` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) unsigned NOT NULL,
  `uid` bigint(20) unsigned NOT NULL,
  `money` int(11) NOT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `modified_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4
```


## spring boot 的 pom.xml
```
<!--web-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!--自动生成 GET SET-->
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<optional>true</optional>
</dependency>

<!--连接池-->
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>1.1.12</version>
</dependency>

<!--MySQL-->
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>

<!--mybatis -->
<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>1.3.2</version>
</dependency>

<dependency>
	<groupId>org.apache.shardingsphere</groupId>
	<artifactId>sharding-jdbc-spring-boot-starter</artifactId>
	<version>4.0.0-RC2</version>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>
```
## spring boot 的 application-sharding-databases.properties
```
spring.shardingsphere.datasource.names=db0,db1

spring.shardingsphere.datasource.db0.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.db0.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.db0.url=jdbc:mysql://localhost:3306/db0?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull
spring.shardingsphere.datasource.db0.username=root
spring.shardingsphere.datasource.db0.password=root

spring.shardingsphere.datasource.db1.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.db1.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.db1.url=jdbc:mysql://localhost:3306/db1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull
spring.shardingsphere.datasource.db1.username=root
spring.shardingsphere.datasource.db1.password=root

# 是否显示SQL语句
spring.shardingsphere.props.sql.show=true

# 配置数据库和表
spring.shardingsphere.sharding.tables.order.actual-data-nodes=db$->{0..1}.order_$->{0..1}
# 配置根据哪个字段选择数据库
spring.shardingsphere.sharding.tables.order.database-strategy.inline.sharding-column=uid
# 配置选择哪个数据库的规则
spring.shardingsphere.sharding.tables.order.database-strategy.inline.algorithm-expression=db$->{uid % 2}
# 配置选择根据哪个字段选择表
spring.shardingsphere.sharding.tables.order.table-strategy.inline.sharding-column=uid
# 配置选择哪个表的规则
spring.shardingsphere.sharding.tables.order.table-strategy.inline.algorithm-expression=order_$->{(Integer)(uid / 2) % 4}

```

## 启动 java web 程序
> 需要注意, 查询订单详情需要带上用户 uid, 用来路由到对应的库和表
- 创建订单: http://localhost:8080/order/create?uid=1
- 订单列表:http://localhost:8080/order/lists?uid=1&page=1&limit=1
- 订单详情:http://localhost:8080/order/detail?uid=1&orderId=373591446294364161



# 相关文章

[多key业务，数据库水平切分架构一次搞定 ](https://mp.weixin.qq.com/s?__biz=MjM5ODYxMDA5OQ==&mid=2651960373&idx=1&sn=abf7d36840c4d3d556b17a8776ee536c&chksm=bd2d01e98a5a88ff0cbf615cb3444668ccdfca58d5dca2da00ed0cc8948585b7509adf648db0&mpshare=1&scene=1&srcid=0121G56TmDMmVjTmLICPMO7r#rd)

[大众点评订单系统分库分表实践](https://mp.weixin.qq.com/s/ZclBXjyvoInUspKSppBvlg?)

> 如果不当之处, 欢迎指出, 共同成长