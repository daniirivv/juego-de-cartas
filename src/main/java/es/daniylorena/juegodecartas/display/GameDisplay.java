package es.daniylorena.juegodecartas.display;

import es.daniylorena.juegodecartas.logic.GameControllerInterface;
import es.daniylorena.juegodecartas.logic.exceptions.IllegalPlayerNameException;
import es.daniylorena.juegodecartas.state.Move;

import java.util.ArrayList;
import java.util.Scanner;

public class GameDisplay implements GameDisplayInterface{

    public final String DONE_COMMAND = "DONE";
    public final String EXIT_COMMAND = "EXIT";

    private final static Scanner keyboardInput = new Scanner (System.in);

    private GameControllerInterface gameController;

    @Override
    public void createNewGame() {
        ArrayList<String> playersNames = askForPlayerNames();
        this.gameController.createAndStartNewGame(playersNames);
    }

    private ArrayList<String> askForPlayerNames(){
        ArrayList<String> result = new ArrayList<>(10);
        boolean done = false;
        do {
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
    public Move askForAMove() {
        return null;
    }



}
