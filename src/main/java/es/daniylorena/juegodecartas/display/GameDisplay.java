package es.daniylorena.juegodecartas.display;

import es.daniylorena.juegodecartas.logic.GameControllerInterface;
import es.daniylorena.juegodecartas.logic.exceptions.IllegalCardException;
import es.daniylorena.juegodecartas.logic.exceptions.IllegalPlayerNameException;
import es.daniylorena.juegodecartas.logic.exceptions.InsufficientPlayersException;
import es.daniylorena.juegodecartas.state.Card;
import es.daniylorena.juegodecartas.state.Move;
import es.daniylorena.juegodecartas.state.Player;
import es.daniylorena.juegodecartas.state.Suit;

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
    public static final String PASS_COMMAND = "PASS";
    public static final Map<Character, Suit> SUIT_TO_CHAR_MAP =
            Map.of(
                    'O', Suit.OROS,
                    'C', Suit.COPAS,
                    'E', Suit.ESPADAS,
                    'B', Suit.BASTOS
            );

    private final static Scanner keyboardInput = new Scanner(System.in);

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
        System.out.println("¿Qué cartas quieres echar?");
        System.out.println("Formato: <Numero><Palo> <N><P>... Ej. 4C 12B -> Cuatro de copas y doce de bastos");
        System.out.println("Para pasar: PASS");
        String inputCards = GameDisplay.keyboardInput.nextLine().toUpperCase();
        if (inputCards.equals(PASS_COMMAND)) {
            return new String[0];
        }
        String[] cards = inputCards.split(" ");
        for (String card : cards) {
            if (card.length() < 2 || card.length() > 3) throw new IllegalCardException("Formato erróneo: " + card);
            String numberPart = card.substring(0, card.length() - 1);
            char suit = card.charAt(card.length() - 1);
            int number;
            try {
                number = Integer.parseInt(numberPart);
            } catch (NumberFormatException e) {
                throw new IllegalCardException("Número inválido: " + numberPart);
            }
            // Validaciones
            if (number <= 0 || number > 12) throw new IllegalCardException("Número fuera de rango: " + number);
            if (!SUIT_TO_CHAR_MAP.containsKey(suit)) throw new IllegalCardException("Palo inválido: " + suit);
        }
        return cards;
    }

    @Override
    public void notifyInvalidMove(Move move, String detail) {
        System.out.println("No puedes jugar el movimiento " + move.toString() + "ahora mismo. " + detail + '.');
    }

    public void notifyPass(Player player) {
        System.out.println(player.toString() + " ha pasado");
    }

    @Override
    public void notifyCloseByPassing() {
        System.out.println("Habéis saltado todos... Nueva ronda!");
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
        return keyboardInput.nextLine().equalsIgnoreCase(EXIT_COMMAND);
    }

}
