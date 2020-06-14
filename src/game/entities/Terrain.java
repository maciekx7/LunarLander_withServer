package game.entities;

import game.controller.KeyHandler;
import game.interfaces.Updatable;

import java.awt.*;

/**
 * Klasa terenu, reprezentująca powierzchnie planety
 */
public class Terrain extends Entity implements Updatable {

    /**
     * Konstruktor klasy, tworzy teren ze zbioru podanych współrzędnych
     * @param xVertecies współrzędne x kolejnych wierchołków
     * @param yVertecies współrzędne y kolejnych wierzchołków
     */
    public Terrain(int[] xVertecies, int[]yVertecies){
        collider = new Polygon(xVertecies, yVertecies, xVertecies.length);
    }

    @Override
    void loadResources() { }


    @Override
    public void update() { }

    @Override
    public void input(KeyHandler key) { }

    /**
     * Metoda render, wywolywana ze stałą częstotliwością
     * Odpowiada ze rysowanie obiektu terenu do obiektu grafiki
     * @param g obiekt grafiki do którego rysowany jest obiekt
     */
    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.gray);
        g.fill(collider);
    }
}
