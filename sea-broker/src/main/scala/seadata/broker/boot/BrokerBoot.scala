/*
 * Copyright (c) Yangbajing 2018
 *
 * This is the custom License of Yangbajing
 */

package seadata.broker.boot

import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings}
import seadata.core.Constants
import seadata.core.server.{BaseBoot, SeaBoot}
import seadata.broker.BrokerNode
import seadata.broker.leader.BrokerLeader

//object BrokerBoot {
//  private var _boot: BrokerBoot = _
//
//  def boot: BrokerBoot = {
//    assert(_boot ne null)
//    _boot
//  }
//
//  def apply(system: ActorSystem) = new BrokerBoot(system)
//
//}

final class BrokerBoot(
    val system: ActorSystem,
    initBrokerLeaderProxy: Boolean = true
) extends BaseBoot {

  private[this] var _brokerNode: ActorRef = _

  def brokerNode: ActorRef = _brokerNode

  def start(): BrokerBoot = {
    SeaBoot.init(system)
    system.actorOf(
      ClusterSingletonManager.props(
        singletonProps = BrokerLeader.props,
        terminationMessage = PoisonPill,
        settings = ClusterSingletonManagerSettings(system).withRole(Constants.Roles.BROKER)
      ),
      Constants.Nodes.BROKER_LEADER)

    if (initBrokerLeaderProxy) {
      startBrokerLeaderProxy()
    }

    _brokerNode = system.actorOf(BrokerNode.props, Constants.Nodes.BROKER)

    this
  }

}
