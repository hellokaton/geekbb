# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.19)
# Database: geek_dev
# Generation Time: 2018-05-09 13:24:47 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table comments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `cid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'comment表主键',
  `tid` varchar(32) NOT NULL DEFAULT '0' COMMENT 'post表主键,关联字段',
  `author` varchar(50) NOT NULL DEFAULT '' COMMENT '评论作者',
  `owner` varchar(50) NOT NULL DEFAULT '0' COMMENT '评论所属内容作者id',
  `content` text NOT NULL COMMENT '评论内容',
  `created` datetime NOT NULL COMMENT '评论生成时的GMT unix时间戳',
  PRIMARY KEY (`cid`),
  KEY `idx_tid` (`tid`),
  KEY `idx_author` (`author`),
  KEY `idx_created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;

INSERT INTO `comments` (`cid`, `tid`, `author`, `owner`, `content`, `created`)
VALUES
	(1,'me64tocwfgo72krrgm','otuta','biezhi','Hello Boy.','2018-04-07 23:42:31'),
	(2,'5loni7cwf62okqyyd0','jiqing112','jiqing112','![alt](http://ww1.sinaimg.cn/large/6af89bc8gw1f8scs1xdttj20g40aq74v.jpg)','2018-04-08 10:46:46'),
	(3,'5loni7cwf62okqyyd0','jiqing112','jiqing112','回复的图片怎么看不到','2018-04-08 10:47:20'),
	(4,'5loni7cwf62okqyyd0','jiqing112','jiqing112','http://ww1.sinaimg.cn/large/6af89bc8gw1f8scs1xdttj20g40aq74v.jpg\n![alt]()\n接下来是md插入图片的语法\n\n![alt](http://ww1.sinaimg.cn/large/6af89bc8gw1f8scs1xdttj20g40aq74v.jpg)','2018-04-08 10:48:29'),
	(5,'5loni7cwf62okqyyd0','jiqing112','jiqing112','富文本编辑模式，也不支持换行回车。//这里有个回车\n不然你试试','2018-04-08 10:49:16'),
	(6,'5loni7cwf62okqyyd0','javaor','jiqing112','我也不知道为什么我点进来了...','2018-04-08 13:05:03'),
	(7,'me64tocwfgo72krrgm','javaor','biezhi','Hello 女装大佬','2018-04-08 13:05:48'),
	(8,'me64tocwfgo72krrgm','sunzhenyucn','biezhi','`Test Markdown` ','2018-04-08 13:17:55'),
	(9,'5loni7cwf62okqyyd0','zhaoweih','jiqing112','来看看楼主如何装逼','2018-04-08 13:32:44'),
	(10,'5loni7cwf62okqyyd0','biezhi','jiqing112','![alt](http://ww1.sinaimg.cn/large/6af89bc8gw1f8scs1xdttj20g40aq74v.jpg)','2018-04-08 14:45:35'),
	(11,'kr6xb7cdf3low5rr3p','biezhi','JZFamily','等稳定一点再开源，还有很多没做的 :)','2018-04-08 16:25:48'),
	(12,'me64tocwfgo72krrgm','chencn','biezhi','```js\ntest\n```','2018-04-08 16:43:18'),
	(13,'5loni7cwf62okqyyd0','otuta','jiqing112','装逼是一门艺术 :grinning:','2018-04-08 23:53:57'),
	(14,'5loni7cwf62okqyyd0','biezhi','jiqing112','feed 流','2018-04-14 21:19:32'),
	(15,'kr6xb7cdf3low5rr3p','biezhi','JZFamily',' :stuck_out_tongue: 我是一个小可爱','2018-04-14 21:38:15'),
	(16,'5loni7cwf62okqyyd0','biezhi','jiqing112','Hello World我知道了。。。\n','2018-04-18 22:46:39');

/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ip_hits
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ip_hits`;

CREATE TABLE `ip_hits` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `created` datetime NOT NULL COMMENT '创建时间',
  `expired` datetime NOT NULL COMMENT '解封时间',
  PRIMARY KEY (`id`),
  KEY `idx_ip` (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='IP黑名单';



# Dump of table logs
# ------------------------------------------------------------

DROP TABLE IF EXISTS `logs`;

CREATE TABLE `logs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '操作用户',
  `action` varchar(1000) NOT NULL DEFAULT '' COMMENT '动作',
  `ip` varchar(20) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `user_agent` varchar(200) NOT NULL DEFAULT '' COMMENT 'UserAgent',
  `created` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_ip` (`ip`),
  KEY `idx_created` (`created`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='系统日志表';

# Dump of table nodes
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nodes`;

CREATE TABLE `nodes` (
  `nid` varchar(100) NOT NULL DEFAULT '',
  `pid` varchar(100) NOT NULL DEFAULT '',
  `title` varchar(50) NOT NULL DEFAULT '',
  `image` varchar(100) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `topics` int(11) NOT NULL,
  `state` tinyint(2) NOT NULL DEFAULT '0',
  `created` datetime NOT NULL,
  PRIMARY KEY (`nid`),
  KEY `idx_topics` (`topics`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节点表';

LOCK TABLES `nodes` WRITE;
/*!40000 ALTER TABLE `nodes` DISABLE KEYS */;

INSERT INTO `nodes` (`nid`, `pid`, `title`, `image`, `description`, `topics`, `state`, `created`)
VALUES
	('affair','general','站务',NULL,NULL,1,1,'2018-04-06 11:23:00'),
	('architecture','progaraming','架构',NULL,NULL,0,1,'2018-04-04 00:00:00'),
	('echo','general','Echo',NULL,NULL,2,1,'2018-04-06 11:23:00'),
	('general','','常规',NULL,NULL,0,1,'2018-04-04 00:00:00'),
	('golang','progaraming','Golang',NULL,NULL,1,1,'2018-04-04 00:00:00'),
	('java8','progaraming','Java 8',NULL,NULL,0,1,'2018-04-04 00:00:00'),
	('progaraming','','编程',NULL,NULL,0,1,'2018-04-04 00:00:00'),
	('program-art','progaraming','编码艺术',NULL,NULL,0,1,'2018-04-04 00:00:00'),
	('qaa','general','问与答',NULL,NULL,0,1,'2018-04-04 00:00:00'),
	('share','general','分享创造',NULL,NULL,1,1,'2018-04-04 00:00:00'),
	('vps','general','VPS 主机',NULL,NULL,0,1,'2018-04-04 00:00:00'),
	('vscode','progaraming','vscode',NULL,NULL,1,1,'2018-04-04 00:00:00');

/*!40000 ALTER TABLE `nodes` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table notices
# ------------------------------------------------------------

DROP TABLE IF EXISTS `notices`;

CREATE TABLE `notices` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT '' COMMENT '标题',
  `to_user` varchar(50) NOT NULL DEFAULT '' COMMENT '发送给',
  `from_user` varchar(50) NOT NULL DEFAULT '' COMMENT '来自',
  `event` varchar(50) NOT NULL DEFAULT '' COMMENT '事件类型',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:未读 1:已读',
  `created` datetime NOT NULL COMMENT '通知创建时间',
  `updated` datetime DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`),
  KEY `idx_to_user` (`to_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息通知表';



# Dump of table profiles
# ------------------------------------------------------------

DROP TABLE IF EXISTS `profiles`;

CREATE TABLE `profiles` (
  `uid` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `followers` int(11) DEFAULT '0' COMMENT '粉丝数',
  `location` varchar(200) DEFAULT NULL COMMENT '所在位置',
  `website` varchar(200) DEFAULT NULL COMMENT '个人主页',
  `github` varchar(200) DEFAULT NULL COMMENT 'github账号',
  `weibo` varchar(200) DEFAULT NULL COMMENT '微博账号',
  `zhihu` varchar(200) DEFAULT NULL COMMENT '知乎账号',
  `twitter` varchar(200) DEFAULT NULL COMMENT '推特账号',
  `bio` varchar(1000) DEFAULT NULL COMMENT '个性签名',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `ux_username` (`username`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;

INSERT INTO `profiles` (`uid`, `username`, `followers`, `location`, `website`, `github`, `weibo`, `zhihu`, `twitter`, `bio`)
VALUES
	(9,'only-case',0,NULL,NULL,'only-case',NULL,NULL,NULL,NULL),
	(3849072,'biezhi',0,'Shanghai, China','https://biezhi.me','biezhi',NULL,'biezhi','biezhii','I am a Java developer, I like a variety of programming languages, I am willing to share and make friends.'),
	(5177746,'xingfly',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(5409429,'chencn',0,'nanjing',NULL,'chencn',NULL,NULL,NULL,NULL),
	(6694695,'yizhilee',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),
	(8357636,'NextToBe',0,NULL,NULL,'NextToBe',NULL,NULL,NULL,NULL),
	(8613196,'javaor',0,NULL,NULL,'javaor',NULL,NULL,NULL,'Love life, love coding...'),
	(9380114,'SomeDargon',0,NULL,NULL,'SomeDargon',NULL,NULL,NULL,NULL),
	(10753805,'zzzzbw',0,NULL,NULL,NULL,NULL,NULL,NULL,'Playing and Coding'),
	(17674011,'jiqing112',0,NULL,NULL,'jiqing112',NULL,NULL,NULL,NULL),
	(18333714,'qiaoqiao888',0,NULL,NULL,'qiaoqiao888',NULL,NULL,NULL,NULL),
	(18475087,'JZFamily',0,NULL,NULL,'JZFamily',NULL,NULL,NULL,'bring disgrace on CPP'),
	(20114873,'sunzhenyucn',0,'China, Shanghai',NULL,'sunzhenyucn',NULL,NULL,NULL,'行动 > idea'),
	(22606989,'ABUGADAY',0,NULL,NULL,'ABUGADAY',NULL,NULL,NULL,'just coding'),
	(22719855,'RoyXiBoy',0,NULL,NULL,'RoyXiBoy',NULL,NULL,NULL,'我们注定是要改变这个时代的人。'),
	(25324065,'zhongmc1995',0,NULL,NULL,NULL,NULL,NULL,NULL,'天空才是我的极限'),
	(25500786,'zhaoweih',0,'Chaozhou,China',NULL,'zhaoweih',NULL,NULL,NULL,NULL),
	(29882936,'yuanjiu041',NULL,NULL,NULL,'yuanjiu041',NULL,NULL,NULL,NULL),
	(32955063,'otuta',0,'意大利，信不信',NULL,'otuta',NULL,NULL,NULL,'{\\__/}\r\n( • - •)\r\n/つ:sushi:寿司要不要?\r\n\r\n{\\__/}\r\n( • - •)\r\n/つ:strawberry:草莓要不要?\r\n\r\n{\\__/}\r\n( • - •)\r\n/つ:rose:玫瑰要不要?\r\n\r\n{\\__/}\r\n( • - •)\r\n/--------:watermelon:西瓜要不要?\r\n\r\n{\\__/}\r\n( • - •)\r\n/つ我  你要'),
	(37607817,'EdwardChan0626',0,NULL,NULL,'EdwardChan0626',NULL,NULL,NULL,NULL),
	(37778292,'ColdNoodleOrz',0,NULL,NULL,NULL,NULL,NULL,NULL,'皮');

/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table promotions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `promotions`;

CREATE TABLE `promotions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sort` tinyint(4) NOT NULL,
  `title` varchar(100) NOT NULL DEFAULT '',
  `icon` varchar(100) DEFAULT NULL,
  `position` varchar(50) NOT NULL DEFAULT '' COMMENT '广告位置',
  `content` varchar(1000) NOT NULL DEFAULT '',
  `footer` varchar(1000) DEFAULT NULL,
  `created` datetime NOT NULL,
  `expired` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;

INSERT INTO `promotions` (`id`, `sort`, `title`, `icon`, `position`, `content`, `footer`, `created`, `expired`)
VALUES
	(1,0,'本站由 Blade 驱动','czs-rocket','RIGHT_BOTTOM','简洁优雅的 MVC 框架 Blade 你值得拥有！','<a target=\"_blank\" href=\"https://github.com/lets-blade/blade\" class=\"btn btn-outline-primary\"><i class=\"czs-github-logo\"></i> Github 地址</a>','2018-04-07 15:32:22','2018-09-06 15:32:22'),
	(2,0,'Geek Dev 极客开发者','czs-hacker','RIGHT_TOP','Geek Dev 是为一群极客开发者打造的轻量级社区产品，在这里你可以抒发你在编程中的任何观点， 保持条理清晰、富有逻辑会受人们的欢迎！<br/>\n做有趣的灵魂，写高效的代码，在这里你会看到他们 :beers:',NULL,'2018-04-07 14:12:08','2028-04-07 14:12:08');

/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table relations
# ------------------------------------------------------------

DROP TABLE IF EXISTS `relations`;

CREATE TABLE `relations` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `event_id` varchar(32) NOT NULL DEFAULT '' COMMENT '帖子、用户id',
  `relate_type` varchar(10) NOT NULL DEFAULT '' COMMENT '关系类型 LOVE:点赞 COLLECT:收藏 FOLLOW:关注',
  `created` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uid_event` (`uid`,`event_id`,`relate_type`),
  KEY `idx_uid_q` (`uid`,`event_id`,`relate_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关系备份';

LOCK TABLES `relations` WRITE;
/*!40000 ALTER TABLE `relations` DISABLE KEYS */;

INSERT INTO `relations` (`id`, `uid`, `event_id`, `relate_type`, `created`)
VALUES
	(22,3849072,'0vxys3c0fdr8ovmm4d','LOVE','2018-04-14 13:24:36'),
	(23,3849072,'4mo2ikc6fxq3ov77rk','LOVE','2018-04-14 13:24:36'),
	(24,3849072,'658ec7c4fqp6633wz8','LOVE','2018-04-14 13:24:36'),
	(25,3849072,'de57tdcvfew2gmpp38','LOVE','2018-04-14 13:24:36'),
	(27,3849072,'p6pqi4cmfvwpp2ne6m','LOVE','2018-04-14 13:24:36'),
	(28,3849072,'p6pqi4cmfvwpp2ne6m','COLLECT','2018-04-14 13:24:36'),
	(29,3849072,'de57tdcvfew2gmpp38','COLLECT','2018-04-14 13:24:36'),
	(30,3849072,'0vxys3c0fdr8ovmm4d','COLLECT','2018-04-14 13:24:36'),
	(34,3849072,'5loni7cwf62okqyyd0','COLLECT','2018-04-14 21:19:02'),
	(35,3849072,'5loni7cwf62okqyyd0','LOVE','2018-04-14 21:19:02'),
	(36,3849072,'me64tocwfgo72krrgm','COLLECT','2018-04-14 21:21:38'),
	(39,3849072,'me64tocwfgo72krrgm','LOVE','2018-04-14 21:22:19');

/*!40000 ALTER TABLE `relations` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table settings
# ------------------------------------------------------------

DROP TABLE IF EXISTS `settings`;

CREATE TABLE `settings` (
  `skey` varchar(50) NOT NULL DEFAULT '' COMMENT '配置键',
  `svalue` varchar(5000) DEFAULT '' COMMENT '配置值',
  `state` tinyint(2) NOT NULL COMMENT '0禁用 1正常',
  PRIMARY KEY (`skey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;

INSERT INTO `settings` (`skey`, `svalue`, `state`)
VALUES
	('count.comments','16',1),
	('count.topics','6',1),
	('count.users','21',1),
	('plugin.instantclick','false',1),
	('site.allowComment','1',1),
	('site.allowRegister','1',1),
	('site.allowTopic','1',1),
	('site.description','',1),
	('site.keywords','极客,开发者,程序员,编程社区,Github',1),
	('site.subtitle','极客开发者社区',1),
	('site.title','Geek Dev',1),
	('site.url','http://127.0.0.1:9000',1),
	('site.version','0.23',1);

/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table topic_tags
# ------------------------------------------------------------

DROP TABLE IF EXISTS `topic_tags`;

CREATE TABLE `topic_tags` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) NOT NULL DEFAULT '',
  `tid` varchar(64) NOT NULL DEFAULT '',
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tagname` (`tag_name`),
  KEY `idx_tid` (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table topics
# ------------------------------------------------------------

DROP TABLE IF EXISTS `topics`;

CREATE TABLE `topics` (
  `tid` varchar(32) NOT NULL DEFAULT '' COMMENT '帖子id',
  `node_id` varchar(50) NOT NULL DEFAULT '' COMMENT '所属节点',
  `node_title` varchar(50) NOT NULL DEFAULT '' COMMENT '节点名称',
  `title` varchar(200) NOT NULL DEFAULT '' COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `topic_type` varchar(10) NOT NULL DEFAULT '' COMMENT '帖子类型:TOPIC, BLOG, VOTE',
  `comments` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `loves` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `collects` int(11) NOT NULL COMMENT '收藏数',
  `gains` int(11) NOT NULL DEFAULT '0' COMMENT '降权值',
  `popular` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是精华贴',
  `weight` double NOT NULL DEFAULT '0' COMMENT '帖子权重',
  `state` tinyint(4) NOT NULL COMMENT '1: 正常 2: 锁定 0: 删除',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`tid`),
  KEY `idx_username` (`username`),
  KEY `idx_weight` (`weight`),
  KEY `idx_created` (`created`),
  KEY `idx_nodeid` (`node_id`),
  KEY `idx_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帖子表';

LOCK TABLES `topics` WRITE;
/*!40000 ALTER TABLE `topics` DISABLE KEYS */;

INSERT INTO `topics` (`tid`, `node_id`, `node_title`, `title`, `content`, `username`, `topic_type`, `comments`, `loves`, `collects`, `gains`, `popular`, `weight`, `state`, `created`, `updated`)
VALUES
	('0vxys3c0fdr8ovmm4d','golang','Golang','通过抄写学 golang','golang 专注于软件工程的实用性，学习起来不同于其他一些很 magic 的编程语言，我们要遵循标准和一些范式。\n\n最好的资料就是 golang 的标准库源代码，只是有些库本身要处理的事情比较复杂，或者需要一定的知识储备，导致我们很难在短时间内理清脉络深入学习。那么我们需要一些小巧精悍适合初学者的开源库，我们可以按照这些标准找：\n\n符合 golang 标准，使用地道的编程范式。\n完善文档和测试。\n核心功能尽可能集中，代码规模尽可能的小。\n下面是我自己在寻觅的过程中感觉还不错的几个开源库：\n\n**语法**：[gorilla/context](https://github.com/gorilla/context)\n\n这个核心代码只有 100 行左右的小库，可以让我们学习到 golang 中 map 和 sync.RWMutex 在实践中的使用，更为重要的是，从中我们可以学习软件工程的一些优秀实践：注释，测试，命名等。\n\n**HTTP**：[urfave/negroni](https://github.com/urfave/negroni)\n\n这个库核心代码也是 100 行左右，其功能就是为 http.Handler 添加 middleware 功能。这个库值得学习的地方在于理解 net/http 的设计理念，看看实践中如何用 interface 降低模块之间的耦合度以及如何设计模块和模块之间的关系。\n\n**gorutine**：[go-playground/pool](https://github.com/go-playground/pool)\n\n核心代码 150 行，主要功能是实现了一个 gorutine 的 pool，抄写这个库的代码可以了解 golang 中 gorutine 的基本概念，channel 和 select 的使用方式以及如何用 sync.WaitGroup 管理 gorutine 。\n\n实践抄写的过程中，可以带着这些问题：\n\n1. 这个开源库的边界、对外接口或者叫核心功能是什么？\n2. 这个开源库在实现的过程中考虑了那些边界条件，如何把握效率与可用性之间的平衡点？\n3. 这个开源库用到了哪些内置库，如何使用的？\n4. 如果只看 README 和 go doc 输出的对外接口，能否在不了解内部实现的情况下自己写出来？\n5. 如果源码中有些东西我不熟悉，看过源码，理清思路后能否默写出来？\n6. 完成实现后，如何写出简单易读并且覆盖完善的测试？\n\n> 作者：王谙然\n> 链接：https://www.jianshu.com/p/662c8f8e5740\n> 來源：简书\n> 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。','otuta','TOPIC',0,1,1,0,0,474.78,1,'2018-04-07 23:11:05','2018-04-07 23:11:05'),
	('4mo2ikc6fxq3ov77rk','affair','站务','本帖接受站务意见反馈','如果你对 `Geek Dev` 有什么建议或者想法，可以在这篇帖子下留言 <kbd>:)</kbd>','biezhi','TOPIC',0,1,0,0,0,474.62,1,'2018-04-07 21:10:56','2018-04-07 21:10:56'),
	('5loni7cwf62okqyyd0','echo','Echo','大家好我是来装逼的','装逼之道，充乎天地。考自太古以来，推至万世而后，未有不装逼者也。装逼者，失其本 也，故常与装纯并论也。故欲至其性者，装逼之至者也。','jiqing112','TOPIC',10,1,1,0,0,497.19,1,'2018-04-08 10:44:56','2018-04-08 10:44:56'),
	('de57tdcvfew2gmpp38','vscode','vscode','快速使用 vscode 进行 Java 编程','任何一个程序员都有自己喜爱的编辑器、工具、开发利器，有这样一群人，对于 vim 这种上古神器难以驾驭、IDE 又太笨重，这时候多了一个选择 vscode！！！\n\nvscode 重新定义了编辑器，它开源、免费、Runs everywhere，是一款介于 IDE 和编辑器之间的产物，我们不能用 IDE 的所有特性都往它身上压，如果都可以的话不就是 IDE 吗？不就是吗？所以用起来的感觉你懂的，美滋滋(๑•̀ㅂ•́) ✧\n\n那么这家伙都有什么牛逼之处呢？\n\n## 特性\n\n- 微软开发，软件质量没的说\n- 高颜值，界面非常漂亮，允许自定义更改\n- 插件超级多，开发者很愿意接受它\n- 对我天朝程序员友好，你懂的\n- 启动速度比 atom 快，资源占用少\n- 跨平台的特性能没有吗？能吗？\n- 对各种编程语言支持良好（当然我只试过java/node/go/python）\n- 自动补全、代码高亮、代码跳转...\n- 集成终端好用，可以用 code 命令打开文件/文件夹\n- debug 功能好用(简直是 mini 的 IDE)\n- ( ⊙ o ⊙ )啊！特性太多了我实在写不完\n\n说了这么多你一定迫不及待想尝试吧~ （也许并不是）那现在\n\n## 安装 vscode\n\n我知道在座的各位其实根本不用我教你怎么安装，因为它是傻瓜式安装啊！它家的官网是 [https://code.visualstudio.com/](https://code.visualstudio.com/)，你只需要在首页下载符合你操作系统的软件即可。\n\n支持 **Windows**、**Linux**、**Mac**。\n\n## 了解 vsccde\n\n为什么要了解它呢？你不了解它的组成就不知道怎么把它用好（高能型大佬除外）。\n\n### 布局\n\n![alt](https://user-gold-cdn.xitu.io/2018/4/2/162842a23ef4cbe8?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)\n\n上面是 vscode 的布局，和大多数编辑器一样，分为\n\n- **Editor** 用来编辑文件的主体区域。可以并排打开三个编辑器。\n- **Side Bar** 包含不同的像浏览器一样的视图来协助来完成工程。\n- **Status Bar** 展示当前打开的工程和正在编辑的文件的信息。\n- **View Bar** 在最左手边，帮助切换视图以及提供额外的上下文相关的提示，比如激活了Git的情况下，需要提交的变化的数目。\n\n在我看来学习一款编辑器并不用像学习编程语言那样，按照某个教程把所有的功能都学会，我更喜欢探索性的尝试自己需要的那些东西，比如在 View Bar 中如何的5个功能，其实点一点就大概明白什么意思了，查阅文字资料只是让自己更精确的掌握使用姿势。\n\n### 配置\n\nvscode 的用户配置分3个级别，分别是默认配置、全局配置和工作区配置，优先级也依次递增。对于团队项目，一些规范可以通过项目目录下建一个 .vscode/setting.json 文件进行配置,比如：\n\n```json\n// tab长度 \n\"editor.tabSize\": 2, \n// 启用后，将在保存文件时剪裁尾随空格。 \n\"files.trimTrailingWhitespace\": true, \n// 配置 glob 模式以排除文件和文件夹。 \n\"files.exclude\": { \n  \"**/.git\": true, \n  \"**/.svn\": true, \n  \"**/.DS_Store\": true, \n  \"**/.idea\": true \n},\n```\n\n这个配置我一般是不用的，只用用户配置，反正电脑就一个用户。配置一下字体大小、自动保存等。\n\n## 必装插件\n\n- [Beautify](https://marketplace.visualstudio.com/items?itemName=HookyQR.beautify)：代码高亮\n- [Terminal](https://marketplace.visualstudio.com/items?itemName=formulahendry.terminal)：直接唤起终端\n- [Project Manager](https://marketplace.visualstudio.com/items?itemName=alefragnani.project-manager)：多个项目切换\n- [Auto Close Tag](https://marketplace.visualstudio.com/items?itemName=formulahendry.auto-close-tag)：标签自动闭合（其实我觉得可以内置的）\n\n怎么安装呢？\n\n![alt](https://user-gold-cdn.xitu.io/2018/4/2/162842a90faedba3?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)\n\n在扩展输入框里输入你想用的插件就可以了，当然它还会经常推荐给我们排行比较高的插件，可以尝试安装，安装完成后重新加载编辑器就可以使用了。\n\n## 常用快捷键\n\n下面这些快捷键是我常用的，如果你想看更全的可以看看 VS Code有哪些常用的快捷键\n\n- 向上向下复制一行：`Shift+Alt+Up` 或 `Shift+Alt+Down`\n- 注释代码: `cmd + /`\n- 切换侧边栏: `cmd + b`\n- 文件夹中查找: `cmd + shift + f`\n- 查找替换: `cmd + shift + h`\n- 重构代码: `fn + F2`\n- 代码格式化: `Shift+Alt+F`，或 `Ctrl+Shift+P` 后输入 `format code`\n\n**Ctrl+P 模式: (Mac 是 CMD+P)**\n\n- 直接输入文件名，快速打开文件\n- > 显示并运行命令\n- : 跳转到行数，也可以 `Ctrl+G` 直接进入(Mac 是 CMD+G)\n- @ 跳转到symbol（搜索变量或者函数），也可以 `Ctrl+Shift+O` 直接进入\n- @: 根据分类跳转symbol，查找属性或函数，也可以 `Ctrl+Shift+O` 后输入`:`进入\n- 根据名字查找symbol，也可以 `Ctrl+T`\n\n## 配置 Java 环境\n\n先安装 Java 语言相关的插件 4 枚\n\n- [Language Support for Java(TM) by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java)\n- [Debugger for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-debug)\n- [Java Test Runner](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test)\n- [Maven for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-maven)\n\n有人想问了，妈耶还要4个插件，这么麻烦的吗？\n\n第一个插件干嘛的？运行 Java 代码的，第二个呢？调试的，不调试可以不装，第三个运行单元测试，不测试可以不装，第四个建议装上，一个标准化的 Java 工程一定不是几个文件组成，maven 是在Java环境下构建应用程序的软件（本地要先安装哦）。\n\n这时候还需要配置一下 `java.home`，我的是\n\n```bash\n\"java.home\": \"/Library/Java/JavaVirtualMachines/jdk1.8.0_101.jdk/Contents/Home\",\n```\n\n大功告成，现在你可以在 vs code 下创建一个 Mmp.java 开始写 Hello vscode! 了。\n\n```java\npublic class Mmp {\n\n  public static void main(){\n    System.out.println(\"Hello vscode!\");\n  }\n\n}\n```\n\n点击 **调试** 或者按下 F5 运行这久经码场的输出语句。具体操作见文章底部视频。\n\n**代码定位**\n\n![alt](https://user-gold-cdn.xitu.io/2018/4/2/162842956408fe63?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)\n\n按住 `ctrl` 键鼠标悬停在类上面会有类描述，点击即可进入类定义处，方法也是同样。\n\n**代码重构**\n\n![alt](https://user-gold-cdn.xitu.io/2018/4/2/1628429584c6c95c?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)\n\n很强大的一个地方就是我们有时候会修改字段、方法的名称。\n\n1. 找到所有的引用： Shift + F12\n2. 同时修改本文件中所有匹配的： Ctrl+F12\n3. 重命名：比如要修改一个方法名，可以选中后按 F2，输入新的名字，回车，会发现所有的文件都修改了\n4. 跳转到下一个 Error 或 Warning：当有多个错误时可以按 F8 逐个跳转\n5. 查看 diff： 在 explorer 里选择文件右键 Set file to compare，然后需要对比的文件上右键选择 Compare with file_name_you_chose\n\n\n运行 SpringBoot 项目\n下载一个 SpringBoot 的示例工程\n\n```bash\ngit clone https://github.com/JavaExamples/spring-boot-helloworld.git\ncode spring-boot-helloworld\n```\n\n> 什么？git 还不会\n> ![alt](https://user-gold-cdn.xitu.io/2018/4/2/1628429584a9e8ed?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)\n\n![alt](https://user-gold-cdn.xitu.io/2018/4/2/1628429584b70f56?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)\n\n启动调试，选择 Java 语言，会提示我们 `launch.json` 文件是下面这样的\n\n```json\n{\n    // 使用 IntelliSense 了解相关属性。 \n    // 悬停以查看现有属性的描述。\n    // 欲了解更多信息，请访问: https://go.microsoft.com/fwlink/?linkid=830387\n    \"version\": \"0.2.0\",\n    \"configurations\": [\n        {\n            \"type\": \"java\",\n            \"name\": \"Debug (Launch)-Application<spring-boot-helloworld>\",\n            \"request\": \"launch\",\n            \"cwd\": \"${workspaceFolder}\",\n            \"console\": \"internalConsole\",\n            \"stopOnEntry\": false,\n            \"mainClass\": \"hello.Application\",\n            \"projectName\": \"spring-boot-helloworld\",\n            \"args\": \"\"\n        },\n        {\n            \"type\": \"java\",\n            \"name\": \"Debug (Attach)\",\n            \"request\": \"attach\",\n            \"hostName\": \"localhost\",\n            \"port\": 0\n        }\n    ]\n}\n```\n\n主要看 `mainClass`，只有第一次生成这个文件，确认后点击 调试 即可看到控制台日志\n\n![alt](https://user-gold-cdn.xitu.io/2018/4/2/1628429584d7e00d?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)\n\n## 尝试 Lombok\n\n这个家伙你用不用都无妨，反正我是挺喜欢的。这里就不啰嗦到底干嘛的，简单来说就是我可以用一个 `@Data` 注解帮我自动生成（编译后生成）getter、setter、toString、equals、hashCode 这些方法，反正多花时间写这些代码也不会提高你的能力，修改的时候还要多花时间，完全没！必！要！好吗？\n\n按照[官方的文档](https://link.juejin.im/?target=https%3A%2F%2Flink.zhihu.com%2F%3Ftarget%3Dhttps%253A%2F%2Fgithub.com%2Fredhat-developer%2Fvscode-java%2Fwiki%2FLombok-support)我尝试了不在 maven 环境下是行不通的！行不通的。如果你可以的话在文章评论下告诉我哈~\n\n所以我们安装一个 [Lombok Annotations Support for VS Code](https://marketplace.visualstudio.com/items?itemName=GabrielBB.vscode-lombok) 就可以了。\n\n在 maven 工程中添加 lombok 依赖\n\n```xml\n<dependency>\n    <groupId>org.projectlombok</groupId>\n    <artifactId>lombok</artifactId>\n    <version>1.16.20</version>\n    <scope>provided</scope>\n</dependency>\n```\n\n试一下吧\n\n![alt](https://user-gold-cdn.xitu.io/2018/4/2/16284295862963a0?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)\n\n大兄弟如果你能看到这里可以跟我领取真经了，下方是本篇文章的视频操作指南，敬请食用：\n\n- [哔哩哔哩](https://www.bilibili.com/video/av21516450/)\n- [Youtube](https://www.youtube.com/watch?v=E-kKggVbiig)\n\n**想看更多 vscode 技巧？点:chicken:下面的开发技巧集锦**\n\n[vscode开发技巧集锦](https://zhuanlan.zhihu.com/p/34989844)\n\n我还有一个 QQ 群聊 Java8、聊代码、聊编程最前言，过于水群直接踢，车牌号：`NjYzODg3NzI5`\n\n','biezhi','TOPIC',0,1,1,0,0,474.75,1,'2018-04-07 22:47:41','2018-04-07 22:47:41'),
	('kr6xb7cdf3low5rr3p','share','分享创造','大家好，我是来游玩的','我擦勒啊，github上关注了大佬，心想这个项目这么久了，怎么一直不填代码呢。。。。=，=','JZFamily','TOPIC',2,0,0,0,0,488.79,1,'2018-04-08 14:26:20','2018-04-08 14:26:20'),
	('me64tocwfgo72krrgm','echo','Echo','Hello Geek Dev','我是 `biezhi`，Geek Dev 的第一位用户。<br/>\n\n```java\npublic static void main(){\n    System.out.println(\"Geek Dev Club!\");\n}\n```\n\n**欢迎更多有意思的小伙伴在这里分享经验，碰撞思维 :P**','biezhi','TOPIC',4,1,1,0,0,477.13,1,'2018-04-07 20:05:58','2018-04-07 20:05:58');

/*!40000 ALTER TABLE `topics` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(100) DEFAULT NULL,
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `email` varchar(100) DEFAULT NULL COMMENT '用户邮箱',
  `avatar` varchar(100) DEFAULT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'member' COMMENT '角色',
  `created` datetime NOT NULL COMMENT '注册时间',
  `logined` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `updated` datetime DEFAULT NULL COMMENT '最后一次操作时间',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '用户状态 0:未激活 1:正常 2:停用 3:注销',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uidx_username_state` (`username`,`state`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;

INSERT INTO `users` (`uid`, `name`, `username`, `email`, `avatar`, `role`, `created`, `logined`, `updated`, `state`)
VALUES
	(9,'only-case','only-case','','https://avatars2.githubusercontent.com/u/29011307?v=4','MEMBER','2017-10-12 17:13:10','2018-04-05 01:46:14','2017-10-13 16:24:33',1),
	(3849072,'王爵nice','biezhi','biezhi.me@gmail.com','https://avatars1.githubusercontent.com/u/3849072?v=4','MASTER','2018-04-05 01:46:14','2018-04-18 22:21:54',NULL,1),
	(5177746,'XingFly','xingfly','549052145@qq.com','https://avatars3.githubusercontent.com/u/5177746?v=4','MEMBER','2018-04-11 18:21:29','2018-04-11 18:21:29',NULL,1),
	(5409429,'chencn','chencn',NULL,'https://avatars1.githubusercontent.com/u/5409429?v=4','MEMBER','2018-04-08 16:40:57','2018-04-09 08:22:00',NULL,1),
	(6694695,'荔枝','yizhilee',NULL,'https://avatars0.githubusercontent.com/u/6694695?v=4','MEMBER','2018-04-09 22:34:46','2018-04-09 22:34:46',NULL,1),
	(8357636,'Zero','NextToBe','571078942@qq.com','https://avatars3.githubusercontent.com/u/8357636?v=4','MEMBER','2018-04-08 11:40:04','2018-04-08 11:40:04',NULL,1),
	(8613196,'赵宏轩','javaor','hxzhenu@gmail.com','https://avatars1.githubusercontent.com/u/8613196?v=4','MEMBER','2018-04-08 13:04:41','2018-04-08 13:04:41',NULL,1),
	(9380114,'SomeDargon','SomeDargon',NULL,'https://avatars0.githubusercontent.com/u/9380114?v=4','MEMBER','2018-04-08 16:05:52','2018-04-08 16:05:52',NULL,1),
	(10753805,'Zzzzbw','zzzzbw','zzzzbw@gmail.com','https://avatars1.githubusercontent.com/u/10753805?v=4','MEMBER','2018-04-09 11:46:16','2018-04-09 11:46:16',NULL,1),
	(17674011,'言之灵','jiqing112',NULL,'https://avatars1.githubusercontent.com/u/17674011?v=4','MEMBER','2018-04-08 10:43:41','2018-04-08 10:43:41',NULL,1),
	(18333714,'我是弟弟','qiaoqiao888',NULL,'https://avatars0.githubusercontent.com/u/18333714?v=4','MEMBER','2018-04-08 16:40:54','2018-04-08 16:40:54',NULL,1),
	(18475087,'JZFamily','JZFamily',NULL,'https://avatars0.githubusercontent.com/u/18475087?v=4','MEMBER','2018-04-08 14:23:10','2018-04-08 14:23:10',NULL,1),
	(20114873,'孙振宇','sunzhenyucn','sunzhenyucn@gmail.com','https://avatars3.githubusercontent.com/u/20114873?v=4','MEMBER','2018-04-08 13:17:04','2018-04-08 13:17:04',NULL,1),
	(22606989,'Fengld','ABUGADAY','dasvenxx@gmail.com','https://avatars1.githubusercontent.com/u/22606989?v=4','MEMBER','2018-04-08 16:05:53','2018-04-08 16:05:53',NULL,1),
	(22719855,'Ronxi','RoyXiBoy','aa543187001@163.com','https://avatars3.githubusercontent.com/u/22719855?v=4','MEMBER','2018-04-07 23:24:31','2018-04-07 23:24:31',NULL,1),
	(25324065,'钟明城','zhongmc1995','zhongmc_me@163.com','https://avatars3.githubusercontent.com/u/25324065?v=4','MEMBER','2018-04-12 10:35:17','2018-04-12 10:35:17',NULL,1),
	(25500786,'Zhao Weihao','zhaoweih','zhaoweihao.dev@gmail.com','https://avatars2.githubusercontent.com/u/25500786?v=4','MEMBER','2018-04-08 13:32:23','2018-04-08 13:32:23',NULL,1),
	(29882936,'yuanjiu041','yuanjiu041',NULL,'https://avatars2.githubusercontent.com/u/29882936?v=4','MEMBER','2018-04-12 15:26:42','2018-04-12 15:26:42',NULL,1),
	(32955063,'可爱的羊驼','otuta','biezhi@tuta.io','https://avatars1.githubusercontent.com/u/32955063?v=4','ADMIN','2018-04-05 20:23:41','2018-04-08 23:53:00',NULL,1),
	(37607817,'EdwardChan0626','EdwardChan0626',NULL,'https://avatars3.githubusercontent.com/u/37607817?v=4','MEMBER','2018-04-08 19:40:58','2018-04-08 19:40:58',NULL,1),
	(37778292,'凉皮','ColdNoodleOrz',NULL,'https://avatars3.githubusercontent.com/u/37778292?v=4','MEMBER','2018-04-11 16:38:27','2018-04-11 16:38:27',NULL,1);

/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;


/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
