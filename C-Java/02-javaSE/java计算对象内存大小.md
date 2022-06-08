# 计算Java对象所占内存大小的几种方法

### 方法一 RamUsageEstimator 【推荐】
使用RamUsageEstimator计算对象占用堆的内存大小

**maven 依赖**

```xml
<!-- https://mvnrepository.com/artifact/com.carrotsearch/java-sizeof -->
<dependency>
    <groupId>com.carrotsearch</groupId>
    <artifactId>java-sizeof</artifactId>
    <version>0.0.5</version>
</dependency>
```

**测试**

```java
ArrayList<Integer> list = new ArrayList<>();
for (int i = 0; i < 1000; i++) {
    list.add(i);
}
//计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果，如：2KB
System.out.println("humanSizeOf:" + RamUsageEstimator.humanSizeOf(list));
//计算指定对象本身在堆空间的大小，单位字节
System.out.println("shallowSizeOf:" + RamUsageEstimator.shallowSizeOf(list));
//计算指定对象及其引用树上的所有对象的综合大小，单位字节
System.out.println("sizeOf:" + RamUsageEstimator.sizeOf(list));

```

**实际使用**

```java
    public static void main(String[] args)throws Exception {
        String text = "{\"requestHost\":\"192.168.200.72\",\"phoneVars\":[{\"mobile\":\"13318951715\",\"vars\":\"\",\"content\":\"【中智政源】您有访客来访，请点击查看来访人详情。\"},{\"mobile\":\"13318952214\",\"vars\":\"\",\"content\":\"【中智政源】您有访客来访，请点击查看来访人详情。\"}],\"batchNo\":\"%s\",\"signText\":\"中智政源\",\"nmsType\":\"1\",\"templateName\":\"通知短信测试\",\"inQueueTime\":1651900504346,\"custId\":\"21060111243897530933896649468547\",\"ChannelWayconfig\":{\"accessNum\":\"1069602088\",\"accountName\":\"东信测试账号\",\"appId\":\"td5673\",\"appSecret\":\"7aSdj4GT\",\"channel\":1,\"cmppSite\":\"120.133.16.65\",\"commitBatchNum\":500,\"nmsChannelType\":\"public\",\"portNum\":36001,\"qpsRate\":250,\"spCode\":4902},\"phoneNum\":500,\"partBatchNo\":\"%s\"}";
        String uid = Base.getUid(32);
        String batchNo = "202205091119129719";
        JSONObject jsonObject = JSONObject.parseObject(String.format(text, batchNo, uid));
//        System.out.println(jsonObject);
        NormalMsgContext smsContext = JSONObject.parseObject(jsonObject.toString(), NormalMsgContext.class);

        //计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果，如：2KB
        System.out.println("humanSizeOf:" + RamUsageEstimator.humanSizeOf(smsContext));
        //计算指定对象本身在堆空间的大小，单位字节
        System.out.println("shallowSizeOf:" + RamUsageEstimator.shallowSizeOf(smsContext));
        //计算指定对象及其引用树上的所有对象的综合大小，单位字节
        System.out.println("sizeOf:" + RamUsageEstimator.sizeOf(smsContext));
    }
```



### 方法二 Instrumentation

可以使用java.lang.instrument.Instrumentation.getObjectSize()方法计算一个运行时对象的大小。这种方式需要在JVM启动时，通过指定代理的方式，让JVM来实例化它。

声明一个premain方法，它接收一个String类型和instrumentation参数。在premain函数中，将instrumentation参数赋给一个静态变量，其它地方就可以使用了。

 <img src=".\images\1.webp" alt="img" style="zoom:50%;" />

用javac编译一下生成class文件，然后使用jar -cvf java-agent-sizetool.jar SizeTool.class打包成jar文件。

假设main方法所在的jar包为：A.jar，premain方法所在的jar包为B.jar。为main所在的代码打包时，声明一个MANIFEST.MF清单文件，如下：

Manifest-Version:1.0

Main-Class:yp.tools.Main

Premain-Class:yp.tools.SizeTool

这里是直接使用IDEA打包的教程：[IDEA打包成jar包教程](https://links.jianshu.com/go?to=https%3A%2F%2Fjingyan.baidu.com%2Farticle%2F7e4409531fbf292fc1e2ef51.html)

然后执行java命令执行jar文件：

java-javaagent:B.jar -jar A.jar

这种方法必须启动一个javaagent，因此要修改Java的启动参数，比较麻烦。并且得到的是Shallow Size，遇到引用时，只计算引用的长度，不计算所引用的对象的实际大小。可以考虑使用递归计算实际大小。



### 方法三 使用Unsafe

unsafe对象可以获取到一个对象中各个属性的内存指针的偏移量，可以利用其来计算一个对象的大小。

使用反射获取Unsafe对象：

 <img src=".\images\2.webp" alt="img" style="zoom:50%;" />

简单研究一下Unsafe类先，先写一个测试类：

 <img src=".\images\3.webp" alt="img" style="zoom:50%;" />

（1）获取实例字段的偏移地址，偏移最小的那个字段(仅挨着头部)就是对象头的大小：

 ![img](.\images\4.webp)

输出12和16，12刚好也是对象头的大小。

（2）获取数组的头部大小和元素大小：

 <img src=".\images\5.webp" alt="img" style="zoom:67%;" />

输出的16也是数组对象头的大小。如果是多维数组就需要递归来计算地址。

Unsafe还可以获取类的静态字段偏移，获取字段属性值等等，不过这些对于内存测量意义不大。因此利用Unsafe加上递归嵌套也可以获取JAVA对象大小。

使用Unsafe可以很精确的计算出对象头的大小和每个字段的偏移，但是需要通过反射才能获取Unsafe实例。对象最后一个字段的大小需要手工计算，其次需要手工写代码递归计算才能得到对象及其所引用的对象的综合大小。



### 第三方的库

（1）备注：8.8.1测试Object的对象方法提取出来变成了SizeOfObject的，但是这个方法貌似有问题（其他测试数组，String等没问题），建议还是使用4.0.0版本，不过效率超级差。

```maven
<!-- https://mvnrepository.com/artifact/org.apache.lucene/lucene-core -->
<dependency>
  <groupId>org.apache.lucene</groupId>
  <artifactId>lucene-core</artifactId>
  <version>8.8.1</version>
</dependency>
```

（2）

```maven
<!-- https://mvnrepository.com/artifact/com.carrotsearch/java-sizeof -->
<dependency>
  <groupId>com.carrotsearch</groupId>
  <artifactId>java-sizeof</artifactId>
  <version>0.0.5</version>
</dependency>
```

（3）https://github.com/jbellis/jamm

（4）目前效率最高：Spark的SizeEstimator.estimate(Object object)，但是依然不够高。（第一次使用比较耗时，后续的话快的时候可以达到300ms）
