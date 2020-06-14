package game.entities;

import game.controller.KeyHandler;
import game.interfaces.Updatable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Kalsa zarządzająca meteorytami
 */
public class MeteorHandler implements Updatable {

    /** lista wszystkich meteorów na planszy
     * @see Meteor */
    private List<Meteor> meteors;

    /**
     * Konstruktor klasy, tworzy i dodaje do listy meteory o zadanych parametrach
     * @param numOfMeteors ilość meteorów na mapie
     * @param x tablica początkowych współrzędnych x meteorów
     * @param y tablica początkowych współrzędnych y meteorów
     * @param mass masa meteoru
     * @param vetricalSpeed tablica prędkości meteorów dla osi x
     * @param gravity siła grawitacji
     */
    public MeteorHandler(int numOfMeteors, int[] x, int[] y, float mass, int[] vetricalSpeed, float gravity){
        meteors = new ArrayList<Meteor>();
        for (int i = 0; i < numOfMeteors; i++) {
            meteors.add(new Meteor(x[i], y[i], mass, vetricalSpeed[i], gravity));
        }
    }

    /**
     * Metoda zwracająca aktualną listę meteorów
     * @return lista obiektów meteorytów
     */
    public List<Meteor> getMeteors(){
        return meteors;
    }

    /**
     * Metoda zmieniająca flagę pauzy dla wszystkich obiektów meteorów
     * @param pause flaga pauzy, jeśli prawda to wchodzi w stan pauzy
     */
    public void pause(boolean pause){
        for (Meteor meteor : meteors)
            meteor.pause = pause;
    }

    /**
     * Metoda update wywoływana ze stałą częstotliwością
     * Wywouje metodę update dla wszyskich obiektów meteorów
     */
    @Override
    public void update(){
        for(Meteor meteor : meteors)
            meteor.update();
    }

    @Override
    public void input(KeyHandler key) { }

    /**
     * Metoda render wywoływana ze stałą częstotliwością
     * Wywołuje metodę render dla wszystkich obiektów meteorów
     * @param g obiekt grafiki do którego rysowane są elementy
     */
    @Override
    public void render(Graphics2D g){
        for (Meteor meteor: meteors)
            meteor.render(g);
    }
}
