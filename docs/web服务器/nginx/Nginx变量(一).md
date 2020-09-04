### 变量

变量说白了就是存放值的容器

nginx中只有一种数据类型：字符串

set  $a  "hello world"; 

上面的语句，我们使用标准的ngx_rewrite模块的set命令来对变量$a 赋值hello world

变量名前面有一个$，是记法上的要求。

所有的Nginx变量在Nginx配置文件中引用时都必须带上$前缀。这种表示方法和Perl和PHP这些语言是相似的。

虽然这种计法对正统的java和C#程序员不舒服，但它有另一个好处：可以嵌入到字符串常量中以构造新的字符串（这种技术，在perl世界被称为“变量插值“）：

set  $a  hello;

set $b  "$a,  $a";

上面两条指令执行后，$a的值是hello，$b的值是hello, hello

nginx中的配置示例：

```nginx
server{
    listen 8080;
    
    location /test{
        set $foo hello;
        echo "foo: $foo";
    }
}
```

使用curl这个http客户端在命令行上请求这个/test接口，可以得到：

```shell
$  curl 'http://localhost:8080/test'
foo: hello
```

不过所有插件的配置指定都支持"变量插值"，第三方ngx_echo模块不支持直接输出含有$的字符串，也不支持转义，但是可以通过不支持”变量插值“的模块配置指令构造出取值为$的Nginx变量，然后在echo中使用这个变量。比如：

```nginx
geo $dollar{
	default "$";
}

server {
	listen 8080;
    location /test{
        echo "This is a dollar sign: $dollar"; 
    }
}
```

测试结果如下：

```shell
$ curl 'http://localhost:8080/test'
This is a dollar sign: $
```

上面的例子使用了标准模块ngx_geo 提供的配置指令geo来为变量$dollar赋予字符串$，这样，后面需要用到美元符的地方，直接使用$dollar变量即可。

> ngx_geo的最常规用法是根据客户端ip地址对指定的Nginx变量进行赋值。

在"变量插值"的上下文中，还有一种特殊情况，当变量名之后紧跟变量名的构成字符串时（比如后跟字母、数字以及下划线），就需要使用特别的记法来消除歧义。例如：

```nginx
server{
    listen 8080;
    
    location /test{
        set $first "hello ";
        echo "${first}world";
    }
}
```

上述例子中，echo配置指令的参数值中引用变量$first的时候，后面紧跟world这个单词，如果直接写作"$firstworld"，则Nginx"变量插值"引擎会将之识别为应用了变量$firstworld。为了避免这个问题，Nginx的字符串记法支持使用花括号在$之后把变量名围起来，比如例子中${first}。上面例子的输出结果是：

```shell
$ curl 'http://localhost:8080/test'
hello world
```

*set* 指令（以及前面提到的*geo*指令）不仅有赋值的功能，他还有创建变量的副作用。即当作为赋值对象的变量尚不存在时，它会自动创建该变量。如果我们不创建就直接使用它的值，则会报错。例如：

```nginx
server{
    listen 8080;
    location /bad{
        echo $foo;
    }
}
```

此时Nginx服务器会拒绝加载配置：

```shell
[emerg] unkown "foo" variable
```

是的，我们甚至无法启动服务！

究其原因：Nginx变量的创建和赋值操作发生在全然不同的时间阶段。Nginx变量的创建只能发生在Nginx配置加载的时候，或者说Nginx启动的时候；而赋值操作则只会发生请求实际处理的时候。这意味着不创建而直接使用变量会导致启动失败，同时也意味着我们无法在请求处理时动态的创建新的Nginx变量。

Nginx变量一旦创建，其变量名的可见范围就是整个Nginx配置，甚至可以跨越不同虚拟主机的server配置块。看下面的例子：

```nginx
server{
    listen 8080;
    
    location /foo{
        echo "foo = [$foo]";
    }
    
    location /bar{
        set $foo 32;
        echo "foo = [$foo]";
    }
}
```

这里我们在location /bar 中用/set指令创建了$foo，于是在整个配置文件中这个变量是可见的，因为我们在location /foo中直接引用这个变量而不用担心Nginx会报错。

但是，观察下面使用curl工具访问这两个接口的结果：

```shell
$ curl 'http://localhost:8080/foo'
foo = []

$ curl 'http://localhost:8080/bar'
foo = 32

$ curl 'http://localhost:8080/foo'
foo = []
```

从上面的例子可以看到，set指令因为是在location /bar中使用的，所以赋值操作只会在访问/bar的请求中执行。而请求/foo接口时，我们总是得到空的$foo值，因为用户变量未赋值就输出的话，得到的是空字符串。

从这个例子也可以看到Nginx的另一个重要特性：Nginx变量名的可见范围虽然是整个配置，但每个请求都有所有变量的独立副本，或者说都有各变量用来存放值的容器的独立副本，彼此互不干扰。比如前面我们请求了/bar接口后，$foo变量被赋予了值32，但它丝毫不会影响后续对/foo接口的请求所对应的$foo值（它仍然是空的！），因为各个请求都有自己独立的$foo变量的副本。

这也是Ngnix新手最常见的错误之一：就是将Nginx变量理解成某种在请求之间全局共享的东西，或者“全局变量”。而事实上，Nginx变量的生命期是不可能跨越请求边界的。
