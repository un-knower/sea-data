# 关键技术

## 主要技术

- [Scala](http://scala-lang.org/)：主力开发语言
- [Akka](https://akka.io/)：整个平台基于 Akka/Akka Cluster/Akka HTTP 构建，使用无中心节点的集群方式。每个子系统将分配一类集群角色。
- [HTML5](https://developer.mozilla.org/zh-CN/docs/Web/Guide/HTML/HTML5)：基于 mxgraph 提供图形化的业务编排能力

## 特性

- 流式ELT（数据处理），基于 Akka Stream 平台提供流式数据处理能力，对于大数量使用 Kafka 做数据缓冲。
  [Alpakka](https://github.com/akka/alpakka) 提供了大多数据源的流式读、写能力。同时，系统基于 Akka Stream 也可以很
  方便地集成其它数据源。提供高性能。
- 多实例、多集群、容错：每个子系统都采用集群方式（通过集群类不同的角色进行区分）部署。提供高可用、小部分节点挂掉不至造成
  整个平台不可服务。
- 可监控：平台提供完善的监控点，可快速定位系统故障，便于排查问题。
- 可扩展：模块式的系统架构，各子系统提供丰富的二次开发接口。采集组件可扩展，用户可方便的扩展系统的采集、数据转换、存储能力。
- 图形化业务编排：基于 HTML5 技术提供易用的图形化业务编排工具。
- 仿真测试：提供完整的仿真测试能力。在关键行业或生产环境特殊情况下，在开始和运维时可快捷的建立一个类生产的仿真测试环境。

## 要求和限制

### 操作系统

- 服务器：Linux x64（Ubuntu 16.04+ RHEL/CentOS 7+）
- 客户端：IE11+ / Chrome / Firefox
