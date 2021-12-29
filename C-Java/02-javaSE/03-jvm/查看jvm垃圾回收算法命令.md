### 命令

```shell
java -XX:_PrintCommandLineFlags -version
```

windows系统结果如下：

```shell
-XX:G1ConcRefinementThreads=4 -XX:GCDrainStackTargetSize=64 -XX:InitialHeapSize=266535360 -XX:MaxHeapSize=4264565760 -XX:+PrintCommandLineFlags -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCa
che -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseLargePagesIndividualAllocation
java version "11.0.9" 2020-10-20 LTS
Java(TM) SE Runtime Environment 18.9 (build 11.0.9+7-LTS)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.9+7-LTS, mixed mode)

```

其中，`-XX:+UseG1GC `表示java11默认使用了G1垃圾回收算法。

切换到jdk8，再执行上述命令看下结果：

```shell
-XX:InitialHeapSize=266535360 -XX:MaxHeapSize=4264565760 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC
java version "1.8.0_111"
Java(TM) SE Runtime Environment (build 1.8.0_111-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.111-b14, mixed mode)
```

其中，`-XX:+UseParallelGC `表示java8默认使用了ps+po的垃圾回收算法。

