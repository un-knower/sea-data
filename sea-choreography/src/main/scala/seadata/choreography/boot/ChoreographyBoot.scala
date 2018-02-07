package seadata.choreography.boot

import com.typesafe.config.ConfigFactory
import seadata.inject.SeaServer

object ChoreographyBoot {

  def main(args: Array[String]): Unit = {
    SeaServer.start(ConfigFactory.load())
  }

}
