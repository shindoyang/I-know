spring session 改造方案

redis存储session的单个用户的完整信息：

![image-20201216084134016](08-spring session 配合oauth.assets/image-20201216084134016.png)



利用了的redis的过期机制，把数据做过期时间管理

压测性能对比：

最原始的版本---> 改用springsession后的单节点版本--->开启双节点后