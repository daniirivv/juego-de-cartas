package es.daniylorena.juegodecartas.state;

import java.util.Iterator;
import java.util.Set;

public record Move(Set<Card> playedCards, Player moveOwner) implements Comparable<Move> {

    private static final int JOKER = 2;
    private static final int CLOSE_CARD = 1;

    public int getMoveSize() {
        return this.playedCards.size();
    }

    // TODO: Agregar llamadas al display para notificar en cada caso
    public boolean isValidStructure() {
        return isValidStructure(this.playedCards);
    }

    public static boolean isValidStructure(Set<Card> playedCards){
        int moveSize = playedCards.size();
        if (moveSize == 0 || moveSize == 1) return true;
        if (moveSize > 8) return false; // MAX: 4 del mismo palo + 4 comodines
        else {
            boolean valid = true;
            Iterator<Card> iterator = playedCards.iterator();
            int firstNonWildcardNumber = JOKER;
            do{
                int actualNumber = iterator.next().number();
                if(actualNumber != JOKER){
                    if(firstNonWildcardNumber == JOKER){ // Only JOKERS found at the moment; still valid
                        firstNonWildcardNumber = actualNumber;
                    }
                    else if(actualNumber != firstNonWildcardNumber){
                        valid = false;
                    }
                }
            }while(valid && iterator.hasNext());
            return valid;
        }
    }

    private int getMovePower() {
        if (!isValidStructure()) {
            return -1;
        }
        // Buscar la primera carta que no sea un 2
        for (Card card : this.playedCards) {
            if (card.number() != 2) {
                return card.getPower();
            }
        }
        // Si solo había comodines
        return new Card(2, Suit.OROS).getPower();
    }

    @Override
    public int compareTo(Move other) {
        if (this.playedCards.size() == other.playedCards.size()) {
            int thisPower = this.getMovePower();
            int otherPower = other.getMovePower();
            return Integer.compareUnsigned(thisPower, otherPower);
        } else return -1;
    }

    public boolean isCloseMove() {
        boolean result = true;
        if (this.playedCards.isEmpty()) {
            result = false;
        } else {
            Iterator<Card> iterator = this.playedCards.iterator();
            while (iterator.hasNext() && result) {
                int cardValue = iterator.next().number();
                if (cardValue != JOKER && cardValue != CLOSE_CARD) {
                    result = false;
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder mensaje = new StringBuilder();
        for (Card card : playedCards) {
            mensaje.append(card.toString()).append(" ");
        }
        return mensaje.toString();
    }
}
