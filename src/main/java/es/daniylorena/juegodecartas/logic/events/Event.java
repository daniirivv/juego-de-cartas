package es.daniylorena.juegodecartas.logic.events;

public class Event<T> {

    private final EventType eventType;
    private final T context;

    public Event(EventType eventType, T context) {
        this.eventType = eventType;
        this.context = context;
    }

    public EventType getEventType() {
        return eventType;
    }

    public T getContext() {
        return context;
    }
}
