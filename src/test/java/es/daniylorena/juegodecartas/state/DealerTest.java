package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DealerTest {

    static List<Player> playerList;

    @BeforeAll
    static void initializePlayerList() {
        playerList = List.of(
            new Player("Dani"),
            new Player("Lorena"),
            new Player("Eva"),
            new Player("Alex")
        );
    }

    @Test
    public void divideAllDeckCardsRoundRobin(){
        Deck deck = new Deck();
        Dealer.divideCards(playerList, deck);

        // Comprueba que la baraja se ha vaciado
        assertTrue(deck.isEmpty());

        // Comprueba que el total de cartas se ha repartido entre los distintos jugadores
        int numCartas = 0;
        for(var player : playerList){
            numCartas += player.getHandSize();
        }
        assertEquals(deck.size(), numCartas);
    }

    @Test
    public void roleApply(){

    }

}
