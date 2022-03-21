# springboot在过滤器Filter中 重新写入request的请求参数

> 原文链接：https://blog.csdn.net/FLL430/article/details/108147343

### 需求说明

1. 想要在Filter过滤器中修改一下request的请求参数。新增修改删除等
2. 但是我们的httpservletrequest是不支持这样操作的，如果想要同一个request请求做数据传递的操作，只能使用setAttribute和getAttribute的方式
3. 但是我现在的需求必须修改request里的请求参数

### 解决思路(这部分可以跳过，直接查看解决方式)

1. 使用idea工具debug项目查看了一下过滤器中接受到的ServletRequest接口具体使用的是哪个实现类
2. 发现用的是package org.apache.catalina.connector.Request.查看里面的源码

  ![在这里插入图片描述](.\images\1.png)

 ![img](.\images\2.png)

找到reqeust为我们提供的getParamterMap的方法，因此只要我们能够修改这个parameterMap里的参数内容就可以了

**实现方式**

1. 新建自定义过滤器ParamValidateFilter，并重写doFilter方法

```java
/**
 *  自定义过滤器，
 * Created by fll430
 */
public class ParamValidateFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ParamValidateFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /**
        通过上面查看的源码，我知道ParameterMap<String, String[]>就是我们的请求参数，
        因此在这里对它进行操作就能达到我们的目的了
        */
        ParameterMap<String, String[]> parameterMap = (ParameterMap<String, String[]>) servletRequest.getParameterMap();
        //由于ParameterMap在实例化后会被锁定，所以我们要先调用一下它的解锁方法。
        parameterMap.setLocked(false);
        //然后就可以对它进行增删改查了
        parameterMap.put("first",new String[]{"first"});
        parameterMap.put("end",new String[]{"end"});
        //操作完之后给它锁定
        parameterMap.setLocked(true);
        log.info("参数转换完毕");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
```

剩下的就是让过滤器生效了。f
springboot中可以使用@WebFilter(urlPatterns="/*",filterName=“filter”)注解
然后在启动类上加上@ServletComponentScan让他生效

如果你和我一样用的是shiro安全框架。
那么可以在shiroConfig中的shiroFilterFactoryBean方法中加上我们的过滤器

```java
//<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
filterChainDefinitionMap.put("/**", "authc,paramFilter");
shiroFilterFactoryBean.getFilters().put("paramFilter",new ParamValidateFilter());

```

