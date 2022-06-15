package computerdatabase

import com.github.phisgr.gatling.grpc.Predef._
import io.gatling.core.Predef._
import io.grpc.health.v1.health.HealthCheckResponse.ServingStatus.SERVING
import io.grpc.health.v1.health.{HealthCheckRequest, HealthGrpc}
import shopping.cart.proto.ShoppingCartService.{CheckoutRequest, GetCartRequest, ShoppingCartServiceGrpc}

import scala.concurrent.duration._

class BasicShoppingCartItSimulation extends Simulation {

  val grpcConf = grpc(managedChannelBuilder("localhost", 8101).usePlaintext())


  var scn = scenario("ShoppingCart") // A scenario is a chain of requests and pauses
//    .exec(request)
//    .pause(7.seconds)
//    .exec(request)
//    .exec(request)
//    .exec(request)

  for (i <- 1 to 1000) {
    val request = grpc("request_getcart")
      .rpc(ShoppingCartServiceGrpc.METHOD_GET_CART)
      .payload(GetCartRequest(s"cart$i"))

//    val request = grpc("request_checkout")
//      .rpc(ShoppingCartServiceGrpc.METHOD_CHECKOUT)
//      .payload(CheckoutRequest(s"cart$i"))

    scn = scn.exec(request)
  }

  setUp(scn.inject(atOnceUsers(100)).protocols(grpcConf))
}
