package bigdata.labs.lab5;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.Pair;

import java.util.*;

public class CacheActor extends AbstractActor {

    private Map<String, Long> cache = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Pair.class, t -> {
                    String key = (String) t.first();
                    if (cache.containsKey(key))
                        sender().tell(new Response(key, cache.get(key)), self());
                    else
                        sender().tell("NO RESPONSE", self());
                })
                .match(Response.class, p -> cache.put(p.getHostName(), p.getResponseTime()))
                .build();
    }

    static Props props() {
        return Props.create(CacheActor.class);
    }
}

