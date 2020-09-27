#### JDK 1.5 

1. 泛型

   ArrayList list = new ArrayList();  -->    ArrayList<String>  list = new  ArrayList<String>();

2. 自动装箱、拆箱

   int i = list.get(0).parseInt();  ---> int i = list.get(0);

   原始类型与对应的包装类不用显式转换

3. for each

   i = 0; i< a.length; i++  ---->   for(int i:a){.....}

4. static import

   Math.sqrt();  ----> sqrt();

5. 变长参数

   int sum(int ...intlist)  有任意个参数，把他看作数组



#### JDK 1.6

1. 增强for循环语句

   Integer[]  numbers  = computeNumbers();

   for(int i =0; i< numbers.length; i++){

   ​	sum += numbers[i];

   }

   --->

   int sum = 0;

   for(int number : computeNumbers()){

   ​	sum += number;

   }

2. 监视和管理

   Java SE 6 对内存泄漏增强了分析以及诊断能力。当遇到 java.lang.OutOfMemory异常的时候，可以得到一个完整的堆栈信息，并且当堆已经满了的时候，会产生一个Log文件来记录这个致命错误。另外，JVM还添加了一个选项，允许你在堆满的时候运行脚本。

3. 插入式注解处理

   插入式注解处理API(JSR 269) 提供一套标准API来处理Annotations

4. 安全性



#### JDK 1.7

1. 模块化特性

   Java7 也是采用了模块的划分方式来提速，一些不是必须的模块并没有下载和安装，当虚拟机需要的时候，再下载相应的模块，同时对启动速度也有了很大的改善。

2. 多语言支持

   Java 7 的虚拟机对多种动态程序语言增加了支持，比如：Rubby、Python 等

3. 开发者的开发效率得到了改善

   switch中可以使用字符串

   在多线程并发与控制方面：轻量级的分离与合并框架，一个支持并发访问的HashMap等等。

   通过注解增强程序的静态检查。

   提供了一些新的API用于文件系统的访问、异步的输入输出操作、Socket通道的配置与绑定、多点数据包的传送等等。

4. 执行效率的提供。

   对对象针由64位压缩到32位指针相匹配的技术使得内存和内存带块的消耗得到了很大的降低因而提高了执行效率。提供了新的垃圾回收机制（G1）来降低垃圾回收的负载和增强垃圾回收的效果。
   
5. **try-with-resources 语句**，自动释放资源

   我们在使用资源的时候，必须关闭资源，比如使用jdbc连接或者imputStream的时候，必须在finally中将资源关闭。然而有的时候我们会忘记关闭资源，有没有更好的办法？

   ```java
   SqlSession sqlSession = sqlSessionFactory.openSession();
   try{
       //......
   }finally{
       sqlSession.close();
   }
   ```

   从JDK 1.7开始，Java 7 增强了try语句的功能，它允许在try关键字后跟一对圆括号，圆括号可以声明，初始化一个或多个资源，此处的资源是指那些必须在程序结束时必须关闭的资源（比如数据库连接、网络连接等），try语句在语句结束时会自动关闭这些资源。这种称为try-with-resources 语句。

   ```java
   try(SqlSession sql = SqlSessionFactory.openSession()){
       //......
   }
   ```

   像这样的话，执行完sqlSession会自动关闭，不用我们在finally中关闭，再也不用担心忘记关闭资源了。

   那为什么可以这样关闭资源呢？是不是所有资源都可以这样关闭呢？

   * 实际上只需要这些资源实现了Closeable 或者 AutoCloseAble接口，都可以实现自动关闭。

   比如SqlSession就是implement  Closeable ， Closeable 继承了 AutoCloseable  （since 1.7）

   几乎所有的资源都可以用这种方式实现自动关闭资源，比如OutputStream，BufferedReader，PrintStream，InputStream 等都可以。据说到目前为止，只有javaMail Transport对象不能利用这种方式实现自动关闭。

   **注意：**如果try()里面有个资源，用逗号分开，资源的close方法的调用顺序与他们的创建顺序相反。

   带有资源的try语句可以像一般的try 语句一样具有catch和finally块。

   在try-with-resources语句中，任何catch 或finally 块都是在声明的资源被关闭后才会执行的。



#### JDK 1.8

1. 



查看JDK各个版本的区别