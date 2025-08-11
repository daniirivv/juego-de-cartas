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
    private int consecutivePasses = 0;
    private Player lastPlayerWhoPlayed = null;

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
            if (moveSize == 0) {
                consecutivePasses++;
            } else {
                consecutivePasses = 0;
            }
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
                lastPlayerWhoPlayed = player;
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
        int activePlayers = getSimpleListOfSubplayers().size();
        return consecutivePasses == activePlayers -1 && closer.equals(lastPlayerWhoPlayed);
        /*
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
                break;
            }
        }

        boolean samePlayerTurn = closer.equals(lastPlayedMove.moveOwner());

        return allPassed && samePlayerTurn;
         */
    }
}
