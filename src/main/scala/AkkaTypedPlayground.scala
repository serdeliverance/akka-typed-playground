import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object AkkaTypedPlayground {

  // 1 - typed messages & actors
  trait ShoppingCartMessage
  case class AddItem(item: String) extends ShoppingCartMessage
  case class RemoveItem(item: String) extends ShoppingCartMessage
  case object ValidateCard extends ShoppingCartMessage

  val shoppingRootActor = ActorSystem(
    Behaviors.receiveMessage[ShoppingCartMessage] { message: ShoppingCartMessage =>
      message match {
        case AddItem(item) => println(s"Adding $item to cart")
        case RemoveItem(item) => println(s"Removing $item from cart")
        case ValidateCard => println("The cart is good")
      }

      Behaviors.same
    },
    "simpleShoppingActor"
  )

  shoppingRootActor ! ValidateCard

  // 2 - mutable state

  val shoppingRootActor2 = ActorSystem(
    Behaviors.setup[ShoppingCartMessage] { ctx =>

      // local state = mutable
      var items: Set[String] = Set()

      Behaviors.receiveMessage[ShoppingCartMessage] { message: ShoppingCartMessage =>
        message match {
          case AddItem(item) =>
            println(s"Adding $item to cart")
            // mutate variable
            items += item
          case RemoveItem(item) =>
            println(s"Removing $item from cart")
            items -= item
          case ValidateCard => println("The cart is good")
        }
        Behaviors.same
      }
    },
    "simpleShoppingActorMutable"
  )

  def shoppingBehavior(items: Set[String]): Behavior[ShoppingCartMessage] =
    Behaviors.receiveMessage[ShoppingCartMessage] {
      case AddItem(item) =>
        println(s"Adding $item to cart")
        // returning a new behavior with the new variable (immutable way)
        shoppingBehavior(items + item)
      case RemoveItem(item) =>
        println(s"Removing $item from cart")
        // returning a new behavior with the new variable (immutable way)
        shoppingBehavior(items - item)
      case ValidateCard =>
        println("The cart is good")
        Behaviors.same
    }

  // 3 - hierarchy

  val rootOnlineStoreActor = ActorSystem(
    Behaviors.setup { ctx =>
      // create children
      ctx.spawn(shoppingBehavior(Set()), "johnShoppingCarg")

      Behaviors.empty
    },
    "onlineStore"
  )
}
