package shopping.cart;

import io.grpc.examples.helloworld.Greeter;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class GreeterServiceImpl implements Greeter {
    @Override
    public CompletionStage<HelloReply> sayHello(HelloRequest in) {
        HelloReply reply = HelloReply.newBuilder()
                .setResponse(in.getRequest())
                .build();
        return CompletableFuture.completedFuture(reply);
    }
}
