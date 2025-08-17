package es.daniylorena.juegodecartas.state;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Player {

    private final String name;
    private Role role;
    private final LinkedList<Card> hand;

    public Player(String name) {
        this.name = name;
        this.role = null;
        this.hand = new LinkedList<>();
    }

    public Player(String name, LinkedList<Card> hand) {
        this.name = name;
        this.role = null;
        this.hand = hand;
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

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        this.hand.remove(card);
    }

    private void sortHandWorstToBest() {
        this.hand.sort((a, b) -> b.getPower() - a.getPower()); // Ordena la mano de mayor a menor peso
    }

    public Card getBestCard() {
        sortHandWorstToBest();
        return this.hand.getFirst();
    }

    public Card takeBestCard() {
        Card best = getBestCard();
        this.hand.remove(best);
        return best;
    }

    public Card getWorstNonRepeatedCard() {
        sortHandWorstToBest();
        int resultIndex = 0;
        int n = this.hand.size();
        int minFrecuencia = n;
        int inicio = 0;

        for (int i = 0; i < n; i++) {
            /*
             Cortamos la cadena cuando:
             - i == n (fin de lista)
             - elemento actual ≠ anterior
            */
            if (i == n - 1 || !this.hand.get(i).equals(hand.get(i + 1))) { // Lazy Evaluation
                int frecuencia = i - inicio + 1;
                if (frecuencia == 1) {
                    return this.hand.remove(inicio);  // Frecuencia 1 → devolver inmediatamente
                } else if (frecuencia < minFrecuencia) {
                    minFrecuencia = frecuencia;
                    resultIndex = inicio; // Guardamos como posible mejor opción
                }
                // Preparamos inicio para siguiente cadena
                inicio = i;
            }
        }
        return this.hand.get(resultIndex);
    }

    public Card takeWorstNonRepeatedCard() {
        Card worst = getWorstNonRepeatedCard();
        this.hand.remove(worst);

        return worst;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
