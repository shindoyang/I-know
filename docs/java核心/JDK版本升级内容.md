#### JDK 1.5 

1. 泛型

   ArrayList list = new ArrayList();  -->    ArrayList<String>  list = new  ArrayList<String>();

2. 自动装箱、拆箱

   int i = list.get(0).parseInt();  ---> int i = list.get(0);

   原始类型与对应的包装类不用显式转换

3. for each

   i = 0; i< a.length; i++  ---->   for(int i:a){.....}

4. static import

   Math.sqrt();  ----> sqrt();

5. 变长参数

   int sum(int ...intlist)  有任意个参数，把他看作数组



#### JDK .16

1. 增强for循环语句

   Integer[]  numbers  = computeNumbers();

   for(int i =0; i< numbers.length; i++){

   ​	sum += numbers[i];

   }

   --->

   int sum = 0;

   for(int number : computeNumbers()){

   ​	sum += number;

   }

2. 监视和管理

   Java SE 6 对内存泄漏增强了分析以及诊断能力。当遇到 java.lang.OutOfMemory异常的时候，可以得到一个完整的堆栈信息，并且当堆已经满了的时候，会产生一个Log文件来记录这个致命错误。另外，JVM还添加了一个选项，允许你在堆满的时候运行脚本。

3. 插入式注解处理

   插入式注解处理API(JSR 269) 提供一套标准API来处理Annotations

4. 安全性



