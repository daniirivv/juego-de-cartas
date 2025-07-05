package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.state.Deck;
import es.daniylorena.juegodecartas.state.Game;
import es.daniylorena.juegodecartas.state.Player;

import java.util.ArrayList;

public class GameController implements GameControllerInterface{

    private Game currentGame;

    public GameController() {

    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    @Override
    public void createAndStartNewGame(ArrayList<String> playersNames) {
        ArrayList<Player> players = new ArrayList<>();
        for(String name : playersNames){
            Player player = createPlayer(name);
            players.add(player);
        }
        Game game = new Game(players);
        setCurrentGame(game);
        startCurrentGame();
    }

    private Player createPlayer(String name){
        return new Player(name);
    }

    private void startCurrentGame() {
        Game currentGame = this.currentGame;

    }

    private void assignDeckForCurrentGame(){
        Deck deck = new Deck();
    }

}
