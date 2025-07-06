package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.display.GameDisplayInterface;
import es.daniylorena.juegodecartas.state.*;
import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GameController implements GameControllerInterface{

    private Game currentGame;

    private GameDisplayInterface gameDisplay;

    public GameController() {

    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public GameDisplayInterface getGameDisplay() {
        return gameDisplay;
    }

    public void setGameDisplay(GameDisplayInterface gameDisplay) {
        this.gameDisplay = gameDisplay;
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
        CircularList<Player> players = new CircularList<>(this.currentGame.getPlayers());
        Iterator<Player> circularIterator = players.iterator();
        Deck deck = this.currentGame.getDeck();
        do{
            Player player = circularIterator.next();
            player.addCardToHand(deck.getFirstCard());
        }while(!deck.isEmpty());
    }

    private void playGame() {
        boolean exitGame = false;
        do{
            applyRolesIfDefined(this.currentGame.getPlayers());
            boolean endOfRound = false;
            do {
                Round currentRound = new Round();
                this.currentGame.addRound(currentRound);
                Move move = executeTurn(currentRound);
                endOfRound = this.currentGame.checkEndRound(move);
            }while(!endOfRound);
        }while(!exitGame);
    }

    private void applyRolesIfDefined(List<Player> players) {

    }

    private Move executeTurn(Round round){
        boolean invalidMove = true;
        Move move;
        do{
            move = gameDisplay.askForAMove(round.getTurnOwner());
            if (round.playMove(move)) {
                invalidMove = false;
                gameDisplay.notifyInvalidMove();
            }
        }while(invalidMove);
        return move;
    }

}
