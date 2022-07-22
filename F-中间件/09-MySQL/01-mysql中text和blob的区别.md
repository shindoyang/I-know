## Mysql中text 和 blob 字段类型的区别

### 一、BLOB

MySQL中，BLOB字段用于存储二进制数据，是一个可以存储大量数据的容器，它能容纳不同大小的数据。

#### MySQL的四种BLOB类型

| 类型       | 大小(单位：字节) |
| ---------- | ---------------- |
| TinyBlob   | 最大 255         |
| Blob       | 最大 65K         |
| MediumBlob | 最大 16M         |
| LongBlob   | 最大 4G          |

#### 将BLOB转化为VARCHAR

```mysql
select CAST(content AS CHAR(10000) CHARACTER SET utf8) from t_bonus_code_log
```

 ![image-20220722095052582](.\images\image-20220722095052582.png)

https://blog.csdn.net/qjc_501165091/article/details/51226018  原生的写入与读取

### 二、TEXT

#### 四种TEXT类型

| 类型       | 大小(单位：字节) |
| ---------- | ---------------- |
| TINYTEXT   | 最大 255         |
| TEXT       | 最大 65K         |
| MEDIUMTEXT | 最大 16M         |
| LONGTEXT   | 最大 4G          |

```tex
MySQL supports 4 TEXT field types (TINYTEXT, TEXT, MEDIUMTEXT and LONGTEXT) and this post looks at the maximum length of each of these field types.
MyISAM tables in MySQL have a maximum size of a row of 65,535 bytes, so all the data in a row must fit within that limit. However, the TEXT types are stored outside the table itself and only contribute 9 to 12 bytes towards that limit. (For more information about this refer to the MySQL Manual - Data Storage Requirements chapter). TEXT data types are also able to store much more data than VARCHAR and CHAR text types so TEXT types are what you need to use when storing web page or similar content in a database. The maximum amount of data that can be stored in each data type is as follows: TINYTEXT 256 bytes  
```



### 三、BLOB与TEXT的区别

一般在保存少量字符串的时候，我们会选择CHAR或者VARCHAR,而在保存较大文本时，通常会选择使用TEXT或者BLOB。二者之间的主要差别是BLOB能用来保存二进制数据，比如照片；而TEXT只能保存字符数据，比如一遍文章或日记。TEXT和BLOB中又分别包括TEXT,MEDIUMTEXT,LONGTEXT和BLOB,MEDIUMBLOB,LONGBLOB三种不同的类型，他们之间的主要区别是存储文本长度不用和存储字节不用，用户应该根据实际情况选择能够满足需求的最小存储类型。

BLOB和TEXT值会引起一些性能问题，特别是执行了大量的删除操作时。
删除操作会在数据库表中留下很大的“空洞”，以后要填入这些“空洞”的记录在插入的性能上会有影响。为了提高性能，建议定期使用OPTIMEIZE TABLE功能对这类表进行碎片整理，避免因为“空洞”导致性能问题。 

详细说明：https://blog.csdn.net/weixin_36910300/article/details/79104536

性能影响：https://blog.csdn.net/zhao_6666/article/details/79132285