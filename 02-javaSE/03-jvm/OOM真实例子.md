#### 批量查询接口

接口逻辑没有对单次调用上限做限制，客户一次请求上万条，多次调用，导致频繁FGC，最终导致OOM



#### 批量下载/导出

数据量很大的excel或者csv，用poi/easypoi都有弊病，会把数据加载到内存里

可以实时阿里的easyexcel，但听说easyexcel也是基于poi，而且easyexcel不支持数据原始类型。

其实用poi的event模型就可以了。



