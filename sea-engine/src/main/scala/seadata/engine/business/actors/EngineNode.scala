package seadata.engine.business.actors

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.typesafe.scalalogging.StrictLogging

class EngineNode extends Actor with StrictLogging {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  override def receive: Receive = {
    case MemberUp(member) =>
      logger.info("Member is Up: {}, roles: {}", member, member.roles)
    case UnreachableMember(member) =>
      logger.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      logger.info("Member is Removed: {} after {}", member, previousStatus)
    case _: MemberEvent => // ignore
  }

}

object EngineNode {

  def props: Props = Props[EngineNode]

}
