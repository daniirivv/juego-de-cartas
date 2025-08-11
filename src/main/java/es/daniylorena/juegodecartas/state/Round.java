package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.display.GameDisplay;
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
        int moveSize = move.size();
        if (proposedMove.isValidStructure()) {
            if(move.contains(Card.ORON)){
                if(moveSize != 1) GameController.getInstance().getGameDisplay().notifyOronWrongPlay();
                playable = true;
            }
            // First-move scenario
            else if (this.moves.isEmpty()) {
                if (moveSize != 0) { // No vale pasar de primer turno
                    this.expectedNumberOfCards = proposedMove.playedCards().size();
                    playable = true;
                }
            }
            else { // Not first move
                Move previous = this.moves.peek();
                if (moveSize == 0) return true; // NO SE ALMACENA UN "PASO"
                if ((moveSize == this.expectedNumberOfCards) && (proposedMove.compareTo(previous) >= 0)) {
                    playable = true;
                }
            }
            if (playable) {
                // Elimina las cartas correspondientes al movimiento, y juega las cartas clonadas
                for (Card card : proposedMove.playedCards()) {
                    player.removeCardFromHand(card);
                }
                this.moves.add(proposedMove);
            }
        }
        return playable;
    }

    public void removePlayerFromPlayerList(Player player) {
        this.actualRoundPlayers.remove(player);
    }

    public boolean isPlin() {
        Stack<Move> moves = this.moves;
        if (moves.size() >= 2) {
            Move lastMove = moves.getLast();
            Move previousMove = moves.get(moves.size() - 2);

            return lastMove.compareTo(previousMove) == 0;
        }

        return false;
    }


    public boolean isCloseByPassing(Player closer) {
        Move lastMove = this.moves.peek();
        return lastMove.moveOwner().equals(closer);
    }
}
