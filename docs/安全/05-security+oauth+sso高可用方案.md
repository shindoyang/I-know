oauth使用默认的jwt方案，所以认证服务一旦多实例部署，绝对出现session不一致的问题，造成web端单独登陆时无限循环。

为了实现oauth多节点部署，高可用支持，都做了哪些努力：

1、该用redis存储access_token

2、原来的oauth版本太久，会报异常：

```java
java.lang.NoSuchMethodError: org.springframework.data.redis.connection.RedisConnection.set([B[B)V
```

处理方式参考：

> https://blog.csdn.net/fyk844645164/article/details/99422429

简单一句话：升级了oauth到2.3.3版本

3、升级oauth后遇到坑了，client客户端配置，必须要验证回调地址的安全性，所以client配置类又补充了redirectUris。

不升级会报错：

```java
At least one redirect_uri must be registered with the client
```

修改配置：

参考 

>  https://segmentfault.com/q/1010000015995274

```java
 /**
     * 客户端配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
        clients.inMemory()
                .withClient("sso-gateway")
                .secret(passwordEncoder.encode("sso-gateway-secret"))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(60 * 30)
                .refreshTokenValiditySeconds(2592000)//30天 单位:秒
                .scopes("read").autoApprove(true)
                .redirectUris(clientSSORedirectUri);
    }
```



3、client端改用redis存储



4、web端该用redis存储

