### 变量(四)

在设置了"取处理程序"的情况下，Nginx变量也可以选择将其值容器用作缓存，这样在多次读取变量的时候，就只需要调用“取处理程序”计算一次。我们下面就来看一个这样的例子：

```nginx
map $args $foo{
    default 0;
    debug   1;
}

server {
    listen 8080;
    location /test{
        set $orig_foo  $foo;
        set $args debug;
        
        echo "original foo: $orig_foo";
        echo "foo: $foo";
    }
}
```

这里首次用到了标准**ngx_map**模块的**map**配置指令，我们有必要在此介绍一下。map在英文中除了“地图”之外，也有“映射”的意思。比方说，中学数学里讲的“函数”就是一种“映射”。而Nginx的这个**map**指令就可以用于定义两个Nginx变量之间的映射关系，或者说是函数关系。回到上面这个例子，我们用**map**指令定义了用户变量$foo与$args内建变量之间的映射关系。特别地，用数学上的函数记法 y = f(x) 来说，我们的$args就是"自变量"x，而$foo 则是“因变量”y ，即$foo 的值是有$args 的值来决定的，或者按照书写顺序可以说，我们将$args变量的值映射到了$foo变量上。

现在我们再来看**map**指令定义的映射规则：

```nginx
map $args $foo{
    default  0;
    debug    1;
}
```

花括号中第一行的default是一个特殊的匹配条件，