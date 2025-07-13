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
            Player closer = singleRound();
            giveRole(closer);
            endOfGame = checkEndGame();
        }while(!endOfGame);
    }

    private void applyRolesIfDefined() {
        Player presi = null;
        Player vice = null;
        Player viceculo = null;
        Player culo = null;

        for (Player p : this.currentGame.getPlayers()) {
            switch (p.getRole()) {
                case PRESI -> presi = p;
                case VICEPRESI -> vice = p;
                case VICECULO -> viceculo = p;
                case CULO -> culo = p;
            }
        }

        // Presidente <--> Culo
        if (presi != null && culo != null) {
            culo.removeCardFromHand(culo.getBestCard());
            culo.removeCardFromHand(culo.getBestCard());


        }

    }

    private Player singleRound(){
        return null;
    }

    private void giveRole(Player closer) {

    }

    private boolean checkEndGame() {
        return false;
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
