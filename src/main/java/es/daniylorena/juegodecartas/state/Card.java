package es.daniylorena.juegodecartas.state;

public record Card(int number, Suit suit) {

    public int getPower() { // Para comprobar en la lógica si la carta echada es mayor que la anterior
        if (this.number == 1 && this.suit == Suit.OROS) return 16; // La carta más poderosa
        if (this.number == 2) return 15; // Comodín - sustituye a todo menos al as de oros
        if (this.number == 1) return 14;
        if (this.number == 3) return 13; // 3 = 13
        return this.number; // 4–12 normales
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.number == card.number && this.suit == card.suit;
    }

    @Override
    public String toString() {
        return this.number + " de " + this.suit.name().toLowerCase();
    }
}
