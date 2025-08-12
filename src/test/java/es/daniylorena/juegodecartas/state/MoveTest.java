package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.Test;

import java.util.Set;

public class MoveTest {

    Move moveToValidate;
    Set<Card> actualCards;

    @Test
    void expectValidStructure(){
        actualCards = Set.of(new Card(6, Suit.BASTOS), new Card(6, Suit.OROS));
        moveToValidate = new Move(actualCards, null);


    }

}
