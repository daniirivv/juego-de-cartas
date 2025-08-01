package es.daniylorena.juegodecartas.state;

import java.util.LinkedList;

public record Deck(LinkedList<Card> cards) {

    private static final int DEFAULT_CARDS_PER_SUIT_SPANISH_DECK = 12;

    public Deck() {
        this(new LinkedList<>());
        for (Suit suit : Suit.values()) {
            addCards(suit, DEFAULT_CARDS_PER_SUIT_SPANISH_DECK);
        }
    }

    private void addCards(Suit suit, int number) {
        for (int i = 1; i <= number; i++) {
            this.cards.add(new Card(i, suit));
        }
    }

    public int size() {
        return this.cards.size();
    }

    public void shuffle() {
        java.util.Collections.shuffle(this.cards);
    }

    public boolean isEmpty() {
        return this.cards().isEmpty();
    }

    public Card draw() {
        return this.cards.removeFirst();
    }

    public int getSize() {
        return this.cards().size();
    }
}
