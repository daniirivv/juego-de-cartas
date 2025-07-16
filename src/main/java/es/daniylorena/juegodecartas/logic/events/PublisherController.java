package es.daniylorena.juegodecartas.logic.events;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PublisherController {

    private final Map<EventType, List<Subscriber>> subscribers = new HashMap<>();

    public void subscribe(EventType event, Subscriber sub){
        List<Subscriber> users = this.subscribers.get(event);
        users.add(sub);
    }

    public void unsubscribe(EventType event, Subscriber sub){
        List<Subscriber> users = this.subscribers.get(event);
        users.remove(sub);
    }

}
