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

    // @TODO: Acabar método
    public int getMovePower() {
        return -1;
    }

    @Override
    public int compareTo(Move other) {
        int thisSize = this.playedCards.size();
        int otherSize = other.playedCards.size();

        if(thisSize!=otherSize) return Integer.compareUnsigned(thisSize, otherSize);
        else{
            int thisPower = this.getMovePower();
            int otherPower = other.getMovePower();
            return Integer.compareUnsigned(thisPower, otherPower);
        }
    }

}
