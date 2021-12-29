# 一、配置私服
## 1、配置本地maven 的settings文件
### 1.1、配置远程仓库
```xml
    <mirrors>
	<!--
	<mirror>
		<id>aliyunmaven</id>
		<mirrorOf>*</mirrorOf>
		<name>阿里云公共仓库</name>
		<url>https://maven.aliyun.com/repository/public</url>
	</mirror>
	-->
	<mirror> 
		<id>nexus-ut</id> 
		<mirrorOf>central</mirrorOf> 
		<name>Nexus ut</name>
		<url>https://nexus.utcook.com/repository/maven-public/</url> 
	</mirror>
  </mirrors>
```
### 1.2、配置私服访问用户名密码
```xml
  <servers>
	<server>
      <id>Nexus</id>
      <username>admin</username>
      <password>{password}</password>
    </server>
	
    <server>
      <id>mavenId</id>
      <username>admin</username>
      <password>{password}</password>
    </server>

    <server>
      <id>dockerId</id>
      <username>docker</username>
      <password>{password}</password>
    </server>
  </servers>
```

## 2、修改项目pom文件
### 2.1、在各自项目对应的pom.xml文件下加入docker打包插件
```xml
<!--dockerfile -->
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<fork>true</fork><!-- 如果没有该项配置，devtools不会起作用，即应用不会restart -->
				</configuration>
			</plugin>
			<plugin>
				<groupId>com.spotify</groupId>
				<artifactId>docker-maven-plugin</artifactId>
				<version>1.0.0</version>
				<configuration>
					<dockerHost>http://192.168.105.71:2375</dockerHost>
					<serverId>dockerId</serverId>
					<imageName>${dockerImageUrl}</imageName>
					<baseImage>openjdk:8-jdk-alpine</baseImage>
					<!--					<entryPoint>["java", "-Xdebug", "-Xnoagent", "-Djava.compiler=NONE", "-Xrunjdwp:transport=dt_socket,address=5009,server=y,suspend=n", "-Duser.timezone=GMT+08","-Djava.security.egd=file:/dev/./urandom", "-jar", "/${project.build.finalName}.jar"]</entryPoint>-->
					<!--参考 【*】修复服务占用1号进程问题 文章说明-->
					<entryPoint>echo "java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n -Duser.timezone=GMT+08 -Djava.security.egd=file:/dev/./urandom -jar /${project.build.finalName}.jar" > start.sh &amp;&amp; chmod 777 start.sh &amp;&amp; ./start.sh</entryPoint>
					<resources>
						<resource>
							<targetPath>/</targetPath>
							<directory>${project.build.directory}</directory>
							<include>${project.build.finalName}.jar</include>
						</resource>
					</resources>
				</configuration>
				<executions>
					<execution>
						<phase>install</phase>
						<goals>
							<goal>build</goal>
						</goals>
					</execution>
					<execution>
						<id>tag-image</id>
						<phase>install</phase>
						<goals>
							<goal>tag</goal>
						</goals>
						<configuration>
							<image>${dockerImageUrl}</image>
							<newName>
								${dockerImageUrl}
							</newName>
						</configuration>
					</execution>
					<execution>
						<id>push-image</id>
						<phase>install</phase>
						<goals>
							<goal>push</goal>
						</goals>
						<configuration>
							<imageName>
								${dockerImageUrl}
							</imageName>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
```
### 2.2、添加nexus配置
```xml
	<repositories>
		<repository>
			<id>nexus</id>
			<name>Nexus Repository</name>
			<url>https://nexus.utcook.com/repository/maven-public/</url>
			<releases>
				<enabled>true</enabled>
			</releases>
			<!--snapshots默认是关闭的,需要开启  -->
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</repository>
	</repositories>

	<pluginRepositories>
		<pluginRepository>
			<id>central</id>
			<name>CentralRepository</name>
			<url>https://nexus.utcook.com/repository/maven-public/</url>
			<layout>default</layout>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
			<releases>
				<updatePolicy>never</updatePolicy>
			</releases>
		</pluginRepository>
	</pluginRepositories>
```