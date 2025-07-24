package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Set;

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
        return moves;
    }

    public CircularList<Player> getActualRoundPlayers() {
        return actualRoundPlayers;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getExpectedNumberOfCards() {
        return expectedNumberOfCards;
    }

    public void setExpectedNumberOfCards(int expectedNumberOfCards) {
        this.expectedNumberOfCards = expectedNumberOfCards;
    }

    public boolean playMove(Move move) {
        Set<Card> playedCards = move.getPlayedCards();
        if (moves.isEmpty()) {
            expectedNumberOfCards = playedCards.size(); // Determina el número de cartas a jugar quien empieza la ronda
            moves.push(move);
            return true;
        }
        if (playedCards.size() != expectedNumberOfCards && !playedCards.isEmpty()) return false; // Jugada inválida
        if (!move.isValid(playedCards)) return false;
        if (move.compareTo(moves.peek()) <= 0) return false;
        if (playedCards.isEmpty()) return true; // Pasar = válido
        moves.push(move);
        return true;
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
