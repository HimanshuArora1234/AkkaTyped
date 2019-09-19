package typed

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

/**
  * Typed actor.
  */
object TypedScalaIOActor {

  // Protocol - request
  sealed trait ScalaIORequest

  final case class BuyTicket(quantity: Int, senderActor: ActorRef[ScalaIOResponse]) extends ScalaIORequest

  final case class Donate(amt: Double, senderActor: ActorRef[ScalaIOResponse]) extends ScalaIORequest

  final case class TicketsLeft(senderActor: ActorRef[ScalaIOResponse]) extends ScalaIORequest

  final case class FundBalance(senderActor: ActorRef[ScalaIOResponse]) extends ScalaIORequest

  final case class GetSchedule(senderActor: ActorRef[ScalaIOResponse]) extends ScalaIORequest

  // Protocol - response
  sealed trait ScalaIOResponse

  final case class Message(message: String, senderActor: ActorRef[ScalaIORequest]) extends ScalaIOResponse

  final case class Quantity(qty: Int, senderActor: ActorRef[ScalaIORequest]) extends ScalaIOResponse

  final case class Fund(fund: Double, senderActor: ActorRef[ScalaIORequest]) extends ScalaIOResponse


  // Typed actor's behaviour
  def typedScalaIOActor(quantity: Int, amt: Double): Behavior[ScalaIORequest] = Behaviors.receive { (context, message) =>

    context.log.info(s"TypedScalaIOActor received the following message: $message")

    message match {

      case BuyTicket(qty, senderActor) =>
        if (qty <= quantity) {
          senderActor ! Message(s"Congratulations !! you have successfully booked $qty tickets of Scala.IO", context.self)
          typedScalaIOActor(quantity - qty, amt)   // Behaviour mutation
        } else {
          senderActor ! Message("Oops !! can't fulfill your request because of shortage of tickets", context.self)
          Behavior.same
        }

      case Donate(donationAmt, senderActor) =>
        senderActor ! Message(s"Scala.IO is grateful to you for your generous donation of $donationAmt euros", context.self)
        typedScalaIOActor(quantity, amt + donationAmt)

      case TicketsLeft(senderActor) =>
        senderActor ! Quantity(quantity, context.self)
        Behavior.same

      case FundBalance(senderActor) =>
        senderActor ! Fund(amt, context.self)
        Behavior.same

      case GetSchedule(senderActor) =>
        senderActor ! Message("29 - 31 Oct 2019", context.self)
        Behavior.same
    }
  }


}
