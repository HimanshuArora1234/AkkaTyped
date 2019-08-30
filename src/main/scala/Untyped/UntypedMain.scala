package Untyped

import java.util.concurrent.TimeUnit

import Untyped.UntypedScalaIOActor.{BuyTicket, Donate, FundBalance, GetSchedule, TicketsLeft}
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object UntypedMain extends App with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("untyped-actor-system")

  val untypedScalaIOActor: ActorRef = system.actorOf(UntypedScalaIOActor.props, "untyped-scalaio-actor")

  implicit val timeout = Timeout(2, TimeUnit.SECONDS)

  val eventualSchedule: Future[Any] = untypedScalaIOActor ? GetSchedule

  eventualSchedule onComplete {
    case Success(value) =>
      logger.info(s"Scala.IO schedule = $value")
    case Failure(exception) => logger.error("Error occurred while asking for schedule", exception)
  }

  Thread.sleep(1000)

  val eventualButTicketResponse: Future[Any] = untypedScalaIOActor ? BuyTicket(5)

  eventualButTicketResponse onComplete {
    case Success(value) => logger.info(value.toString)
    case Failure(exception) => logger.error("Error occurred while buying tickets", exception)
  }

  Thread.sleep(1000)

  val eventualDonateResponse: Future[Any] = untypedScalaIOActor ? Donate(500.0)

  eventualDonateResponse onComplete {
    case Success(value) => logger.info(value.toString)
    case Failure(exception) => logger.error("Error occurred while making donation", exception)
  }

  Thread.sleep(1000)

  val eventualTicketsLeft: Future[Any] = untypedScalaIOActor ? TicketsLeft

  eventualTicketsLeft onComplete {
    case Success(value) =>
      logger.info(s"Scala.IO remaining tickets = $value")
    case Failure(exception) => logger.error("Error occurred while asking for remaining tickets", exception)
  }

  Thread.sleep(1000)

  val eventualFundBalance: Future[Any] = untypedScalaIOActor ? FundBalance

  eventualFundBalance onComplete {
    case Success(value) =>
      logger.info(s"Scala.IO fund balance = $value")
    case Failure(exception) => logger.error("Error occurred while asking for fund balance", exception)
  }

  /*Thread.sleep(1000)

  val eventualUnhandledMsgReply: Future[Any] = untypedScalaIOActor ? "Hello"

  eventualUnhandledMsgReply onComplete {
    case Success(value) =>
      logger.info(s"Unhandled msg reply = $value")
    case Failure(exception) => logger.error("Error occurred while sending unhandled message", exception)
  }*/

}
