package Untyped

import Untyped.UntypedScalaIOActor._
import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.LazyLogging

/**
  * Untyped actor.
  */
class UntypedScalaIOActor extends Actor with LazyLogging {

  // Initial state
  private var numberOfTickets: Int = 100
  private var fund: Double = 0.0

  override def receive: Receive = {

    case BuyTicket(quantity) =>
      if (numberOfTickets >= quantity) {
        numberOfTickets = numberOfTickets - quantity
        sender ! s"Congratulations !! you have successfully booked $quantity tickets of Scala.IO"
      } else {
        sender ! "Oops !! can't fulfill your request because of shortage of tickets"
      }

    case Donate(amt) =>
      fund = fund + amt
      sender ! s"Scala.IO is grateful to you for your generous donation of $amt euros"

    case TicketsLeft => sender ! numberOfTickets

    case FundBalance => sender ! fund

    case GetSchedule => sender ! "29 - 31 Oct 2019"
  }


  /*override def unhandled(message: Any): Unit = {
    message match {
      case _  => logger.error(s"UntypedScalaIOActor can't handle $message")
    }
  }*/
}

object UntypedScalaIOActor {

  // Communication protocol
  case class BuyTicket(quantity: Int)

  case class Donate(amt: Double)

  case object TicketsLeft

  case object FundBalance

  case object GetSchedule


  // Factory of AccountActor, props for configuration
  def props: Props = Props[UntypedScalaIOActor]
}
