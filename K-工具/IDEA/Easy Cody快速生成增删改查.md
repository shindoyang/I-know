### 使用idea插件Easy Cody快速生成增删改查

1. 使用idea插件Easy Cody快速生成增删改查

打开工具栏点击File–>Settings–>Plugins 搜索Easy Cody

![image-20220614164238529](.\images\image-20220614164238529.png)

点击Install 下载2020年4月的Easy Code,下载之后重启IDEA；

 ![image-20220614164313489](.\images\image-20220614164313489.png)

单击打开Database，点击左上角加号

 ![image-20220614164342242](.\images\image-20220614164342242.png)

鼠标移动至Data Source选择你使用的数据库

 ![image-20220614164408784](.\images\image-20220614164408784.png)

填好数据库用户名和密码，URL必须和你idea使用的一样，否则会报错

 ![image-20220614164440854](.\images\image-20220614164440854.png)

创建成功右击选中表格选择EasyCode–》Generate Code

注意，下图的package也要指定，不然生成的类的第一行package 不准确

 ![image-20220614164514296](.\images\image-20220614164514296.png)

勾选成功点击ok快速生成各层代码(**增删改查**)

