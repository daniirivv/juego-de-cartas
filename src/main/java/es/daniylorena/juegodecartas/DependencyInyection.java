package es.daniylorena.juegodecartas;

import es.daniylorena.juegodecartas.display.*;
import es.daniylorena.juegodecartas.logic.*;

public class DependencyInyection {

    public static void inyect(App app){
        GameDisplay gameDisplay = new GameDisplay();
        GameController gameController = new GameController();

        gameDisplay.setGameController(gameController);
        gameController.setGameDisplay(gameDisplay);

        app.setUI(gameDisplay);
    }

}
