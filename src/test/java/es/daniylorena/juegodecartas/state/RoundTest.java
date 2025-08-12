package es.daniylorena.juegodecartas.state;

import es.daniylorena.juegodecartas.utilities.CircularList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoundTest {

    @Mock
    CircularList<Player> players;

    @Test
    void playBetterMove(){
        // Creación de mocks
        Move previousMove = mock(Move.class);
        Move actualMove = mock(Move.class);

        // Esqueleto de la clase Move
        Card playedCard = new Card (6, Suit.BASTOS);
        Set<Card> actualCards = Set.of(playedCard);
        when(actualMove.playedCards()).thenReturn(actualCards);
        when(actualMove.isValidStructure()).thenReturn(true);

        when(actualMove.compareTo(previousMove)).thenReturn(1);

        // Creación de un objeto Round preparado con una jugada previa
        Round round = new Round(players);
        round.getMoves().push(previousMove);
        round.setExpectedNumberOfCards(1);

        // Preparación del contexto
        Player player = new Player("Player", new LinkedList<>(actualMove.playedCards()));
        int previousMovesInRound = round.getMoves().size();

        // Lanzamiento del comportamiento
        boolean result = round.playMove(actualMove, player);

        // Comprobaciones
        assertTrue(result);
        assertEquals(previousMovesInRound + 1, round.getMoves().size());
        assertEquals(round.getMoves().peek().playedCards(), actualCards);
        assertFalse(player.getHand().contains(playedCard));
    }

    @Test
    void playWorseMove(){
        // Creación de mocks
        Move previousMove = mock(Move.class);
        Move actualMove = mock(Move.class);

        // Esqueleto de la clase Move
        Card playedCard = new Card (4, Suit.BASTOS);
        Set<Card> actualCards = Set.of(playedCard);
        when(actualMove.playedCards()).thenReturn(actualCards);
        when(actualMove.isValidStructure()).thenReturn(true);

        when(actualMove.compareTo(previousMove)).thenReturn(-1);

        // Creación de un objeto Round preparado con una jugada previa
        Round round = new Round(players);
        round.getMoves().push(previousMove);
        round.setExpectedNumberOfCards(1);

        // Preparación del contexto
        Player player = new Player("Player", new LinkedList<>(actualMove.playedCards()));
        int previousMovesInRound = round.getMoves().size();

        // Lanzamiento del comportamiento
        boolean result = round.playMove(actualMove, player);

        // Comprobaciones
        assertFalse(result);
        assertEquals(previousMovesInRound, round.getMoves().size());
        assertTrue(player.getHand().contains(playedCard));
    }

}
