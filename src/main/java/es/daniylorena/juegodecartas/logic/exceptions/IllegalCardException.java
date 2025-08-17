package es.daniylorena.juegodecartas.logic.exceptions;

public class IllegalCardException extends RuntimeException {

    private static final String DESCRIPTION = "Formato erróneo";

    public IllegalCardException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
