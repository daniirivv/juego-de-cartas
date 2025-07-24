package es.daniylorena.juegodecartas.state;

import java.util.List;

public class Player {

    private final String name;
    private Role role;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int numberOfCards() {
        return hand.size();
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        this.hand.remove(card);
    }

    public Card getBestCard() {
        this.hand.sort((a, b) -> b.getPower() - a.getPower()); // Ordena la mano de mayor a menor peso
        return hand.getFirst();
    }

    public Card getWorstNonRepeatedCard() {
        this.hand.sort((a, b) -> a.getPower() - b.getPower());
        Card candidate = null;
        int minFrequency = Integer.MAX_VALUE;
        for (Card card : this.hand) {
            int count = 0;
            for (Card c : this.hand) {
                if (c.getNumber() == card.getNumber()) {
                    count++;
                }
            }
            if (count == 1) {
                return card;
            } else if (count < minFrequency) {
                // Guardamos la carta con la menor frecuencia (aunque repetida)
                minFrequency = count;
                candidate = card;
            }
        }
        // Si no hay cartas no repetidas, devolvemos la carta con menor frecuencia
        return candidate;
    }

}
