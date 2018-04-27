/*
 * Copyright (c) Yangbajing 2018
 *
 * This is the custom License of Yangbajing
 */

package seadata.console.boot

import akka.actor.{ActorRef, ActorSystem}
import seadata.console.ConsoleNode
import seadata.core.Constants
import seadata.core.server.{BaseBoot, SeaBoot}

final class ConsoleBoot(
    val system: ActorSystem,
    initBrokerLeaderProxy: Boolean = true
) extends BaseBoot {
  private[this] var consoleNode: ActorRef = _

  def start(): ConsoleBoot = {
    SeaBoot.init(system)
    consoleNode = system.actorOf(ConsoleNode.props, Constants.Nodes.CONSOLE)

    this
  }

}

