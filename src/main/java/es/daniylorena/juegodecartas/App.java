package es.daniylorena.juegodecartas;


import es.daniylorena.juegodecartas.display.UI;


public class App {

    private UI gameDisplay;

    public UI getUI() {
        return gameDisplay;
    }

    public void setUI(UI gameDisplay) {
        this.gameDisplay = gameDisplay;
    }

    public void run(){
        gameDisplay.play();
    }

    public static void main(String[] args){
        App app = new App();
        DependencyInyector.inyect(app);
        boolean exit = false;
        do{
            app.run();
            exit = app.getUI().askForLeave();
        }while(!exit);
    }
}
