package es.daniylorena.juegodecartas.display;

import es.daniylorena.juegodecartas.state.Move;
import es.daniylorena.juegodecartas.state.Player;

public interface GameDisplayInterface {

    void createNewGame();

    Move askForAMove(Player turnOwner);
}
