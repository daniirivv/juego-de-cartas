package es.daniylorena.juegodecartas.state;

import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {

    static List<Player> playerList;

    @BeforeEach
    void initializePlayerList() {
        playerList = List.of(
            new Player("Dani"),
            new Player("Lorena"),
            new Player("Eva"),
            new Player("Alex")
        );
    }

    @Test
    void divideAllDeckCardsRoundRobin(){
        Deck deck = new Deck();
        int deckSize = deck.getSize();
        Dealer.divideCards(playerList, deck);

        // Comprueba que la baraja se ha vaciado
        assertTrue(deck.isEmpty());

        // Comprueba que el total de cartas se ha repartido entre los distintos jugadores
        int dealedCards = 0;
        for(var player : playerList){
            dealedCards += player.getHandSize();
        }
        assertEquals(deckSize, dealedCards);
    }

    @Test
    void roleApply(){
        // @TODO: Probar RoleApply mockeando el comportamiento de player.takeBestCard y player.takeWorstNonRepeatedCard
    }
}
