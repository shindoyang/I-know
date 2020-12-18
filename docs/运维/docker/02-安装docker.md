## 概述与历史

基于go语言开发

![image-20201218102238366](02-初始docker.assets/image-20201218102238366.png)

![image-20201218102324923](02-初始docker.assets/image-20201218102324923.png)

标准化应用打包本质上是镜像文件，包含了应用所需要的所有资源依稀外部引用，比如数据库，队列，web应用服务器......，同时还描述运行这些应用要哪些硬件上的要求：硬盘/cpu/内存，部署后还可以灵活调整。

![image-20201218102738103](02-初始docker.assets/image-20201218102738103.png)

ce：社区版，ee：企业版-收费

## 安装docker

windows 必须是win10 64 专业版：这版才有虚拟linux系统

https://docs.docker.com/

![image-20201218103537334](02-初始docker.assets/image-20201218103537334.png)

![image-20201218103548721](02-初始docker.assets/image-20201218103548721.png)

下载源是国外的，需增加国内的安装源（ce：社区版）

```
https://download.docker.com/linux/centos/docker-ce.repo
```

![image-20201218103854464](02-初始docker.assets/image-20201218103854464.png)

1、安装组件包

-y ：自动确认安装

yum-utils ：yum 的工具集

device-mapper-persistent-data：数据存储的驱动包

lvm2：数据存储的驱动包

docker的内部容器如果要做数据存储，是需要通过device-mapper-persistent-data、lvm2这两个驱动来完成。

 ```shell
yum install -y yum-utils device-mapper-persistent-data lvm2
 ```

2、修改yum的安装源

yum-config-manager ：是上一步yum-utils准备的简化工具

--add-repo：设置新的安装源

```shell
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

3、yum自动检测哪个哪个安装最快，优先使用

```she
yum makecache fast
```

4、安装docker

```shell
yum -y install docker-ce
```

5、验证docker是否安装：

```shell
service docker start
```

使用docker常用命令测试docker是否正常运行。

