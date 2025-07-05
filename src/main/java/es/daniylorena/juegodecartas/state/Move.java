package es.daniylorena.juegodecartas.state;

import javax.swing.text.html.HTMLDocument;
import java.lang.Comparable;
import java.util.Iterator;
import java.util.Set;

public class Move implements Comparable<Move> {

    private Set<Card> playedCards;
    private Player moveOwner;

    public Move(Set<Card> playedCards, Player moveOwner) {
        this.playedCards = playedCards;
        this.moveOwner = moveOwner;
    }

    public Set<Card> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(Set<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public Player getMoveOwner() {
        return moveOwner;
    }

    public void setMoveOwner(Player moveOwner) {
        this.moveOwner = moveOwner;
    }

    public boolean validMove(Set<Card> playedCards) {
        boolean valid = true;
        int referenceNumber = -1;

        for (Card card : playedCards) {
            if (card.getNumber() != 2) {
                if (referenceNumber == -1) {
                    referenceNumber = card.getNumber();
                } else if (card.getNumber() != referenceNumber) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    public int getMovePower() {
        return -1; // Terminar
    }

    @Override
    public int compareTo(Move other) {
        int thisSize = this.playedCards.size();
        int otherSize = other.playedCards.size();

        if (thisSize < otherSize) {
            return -1;
        } else if (thisSize > otherSize) {
            return 1;
        } else { // mismo número de cartas --> válido
            int thisPower = getMovePower();
            int otherPower = other.getMovePower();

            if (thisPower < otherPower) {
                return -1;
            } else if (thisPower > otherPower) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
