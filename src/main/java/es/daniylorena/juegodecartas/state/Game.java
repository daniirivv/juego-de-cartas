package es.daniylorena.juegodecartas.state;

import java.util.LinkedList;
import java.util.List;

public record Game(List<Player> players, LinkedList<Round> rounds, Deck deck) {

    public Game(List<Player> players, Deck deck) {
        this(players, new LinkedList<>(), deck);
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

    public void shuffleDeck() {
        this.deck.shuffle();
    }

    public void addRound(Round round) {
        this.rounds.add(round);
    }

    public Round getCurrentRound() {
        return this.getRounds().getLast();
    }

    // @OPTIMIZE: While-based loop instead of For loop
    public boolean checkEndGame() {
        int totalPlayers = this.players.size();
        int playersWithoutCards = 0;
        for (Player player : this.players) {
            if (player.getHand().isEmpty()) {
                playersWithoutCards++;
            }
        }
        return playersWithoutCards >= totalPlayers - 1;
    }
}
