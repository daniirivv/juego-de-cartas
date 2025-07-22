package es.daniylorena.juegodecartas.logic.exceptions;

public class InsufficientPlayersException extends RuntimeException {

    private static final String DESCRIPTION = "Deben jugar, como mínimo, 3 jugadores";

    public InsufficientPlayersException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
