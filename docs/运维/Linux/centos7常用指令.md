### 文件与目录操作

| 命令                    | 解析                                                         |
| ----------------------- | ------------------------------------------------------------ |
| cd /home                | 进入‘/home’目录                                              |
| cd ..                   | 返回上一级目录                                               |
| cd ../..                | 返回上两级目录                                               |
| cd -                    | 返回上次所在目录                                             |
| cp file1 file2          | 将file1复制为file2                                           |
| cp -a dir1 dir2         | 复制一个目录                                                 |
| cp -a /tmp/dir1 .       | 复制一个目录到当前工作目录(.代表当前目录)                    |
| ls                      | 查看目录中的文件                                             |
| ls -a                   | 显示隐藏文件                                                 |
| ls -l                   | 显示详细信息                                                 |
| ls -lrt                 | 按时间显示文件(l表示详细列表，r表示反向排序，t表示按时间排序) |
| pwd                     | 显示工作路径                                                 |
| mkdir dir1              | 创建‘dir1’目录                                               |
| mkdir dir1 dir2         | 同时创建两个目录                                             |
| mkdir -p /tmp/dir1/dir2 | 创建一个目录树                                               |
| mv dir1 dir2            | 移动/重命名一个目录                                          |
| rm -f file1             | 删除‘file1’，会出现确认对话                                  |
| rm -rf dir1             | 删除'file1'，强制删除，不会出现确认对话，所以该命令需谨慎    |


### 查看文件内容

| 命令          | 解析                                 |
| ------------- | ------------------------------------ |
| cat file1     | 从第一个字节开始正向查看文件的内容   |
| head -2 file1 | 查看一个文件的前两行                 |
| more file1    | 查看一个长文件的内容                 |
| tac file1     | 从最后一行开始反向查看一个文件的内容 |
| tail -3 file1 | 查看一个文件的最后三行               |
| vi file       | 打开并浏览文件                       |

### 文本内容处理

| 命令                 | 解析                                            |
| -------------------- | ----------------------------------------------- |
| grep str /tmp/test   | 在文件'/tmp/test'中查找'str'                    |
| grep ^str /tmp/test  | 在文件'/tmp/test'中查找以‘str’开始的行          |
| grep [0-9] /tmp/test | 查找‘/tmp/test’文件中所有包含数字的行           |
| grep str -r /tmp/*   | 在目录'/tmp'及其子目录中查找'str'               |
| diff file1 file2     | 找出两个文件的不同处                            |
| sdiff file1 file2    | 以对比的方式显示两个文件的不同                  |
| vi file              | i/a  进入编辑文本模式（i:向前插入，a:向后插入） |
|                      | Esc 退出编辑模式                                |
|                      | :w 保存当前修改                                 |
|                      | :q 不保存退出                                   |
|                      | :q! 不保存强制退出                              |
|                      | :wq 保存当前修改并退出vi                        |
|                      | :set nu 显示行号                                |
|                      | :set nonu 取消行号显示                          |
|                      | /str 向下查找文本中包含str字段                  |
|                      | ?str 向上查找文本中包含str字段                  |
|                      | dd 删除整行                                     |
|                      | 待补充                                          |

### 查询操作

|      |      |
| ---- | ---- |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |

### 压缩、解压

|      |      |
| ---- | ---- |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |

### yum安装器

|      |      |
| ---- | ---- |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |

### 网络相关

|      |      |
| ---- | ---- |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |

```yml
CentOS防火墙的关闭，关闭其服务即可：

查看CentOS防火墙信息：/etc/init.d/iptables status

关闭CentOS防火墙服务：/etc/init.d/iptables stop

永久关闭防火墙： chkconfig –level 35 iptables off

如果是centos7
	
[root@rhel7 ~]# systemctl status firewalld.service
 
[root@rhel7 ~]# systemctl stop firewalld.service
 
[root@rhel7 ~]# systemctl disable firewalld.service
 
[root@rhel7 ~]# systemctl status firewalld.service

扩展知识：

启动一个服务：systemctl start firewalld.service

关闭一个服务：systemctl stop firewalld.service

重启一个服务：systemctl restart firewalld.service

显示一个服务的状态：systemctl status firewalld.service

在开机时启用一个服务：systemctl enable firewalld.service

在开机时禁用一个服务：systemctl disable firewalld.service

查看服务是否开机启动：systemctl is-enabled firewalld.service;echo $?

查看已启动的服务列表：systemctl list-unit-files|grep enabled
```



### 系统相关

|      |      |
| ---- | ---- |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |

### XShell相关操作

#### 窗体快捷键

|      |      |
| ---- | ---- |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |
|      |      |

#### 操作小技巧

