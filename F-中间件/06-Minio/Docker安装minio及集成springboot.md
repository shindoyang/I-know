## Docker 安装Minio 及 集成springboot

参考：

[Docker 搭建 Minio 容器 (完整详细版)](https://blog.csdn.net/BThinker/article/details/125412751)

[SpringBoot整合MinIO 「看这一篇就够了」](https://blog.csdn.net/Darling_qi/article/details/124743303)

### 简介：

>Minio 是一个基于Apache License v2.0开源协议的对象存储服务，虽然轻量，却拥有着不错的性能。它兼容亚马逊S3云存储服务接口，非常适合于存储大容量非结构化的数据。 
>
>例如图片、视频、日志文件、备份数据和容器/虚拟机镜像等，而一个对象文件可以是任意大小，从几 kb 到最大 5T 不等。

### 说明：

> [Docker](https://so.csdn.net/so/search?q=Docker&spm=1001.2101.3001.7020)如果想安装软件 , 必须先到 [Docker](https://so.csdn.net/so/search?q=Docker&spm=1001.2101.3001.7020) 镜像仓库下载镜像。

[Docker官方镜像仓库](https://hub.docker.com/)

### 1、寻找Minio镜像 

![img](.\images\0dd77e19b8824b618a9ce72f92d55ce3.png)

![img](.\images\8bb586b98b3b441a86c1f3ec137eea04.png)

###  2、下载Minio镜像

| 命令                                                      | 描述                                                         |
| --------------------------------------------------------- | ------------------------------------------------------------ |
| docker pull minio/minio                                   | 下载最新版Minio镜像 (其实此命令就等同于 : docker pull minio/minio:latest ) |
| docker pull minio/minio:RELEASE.2022-06-20T23-13-45Z.fips | 下载指定版本的Minio镜像 (xxx指具体版本号)                    |

![img](.\images\13fe86f030f54aa58d0217d51cfd3944.png)

检查当前所有[Docker](https://so.csdn.net/so/search?q=Docker&spm=1001.2101.3001.7020)下载的镜像

```shell
docker images
```

###  3、创建目录

> 一个用来存放配置，一个用来存储上传文件的目录
>
> 启动前需要先创建Minio外部挂载的配置文件（ /home/minio/config）,和存储上传文件的目录（ /home/minio/data）

```shell
mkdir -p /home/minio/config
mkdir -p /home/minio/data
```

### 4、创建Minio容器并运行

> 多行模式

```shell
docker run -p 9000:9000 -p 9090:9090 \
     --net=host \
     --name minio \
     -d --restart=always \
     -e "MINIO_ACCESS_KEY=minioadmin" \
     -e "MINIO_SECRET_KEY=minioadmin" \
     -v /home/minio/data:/data \
     -v /home/minio/config:/root/.minio \
     minio/minio server \
     /data --console-address ":9090" -address ":9000"
```

> 单行模式

```shell
docker run -p 9000:9000 -p 9090:9090      --net=host      --name minio      -d --restart=always      -e "MINIO_ACCESS_KEY=minioadmin"      -e "MINIO_SECRET_KEY=minioadmin"      -v /home/minio/data:/data      -v /home/minio/config:/root/.minio      minio/minio server      /data --console-address ":9090" -address ":9000"

```

>9090端口指的是minio的客户端端口
>
>MINIO_ACCESS_KEY ：账号
>
>MINIO_SECRET_KEY ：密码（账号长度必须大于等于5，密码长度必须大于等于8位）

![img](.\images\6d317cc41fca4e64b8ef14398dd20459.png)

### 5、访问操作

访问：http://123.57.131.180:9090/login 用户名：密码 minioadmin：minioadmin

![image-20220810163804327](.\images\image-20220810163804327-16601206859455.png)

#### 创建用户

![img](.\images\85d71387666f4c8eae7cf5d1377030e0.png)

![img](.\images\51df36c96dc243f486fee70a28cb2d0f.png)

![img](.\images\9e53f48a825e45b9aadb877d46fe7b30.png)

#### 创建组

![img](.\images\8db2fcd2c58d4b818724b7fdedb801a1.png)

![img](.\images\f3630549378843a0862835e3ac56b995.png)

#### 创建accessKey和secretKey 

![img](.\images\7e45174f035d478eb2403ebc727cadf4.png)

![img](.\images\e3beea9516374cd9a282e5381a2a5339.png)

点击下载

![img](.\images\7a23b700366344daa14679d920591d12.png)

文件内容如下，保存文件，SDK操作文件的[API](https://so.csdn.net/so/search?q=API&spm=1001.2101.3001.7020)需要用到

```json
{"url":"http://192.168.124.132:9000","accessKey":"XO1JDovW2FTmGaBb","secretKey":"uG6wMfylUnOVH5WzwxqnldOWw2dMshNX","api":"s3v4","path":"auto"}
```

#### 创建Bucket

![img](.\images\1a434159f1c54d9fbaf6cef41d8d83e4.png)

![img](.\images\4c90c360157740019aabac2cef1e649d.png)

![img](.\images\76e1bd28a0fd42bfa10621bd4ebf7010.png)

#### 上传文件

![img](.\images\58986f3a0d4142d18eee98b3cbf37459.png)

![img](.\images\6dd2bf16650a48b9b287dc40f2d047bc.png)

![img](.\images\c429a4ebb28346558f03a900d9a93aeb.png)

### 6、SDK操作

官方文档：https://docs.min.io/docs/

![img](.\images\c63ec4bb5a0d42e69995eaf05f38c900.png)

 javaSDK：https://docs.min.io/docs/java-client-quickstart-guide.html

![img](.\images\fbb290153ad440c685a0b71c7beb73d2.png)

maven依赖

```xml
<!-- #低版本的okhttp会报错提示 -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.9.0</version>
</dependency>

<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.4.2</version>
    <exclusions>
        <exclusion>
            <artifactId>okhttp</artifactId>
            <groupId>com.squareup.okhttp3</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

![img](.\images\b1c21dff2b0544f6b0f5dfd820cfe324.png)

测试文件上传 

```java
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
 
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
 
public class FileUploader {
 
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://123.57.131.180:9000")
                            .credentials("XO1JDovW2FTmGaBb", "uG6wMfylUnOVH5WzwxqnldOWw2dMshNX")
                            .build();
 
            // Make 'asiatrip' bucket if not exist.
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket("public").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("public").build());
            } else {
                System.out.println("Bucket 'public' already exists.");
            }
 
            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("public")
                            .object("credentials.json")
                            .filename("C:/Users/lai.huanxiong/Downloads/credentials.json")
                            .build());
            System.out.println("'C:/Users/lai.huanxiong/Downloads/credentials.json' is successfully uploaded as " + "object 'credentials.json' to bucket 'public'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}
```

![img](.\images\3e55cf31a46d4c4aab03ebdb0ddc542d.png)

文件上传成功展示

![img](.\images\6e5a403aad544764a442ac0da3a94cb4.png)

至此，[Docker](https://so.csdn.net/so/search?q=Docker&spm=1001.2101.3001.7020)搭建Minio服务器和简单操作完成！！！

### 7、Springboot整合Minio

##### maven依赖

见6、sdk操作

##### application.yml 配置信息

```yaml
minio:
  endpoint: http://123.57.131.180:9000 #Minio服务所在地址
  bucketName: gdqxj #存储桶名称
  accessKey: yanggeng #访问的key
  secretKey: 2fMzxPtElqon #访问的秘钥
```

##### MinioConfig.class配置类

```java
package com.gdqxj.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
```

##### minio工具类

```java
package com.gdqxj.utils;

import com.gdqxj.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MinioUtil {
    @Autowired
    private MinioConfig prop;

    @Resource
    private MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     *
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     *
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new RuntimeException();
        }
        /*String fileName = IdUtils.getSnowId() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = DateUtil.getShotName().getNowDateLongStr("yyyy-MM/dd") + "/" + fileName;*/

        String fileName = originalFilename;
        //String objectName = LocalDateTime.now().format(dateTimeFormatter)+ "/" + fileName;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String objectName = LocalDateTime.now().format(dateTimeFormatter) + "/" + fileName;

        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objectName;
    }

    /**
     * 预览图片
     *
     * @param fileName
     * @return
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder().bucket(prop.getBucketName()).object(fileName).method(Method.GET).build();
        try {
            String url = minioClient.getPresignedObjectUrl(build);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     * @param res      response
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("utf-8");
                // 设置强制下载不打开
                // res.setContentType("application/force-download");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(prop.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    /**
     * 删除
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
```

##### 文件处理接口

```java
package com.gdqxj.controller;


import com.gdqxj.bean.DataResult;
import com.gdqxj.config.MinioConfig;
import com.gdqxj.utils.MinioUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.minio.messages.Bucket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "文件相关接口")
@Slf4j
@RestController
@RequestMapping(value = "/file")
@ApiSort(6)
@ApiIgnore
public class FileController {

    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioConfig prop;

    @ApiOperation(value = "查看存储bucket是否存在")
    @GetMapping("/bucketExists")
    public DataResult bucketExists(@RequestParam("bucketName") String bucketName) {
        return DataResult.success(minioUtil.bucketExists(bucketName));
    }

    @ApiOperation(value = "创建存储bucket")
    @GetMapping("/makeBucket")
    public DataResult makeBucket(String bucketName) {
        return DataResult.success(minioUtil.makeBucket(bucketName));
    }

    /**
     * 只能删除空的存储bucket，如果bucket已经上传了图片，删除失败
     *
     * @param bucketName
     * @return
     */
    @ApiOperation(value = "删除存储bucket")
    @GetMapping("/removeBucket")
    public DataResult removeBucket(String bucketName) {
        return DataResult.success(minioUtil.removeBucket(bucketName));
    }

    @ApiOperation(value = "获取全部bucket")
    @GetMapping("/getAllBuckets")
    public DataResult getAllBuckets() {
        List<Bucket> allBuckets = minioUtil.getAllBuckets();
        return DataResult.success(allBuckets);
    }

    @ApiOperation(value = "文件上传返回url")
    @PostMapping("/upload")
    public DataResult upload(@RequestParam("file") MultipartFile file) {
        String objectName = minioUtil.upload(file);
        if (null != objectName) {
            return DataResult.success(prop.getEndpoint() + "/" + prop.getBucketName() + "/" + objectName);
        }
        return DataResult.fail("文件上传失败！");
    }

    @ApiOperation(value = "图片/视频预览")
    @GetMapping("/preview")
    public DataResult preview(@RequestParam("fileName") String fileName) {
        return DataResult.success(minioUtil.preview(fileName));
    }

    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    public DataResult download(@RequestParam("fileName") String fileName, HttpServletResponse res) {
        minioUtil.download(fileName, res);
        return DataResult.success();
    }

    @ApiOperation(value = "删除文件", notes = "根据url地址删除文件")
    @PostMapping("/delete")
    public DataResult remove(String url) {
        String objName = url.substring(url.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
        minioUtil.remove(objName);
        return DataResult.success();
    }

}
```

##### 接口测试

上传：

![image-20220810171937352](.\images\image-20220810171937352-166012317934925.png)

 下载：

 ![image-20220810172008330](.\images\image-20220810172008330-166012320960226.png)

预览：

![image-20220810172104237](.\images\image-20220810172104237-166012326546827.png)

![image-20220810172125032](.\images\image-20220810172125032-166012328690128.png)