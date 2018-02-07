import Commons._
import Dependencies._
import com.typesafe.sbt.SbtMultiJvm.multiJvmSettings
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

lazy val root = Project(id = "sea-data-root", base = file("."))
  .aggregate(
    seaDocs,
    seaFunctest,
    seaConsole,
    seaIPCConsole,
    seaScheduler,
    seaEngine,
    seaData,
    seaCoreServer,
    seaIPC,
    seaCore
  )
  .settings(Publishing.noPublish: _*)
  .settings(Environment.settings: _*)

lazy val seaDocs = _project("sea-docs")
  .enablePlugins(AkkaParadoxPlugin)
  .dependsOn(
    seaFunctest, seaConsole, seaScheduler, seaChoreography, seaEngine, seaIPCConsole,
    seaCoreServer % "compile->compile;test->test",
    seaIPC % "compile->compile;test->test",
    seaCore % "compile->compile;test->test"
  )
  .settings(
    name in(Compile, paradox) := "SeaData",
    paradoxTheme := Some(builtinParadoxTheme("generic")),
    paradoxProperties ++= Map(
      "github.base_url" -> s"https://github.com/yangbajing/sea-data/tree/${version.value}",
      "scala.version" -> scalaVersion.value,
      "scala.binary_version" -> scalaBinaryVersion.value,
      "scaladoc.akka.base_url" -> s"http://doc.akka.io/api/$versionAkka",
      "akka.version" -> versionAkka
    )
  )

lazy val seaFunctest = _project("sea-functest")
  .dependsOn(seaConsole, seaScheduler, seaChoreography, seaEngine, seaIPCConsole,
    seaCoreServer % "compile->compile;test->test",
    seaIPC % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .enablePlugins(MultiJvmPlugin)
  .configs(MultiJvm)
  .settings(Publishing.noPublish: _*)
  .settings(
    jvmOptions in MultiJvm := Seq("-Xmx256M"),
    libraryDependencies ++= Seq(
      _akkaMultiNodeTestkit
    ) //++ _kamons
  )

// 监查、控制、管理
lazy val seaConsole = _project("sea-console")
  .dependsOn(
    seaCoreServer % "compile->compile;test->test",
    seaIPC % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(Packaging.settings: _*)
  .settings(Publishing.noPublish: _*)
  .settings(
    mainClass in Compile := Some("seadata.console.boot.ConsoleBoot"),
    libraryDependencies ++= Seq(

    )
  )

lazy val seaChoreography = _project("sea-choreography")
  .dependsOn(
    seaCoreServer % "compile->compile;test->test",
    seaIPC % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(Packaging.settings: _*)
  .settings(Publishing.noPublish: _*)
  .settings(
    mainClass in Compile := Some("seadata.choreography.boot.ChoreographyBoot"),
    libraryDependencies ++= Seq(

    )
  )

// 进程间通信、服务发现、消息路由 管理控制台程序
lazy val seaIPCConsole = _project("sea-ipc-console")
  .dependsOn(
    seaCoreServer % "compile->compile;test->test",
    seaIPC % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(Packaging.settings: _*)
  .settings(Publishing.noPublish: _*)
  .settings(
    mainClass in Compile := Some("seadata.ipc.console.boot.IPCConsoleBoot"),
    libraryDependencies ++= Seq(

    )
  )

// 调度
lazy val seaScheduler = _project("sea-scheduler")
  .enablePlugins(JavaAppPackaging)
  .dependsOn(
    seaCoreServer % "compile->compile;test->test",
    seaIPC % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .settings(Publishing.noPublish: _*)
  .settings(
    mainClass in Compile := Some("seadata.scheduler.boot.SchedulerBoot"),
    libraryDependencies ++= Seq(
      _quartz
    )
  )

// 执行引擎
lazy val seaEngine = _project("sea-engine")
  .dependsOn(
    seaCoreServer % "compile->compile;test->test",
    seaIPC % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(Packaging.settings: _*)
  .settings(Publishing.noPublish: _*)
  .settings(
    mainClass in Compile := Some("seadata.engine.boot.EngineBoot"),
    libraryDependencies ++= Seq(

    )
  )

// 数据组件（采集、存储）
lazy val seaData = _project("sea-data")
  .settings(Publishing.noPublish: _*)
  .dependsOn(
    seaCoreServer % "compile->compile;test->test",
    seaIPC % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .settings(
    test in assembly := {},
//    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
    libraryDependencies ++= Seq(
      _akkaStreamKafka,
      _postgresql
    ) ++ _alpakkas
  )

// 管理程序公共库
lazy val seaCoreServer = _project("sea-core-server")
  .settings(Publishing.publishing: _*)
  .dependsOn(seaIPC, seaCore % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      _guice
    ) ++ _akkaHttps
  )

// 进程间通信、服务发现、消息路由 公共库
lazy val seaIPC = _project("sea-ipc")
  .dependsOn(
    seaCore % "compile->compile;test->test")
  .settings(Publishing.publishing: _*)
  .settings(
    libraryDependencies ++= Seq(
      _protobuf
    ) ++ _akkaClusters
  )

lazy val seaCore = _project("sea-core")
  .settings(Publishing.publishing: _*)
  .settings(
    libraryDependencies ++= Seq(
      _swaggerAnnotation % Provided,
      _hikariCP,
      _scalaLogging,
      _logbackClassic,
      _config,
      _scalaXml,
      "org.scala-lang" % "scala-library" % scalaVersion.value,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      _scalatest % Test
    ) ++ _jacksons ++ _akkas
  )

def _project(name: String, _base: String = null) =
  Project(id = name, base = file(if (_base eq null) name else _base))
    .settings(basicSettings: _*)
