/*
 * Copyright (c) Yangbajing 2018
 *
 * This is the custom License of Yangbajing
 */

package seadata.broker

import seadata.core.server.SeaBoot
import seadata.broker.boot.BrokerBoot

object Main {

  def main(args: Array[String]): Unit = {
    BrokerBoot(SeaBoot.actorSystem).start()
  }

}
