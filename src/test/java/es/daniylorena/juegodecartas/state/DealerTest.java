package es.daniylorena.juegodecartas.state;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DealerTest {

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
        Map<Role, Player> roleToPlayerMap = initializeRolesInPlayerList();
        Player presi = roleToPlayerMap.get(Role.PRESI);
        Player culo = roleToPlayerMap.get(Role.CULO);

        if (presi != null && culo != null) {
            List<Card> presiToExchange = new ArrayList<>(Dealer.PRESI_CULO_EXCHANGE);
            List<Card> culoToExchange = new ArrayList<>(Dealer.PRESI_CULO_EXCHANGE);

            for (int i = 0; i < Dealer.PRESI_CULO_EXCHANGE; i++){
                presiToExchange.add(presi.takeWorstNonRepeatedCard());
                culoToExchange.add(culo.takeBestCard());
            }
            for (int i = 0; i < Dealer.PRESI_CULO_EXCHANGE; i++){
                presi.addCardToHand(presiToExchange.removeFirst());
                culo.addCardToHand(culoToExchange.removeFirst());
            }

            Dealer.applyRolesIfDefined(playerList);

            assertTrue(presi.getHand().containsAll(culoToExchange) && culo.getHand().containsAll(presiToExchange));
        }
    }

    private static Map<Role, Player> initializeRolesInPlayerList() {
        Map<Role, Player> result = new HashMap<>();
        Role[] criticalRoles = {Role.PRESI, Role.CULO, Role.VICEPRESI, Role.VICECULO};
        Iterator<Player> iterator = playerList.iterator();
        for (var role : criticalRoles){
            if(iterator.hasNext()){
                var player = iterator.next();
                player.setRole(role);
                result.put(role, player);
            }
        }
        return result;
    }

}
