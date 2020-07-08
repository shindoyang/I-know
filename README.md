# I-know



**一口吃不成胖子，一步一脚印，扎实走过来**

**优先级：多线程高并发-->JVM-->设计模式-->zookeeper-->redis-->Mysql调优-->Netty-->Spring源码**



知识网络


* 是什么
* 产生的背景
* 解决什么痛点
* 优势
* 竞品是什么



## 多线程与高并发

###### static和volatile的区别

* static指的是类的静态成员，实例间共享
* volatile跟java内存模型相关，线程执行时会将变量从主内存加载到线程工作内存，建立一个副本，在某个时刻写回。volatile指的每次都读取主内存的值，有更新则立即写回主内存。
* static是类的属性，存储在类的那块内存，每个线程操作的时候会读取这个内存块，甚至会加载到寄存器或高速缓存中，这样自然不会保证其他线程对该值的可见性；而volatile表示每次读操作直接到内存，如果多个线程都遵循这样的约定，就会读取到最新的状态.

## JVM

###### 调优

###### 排查

## 设计模式

## Spring

###### spring的事务传播机制与数据库事务的关系



###### http异常传播的方式，不是rpc

###### 分布式事务

###### 灰度发布

###### 怎么界定哪些异常需要对外抛出



## MySql

###### 数据库调优

###### 索引设计，性能比较 （一个表设置多少个索引会比较好）

###### 一张表上建多少索引合适，性能上有没自己测试过



## redis

###### 用法、处理什么问题

## Netty

## zookeeper

## SpringCloud

## 算法与数据结构

## 计算机系统
### Linux
#### Vi

## 计算机组成

## 计算机网络

## MongoDB

## MQ

## ES

## docker k8s

## 大数据

## 高性能网关

### nginx  lua  

### 高可用 LVS

### 分布式服务
##### 限流
	一般限流做在网关这一层，如Nginx、OpenResty、kong、zuul、Spring Cloud Gateway等，也可以在应用层通过Aop这种方式去做限流。
###### 在Spring Cloud Gateway中如何实现

> https://blog.csdn.net/forezp/article/details/85081162

1. 计数器算法
    简单粗暴，一般我们会限制一秒钟能够通过的请求数，比如限流qps为100。--> 容易产生“突刺现象”
2. 漏桶算法
* 解决“突刺现象”
* 固定流速-->固定的处理速度
* 无法应对短时间的突然流量
3. 令牌桶算法
* 从某种意义上，是对漏桶算法的一种改进。桶算法能够限制请求调用的速率，而令牌桶算法能够在限制调用的平均速率的同时还允许一定程度的突发调用。








