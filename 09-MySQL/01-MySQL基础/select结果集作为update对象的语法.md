### 需求：把my_user_relate_authority_groups(用户角色关系表)中用户id和权限key颠倒的数据修复。

实现：权限key是以appkey_为前缀，可以通过这个条件来过滤出用户id列哪些数据是错误的权限key，再将这些数据修正。

1. 语句1，实现数据过滤，找出需要修正的数据

```sql
SELECT * FROM my_user_relate_authority_groups WHERE app_key != SUBSTRING(authority_group_key , 1 , LOCATE('_',authority_group_key)-1) ;
```

2. 语句2，修正表数据。下面的sql是伪sql，直接执行会把uid的值覆盖掉auth_group_key的值。所以目标是要把下面语句中的my_user_relate_authority_groups换位第一步排查出的结果集。

```sql
update  my_user_relate_authority_groups a ,my_user_relate_authority_groups b set a.authority_group_key = b.user_uid,a.user_uid=b.authority_group_key WHERE a.id = b.id
```

3. 语句3，两句sql合并后的效果：

```sql
update  
	(SELECT * FROM my_user_relate_authority_groups WHERE app_key != SUBSTRING(authority_group_key , 1 , LOCATE('_',authority_group_key)-1)) a ,
	(SELECT * FROM my_user_relate_authority_groups WHERE app_key != SUBSTRING(authority_group_key , 1 , LOCATE('_',authority_group_key)-1)) b 
	set a.authority_group_key = b.user_uid,a.user_uid=b.authority_group_key 
	WHERE a.id = b.id
```

但很不幸，上述sql语句无法执行，报语法错误。



查阅资料后，发现可通过下面方式实现：

* 这样是不行的

```sql
UPDATE `table_name` 
SET `column_1` = value 
WHERE `column_2` IN 
(SELECT * FROM `table_name` 
    WHERE `column` = value
) 
```

* 必须得这样

```sql
UPDATE `table_name` AS alias_1
 INNER JOIN
 (SELECT * FROM `table_name` WHERE `column` = value) AS alias_2 
SET alias_1.column_1 = value 
WHERE alias_1.id = alias_2.id
```



所以，修改上述语句为：

```sql
update  my_user_relate_authority_groups as a inner join (SELECT * FROM my_user_relate_authority_groups WHERE app_key != SUBSTRING(authority_group_key , 1 , LOCATE('_',authority_group_key)-1) ) as  b set a.authority_group_key = b.user_uid,a.user_uid=b.authority_group_key WHERE a.id = b.id
```

执行成功，检查也发现修改的结果集符合预期。

