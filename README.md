
[官方文档](https://baomidou.com)

[源码-v3.5.2](https://github.com/baomidou/generator)

## init

源码是一个单体项目`mybatis-plus-generator`,是基于gradle进行打包的，参考build.gradle新建pom文件；同时更新readme。

## 工作流程

根据**用户配置**从数据库中**查询表、字段信息**存入**模板引擎上下文信息**

- **用户配置**  基于构建者模式、消费者模式将六种配置统一到`ConfigBuilder`：<br>
`DataSourceConfig 数据源配置` `GlobalConfig 全局配置` `PackageConfig 包配置` `InjectionConfig 注入配置` `TemplateConfig 模板配置` `StrategyConfig 库表配置` 
- **查询表、字段信息**  `ConfigBuilder`提供接口查询数据表、字段信息；目前有元数据查询、SQL查询两种实现。
- **模板引擎上下文信息**  存放在一个Map中，注入配置提供了设置自定义上下文信息的接口和Consumer函数

主流程相关函数  `AutoGenerator.execute()` `AbstractTemplateEngine.batchOutput()`

`ConfigBuilder`查询接口实现  元数据查询`DefaultQuery.queryTables()` `DefaultQuery.convertTableFields()`

SQL查询`SQLQuery.queryTables()` `SQLQuery.convertTableFields()`

## 配置信息

参考[官方文档](https://baomidou.com)及代码示例