package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.display.GameDisplayInterface;
import es.daniylorena.juegodecartas.state.*;
import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.*;


public class GameController implements GameControllerInterface {

    private static final GameController instance = new GameController();

    private GameDisplayInterface gameDisplay;
    private Game currentGame;
    private final RoleAssigner roleAssigner;

    private GameController() {
        this.roleAssigner = new RoleAssigner();
    }

    public static GameController getInstance(){
        return instance;
    }

    public GameDisplayInterface getGameDisplay() {
        return gameDisplay;
    }

    public void setGameDisplay(GameDisplayInterface gameDisplay) {
        this.gameDisplay = gameDisplay;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public RoleAssigner getRoleAssigner() {
        return roleAssigner;
    }

    @Override
    public void launchGame(ArrayList<String> playersNames) {
        boolean rematch;
        do {
            ArrayList<Player> players = initializePlayers(playersNames);
            Deck deck = new Deck();
            this.currentGame = new Game(players, deck);
            this.roleAssigner.initializeRoles(players.size());
            this.currentGame.shuffleDeck();
            // OPTIMIZE: Crear una clase Dealer que encapsule la lógica de reparto de cartas
            distributeCardsAmongPlayers();
            matchLoop();
            rematch = this.gameDisplay.askForRematch();
        } while(rematch);
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

    private void matchLoop() {
        // OPTIMIZE: Agregar esta responsabilidad al Dealer
        applyRolesIfDefined();
        boolean endGame;
        do {
            Round round = new Round(generateRoundPlayers(this.currentGame.getRounds().size()));
            this.currentGame.addRound(round);
            roundLoop();
            endGame = this.currentGame.checkEndGame();
        } while (!endGame);
    }

    // OPTIMIZE: Revisar
    private void applyRolesIfDefined() {
        Player presi = null;
        Player vicepresi = null;
        Player viceculo = null;
        Player culo = null;

        for (Player p : this.currentGame.getPlayers()) {
            switch (p.getRole()) {
                case PRESI -> presi = p;
                case VICEPRESI -> vicepresi = p;
                case VICECULO -> viceculo = p;
                case CULO -> culo = p;
            }
        }

        // Presidente <--> Culo
        if (presi != null && culo != null) {

            Card bestCard1 = culo.getBestCard();
            culo.removeCardFromHand(bestCard1);

            Card bestCard2 = culo.getBestCard();
            culo.removeCardFromHand(bestCard2);

            presi.addCardToHand(bestCard1);
            presi.addCardToHand(bestCard2);

            Card worstCard1 = presi.getWorstNonRepeatedCard();
            presi.removeCardFromHand(worstCard1);

            Card worstCard2 = presi.getWorstNonRepeatedCard();
            presi.removeCardFromHand(worstCard2);

            culo.addCardToHand(worstCard1);
            culo.addCardToHand(worstCard2);
        }

        // Vicepresidente <--> Viceculo
        if (vicepresi != null && viceculo != null) {

            Card bestCard = viceculo.getBestCard();
            viceculo.removeCardFromHand(bestCard);

            vicepresi.addCardToHand(bestCard);

            Card worstCard = vicepresi.getWorstNonRepeatedCard();
            vicepresi.removeCardFromHand(worstCard);

            viceculo.addCardToHand(worstCard);
        }

    }

    private CircularList<Player> generateRoundPlayers(int i) {
        CircularList<Player> roundPlayers;
        if(i == 0){
            roundPlayers = new CircularList<>(this.currentGame.getPlayers());
        }
        else {
            Player roundWinner = this.currentGame.getCurrentRound().getWinner();
            List<Player> previousRoundPlayers = this.currentGame.getCurrentRound().getSimpleListOfSubplayers();
            List<Player> actualRoundPlayers = new ArrayList<>(previousRoundPlayers);
            actualRoundPlayers.remove(roundWinner);
            roundPlayers = new CircularList<>(actualRoundPlayers);
        }
        return roundPlayers;
    }

    private void roundLoop(){
        Round round = this.currentGame.getCurrentRound();

        Player player;
        Move move;
        boolean endOfRound = false;
        do{
            player = round.getActualRoundPlayers().next();
            move = executeTurn(player);
            if(move.isCloseMove()){
                round.setWinner(player);
                endOfRound = true;
            }
            if(player.getHand().isEmpty()){
                this.roleAssigner.assignRole(player);
            }
        }while(!endOfRound && this.currentGame.checkEndGame());
    }

    private Move executeTurn(Player player){
        Round round = this.currentGame.getCurrentRound();
        boolean invalidMove = true;
        Move move;
        do{
            move = gameDisplay.askForAMove(player);
            if (round.playMove(move)) {
                invalidMove = false;
                if (isPlin()) {
                    Player skipped = round.getActualRoundPlayers().next();
                    // TODO: Los print van al display (gameDisplay.notifyPlin())
                    System.out.println("¡PLIN! Se salta el turno de " + skipped.getName());
                }
            } else gameDisplay.notifyInvalidMove(move);
        }while(invalidMove);
        return move;
    }

    // TODO: A Move.java
    private boolean isPlin() {
        boolean plin = false;
        Round round = this.currentGame.getCurrentRound();
        Stack<Move> moves = round.getMoves();
        if (moves.size() >= 2) {
            Move lastMove = moves.get(moves.size() - 1);
            Move previousMove = moves.get(moves.size() - 2);
            if (lastMove.compareTo(previousMove) == 0) {
                plin = true;
            }
        }
        return plin;
    }

    // TODO: A Move.java
    private boolean isCloseByPassing(Player closer) {
        Round round = this.currentGame.getCurrentRound();
        Stack<Move> moves = round.getMoves();

        Move lastPlayedMove = null;
        int lastPlayedIndex = -1;

        // Encontrar el último movimiento con cartas
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (!move.getPlayedCards().isEmpty()) {
                lastPlayedMove = move;
                lastPlayedIndex = i;
            }
        }

        if (lastPlayedMove == null) return false;

        // Comprobar si todos los siguientes movimientos fueron pases
        boolean allPassed = true;
        for (int i = lastPlayedIndex + 1; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (!move.getPlayedCards().isEmpty()) {
                allPassed = false;
            }
        }

        boolean samePlayerTurn = closer.equals(lastPlayedMove.getMoveOwner());

        return allPassed && samePlayerTurn;
    }

}
