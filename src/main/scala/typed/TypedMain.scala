package typed

import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import typed.TypedScalaIOActor._

object TypedMain extends App {

  val main: Behavior[NotUsed] =
    Behaviors.setup { context =>

      val scalaIOBotActor: ActorRef[ScalaIOResponse] = context.spawn(ScalaIOBot.scalaIOBotActor, "scalaIOBotActor")

      val typedScalaIOActor: ActorRef[ScalaIORequest] =
        context.spawn(TypedScalaIOActor.typedScalaIOActor(100, 0), "typedScalaIOActor")

      typedScalaIOActor ! GetSchedule(scalaIOBotActor)
      typedScalaIOActor ! BuyTicket(5, scalaIOBotActor)
      typedScalaIOActor ! Donate(500.0, scalaIOBotActor)
      typedScalaIOActor ! TicketsLeft(scalaIOBotActor)
      typedScalaIOActor ! FundBalance(scalaIOBotActor)

      //typedScalaIOActor ! "String type message"

      Behavior.empty // root or main actor doesn't do anything but to spawn the children actors
    }

  val system = ActorSystem(main, "typed-actor-system")
}
