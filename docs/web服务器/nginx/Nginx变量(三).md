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

在读取变量时执行的这段特殊代码，在Nginx中被称为"取处理程序"（get handler）；而改写变量时执行的这段特殊代码，则被称为”存处理程序“（set handler）。不同的Nginx模块一般会为他们的变量准备不同的”存取处理程序“，从而让这些变量的行为充满魔法。

其实这种技巧在计算机世界并不鲜见。比如在面试对象编程中，类的设计者一般不会把类的成员变量直接暴露给类的用户，而是另行提供两个方法（method），分别用于该成员变量的读操作和写操作，这两个方法常常被称为“存取器”（accessor）。下面是C++语言中的一个例子：

```c++
#include <string>
using namespace std;
class Person{
    public:
    	const string get_name(){
            return m_name;
        }
    	void set_name(const string name){
            m_name = name;
        }
    private:
    	string m_name;
};
```

在这个名叫Person的C++类中，我们提供了get_name和set_name这两个方法，以作为私有成员变量m_name的”存取器“。

这样设计的好处是显而易见的。类的设计者可以在“存取器”中执行任意代码，以实现所需的业务逻辑以及“副作用”，比如自动更新与当前成员变量存在依赖关系的其他成员变量，抑或是直接修改某个与当前对象相关联的数据库表中的对应字段。而对于后一种情况，也许“存取器”所对应的成员变量压根就不存在，或者及时存在，也顶多扮演这数据缓存的角色，以缓解被代理数据库的访问压力。

与面向对象编程中的“存取器”概念想对应，Nginx变量也是支持绑定“存取处理程序”的，Nginx模块在创建变量时，可以选择是否为变量分配存放值的容器，以及是否自已提供与读写操作相对应的“存取处理程序”。

不是所有的Nginx变量都拥有存放值的容器。拥有值容器的变量在Nginx核心中被称为“被索引的”（indexed）;反之，则被称为“未索引的”（non-indexed）。

我们前面在**变量(二)**中已经知道，像**$arg_XXX**这样具有无数变种的变量群，是“未索引的”。当读取这样的变量时，其实是它的“取处理程序”在起作用，即实时扫描当前请求的URL参数串，提取出变量名所指定的URL参数的值。很多新手都会对**$arg_XXX**的实现方法产生误解，以为Nginx会实现解析好当前请求的所有URL参数，并且把相关的**$arg_XXX**变量的值都事先设置好。然后事实并非如此，Nginx根本不会事先解析好URL参数串，而是在用户读取某个**$arg_XXX**变量时，调用其“取处理程序”，即时去扫描URL参数串。类似地，内建变量**$cookie_XXX**也是通过它的“取处理程序”，即时去扫描Cookie请求头中的相关定义的。









