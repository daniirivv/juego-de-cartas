package es.daniylorena.juegodecartas.state;

import java.util.Stack;
import java.util.Set;

public class Round {

    private Stack<Move> moves;
    private int expectedNumberOfCards;

    public Round(Stack<Move> moves, int expectedNumberOfCards) {
        this.moves = moves;
        this.expectedNumberOfCards = expectedNumberOfCards;
    }

    public int getExpectedNumberOfCards() {
        return expectedNumberOfCards;
    }

    public Stack<Move> getMoves() {
        return moves;
    }

    public boolean playMove(Move move) {
        Set<Card> playedCards = move.getPlayedCards();

        if (moves.isEmpty()) {
            expectedNumberOfCards = playedCards.size(); // Determina el número de cartas a jugar quien empieza la ronda
            moves.push(move);
            return true;
        }

        if (playedCards.size() != expectedNumberOfCards) return false; // Jugada inválida
        if (!move.validMove(playedCards)) return false;
        if (move.compareTo(moves.peek()) <= 0) return false;

        moves.push(move);
        return true;
    }

}
