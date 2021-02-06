### 1、系统要求

* Java8 & 兼容java14
* Maven3.3+
* idea 2020.1.3

#### 1

#### .1、maven设置

```xml
# 更换阿里云镜像
<mirrors>
    <mirror>
        <id>nexus-aliyun</id>
        <mirrorOf>central</mirrorOf>
        <name>Nexus aliyun</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
    </mirror>
</mirrors>

# 设置maven编译java版本
<profiles>
    <profile>
        <id>jdk-1.8</id>
        <activation>
            <activeByDefault>true</activeByDefault>
            <jdk>1.8</jdk>
        </activation>
        <properties>
            <maven.compiler.source>1.8</maven.compiler.source>
            <maven.compiler.target>1.8</maven.compiler.target>
            <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
        </properties>
    </profile>
</profiles>
```



### 2、HelloWorld

需求：浏览器发送/hello请求，响应hello，SpringBoot2

#### 2.1、创建maven工程

#### 2.2、引入依赖

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.4.RELEASE</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```



#### 2.3、创建主程序

```java
/**
 * 主程序类
 * @SpringBootApplication：这是一个SpringBoot应用
 */
@SpringBootApplication
public class MainApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class,args);
    }
}
```



#### 2.4、编写业务

```java
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String handle01(){
        return "Hello, Spring Boot 2!";
    }
}
```



#### 2.5、测试

直接运行main方法测试

#### 2.6、简化配置

application.properties

```yaml
server.port=8888
```

#### 2.7、简化部署

```xml
 <build>
     <plugins>
         <plugin>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-maven-plugin</artifactId>
         </plugin>
     </plugins>
</build>
```

把项目打成jar包，直接在目标服务器执行即可。



注意点：

* 取消掉cmd的快速编辑模式



