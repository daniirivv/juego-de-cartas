package es.daniylorena.juegodecartas;


import es.daniylorena.juegodecartas.display.UI;


public class App {

    UI gameDisplay;

    public UI getGameDisplay() {
        return gameDisplay;
    }

    public void setGameDisplay(UI gameDisplay) {
        this.gameDisplay = gameDisplay;
    }

    public void run(){
        gameDisplay.createNewGame();
    }

    public static void main(String[] args){
        App app = new App();
        DependencyInyection.inyect(app);
    }
}
