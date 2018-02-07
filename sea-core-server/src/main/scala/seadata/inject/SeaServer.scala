package seadata.inject

import akka.cluster.Cluster
import akka.actor.{ActorSystem, Address}
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging
import helloscala.common.Configuration
import helloscala.common.util.Utils

object SeaServer extends StrictLogging {

  private[this] var _actorSystem: ActorSystem = _

  def actorSystem: ActorSystem = {
    assert(_actorSystem ne null, "_actorSystem 在使用前必需先设置")
    _actorSystem // ActorSystem(config.getString("sea.cluster.name"), config)
  }

  def config: Config = actorSystem.settings.config

  def configuration: Configuration = Configuration(config)

  def start(config: Config): Unit = {
    start(ActorSystem(Utils.getClusterName(config), config))
  }

  def start(s: ActorSystem): Unit = {

  }

  def start(s: ActorSystem, joinAddress: Address): Unit = {
    _actorSystem = s
    if (joinAddress ne null) {
      Cluster(actorSystem).join(joinAddress)
    }
    logger.info(startupDump())
  }

  def startupDump(): String = {
    (List("akka.loglevel", "akka.stdout-loglevel").map(path => config.getString(path)) :::
      List("akka.cluster.seed-nodes", "akka.cluster.roles").map(path => config.getStringList(path)) :::
      List("sea", "akka.remote").map(path => config.getConfig(path))
    ).mkString("\n")
  }

}
