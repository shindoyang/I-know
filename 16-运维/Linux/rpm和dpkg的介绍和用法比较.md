> https://blog.csdn.net/weixin_38111667/article/details/86062866



linux的经销商主要分为 Red Hat  和 Debian 两大厂牌。

而这两个厂商对应的软件管理机制则为：

Red Hat 的 rpm

Debian 的 dpkg

但是不管rpm和dpkg 都无法很好的处理软件的依赖关系，比如你要安全A，其他他依赖了D，而D又依赖了C和E。

这是linux开发商就提供了 **‘线上升级’** 机制。

对应的，在dpkg管理机制上开发出APT的线上升级机制，RPM 则有Red Hat的yum线上升级机制。



