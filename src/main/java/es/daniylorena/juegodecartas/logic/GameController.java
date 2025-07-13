package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.display.GameDisplayInterface;
import es.daniylorena.juegodecartas.state.*;
import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.*;


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

    private ArrayList<Player> initializePlayers(ArrayList<String> playersNames) {
        ArrayList<Player> players = new ArrayList<>();
        for(String name : playersNames){
            Player player = new Player(name);
            players.add(player);
        }
        return players;
    }

    private void shuffleDeck(){
        this.currentGame.getDeck().shuffle();
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
        boolean rematch;
        do{
            singleMatch();
            rematch = this.gameDisplay.askForRematch();
        }while(rematch);
    }

    private void singleMatch() {
        applyRolesIfDefined();
        int numberOfRounds = this.currentGame.getPlayers().size()-1;
        for(int i = 0; i < numberOfRounds; i++) {
            Round round = new Round(generateRoundPlayers(i));
            this.currentGame.addRound(round);
            singleRound();
            // PATRÓN OBSERVER PARA ASIGNAR ROL A UN JUGADOR CUANDO SE QUEDA SIN CARTAS
        }

    }

    private void applyRolesIfDefined() {
        // Revisar los roles de los jugadores de la partida e intercambiar las cartas correspondientes
    }

    private CircularList<Player> generateRoundPlayers(int i) {
        CircularList<Player> roundPlayers;
        if(i == 0){
            roundPlayers = new CircularList<>(this.currentGame.getPlayers());
        }
        else {
            Player roundWinner = this.currentGame.getLastRound().getWinner();
            List<Player> previousRoundPlayers = this.currentGame.getLastRound().getSimpleListOfSubplayers();
            List<Player> actualRoundPlayers = new ArrayList<>(previousRoundPlayers);
            actualRoundPlayers.remove(roundWinner);
            roundPlayers = new CircularList<>(actualRoundPlayers);
        }
        return roundPlayers;
    }

    private void singleRound(){
        Move move;
        boolean endOfRound;
        do{
            move = executeTurn();
            endOfRound = move.isCloseMove() || checkEndGame();
        }while(!endOfRound);
    }

    private void giveRole(Player closer) {
        // PATRÓN OBSERVER
    }

    private boolean checkEndGame() {
        // PATRÓN OBSERVER
        return false;
    }

    private Move executeTurn(){
        Round round = this.currentGame.getLastRound();
        boolean invalidMove = true;
        Move move;
        do{
            move = gameDisplay.askForAMove(round.getTurnOwner());
            if (round.playMove(move)) {
                invalidMove = false;
            } else gameDisplay.notifyInvalidMove();
        }while(invalidMove);
        return move;
    }

}
