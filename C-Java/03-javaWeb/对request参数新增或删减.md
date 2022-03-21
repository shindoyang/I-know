# 对Request.parameter中参数进行添加或修改

> https://www.jianshu.com/p/17256f94fd30

在讲解这个问题之前，先来聊聊我程序的设计，为移动端提供接口的同学们都知道，在接口通讯的过程中，数据是需要加密传输的，博主设计的也不例外，请看下面的内容:

 ![img](E:\I know\C-Java\03-javaWeb\images\1.webp)

 可以看到，`parameter`参数是一段加密串，接下来在看我们的接口定义部分:

 ![img](E:\I know\C-Java\03-javaWeb\images\2.webp)

可以看到，接口需要的参数是一个`User`对象，现在有个问题，怎么将加密的数据解密并将解密出的数据映射到`User`对象中呢?
    其实很简单，我们定义一个`Filter`，过滤请求`app/**`的路径，首先对其进行解密，然后将参数放入`request.parameter`中，但是我们都知道`request.parameter`中的数据只能进行读操作，不能进行写操作，怎么解决呢？
    这里就引入一个类 `javax.servlet.http.HttpServletRequestWrapper`，是一个扩展的通用接口，也就是会对`request`做一次包装，我们继承并重写这个方法。

```java
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * request.parameter
 *
 * @author SanLi
 * Created by 2689170096@qq.com/SanLi on 2018/1/28
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> params = new HashMap<>();

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
        //将参数表，赋予给当前的Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
    }

    /**
     * 重载构造方法
     */

    public ParameterRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
        this(request);
        //这里将扩展参数写入参数表
        addAllParameters(extendParams);
    }

    /**
     * 在获取所有的参数名,必须重写此方法，否则对象中参数值映射不上
     *
     * @return
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector(params.keySet()).elements();
    }

    /**
     * 重写getParameter方法
     *
     * @param name 参数名
     * @return 返回参数值
     */
    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values;
    }

    /**
     * 增加多个参数
     *
     * @param otherParams 增加的多个参数
     */
    public void addAllParameters(Map<String, Object> otherParams) {
        for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 增加参数
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void addParameter(String name, Object value) {
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }
}
```

  大多数情况下我们都是使用`Spring`作为开发框架，博主也不例外，定义过滤器，继承`Spring`的`OncePerRequestFilter`。

```java
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 参数过滤,对所有请求app/接口的请求进行拦截，解密，并将参数放入request.parameter中
 *
 * @author SanLi
 * Created by 2689170096@qq.com/SanLi on 2018/1/28
 */
@Component
public class RequestParameterFilter extends OncePerRequestFilter {
    /**
     * 过滤路径
     */
    static final String AUTH_PATH = "/app/";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*如果请求路径是为app,进行过滤对参数parameter内容解密，放入request.parameter中*/
        if (request.getRequestURI().indexOf(AUTH_PATH) != -1) {
            /*1.获取加密串,进行解密*/

            /*2.解密出加密串，我和前台约定的是JSON,获取到JSON我将其转换为map，这里我直接用手动封装map代替*/
            Map paramter = new HashMap(16);
            paramter.put("username", "admin");
            paramter.put("password", "password");
            ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request, paramter);
            filterChain.doFilter(wrapper, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
```

这样`controller`获取到的就是解密后的参数了，写这篇文章的我还不太了解原理，只是会用，但是用也是一种成长，比如这样用法，这篇博文我根据自己程序解决方式思想引入的，编程思想最重要的，我们必须要好好学习，不断进步。



作者：乐傻驴
链接：https://www.jianshu.com/p/17256f94fd30
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

