package shopping.cart;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.pubsub.Topic;
import akka.grpc.GrpcClientSettings;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import shopping.cart.proto.ShoppingCartService;
import shopping.cart.repository.ItemPopularityRepository;
import shopping.cart.repository.SpringIntegration;
import shopping.order.proto.ShoppingOrderService;
import shopping.order.proto.ShoppingOrderServiceClient;

public class Main {


  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static ActorRef<Message> subscriber;
  public static ActorRef<Topic.Command<Message>> adminTopic;


  public static void main(String[] args) {
//    ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), "ShoppingCartService");
//    ActorSystem<SpawnProtocol.Command> system = ActorSystem.create(SpawnProtocol.create(), "ShoppingCartService");
    ActorSystem<SpawnProtocol.Command> system = ActorSystem.create(Main.create(), "ShoppingCartService");
    try {
      init(system, orderServiceClient(system));
    } catch (Exception e) {
      logger.error("Terminating due to initialization failure.", e);
      system.terminate();
    }
  }

  private static Behavior<SpawnProtocol.Command> create() {
    return Behaviors.setup(ctx -> {
      adminTopic = ctx.spawn(Topic.create(Message.class, "admin-topic"), "AdminTopic");

      subscriber = ctx.spawn(Behaviors.receive(Message.class).onMessage(Message.class, (msg) -> {
        System.out.println(msg.text);
        return Behaviors.same();
      }).build(), "subscriber");

      adminTopic.tell(Topic.subscribe(subscriber));

      return SpawnProtocol.create();
    });
  }


  public static void init(ActorSystem<?> system, ShoppingOrderService orderService) {

    AkkaManagement.get(system).start();
    ClusterBootstrap.get(system).start();

    ShoppingCart.init(system);

    ApplicationContext springContext = SpringIntegration.applicationContext(system);

    ItemPopularityRepository itemPopularityRepository =
        springContext.getBean(ItemPopularityRepository.class);

    JpaTransactionManager transactionManager = springContext.getBean(JpaTransactionManager.class);

    ItemPopularityProjection.init(system, transactionManager, itemPopularityRepository);

    PublishEventsProjection.init(system, transactionManager);

    
    SendOrderProjection.init(system, transactionManager, orderService); 
    

    Config config = system.settings().config();
    String grpcInterface = config.getString("shopping-cart-service.grpc.interface");
    int grpcPort = config.getInt("shopping-cart-service.grpc.port");
    ShoppingCartService grpcService = new ShoppingCartServiceImpl(system, itemPopularityRepository);
    ShoppingCartServer.start(grpcInterface, grpcPort, system, grpcService);


    adminTopic.tell(Topic.publish(new Message("HELLO ALL, DID YOU GET IT?")));
    
  }

  static ShoppingOrderService orderServiceClient(ActorSystem<?> system) { 
    GrpcClientSettings orderServiceClientSettings =
        GrpcClientSettings.connectToServiceAt(
                system.settings().config().getString("shopping-order-service.host"),
                system.settings().config().getInt("shopping-order-service.port"),
                system)
            .withTls(false);

    return ShoppingOrderServiceClient.create(orderServiceClientSettings, system);
  }
  

}
