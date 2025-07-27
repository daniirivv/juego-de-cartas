package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.Iterator;
import java.util.List;
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

    public boolean playMove(Move move) {
        boolean playable = false;
        int numberOfPlayedCards = move.playedCards().size();
        if (move.isValidStructure()){
            // First-move scenario
            if(this.moves.isEmpty()){
                if (numberOfPlayedCards != 0){ // No vale pasar de primer turno
                    this.expectedNumberOfCards = move.playedCards().size();
                    playable = true;
                }
            } else { // Not first move
                Move previous = this.moves.peek();
                if (numberOfPlayedCards == 0) return true; // NO SE ALMACENA UN "PASO"
                if((numberOfPlayedCards == this.expectedNumberOfCards) && (move.compareTo(previous) >= 0)){
                    playable = true;
                }
                if(playable) this.moves.push(move);
            }
        }
        return playable;
    }

    public List<Player> getSimpleListOfSubplayers() {
        return this.actualRoundPlayers.getElements();
    }

    public Iterator<Player> getCircularIterator() {
        return this.actualRoundPlayers.iterator();
    }

    public boolean isPlin() {
        Stack<Move> moves = this.moves;
        if (moves.size() >= 2) {
            Move lastMove = moves.get(moves.size() - 1);
            Move previousMove = moves.get(moves.size() - 2);

            return lastMove.compareTo(previousMove) == 0;
        }

        return false;
    }


    private boolean isCloseByPassing(Player closer) {
        Stack<Move> moves = this.moves;

        Move lastPlayedMove = null;
        int lastPlayedIndex = -1;

        // FIXME: Asumir que solo se guardan los movimientos donde se han jugado cartas
        // Encontrar el último movimiento con cartas
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (!move.playedCards().isEmpty()) {
                lastPlayedMove = move;
                lastPlayedIndex = i;
            }
        }

        if (lastPlayedMove == null) return false;

        // Comprobar si todos los siguientes movimientos fueron pases
        boolean allPassed = true;
        for (int i = lastPlayedIndex + 1; i < moves.size(); i++) {
            Move move = moves.get(i);
            if (!move.playedCards().isEmpty()) {
                allPassed = false;
            }
        }

        boolean samePlayerTurn = closer.equals(lastPlayedMove.moveOwner());

        return allPassed && samePlayerTurn;
    }
}
