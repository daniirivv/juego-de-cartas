package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.Iterator;
import java.util.List;

public class Dealer {

    public void divideCards(List<Player> playerList, Deck deck) {
        CircularList<Player> players = new CircularList<>(playerList);
        do {
            Player player = players.next();
            player.addCardToHand(deck.takeCard());
        } while (!deck.isEmpty());
    }

    // OPTIMIZE: Revisar y agregar esta responsabilidad al Dealer
    public void applyRolesIfDefined(List<Player> players) {
        Player presi = null;
        Player vicepresi = null;
        Player viceculo = null;
        Player culo = null;

        for (Player p : players) {
            switch (p.getRole()) {
                case PRESI -> presi = p;
                case VICEPRESI -> vicepresi = p;
                case VICECULO -> viceculo = p;
                case CULO -> culo = p;
            }
        }

        if (presi != null && culo != null) {
            cardExchange(presi, culo, 2);
        }

        if (vicepresi != null && viceculo != null) {
            cardExchange(vicepresi, viceculo, 1);
        }
    }

    private void cardExchange(Player winner, Player loser, int exchangedCards) {
        for (int i = 1; i <= exchangedCards; i++) {
            Card best = loser.getBestCard();

            Card worst = winner.getWorstNonRepeatedCard();

            loser.addCardToHand(worst);
            winner.addCardToHand(best);
        }
    }
}
