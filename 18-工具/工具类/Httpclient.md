### get 请求demo

```java
@Test
public void getParams() {
 
    // 获取连接客户端工具
    CloseableHttpClient httpClient = HttpClients.createDefault();
 
    String entityStr = null;
    CloseableHttpResponse response = null;
 
    try {
        /*
         * 由于GET请求的参数都是拼装在URL地址后方，所以我们要构建一个URL，带参数
         */
        URIBuilder uriBuilder = new URIBuilder("http://www.baidu.com");
        /** 第一种添加参数的形式 */
        /*uriBuilder.addParameter("name", "root");
        uriBuilder.addParameter("password", "123456");*/
        /** 第二种添加参数的形式 */
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("name", "root");
        BasicNameValuePair param2 = new BasicNameValuePair("password", "123456");
        list.add(param1);
        list.add(param2);
        uriBuilder.setParameters(list);
 
        // 根据带参数的URI对象构建GET请求对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
 
        /* 
         * 添加请求头信息
         */
        // 浏览器表示
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        // 传输的类型
        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
 
        // 执行请求
        response = httpClient.execute(httpGet);
        // 获得响应的实体对象
        HttpEntity entity = response.getEntity();
        // 使用Apache提供的工具类进行转换成字符串
        entityStr = EntityUtils.toString(entity, "UTF-8");
    } catch (ClientProtocolException e) {
        System.err.println("Http协议出现问题");
        e.printStackTrace();
    } catch (ParseException e) {
        System.err.println("解析错误");
        e.printStackTrace();
    } catch (URISyntaxException e) {
        System.err.println("URI解析异常");
        e.printStackTrace();
    } catch (IOException e) {
        System.err.println("IO异常");
        e.printStackTrace();
    } finally {
        // 释放连接
        if (null != response) {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                System.err.println("释放连接出错");
                e.printStackTrace();
            }
        }
    }
 
    // 打印响应内容
    System.out.println(entityStr);
 
}
```



### post请求demo

```java
@Test
public void postParams() {
    // 获取连接客户端工具
    CloseableHttpClient httpClient = HttpClients.createDefault();
 
    String entityStr = null;
    CloseableHttpResponse response = null;
 
    try {
 
        // 创建POST请求对象
        HttpPost httpPost = new HttpPost("http://www.baidu.com");
 
        /*
         * 添加请求参数
         */
        // 创建请求参数
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("name", "root");
        BasicNameValuePair param2 = new BasicNameValuePair("password", "123456");
        list.add(param1);
        list.add(param2);
        // 使用URL实体转换工具
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        httpPost.setEntity(entityParam);
 
        /* 
         * 添加请求头信息
         */
        // 浏览器表示
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        // 传输的类型
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
 
        // 执行请求
        response = httpClient.execute(httpPost);
        // 获得响应的实体对象
        HttpEntity entity = response.getEntity();
        // 使用Apache提供的工具类进行转换成字符串
        entityStr = EntityUtils.toString(entity, "UTF-8");
 
        // System.out.println(Arrays.toString(response.getAllHeaders()));
 
    } catch (ClientProtocolException e) {
        System.err.println("Http协议出现问题");
        e.printStackTrace();
    } catch (ParseException e) {
        System.err.println("解析错误");
        e.printStackTrace();
    } catch (IOException e) {
        System.err.println("IO异常");
        e.printStackTrace();
    } finally {
        // 释放连接
        if (null != response) {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                System.err.println("释放连接出错");
                e.printStackTrace();
            }
        }
    }
 
    // 打印响应内容
    System.out.println(entityStr);
}
```



---

### 接口调试demo

## 爱音乐验证码接口调试



> 本接口测试请在vpn环境下调用

### 1、maven依赖

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.7</version>
</dependency>

<!-- 阿里JSON解析器 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.70</version>
</dependency>
```



### 2、MD5 工具类

```java
package com.example.demo.aimusic;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i<byteArray.length; i++) {
            if (Integer.toHexString(0xFF &byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            }else{
                md5StrBuff.append(Integer.toHexString(0xFF &byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }
}

```



### 3、接口测试代码

```java
package com.example.demo.aimusic;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SmsTemplate {
    private static final String TEMPLATEURL = "http://{host}/remoting/portal_api/imusic/sms_ability/send_templatesms";
    private static final String MD5_KEYWORD = "xxoo";

    public static void main(String[] args)throws Exception {


        CloseableHttpClient httpClient = null;
        String entityStr = null;
        CloseableHttpResponse response = null;

        try {

            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            //md5签名生成方式MD5(keyword+timestamp)，keyword由系统分配
            String signature = MD5Util.getMD5Str(new StringBuilder().append(MD5_KEYWORD).append(timestamp).toString());
            System.out.println("signature = " + signature);

            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(TEMPLATEURL);
            httpPost.setHeader("deviceid","10063");
            httpPost.setHeader("timestamp",timestamp);
            httpPost.setHeader("signature",signature);

            /*
             * 模板内容：【爱音乐】统一认证中心验证码：{randcode}，有效时间为{minute}分钟，请勿将验证码告知他人！
             * 参数格式为(json对象字符串): {参数名：参数值，参数名：参数值}
             */
            Map tempPara = new HashMap();
            tempPara.put("randcode","990737");
            tempPara.put("minute","5");

            // 添加请求参数
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair param1 = new BasicNameValuePair("mobile", "134......8470");
            BasicNameValuePair param2 = new BasicNameValuePair("templateId", "1091549134");
            BasicNameValuePair param3 = new BasicNameValuePair("templateParam", JSONObject.toJSONString(tempPara));
            BasicNameValuePair param4 = new BasicNameValuePair("sendSmsOrder", "1");
            list.add(param1);
            list.add(param2);
            list.add(param3);
            list.add(param4);

            // 使用URL实体转换工具
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(entityParam);

            // 执行请求
            response = httpClient.execute(httpPost);
            // 获得响应的实体对象
            HttpEntity entity = response.getEntity();
            // 使用Apache提供的工具类进行转换成字符串
            entityStr = EntityUtils.toString(entity, "UTF-8");
            // 响应结果：{"spid":"000","idertifier":"{\"result\":\"1\",\"msgid\":\"91202105261539445737\",\"resultdesc\":\"SUCCESS\"}\n","description":"成功","returnCode":"0000"}
            System.out.println(entityStr);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }

    }


}

```

### 4、接口成功响应

#### 4.1 响应报文：

```json
{"spid":"000","idertifier":"{\"result\":\"1\",\"msgid\":\"91202105261539445737\",\"resultdesc\":\"SUCCESS\"}\n","description":"成功","returnCode":"0000"}
```

 ![image-20210526154643386](E:\工作\项目\爱音乐\images\image-20210526154643386.png)

#### 4.2 成功响应完整日志

```java
signature = b381a181dc8caf93ee8a2ce638f905a3
15:39:44.183 [main] DEBUG org.apache.http.client.protocol.RequestAddCookies - CookieSpec selected: default
15:39:44.200 [main] DEBUG org.apache.http.client.protocol.RequestAuthCache - Auth cache not set in the context
15:39:44.203 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection request: [route: {}->http://cmsapi.imu.cn:80][total kept alive: 0; route allocated: 0 of 2; total allocated: 0 of 20]
15:39:44.222 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection leased: [id: 0][route: {}->http://cmsapi.imu.cn:80][total kept alive: 0; route allocated: 1 of 2; total allocated: 1 of 20]
15:39:44.225 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Opening connection {}->http://cmsapi.imu.cn:80
15:39:44.235 [main] DEBUG org.apache.http.impl.conn.DefaultHttpClientConnectionOperator - Connecting to cmsapi.imu.cn/172.26.7.236:80
15:39:44.245 [main] DEBUG org.apache.http.impl.conn.DefaultHttpClientConnectionOperator - Connection established 172.21.254.127:51033<->172.26.7.236:80
15:39:44.246 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Executing request POST /remoting/portal_api/imusic/sms_ability/send_templatesms HTTP/1.1
15:39:44.246 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Target auth state: UNCHALLENGED
15:39:44.246 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Proxy auth state: UNCHALLENGED
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> POST /remoting/portal_api/imusic/sms_ability/send_templatesms HTTP/1.1
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> deviceid: 10063
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> timestamp: 20210526153943
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> signature: b381a181dc8caf93ee8a2ce638f905a3
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Content-Length: 130
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Host: cmsapi.imu.cn
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Connection: Keep-Alive
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> User-Agent: Apache-HttpClient/4.5.7 (Java/11.0.6)
15:39:44.249 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Accept-Encoding: gzip,deflate
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "POST /remoting/portal_api/imusic/sms_ability/send_templatesms HTTP/1.1[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "deviceid: 10063[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "timestamp: 20210526153943[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "signature: b381a181dc8caf93ee8a2ce638f905a3[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Content-Length: 130[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Content-Type: application/x-www-form-urlencoded; charset=UTF-8[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Host: cmsapi.imu.cn[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "User-Agent: Apache-HttpClient/4.5.7 (Java/11.0.6)[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "[\r][\n]"
15:39:44.250 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "mobile=13417778470&templateId=1091549134&templateParam=%7B%22randcode%22%3A%22990737%22%2C%22minute%22%3A%225%22%7D&sendSmsOrder=1"
15:39:44.904 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Date: Wed, 26 May 2021 07:39:44 GMT[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Content-Type: application/json;charset=UTF-8[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Transfer-Encoding: chunked[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Connection: keep-alive[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Server: imusic-gateway[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Req-ID: 00000c0004a8332409f89a1d[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Content-Encoding: gzip[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "96[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "[0x1f][0x8b][0x8][0x0][0x0][0x0][0x0][0x0][0x0][0x3]%[0x8e]1[0xe][0xc2]0[0x10][0x4][0xff]ru[0x8a][0xb3][0x13][0x13]%[0xad][0xc5][0xb],[0xba][0xeb][0xb0]A'[0x81][0x13][0x9d][0x9d]*J[0x8f][0xa8][0xf8][0x1][0xaf][0x83]w`[0x9c]rG[0xbb][0x9a]]![0xcd][0xec]a[0x4]D[0x84][0x6][0xd8][0x7][0xc9]|[0xe1] [0x5][0xad][0x4][0x12][0xd2]r[0xcb][0x4]#[0x81]"h[0x8][0xee][0xe9][0xca][0xbe][0xe6]Ai[0xd4][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "[0x8d]>([0xd3][0xe]]g[0xfa][0xb6][0xaf][0x95]}[0xe3]C:[0xd7][0x9e];Y{t[0x8e]`[0xa3]X[0xc].<g[0x9e]bQ|[0x1f][0xaf][0xcf][0xf3]][0xb0][0x84][0xbc]H[0xb4][0x93][0xf][0xfb][0x17][0x84][0xed][0x7]c[0xc4]1[0x9b][0x0][0x0][0x0][\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "0[\r][\n]"
15:39:44.905 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "[\r][\n]"
15:39:44.908 [main] DEBUG org.apache.http.headers - http-outgoing-0 << HTTP/1.1 200 OK
15:39:44.908 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Date: Wed, 26 May 2021 07:39:44 GMT
15:39:44.908 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Content-Type: application/json;charset=UTF-8
15:39:44.908 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Transfer-Encoding: chunked
15:39:44.908 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Connection: keep-alive
15:39:44.908 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Server: imusic-gateway
15:39:44.908 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Req-ID: 00000c0004a8332409f89a1d
15:39:44.908 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Content-Encoding: gzip
15:39:44.915 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Connection can be kept alive indefinitely
15:39:44.921 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection [id: 0][route: {}->http://cmsapi.imu.cn:80] can be kept alive indefinitely
15:39:44.922 [main] DEBUG org.apache.http.impl.conn.DefaultManagedHttpClientConnection - http-outgoing-0: set socket timeout to 0
15:39:44.922 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection released: [id: 0][route: {}->http://cmsapi.imu.cn:80][total kept alive: 1; route allocated: 1 of 2; total allocated: 1 of 20]
{"spid":"000","idertifier":"{\"result\":\"1\",\"msgid\":\"91202105261539445737\",\"resultdesc\":\"SUCCESS\"}\n","description":"成功","returnCode":"0000"}
15:39:44.922 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection manager is shutting down
15:39:44.922 [main] DEBUG org.apache.http.impl.conn.DefaultManagedHttpClientConnection - http-outgoing-0: Close connection
15:39:44.923 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection manager shut down
```

### 5、接口错误响应

#### 5.1 响应报文：

```json
{"returnCode":"0007","description":"Sig 授权验证失败","code":"0007","desc":"Sig 授权验证失败"}
```

 ![image-20210526154854977](E:\工作\项目\爱音乐\images\image-20210526154854977.png)

#### 5.2 错误响应完整日志

```java
"D:\Program Files\JetBrains\IntelliJ IDEA 2019.3.4\jbr\bin\java.exe" "-javaagent:D:\Program Files\JetBrains\IntelliJ IDEA 2019.3.4\lib\idea_rt.jar=51273:D:\Program Files\JetBrains\IntelliJ IDEA 2019.3.4\bin" -Dfile.encoding=UTF-8 -classpath "D:\Program Files\JetBrains\workspaces\demo\target\classes;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot-starter-web\2.2.5.RELEASE\spring-boot-starter-web-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot-starter\2.2.5.RELEASE\spring-boot-starter-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot\2.2.5.RELEASE\spring-boot-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot-autoconfigure\2.2.5.RELEASE\spring-boot-autoconfigure-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot-starter-logging\2.2.5.RELEASE\spring-boot-starter-logging-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar;D:\Program Files (x86)\maven\repo\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;D:\Program Files (x86)\maven\repo\org\apache\logging\log4j\log4j-to-slf4j\2.12.1\log4j-to-slf4j-2.12.1.jar;D:\Program Files (x86)\maven\repo\org\apache\logging\log4j\log4j-api\2.12.1\log4j-api-2.12.1.jar;D:\Program Files (x86)\maven\repo\org\slf4j\jul-to-slf4j\1.7.30\jul-to-slf4j-1.7.30.jar;D:\Program Files (x86)\maven\repo\jakarta\annotation\jakarta.annotation-api\1.3.5\jakarta.annotation-api-1.3.5.jar;D:\Program Files (x86)\maven\repo\org\yaml\snakeyaml\1.25\snakeyaml-1.25.jar;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot-starter-json\2.2.5.RELEASE\spring-boot-starter-json-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\com\fasterxml\jackson\core\jackson-databind\2.10.2\jackson-databind-2.10.2.jar;D:\Program Files (x86)\maven\repo\com\fasterxml\jackson\core\jackson-annotations\2.10.2\jackson-annotations-2.10.2.jar;D:\Program Files (x86)\maven\repo\com\fasterxml\jackson\core\jackson-core\2.10.2\jackson-core-2.10.2.jar;D:\Program Files (x86)\maven\repo\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.10.2\jackson-datatype-jdk8-2.10.2.jar;D:\Program Files (x86)\maven\repo\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.10.2\jackson-datatype-jsr310-2.10.2.jar;D:\Program Files (x86)\maven\repo\com\fasterxml\jackson\module\jackson-module-parameter-names\2.10.2\jackson-module-parameter-names-2.10.2.jar;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot-starter-tomcat\2.2.5.RELEASE\spring-boot-starter-tomcat-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\apache\tomcat\embed\tomcat-embed-core\9.0.31\tomcat-embed-core-9.0.31.jar;D:\Program Files (x86)\maven\repo\org\apache\tomcat\embed\tomcat-embed-el\9.0.31\tomcat-embed-el-9.0.31.jar;D:\Program Files (x86)\maven\repo\org\apache\tomcat\embed\tomcat-embed-websocket\9.0.31\tomcat-embed-websocket-9.0.31.jar;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot-starter-validation\2.2.5.RELEASE\spring-boot-starter-validation-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\jakarta\validation\jakarta.validation-api\2.0.2\jakarta.validation-api-2.0.2.jar;D:\Program Files (x86)\maven\repo\org\hibernate\validator\hibernate-validator\6.0.18.Final\hibernate-validator-6.0.18.Final.jar;D:\Program Files (x86)\maven\repo\org\jboss\logging\jboss-logging\3.4.1.Final\jboss-logging-3.4.1.Final.jar;D:\Program Files (x86)\maven\repo\com\fasterxml\classmate\1.5.1\classmate-1.5.1.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-web\5.2.4.RELEASE\spring-web-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-beans\5.2.4.RELEASE\spring-beans-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-webmvc\5.2.4.RELEASE\spring-webmvc-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-aop\5.2.4.RELEASE\spring-aop-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-context\5.2.4.RELEASE\spring-context-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-expression\5.2.4.RELEASE\spring-expression-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\boot\spring-boot-starter-data-redis\2.2.5.RELEASE\spring-boot-starter-data-redis-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\data\spring-data-redis\2.2.5.RELEASE\spring-data-redis-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\data\spring-data-keyvalue\2.2.5.RELEASE\spring-data-keyvalue-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\data\spring-data-commons\2.2.5.RELEASE\spring-data-commons-2.2.5.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-tx\5.2.4.RELEASE\spring-tx-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-oxm\5.2.4.RELEASE\spring-oxm-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-context-support\5.2.4.RELEASE\spring-context-support-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\slf4j\slf4j-api\1.7.30\slf4j-api-1.7.30.jar;D:\Program Files (x86)\maven\repo\io\lettuce\lettuce-core\5.2.2.RELEASE\lettuce-core-5.2.2.RELEASE.jar;D:\Program Files (x86)\maven\repo\io\netty\netty-common\4.1.45.Final\netty-common-4.1.45.Final.jar;D:\Program Files (x86)\maven\repo\io\netty\netty-handler\4.1.45.Final\netty-handler-4.1.45.Final.jar;D:\Program Files (x86)\maven\repo\io\netty\netty-buffer\4.1.45.Final\netty-buffer-4.1.45.Final.jar;D:\Program Files (x86)\maven\repo\io\netty\netty-codec\4.1.45.Final\netty-codec-4.1.45.Final.jar;D:\Program Files (x86)\maven\repo\io\netty\netty-transport\4.1.45.Final\netty-transport-4.1.45.Final.jar;D:\Program Files (x86)\maven\repo\io\netty\netty-resolver\4.1.45.Final\netty-resolver-4.1.45.Final.jar;D:\Program Files (x86)\maven\repo\io\projectreactor\reactor-core\3.3.3.RELEASE\reactor-core-3.3.3.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\reactivestreams\reactive-streams\1.0.3\reactive-streams-1.0.3.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-core\5.2.4.RELEASE\spring-core-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\springframework\spring-jcl\5.2.4.RELEASE\spring-jcl-5.2.4.RELEASE.jar;D:\Program Files (x86)\maven\repo\org\apache\commons\commons-pool2\2.7.0\commons-pool2-2.7.0.jar;D:\Program Files (x86)\maven\repo\com\google\guava\guava\16.0.1\guava-16.0.1.jar;D:\Program Files (x86)\maven\repo\org\apache\httpcomponents\httpclient\4.5.7\httpclient-4.5.7.jar;D:\Program Files (x86)\maven\repo\org\apache\httpcomponents\httpcore\4.4.13\httpcore-4.4.13.jar;D:\Program Files (x86)\maven\repo\commons-codec\commons-codec\1.13\commons-codec-1.13.jar;D:\Program Files (x86)\maven\repo\com\alibaba\fastjson\1.2.70\fastjson-1.2.70.jar;D:\Program Files (x86)\maven\repo\com\squareup\okhttp3\okhttp\3.10.0\okhttp-3.10.0.jar;D:\Program Files (x86)\maven\repo\com\squareup\okio\okio\1.14.0\okio-1.14.0.jar" com.example.demo.aimusic.SmsTemplate
signature = 4cdcbe010400bd1316f910542933032b
15:47:11.988 [main] DEBUG org.apache.http.client.protocol.RequestAddCookies - CookieSpec selected: default
15:47:12.003 [main] DEBUG org.apache.http.client.protocol.RequestAuthCache - Auth cache not set in the context
15:47:12.005 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection request: [route: {}->http://cmsapi.imu.cn:80][total kept alive: 0; route allocated: 0 of 2; total allocated: 0 of 20]
15:47:12.016 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection leased: [id: 0][route: {}->http://cmsapi.imu.cn:80][total kept alive: 0; route allocated: 1 of 2; total allocated: 1 of 20]
15:47:12.017 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Opening connection {}->http://cmsapi.imu.cn:80
15:47:12.032 [main] DEBUG org.apache.http.impl.conn.DefaultHttpClientConnectionOperator - Connecting to cmsapi.imu.cn/172.26.7.236:80
15:47:12.040 [main] DEBUG org.apache.http.impl.conn.DefaultHttpClientConnectionOperator - Connection established 172.21.254.127:51281<->172.26.7.236:80
15:47:12.040 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Executing request POST /remoting/portal_api/imusic/sms_ability/send_templatesms HTTP/1.1
15:47:12.040 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Target auth state: UNCHALLENGED
15:47:12.040 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Proxy auth state: UNCHALLENGED
15:47:12.041 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> POST /remoting/portal_api/imusic/sms_ability/send_templatesms HTTP/1.1
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> deviceid: 10063
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> timestamp: 20210526154711
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> signature: 4cdcbe010400bd1316f910542933032b
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Content-Length: 130
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Content-Type: application/x-www-form-urlencoded; charset=UTF-8
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Host: cmsapi.imu.cn
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Connection: Keep-Alive
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> User-Agent: Apache-HttpClient/4.5.7 (Java/11.0.6)
15:47:12.042 [main] DEBUG org.apache.http.headers - http-outgoing-0 >> Accept-Encoding: gzip,deflate
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "POST /remoting/portal_api/imusic/sms_ability/send_templatesms HTTP/1.1[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "deviceid: 10063[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "timestamp: 20210526154711[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "signature: 4cdcbe010400bd1316f910542933032b[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Content-Length: 130[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Content-Type: application/x-www-form-urlencoded; charset=UTF-8[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Host: cmsapi.imu.cn[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "User-Agent: Apache-HttpClient/4.5.7 (Java/11.0.6)[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "Accept-Encoding: gzip,deflate[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "[\r][\n]"
15:47:12.042 [main] DEBUG org.apache.http.wire - http-outgoing-0 >> "mobile=13417778470&templateId=1091549134&templateParam=%7B%22randcode%22%3A%22990737%22%2C%22minute%22%3A%225%22%7D&sendSmsOrder=1"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Date: Wed, 26 May 2021 07:47:11 GMT[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Content-Type: application/json;charset=UTF-8[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Transfer-Encoding: chunked[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Connection: keep-alive[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Server: imusic-gateway[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Req-ID: 00000b8004a8332417f81736[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "Content-Encoding: gzip[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "57[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "[0x1f][0x8b][0x8][0x0][0x0][0x0][0x0][0x0][0x0][0x3][0xab]V*J-)-[0xca]s[0xce]OIU[0xb2]R2000W[0xd2]QJI-N.[0xca],([0xc9][0xcc][0xcf][0x3][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "[0x6]g[0xa6]+<[0xeb][0xeb]x6[0xb7][0xf9][0xe5][0xaa][0x9e][0x17][0xeb][0x1b][0x9f].[0xd9][0xf8]b[0xcb]R[0xa0][0xb2]d[0xc]M[0xb8]U[0xd7][0x2][0x0][0xb5][0xb0][0x9c][0x0]j[0x0][0x0][0x0][\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "0[\r][\n]"
15:47:12.054 [main] DEBUG org.apache.http.wire - http-outgoing-0 << "[\r][\n]"
15:47:12.056 [main] DEBUG org.apache.http.headers - http-outgoing-0 << HTTP/1.1 200 OK
15:47:12.056 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Date: Wed, 26 May 2021 07:47:11 GMT
15:47:12.057 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Content-Type: application/json;charset=UTF-8
15:47:12.057 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Transfer-Encoding: chunked
15:47:12.057 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Connection: keep-alive
15:47:12.057 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Server: imusic-gateway
15:47:12.057 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Req-ID: 00000b8004a8332417f81736
15:47:12.057 [main] DEBUG org.apache.http.headers - http-outgoing-0 << Content-Encoding: gzip
15:47:12.060 [main] DEBUG org.apache.http.impl.execchain.MainClientExec - Connection can be kept alive indefinitely
15:47:12.067 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection [id: 0][route: {}->http://cmsapi.imu.cn:80] can be kept alive indefinitely
15:47:12.067 [main] DEBUG org.apache.http.impl.conn.DefaultManagedHttpClientConnection - http-outgoing-0: set socket timeout to 0
15:47:12.067 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection released: [id: 0][route: {}->http://cmsapi.imu.cn:80][total kept alive: 1; route allocated: 1 of 2; total allocated: 1 of 20]
{"returnCode":"0007","description":"Sig 授权验证失败","code":"0007","desc":"Sig 授权验证失败"}
15:47:12.067 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection manager is shutting down
15:47:12.067 [main] DEBUG org.apache.http.impl.conn.DefaultManagedHttpClientConnection - http-outgoing-0: Close connection
15:47:12.067 [main] DEBUG org.apache.http.impl.conn.PoolingHttpClientConnectionManager - Connection manager shut down

```