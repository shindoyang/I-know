maven的聚合：

* 父子项目既可以是平行，也可以是嵌套的形式
* 





打包上传私服：

```yaml
  <distributionManagement>
                <repository>
                        <id>Nexus</id>
                        <name>Releases</name>
                        <url>https://nexus.utcook.com/repository/maven-releases</url>
                </repository>
                <snapshotRepository>
                        <id>Nexus</id>
                        <name>Snapshot</name>
                        <url>https://nexus.utcook.com/repository/maven-snapshots</url>
                </snapshotRepository>
        </distributionManagement>
```

