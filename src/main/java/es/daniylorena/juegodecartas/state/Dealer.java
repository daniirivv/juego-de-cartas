package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.ArrayList;
import java.util.Iterator;

public class Dealer {

    public void divideCards(ArrayList<Player> playerList, Deck deck) {
        CircularList<Player> players = new CircularList<>(playerList);
        Iterator<Player> circularIterator = players.iterator();
        do{
            Player player = circularIterator.next();
            player.addCardToHand(deck.getFirstCard());
        }while(!deck.isEmpty());
    }

}
