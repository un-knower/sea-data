package seadata.engine.server

import akka.actor.ActorSystem
import seadata.engine.business.actors.EngineNode

class EngineServer(actorSystem: ActorSystem) {
  def start(): Unit = {
    actorSystem.actorOf(EngineNode.props, "engine-node")
  }
}
