package game.entities;

import game.controller.KeyHandler;
import game.interfaces.Updatable;

import java.awt.*;

/**
 * Klasa lądowiska, reprezentuje lądowisko dla statku
 */
public class LandingSpace extends Entity implements Updatable {

    /**
     * Konstruktor klasy, tworzy lądowisko ze zbioru podanych współrzędnych
     * @param xVertecies współrzędne x kolejnych wierchołków
     * @param yVertecies współrzędne y kolejnych wierzchołków
     */
    public LandingSpace(int[] xVertecies, int[] yVertecies){
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
     * Odpowiada ze rysowanie obiektu lądowiska do obiektu grafiki
     * @param g obiekt grafiki do którego rysowany jest obiekt
     */
    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.darkGray);
        g.fill(collider);
    }
}
