### Springboot项目启动时自动执行sql脚本文件

需要在application.properties文件中加上以下配置：

```properties
#需要加上这句，否则不会自动执行sql文件
spring.datasource.initialization-mode=always
# schema.sql中一般存放的是建表语句DDL
spring.datasource.schema = classpath:schema.sql
# data.sql中一般存放的是需要插入更新等sql语句DML
spring.datasource.data =  classpath:data.sql
```

或者yml配置中加上如下配置：

```yaml
  datasource:
    # 开启自动建表功能
    initialization-mode: always
    schema: classpath:schema.sql
```

同时在主工程的resources文件夹下需要准备好schema.sql和data.sql两个文件。

做好上面的准备工作即可完成。

![image-20201216091754839](Untitled.assets/image-20201216091754839.png)

