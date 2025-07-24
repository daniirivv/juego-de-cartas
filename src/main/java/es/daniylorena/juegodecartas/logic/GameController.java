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
    private final Dealer dealer;

    private GameController() {
        this.roleAssigner = new RoleAssigner();
        this.dealer = new Dealer();
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
            this.dealer.divideCards(players, deck);
            matchLoop();
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

        if (presi != null && culo != null) {
            cardExchange(presi, culo, 2);
        }

        if (vicepresi != null && viceculo != null) {
            cardExchange(vicepresi, viceculo, 1);
        }
    }

    private void cardExchange(Player highestRankingPlayer, Player lowestRankingPlayer, int numberOfCardsToExchange) {
        List<Card> bestCardsFromLowerRankingPlayer = new ArrayList<>();
        List<Card> worstCardsFromHigherRankingPlayer = new ArrayList<>();

        for (int i = 1; i <= numberOfCardsToExchange; i++) {
            Card best = lowestRankingPlayer.getBestCard();
            bestCardsFromLowerRankingPlayer.add(best);
            lowestRankingPlayer.removeCardFromHand(best);
        }

        for (int i = 1; i <= numberOfCardsToExchange; i++) {
            Card worst = highestRankingPlayer.getWorstNonRepeatedCard();
            worstCardsFromHigherRankingPlayer.add(worst);
            highestRankingPlayer.removeCardFromHand(worst);
        }

        for (Card card : bestCardsFromLowerRankingPlayer) {
            highestRankingPlayer.addCardToHand(card);
        }

        for (Card card : worstCardsFromHigherRankingPlayer) {
            lowestRankingPlayer.addCardToHand(card);
        }
    }

    private CircularList<Player> generateRoundPlayers(int i) {
        CircularList<Player> roundPlayers;
        if (i == 0) {
            roundPlayers = new CircularList<>(this.currentGame.getPlayers());
        } else {
            Player roundWinner = this.currentGame.getCurrentRound().getWinner();
            List<Player> previousRoundPlayers = this.currentGame.getCurrentRound().getSimpleListOfSubplayers();
            List<Player> actualRoundPlayers = new ArrayList<>(previousRoundPlayers);
            actualRoundPlayers.remove(roundWinner);
            roundPlayers = new CircularList<>(actualRoundPlayers);
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
            }
        } while (!endOfRound && this.currentGame.checkEndGame());
    }

    private Move executeTurn(Player player) {
        Round round = this.currentGame.getCurrentRound();
        boolean invalidMove = true;
        Move move;
        do {
            move = this.gameDisplay.askForAMove(player);
            if (round.playMove(move)) {
                invalidMove = false;
                if (round.isPlin()) {
                    Player skipped = round.getActualRoundPlayers().next();
                    this.gameDisplay.notifyPlin(skipped.getName());
                }
            } else gameDisplay.notifyInvalidMove(move);
        } while (invalidMove);
        return move;
    }
}
