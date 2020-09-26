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

   