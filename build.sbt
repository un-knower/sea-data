import Commons._
import Dependencies._
import com.typesafe.sbt.SbtMultiJvm.multiJvmSettings
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

lazy val root = Project(id = "sea-data-root", base = file("."))
  .aggregate(
    seaDocs,
    seaFunctest,
    seaConsole,
    seaBroker,
    seaData,
    seaCoreExt,
    seaCore,
    seaCommon
  )
  .settings(Publishing.noPublish: _*)
  .settings(Environment.settings: _*)

lazy val seaDocs = _project("sea-docs")
  .enablePlugins(ParadoxPlugin)
  .dependsOn(
    seaFunctest, seaConsole, seaBroker,
    seaCoreExt % "compile->compile;test->test",
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
    ))
      .settings(Publishing.noPublish: _*)

lazy val seaFunctest = _project("sea-functest")
  .dependsOn(seaConsole, seaBroker,
    seaCoreExt % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .enablePlugins(MultiJvmPlugin)
  .configs(MultiJvm)
  .settings(Publishing.noPublish: _*)
  .settings(
    jvmOptions in MultiJvm := Seq("-Xmx1024M"),
    libraryDependencies ++= Seq(
      _akkaMultiNodeTestkit
    ) //++ _kamons
  )

// 监查、控制、管理
lazy val seaConsole = _project("sea-console")
  .dependsOn(
    seaCoreExt % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(Packaging.settings: _*)
  .settings(Publishing.noPublish: _*)
  .settings(
    mainClass in Compile := Some("seadata.console.Main"),
    libraryDependencies ++= Seq(

    )
  )

// 执行引擎
lazy val seaBroker = _project("sea-broker")
  .dependsOn(
    seaCoreExt % "compile->compile;test->test",
    seaCore % "compile->compile;test->test")
  .enablePlugins(JavaAppPackaging)
  .settings(Packaging.settings: _*)
  .settings(Publishing.noPublish: _*)
  .settings(
    mainClass in Compile := Some("seadata.broker.Main"),
    libraryDependencies ++= Seq(
      _quartz,
      _alpakkaFile,
      _alpakkaFtp
    )
  )

// 数据组件（采集、存储）
lazy val seaData = _project("sea-data")
  .settings(Publishing.noPublish: _*)
  .dependsOn(
    seaCore % "compile->compile;test->test")
  .settings(
    test in assembly := {},
    //    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
    libraryDependencies ++= Seq(
      _akkaStreamKafka,
      _mysql,
      _postgresql
    ) ++ _alpakkas
  )

// 管理程序公共库
lazy val seaCoreExt = _project("sea-core-ext")
  .settings(Publishing.publishing: _*)
  .dependsOn(seaCore % "compile->compile;test->test")
  .settings(
    libraryDependencies ++= Seq(
      _sigarLoader
    ) ++ _akkaClusters ++ _akkaHttps //++ _kamons
  )

lazy val seaCore = _project("sea-core")
  .dependsOn(seaCommon % "compile->compile;test->test")
  .settings(Publishing.publishing: _*)
  .settings(
    libraryDependencies ++= Seq(
      _protobuf,
      _akkaHttpCore % Provided
    )
  )

lazy val seaCommon = _project("sea-common")
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
      _scalaJava8Compat,
      _mysql % Test,
      _postgresql % Test,
      _scalatest % Test
    ) ++ _jacksons ++ _akkas
  )

def _project(name: String, _base: String = null) =
  Project(id = name, base = file(if (_base eq null) name else _base))
    .settings(basicSettings: _*)
