package bigdata.labs.lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.asynchttpclient.AsyncHttpClient;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class AkkaStreamsApp extends AllDirectives{


    private static final String HOST = "localhost";
    private static final int PORT = 8081;

    private ActorRef routeActor;

    private AkkaStreamsApp(ActorRef routeActor) {
        this.routeActor = routeActor;
    }

    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("routes");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final AsyncHttpClient asyncHttpClient = asyncHttpClient();

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = new bigdata.labs.lab5.AsyncHttpClient(system).flowHttp(materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                materializer
        );

        String startMessage = String.format("App started at http://%s:%d/", HOST, PORT);
        System.out.println(startMessage);
        System.in.read();

        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
        try {
            asyncHttpClient.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}
