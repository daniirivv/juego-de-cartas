package es.daniylorena.juegodecartas.logic.exceptions;

public class IllegalCardException extends RuntimeException {

    private static final String DESCRIPTION = "No existen cartas con el caracter indicado";

    public IllegalCardException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
