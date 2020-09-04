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



