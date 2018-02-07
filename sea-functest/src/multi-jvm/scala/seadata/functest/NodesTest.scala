package seadata.functest

import akka.actor.ActorSystem
import akka.remote.testkit.{MultiNodeConfig, MultiNodeSpec}
import akka.testkit.ImplicitSender
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import seadata.engine.server.EngineServer
import seadata.inject.SeaServer

object NodesTestMultiNodeConfig extends MultiNodeConfig {
  val nodeScheduler1 = role("scheduler1")
  val nodeEngine1 = role("engine1")
  val nodeConsole1 = role("console1")
  val nodeChoreography1 = role("choreography1")

  nodeList.foreach { role =>
    nodeConfig(role) {
      ConfigFactory.parseString(
//        akka.extensions=["akka.cluster.metrics.ClusterMetricsExtension"]
//      akka.cluster.metrics.native-library-extract-folder=target/native/${role.name}

      s"""|akka.remote.artery.enabled = off
            |akka.cluster.seed-nodes = []
            """.stripMargin)
    }
  }

  nodeSchedulerList.zipWithIndex.foreach { case (role, idx) =>
    val port = 30011 + idx
    nodeConfig(role)(
      ConfigFactory
        .parseString(
          s"""|akka.remote.netty.tcp.port = $port
              |akka.remote.artery.canonical.port = $port""".stripMargin)
        .withFallback(ConfigFactory.load("sea-scheduler"))
    )
  }

  nodeEngineList.zipWithIndex.foreach { case (role, idx) =>
    val port = 30021 + idx
    nodeConfig(role)(
      ConfigFactory
        .parseString(
          s"""|akka.remote.netty.tcp.port = $port
              |akka.remote.artery.canonical.port = $port""".stripMargin)
        .withFallback(ConfigFactory.load("sea-engine"))
    )
  }

  nodeConsoleList.zipWithIndex.foreach { case (role, idx) =>
    val port = 30031 + idx
    nodeConfig(role)(
      ConfigFactory
        .parseString(
          s"""|akka.remote.netty.tcp.port = $port
              |akka.remote.artery.canonical.port = $port""".stripMargin)
        .withFallback(ConfigFactory.load("sea-console"))
    )
  }

  nodeChoreographyList.zipWithIndex.foreach { case (role, idx) =>
    val port = 30041 + idx
    nodeConfig(role)(
      ConfigFactory
        .parseString(
          s"""|akka.remote.netty.tcp.port = $port
              |akka.remote.artery.canonical.port = $port""".stripMargin)
        .withFallback(ConfigFactory.load("sea-choreography"))
    )
  }

  def nodeList = nodeSchedulerList ++ nodeEngineList ++ nodeConsoleList ++ nodeChoreographyList

  def nodeSchedulerList = Seq(nodeScheduler1)

  def nodeEngineList = Seq(nodeEngine1)

  def nodeConsoleList = Seq(nodeConsole1)

  def nodeChoreographyList = Seq(nodeChoreography1)
}

abstract class NodesTest
  extends MultiNodeSpec(NodesTestMultiNodeConfig, config => ActorSystem("sea", config))
    with WordSpecLike with Matchers with BeforeAndAfterAll
    with ImplicitSender {

  import NodesTestMultiNodeConfig._

  override def beforeAll(): Unit = multiNodeSpecBeforeAll()

  override def afterAll(): Unit = multiNodeSpecAfterAll()

  override def initialParticipants: Int = roles.size

  "Sea NodesTest" should {
    "启动服务" in {
      runOn(nodeScheduler1) {
        SeaServer.start(system, node(nodeScheduler1).address)
        enterBarrier("startup")
      }
      runOn(nodeEngine1) {
        enterBarrier("startup")
        SeaServer.start(system, node(nodeScheduler1).address)
        new EngineServer(system).start()
      }
      runOn(nodeConsole1) {
        enterBarrier("startup")
        SeaServer.start(system, node(nodeScheduler1).address)
      }
      runOn(nodeChoreography1) {
        enterBarrier("startup")
        SeaServer.start(system, node(nodeScheduler1).address)
      }
    }

    "查询状态" in {
      println(s"$myself " + node(myself).address)
      enterBarrier("finished")
      java.util.concurrent.TimeUnit.SECONDS.sleep(2)
    }
  }

}

class NodesTestMultiJvmNode1 extends NodesTest

class NodesTestMultiJvmNode2 extends NodesTest

class NodesTestMultiJvmNode3 extends NodesTest

class NodesTestMultiJvmNode4 extends NodesTest
