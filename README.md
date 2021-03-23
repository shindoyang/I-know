### 目标

问你敢不敢！趁年轻，做自己想做的事

* 短期目标

  1. 面向RMB(面试)学习

  2. 学习路线：

     juc--> jvm--> 设计模式--> zookeeper--> redis--> mysql调优-->netty--> spring源码

* 长期目标 

  1. 面向底层学习
  
  2. 学习路线：补全知识栈
  
     javaSE(java8)--> juc--> jvm--> spring--> springmvc--> springboot--> springcloud -->nio--> netty          +   大数据


3,学习go




---

**欲速则不达**

练级指引：

先玩命的补全知识体系，形成完整的大局观！具备前瞻性技术视野！再逐一击破！



第一遍不要求面面俱到，但是要有大体的印象，知道是什么，可以怎么玩。

第二遍要做笔记，深刻记忆，查漏补缺，梳理细节。

第三遍在脱离笔记的前提下，想想用自己的语言怎么把知识点有体系的表达出来。



先戳穿那层窗户纸



想那么远干嘛，先把眼前能做的认真做好！



致美斋  >  海天 >  



生活的本质就是：家，在人生的规划上，懂得权衡轻重，做个成熟的人。

还是那句话：有本事，哪里都是舞台！



你无法改变你生命的长度，但是你可以改变你生命的高度

如果只在乎结果，那么就会想着怎么走捷径，而忽略了沿途的美景。

在架构的思路中，你要有趁手的工具，这样才能编制你的“大厦”；

就好比做木工的时候，你需要先了解你手里的工具。



分布式锁解决共享资源在高并发访问下出现的并发问题


成长为能与偶像肩并肩的硬核实力。

亚马逊贝佐斯：如何用遗憾最小化框架做艰难的决定？

如何正确、高效做出决策，并确保方案落地执行，越来越成为每个企业家在管理中最重要的一环。
亚马逊CEO贝佐斯的决策方法是——遗憾最小化框架。简而言之，即我们做任何决定，最终都有可能会后悔会遗憾，但是面对多个选择时，我们应该选让自己后悔或者遗憾最少的那个。

我们做任何决定，最终都有可能会后悔会遗憾，但是面对多个选择时，我们应该选让自己后悔或者遗憾最少的那个。

贝佐斯说：“我把自己想象成80岁的模样，并思考：现在回望我的一生，我要把遗憾事件的数量降到最低。”

善良比聪明更难

选择比天赋更重要。天赋和选择不同：

聪明是一种天赋，而善良是一种选择；天赋得来很容易——毕竟它们与生俱来，而选择则颇为不易。
如果一不小心，你可能被天赋所诱惑，这可能会损害到你做出的选择。



达摩院的定位: Research for solving the problem with profit and fun   为解决问题而做研究，创造价值并享受快乐

对自己的定位是做一个善良的人，做有价值的事。


千锤百炼、韬光养晦

手中无剑，如何仗剑走天涯

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











栈： 
 1 基础数据类型 byte short int long float double char boolean
 2 方法的形式参数，方法调用完后从栈空间回收
 3 引用对象的地址，引用完后，栈空间地址立即被回收，堆空间等待GC
 a) 栈内的数据线程之间独立
 b) 具体细分为:
 b.1) 基本类型变量区
 b.2) 执行环境上下文
 b.3) 操作指令区

 

堆
 1 this
 2 new出来的对象
 3 数组
 a) jvm只有一个堆区，并被所有线程共享。

 

方法区域(又叫 静态区) 
 1 字符串常量
 2 static
 3 所有的class
 a) 被所有线程共享, 其内存放程序中永远唯一的元素，eg: static class





