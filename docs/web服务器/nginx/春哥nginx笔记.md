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





