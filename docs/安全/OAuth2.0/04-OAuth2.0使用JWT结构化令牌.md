### 1、什么是JWT

1. jwt是什么

   > ​	JSON Web Token（JWT）是一个开放标准（RFC 7519），它定义了一种紧凑的、自包含的方式，用于作为JSON对象在各方之间的安全传输信息

   简单来说，jwt就是一种结构化封装的方式来生成token的技术。结构化后的token可以被赋予非常丰富的含义，这也是它与原先毫无意义的、随机的字符串形式token的最大区别。

   结构化之后，令牌本身可以被“塞进”一些有用的信息，比如授权的信息、授权的范围信息等。

2. jwt的特点

   JWT结构体可分为HEADER（头部）、PAYLOAD（数据体）和SIGNTURE（签名）三部分。

   经过签名之后的JWT整体结构，是被**句点符号**分割的三段内容，结构为：header.payload.signature，如下：

   > 不带权限
   
   ```json
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJwb21laSIsInNjb3BlIjpbInJlYWQiXSwiZXhwIjoxNjAzNzcwOTUxLCJhY2NvdW50U3lzdGVtS2V5IjoiY29vayIsImp0aSI6ImU3MWNlZDVlLWU4NGQtNGI5NS04YzM2LThhMmU1NTU2NzE2YSIsImNsaWVudF9pZCI6InNzby1nYXRld2F5IiwidXNlcm5hbWUiOiJwb21laSJ9.RjZqdtv4-z8HMwnnBhzlxOI910oCs2OG3ZJlLaX1yXI
   ```
   
   > 带权限
   
```json
   eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiI0Nzc0OTk5MjYzNjQwMzEzODcwIiwic2NvcGUiOlsicmVhZCJdLCJleHAiOjE2MDY1NTI1MTAsImFjY291bnRTeXN0ZW1LZXkiOiJkZWZhdCIsImF1dGhvcml0aWVzIjpbImFuYWx5c2lzdG9vbF9hMV9hbmFseXNpc3Rvb2xfYWRtaW4iLCJkbXBfbW9kZWxfbWFuYWdlciIsImZpZWxkbW9kZWx0b29sX2FkbWluIiwiYW5hbHlzaXN0b29sX2FkbWluIiwiZG1wX2ZfZG9tYWluX2V4cGVydCIsImRtcF9mX21vZGVsX21hbmFnZXIiLCJkbXBfZG9tYWluX2V4cGVydCIsImZpZWxkbW9kZWx0b29sX2FkbWluZnVuIl0sImp0aSI6ImJkZWY1NGYyLTMzZmEtNDhkNS1hZDYzLWY0ZTBhMGE3YTAxYSIsImNsaWVudF9pZCI6InNzby1nYXRld2F5IiwidXNlcm5hbWUiOiJzaGluZG8ifQ.FBymvJxuLkuv2_uysOvTgSAbQx2KP48ceHAG6l2oKfQ
```




   > JWT令牌需要在**公网上传输**，所以在传输过程中，JWT令牌需要进行**Base64编码**以防止乱码，同时还需要进行签名及加密处理来防止数据信息泄露

   该字符串直接看没有意义，可以放到https://jwt.io/  网站的在线校验工具中，就可以看到解码后的数据：

   ![image-20200928145345840](04-OAuth2.0使用JWT结构化令牌.assets/image-20200928145345840.png)

   

   * **HEADER**  表示装载令牌类型和算法等信息，是JWT的头部。其中，type表示第二部分PAYLOAD是JWT类型，alg表示使用HS256对称签名的算法。
   
   * **PAYLOAD**  表示JWT的数据体，代表了一组数据。其中，sub（令牌的主题，一般设为资源拥有者的唯一标识）、exp（令牌的过期时间戳）、iat（令牌颁发的时间戳）是JWT规范性的生命，代表的常规性操作。不过，PAYLOAD表示的一组数据**允许我们自定义声明**。
   
   * **SIGNATURE**  表示对JWT信息的签名。有了HEADER和PAYLOAD两部分内容后，就可以让令牌携带信息了，似乎就可以在网络中传输了，但是这样的信息体是不安全的，相当于裸奔。所以，还需要对其进行加密签名处理，而SIGNATURE就是对信息的签名结果，当受保护资源接收到第三方软件的签名后需要验证令牌的签名是否合法。
   
     ```java
     /*
     ```
  * 1、先用Base64对 header.payload 编码
     * 2、再用加密算法加密上一步的结果，加密的时候需要加盐
    */
     String encodingString = base64UrlEncode(header) +"." + base64UrlEncode(payload0);
 String signature = HMACSHA256(encodingString, "secret");
     ```
     
     ```

   3. 为什么要使用JWT令牌
   
      1） **JWT的核心思想，就是用计算代替存储，有些“时间换空间”的味道。** 当然，这种经过计算并结构化封装的方式，也减少了“共享数据库”因远程调用带来的网络传输消耗，所以也是节省时间的。
   
      2）加密。即SIGNATURE签名，也可以是对称加密，也可以是非对称加密
   
      3）使用JWT格式的令牌，有助于增强系统的可用性和可伸缩性。这种 JWT 格式的令牌，通过“自编码”的方式包含了身份验证需要的信息，不再需要服务端进行额外的存储，所以每次的请求都是无状态会话。这就符合了我们尽可能遵循无状态架构设计的原则，也就是增强了系统的可用性和伸缩性。
   
   4. 缺点：覆水难收

### 2、Spring Security 使用SHA对JWT信息签名

1）授权服务器 在OAuth2Configuration中定义jwt生成策略：

```java
@Configuration
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
    //token发放等其他逻辑
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(customTokenEnhancer(), jwtAccessTokenConverter()));

        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .userDetailsService(myUserDetailService)
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    //使用SHA256
    @Bean
    protected JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("123");
        return jwtAccessTokenConverter;
    }
}

```

除了常规的内容，也可以增强token 内容：

```java
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();
        // 注意添加的额外信息，最好不要和已有的json对象中的key重名，容易出现错误
        MyUserEntity userByUid = feignUserService.getUserByUid(user.getUsername());
        additionalInfo.put("username",userByUid.getLoginName());
        additionalInfo.put("accountSystemKey",userByUid.getAccountSystemKey());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
       return accessToken;
    }
}
```

2）资源服务器验证 JWT 签名合法性

```java

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/druid/**", "/public/**").permitAll()
                .antMatchers("/**").authenticated()
                .anyRequest().authenticated().and().csrf().disable();
    }
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("user").tokenStore(tokenStore());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

}
```

3）JWT   token校验

资源服务器在接收到第三方请求时，会校验请求头上携带的JWT  token ，如果是对称加密，那么SHA256的key需要一致，不然会报invaild  token异常



### 3、Spring Security 使用RSA对JWT信息签名

jks证书生成步骤：

> https://stackoverflow.com/questions/62055008/spring-boot-jwt-jks-file-and-public-key

* 电脑安装对应版本的openssl

  > https://oomake.com/download/openssl

* 把openssl的bin目录设置为环境变量

  ![image-20200928231618993](04-OAuth2.0使用JWT结构化令牌.assets/image-20200928231618993.png)

* 最简方式生成jks文件及其公钥

  在cmd窗口输入以下命令：

  keytool -genkeypair -alias authentication-server -keyalg RSA -dname "CN=GC,OU=DC,O=BatmanCorp,L=GT,C=Gotham" -keystore jwt.jks -keypass Utcook -storepass Utcook

  执行命令后，会在c盘个人目录下生成对应的 jwt.jks 文件

  ![image-20200928231859526](04-OAuth2.0使用JWT结构化令牌.assets/image-20200928231859526.png)

  再执行以下命令，在cmd窗口显示公钥信息：

  keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey -noout

  ![image-20200928232024259](04-OAuth2.0使用JWT结构化令牌.assets/image-20200928232024259.png)

  将公钥内容单独拷贝出来，使用txt格式文本存放。

  至此，jks和对应的公钥文件已生成完毕，可以放到对应项目的资源目录下使用。<详情见下方工程使用>

* 方法二：

  1首先，我像这样创建JKS文件： 

  keytool -genkeypair -alias authentication-server -keyalg RSA -dname "CN=GC,OU=DC,O=BatmanCorp,L=GT,C=Gotham" -keystore auth-server.jks -keypass batman20; -storepass bruce20; -validity 180 -keysize 2048

  2创建P12文件 

  keytool -importkeystore -srckeystore auth-server.jks -srcstorepass bruce20; -srckeypass batman20; -srcalias authentication-server -destalias authentication-server -destkeystore auth-server.p12 -deststoretype PKCS12 -deststorepass batman_20; -destkeypass batman_20;

  3创建PEM文件 

  openssl pkcs12 -in auth-server.p12 -nodes -nocerts -out auth-server-private-key.pem

  4创建Pub文件

  openssl rsa -in auth-server-private-key.pem -pubout -out PublicKey.pub

1）授权服务器使用RSA私钥加密的JWT

在授权服务上添加jks私钥

![image-20200929082208643](04-OAuth2.0使用JWT结构化令牌.assets/image-20200929082208643.png)

JWT加密方法：

```java
    @Bean
    protected JwtAccessTokenConverter jwtAccessTokenConverter() {
        /*
        //使用SHA256加密JWT
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("123");
        return jwtAccessTokenConverter;*/

        //使用RSA私钥加密JWT
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "Utcook".toCharArray());
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("authentication-server"));
        return jwtAccessTokenConverter;
    }
```

2）资源服务器使用RSA公钥解密JWT

在资源服务器上添加私钥

![image-20200929082148742](04-OAuth2.0使用JWT结构化令牌.assets/image-20200929082148742.png)

使用RSA解密JWT

```java
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        /*
        //使用SHA256解密JWT
        CustomJwtAccessTokenConverter converter = new CustomJwtAccessTokenConverter();
        converter.setSigningKey("123");
        return converter;*/

        //使用RSA公钥解密JWT
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        Resource resource = new ClassPathResource("public.txt");
        String publicKey = null;
        try {
            publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }
```





### 本地解析jwt token

查看源码：JwtAccessTokenConverter.class  L158   --->   JwtHelper.decode（String token）方法可以解析token

> 源码支持来自于：spring-security-jwt:1.0.9.RELEASE

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiI0Nzc0OTk5MjYzNjQwMzEzODcwIiwic2NvcGUiOlsicmVhZCJdLCJleHAiOjE2MDcwNzI0NjcsImFjY291bnRTeXN0ZW1LZXkiOiJkZWZhdCIsImF1dGhvcml0aWVzIjpbImFwcGF1dGhfYWRtaW4iLCJkZXZlbG9wZXJfZGV2ZWxvcGVyX21pZGRsZV9hZG1pbiIsImRldmVsb3Blcl9kZXZlbG9wZXJfbWlkZGxlX2ZpZWxkX21vZGVsIiwiYW5hbHlzaXN0b29sX2ExX2FuYWx5c2lzdG9vbF9hZG1pbiIsImRldmVsb3Blcl9kZXZlbG9wZXJfbWlkZGxlX3Byb2Nlc3NfZ3VpZGFuY2UiLCJkbXBfbW9kZWxfbWFuYWdlciIsImFwcGF1dGhfYXBwX2RldmVsb3BlciIsImRtcF9mX21vZGVsX21hbmFnZXIiLCJhcHBhdXRoX2luaXRfYXBwIiwiZmllbGRtb2RlbHRvb2xfYXBwYXV0aF91cGRhdGVfZnVuYyIsImFwcGF1dGhfdXBkYXRlX2Z1bmMiLCJmaWVsZG1vZGVsdG9vbF9hZG1pbiIsImFuYWx5c2lzdG9vbF9hZG1pbiIsImRtcF9mX2RvbWFpbl9leHBlcnQiLCJkbXBfZG9tYWluX2V4cGVydCIsImRldmVsb3Blcl9kZXZlbG9wZXJfbWlkZGxlX2J1c2luZXNzX2FuYWx5c2lzIiwiZmllbGRtb2RlbHRvb2xfYWRtaW5mdW4iXSwianRpIjoiZWJmNzBjMzYtYmI0Zi00ZTNjLWI5ZjYtZmM2NTBkMjk0MzdkIiwiY2xpZW50X2lkIjoic3NvLWdhdGV3YXkiLCJ1c2VybmFtZSI6InNoaW5kbyJ9.b_q0RkgvOvrGUayZZu7GAraf_ET6YLtFYLs2rvLNV5M";

        Map<String, String> header = new HashMap<>();
        Map<String, String> headers = JwtHelper.headers(token);
        headers.entrySet().stream().forEach(entry -> {
            header.put(entry.getKey(), entry.getValue());
        });
        Map<String, Object> result = new HashMap<>();
        result.put("PAYLOAD", JSONObject.parse(JwtHelper.decode(token).getClaims()));
        result.put("HEADER", header);
        System.out.println(JSONObject.toJSONString(result));
```

