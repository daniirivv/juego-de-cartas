package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    Game game;
    List<Player> players;

    @BeforeEach
    void setUp() {
        this.players = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Player player = mock(Player.class);
            players.add(player);
        }
    }

    @Test
    void expectEndGame(){
        for(var player : this.players){
            when(player.getHand()).thenReturn(Collections.emptyList());
        }

        this.game = new Game(this.players, null);
        boolean endGame = game.checkEndGame();

        assertTrue(endGame);

    }

    @Test
    void notExpectedEndGame(){

        for(var player : this.players){
            when(player.getHand()).thenReturn(new ArrayList<>(List.of(new Card(4, Suit.BASTOS))));
        }
        this.game = new Game(players, null);
        boolean endGame = game.checkEndGame();

        assertFalse(endGame);
    }

}
