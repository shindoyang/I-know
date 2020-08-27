## String 和StringBuffer StringBuilder



list中可以存储null值

这个问题其实可以说是非常典型的一个问题了，建议去看一下数据结构的基础知识。
这个问题的关键在于，List中存储的每个元素本质上是一个地址指针，它指向了一个对象的存储位置。
当你向List中添加null的时候，就相当于在List中添加了一个没有指向任何有效对象的指针或者说引用，换言之，就是先占了一个位置。
随后，你可以将这个位置的引用换成一个有效的引用。
至于你这里输出结果中打印的结果是"null"，作为字符串出现。只是因为`System.out.println`调用了`String.valueOf()`，后者的代码是

```
 public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
 }
```

也就是说，你的List中本来元素是null,但是输出的时候被转化成了"null"这样的一个字符串。



### java 8

#### lambda表达式

#### stream Api

#### 自定义注解

##### 元注解

@Target 作用的对象范围，必填字段

@Retention 作用的时机，必填字段

@Documented 是否在jdk文档中显示

@Inherited 表示该注解是否可以被继承





