## mysqldump 数据库备份

mysqldump命令是常用的数据导入导出命令，详细使用说明如下：

---

### 简单使用

#### 备份数据库(概述)

```mysql
# mysqldump 数据库名 > 数据库备份名
# mysqldump -A -u 用户名 -p 密码  数据库名 >  数据库备份名
# mysqldump -d -A --add-drop-table -uroot -p > xxx.sql
```

#### 导出结构不导出数据

```mysql
# mysqldump --opt -d 数据库名 -u root -p > xxx.sql
```

#### 导出数据不导出结构

```mysql
# mysqldump -t 数据库名 -uroot -p > xxx.sql
```

#### 导出数据和表结构

```mysql
# mysqldump 数据库名 -uroot -p > xxx.sql
```

#### 导出特定表的结构

```mysql
# mysqldump -uroot -p -B 数据库名 --table 表名 > xxx.sql
```

#### 导入数据

由于mysqldump导出的是完整的SQL语句，所以用mysql客户程序很容易就能把数据导入了：

```mysql
# mysql 数据库名 < 文件名
# source /temp/xxx.sql
```

---

### 详细使用

#### 锁表导出完整的数据库备份

```mysql
# mysqldump -h127.0.0.1 -p3306 -uroot -ppassword --add-locks -q dbname > dbname.sql
```

**说明：**--add-locks  导出过程中锁表，完成后会解锁。-q : 不缓冲查询，直接导出至标准输出。

#### 锁表导出完整的数据库表结果

```mysql
# mysqldump -h127.0.0.1 -p3306 -uroot -ppassword --add-locks -q -d dbname > dbname.sql
```

**说明：** -d  只导出表结构，不含数据

#### 导出完整的数据库的数据不含表结构

```mysql
# mysqldump -h127.0.0.1 -p3306 -uroot -ppassword --add-lock -q -t dbname.user > dbanme.sql
```

**说明：** -t 只导出数据，不含表结构





