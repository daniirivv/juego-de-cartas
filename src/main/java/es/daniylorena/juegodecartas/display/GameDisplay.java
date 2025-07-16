package es.daniylorena.juegodecartas.display;

import es.daniylorena.juegodecartas.logic.GameControllerInterface;
import es.daniylorena.juegodecartas.logic.exceptions.IllegalPlayerNameException;
import es.daniylorena.juegodecartas.state.*;

import java.util.*;

public class GameDisplay implements UI, GameDisplayInterface{

    public static final int MAX_PLAYERS = 10;
    public static final String DONE_COMMAND = "DONE";
    public static final String EXIT_COMMAND = "EXIT";
    public static final String POSITIVE_ANSWER = "Y";

    private final static Scanner keyboardInput = new Scanner (System.in);

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

    private ArrayList<String> askForPlayerNames(){
        ArrayList<String> result = new ArrayList<>(MAX_PLAYERS);
        boolean done = false;
        do {
            System.out.println("Introduce los nombres de los jugadores:");
            String name = GameDisplay.keyboardInput.nextLine();
            if (!name.equalsIgnoreCase(DONE_COMMAND)) {
                if (!name.isEmpty()) {
                    if(!result.contains(name)){
                        result.add(name);
                    } else throw new IllegalPlayerNameException("Dos jugadores no pueden llamarse igual.");
                } else throw new IllegalPlayerNameException(
                        "El nombre tiene que estar compuesto por, como mínimo, un caracter.");
            } else done = true;
        }while (!done);
        return result;
    }

    @Override
    public Move askForAMove(Player turnOwner) {
        System.out.println("¿Qué cartas quieres echar?");
        System.out.println("Formato: <Número> de <Palo>, <Número> de <Palo>... Ej: 4 de oros, 4 de bastos, 4 de copas");

        String inputCards = GameDisplay.keyboardInput.nextLine().toLowerCase().trim();
        String[] cards = inputCards.split(",");

        Set<Card> setOfCards = new HashSet<>();

        for (String card : cards) {
            card = card.trim();

            if (!card.isEmpty()) {
                String[] individualCard = card.split("\\s+de+\\s+"); // Expresión regular que permite 1/+ espacios

                if (individualCard.length == 2) {
                    try {
                        int cardNumber = Integer.parseInt(individualCard[0].trim());
                        String suitInput = individualCard[1];

                        if (isValidCardNumber(cardNumber) && isValidSuit(suitInput)) {
                            Suit suit = Suit.valueOf(suitInput);
                            Card cardToPlay = new Card(cardNumber, suit);
                            setOfCards.add(cardToPlay);
                        } else {
                            System.out.println("Carta inválida: " + card);
                        }
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Número inválido en: " + card);
                    }
                } else {
                    System.out.println("Formato incorrecto en: " + card);
                }
            }
        }

        return new Move(setOfCards, turnOwner);
    }

    @Override
    public void notifyInvalidMove(Move move) {
        System.out.println("No puedes jugar el movimiento " + move + "ahora mismo.");
    }

    private boolean isValidCardNumber(int number) {
        return number > 0 && number <= 12;
    }

    private boolean isValidSuit(String inputSuit) {
        return inputSuit.equals("oros") || inputSuit.equals("bastos") || inputSuit.equals("copas") || inputSuit.equals("espadas");
    }

    @Override
    public boolean askForRematch() {
        return keyboardInput.nextLine().equalsIgnoreCase(POSITIVE_ANSWER);
    }

    @Override
    public boolean askForLeave() {
        return keyboardInput.nextLine().equalsIgnoreCase(EXIT_COMMAND);
    }

}
