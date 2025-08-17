package es.daniylorena.juegodecartas.state;

import java.util.*;

public class Player {

    private final String name;
    private Role role;
    private final LinkedList<Card> hand;

    public Player(String name) {
        this.name = name;
        this.role = null;
        this.hand = new LinkedList<>();
    }

    public Player(String name, List<Card> hand) {
        this.name = name;
        this.role = null;
        this.hand = new LinkedList<>(hand);
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand.stream().sorted(Comparator.comparingInt(Card::getPower)).toList();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getHandSize() {
        return hand.size();
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        this.hand.remove(card);
    }

    private void sortHandWorstToBest() {
        this.hand.sort(Comparator.comparingInt(Card::getPower)); // Ordena la mano de menor a mayor
    }

    public Card takeBestCard() {
        Card best = getBestCard();
        this.hand.remove(best);
        return best;
    }

    private Card getBestCard() {
        sortHandWorstToBest();
        return this.hand.getLast();
    }

    public Card takeWorstNonRepeatedCard() {
        Card worst = getWorstNonRepeatedCard();
        this.hand.remove(worst);

        return worst;
    }

    private Card getWorstNonRepeatedCard() {
        sortHandWorstToBest();
        // Inicializamos el primer bloque
        Card firstInBlock = hand.getFirst();
        int currentPower = firstInBlock.getPower();
        int count = 1;

        // Variables para el mejor candidato no único
        Card bestCard = firstInBlock;
        int bestCount = Integer.MAX_VALUE;

        // Recorremos desde el segundo elemento
        for (int i = 1; i < hand.size(); i++) {
            Card card = hand.get(i);
            int power = card.getPower();

            if (power == currentPower) {
                count++;
            } else {
                // Fin del bloque anterior
                if (count == 1) {
                    return firstInBlock;
                }
                if (count < bestCount) {
                    bestCount = count;
                    bestCard = firstInBlock;
                }
                // Nuevo bloque
                firstInBlock = card;
                currentPower = power;
                count = 1;
            }
        }

        // Procesar el último bloque
        if (count == 1) {
            return firstInBlock;
        }
        if (count < bestCount) {
            bestCard = firstInBlock;
        }

        return bestCard;

    }
}
