package untyped

import untyped.UntypedScalaIOActor._
import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.LazyLogging

/**
  * Untyped actor.
  */
class UntypedScalaIOActor extends Actor with LazyLogging {

  // Initial state
  private var numberOfTickets: Int = 100
  private var fund: Double = 0.0

  // Actor behaviour
  override def receive: Receive = {     // PartialFunction[Any, Unit]

    case BuyTicket(quantity) =>
      logger.info(s"UntypedScalaIOActor received BuyTicket($quantity) message")
      if (numberOfTickets >= quantity) {
        numberOfTickets = numberOfTickets - quantity // State mutation
        sender ! s"Congratulations !! you have successfully booked $quantity tickets of Scala.IO"
      } else {
        sender ! "Oops !! can't fulfill your request because of shortage of tickets"
      }

    case Donate(amt) =>
      logger.info(s"UntypedScalaIOActor received Donate($amt) message")
      fund = fund + amt
      sender ! s"Scala.IO is grateful to you for your generous donation of $amt euros"

    case TicketsLeft =>
      logger.info(s"UntypedScalaIOActor received TicketsLeft message")
      sender ! numberOfTickets

    case FundBalance =>
      logger.info(s"UntypedScalaIOActor received FundBalance message")
      sender ! fund

    case GetSchedule =>
      logger.info(s"UntypedScalaIOActor received GetSchedule message")
      sender ! "29 - 31 Oct 2019"
  }


  override def unhandled(message: Any): Unit = {
    message match {
      case _  => logger.error(s"UntypedScalaIOActor can't handle $message")
    }
  }
}

object UntypedScalaIOActor {

  // Communication protocol
  case class BuyTicket(quantity: Int)

  case class Donate(amt: Double)

  case object TicketsLeft

  case object FundBalance

  case object GetSchedule


  // Factory of UntypedScalaIOActor, props for configuration
  def props: Props = Props[UntypedScalaIOActor]
}
