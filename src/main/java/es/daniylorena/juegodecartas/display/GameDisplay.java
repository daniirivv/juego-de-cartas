package es.daniylorena.juegodecartas.display;

import es.daniylorena.juegodecartas.logic.GameControllerInterface;
import es.daniylorena.juegodecartas.logic.exceptions.IllegalCardException;
import es.daniylorena.juegodecartas.logic.exceptions.IllegalPlayerNameException;
import es.daniylorena.juegodecartas.logic.exceptions.InsufficientPlayersException;
import es.daniylorena.juegodecartas.state.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GameDisplay implements UI, GameDisplayInterface {

    public static final int MAX_PLAYERS = 10;
    public static final int MIN_PLAYERS = 3;
    public static final String DONE_COMMAND = "DONE";
    public static final String EXIT_COMMAND = "EXIT";
    public static final String POSITIVE_ANSWER = "Y";
    public static final Map<Character, Suit> SUIT_TO_CHAR_MAP =
            Map.of(
                    'O', Suit.OROS,
                    'C', Suit.COPAS,
                    'E', Suit.ESPADAS,
                    'B', Suit.BASTOS
            );

    private static final Scanner keyboardInput = new Scanner(System.in);

    private GameControllerInterface gameController;

    public GameControllerInterface getGameController() {
        return gameController;
    }

    public void setGameController(GameControllerInterface gameController) {
        this.gameController = gameController;
    }

    @Override
    public void play() {
        ArrayList<String> playersNames = askForPlayerNames();
        this.gameController.launchGame(playersNames);
    }

    private ArrayList<String> askForPlayerNames() {
        ArrayList<String> result = new ArrayList<>(MAX_PLAYERS);
        boolean done = false;
        System.out.println("Introduce los nombres de los jugadores: (" + DONE_COMMAND + " para finalizar)");
        do {
            String name = GameDisplay.keyboardInput.nextLine();
            if (!name.equalsIgnoreCase(DONE_COMMAND)) {
                if (!name.isEmpty()) {
                    if (!result.contains(name)) {
                        result.add(name);
                    } else throw new IllegalPlayerNameException("Dos jugadores no pueden llamarse igual.");
                } else throw new IllegalPlayerNameException(
                        "El nombre tiene que estar compuesto por, como mínimo, un caracter.");
            } else if (result.size() < MIN_PLAYERS) {
                throw new InsufficientPlayersException("Falta/n " + (MIN_PLAYERS - result.size()) + " jugadores.");
            } else done = true;
        } while (!done);
        return result;
    }

    @Override
    public String[] askForAMove(Player turnOwner) {
        boolean validCards;
        String[] cards;
        do {
            System.out.println("¿Qué cartas quieres echar?");
            System.out.println("Formato: <Numero><Palo> <N><P>... Ej. 4C 12B -> Cuatro de copas y doce de bastos");
            String inputCards = GameDisplay.keyboardInput.nextLine().toUpperCase();
            if (inputCards.isBlank()) return new String[0];
            cards = inputCards.split(" ");
            validCards = checkCards(cards);
        } while (!validCards);

        return cards;
    }

    private static boolean checkCards(String[] cards) {
        boolean result;
        int i = 0;
        do{
            result = isValidCard(cards[i]);
            i++;
        } while(result && i < cards.length);

        return result;
    }

    private static boolean isValidCard(String card) {
        boolean result = true;
        try{
            String detail;
            if (card.length() < 2 || card.length() > 3) {
                detail = "La carta debe estar formada por hasta dos dígitos y una letra que indique el palo";
                throw new IllegalCardException(detail);
            }
            int number = Integer.parseInt(card.substring(0, card.length() - 1));
            char suit = card.charAt(card.length() - 1);
            if (number <= 0 || number > 12) throw new IllegalCardException("Número fuera de rango: " + number);
            if (!SUIT_TO_CHAR_MAP.containsKey(suit)) throw new IllegalCardException("Palo inválido: " + suit);
        }catch(IllegalCardException | NumberFormatException e){
            System.out.println(e.getMessage());
            result = false;
        }
        return result;
    }

    @Override
    public void notifyInvalidMove(Move move, String detail) {
        System.out.println("No puedes jugar el movimiento " + move.toString() + "ahora mismo. " + detail + '.');
    }

    @Override
    public void notifyPass(Player player) {
        System.out.println(player.toString() + " ha pasado");
    }

    @Override
    public void notifyCardExchange(Player winner, Player loser, List<Card> bestCards, List<Card> worstCards) {
        StringBuilder message = new StringBuilder("♔(" + winner.getName() + ") ");
        for (Card card : worstCards) {
            // TODO: Impresión como 1E 12C en vez de 1 de espadas 12 de copas
            message.append(card.toString() + " ");
        }
        message.append("<---> ☹(" + loser.getName() + ") ");
        for (Card card : bestCards) {
            message.append(card.toString() + " ");
        }
        System.out.println(message);
    }

    @Override
    public void notifyCloseByPassing() {
        System.out.println("Habéis pasado todos");
    }

    private boolean isValidCardNumber(int number) {
        return number > 0 && number <= 12;
    }

    private boolean isValidSuit(String inputSuit) {
        return inputSuit.equals("oros") || inputSuit.equals("bastos") || inputSuit.equals("copas") || inputSuit.equals("espadas");
    }

    @Override
    public boolean askForRematch() {
        System.out.println("¿Quieres volver a jugar? (" + POSITIVE_ANSWER + '/' + "N)");
        return keyboardInput.nextLine().equalsIgnoreCase(POSITIVE_ANSWER);
    }

    @Override
    public void notifyPlin(String skippedPlayer) {
        System.out.println("¡¡PLIN!! " + skippedPlayer + " ha sido saltado/a");
    }

    @Override
    public void notifyRole(Player player) {
        System.out.println(player.toString() + " es " + player.getRole().toString());
    }

    @Override
    public void notifyNewRound() {
        System.out.println("★ Nueva ronda ★");
    }

    @Override
    public void printTurn(Player player) {
        System.out.println("-- Turno de " + player.getName() + " --");
        printHand(player.getHand());
    }

    @Override
    public void printHand(List<Card> hand) {
        for (Card card : hand) {
            System.out.println(card.toString());
        }
    }

    @Override
    public void notifyOronWrongPlay() {
        System.out.println(
                """
                        El Orón (1 de Oros) es la carta más poderosa del juego, y ella sola se basta para cerrar jugadas de \
                        varias cartas. Por tanto, debe jugarse solo. Hemos eliminado el resto de cartas de tu jugada; \
                        guárdalas para más tarde y saca el máximo potencial del Orón.
                        """
        );
    }

    @Override
    public boolean askForLeave() {
        System.out.println("¿Quieres salir del juego? (EXIT)");
        return keyboardInput.nextLine().equalsIgnoreCase(EXIT_COMMAND);
    }

}
