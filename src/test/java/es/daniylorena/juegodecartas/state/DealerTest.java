package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;

public class DealerTest {

    @Test
    public void divideAllDeckCardsRoundRobin(){
        Player playerOne = new Player("Dani");
        Player playerTwo = new Player("Lorena");
        Player playerThree = new Player("Eva");
        Player playerFour = new Player("Alex");
        Deck deck = new Deck();
        int deckSize = deck.getSize();
        List<Player> playerList = List.of(
                playerOne,
                playerTwo,
                playerThree,
                playerFour
        );

        Dealer.divideCards(playerList, deck);

        // Comprueba que la baraja se ha vaciado
        assertTrue(deck.isEmpty());

        // Comprueba que el total de cartas se ha repartido entre los distintos jugadores
        int numCartas = 0;
        for(var player : playerList){
            numCartas += player.getHandSize();
        }
        assertEquals(deckSize, numCartas);
    }

}
