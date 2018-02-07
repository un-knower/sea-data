package seadata.scheduler.boot

import com.typesafe.config.ConfigFactory
import seadata.inject.SeaServer

object SchedulerBoot {

  def main(args: Array[String]): Unit = {
    SeaServer.start(ConfigFactory.load())
  }

}
