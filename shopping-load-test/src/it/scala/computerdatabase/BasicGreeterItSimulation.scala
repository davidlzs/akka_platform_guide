package computerdatabase

import com.github.phisgr.gatling.grpc.Predef._
import io.gatling.core.Predef._
import io.grpc.examples.helloworld.helloworld.{GreeterGrpc, Hello, HelloRequest}
import shopping.cart.proto.ShoppingCartService.{CheckoutRequest, GetCartRequest, ShoppingCartServiceGrpc}

class BasicGreeterItSimulation extends Simulation {

  val grpcConf = grpc(managedChannelBuilder("localhost", 8101).usePlaintext())

//  val request = grpc("request_say_hello")
//    .rpc(GreeterGrpc.METHOD_SAY_HELLO)
//    .payload(HelloRequest(Option(Hello("David"))))

  var scn = scenario("Greeting") // A scenario is a chain of requests and pauses
//    .exec(request)
//    .pause(7.seconds)
//    .exec(request)
//    .exec(request)
//    .exec(request)

  for (i <- 1 to 1000) {
    val request = grpc("request_greeting")
      .rpc(GreeterGrpc.METHOD_SAY_HELLO)
      .payload(HelloRequest(Option(Hello(s"David$i"))))

    scn = scn.exec(request)
  }

  setUp(scn.inject(atOnceUsers(100)).protocols(grpcConf))
}
