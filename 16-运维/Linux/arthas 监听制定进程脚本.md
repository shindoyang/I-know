```shell
#!/bin/bash
ID=`ps -ef|grep imos-admin.jar |grep -v grep | awk '{print $2}'`
echo $ID
/opt/jdk8u242-b08/bin/java -jar /apps/arthas/arthas-boot.jar $ID

```



将以上脚本命令为 arthas.sh 并保存

使用 `sh arthas.sh`命令即可执行该脚本，启动arthas

启动效果如下：

 ![image-20210528150231972](E:\I know\16-运维\Linux\images\image-20210528150231972.png)