package es.daniylorena.juegodecartas.display;

import es.daniylorena.juegodecartas.state.Move;
import es.daniylorena.juegodecartas.state.Player;

public interface GameDisplayInterface {

    Move askForAMove(Player turnOwner);

    void notifyInvalidMove(Move move);

    boolean askForRematch();

    default void notifyPlin(String skippedPlayer) {
        System.out.println("¡PLIN! Se salta el turno de " + skippedPlayer);
    }
}
