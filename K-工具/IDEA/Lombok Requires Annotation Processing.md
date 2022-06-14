### IDEA启动报错：Lombok Requires Annotation Processing



#### 错误复现：

[Lombok](https://so.csdn.net/so/search?q=Lombok&spm=1001.2101.3001.7020)安装完成之后，启动项目时出现 “`Lombok Requires Annotation Processing...`”的错误提示。

```java
Lombok Requires Annotation Processing: Do you want to enable annotation processors?
```

![image-20220614163657637](.\images\image-20220614163657637.png)

或者

![image-20220614163728847](.\images\image-20220614163728847.png)

#### 错误原因：

小编不太理解，异常可以译：”Lombok需要进行批注处理。

通常我们的做法是：勾选“enable Annotation Processing”，指明不处理即可。

#### 解决办法：

1、根据异常提示操作：

直接点击错误提示后面的蓝色标识【Enable】（小编点完了所以变灰色），此操作等价于下面的步骤：

【File】-->【Settings】-->【Build】-->【Compiler】-->【Annotation Processing】--> 勾选“enable Annotation Processing”

![image-20220614163806823](.\images\image-20220614163806823.png)

2、个别情况下上面的方法并不适用，原因可能是Lombok需要更新，操作如下：

【File】-->【Settings】-->【Plugins】-->【Updates】更新下插件，尤其是Lombok，最后重启IDEA即可

![image-20220614163832534](.\images\image-20220614163832534.png)