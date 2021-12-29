进IT大厂，说容易也容易，说难也挺难得，了解很多技术，而一般的IT公司，因为规模和成本问题，大都不会使用。

本文包括了很多知识点，如基础知识、Java集合、JVM、多线程并发、spring原理、微服务、Netty 与RPC 、Kafka、日记、设计模式、Java算法、数据库、Zookeeper、分布式缓存、数据结构等等。

这些都是大厂都必须使用的技术。

JAVA基础

JAVA异常分类及处理
 JAVA反射
 JAVA注解
 JAVA内部类
 JAVA泛型
 JAVA序列化(创建可复用的Java对象)
 JAVA复制

数据结构

栈（stack）
 队列（queue）
 链表（Link）
 散列表（Hash Table）
 排序二叉树
 红黑树
 B-TREE
 位图
 加密算法
 AES
 RSA
 CRC
 MD5

JAVA算法

二分查找
 冒泡排序算法
 插入排序算法
 快速排序算法
 希尔排序算法
 归并排序算法
 桶排序算法
 基数排序算法
 剪枝算法
 回溯算法
 最短路径算法
 最大子数组算法
 最长公共子序算法
 最小生成树算法

JAVA集合

接口继承关系和实现
 List
 ArrayList（数组）
 Vector（数组实现、线程同步）
 LinkList（链表）
 Set
 HashSet（Hash表）
 TreeSet（二叉树）

JVM

线程
 JVM内存区域
 JVM运行时内存
 垃圾回收与算法
 JAVA 四中引用类型
 GC分代收集算法 VS 分区收集算法
 GC垃圾收集器
 JAVA IO/NIO
 JVM 类加载机制

设计模式

设计原则
 工厂方法模式
 抽象工厂模式
 单例模式
 建造者模式
 原型模式
 适配器模式
 装饰器模式
 代理模式
 外观模式
 桥接模式
 组合模式
 享元模式
 策略模式
 模板方法模式
 观察者模式
 迭代子模式
 责任链模式
 命令模式
 备忘录模式

JAVA多线程并发

JAVA并发知识库
 JAVA线程实现/创建方式
 4种线程池
 线程生命周期(状态)
 终止线程4种方式
 sleep与wait 区别
 start与run区别
 JAVA后台线程
 JAVA锁
 线程基本方法4.1.11. 线程上下文切换
 同步锁与死锁
 线程池原理
 JAVA阻塞队列原理
 CyclicBarrier、CountDownLatch、Semaphore的用法
 volatile关键字的作用（变量可见性、禁止重排序）
 如何在两个线程之间共享数

网络

网络7层架构
 TCP/IP原理
 TCP三次握手/四次挥手
 HTTP原理
 CDN 原理
 分发服务系统
 负载均衡系统
 管理系统

Spring 原理

Spring 特点
 Spring 核心组件
 Spring 常用模块
 Spring 主要包
 Spring 常用注解
 Spring第三方结合
 Spring IOC原理
 Spring APO原理
 Spring MVC原理
 Spring Boot原理
 JPA原理
 Mybatis缓存
 Tomcat架构

微服务

服务注册发现
 API 网关
 配置中心
 事件调度（kafka）
 服务跟踪（starter-sleuth）
 服务熔断（Hystrix）
 Hystrix断路器机制
 API管理

Netty 与RPC

Netty 原理
 Netty 高性能
 Netty RPC实现
 关键技术
 核心流程
 消息编解码
 通讯过程
 RMI实现方式

分布式缓存

缓存雪崩
 缓存穿透
 缓存预热
 缓存更新
 缓存降级

日志

Slf4j
 Log4j
 LogBack
 Logback优点
 ELK

Zookeeper

Zookeeper概念
 Zookeeper角色
 Zookeeper工作原理（原子广播）
 Znode有四种形式的目录节点

Kafka

Kafka概念
 Kafka数据存储设计
 partition的数据文件（offset，MessageSize，data）
 数据文件分段segment（顺序读写、分段命令、二分查找）
 数据文件索引（分段索引、稀疏存储）
 生产者设计
 负载均衡（partition会均衡分布到不同broker上）
 批量发送
 压缩（GZIP或Snappy）
 消费者设计

RabbitMQ

RabbitMQ概念
 RabbitMQ架构
 Exchange 类型

Hbase

Hbase概念
 列式存储
 Hbase核心概念
 Hbase核心架构
 Hbase的写逻辑
 HBase vs Cassandra

MongoDB

MongoDB概念
 MongoDB特点

Cassandra

Cassandra概念
 数据模型
 Cassandra一致Hash和虚拟节点
 Gossip协议
 数据复制
 数据写请求和协调者
 数据读请求和后台修复
 数据存储（CommitLog、MemTable、SSTable）
 二级索引（对要索引的value摘要，生成RowKey）
 数据读写

负载均衡

四层负载均衡 vs 七层负载均衡
 负载均衡算法/策略
 LVS
 Keepalive
 Nginx反向代理负载均衡
 HAProxy

数据库

存储引擎
 索引
 数据库三范式
 数据库是事务
 存储过程(特定功能的SQL 语句集)
 触发器(一段能自动执行的程序)
 数据库并发策略
 数据库锁
 基于Redis分布式锁
 分区分表
 两阶段提交协议
 三阶段提交协议
 柔性事务
 CAP

一致性算法

Paxos
 Zab
 Raft
 NWR
 Gossip
 一致性Hash
 一致性Hash特性
 一致性Hash原

Hadoop

Hadoop概念
 HDFS
 Client
 NameNode
 Secondary NameNode
 DataNode
 MapReduce
 JobTracker
 TaskTracker
 Task
 Reduce Task 执行过程
 Hadoop MapReduce 作业的生命周期
 作业提交与初始化
 任务调度与监控。
 任务运行环境准备
 任务执行
 作业完成

Spark

Spark概念
 核心架构
 核心组件
 SPARK编程模型
 SPARK计算模型
 SPARK运行流程
 SPARK RDD流程
 SPARK RDD

Storm

Storm概念
 集群架构
 Nimbus（master-代码分发给Supervisor）
 Supervisor（slave-管理Worker进程的启动和终止）
 Worker（具体处理组件逻辑的进程）
 Task
 ZooKeeper
 编程模型（spout->tuple->bolt）
 opology运行
 Storm Streaming Grouping
 ResourceManager
 NodeManager
 ApplicationMaster
 YARN运行流程

云计算

SaaS
 PaaS
 IaaS
 Docker
 Openstack
 Namespaces
 进程(CLONE_NEWPID 实现的进程隔离)
 Libnetwork与网络隔离
 资源隔离与CGroups
 镜像与UnionFS
 存储驱动