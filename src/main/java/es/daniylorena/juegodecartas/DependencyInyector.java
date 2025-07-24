package es.daniylorena.juegodecartas;

import es.daniylorena.juegodecartas.display.*;
import es.daniylorena.juegodecartas.logic.*;

public class DependencyInyector {

    public static void inyect(App app) {
        GameDisplay gameDisplay = new GameDisplay();
        GameController gameController = GameController.getInstance();

        gameDisplay.setGameController(gameController);
        gameController.setGameDisplay(gameDisplay);

        app.setUI(gameDisplay);
    }

}
