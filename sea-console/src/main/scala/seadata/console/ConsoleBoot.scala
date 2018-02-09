/*
 * Copyright (c) Yangbajing 2018
 *
 * This is the custom License of Yangbajing
 */

package seadata.console

import akka.actor.{ActorRef, ActorSystem}
import seadata.core.Constants
import seadata.core.server.{BaseBoot, SeaBoot}

object ConsoleBoot {
  private var _boot: ConsoleBoot = _

  def boot: ConsoleBoot = {
    assert(_boot ne null)
    _boot
  }

  def apply(system: ActorSystem): ConsoleBoot = new ConsoleBoot(system)
}

case class ConsoleBoot private (
    system: ActorSystem,
    initBrokerLeaderProxy: Boolean = true
) extends BaseBoot {
  private[this] var consoleNode: ActorRef = _

  def start(): ConsoleBoot = {
    SeaBoot.init(system)
    consoleNode = system.actorOf(ConsoleNode.props, Constants.Nodes.CONSOLE)

    setupBoot()
  }

  private def setupBoot(): ConsoleBoot = {
    if (ConsoleBoot._boot ne null) {
      throw new ExceptionInInitializerError(getClass.getSimpleName + " 已实例化")
    }
    ConsoleBoot._boot = this
    this
  }

}

