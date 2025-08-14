package es.daniylorena.juegodecartas;

import es.daniylorena.juegodecartas.display.GameDisplay;
import es.daniylorena.juegodecartas.logic.GameController;

public class DependencyInyector {

    private DependencyInyector() {
    }

    public static void inyect(App app) {
        GameDisplay gameDisplay = new GameDisplay();
        GameController gameController = GameController.getInstance();

        gameDisplay.setGameController(gameController);
        gameController.setGameDisplay(gameDisplay);

        app.setUI(gameDisplay);
    }

}
