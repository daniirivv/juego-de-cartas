package es.daniylorena.juegodecartas.logic.exceptions;

public class IllegalPlayerNameException extends RuntimeException {

    private static final String DESCRIPTION = "El nombre no es válido";

    public IllegalPlayerNameException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
