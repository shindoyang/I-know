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

其效果和使用 echo_exec 是完全相同的。后面我们还会专门介绍这个**rewrite**指令的更多用法，比如发起301和302这样的“外部跳转”。

从上面这个例子我们看到，Nginx变量值容器的生命期是与当前正在处理的请求绑定的，而与location无关。

前面我们接触到的都是通过**set**指令隐式创建的Nginx变量。这些变量我们一般称为“用户自定义变量”，或者更简单一些，“用户变量”。既然有“用户自定义变量”，自然也就有由Nginx核心和各个Nginx模块提供的“预定义变量”，或者说“内建变量”（builtin variables）。

Nginx内建变量最常见的用途就是获取关于请求或相应的各种信息。例如由**ngx_http_core**模块提供的内建变量**$uri**，可以用来获取当前请求的URI（经过解码，并且不含请求参数），而**$request_uri**则用来获取请求最原始的URI（未经解码，并且包含请求参数）。请看下面这个例子：

```nginx
location /test{
    echo "uri = $uri";
    echo "request_uri = $request_uri";
}
```

这里为了简单起见，连server配置快也省略了，和前面所有示例一样，我们监听的依然是8080端口。在这个例子里，我们把$uri和$request_uri的值输出到响应体中去。下面我们用不同的请求来测试一下这个/test接口：

```shell
$ curl 'http://localhost:8080/test'
uri = /test
request_uri = /test

$ curl 'http://localhost:8080/test?a=3&b=4'
uri = /test
request_uri = /test?a=3&b=4

$ curl 'http://localhost:8080/test/hello%20world?a=3&b=4'
uri = /test/hello world
request_uri = /test/hello%20world?a=3&b=4
```

另一个特别常用的内建变量其实并不是一个单独一个变量，而是有无限多变种的一群变量，即名字以**arg_**开头的所有变量，我们姑且称之为**$arg_XXX**变量群。一个例子是**$arg_name**，这个变量的值是当前请求名为name的URI参数的值，而且还是未解码的原始形式的值。我们来看一个比较完整的示例：

```nginx
location /test{
    echo "name: $arg_name";
    echo "class: $arg_class";
}
```

然后在命令行上使用各种参数组合去请求这个/test接口：

```shell
$ curl 'http://localhost:8080/test'
name:
class:

$ curl 'http://localhost:8080/test?name=Tom&class=3'
name: Tom
class: 3

$ curl 'http://localhost:8080/test?name=hello%20world&class=9'
name: hello20%world
class: 9
```

其实**$arg_name**不仅可以匹配name参数，也可以匹配NAME参数，抑或是Name，等等：

```shell
$ curl 'http://localhost:8080/test?NAME=Marry'
name: Marry
class: 

$ curl 'http://localhost:8080/test?Name=Jimmy'
name: Jimmy
class:
```

Nginx会在匹配参数名之前，自动把原始请求中的参数名调整为全部小写的形式。

如果你想对URI参数值中的%XX这样的编码序列进行解码，可以使用第三方**ngx_set_misc**模块提供的**set_unescape_uri**配置指令：

```nginx
location /test{
    set_unescape_uri $name $arg_name;
    set_unescape_uri $class $arg_class;
    
    echo "name: $name";
    echo "class: $class";
}
```

现在我们再看一下效果：

```shell
$ curl 'http://localhost:8080/test?name=hello%20world&class=9'
name: hello world
class： 9
```

空格果然被解码出来了！

从这个例子我们同时可以看到，这个**set_unescape_uri**指令也像**set**指令那样，拥有自动创建Nginx变量的功能。后面我们还会专门介绍到**ngx_set_misc**模块。

像**$arg_XXX**这种类型的变量拥有无穷无尽可能的名字，所以它们并不对应任何存放值的容器。而且这种变量在Nginx核心中是经过特别处理的，第三方Nginx模块是不能提供这样充满魔法的内建变量的。

类似**$arg_XXX**的内建变量还有不少，比如用来取cookie值的**$cookie_XXX**变量群，用来取请求头的**$http_XXX**变量群，以及用来取响应头的**$sent_http_XXX**变量群。这里就不一一介绍了，感兴趣的读者可以参考ngx_http_core模块的官方文档：http://nginx.org/en/docs/http/ngx_http_core_module.html

需要指出的是，许多内建变量都是只读的，比如我们刚才介绍的**$uri**和**$request_uri**，对只读变量进行赋值是应当绝对避免的，因为会有意想不到的后果，比如：

```nginx
? location /bad{
? 		set $uri /blah;
?       echo $uri;
? }
```

这个有问题的配置会让Nginx在启动的时候报出一条令人匪夷所思的错误：

```shell
[emerg] the duplicate "uri" variable in ...
```

如果你尝试改写另外一些只读的内建变量，比如**$arg_XXX**变量，在某些Nginx版本中设置可能导致进程崩溃。













