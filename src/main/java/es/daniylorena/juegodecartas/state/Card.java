package es.daniylorena.juegodecartas.state;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Card {

    private final int number;
    private final Suit suit;

    public Card(int number, Suit suit) {
        this.number = number;
        this.suit = suit;
    }

    public int getNumber() {
        return number;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getPower() { // Para comprobar en la lógica si la carta echada es mayor que la anterior
        if (this.number == 1 && this.suit == Suit.OROS) return 16; // La carta más poderosa
        if (number == 2) return 15; // Comodín - sustituye a todo menos al as de oros
        if (number == 1) return 14;
        if (number == 3) return 13; // 3 = 13
        return number; // 4–12 normales
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return number == card.number && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, suit);
    }

    @Override
    public String toString() {
        return number + " de " + suit.name().toLowerCase();
    }
}
