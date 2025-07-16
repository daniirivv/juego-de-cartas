package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.utilities.CircularList;

import java.util.List;
import java.util.Stack;
import java.util.Set;

public class Round {

    private final Stack<Move> moves;
    private final CircularList<Player> actualRoundPlayers;

    private Player turnOwner;
    private Player winner;
    private int expectedNumberOfCards;

    public Round(CircularList<Player> actualRoundPlayers) {
        this.actualRoundPlayers = actualRoundPlayers;

        this.moves = new Stack<>();
        this.turnOwner = actualRoundPlayers.next();
        this.winner = null;
    }

    public Stack<Move> getMoves() {
        return moves;
    }

    public CircularList<Player> getActualRoundPlayers() {
        return actualRoundPlayers;
    }

    public Player getTurnOwner() {
        return turnOwner;
    }

    public void setTurnOwner(Player turnOwner) {
        this.turnOwner = turnOwner;
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
}
