package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.state.Deck;
import es.daniylorena.juegodecartas.state.Game;
import es.daniylorena.juegodecartas.state.Player;

import java.util.ArrayList;
import java.util.List;

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
        Game currentGame = new Game(initializePlayers(playersNames), new Deck());
        startCurrentGame();
    }

    private ArrayList<Player> initializePlayers(ArrayList<String> playersNames) {
        ArrayList<Player> players = new ArrayList<>();
        for(String name : playersNames){
            Player player = new Player(name);
            players.add(player);
        }
        return players;
    }

    private void startCurrentGame() {
        this.currentGame.getDeck().shuffle();
        distributeCardsAmongPlayers();
        playGame();
    }

    private void distributeCardsAmongPlayers() {
        List<Player> players = this.currentGame.getPlayers();
        Deck deck = this.currentGame.getDeck();
        // Estructura de datos circular en jugadores para repartir constantemente.
    }

    private void playGame() {

    }

}
