package es.daniylorena.juegodecartas.display;

import es.daniylorena.juegodecartas.state.Card;
import es.daniylorena.juegodecartas.state.Move;
import es.daniylorena.juegodecartas.state.Player;

import java.util.List;

public interface GameDisplayInterface {

    String[] askForAMove(Player turnOwner);

    void notifyInvalidMove(Move move, String detail);

    void notifyCloseByPassing();

    boolean askForRematch();

    void notifyPlin(String skippedPlayer);

    void notifyRole(Player player);

    void notifyPass(Player player);

    void printTurn(Player player);

    void printHand(List<Card> hand);

    void notifyOronWrongPlay();
}
