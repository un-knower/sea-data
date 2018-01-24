package helloscala.test

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}

trait AkkaSpec extends BeforeAndAfterAll {
  this: HelloscalaSpec =>

  implicit def actorSystem: ActorSystem = ActorSystem("akka-spec")

  implicit def actorMaterializer: ActorMaterializer = ActorMaterializer()

  implicit def executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  override protected def afterAll(): Unit = {
    Await.result(actorSystem.terminate(), 60.seconds)
  }

}
