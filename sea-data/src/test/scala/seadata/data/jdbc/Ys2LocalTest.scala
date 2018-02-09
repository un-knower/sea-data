package seadata.data.jdbc

import java.sql.ResultSet
import java.util.Properties
import java.util.concurrent.TimeUnit

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import helloscala.common.jdbc.{JdbcSink, JdbcSinkResult, JdbcSource}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpec}
import seadata.ipc.data.ElementCell

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class Ys2LocalTest extends WordSpec with MustMatchers with BeforeAndAfterAll {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  val localDS = createPGDataSource()
  val ysDS = createYsPGDataSource()

  "migrate" in {
    val columns = Array("id", "ban_id", "banletter", "banlettpiny", "bantype", "banexp", "banform", "banto", "recper", "recdate")
    val len = columns.length

    val source: Source[ResultSet, NotUsed] = JdbcSource("SELECT * FROM haishu_ys.reg_name_baninfo;", Nil, 1500)(ysDS)

    val extractFlow: Flow[ResultSet, Array[ElementCell], NotUsed] = Flow[ResultSet].map { rs =>
      var i = 0
      val items = Array.ofDim[ElementCell](len)
      while (i < len) {
        val value = rs.getObject(columns(i))
        items(i) = ElementCell(i + 1, value)
        i += 1
      }
      items
    }

    val transferFlow: Flow[Array[ElementCell], Array[ElementCell], NotUsed] = Flow[Array[ElementCell]].map { items =>
      val idxBanId = 1
      val banId = items(idxBanId)
      if (banId.value ne null) {
        val value = banId.value.asInstanceOf[String].toLowerCase()
        items(idxBanId) = ElementCell(banId.idx, value)
      }
      items
    }

    val sink: Sink[Array[ElementCell], Future[JdbcSinkResult]] = JdbcSink[Array[ElementCell]](
      conn => conn.prepareStatement(
        """INSERT INTO reg_name_baninfo (id, ban_id, banletter, banlettpiny, bantype, banexp, banform, banto, recper, recdate)
          |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""".stripMargin),
      (items, stmt) => {
        var i = 0
        while (i < len) {
          stmt.setObject(i + 1, items(i).value)
          i += 1
        }
      },
      1000)(localDS)

    val graph: RunnableGraph[Future[JdbcSinkResult]] = source
      .via(extractFlow)
      .via(transferFlow)
      .toMat(sink)(Keep.right)

    val begin = System.nanoTime()
    val f = graph.run()
    val result = Await.result(f, Duration.Inf)
    val end = System.nanoTime()

    val costTime = java.time.Duration.ofNanos(end - begin)
    println(s"从 155 导 reg_name_baninfo 表的 ${result.count} 条数据到本地共花费时间：$costTime")
  }

  override protected def beforeAll(): Unit = {
    TimeUnit.SECONDS.sleep(3) // JVM预热
  }

  override protected def afterAll(): Unit = {
    localDS.close()
    ysDS.close()
    system.terminate()
  }

  private def createPGDataSource(): HikariDataSource = {
    val props = new Properties()
    props.setProperty("poolName", "local")
    props.setProperty("maximumPoolSize", "4")
    props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
    props.setProperty("dataSource.serverName", "localhost")
    props.setProperty("dataSource.databaseName", "yangbajing")
    props.setProperty("dataSource.user", "yangbajing")
    props.setProperty("dataSource.password", "yangbajing")

    val config = new HikariConfig(props)
    new HikariDataSource(config)
  }

  private def createYsPGDataSource(): HikariDataSource = {
    val props = new Properties()
    props.setProperty("poolName", "ys")
    props.setProperty("maximumPoolSize", "4")
    props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
    props.setProperty("dataSource.serverName", "192.168.32.155")
    props.setProperty("dataSource.portNumber", "10032")
    props.setProperty("dataSource.databaseName", "postgres")
    props.setProperty("dataSource.user", "postgres")
    props.setProperty("dataSource.password", "hl.Data2018")

    val config = new HikariConfig(props)
    new HikariDataSource(config)
  }

}
