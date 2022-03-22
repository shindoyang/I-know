## Windows查看端口占用

查看端口占用、查看PID对应的进程、并终止进程

### Windows下：

#### 查看端口占用

```shell
netstat -ano | findstr "端口号"
```

获取到pid

#### 查看PID对应的进程

```shell
tasklist | findstr "进程ID"
```

获取到进程名称

#### 终止进程

```shell
taskkill /F /PID 进程ID
```



### Linux下：

```shell
ps -aux | grep  进程名
kill -9  PID
```

