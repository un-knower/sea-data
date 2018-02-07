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
  "project sea-scheduler" universal:packageZipTarball \
  "project sea-console" universal:packageZipTarball \
  "project sea-choreography" universal:packageZipTarball
```
