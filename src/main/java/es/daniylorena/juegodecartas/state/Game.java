package es.daniylorena.juegodecartas.state;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private final List<Player> players;
    private List<Round> rounds;

    public Game(List<Player> players) {
        this.players = players;
        this.rounds = new LinkedList<>();
    }
}
