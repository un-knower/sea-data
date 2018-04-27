/*
 * Copyright (c) Yangbajing 2018
 *
 * This is the custom License of Yangbajing
 */

package seadata.broker.boot

import seadata.core.server.SeaBoot

object Main {

  def main(args: Array[String]): Unit = {
    new BrokerBoot(SeaBoot.actorSystem).start()
  }

}
