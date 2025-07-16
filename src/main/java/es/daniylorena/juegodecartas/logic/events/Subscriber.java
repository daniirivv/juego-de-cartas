package es.daniylorena.juegodecartas.logic.events;

public interface Subscriber {

    public <T> void  update(T context);

}
