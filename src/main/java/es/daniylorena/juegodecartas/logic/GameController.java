package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.state.Game;
import es.daniylorena.juegodecartas.state.Player;

import java.util.ArrayList;

public class GameController implements GameControllerInterface{

    private Game currentGame;

    @Override
    public void createAndStartNewGame(ArrayList<String> playersNames) {

    }

    private Player createPlayer(String name){
        return new Player(name);
    }

}
