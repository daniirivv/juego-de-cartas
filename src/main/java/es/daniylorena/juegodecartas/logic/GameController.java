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
    public void launchGame(ArrayList<String> playersNames) {
        ArrayList<Player> players = initializePlayers(playersNames);
        Deck deck = new Deck();
        this.currentGame = new Game(players, deck);
        shuffleDeck();
        distributeCardsAmongPlayers();
        playGame();
    }

    private void shuffleDeck(){
        this.currentGame.getDeck().shuffle();
    }

    private ArrayList<Player> initializePlayers(ArrayList<String> playersNames) {
        ArrayList<Player> players = new ArrayList<>();
        for(String name : playersNames){
            Player player = new Player(name);
            players.add(player);
        }
        return players;
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
        boolean rematch = false;
        do{
            singleMatch();
            rematch = this.gameDisplay.askForRematch();
        }while(rematch);
    }

    private void singleMatch() {
        applyRolesIfDefined();
        boolean endOfGame = false;
        do {
            Round currentRound = new Round();
            this.currentGame.addRound(currentRound);
            boolean endOfRound;
            do{
                Move move = executeTurn();
                endOfRound = move.isCloseMove();
            }while(!endOfRound);
            /* @TODO:
                    * Comprobar si algún jugador se ha quedado sin cartas -> asignar rol
                    * Si solamente queda un jugador con cartas -> fin del juego (es culo)
            */
        }while(!endOfGame);
    }

    private void applyRolesIfDefined() {

    }

    private Move executeTurn(){
        Round round = this.currentGame.getCurrentRound();
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
