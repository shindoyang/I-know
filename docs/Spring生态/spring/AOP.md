### 使用技能

1. 导包
2. 打开扫描机制
3. 加入@Aspect注解
4. 底层动态代理：
   * jdk 自带的，通过Proxy.newInstance  和InvocationHandler，需要有接口
   * cglib，有没有接口都可以
   * jdk自带的动态代理和cglib动态代理，没有性能优劣之分，spring判断如果基于接口实现，使用jdk动态代理，如果没有接口，则使用cglib做动态代理。（早期cglib的性能会比jdk动态代理略为突出，不过随着jdk的不断迭代更新，两者性能相仿）
5. aop的使用场景：
   * 日志
   * 事务
6. 声明式事务
   1. mysql的对应maven版本说明：8对应的是8的版本，6对应的是5.7以上的版本，5.7以下的版本用5.1
   2. 包依赖：druid、mysql、spring-jdbc、spring-orm（object relation mapping 对应关系映射）、spring-transations（spring-tx）
   3. jdbcTemplate     看源码的时候有注释，使用maven的download sources 把源码包下载回来，那底层源码就会有注释

7. 士大夫

