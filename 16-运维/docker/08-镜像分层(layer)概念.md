![image-20201223155521296](images/image-20201223155521296.png)

镜像层：只读

容器层：可读可写

![image-20201223155809169](images/image-20201223155809169.png)

每一行下面的字符串是什么意思？

![image-20201223161359389](images/image-20201223161359389.png)

其实是临时镜像，一层层叠加功能，最后的一层就是完整的镜像。

临时镜像只能用于镜像的构建，不可以直接使用。

有点像RGB游戏里面的存档点



Docker在使用Dockerfile构建镜像的过程中采用了两种机制：分层和系统快照

1. 分层：按层进行堆叠，最后全部完成才是完整的容器
2. 快照，每一步采用临时镜像存档（优点：临时容器在构建过程中是可以重用的）

例子：

![image-20201223163126132](images/image-20201223163126132.png)

![image-20201223163232181](images/image-20201223163232181.png)

![image-20201223163610710](images/image-20201223163610710.png)

![image-20201223163650148](images/image-20201223163650148.png)

再次修改Dockerfile：

![image-20201223163730665](images/image-20201223163730665.png)

将构建的版本号改为1.1：

![image-20201223163757513](images/image-20201223163757513.png)

![image-20201223163825965](images/image-20201223163825965.png)

发现前三步直接重用了临时镜像