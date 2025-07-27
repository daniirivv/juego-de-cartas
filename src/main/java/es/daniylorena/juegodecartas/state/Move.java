package es.daniylorena.juegodecartas.state;

import java.lang.Comparable;
import java.util.Iterator;
import java.util.Set;

public class Move implements Comparable<Move> {

    private static final int JOKER = 2;
    private static final int CLOSE_CARD = 1;

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

    // TODO: Agregar llamadas al display para notificar en cada caso
    public boolean isValidStructure() {
        int moveSize = this.playedCards.size();
        if (moveSize == 0 || moveSize == 1) return true;
        if (moveSize > 4) return false;
        else{
            boolean valid = true;
            Iterator<Card> iterator = this.playedCards.iterator();
            int firstNumber = iterator.next().getNumber();
            do {
                int number = iterator.next().getNumber();
                if(firstNumber != number) valid = false;
            } while (!valid && iterator.hasNext());
            return valid;
        }
    }

    public int getMovePower() {
        if (!isValidStructure()) {
            return 0;
        }
        // Buscar la primera carta que no sea un 2
        for (Card card : this.playedCards) {
            if (card.getNumber() != 2) {
                return card.getPower();
            }
        }
        // Si solo había comodines
        return new Card(2, Suit.OROS).getPower();
    }

    @Override
    public int compareTo(Move other) {
        int thisSize = this.playedCards.size();
        int otherSize = other.playedCards.size();
        if (thisSize != otherSize) return Integer.compareUnsigned(thisSize, otherSize);
        else {
            int thisPower = this.getMovePower();
            int otherPower = other.getMovePower();
            return Integer.compareUnsigned(thisPower, otherPower);
        }
    }

    public boolean isCloseMove() {
        boolean result = true;
        Iterator<Card> iterator = this.playedCards.iterator();
        while (iterator.hasNext() && result) {
            int cardValue = iterator.next().getNumber();
            if (cardValue != JOKER && cardValue != CLOSE_CARD) {
                result = false;
            }
        }
        return result;
    }
}
