![image-20201225081904435](10-Dockerfile执行指令.assets/image-20201225081904435.png)

三个执行命令的区别：

![image-20201225082027661](10-Dockerfile执行指令.assets/image-20201225082027661.png)

run：镜像创建时，修改镜像内部的文件

cmd\entrypoint：创建容器时， 对容器内执行命令。

---

书写语法：

![image-20201225082545500](10-Dockerfile执行指令.assets/image-20201225082545500.png)

![image-20201225083008660](10-Dockerfile执行指令.assets/image-20201225083008660.png)

![image-20201225083215955](10-Dockerfile执行指令.assets/image-20201225083215955.png)

![image-20201225083314832](10-Dockerfile执行指令.assets/image-20201225083314832.png)

根本区别：是否创建子进程

结论：在大多数情况下，从综合应用的角度考虑，推荐使用Exec的方式运行指令。

![image-20201225085226737](10-Dockerfile执行指令.assets/image-20201225085226737.png)

Dockerfile中如果书写了多行Entrypoint命令，只有最后一行会被执行。

![image-20201225155658440](10-Dockerfile执行指令.assets/image-20201225155658440.png)

entrypoint一定会被运行，而cmd不一定会被运行

