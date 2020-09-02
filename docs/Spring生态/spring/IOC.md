### 注入方式

1.  构造器注入

2.  setter注入

   

### @Qualifier

场景：在Controller中需要注入service，那么如果这个service有多个实现类的时候，如果区分这两个impl？

```java
//接口
public interface IPfProductService{}

//实现类1
@Service("pfProductService1")
public class PfProductService1 implements IPfProductService{} 

//实现类2
@Service("pfProductService2")
public class PfProductService2 implements IPfProductService{} 

//在Controller中使用@Autowired注入时
public class ProductController extends BaseController{
    Logger logger = Logger.getLogger(this.getClass());
    @Autowired
	@Qualifier("pfProductService1")
	IPfProductService iPfProductService;
}

```

Qualifier的意思是合格者，通过这个注解可以让Controller明确自己当前要使用哪个实现类。@Qualifier的参数名称就是之前定义的@Service注解名称之一。

```java
//使用@Resource注解
public class ProductController extends BaseController{
    Logger logger = Logger.getLogger(this.getClass());
    @Resource(name = "pfProductService1")
	IPfProductService iPfProductService;
}
```

使用@Resource注解时相对简单，注解自带的name的val就是@Service注解名称之一。

