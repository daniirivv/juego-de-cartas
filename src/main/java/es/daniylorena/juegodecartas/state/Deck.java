package es.daniylorena.juegodecartas.state;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards;

    public Deck(int oros, int copas, int espadas, int bastos) {
        this.cards = new ArrayList<>();

        addCards(Suit.OROS, oros);
        addCards(Suit.COPAS, copas);
        addCards(Suit.ESPADAS, espadas);
        addCards(Suit.BASTOS, bastos);
    }

    private void addCards(Suit suit, int number) {
        for (int i=1; i<=number; i++) {
            this.cards.add(new Card(i, suit));
        }
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public int getNumCards() {
        return this.cards.size();
    }

    public void shuffle() {
        java.util.Collections.shuffle(this.cards);
    }

}
