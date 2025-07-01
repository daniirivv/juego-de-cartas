package es.daniylorena.juegodecartas.state;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards;

    public Deck(int oros, int copas, int espadas, int bastos) {
        cards = new ArrayList<>();

        addCards(Suit.OROS, oros);
        addCards(Suit.COPAS, copas);
        addCards(Suit.ESPADAS, espadas);
        addCards(Suit.BASTOS, bastos);
    }

    private void addCards(Suit suit, int number) {
        for (int i=1; i<=number; i++) {
            cards.add(new Card(i, suit));
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getNumCards() {
        return cards.size();
    }

    public void shuffle() {
        java.util.Collections.shuffle(cards);
    }

}
