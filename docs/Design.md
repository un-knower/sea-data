# 设计思路

使用 Akka Cluster 来实现整个数据平台的集群化。不同的系统用 member role 来体现。

- 监管系统 Role: console
- 调度系统 Role: scheduler
- 引擎     Role: engine
- 通信总线 Role: ipc（？还需要吗？）

- 编排系统 Role: choreography
