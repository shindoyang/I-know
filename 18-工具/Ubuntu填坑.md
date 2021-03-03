# 安装smartgit 

1. 官方下载smartgit [下载地址](https://www.syntevo.com/smartgit/download/)

2. 移动安装包到用户目录下

   mv smartgit-linux-20_2_3.tar.gz /home/shindo

3. 解压安装包

   tar -xvf smartgit-linux-20_2_3.tar.gz 

   默认在解压后的目录名即为smartgit

4. 把smartgit添加到应用程序列表里

   cd smartgit/bin/

   sudo ./add-menuitem.sh 

5. 启动smartgit

   (1)可直接在应用程序列表里面，点击smartgit图表打开

   (2)也可以使用命令启动：

   cd smartgit/bin

   sudo ./smartgit.sh

6. 如果第一次使用smartgit的时候选择了商业版，那么30试用期到期后，可以删除（删除前先做文件备份）~/.config/smartgit/对应版本 下的license  和 preference.yml 文件进行再次选定。

   再次选定时，请切记选择非商业版即可。



# 卸载软件

dpkg -L smartgit

sudo apt-get  --purge remove smartgit



# 安装百度云盘

1. wget http://issuecdn.baidupcs.com/issue/netdisk/LinuxGuanjia/baidunetdisk_linux_2.0.1.deb

2. sudo dpkg -i baidunetdisk_linux_2.0.1.deb



# MacOS面板

1. 整体方案

> 方案中要下载的插件已上传百度云盘/软件仓库/ubuntu改macOS工具目录下

[Ubuntu20.04修改MAC风格](https://blog.csdn.net/sxdx2007401103/article/details/108145648)

上面的教程，dock优化一项，不使用plank的方案，而采用下方的 Dash to Dock

[如何美化让你的 Ubuntu 看起来更像MacOS](https://www.jianshu.com/p/161ec1956847)

> https://blog.csdn.net/weixin_45819130/article/details/107309056
>
> https://www.cnblogs.com/WXGC-yang/p/10423301.html



2. 常见问题

   2.1. 关于在tweaks 优化界面，在设置外观选项时，无法设置gnome shell

![img](images/gnome-shell)

这是因为user-theme没有启用的原因；

解决方法：按照上述步骤安装user theme即可：https://extensions.gnome.org/extension/19/user-themes/

​	2.2.个人推荐安装

dash to dock

places menu

user theme

# rdesktop 远程桌面访问 

1、安装rdesktop

sudo apt-get install rdesktop

2、远程链接

终端输入命令：

rdesktop -f -a 16 192.168.81.66



**退出****全屏：****crtl+alt+enter**



**用法：** **rdesktop[options] server[:port]**   

  命令参数常用的有：

  -u用户名

  -p密码

  -n客户端主机名（显示windows任务管理器中的连接客户端名）

  -g桌面大小（ 宽＊ 高）[也可以用 x(小写的X)]

   -f全屏模式,从全屏模式切换出来按Ctrl+Alt+Enter

  -a连接颜色深度（最高到16位），一般选16才会显示真彩色（window7支持32位）

  -0数字0表示连接上windows控制台，等效mstsc/console命令



> **参考：****https://blog.csdn.net/u012472945/article/details/79447989**

# ubuntu 17.04 启动easyConnect

```
# 命令行启动：
usr/share/sangfor/EasyConnect/EasyConnect
# 启动失败的补偿命令：
sudo /bin/bash /usr/share/sangfor/EasyConnect/resources/shell/sslservice.sh
```



## 强制解锁

ubuntu下安装出现错误：另一个进程已经为dpkg frontend 加锁

直接删除文件，使用强制解锁办法

1.sudo rm /var/cache/apt/archives/lock

2.sudo rm /var/lib/dpkg/lock