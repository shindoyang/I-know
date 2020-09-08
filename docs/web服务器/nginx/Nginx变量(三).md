### 变量(三)

也有一些内建变量是支持改写的，其中一个例子是**$args**。这个变量在读取时返回当前请求的URL参数串（即请求URL中问号后面的部分，如果有的话），而在赋值时可以直接修改参数串。我们来看一个例子：

```nginx
location /test{
    set $orig_args $args;
    set $args "a=3&b=4";
    
    echo "original args: $origs_args";
    echo "args: $args";
}
```

这里我们把原始的URL参数串先保存在$orig_args变量中，然后通过改写**$args**变量来修改当前的URL参数串，最后我们用**echo**指令分别输出$orig_args和**$args**变量的值。接下来我们来测试这个/test接口：

```shell
$ curl 'http://localhost:8080/test'
original args: 
args: a=3&b=4

$ curl 'http://localhost:8080/test?a-0&b=1&c=2'
original args: a=0&b=1&c=2
args: a=3&b=4
```

在第一次测试中，我们没有设置任务URL参数串，所以输出$orig_args变量的值时便得到空。而在第一次和第二次测试中，无论我们是否提供URL参数串，参数串都会在location /test中被强行改写成 a=3&b=4

需要特别指出的是，这里的**$args**变量和**$args_XXX**一样，也不再使用属于自己的存放值的容器。当我们读取**$args**时，Nginx会执行一小段代码，从Nginx核心中专门存放当前URL参数串的位置去读取数据；而当我们改写**$args**时，Nginx会执行另一小段代码，对相同位置进行改写。Nginx的其他部分在需要当前URL参数串的时候，都会从那个位置去读数据，所以我们对**$args**的修改会影响到所有部分的功能。我们来看一个例子：

```nginx
location /test{
    set $orig_a  $arg_a;
    set $args  "a=5";
    echo "original a: $orig_a";
    echo "a: $arg_a";
}
```

