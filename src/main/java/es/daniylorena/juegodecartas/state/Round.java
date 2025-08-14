package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.logic.GameController;
import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Round {

    private final Stack<Move> moves;
    private final CircularList<Player> actualRoundPlayers;

    private Player winner;
    private int expectedNumberOfCards;

    public Round(CircularList<Player> actualRoundPlayers) {
        this.actualRoundPlayers = actualRoundPlayers;

        this.moves = new Stack<>();
        this.winner = null;
    }

    public Stack<Move> getMoves() {
        return this.moves;
    }

    public CircularList<Player> getActualRoundPlayers() {
        return this.actualRoundPlayers;
    }

    public Player getWinner() {
        return this.winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getExpectedNumberOfCards() {
        return this.expectedNumberOfCards;
    }

    public void setExpectedNumberOfCards(int expectedNumberOfCards) {
        this.expectedNumberOfCards = expectedNumberOfCards;
    }

    public List<Player> getSimpleListOfSubplayers() {
        return this.actualRoundPlayers.getPlayerList();
    }

    public Iterator<Player> getCircularIterator() {
        return this.actualRoundPlayers.iterator();
    }

    public boolean playMove(Move proposedMove, Player player) {
        boolean playable = false;
        Set<Card> move = proposedMove.playedCards();
        if (proposedMove.isValidStructure()) {
            if (move.contains(Card.ORON)) {
                playable = manageOronPlay(proposedMove);
            }
            // First-move scenario
            else if (this.moves.isEmpty()) {
                playable = manageFirstMoveScenario(proposedMove);
            } else { // Not first move
                if (proposedMove.isPassing()) return true; // NO SE ALMACENA UN "PASO"
                playable = manageBasicMoveScenario(proposedMove);
            }
            if (playable) {
                // Elimina las cartas correspondientes al movimiento, y juega las cartas clonadas
                deleteCardsFromPlayerHand(proposedMove, player);
                this.moves.add(proposedMove);
            }
        }

        return playable;
    }

    private static boolean manageOronPlay(Move move) {
        if (move.getMoveSize() > 1) {
            GameController.getInstance().getGameDisplay().notifyOronWrongPlay();
            move.playedCards().removeIf(card -> card != Card.ORON);
        }

        return true;
    }

    private boolean manageFirstMoveScenario(Move proposedMove) {
        if (proposedMove.isPassing()) {
            String detail = "No se puede pasar en el primer turno.";
            GameController.getInstance().getGameDisplay().notifyInvalidMove(proposedMove, detail);

            return false;
        } else {
            this.expectedNumberOfCards = proposedMove.playedCards().size();

            return true;
        }
    }

    private boolean manageBasicMoveScenario(Move move) {
        boolean correctCardsNumber = move.getMoveSize() == this.expectedNumberOfCards;
        Move previous = this.moves.peek();
        boolean isBetterMove = move.compareTo(previous) >= 0;
        if (!correctCardsNumber) {
            String detail = "Deben jugarse exactamente el mismo nº de cartas que en el movimiento inicial.";
            GameController.getInstance().getGameDisplay().notifyInvalidMove(move, detail);
        }
        if (!isBetterMove) {
            String detail = "Debes jugar un movimiento de igual o mayor poder al último movimiento jugado.";
            GameController.getInstance().getGameDisplay().notifyInvalidMove(move, detail);
        }

        return correctCardsNumber && isBetterMove;
    }

    private static void deleteCardsFromPlayerHand(Move proposedMove, Player player) {
        for (Card card : proposedMove.playedCards()) {
            player.removeCardFromHand(card);
        }
    }

    public void removePlayerFromPlayerList(Player player) {
        this.actualRoundPlayers.remove(player);
    }

    public boolean isPlin() {
        boolean result = false;
        if (this.moves.size() > 1) {
            Move lastMove = this.moves.getLast();
            Move previousMove = this.moves.get(moves.size() - 2);
            result = lastMove.compareTo(previousMove) == 0;
        }

        return result;
    }

    public boolean isCloseByPassing(Player closer) {
        if (!this.moves.isEmpty()) {
            Move lastMove = this.moves.peek();
            return lastMove.moveOwner().equals(closer);
        }

        return false;
    }
}
