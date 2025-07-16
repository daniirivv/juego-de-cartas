package es.daniylorena.juegodecartas.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Deck {

    private static final int DEFAULT_CARDS_PER_SUIT = 12;
    private List<Card> cards;

    public Deck(int oros, int copas, int espadas, int bastos) {
        this.cards = new LinkedList<>();
        addCards(Suit.OROS, oros);
        addCards(Suit.COPAS, copas);
        addCards(Suit.ESPADAS, espadas);
        addCards(Suit.BASTOS, bastos);
    }

    public Deck() {
        this.cards = new LinkedList<>();
        for(Suit suit : Suit.values()){
            addCards(suit, DEFAULT_CARDS_PER_SUIT);
        }
    }

    private void addCards(Suit suit, int number) {
        for (int i=1; i<=number; i++) {
            this.cards.add(new Card(i, suit));
        }
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public int size() {
        return this.cards.size();
    }

    public void shuffle() {
        java.util.Collections.shuffle(this.cards);
    }

    public boolean isEmpty() {
        return this.getCards().isEmpty();
    }

    public Card getFirstCard() {
        return this.cards.getFirst();
    }
}
