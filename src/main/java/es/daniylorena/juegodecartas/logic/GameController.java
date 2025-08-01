package es.daniylorena.juegodecartas.logic;

import es.daniylorena.juegodecartas.display.GameDisplay;
import es.daniylorena.juegodecartas.display.GameDisplayInterface;
import es.daniylorena.juegodecartas.state.*;
import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GameController implements GameControllerInterface {

    private static final GameController instance = new GameController();

    private GameDisplayInterface gameDisplay;
    private Game currentGame;
    private final RoleAssigner roleAssigner;

    private GameController() {
        this.roleAssigner = new RoleAssigner();
    }

    public static GameController getInstance() {
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
            Dealer.divideCards(players, deck);
            Dealer.applyRolesIfDefined(players);
            singleGameLoop();
            rematch = this.gameDisplay.askForRematch();
        } while (rematch);
    }

    private ArrayList<Player> initializePlayers(ArrayList<String> playersNames) {
        ArrayList<Player> players = new ArrayList<>();
        for (String name : playersNames) {
            Player player = new Player(name);
            players.add(player);
        }
        return players;
    }

    private void singleGameLoop() {
        boolean endGame;
        do {
            Round round = new Round(generateRoundPlayers());
            this.currentGame.addRound(round);
            roundLoop();
            endGame = this.currentGame.checkEndGame();
        } while (!endGame);
    }

    private CircularList<Player> generateRoundPlayers() {
        int i = this.currentGame.getRounds().size();
        CircularList<Player> roundPlayers;
        if (i == 0) {
            roundPlayers = new CircularList<>(this.currentGame.getPlayers());
        } else {
            Player roundWinner = this.currentGame.getCurrentRound().getWinner();
            List<Player> previousRoundPlayers = this.currentGame.getCurrentRound().getSimpleListOfSubplayers();
            List<Player> actualRoundPlayers = new ArrayList<>(previousRoundPlayers);
            int roundWinnerIndex = actualRoundPlayers.indexOf(roundWinner);
            roundPlayers = new CircularList<>(actualRoundPlayers, roundWinner);
        }
        return roundPlayers;
    }

    private void roundLoop() {
        Round round = this.currentGame.getCurrentRound();

        Player player;
        Move move;
        boolean endOfRound = false;
        do {
            player = round.getActualRoundPlayers().next();
            move = executeTurn(player);
            if (move.isCloseMove()) {
                round.setWinner(player);
                endOfRound = true;
            }
            if (player.getHand().isEmpty()) {
                this.roleAssigner.assignRole(player);
                round.removePlayerFromPlayerList(player);
            }
        } while (!endOfRound && !this.currentGame.checkEndGame());
    }

    private Move executeTurn(Player player) {
        this.gameDisplay.printTurn(player);
        Round round = this.currentGame.getCurrentRound();
        boolean invalidMove = true;
        Move move;
        do {
            String[] proposedMove = this.gameDisplay.askForAMove(player);
            move = createCloneMove(proposedMove, player);
            if (verifyCardsOwnership(move, player)) {
                if (round.playMove(move, player)) {
                    invalidMove = false;
                    if (round.isPlin()) {
                        Player skipped = round.getActualRoundPlayers().next();
                        this.gameDisplay.notifyPlin(skipped.getName());
                    }
                } else gameDisplay.notifyInvalidMove(move);
            } else {
                this.gameDisplay.notifyInvalidMove(move);
            }
        } while (invalidMove);
        return move;
    }

    private boolean verifyCardsOwnership(Move proposedMove, Player player) {
        for (Card clone : proposedMove.playedCards()) {
            if (!player.getHand().contains(clone)) return false;
        }
        return true;
    }

    private Move createCloneMove(String[] proposedMove, Player player) {
        Set<Card> cards = new HashSet<>();
        for (String inputCard : proposedMove) {
            int number = Integer.parseInt(inputCard.substring(0, inputCard.length() - 1));
            // int number = inputCard.charAt(0);
            Suit suit = GameDisplay.SUIT_TO_CHAR_MAP.get(inputCard.charAt(1));
            cards.add(new Card(number, suit));
        }
        return new Move(cards, player);
    }
}
