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

1. JAVA 8 允许我们给接口添加一个非抽象的方法实现，只需要使用default 关键字即可，这个特征又叫扩展方法。

2. Lambda表达式

   在java 8 中你就没必要使用传统的匿名对象的方法，而是提供了更简洁的方法，Lambda表达式：

   ```java
   Collections.sort(names, (String a, String b) -> return b.compareTo(a))
   ```

   

3. 函数式接口

   Lambda表达式是如何在java的类型系统中表示的呢？每一个Lambda表达式都对应一个类型，通常是接口类型。而“函数式接口”是指仅仅只包含一个抽象方法的接口，每一个该类型的lambda表达式都会被匹配到这个抽象方法。因为默认方法不算抽象方法，所以你也可以给你的函数式接口添加默认方法。

4. 方法与构造函数引用

   Java 8 允许你使用 :: 关键字来传递方法或者构造函数引用，可以一个对象的方法：

   converter =  something::startswith

5. Lambda 作用域

   在lambda表达式中访问外层作用域和老版本的匿名对象中的方式很相似。你可以直接访问标记了final的外层局部变量，或者实例的字段以及静态变量。

6. 访问局部变量

   可以直接在lambda表达式中访问外层的局部变量

7. 访问对象字段与静态变量

   和本地变量不同的是，lambda内部对于实例的字段以及静态变量是即可读又可写。该行为和匿名对象一致的。

8. 访问接口的默认方法

   JDK 1.8 API 包含了很多内建的函数式接口，在老Java中常用到的比如Comparator或者Runnable接口，这些接口都增加了@FunctionalInterface注解以便能用在lambda上。

   java 8 API 同样提供了很多全新的函数式接口来让工作更方便，有一些接口式来自Google Guava库里的，即便你对这些很熟悉了，还是有必要看看这些式如何扩展到lambda上使用的。



#### JDK 1.9 

java 7 2011发布，Java8 2014发布，Java9 发布于2017年9月21日。你可以已经听说过java9的模块系统，但是这个新版本还有许多其他的更新。这里有九个令人兴奋的新功能将与java 9一起发布。

1. Java平台级模块系统

   java9 的定义功能是一套全新的模块系统。当代码库越来越大，创建复杂，盘根错节的“意大利面条式代码”的几率呈指数级增长。这时候就得面对两个基础问题：很难真正地对代码进行封装，而系统并没有对不同部分（也就是jar文件）之间的依赖关系有个明确的概念。每一个公共类都可以被类路径之下任何其他的公共类所访问到，这样就会导致无意中使用了并不想被公开访问的api。此外，类路径本身也存在问题：你怎么知道所有需要的jar都已经有了，或者是不是会有重复的项呢？模块系统把这两个问题都给解决了。

   

2. 



查看JDK各个版本的区别