#### 使用mysql实现分布式锁

使用订单号作为主键，抢到了就入库，下一个人来抢，就抢不到，会主键冲突

jvm锁--解决不了

mysql锁--性能不好，比redis差10万倍

手写redis--

方案一：

![image-20200831210129509](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20200831210129509.png)

方案二：

单个redisson

redis 锁续期 3分之一时间，业务未执行完，自动续期。



哨兵模式





红锁--多个redis





