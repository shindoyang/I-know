
最简版：


```shell
#!/bin/bash
ID=`ps -ef|grep imos-admin.jar |grep -v grep | awk '{print $2}'`
echo $ID
/opt/jdk8u242-b08/bin/java -jar /apps/arthas/arthas-boot.jar $ID

```

 ![image-20210528150231972](E:\I know\16-运维\Linux\images\image-20210528150231972.png)



入参版：

```shell
#!/bin/bash

PARA=${1}
echo PARA = $PARA

PROJECT_NAME=

if [ ! ${PARA} ];then
  echo is null
  PROJECT_NAME=imos-admin.jar
else
  echo not null
  PROJECT_NAME=${PARA}
  echo $PROJECT_NAME
fi

echo $PROJECT_NAME

ID=`ps -ef|grep ${PROJECT_NAME} |grep -v grep | awk '{print $2}'`

echo project_name = $PROJECT_NAME , ID = $ID

/opt/jdk8u242-b08/bin/java -jar /apps/arthas/arthas-boot.jar $ID

```


将以上脚本命令为 arthas.sh 并保存

使用 `sh arthas.sh`命令即可执行该脚本，启动arthas

启动效果如下：

 ![image-20210528155046554](E:\I know\16-运维\Linux\images\image-20210528155046554.png)





