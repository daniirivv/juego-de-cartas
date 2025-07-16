package es.daniylorena.juegodecartas.logic.events;

public interface Subscriber {

    <T> void  update(T context);

}
