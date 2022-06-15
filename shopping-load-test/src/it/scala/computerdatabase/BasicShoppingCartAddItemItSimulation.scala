package computerdatabase

import com.github.phisgr.gatling.grpc.Predef._
import io.gatling.core.Predef._
import shopping.cart.proto.ShoppingCartService.{AddItemRequest, GetCartRequest, ShoppingCartServiceGrpc}

class BasicShoppingCartAddItemItSimulation extends Simulation {

  val grpcConf = grpc(managedChannelBuilder("localhost", 8101).usePlaintext())


  var scn = scenario("ShoppingCartAddItem") // A scenario is a chain of requests and pauses
//    .exec(request)
//    .pause(7.seconds)
//    .exec(request)
//    .exec(request)
//    .exec(request)

  for (i <- 1 to 1000) {
    val request = grpc("request_add_item")
      .rpc(ShoppingCartServiceGrpc.METHOD_ADD_ITEM)
      .payload(AddItemRequest(s"cart$i", "socks", i * 2))

//    val request = grpc("request_checkout")
//      .rpc(ShoppingCartServiceGrpc.METHOD_CHECKOUT)
//      .payload(CheckoutRequest(s"cart$i"))

    scn = scn.exec(request)
  }

  setUp(scn.inject(atOnceUsers(100)).protocols(grpcConf))
}
