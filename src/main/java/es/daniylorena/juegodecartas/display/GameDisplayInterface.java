package es.daniylorena.juegodecartas.display;

import es.daniylorena.juegodecartas.state.Move;

public interface GameDisplayInterface {

    void createNewGame();

    Move askForAMove();
}
