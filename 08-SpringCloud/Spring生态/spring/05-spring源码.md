**BeanFactory的类注释**

```java
/**
 * The root interface for accessing a Spring bean container.
 * This is the basic client view of a bean container;
 * further interfaces such as {@link ListableBeanFactory} and
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * are available for specific purposes.
 *
 * <p>This interface is implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. Depending on the bean definition,
 * the factory will return either an independent instance of a contained object
 * (the Prototype design pattern), or a single shared instance (a superior
 * alternative to the Singleton design pattern, in which the instance is a
 * singleton in the scope of the factory). Which type of instance will be returned
 * depends on the bean factory configuration: the API is the same. Since Spring
 * 2.0, further scopes are available depending on the concrete application
 * context (e.g. "request" and "session" scopes in a web environment).
 *
 * <p>The point of this approach is that the BeanFactory is a central registry
 * of application components, and centralizes configuration of application
 * components (no more do individual objects need to read properties files,
 * for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
 * Development" for a discussion of the benefits of this approach.
 *
 * <p>Note that it is generally better to rely on Dependency Injection
 * ("push" configuration) to configure application objects through setters
 * or constructors, rather than use any form of "pull" configuration like a
 * BeanFactory lookup. Spring's Dependency Injection functionality is
 * implemented using this BeanFactory interface and its subinterfaces.
 *
 * <p>Normally a BeanFactory will load bean definitions stored in a configuration
 * source (such as an XML document), and use the {@code org.springframework.beans}
 * package to configure the beans. However, an implementation could simply return
 * Java objects it creates as necessary directly in Java code. There are no
 * constraints on how the definitions could be stored: LDAP, RDBMS, XML,
 * properties file, etc. Implementations are encouraged to support references
 * amongst beans (Dependency Injection).
 *
 * <p>In contrast to the methods in {@link ListableBeanFactory}, all of the
 * operations in this interface will also check parent factories if this is a
 * {@link HierarchicalBeanFactory}. If a bean is not found in this factory instance,
 * the immediate parent factory will be asked. Beans in this factory instance
 * are supposed to override beans of the same name in any parent factory.
 *
 * <p>Bean factory implementations should support the standard bean lifecycle interfaces
 * as far as possible. The full set of initialization methods and their standard order is:
 * <ol>
 * <li>BeanNameAware's {@code setBeanName}
 * <li>BeanClassLoaderAware's {@code setBeanClassLoader}
 * <li>BeanFactoryAware's {@code setBeanFactory}
 * <li>EnvironmentAware's {@code setEnvironment}
 * <li>EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}
 * <li>ResourceLoaderAware's {@code setResourceLoader}
 * (only applicable when running in an application context)
 * <li>ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
 * (only applicable when running in an application context)
 * <li>MessageSourceAware's {@code setMessageSource}
 * (only applicable when running in an application context)
 * <li>ApplicationContextAware's {@code setApplicationContext}
 * (only applicable when running in an application context)
 * <li>ServletContextAware's {@code setServletContext}
 * (only applicable when running in a web application context)
 * <li>{@code postProcessBeforeInitialization} methods of BeanPostProcessors
 * <li>InitializingBean's {@code afterPropertiesSet}
 * <li>a custom init-method definition
 * <li>{@code postProcessAfterInitialization} methods of BeanPostProcessors
 * </ol>
 *
 * <p>On shutdown of a bean factory, the following lifecycle methods apply:
 * <ol>
 * <li>{@code postProcessBeforeDestruction} methods of DestructionAwareBeanPostProcessors
 * <li>DisposableBean's {@code destroy}
 * <li>a custom destroy-method definition
 * </ol>
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 13 April 2001
 * @see BeanNameAware#setBeanName
 * @see BeanClassLoaderAware#setBeanClassLoader
 * @see BeanFactoryAware#setBeanFactory
 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware#setMessageSource
 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
 * @see org.springframework.web.context.ServletContextAware#setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization
 * @see InitializingBean#afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization
 * @see DisposableBean#destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 */
```

中文翻译：

```java

org.springframework.beans.factory public interface BeanFactory
根接口，用于访问一个Spring bean容器。 这是一个bean容器的基本客户端视图; 其它接口如ListableBeanFactory和org.springframework.beans.factory.config.ConfigurableBeanFactory可用于特定目的。
该接口由持有一些bean定义，每一个字符串名称唯一标识对象来实现。 取决于bean定义，工厂将返回一个包含的对象（原型设计图案），或者一个单个共享的实例（一个更好的选择Singleton设计模式，其中，所述实例是在范围单身的任一个独立的实例工厂）。 哪个实例的类型将返回取决于bean工厂配置：API是一样的。 由于弹簧2.0，进一步范围根据具体的应用上下文（例如，“请求”和在网络环境中“会话”范围）是可用的。
这种方法的一点是，BeanFactory的是应用程序组件的中央登记，并集中应用组件的配置（没有更多的做单独的对象需要读取属性文件，例如）。 见第4和第11个“专家单对单的J2EE设计与开发”为这种做法的好处的讨论。
需要注意的是，通常最好依赖于依赖注入（“推”的配置）来配置应用对象通过setter方法或构造函数，而不是使用任何形式的像一个BeanFactory查找“拉”配置的是。 Spring的依赖注入功能使用这个BeanFactory接口和子接口来实现。
通常一个BeanFactory将加载存储在配置源（例如，XML文档）bean定义，并使用org.springframework.beans包来配置豆。 然而，一个实现可以简单地返回它直接在Java代码中创建必要的Java对象。 对于如何没有约束的定义可以存储：LDAP，RDBMS，XML，属性文件等实现鼓励支持引用豆之间（依赖注入）。
与此相反的方法ListableBeanFactory ，都在这个界面的操作也将检查父工厂如果这是一个HierarchicalBeanFactory 。 如果在此工厂实例没有找到一个bean，直接父工厂将被要求。 在此工厂实例豆类都应该覆盖同名豆任何父工厂。
bean工厂实现应该支持标准的bean的生命周期接口尽可能。 全套的初始化方法以及它们的标准顺序是：
BeanNameAware的setBeanName
BeanClassLoaderAware的setBeanClassLoader
实现BeanFactoryAware的setBeanFactory
EnvironmentAware的setEnvironment
EmbeddedValueResolverAware的setEmbeddedValueResolver
ResourceLoaderAware的setResourceLoader （仅适用于应用程序上下文中运行时）
ApplicationEventPublisherAware的setApplicationEventPublisher （仅适用于应用程序上下文中运行时）
MessageSourceAware的setMessageSource （仅适用于应用程序上下文中运行时）
了ApplicationContextAware的setApplicationContext （仅适用于应用程序上下文中运行时）
ServletContextAware的setServletContext （仅适用于Web应用程序上下文中运行时）
postProcessBeforeInitialization BeanPostProcessor的方法
的InitializingBean的afterPropertiesSet
自定义的初始化方法定义
postProcessAfterInitialization BeanPostProcessor的方法
在一个bean工厂的关闭，下列生命周期方法适用于：
postProcessBeforeDestruction DestructionAwareBeanPostProcessors的方法
DisposableBean的的destroy
一个自定义的破坏法的定义
以来：
2001年4月13日
也可以看看：
BeanNameAware.setBeanName ， BeanClassLoaderAware.setBeanClassLoader ， BeanFactoryAware.setBeanFactory ， org.springframework.context.ResourceLoaderAware.setResourceLoader ， org.springframework.context.ApplicationEventPublisherAware.setApplicationEventPublisher ， org.springframework.context.MessageSourceAware.setMessageSource ， org.springframework.context.ApplicationContextAware.setApplicationContext ，org.springframework.web.context.ServletContextAware.setServletContext， org.springframework.beans.factory.config.BeanPostProcessor.postProcessBeforeInitialization ， InitializingBean.afterPropertiesSet ， org.springframework.beans.factory.support.RootBeanDefinition.getInitMethodName ， org.springframework.beans.factory.config.BeanPostProcessor.postProcessAfterInitialization ， DisposableBean.destroy ， org.springframework.beans.factory.support.RootBeanDefinition.getDestroyMethodName
  Maven: org.springframework:spring-beans:5.2.3.RELEASE
```

