package es.daniylorena.juegodecartas.state;

import java.util.Objects;

public class Carta {

    private final int numero;
    private final Palo palo;

    public Carta(int numero, Palo palo) {
        this.numero = numero;
        this.palo = palo;
    }

    public int getNumero() {
        return numero;
    }

    public Palo getPalo() {
        return palo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carta carta = (Carta) o;
        return numero == carta.numero && palo == carta.palo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, palo);
    }

}
