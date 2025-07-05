package es.daniylorena.juegodecartas.state;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private final List<Player> players;
    private final List<Round> rounds;

    private final Deck deck;

    public Game(List<Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;

        this.rounds = new LinkedList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Deck getDeck() {
        return deck;
    }
}
