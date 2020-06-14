package game.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Klasa abstrakcyjna reprezentująca obiekt gry
 */
public abstract class Entity {

    /** współrzędne x i y obiektu */
    public int x, y;
    /** wielokąt reprezentujący kształt obiektu dla systemu kolizji */
    public Shape collider;
    /** obraz reprezentujący obiekt graficznie */
    public BufferedImage image;

    /** Metoda abstrakcyjna do ładowania zasobów, np. obrazów */
    abstract void loadResources();

    /**
     * Metoda zwracająca wielokąt będący reprezentacją obiektu dla systemu kolizji
     * @return Wielokąt reprezentujący obiekt
     */
    public Shape getCollider(){
        return collider;
    }

}
