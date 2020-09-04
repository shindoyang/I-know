### 变量

nginx中只有一种数据类型：字符串

set  $a  "hello world"; 

变量名前面有一个$，是记法上的要求。

另一个好处：可以嵌入到字符串常量中以构造新的字符串（这种技术，在perl世界被称为“变量插值“）：

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

```c
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

```c
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

```c
$ curl 'http://localhost:8080/test'
hello world
```





