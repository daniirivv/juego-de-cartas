package es.daniylorena.juegodecartas.state;

import java.util.Map;

public record Card(int number, Suit suit) {

    private static final int MAX_POWER = 13;
    private static final int BASE_POWER = 4;

    private static final Card ORON = new Card(1, Suit.OROS);
    private static final Map<Integer, Integer> specialCardsToPowerMap =
            Map.of(
                    3, 10,
                    1, 11,
                    2, 12
            );

    public int getPower() {
        if (this.equals(ORON)) return MAX_POWER; // La carta más poderosa
        else return specialCardsToPowerMap.getOrDefault(this.number, this.number - BASE_POWER + 1);
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
