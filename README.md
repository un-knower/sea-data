# Sea Data

海量数据处理平台

- Java 8+
- Scala 2.12+

## 文档

```
./sbt sea-docs/paradox
google-chrome sea-docs/target/paradox/site/main/index.html
```

## 编译，构建

```
./sbt -Dbuild.env=prod "project sea-console" universal:packageZipTarball  \
  "project sea-data" assembly \
  "project sea-engine" universal:packageZipTarball \
  "project sea-console" universal:packageZipTarball \
```

## 项目结构

- [sea-docs](sea-docs)：使用 [Lightbend Paradox](https://developer.lightbend.com/docs/paradox/latest/) 编写的文档。在线访问地址：[http://www.yangbajing.me/sea-data/doc/](http://www.yangbajing.me/sea-data/doc/)
- [sea-functest](sea-functest)：功能测试项目，支持 multi-jvm 测试（单机上启动多个JVM实例模拟集群）
- [sea-console](sea-console)：管理控制台、监控、业务流程编排
- [sea-broker](sea-broker)：Sea 执行Broker节点
- [sea-core-ext](sea-core-ext)：Sea 核心库扩展
- [sea-ipc](sea-pic)：Sea 通信协议、规范 ？需要
- [sea-core](sea-core)：Sea 核心库
- [sea-common](sea-common)：一些工具类、库

## 开发

**开始、运行 multi-jvm 测试：NodesTest**

```
git clone https://github.com/yangbajing/sea-data
cd sea-data
> sea-functest/multi-jvm:testOnly seadata.functest.NodesTest
```
