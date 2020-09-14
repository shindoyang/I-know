### 安装centOS 7

 

![image-20200914222610022](01-vmware12安装linux虚拟机.assets/image-20200914222610022.png)

![image-20200914222700956](01-vmware12安装linux虚拟机.assets/image-20200914222700956.png)



![image-20200914222721657](01-vmware12安装linux虚拟机.assets/image-20200914222721657.png)



![image-20200914222755198](01-vmware12安装linux虚拟机.assets/image-20200914222755198.png)



![image-20200914223358347](01-vmware12安装linux虚拟机.assets/image-20200914223358347.png)

![image-20200914223513642](01-vmware12安装linux虚拟机.assets/image-20200914223513642.png)

![image-20200914223555247](01-vmware12安装linux虚拟机.assets/image-20200914223555247.png)

![image-20200914223704123](01-vmware12安装linux虚拟机.assets/image-20200914223704123.png)



![image-20200914223750824](01-vmware12安装linux虚拟机.assets/image-20200914223750824.png)

![image-20200914223803790](01-vmware12安装linux虚拟机.assets/image-20200914223803790.png)

![image-20200914223828090](01-vmware12安装linux虚拟机.assets/image-20200914223828090.png)

![image-20200914224006609](01-vmware12安装linux虚拟机.assets/image-20200914224006609.png)

![image-20200914224038004](01-vmware12安装linux虚拟机.assets/image-20200914224038004.png)

![image-20200914224051282](01-vmware12安装linux虚拟机.assets/image-20200914224051282.png)

![image-20200914224745366](01-vmware12安装linux虚拟机.assets/image-20200914224745366.png)

---



#### 安装centOS 6.5操作系统

双击CD/DVD，选择系统镜像

![image-20200914224857462](01-vmware12安装linux虚拟机.assets/image-20200914224857462.png)



开启虚拟机



skip跳过

![image-20200914230334436](01-vmware12安装linux虚拟机.assets/image-20200914230334436.png)

![image-20200914230844116](01-vmware12安装linux虚拟机.assets/image-20200914230844116.png)

![image-20200915060459637](01-vmware12安装linux虚拟机.assets/image-20200915060459637.png)

![image-20200915060525941](01-vmware12安装linux虚拟机.assets/image-20200915060525941.png)

![image-20200915060607628](01-vmware12安装linux虚拟机.assets/image-20200915060607628.png)

![image-20200915060703116](01-vmware12安装linux虚拟机.assets/image-20200915060703116.png)

选择，yes,discard any data

![image-20200915060830485](01-vmware12安装linux虚拟机.assets/image-20200915060830485.png)

给系统起名字

![image-20200915060925686](01-vmware12安装linux虚拟机.assets/image-20200915060925686.png)

![image-20200915061022661](01-vmware12安装linux虚拟机.assets/image-20200915061022661.png)

![image-20200915061114473](01-vmware12安装linux虚拟机.assets/image-20200915061114473.png)

密码太简单，给出警告，这里选择忽略：Use Anyway

![image-20200915061949884](01-vmware12安装linux虚拟机.assets/image-20200915061949884.png)

创建分区，选择：create custom layout

原来是设置了100g,这里不是大数据，缩小到20G

下面主要分三个区：

![image-20200915063653922](01-vmware12安装linux虚拟机.assets/image-20200915063653922.png)

（第一块磁盘叫sda，如果有第二/第三块磁盘，就叫sdb,sdc，如此类推 ）

创建第一个分区：引导程序区 boot

![image-20200915062440603](01-vmware12安装linux虚拟机.assets/image-20200915062440603.png)

点create

![image-20200915062520964](01-vmware12安装linux虚拟机.assets/image-20200915062520964.png)

再点create

![image-20200915062548755](01-vmware12安装linux虚拟机.assets/image-20200915062548755.png)

挂载到/boot

![image-20200915062641862](01-vmware12安装linux虚拟机.assets/image-20200915062641862.png)

下面点create-create创建第二个分区：交换分区

![image-20200915062832726](01-vmware12安装linux虚拟机.assets/image-20200915062832726.png)

内存大小设为：2048，一般是虚拟机内存的两倍（这里因为虚拟机内存是1G）

![image-20200915062929873](01-vmware12安装linux虚拟机.assets/image-20200915062929873.png)

创建第三个分区：用户分区

![image-20200915063045708](01-vmware12安装linux虚拟机.assets/image-20200915063045708.png)

主要要选择：Fill to maximum allowable size，就是把磁盘剩余的空间都给到用户分区(/)

![image-20200915063201243](01-vmware12安装linux虚拟机.assets/image-20200915063201243.png)

分区创建完，点Next

![image-20200915063235269](01-vmware12安装linux虚拟机.assets/image-20200915063235269.png)

format格式化磁盘

![image-20200915063330695](01-vmware12安装linux虚拟机.assets/image-20200915063330695.png)

提示：是否把分区的变化写入磁盘，直接选：Write changes to disk

![image-20200915063453062](01-vmware12安装linux虚拟机.assets/image-20200915063453062.png)

安装引导程序，直至安装完成，系统即安装完成。

---

### 配置虚拟机的网络服务

其实就是配置虚拟机网络，让其能够连接外网。原理和windows相仿。

大致步骤：



1、找到网卡位置：cd /etc/sysconfig/network-scripts/

![image-20200915064427806](01-vmware12安装linux虚拟机.assets/image-20200915064427806.png)

ls查看：

![image-20200915064606945](01-vmware12安装linux虚拟机.assets/image-20200915064606945.png)

ifcfg-eth0，表示第0块网卡  if代表interface，cfg代表config，eth 代表ethirnet,以太网，0代表第0块网卡

编辑网卡中的协议：vi ifcfg-eth0

![image-20200915065423676](01-vmware12安装linux虚拟机.assets/image-20200915065423676.png)

HWADDR，网卡物理地址，因为要克隆，可以干掉（包括UUID）

ONBOOT=yes，代表是否启用这块网卡

MM_CONTROLLED=yes，

BOOTPROTO=dhcp，代表动态获取ip地址的方式，改为static ，手动获取ip的意思

配置ip（保证=号前后没有空格）：

IPADDR=

怎么知道要写什么ip，方式：

![image-20200915065628032](01-vmware12安装linux虚拟机.assets/image-20200915065628032.png)



![image-20200915065718467](01-vmware12安装linux虚拟机.assets/image-20200915065718467.png)

更改设置

![image-20200915065754817](01-vmware12安装linux虚拟机.assets/image-20200915065754817.png)

搭建虚拟机的时候，已经提前设置好net模式

![image-20200915065921206](01-vmware12安装linux虚拟机.assets/image-20200915065921206.png)

子网IP范围：1-254

255已经被用作广播号

NAT设置，发现.2已经被当作网关了

![image-20200915070033951](01-vmware12安装linux虚拟机.assets/image-20200915070033951.png)

还有一个要注意的地方，：将主机虚拟适配器连接到此网络，是对应window系统下的虚拟网卡，用1的地址



