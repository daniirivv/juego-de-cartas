package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.utilities.CircularList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoundTest {

    @Mock
    CircularList<Player> players;

    @Mock
    Move previousMove;

    @Mock
    Move actualMove;

    private Round round;
    private Player player;
    private Set<Card> actualCards;
    private int initialMovesCount;

    @BeforeEach
    void setUp() {
        // 1. Definir la carta y el conjunto de cartas de la jugada
        Card playedCard = new Card(6, Suit.BASTOS);
        actualCards = Set.of(playedCard);

        // 2. Configurar el mock de actualMove con comportamiento genérico
        when(actualMove.playedCards()).thenReturn(actualCards);
        when(actualMove.isValidStructure()).thenReturn(true);

        // 3. Crear la ronda y forzar el estado “no primer movimiento”
        round = new Round(players);
        round.getMoves().push(previousMove);
        round.setExpectedNumberOfCards(actualCards.size());

        // 4. Crear el jugador con la mano inicial (copia del conjunto)
        player = new Player("Player", new LinkedList<>(actualCards));

        // 5. Capturar el número de movimientos antes de la jugada bajo prueba
        initialMovesCount = round.getMoves().size();
    }

    @Test
    void playBetterValidMove() {
        // Solo modifico lo que cambia en este escenario
        when(actualMove.compareTo(previousMove)).thenReturn(1);

        boolean result = round.playMove(actualMove, player);

        // Assertions específicas
        assertTrue(result);
        assertEquals(initialMovesCount + 1, round.getMoves().size());
        assertSame(actualMove, round.getMoves().peek());
        assertFalse(player.getHand().contains(new Card(6, Suit.BASTOS)));
    }

    @Test
    void playWorseNonValidMove() {
        // Escenario opuesto: la nueva jugada pierde
        when(actualMove.compareTo(previousMove)).thenReturn(-1);

        boolean result = round.playMove(actualMove, player);

        // Assertions específicas
        assertFalse(result);
        assertEquals(initialMovesCount, round.getMoves().size());
        assertTrue(player.getHand().contains(new Card(6, Suit.BASTOS)));
    }
}