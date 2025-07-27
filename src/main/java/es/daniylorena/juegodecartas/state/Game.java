package es.daniylorena.juegodecartas.state;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private final List<Player> players;
    private final LinkedList<Round> rounds;

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

    public void shuffleDeck() {
        this.deck.shuffle();
    }

    public boolean addRound(Round round) {
        return this.rounds.add(round);
    }

    public Round getCurrentRound() {
        return this.getRounds().getLast();
    }

    public boolean checkEndGame() {
        int totalPlayers = this.players.size();
        int playersWithoutCards = 0;
        for (Player player : this.players) {
            if (player.getHand().isEmpty()) {
                playersWithoutCards++;
            }
        }
        return playersWithoutCards < totalPlayers - 1;
    }
}
