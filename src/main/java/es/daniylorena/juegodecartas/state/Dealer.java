package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.logic.GameController;
import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.List;
import java.util.Optional;

public class Dealer {

    public static int PRESI_CULO_EXCHANGE = 2;
    public static int VICEPRESI_VICECULO_EXCHANGE = 1;

    public static void divideCards(List<Player> playerList, Deck deck) {
        CircularList<Player> players = new CircularList<>(playerList);
        do {
            Player player = players.next();
            player.addCardToHand(deck.draw());
        } while (!deck.isEmpty());
    }

    public static void applyRolesIfDefined(List<Player> players) {
        Player presi = null;
        Player vicepresi = null;
        Player viceculo = null;
        Player culo = null;

        for (Player p : players) {
            Optional<Role> rol = Optional.ofNullable(p.getRole());
            if (rol.isPresent()) {
                switch (rol.get()) {
                    case PRESI -> presi = p;
                    case VICEPRESI -> vicepresi = p;
                    case VICECULO -> viceculo = p;
                    case CULO -> culo = p;
                    case NEUTRO -> {continue;}
                }
            }
        }

        if (presi != null && culo != null) {
            cardExchange(presi, culo, PRESI_CULO_EXCHANGE);
        }

        if (vicepresi != null && viceculo != null) {
            cardExchange(vicepresi, viceculo, VICEPRESI_VICECULO_EXCHANGE);
        }
    }

    private static void cardExchange(Player winner, Player loser, int exchangedCards) {
        for (int i = 1; i <= exchangedCards; i++) {
            Card best = loser.takeBestCard();
            Card worst = winner.takeWorstNonRepeatedCard();

            loser.addCardToHand(worst);
            winner.addCardToHand(best);
        }
        GameController.getInstance().getGameDisplay().notifyCardExchange(winner, loser);
    }
}
