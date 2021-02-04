本节涉及6个命令

![image-20201222065812067](images/image-20201222065812067.png)

可以通过imageID来判断当前镜像和其他镜像是否相同

![image-20201222072505941](images/image-20201222072505941.png)

![image-20201222072637776](images/image-20201222072637776.png)

端口映射：

![image-20201222072833463](images/image-20201222072833463.png)

端口映射命令：

![image-20201222073020259](images/image-20201222073020259.png)

查看启用的端口：

![image-20201222073128096](images/image-20201222073128096.png)

后台运行（非阻塞状态）： -d

![image-20201222073334679](images/image-20201222073334679.png)

查看当前运行的容器：docker  ps

![image-20201222073441615](images/image-20201222073441615.png)

查看所有容器（包括已停止的）：docker ps -a

停止容器：docker stop 容器ID

删除容器：docker rm 容器ID

强制删除容器：docker rm -f 容器ID

删除镜像：docker rmi 镜像ID

强制删除镜像：docker rmi -f 镜像ID



