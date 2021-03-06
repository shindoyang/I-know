servlet技术产生的背景：动态生成页面

![火狐截图_2020-10-02T12-33-45.550Z](images/servlet/火狐截图_2020-10-02T12-33-45.550Z.png)

http协议的特点

![火狐截图_2020-10-02T12-35-18.809Z](images/servlet/火狐截图_2020-10-02T12-35-18.809Z.png)

1、简单快速：定义了数据规范，键值对

2、灵活：content-type ： json  或者  text

3、无连接，http1.1  之前是短链接

4、无状态：不会保存上一次访问的状态

5、支持B/S 和 C/S 架构 ： 浏览器或者客户端都是同样的http协议



http协议请求格式

![火狐截图_2020-10-02T13-59-03.171Z](images/servlet/火狐截图_2020-10-02T13-59-03.171Z.png)



http响应格式：

![火狐截图_2020-10-02T22-18-22.489Z](images/servlet/火狐截图_2020-10-02T22-18-22.489Z.png)



http响应状态码：

![火狐截图_2020-10-02T22-22-39.168Z](images/servlet/火狐截图_2020-10-02T22-22-39.168Z.png)

![火狐截图_2020-10-02T22-24-34.088Z](images/servlet/火狐截图_2020-10-02T22-24-34.088Z.png)

---



自定义Tomcat架构图：

![火狐截图_2020-10-03T08-55-27.370Z](images/servlet/火狐截图_2020-10-03T08-55-27.370Z.png)



手写自定义的request对象：

![火狐截图_2020-10-03T08-32-22.021Z](images/servlet/火狐截图_2020-10-03T08-32-22.021Z.png)

自定义response 对象

![火狐截图_2020-10-03T08-38-45.547Z](images/servlet/火狐截图_2020-10-03T08-38-45.547Z.png)

自定义mapping

![火狐截图_2020-10-03T08-42-28.599Z](images/servlet/火狐截图_2020-10-03T08-42-28.599Z.png)

定义抽象servlet接口

![火狐截图_2020-10-03T08-50-42.971Z](images/servlet/火狐截图_2020-10-03T08-50-42.971Z.png)

servlet子类实现：

![火狐截图_2020-10-03T08-53-01.745Z](images/servlet/火狐截图_2020-10-03T08-53-01.745Z.png)

自定义MyTomcat服务的启动类：

![火狐截图_2020-10-03T09-05-28.798Z](images/servlet/火狐截图_2020-10-03T09-05-28.798Z.png)

启动MyTomcat服务

![火狐截图_2020-10-03T09-06-40.396Z](images/servlet/火狐截图_2020-10-03T09-06-40.396Z.png)



<完>

Tomcat的安装，注意lib目录下的jsp-api.lib 和servlet-api.jar 包，部署项目的时候，区别与传统项目的jar，web项目要打war包，并放在webapps目录下，即可部署成功。



servlet：小服务程序



![火狐截图_2020-10-06T09-48-33.368Z](images/servlet/火狐截图_2020-10-06T09-48-33.368Z.png)

![火狐截图_2020-10-06T09-50-49.841Z](images/servlet/火狐截图_2020-10-06T09-50-49.841Z.png)



![火狐截图_2020-10-06T09-54-03.069Z](images/servlet/火狐截图_2020-10-06T09-54-03.069Z.png)

![火狐截图_2020-10-06T09-55-27.929Z](images/servlet/火狐截图_2020-10-06T09-55-27.929Z.png)





![火狐截图_2020-10-06T09-57-27.910Z](images/servlet/火狐截图_2020-10-06T09-57-27.910Z.png)

![火狐截图_2020-10-06T10-55-33.968Z](images/servlet/火狐截图_2020-10-06T10-55-33.968Z.png)





![火狐截图_2020-10-06T11-01-19.196Z](images/servlet/火狐截图_2020-10-06T11-01-19.196Z.png)





![火狐截图_2020-10-06T12-27-25.066Z](images/servlet/火狐截图_2020-10-06T12-27-25.066Z.png)



认识servlet中的request和response，

request和response 对象是由容器封装的。后端服务按需从对象中获取所需参数即可。

![image-20201007165635025](images/servlet/image-20201007165635025.png)

![image-20201007170053552](images/servlet/image-20201007170053552.png)



![image-20201007171114107](images/servlet/image-20201007171114107.png)

![image-20201007210528880](images/servlet/image-20201007210528880.png)



![image-20201007211236410](images/servlet/image-20201007211236410.png)



![image-20201007211521992](images/servlet/image-20201007211521992.png)



![image-20201007212522921](images/servlet/image-20201007212522921.png)



![image-20201007213805726](images/servlet/image-20201007213805726.png)



![image-20201007213856395](images/servlet/image-20201007213856395.png)

![image-20201008020159909](images/servlet/image-20201008020159909.png)





![image-20201008032541673](images/servlet/image-20201008032541673.png)

![image-20201008032857047](images/servlet/image-20201008032857047.png)



![image-20201008034052843](images/servlet/image-20201008034052843.png)



![image-20201008035247794](images/servlet/image-20201008035247794.png)

request.getDispatcher("page").forword(request, response);



![image-20201008040200188](images/servlet/image-20201008040200188.png)



![image-20201008040530711](images/servlet/image-20201008040530711.png)

response.sendRediret("hello");



![image-20201008041753145](images/servlet/image-20201008041753145.png)



![image-20201008041809249](images/servlet/image-20201008041809249.png)



session的技术出现，解决不同请求之间数据共享，但是依赖与cookie

![image-20201008042349073](images/servlet/image-20201008042349073.png)

![image-20201008042417018](images/servlet/image-20201008042417018.png)



![image-20201008071154105](images/servlet/image-20201008071154105.png)



session ： 解决同一个用户的不同请求的数据共享问题

![image-20201008085644052](images/servlet/image-20201008085644052.png) 



![image-20201008090019303](images/servlet/image-20201008090019303.png)

![image-20201008090120792](images/servlet/image-20201008090120792.png)



![image-20201008093706165](images/servlet/image-20201008093706165.png)

![image-20201008100106009](images/servlet/image-20201008100106009.png)



servletContext ： 解决不同用户不同请求之间的数据共享问题

![image-20201008104339274](images/servlet/image-20201008104339274.png)

![image-20201008104446251](images/servlet/image-20201008104446251.png)

![image-20201008104638611](images/servlet/image-20201008104638611.png)

![image-20201008104951421](images/servlet/image-20201008104951421.png)

![image-20201008110507288](images/servlet/image-20201008110507288.png)

![image-20201008110940473](images/servlet/image-20201008110940473.png)

![image-20201009212618261](images/servlet/image-20201009212618261.png)

![image-20201009213328365](images/servlet/image-20201009213328365.png)

![image-20201009213516975](images/servlet/image-20201009213516975.png)

![image-20201009214052179](images/servlet/image-20201009214052179.png)



---

### JSP

![image-20201009214518451](images/servlet/image-20201009214518451.png)![image-20201009214647049](images/servlet/image-20201009214647049.png)

![image-20201009222000849](images/servlet/image-20201009222000849.png)

![image-20201009222133680](images/servlet/image-20201009222133680.png)

![image-20201009222250273](images/servlet/image-20201009222250273.png)

![image-20201009230340719](images/servlet/image-20201009230340719.png)



intellij IDEA 中，tomcat编译的文件的存放路径

![image-20201010062809859](images/servlet/image-20201010062809859.png)



jsp修改后，不需要重新启动服务就能在页面上看到发生的变化，因为jsp是第一次请求的时候才编译的，但是IDEA中要选中下面两个配置：

![image-20201010063334854](images/servlet/image-20201010063334854.png)



![image-20201010070727758](images/servlet/image-20201010070727758.png)

设置了pageEncoding之后，其实可以不用设置contentType属性



jsp里面嵌入java代码：

![image-20201011073946621](images/servlet/image-20201011073946621.png)

![image-20201011073444131](images/servlet/image-20201011073444131.png)



jsp页面的导入方式：

![image-20201011074102790](images/servlet/image-20201011074102790.png)

静态或动态导入页面

![image-20201011075423029](images/servlet/image-20201011075423029.png)

jsp页面的请求转发

![image-20201011075606752](images/servlet/image-20201011075606752.png)

![image-20201011081825715](images/servlet/image-20201011081825715.png)



jsp九大内置对象

![image-20201011081854885](images/servlet/image-20201011081854885.png)

生成的servlet文件中service方法可以看到这九个内置对象：

![image-20201011084007886](images/servlet/image-20201011084007886.png)

![image-20201011174943823](images/servlet/image-20201011174943823.png)



四大作用域

![image-20201011175304379](images/servlet/image-20201011175304379.png)



路径问题：

![image-20201011180815780](images/servlet/image-20201011180815780.png)



EL表达式：

![image-20201011210531899](images/servlet/image-20201011210531899.png)

![image-20201011210610415](images/servlet/image-20201011210610415.png)

![image-20201011210657535](images/servlet/image-20201011210657535.png)



EL表达式的概念：

![image-20201011210925190](images/servlet/image-20201011210925190.png)

![image-20201011211006713](images/servlet/image-20201011211006713.png)

![image-20201011211429784](images/servlet/image-20201011211429784.png)

![image-20201011213921525](images/servlet/image-20201011213921525.png)

![image-20201011214405323](images/servlet/image-20201011214405323.png)

![image-20201011220205360](images/servlet/image-20201011220205360.png)



JSTL的基本介绍和导入

![image-20201011220917307](images/servlet/image-20201011220917307.png)

背景:

如果只是EL表示式，要处理较复杂的逻辑的时候，比如if  else 或者for 循环 这些，又需要在jsp页面中写入java代码，这样页面可读性就比较差了。这时就出现了JSTL标签

![image-20201011222531202](images/servlet/image-20201011222531202.png)

![image-20201011222557022](images/servlet/image-20201011222557022.png)



配置属性

![image-20201013054938345](images/servlet/image-20201013054938345.png)

![image-20201013055051307](images/servlet/image-20201013055051307.png)



![image-20201013055309376](images/servlet/image-20201013055309376.png)

![image-20201013055339175](images/servlet/image-20201013055339175.png)



![image-20201013055850625](images/servlet/image-20201013055850625.png)

![image-20201013055925096](images/servlet/image-20201013055925096.png)

![image-20201013060155190](images/servlet/image-20201013060155190.png)



![image-20201013061234630](images/servlet/image-20201013061234630.png)



标签学习：菜鸟教程

![image-20201013061943472](images/servlet/image-20201013061943472.png)





---

servlet过滤器

场景：设置请求编码集，可以在每个servlet里面设置，但是要重复多次，可以使用同一代码

![image-20201013063324834](images/servlet/image-20201013063324834.png)

![image-20201013214503115](images/servlet/image-20201013214503115.png)

![image-20201013215703428](images/servlet/image-20201013215703428.png)

![image-20201013215910203](images/servlet/image-20201013215910203.png)

![image-20201013221734205](images/servlet/image-20201013221734205.png)



![image-20201013223205395](images/servlet/image-20201013223205395.png)

![image-20201013224430454](images/servlet/image-20201013224430454.png)

![image-20201013225329880](images/servlet/image-20201013225329880.png)

![image-20201013225441678](images/servlet/image-20201013225441678.png)

![image-20201013232842299](images/servlet/image-20201013232842299.png)

![image-20201014055640700](images/servlet/image-20201014055640700.png)

![image-20201014055951058](images/servlet/image-20201014055951058.png)

![image-20201014060614101](images/servlet/image-20201014060614101.png)

![image-20201014061609915](images/servlet/image-20201014061609915.png)

![image-20201014062633593](images/servlet/image-20201014062633593.png)

![image-20201014063337683](images/servlet/image-20201014063337683.png)



监听网站在线人数：

![image-20201014233534367](images/servlet/image-20201014233534367.png)

![image-20201014233740106](images/servlet/image-20201014233740106.png)

![image-20201014233832972](images/servlet/image-20201014233832972.png)