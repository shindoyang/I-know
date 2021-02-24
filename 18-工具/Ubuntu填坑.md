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

2. 常见问题

   2.1. 关于在tweaks 优化界面，在设置外观选项时，无法设置gnome shell

![img](images/gnome-shell)

这是因为user-theme没有启用的原因；

解决方法：按照上述步骤安装user theme即可：https://extensions.gnome.org/extension/19/user-themes/

​	2.2.个人推荐安装

dash to dock

places menu

user theme



