package seadata.console.boot

import com.typesafe.config.ConfigFactory
import seadata.inject.SeaServer

object ConsoleBoot {

  def main(args: Array[String]): Unit = {
    SeaServer.start(ConfigFactory.load())
  }

}
