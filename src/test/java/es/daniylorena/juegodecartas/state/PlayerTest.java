package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static es.daniylorena.juegodecartas.state.Suit.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;
    Card bestCard;
    Card worstCard;

    @BeforeEach
    void setUp(){
        List<Card> hand = List.of(
                new Card(4, BASTOS),
                new Card(4, COPAS),
                new Card(5, OROS),
                new Card(12, ESPADAS)
                );
        player = new Player("Player", hand);
    }

    @Test
    void getBestCard(){
        bestCard = new Card(12, ESPADAS);
        assertEquals(bestCard, player.takeBestCard());
        assertFalse(player.getHand().contains(bestCard));
    }

    @Test
    void getWorstNonRepeatedCard(){
        worstCard = new Card(5, OROS);
        assertEquals(worstCard, player.takeWorstNonRepeatedCard());
        assertFalse(player.getHand().contains(worstCard));
    }
}
