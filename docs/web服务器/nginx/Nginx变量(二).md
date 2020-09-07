### 变量(二)

关于Nginx变量的另一个常见误区是认为变量容器的生命期，是与location配置块绑定的。其实不然，我们来看一个设计"内部跳转"的例子：

```nginx
server{
    listen 8080;
    location /foo{
        set $a hello;
        echo_exec /bar;
    }
    location /bar{
        echo "a = [$a]";
    }
}
```

这里我们在location /foo中，使用第三方模块ngx_echo提供的**echo_exec**配置指令，发起到location /bar的"内部跳转"。所谓"内部跳转"，就是在处理请求的过程中，于服务器内部，从一个location跳转到另一个location的过程。这不同于利用http状态码301和302所进行的”内部跳转“，因为后者是有HTTP客户端配合进行跳转的，而且在客户端，用户可以通过浏览器地址栏这样的的界面，看到请求的URL地址发了变化。内部跳转和Bourne Shell（或Bash）中的exec命令很像，都是”有去无回“。另一个相近的例子是C语言中的goto语句。

既然是内部跳转，当前正在处理的请求就还是原来那个，只是当前的location发生了变化，所以还是原来的那一套Nginx变量的容器副本。对应到上例，如果我们请求的是/foo这个接口，那么整个工程流程是这样的：现在location /foo中通过**set**指令将$a变量的值赋为字符串hello，然后通过**echo exec**指令发起内部跳转，又进入到location /bar中，再输出$a变量的值。因为$a还是原来的$a，所以我们可以期望得到hello这行输出。测试证实了这一点：

```shell
$ curl 'http://localhost:8080/foo'
a = [hello]
```

但如果我们从客户端直接访问/bar接口，就会得到空的$a变量的值，因为它依赖于 location /foo 来对$a进行初始化。

从上面这个例子我们看到，一个请求在其处理过程中，即使经历多个不同的location配置快，它使用的还是同一套Nginx变量的副本。这里，我们也首次涉及到了“内部跳转”这个概念。 值得一提的是，标准**ngx_rewrite**模块的**rewrite**配置指令其实也可以发起“内部跳转”，例如上面那个例子用**rewrite**配置指令可以改写成下面这样的形式：

```nginx
server{
    listen 8080;
    
    location /foo{
        set $a hello;
        rewirte ^ /bar;
    }
    location /bar{
        echo "a = [$a]";
    }
}
```

其效果和使用 echo_exec 是完全相同的。



