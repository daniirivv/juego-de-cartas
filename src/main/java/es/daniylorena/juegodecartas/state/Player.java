package es.daniylorena.juegodecartas.state;

import java.util.List;

public class Player {

    private List<Card> hand;
    private Role role;

    public Player(List<Card> hand, Role role) {
        this.hand = hand;
        this.role = role;
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

}
