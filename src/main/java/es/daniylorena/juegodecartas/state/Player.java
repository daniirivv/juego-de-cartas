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

    public Card takeBestCard() {
        this.hand.sort((a, b) -> b.getPower() - a.getPower()); // Ordena la mano de mayor a menor peso
        return hand.removeFirst();
    }

    public Card takeWorstNonRepeatedCard() {
        int resultIndex = -1;
        int n = this.hand.size();
        int minFrecuencia = n;
        int inicio = 0;

        for (int i = 0; i < n; i++) {
            /*
             Cortamos la cadena cuando:
             - i == n (fin de lista)
             - elemento actual ≠ anterior
            */
            if (i == n-1 || !this.hand.get(i).equals(hand.get(i + 1))) { // Lazy Evaluation
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
        return (resultIndex == -1) ? null : this.hand.remove(resultIndex);

        /* VERSION LORENA
        int minFrequency = Integer.MAX_VALUE;
        for (Card card : this.hand) {
            int count = 0;
            for (Card c : this.hand) {
                if (c.getNumber() == card.getNumber()) {
                    count++;
                }
            }
            if (count == 1) {
                this.hand.remove(card);
                return card;
            } else if (count < minFrequency) {
                // Guardamos la carta con la menor frecuencia (aunque repetida)
                minFrequency = count;
                candidate = card;
            }
        }
        // Si no hay cartas no repetidas, devolvemos la carta con menor frecuencia
        this.hand.remove(candidate);
        return result;
        */
    }

}
