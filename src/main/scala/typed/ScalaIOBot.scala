package typed

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import typed.TypedScalaIOActor.{Fund, Message, Quantity, ScalaIOResponse}

/**
  * Typed actor to interact with [[TypedScalaIOActor]].
  */
object ScalaIOBot {

  val scalaIOBotActor: Behavior[ScalaIOResponse] = Behaviors.receive { (context, message) =>
    message match {
      case Message(message, senderActor) =>
        context.log.info(message)
        Behavior.same
      case Quantity(qty, senderActor) =>
        context.log.info(s"ScalaIO remaining tickets = $qty")
        Behavior.same
      case Fund(fund, senderActor) =>
        context.log.info(s"ScalaIO available fund = $fund")
        Behavior.same
    }
  }

}
