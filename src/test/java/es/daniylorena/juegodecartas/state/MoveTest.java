package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class MoveTest {

    Move moveToValidate;
    Set<Card> actualCards;

    @Test
    void expectValidStructure(){
        actualCards = Set.of(new Card(6, Suit.BASTOS), new Card(6, Suit.OROS));
        moveToValidate = new Move(actualCards, null);

        boolean valid = Move.isValidStructure(actualCards);

        assertTrue(valid);
    }

    @Test
    void expectInvalidStructure(){
        actualCards = Set.of(new Card(6, Suit.BASTOS), new Card(7, Suit.BASTOS));
        moveToValidate = new Move(actualCards, null);

        boolean valid = Move.isValidStructure(actualCards);

        assertFalse(valid);
    }

    @Test
    void expectValidStructureWithWildcards(){
        actualCards = Set.of(new Card(3, Suit.BASTOS), new Card(2, Suit.OROS));
        moveToValidate = new Move(actualCards, null);

        boolean valid = Move.isValidStructure(actualCards);

        assertTrue(valid);
    }

    @Test
    void expectInvalidStructureWithWildcards(){
        actualCards = Set.of(
                new Card(3, Suit.BASTOS),
                new Card(2, Suit.OROS),
                new Card(12, Suit.COPAS)
        );
        moveToValidate = new Move(actualCards, null);

        boolean valid = Move.isValidStructure(actualCards);

        assertFalse(valid);
    }

}
