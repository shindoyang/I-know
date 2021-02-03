### 使用技能

1. 导包

   	* aspectj
   	* aop

2. 打开扫描机制及aop动态代理

   * <context:component-scan>
   * <aop:aspectj-autoproxy>

3. 加入@Aspect注解

4. 四个主要的概念：切面、连接点、切点、通知，（切点是连接点的子集）

5. 通知的几种类型：

   1. 前置通知@Before
   2. 后置通知@After
   3. 通知结果@After Returnning
   4. 通知异常@After Throwing
   5. 环绕通知@Around

6. 通知的执行顺序：

   1. @Before ->  @After  -> @AfterReturnning
   2. @Before -> @After  ->  @After Throwing

7. 切点的表达式：

   1. 最全写法：excution("public Integer com.shindo.service.MyCalculator.add(Integer,Integer)")
   2. 最简写法：excution("* *(..)")
   3. *符号代表一个或多个字符，只代表一层
   4. ..符号代表任意字符，代表多层

8. 底层动态代理：

   * jdk 自带的，通过Proxy.newInstance  和InvocationHandler，需要有接口
   * cglib，有没有接口都可以
   * jdk自带的动态代理和cglib动态代理，没有性能优劣之分，spring判断如果基于接口实现，使用jdk动态代理，如果没有接口，则使用cglib做动态代理。（早期cglib的性能会比jdk动态代理略为突出，不过随着jdk的不断迭代更新，两者性能相仿）

9. aop的使用场景：
   * 日志
   * 事务

10. 声明式事务
   1. mysql的对应maven版本说明：8对应的是8的版本，6对应的是5.7以上的版本，5.7以下的版本用5.1
   2. 包依赖：druid、mysql、spring-jdbc、spring-orm（object relation mapping 对应关系映射）、spring-transations（spring-tx）
   3. jdbcTemplate     看源码的时候有注释，使用maven的download sources 把源码包下载回来，那底层源码就会有注释，实际一般不用，后面会学spring 和 mybatis 结合
   4. 

   

