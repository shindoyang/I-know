Linux执行.sh文件，提示No such file or directory错误的解决办法

解决方法

分析原因，可能因为我平台迁移碰到权限问题我们来进行权限转换

1）在Windows下转换：

利用一些编辑器如UltraEdit或EditPlus等工具先将脚本编码转换，再放到Linux中执行。转换方式如下（UltraEdit）：File-->Conversions-->DOS->UNIX即可。

2)方法

用vim打开该sh文件，输入：

[plain]
:set ff 

回车，显示fileformat=dos，重新设置下文件格式：

[plain]
:set ff=unix 

保存退出:

[plain]
:wq 

再执行，竟然可以了

3）在linux中的权限转换

也可在Linux中转换：

首先要确保文件有可执行权限

#chmod u+x filename

然后修改文件格式

#vi filename

以上三种方法都可以帮助大家解决有关linux执行.sh文件时出现no such file or directiory一问题，有需要的朋友可以参考下了，希望本文分享对大家有所帮助。