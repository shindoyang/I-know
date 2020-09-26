AbstractApplicationContext.class

refresh()

prepareRefresh() --> 设置启动时间、关闭状态false、激活状态true、获取环境变量（propertySourceList  使用的是 CopyOnWriteArrayList数据类型   方法都加了ReentrantLock锁，since JDK1.5）、earlyApplicationEvents 使用的是 LinkedHashSet数据类型

任务：明确了解各个数据集合类型的作用



查询下JDK各个版本之间的变化

从现在来看，感觉JDK1.5的进步好大



1、首先创建Bean工厂

**一定要看注释**

看DefaultListableBeanFactory的类图，使用最多的一个类

注意：

* HierarchicalBeanFactory  继承，父子容器。AbstractBeanFactory.doGetBean() --> getParentBeanFactory();
* ListableBeanFactory：枚举bean实例
* ConfigurableBeanFactory

2、设置工厂的具体参数

**loadBeanDefinitions(factory)**  特别复杂，重载方法特别多，do方法才是真正干活的

configLocations  ，xml的时候set进去的值



3、加载xml

注意栈里面refreshBeanFactory的两个属性：beanDefinitionMap，beanDefinitionNames ，当xml配置文件的bean配置被解析后，后存放到这里

RootBeanDefinition ，GenericBeanDefinition  涉及BeanDefinition合并



prepareRefresh（）

注释：前戏，做容器刷新前的准备工作

* 设置容器的启动时间
* 设置活跃状态为true
* 设置关闭状态为false
* 获取Environment对象，并加载当前系统的属性值到Environment对象中。
* 准备监听器和事件的集合对象，默认为空的集合



configurableListableBeanFactory beanFactory= obtainFreshBeanFactory();

注释：

* 创建容器对象：DefaultLlistableBeanFactory
* 加载xml配置文件的属性值到当前工厂中，最重要的是BeanDefinition



4、初始化BeanFactory，或者给bean工厂设置具体的属性值

spring EL



好好看下，了解其作用：

ignoreDependencyTypes

ignoreDependencyInterface

忽略接口的实现



5、增强器

postProcessBeanFactory(beanFactory) 模板方法，当前类实现



6、执行BFPP，修改bean定义信息



---



实例化之前，也要做前戏，listener，观察者模式

7、registerBeanFactoryProcessor(beanFactory)

* 准备BPP

8、initMeassageSource()  -->国际化，i18n    nginx.org

9、initApplicationEventMulticaster()

10、onRefresh()  --> 空方法

但在springBoot中，tomcat就是实现这个方法启动的

11、registerListeners()；

注册监听器

* 观察者模式：监听器、监听事件、多播器（广播器）

---



12、finishBeanFactoryInitialization(beanFactory)  -- 实例化（对应图上的初始化那一大块），死扣

工作中常用，面试中常问

实例化所有的剩下的非懒加载的单例对象

* beanFactory.preInstantiateSingletons()



//提前检查单利缓存中是否有了手动注册的单例对象，跟循环依赖有关联  **DefaultSingletonBeanRegistry**

**Object shareInstance = getSingeton(beanName);**

单例的时候Spring会尝试使用三级缓存解决循环依赖的问题，但是如果在原型模式下，如果存在循环依赖的情况，会直接抛出异常。

doCreateBean()



反射的优缺点：

优点：灵活

缺点：性能低，但是有前提的，只是少数反射，不会有什么影响的，阀值在10万次调用左右，才会体现性能低



相对new对象而言，new的耦合度太高了



循环依赖 --提前暴露（只完成了实例化，但未完成初始化）

* 构造器的循环依赖，无法解决

* set的循环依赖，可以解决，因为实例化和初始化分开了



面试题：为什么要使用三级缓存？

关健在于 getEarlyBeanReference方法





初始化分两步：

* 填充属性
* 执行init方法







 























































