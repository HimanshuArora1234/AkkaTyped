package untyped

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object UntypedMain2 extends App with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("untyped-actor-system")

  val untypedScalaIOActor: ActorRef = system.actorOf(UntypedScalaIOActor.props, "untyped-scalaio-actor")

  implicit val timeout = Timeout(2, TimeUnit.SECONDS)


  val eventualUnhandledMsgReply: Future[Any] = untypedScalaIOActor ? "Hello"

  eventualUnhandledMsgReply onComplete {
    case Success(value) =>
      logger.info(s"Unhandled msg reply = $value")
    case Failure(exception) => logger.error("Error occurred while sending unhandled message", exception)
  }
}
