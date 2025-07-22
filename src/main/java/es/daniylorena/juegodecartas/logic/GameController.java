package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.display.GameDisplayInterface;
import es.daniylorena.juegodecartas.state.*;
import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.*;


public class GameController implements GameControllerInterface {

    private static final GameController instance = new GameController();

    private GameDisplayInterface gameDisplay;
    private Game currentGame;
    private RoleAssigner roleAssigner;

    private GameController() {

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

    public void setRoleAssigner(RoleAssigner roleAssigner) {
        this.roleAssigner = roleAssigner;
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
        // @TODO: CAMBIAR FOR POR WHILE; FIN DE RONDA NO IMPLICA UN JUGADOR MENOS
        for(int i = 0; i < numberOfRounds; i++) {
            Round round = new Round(generateRoundPlayers(i));
            this.currentGame.addRound(round);
            singleRound();
            // PATRÓN OBSERVER PARA ASIGNAR ROL A UN JUGADOR CUANDO SE QUEDA SIN CARTAS
        }

    }

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

    private void singleRound(){
        Round round = this.currentGame.getCurrentRound();

        Player player;
        Move move;
        boolean endOfRound = false;
        do{
            player = round.getActualRoundPlayers().next();
            move = executeTurn(player);
            if(move.isCloseMove()) endOfRound = true;
            if(player.getHand().isEmpty()){
                Role assignedRole = this.roleAssigner.assignRole(player);
                if(assignedRole.equals(Role.VICECULO)){

                }
            }
        }while(!endOfRound);
    }

    private void giveRole(Player player) {
        // PATRÓN OBSERVER
    }

    private boolean checkEndGame() {
        // PATRÓN OBSERVER
        return false;
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
