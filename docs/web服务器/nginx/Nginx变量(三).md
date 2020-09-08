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

这里我们先把内建变量$arg_a的值，即原始请求的URL参数a的值，保存在用户变量$orig_a中，然后通过对内建变量**$args**进行赋值，把当前请求的参数串改写成a=5，最后再用**echo**指令分别输出$orgi_a和$arg_a变量的值。因为对内建变量**$args**的修改会直接导致当前请求的URL参数串发生变化，因为内建变量**$arg_XXX**自然会随之变化。测试的结果证实了这一点：

```shell
curl 'http://localhost:8080/test?a=3'
original a:3
a: 5
```

我们看到，因为原始请求的URL参数串是a=3，所以$arg_a最初的值是3，但随后通过改写**$args**变量，将URL参数串又强行修改为a=5，所以最终$arg_a的值又自动变为了5。

我们再来看一个通过修改$args变量影响标准的HTTP代理模块**ngx_proxy**的例子：

```nginx
server{
    listen 8080;
    
    location /test{
        set $args "foo=1&bar=2";
        proxy_pass http://127.0.0.1:8081/args;
    }
}

server{
    listen 8081;
    
    location /args{
        echo "args: $args";
    }
}
```

这里我们在http配置块中定义了两个虚拟主机。第一个虚拟主机监听8080端口，其/test接口自己通过改写$args变量，将当前请求的URL参数串无条件的修改为foo=1&bar=2.然后/test接口再通过**ngx_proxy**模块的**proxy_pass**指令配置了一个反向代理，指向本机的8081端口上的HTTP服务/args.默认情况下，**ngx_proxy**模块在转发HTTP请求到远方HTTP服务的时候，会自动把当前请求的URL参数串也转发到远方。

而本机的8081端口上的HTTP服务正是由我们定义的第二个虚机主机来提供的。我们在第二个虚拟主机的location /args中利用echo 指令输出当前请求的URL参数串，以检查/test接口通过**ngx_proxy**模块实际转发过来的URL请求参数串。

我们来实际访问一下第一个虚拟主机的/test接口：

```shell
$ curl 'http://localhost:8080/test?blah=7'
args: foo=1&bar=2
```

我们看到，虽然请求自己提供了URL参数串blah=7，但在location /test中，参数串被强行改写成了foo=1&bar=2，接着经由**proxy_pass**指令将我们被改写掉的参数串转发给了第二个虚拟主机上配置的/args接口，然后在把/args接口的URL参数串输出。事实证明，我们对**$args**变量的赋值操作，也成功影响到了**ngx_proxy**模块的行为。

