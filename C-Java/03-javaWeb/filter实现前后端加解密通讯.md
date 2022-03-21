## 一、对ServletRequest 对象重新包装

可针对前端加密请求解密，并重新设置到ServletRequest 对象，让下游的业务接口可接收解密后的明文参数

```java
package com.xy.framework.security.filter;


import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class WrapperedRequest extends HttpServletRequestWrapper {
    /**
     * 请求报文
     */
    private String requestBody = null;
    HttpServletRequest req = null;
    private Map<String, String[]> params = new HashMap<>();

    public WrapperedRequest(HttpServletRequest request) {
        super(request);
        this.req = request;
        //将参数表，赋予给当前的Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
    }

    public WrapperedRequest(HttpServletRequest request, String requestBody) {
        super(request);
        this.requestBody = requestBody;
        this.req = request;
    }

    /**
     * 重载构造方法
     */

    public WrapperedRequest(HttpServletRequest request, Map<String, Object> extendParams) {
        this(request);
        //这里将扩展参数写入参数表
        addAllParameters(extendParams);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.ServletRequestWrapper#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new StringReader(requestBody));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
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

## 二、对ServletResponse 对象重新包装

拦截响应报文，针对需要加密的接口做加密后响应

```java
package com.xy.framework.security.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * @Description: 响应包装类
 * @Date: 2020/5/26 16:29
 */
public class MyResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream buffer = null;
    private ServletOutputStream out = null;
    private PrintWriter writer = null;

    public MyResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        buffer = new ByteArrayOutputStream();// 真正存储数据的流
        out = new WapperedOutputStream(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding()));
    }

    /**
     * 重载父类获取outputstream的方法
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return out;
    }

    /**
     * 重载父类获取writer的方法
     */
    @Override
    public PrintWriter getWriter() throws UnsupportedEncodingException {
        return writer;
    }

    /**
     * 重载父类获取flushBuffer的方法
     */
    @Override
    public void flushBuffer() throws IOException {
        if (out != null) {
            out.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    /**
     * 将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据
     */
    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    /**
     * 内部类，对ServletOutputStream进行包装
     */
    private class WapperedOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos = null;

        public WapperedOutputStream(ByteArrayOutputStream stream)
                throws IOException {
            bos = stream;
        }

        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            bos.write(b, 0, b.length);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}

```



## 三、自定义Filter过滤器

针对业务接口做统一加解密处理

```java
package com.xy.framework.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.xy.common.constant.Constants;
import com.xy.common.constant.HttpStatus;
import com.xy.common.core.domain.AjaxResult;
import com.xy.common.core.domain.model.LoginBody;
import com.xy.common.core.domain.model.LoginUser;
import com.xy.common.core.redis.RedisCache;
import com.xy.common.exception.ServiceException;
import com.xy.common.utils.*;
import com.xy.common.utils.spring.SpringUtils;
import org.apache.catalina.util.ParameterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter
public class ZEncryptFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(ZEncryptFilter.class);

    //2048
//    private static final String rsaPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDKfIiQany1sL6PJWeKatt825lLOSVnVOMsNsQLETpmeMDYVzhXFAFw40rAVrsPw0NvrgUg8nHH4WS/SKSMVOmIjHh3ZDNPiYY34j9CeiIuXu6usDHqvUtJTY0BsDwv6glpRu0ek1kOvnQgcvznI4JW7iF+UUOlliUN/dguvvMT2fd6w8Zu9dYuSQDeT+hSJaWKvz7k3HgB2wbUJOU0dsl2vOdvaK5SCc6bhbFRY3+mmXtcun2+PQHYoPoqF2thm+P9PHhFhww+WqEgFyUqVPVQzgshDVSXQzfOi21moHLDycTo/oYdWPlvziLFcXpMWDXrrwEVF7DSJH+Jt4QaP0ErAgMBAAECggEAK9INn+tbt9pbMxFuOfdsLrY7k5r3bEhjrYLgpGCDGgASA//EMpwiv7KAz+U1MHEanAW2GG4/j3lI21i2O6R4sdNskrH9pL3nbRqqhBF4aj6jOuzosJtsup1s4aupKtlkVgNCd7Z9tIIlEYyxDLiTkYqRdwvRLvmyZTfFXEB4zJehfbZ2UyuSEY8kDjqHfzqOOsJOEVy3AUS1XiEHdiwvzILFFKgjZlXdqxn4Jif9Eu1LL681psvUD8f/zneU418CTfrEFYruc57Vj+3wiVe6xpfPnk6bYc4+7/JVbrj270mx7j4DtMkJbjXfsdgTvdQXfpdfzUjDrtgEKKpRDqZaCQKBgQD0EYVYE22Z3VrhHbd+LeSYtU5JVXLydk83uCzE/SAXaNXPpw/pzHgxuMHPExZn3H/ppMBbkDYdybv875hmtLqa93f65yCNB1g8HxR3E2QfHC0Q+F0pT3ZN/kPGV4RGX11F64LaYA/5m8EqJCw8GzrzfQmrRuFB3OVmkbMl7dGa7wKBgQDUYp7VkqPY8yPGYZa9rivn03bUGheyBSJ1g1norGE4FFFzFrcFx/y4W6/qi0NOSIOOMwiH+v3VGhowdqGKg+isZ6HVP19eDCdt7L/jL9zafuAA0TJiwg7tDx/T8qNtNFiMDzDfnjnjRORzf/kGtWLV55Tru67vMNDwj6UIsL5thQKBgQDSQsHYVaxnEF45b4tjhY/YyuDtplKHdMrcGqte7R4XbLY91RONfBvT4fH9PdlgkoITu14Hw54LXX648YQefvX/iM/w8+qD9F7cNH5B8w+fCXv9kalYbF9j43dpyH1ozsEg+jpZfMZmZ/sqHc2J3n+TwhVxW3e1cOKj0UM/mrinOwKBgQCDyt79cQ2zps1gYVaFRfuA1Dw8yiRCF+WWPnggZje98P3KCSJxt/QfAuZbCSgu/9iC+TAmEZ5KCvSNdlAwAEL2GSjXh4fKeIhPMkaw2BaYj3q5hxdZKUrR2DzL7Rl0vrLj1tlhKnKJkUqiUZtailxWmGpVDhYz33sIjFrA1cyVcQKBgBDKyfDGuV+f1yHptdk61OMVYsvpN6yu68HNxJV9azl3TRCbbxVidpDh0fbv28Uzqktxycrn/VXxQooiB6z1o6Ae9QPTpJrGHMwLCtwrg2S2O6iT0I9l+5nQA3g88vh4puYuYRZwyqENPJkYASdf2Xqb+yQUu33qiV4tcQn1Lgm8";
    //4096
    private static final String rsaPrivateKey = "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";

    private String ignoreStr = "html,swagger,js,css,profile,webjars,api-docs,druid,ico,error," +
            "logout,captchaImage,register,download," +
            "/report/export,/batchImport,/etprInfo/find/password,/document/add,smsSendRecord/export";
    private String[] ignoreArr;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if ("local".equals(SpringContextUtil.getActiveProfile()) || "dev".equals(SpringContextUtil.getActiveProfile())) {
            log.info("请求：{}, 不做加解密处理", ((HttpServletRequest) servletRequest).getRequestURI());
            chain.doFilter(servletRequest, servletResponse);
        } else {
            if (ignoreArr == null) {
                ignoreArr = ignoreStr.split(",");
            }
            boolean flag = isIgnore(servletRequest, ignoreArr);
            if (!flag) {
                try {
                    WrapperedRequest wrapRequest = new WrapperedRequest((HttpServletRequest) servletRequest);
                    MyResponseWrapper responseWrapper = new MyResponseWrapper((HttpServletResponse) servletResponse);

                    log.info("请求：{}, {}, 进入加解密filter", wrapRequest.getRequestURI(), wrapRequest.getMethod());
                    String aesKey = getAesKey(servletRequest);
                    if (wrapRequest.getMethod().equals("POST")) {
                        String decryptRequestBody = decryptRequestBody(servletRequest, aesKey);
                        if (wrapRequest.getRequestURI().contains("login")) {
                            LoginBody loginBody = JSONObject.parseObject(decryptRequestBody, LoginBody.class);
                            aesKey = loginBody.getAesKey();
                        }
                        log.debug("请求：{}，解密请求体：{}", wrapRequest.getRequestURI(), decryptRequestBody);
                        wrapRequest = new WrapperedRequest((HttpServletRequest) servletRequest, decryptRequestBody);
                    }
                    if (wrapRequest.getMethod().equals("GET")) {
                        String data = wrapRequest.getParameter("data");
                        log.info("请求：{}，原始参数:{}", wrapRequest.getRequestURI(), data);
                        if (!Strings.isNullOrEmpty(data)) {
                            String decryptRequestParam = AESUtil.decryptBase64URL(aesKey, data);
                            log.debug("请求：{}，参数解密:{}", wrapRequest.getRequestURI(), decryptRequestParam);
                            if (!Strings.isNullOrEmpty(decryptRequestParam)) {
                                ParameterMap<String, String[]> parameterMap = (ParameterMap<String, String[]>) wrapRequest.getParameterMap();
                                String[] split = decryptRequestParam.split("&");
                                if (split.length > 0) {
                                    Map paramter = new HashMap(16);
                                    for (int i = 0; i < split.length; i++) {
                                        String s = split[i];
                                        String[] vo = s.split("=");
                                        if (vo.length > 1) {
                                            paramter.put(vo[0], vo[1]);
                                        } else {
                                            paramter.put(vo[0], "");
                                        }

                                    }
                                    wrapRequest = new WrapperedRequest((HttpServletRequest) servletRequest, paramter);
                                }
                            }
                        }
                    }

                    //执行业务逻辑 交给下一个过滤器或servlet处理
                    chain.doFilter(wrapRequest, responseWrapper);

                    //获取响应内容
                    String resData = new String(responseWrapper.getResponseData());
                    if (!Strings.isNullOrEmpty(aesKey)) {
                        //加密响应报文
                        String encryptBASE64 = AESUtil.encryptBase64URL(aesKey, resData);
                        log.info("请求：{} 密文响应, aesKey: {}, 密文：{}, 明文：{}", wrapRequest.getRequestURI(), aesKey, encryptBASE64, resData);
                        HttpResponseUtil.output((HttpServletResponse) servletResponse, encryptBASE64);
                    } else {
                        log.info("请求：{} 明文响应, 明文：{}", wrapRequest.getRequestURI(), resData);
                        HttpResponseUtil.output((HttpServletResponse) servletResponse, resData);
                    }
                } catch (Exception e) {
                    AjaxResult ajaxResult = new AjaxResult(HttpStatus.ERROR, e.getMessage());
                    HttpResponseUtil.output((HttpServletResponse) servletResponse, JSONObject.toJSONString(ajaxResult));
                }
            } else {
                log.info("请求：{}, 不做加解密处理", ((HttpServletRequest) servletRequest).getRequestURI());
                chain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    /**
     * 解密post请求体
     */
    private String decryptRequestBody(ServletRequest servletRequest, String aesKey) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String decryptStr = "";
        try {
            String requestBody = getRequestBody((HttpServletRequest) servletRequest);
            if (!Strings.isNullOrEmpty(requestBody)) {
                JSONObject jsonObject = JSONObject.parseObject(requestBody);
                if (request.getRequestURI().contains("login")) {
                    decryptStr = RSAUtil.privateKeyDecrypt(rsaPrivateKey, jsonObject.getString("encryptData"));
                } else {
                    decryptStr = AESUtil.decryptBase64URL(aesKey, jsonObject.getString("encryptData"));
                }
            }
        } catch (Exception e) {
            throw new ServiceException("请求报文体解密异常");
        }
        return decryptStr;
    }

    private String getAesKey(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String aesKey = null;
        if (!request.getRequestURI().contains("login")) {
            SecurityContext context = SecurityContextHolder.getContext();
            LoginUser principal = (LoginUser) context.getAuthentication().getPrincipal();
            log.info("tokenId: {}", principal.getToken());

            RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
            //获取用户的aesKey
            Long userId = SecurityUtils.getLoginUser().getUserId();
            String aesCacheKey = String.format(Constants.AES_KEY, principal.getToken(), userId);
            aesKey = redisCache.getCacheObject(aesCacheKey);
            log.info("当前请求用户id：{}, aes缓存key：{}, aes缓存值：{} ", userId, aesCacheKey, aesKey);
        }
        return aesKey;
    }

    /**
     * 哪些路径不处理
     *
     * @param servletRequest
     * @param strArr
     * @return
     */
    public boolean isIgnore(ServletRequest servletRequest, String[] strArr) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();
        for (String ignore : strArr) {
            if (path.contains(ignore)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param req
     * @return
     */
    private String getRequestBody(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            return json;
        } catch (IOException e) {
            log.error("验签时请求体读取失败", e);
            throw new ServiceException("验签时请求体读取失败");
        }
    }
}


```



## 四、设置Security配置

需要把自定义Filter放在security过滤链的SessionManagementFilter.class 后，这样才能获取SecurityContext的上下文会话信息。

```java
package com.xy.framework.config;

import com.xy.framework.security.filter.JwtAuthenticationTokenFilter;
import com.xy.framework.security.filter.ZEncryptFilter;
import com.xy.framework.security.handle.AuthenticationEntryPointImpl;
import com.xy.framework.security.handle.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * spring security配置
 *
 * @author ruoyi
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                .antMatchers("/login", "/register", "/captchaImage").anonymous()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/profile/**",
                        "/login",
                        "/**/download/**"
                ).permitAll()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/druid/**").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 报文加解密
        httpSecurity.addFilterAfter(new ZEncryptFilter(), SessionManagementFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        //强制忽略
        web.ignoring()
        .antMatchers("/etprInfo/find/password")
        .antMatchers("/smsSend/etprFindPwdVerifyCode")
        .antMatchers("/smsSend/loginVerifyCode");
    }


    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}

```

## 五、登录成功记录用户的aesKey

这样能确保每个用户的每次会话的aesKey都不一样。会话加解密的安全等级能去到会话级。

```java
/**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public LoginResponse login(String username, String password, String code, String uuid, LoginBody loginBody) {
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        if (LoginChannelEnum.MANAGEMENT.getCode().equals(loginBody.getLoginType()) && !"admin".equals(loginBody.getUsername())) {
            // 校验短信验证码
            FgiVerifySmsCodeVo verifySmsCodeVo = new FgiVerifySmsCodeVo(loginBody.getMsgId(), loginBody.getUsername(), loginBody.getSmsCode());
            smsSendService.VerifySmsCode(verifySmsCodeVo);
        } else if (captchaOnOff) {
            // 验证码开关
            validateCaptcha(username, code, uuid);
        }
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        String token = tokenService.createToken(loginUser);
        FgiEtprInfo etprInfo = loginUser.getEtprInfo();
        SysUser user = loginUser.getUser();
        LoginResponse loginResponse = new LoginResponse(token, user.getChangePwd(), loginUser.getEtprType(), loginUser.getUserKid());
        if (null != etprInfo) {
            if (Strings.isNullOrEmpty(etprInfo.getDeliveryUser()) || Strings.isNullOrEmpty(etprInfo.getDeliveryAddress()) || Strings.isNullOrEmpty(etprInfo.getDeliveryPhone())) {
                loginResponse.setIsDeliveryInfo(ChangePwdStatusEnum.UNCHANGE.getCode());
            }
        }
        //用户登录成功后，设置缓存记录用户对应的aesKey
        cacheAesKey(loginUser, loginBody);
        return loginResponse;
    }



    /**
     * 缓存用户的aesKey
     */
    public void cacheAesKey(LoginUser loginUser, LoginBody loginBody) {
        if (null != loginBody && !Strings.isNullOrEmpty(loginBody.getAesKey())) {
            String aesCacheKey = String.format(Constants.AES_KEY, loginUser.getToken(), loginUser.getUserId());
            redisCache.setCacheObject(aesCacheKey, loginBody.getAesKey(), tokenExpireMins, TimeUnit.MINUTES);
            log.info("用户:{}, 用户id：{}, tokenId：{}, aes缓存key：{}, aes缓存值：{} ", loginUser.getUsername(), loginUser.getUserId(), loginUser.getToken(), aesCacheKey, loginBody.getAesKey());
        }
    }


    /**
     * 缓存用户的 AES 密钥
     * AES_KEY:tokenId:userId
     */
    public static final String AES_KEY = "AES_KEY:%s:%s";

```



## 六、RSA工具类

使用4096的密钥长度，解密内容超过256个字符就会报错

```java
package com.xy.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.xy.common.core.domain.model.LoginBody;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtil {

    private static final String ALGORITHM = "RSA";

    /**
     * 生成RSA公私钥
     *
     * @param keysize 密钥位数
     * @return 返回RSA公私钥
     * @throws Exception
     */
    public static KeyPair getKeyPair(int keysize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(keysize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取Base64编码的RSA公钥
     *
     * @param keyPair
     * @return 返回Base64编码的RSA公钥，参数不符合要求时返回null
     */
    public static String getPublicKey(KeyPair keyPair) {
        return keyPair == null ? null : byteToBase64(keyPair.getPublic().getEncoded());
    }

    /**
     * 获取Base64编码的RSA私钥
     *
     * @param keyPair
     * @return 返回Base64编码的RSA私钥，参数不符合要求时返回null
     */
    public static String getPrivateKey(KeyPair keyPair) {
        return keyPair == null ? null : byteToBase64(keyPair.getPrivate().getEncoded());
    }

    /**
     * 将Base64编码公钥转换成PublicKey对象
     *
     * @param base64PublicKey Base64编码公钥
     * @return 返回RSA公钥PublicKey对象，参数不符合要求时返回null
     */
    public static PublicKey stringToPublicKey(String base64PublicKey) throws Exception {
        return base64PublicKey == null
                ? null
                : KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(base64ToByte(base64PublicKey)));
    }

    /**
     * 将Base64编码私钥转换成PrivateKey对象
     *
     * @param base64PrivateKey Base64编码私钥
     * @return 返回RSA私钥PrivateKey对象，参数不符合要求时返回null
     */
    public static PrivateKey stringToPrivateKey(String base64PrivateKey) throws Exception {
        return base64PrivateKey == null
                ? null
                : KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(base64ToByte(base64PrivateKey)));
    }

    /**
     * 使用Base64编码公钥加密数据
     *
     * @param base64PublicKey Base64编码公钥
     * @param content         待加密数据
     * @return 返回加密后的base64编码数据，参数不符合要求时返回null
     * @throws Exception
     */
    public static String publicKeyEncrypt(String base64PublicKey, String content) throws Exception {
        if (base64PublicKey == null || content == null) {
            return null;
        }

        return byteToBase64(publicKeyEncrypt(stringToPublicKey(base64PublicKey), content.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 使用公钥加密数据
     *
     * @param publicKey 公钥
     * @param content   待加密byte数据
     * @return 返回加密后的byte数据
     * @throws Exception
     */
    public static byte[] publicKeyEncrypt(PublicKey publicKey, byte[] content) throws Exception {
        if (publicKey == null || content == null) {
            return new byte[0];
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 使用Base64编码私钥解密数据
     *
     * @param base64PrivateKey Base64编码私钥
     * @param base64Content    待解密base64编码数据
     * @return 返回解密后的数据，参数不符合要求时返回null
     * @throws Exception
     */
    public static String privateKeyDecrypt(String base64PrivateKey, String base64Content) throws Exception {
        if (base64Content == null || base64PrivateKey == null) {
            return null;
        }
        return new String(privateKeyDecrypt(stringToPrivateKey(base64PrivateKey), base64ToByte(base64Content)), StandardCharsets.UTF_8);
    }


    /**
     * 使用私钥解密数据
     *
     * @param privateKey 私钥
     * @param content    待解密byte数据
     * @return 返回解密后的byte数据
     * @throws Exception
     */
    public static byte[] privateKeyDecrypt(PrivateKey privateKey, byte[] content) throws Exception {
        if (privateKey == null || content == null) {
            return new byte[0];
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    /**
     * byte数组转Base64编码
     *
     * @param bytes
     * @return 返回Base64编码内容，参数不符合要求时返回null
     */
    public static String byteToBase64(byte[] bytes) {
        return bytes == null ? null : new BASE64Encoder().encode(bytes);
    }

    /**
     * Base64编码内容解码后转byte数组
     *
     * @param base64Content Base64编码内容
     * @return 返回解码后的内容byte数组，参数不符合要求时返回null
     */
    public static byte[] base64ToByte(String base64Content) throws IOException {
        return base64Content == null ? null : new BASE64Decoder().decodeBuffer(base64Content);
    }

    public static void main(String[] args) {
        try {
            /*String content = "rsa数据加密";
            System.out.println("## content：" + content);

            KeyPair keyPair = RSAUtil.getKeyPair(4096);
            String privateKey = RSAUtil.getPrivateKey(keyPair);
            String publicKey = RSAUtil.getPublicKey(keyPair);
            System.out.println("## privateKey：" + privateKey);
            System.out.println("## publicKey：" + publicKey);*/

            //2048密钥
//            String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDKfIiQany1sL6PJWeKatt825lLOSVnVOMsNsQLETpmeMDYVzhXFAFw40rAVrsPw0NvrgUg8nHH4WS/SKSMVOmIjHh3ZDNPiYY34j9CeiIuXu6usDHqvUtJTY0BsDwv6glpRu0ek1kOvnQgcvznI4JW7iF+UUOlliUN/dguvvMT2fd6w8Zu9dYuSQDeT+hSJaWKvz7k3HgB2wbUJOU0dsl2vOdvaK5SCc6bhbFRY3+mmXtcun2+PQHYoPoqF2thm+P9PHhFhww+WqEgFyUqVPVQzgshDVSXQzfOi21moHLDycTo/oYdWPlvziLFcXpMWDXrrwEVF7DSJH+Jt4QaP0ErAgMBAAECggEAK9INn+tbt9pbMxFuOfdsLrY7k5r3bEhjrYLgpGCDGgASA//EMpwiv7KAz+U1MHEanAW2GG4/j3lI21i2O6R4sdNskrH9pL3nbRqqhBF4aj6jOuzosJtsup1s4aupKtlkVgNCd7Z9tIIlEYyxDLiTkYqRdwvRLvmyZTfFXEB4zJehfbZ2UyuSEY8kDjqHfzqOOsJOEVy3AUS1XiEHdiwvzILFFKgjZlXdqxn4Jif9Eu1LL681psvUD8f/zneU418CTfrEFYruc57Vj+3wiVe6xpfPnk6bYc4+7/JVbrj270mx7j4DtMkJbjXfsdgTvdQXfpdfzUjDrtgEKKpRDqZaCQKBgQD0EYVYE22Z3VrhHbd+LeSYtU5JVXLydk83uCzE/SAXaNXPpw/pzHgxuMHPExZn3H/ppMBbkDYdybv875hmtLqa93f65yCNB1g8HxR3E2QfHC0Q+F0pT3ZN/kPGV4RGX11F64LaYA/5m8EqJCw8GzrzfQmrRuFB3OVmkbMl7dGa7wKBgQDUYp7VkqPY8yPGYZa9rivn03bUGheyBSJ1g1norGE4FFFzFrcFx/y4W6/qi0NOSIOOMwiH+v3VGhowdqGKg+isZ6HVP19eDCdt7L/jL9zafuAA0TJiwg7tDx/T8qNtNFiMDzDfnjnjRORzf/kGtWLV55Tru67vMNDwj6UIsL5thQKBgQDSQsHYVaxnEF45b4tjhY/YyuDtplKHdMrcGqte7R4XbLY91RONfBvT4fH9PdlgkoITu14Hw54LXX648YQefvX/iM/w8+qD9F7cNH5B8w+fCXv9kalYbF9j43dpyH1ozsEg+jpZfMZmZ/sqHc2J3n+TwhVxW3e1cOKj0UM/mrinOwKBgQCDyt79cQ2zps1gYVaFRfuA1Dw8yiRCF+WWPnggZje98P3KCSJxt/QfAuZbCSgu/9iC+TAmEZ5KCvSNdlAwAEL2GSjXh4fKeIhPMkaw2BaYj3q5hxdZKUrR2DzL7Rl0vrLj1tlhKnKJkUqiUZtailxWmGpVDhYz33sIjFrA1cyVcQKBgBDKyfDGuV+f1yHptdk61OMVYsvpN6yu68HNxJV9azl3TRCbbxVidpDh0fbv28Uzqktxycrn/VXxQooiB6z1o6Ae9QPTpJrGHMwLCtwrg2S2O6iT0I9l+5nQA3g88vh4puYuYRZwyqENPJkYASdf2Xqb+yQUu33qiV4tcQn1Lgm8";
//            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAynyIkGp8tbC+jyVnimrbfNuZSzklZ1TjLDbECxE6ZnjA2Fc4VxQBcONKwFa7D8NDb64FIPJxx+Fkv0ikjFTpiIx4d2QzT4mGN+I/QnoiLl7urrAx6r1LSU2NAbA8L+oJaUbtHpNZDr50IHL85yOCVu4hflFDpZYlDf3YLr7zE9n3esPGbvXWLkkA3k/oUiWlir8+5Nx4AdsG1CTlNHbJdrznb2iuUgnOm4WxUWN/ppl7XLp9vj0B2KD6KhdrYZvj/Tx4RYcMPlqhIBclKlT1UM4LIQ1Ul0M3zottZqByw8nE6P6GHVj5b84ixXF6TFg1668BFRew0iR/ibeEGj9BKwIDAQAB";

            //4096密钥
            String privateKey = "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";
            String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAjoOcNfMospewidNqvJwjLFwN81sXZVcg5zYCuDs5RF2u5LmxcNctYqX/LQEQ/cBujkp+NjvVjcR3Qd8Vp1mWc1cjJ67qjHF12FIWIgKeUzwtaWphkzt3onvosFLCRUj0PdeYX+sdt7BIBUE1MxlNmrc9x8ztcCdINtl4Y8tWQOOmXq+sT04GTL6EoQsNOoAUI+iAtDW/ArYye0Gsd4Cc+yz71YmJZ7OL6ib+jDeKs3QymPSqja/SqlsQe5W4UVdbrqkG8Q1vHk635Sx4JSiszeJ8+6X7QiBo/at35v/jJiuHhRyrqWdaDjS82XteTdIOmfBsEC2LqoMDabzaze24euozIoBZe+0TH4+Bis6dUAGitgFD70edNoYowxnRvlx4lM4Zs0YUMVeu34XerdPWYAdJFuq96gkEBzryE3NB51V5a2yLxf5GPsZmOFSOMvIOrj5APllIzGq3VmOwtTcxV8/aUuOlBBy1NBLCUstMj5xTKLyZd6ckXaMG6txwvPnlCWuqZDSgQJswXmhhCAao2alkYQIWyc0wy3ScmrPgU+f7BU7sm2fkge0F1WiS5WM6kh+oxqcHCa2fDqbGzIr9LT3sPo1O6bAAzvVZgfKj1jHCYPmCpAuBd0kR5SuBvSeATe73rwFf04Zauei4ArjBmxHO7LsSe9UIMOHB0MsP4RkCAwEAAQ==";
            //管理端
            /*LoginBody loginBody = new LoginBody();
            loginBody.setLoginType("1");
            loginBody.setUsername("admin");
            loginBody.setPassword("08a56c78ae1fc2988eb7dc51be530d38854ec658dd555a3fc9633af6bfdc4891");
            loginBody.setCode("SPWz");//图形验证码
            loginBody.setUuid("62d4c8bbd7254930b08e132480ca62aa");//图形验证码uuid
            loginBody.setAesKey("2234567891234567");
            String content = JSONObject.toJSONString(loginBody);
            System.out.println("## content：" + content);*/

            //企业端
            LoginBody loginBody = new LoginBody();
            loginBody.setLoginType("0");
            loginBody.setEtprUniCode("91440400MA4UAE346I");
            loginBody.setUsername("admin");
            loginBody.setPassword("57c8b3d053322da4636ac1a5f7a6f88a0dc0636969d646a7d13b079e719bcb8a");
            loginBody.setCode("YFYY");//图形验证码
            loginBody.setUuid("b87931382ab94bc3b2fcfd24374aff64");//图形验证码uuid
            loginBody.setAesKey("99c4e742b3ab91ba51dc0feeccbad723");//99c4e742b3ab91ba51dc0feeccbad723
            String content = JSONObject.toJSONString(loginBody);
            System.out.println("##content长度:" + content.length() + " content：" + content);

            System.out.println("## 加密：" + RSAUtil.publicKeyEncrypt(publicKey, content));
            System.out.println("## 解密：" + RSAUtil.privateKeyDecrypt(privateKey, RSAUtil.publicKeyEncrypt(publicKey, content)));

            /*String encryptStr = "trIqAdPXBUE7ceufWvc4bBjFqJ3pDzlLszek3Eu4dfURmCqkTyIKRZyVfUHV9GDoVxOvdyadNRY+LzmDNNcZiFUh7mt4DQdDTshK61dhYXj8gFyCrKzkPY4cQFpNS6afI000teCl2fM+p+Z11bhTXNitbNi6QDH0AInbiqoEEJPylXPu2lLSAC+HJ2erFRGBoMiQ2sQTLTur5wx3lHj35PpQPqLknveuK56gOx7gOh+4y27zHxl/BdWPLklc7XYq64CZo131Ko0P5DykqkGs3zYX0mC6kCqHY9wQRAl5Q2jDDd7Sl+tmf+ilyV7A/BdbHnFaBlLWWpe2nJJFeUUZpQ==";
            System.out.println("## 解密：" + RSAUtil.privateKeyDecrypt(privateKey, encryptStr));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```



## 七、AES工具类

加密内容响应前端需要使用 Base64.encodeBase64URLSafeString ，如果返回字符串最后有“==”字符串，前端解密失败。

Base64推荐使用apache的commons-codec

```xml
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.15</version>
</dependency>
```



工具类：

```java
package com.xy.common.utils;


import com.xy.common.exception.ServiceException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AESUtil {

    // 密钥算法
    private static final String KEY_ALGORITHM = "AES";
    // 加密算法
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    // 密钥长度256位，即AES-256
    private static final int KEY_SIZE = 256;
    // 随机数生成器算法
    private static final String ALGORITHM = "SHA1PRNG";


    /**
     * aes加密(AES-256/ECB/PKCS5Padding)
     *
     * @param key     密钥
     * @param content 待加密内容
     * @return 返回加密后的base64URL编码数据
     */
    public static String encryptBase64URL(String key, String content) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
//            // 使用密钥(SHA1PRNG随机数生成器算法再次生成指定位数的AES密钥)初始化，设置为加密模式
//            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key, KEY_SIZE));

            // 直接将字符串密钥作为密钥时的使用 AES-128(key.getBytes后length要是16) 通常对应长度为16的字符串即可 sha256后拿前16个字符
            // AES-192(key.getBytes后length要是24) 通常对应长度为24的字符串即可 sha256后拿前24个字符
            // AES-256(key.getBytes后length要是32) 通常对应长度为32的字符串即可 sha256后拿前32个字符
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), KEY_ALGORITHM));
            // 加密
            return Base64.encodeBase64URLSafeString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
//            Base64.Encoder urlEncoder = Base64.getUrlEncoder();
//            return urlEncoder.encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * aes解密(AES-256/ECB/PKCS5Padding)
     *
     * @param key              密钥
     * @param base64UrlContent 待解密Base64Url编码数据
     * @return 返回解密后的数据
     */
    public static String decryptBase64URL(String key, String base64UrlContent) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
//            // 使用密钥(SHA1PRNG随机数生成器算法再次生成指定位数的AES密钥)初始化，设置为解密模式
//            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key, KEY_SIZE));

            // 直接将字符串密钥作为密钥时的使用 AES-128(key.getBytes后length要是16) 通常对应长度为16的字符串即可 sha256后拿前16个字符
            // AES-192(key.getBytes后length要是24) 通常对应长度为24的字符串即可 sha256后拿前24个字符
            // AES-256(key.getBytes后length要是32) 通常对应长度为32的字符串即可 sha256后拿前32个字符
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), KEY_ALGORITHM));
            // 解密
//            return new String(cipher.doFinal(Base64.decodeBase64URLSafe(base64UrlContent)), StandardCharsets.UTF_8);
            return new String(cipher.doFinal(Base64.decodeBase64(base64UrlContent)), StandardCharsets.UTF_8);
//            java.util.Base64.Decoder urlDecoder = java.util.Base64.getUrlDecoder();
//            return new String(cipher.doFinal(urlDecoder.decode(base64UrlContent)), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("解密请求参数异常！");
        }
    }

    /**
     * 基于SHA1PRNG随机数生成器算法再次生成指定位数的AES密钥
     * 前端需要的AES密钥是Base64.encodeBase64String(AESUtil.getSecretKey(KEY,256).getEncoded())
     *
     * @param key     AES字符串密钥
     * @param keySize 密钥长度（128、192、256）
     * @return 指定位数的AES密钥
     */
    private static SecretKeySpec getSecretKey(String key, int keySize) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        try {
            SecureRandom random = SecureRandom.getInstance(ALGORITHM);
            random.setSeed(key.getBytes());
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            kg.init(keySize, random);
            // 生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public static void main(String[] args) {
        // 密钥
        // E1a2 sha256 后取前32位，用于直接使用字符串aes密钥的情况，32位对应aes-256，24位对应aes-192，16位对应aes-128;
        // 如果是用getSecretKey方法再次生成指定位数密钥的，则这个字符串是几位都可以，这种方式下提供的前端的key应该是:
        // Base64.encodeBase64String(AESUtil.getSecretKey(KEY,256).getEncoded()) 得到Base64Url编码后的最终密钥
        // 前端通过CryptoJS.enc.Base64.parse(Base64Url编码后的最终密钥) 就可以得到这个密钥

        /*String key = "660a66d5f71bd3c1284a6c7a87fed08c";
        String content = "加密数据";
        System.out.println("加密：" + AESUtil.encryptBase64URL(key, content));
        System.out.println("解密：" + AESUtil.decryptBase64URL(key, AESUtil.encryptBase64URL(key, content)));*/

        String key = "660a66d5f71bd3c1284a6c7a87fed08c";
        String content = "lCwVYrD8d_vdNOg0QgcH-ZTkf2xxQuNGGbZ4xj3FGvZKRyxYN1rFikFzJhdWnvtUc6popF2G9NO6VqyQQwbLnQ";
        System.out.println("解密：" + AESUtil.decryptBase64URL(key, content));
    }

}


```



## 八、参考资料

1、前端预制rsa公钥，后端预制rsa私钥
2、前端动态生成aes秘钥，使用rsa公钥加密aes秘钥和登录信息发起登录请求
3、后端通过rsa私钥解密获取aes秘钥和登录信息
4、后端登录验证通过后使用aes秘钥加密账号信息返回给客户端，后端保存好aes秘钥
5、后续前后端通信都使用该aes秘钥加解密数据


后端使用
AESUtil.java
的encryptBase64URL和decryptBase64URL进行aes加解密就可以

RSAUtil.java
getKeyPair生成公私钥
getPrivateKey获取私钥字符串
getPublicKey获取公钥字符串
publicKeyEncrypt公钥加密数据
privateKeyDecrypt私钥解密数据



```txt
前后端接口加密通讯：
一、get请求方式处理：
get方式的如果以前是
api?aaa=1&bbb=2
这种的，统一改成
api?data=aes密文，从data字段拿到密文

二、post请求方式处理：
post方式，以前的requestbody密文传输，请求contentType仍使用application/json
请求报文格式为：{"encryptData":"原requestBody使用aesKey加密后的密文字符串"}，后端使用encryptData 获取密文解密。

三、url传参方式处理：
有些get和delete以前是用
/document/delete/id值
这种url方式传参的，不处理，按原来明文传输，结果密文响应

四、不进行加解密处理接口：
download,
/report/export,
/batchImport,
/etprInfo/find/password,
/document/add,
smsSendRecord/export

五、因加解密属全面请求拦截，测试在验证本期需求的时候，仍需要对服务主功能做一次全量冒烟（管理侧/企业侧全部页面功能（按钮）点一下）

文件上传/下载类接口、登录验证码、登录图形验证码接口不纳入加密通信范围，可以忽略
```



https://blog.csdn.net/temp_44/article/details/107762290
https://blog.csdn.net/zaige66/article/details/119411079
过滤链：https://blog.csdn.net/DuShiWoDeCuo/article/details/78913113

https://www.cnblogs.com/dhqz/p/12792456.html
https://www.jianshu.com/p/17256f94fd30



SecurityContextPersistenceFilter