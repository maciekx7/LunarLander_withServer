package game.entities;

import game.Constant.GraphicsConstants;
import game.controller.KeyHandler;
import game.interfaces.Updatable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;


/**
 * Klasa reprezentująca meteoryt
 */
public class Meteor extends Entity implements Updatable {

    /** aktualne prędkości meteoru, kolejno w osi x i y */
    private float speedX, speedY;
    /** masa meteoru */
    private float mass;
    /** siła przyciągania meteoru do planety */
    private float gravity;
    /** flaga określająca stan pauzy */
    public boolean pause = false;

    /**
     * Konstruktor klasy meteor, przypisuje początkowe wartości oraz ładuje zasoby
     * @param x współrzędna x obiektu
     * @param y współrzędna y obiektu
     * @param mass masa obiektu
     * @param vetricalSpeed prędkość obiektu w osi x
     * @param gravity siła grawitacji
     */
    public Meteor(int x, int y, float mass, float vetricalSpeed, float gravity){
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.gravity = (float) (gravity/9.81 *(-0.18));;

        speedX = vetricalSpeed;

        loadResources();
    }

    /** Metoda ładująca zasoby - obraz obiektu */
     void loadResources(){
        try {
            image = ImageIO.read(new File(GraphicsConstants.METEOR_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda update, wywolywana ze stałą częstotliwością
     * Odpowiada za ustalenie pozycji i poprawego collidera obiektu
     */
    @Override
    public void update(){
        collider = new Rectangle2D.Float(x, y, image.getWidth(), image.getHeight());

        if(!pause) {
            speedY -= gravity * mass/100;

            x += speedX;
            y += speedY;
        }
    }

    @Override
    public void input(KeyHandler key){ }

    /**
     * Metoda render, wywolywana ze stałą częstotliwością
     * Odpowiada ze rysowanie obiektu meteoru do obiektu grafiki
     * @param g obiekt grafiki do którego rysowany jest obiekt
     */
    @Override
    public void render(Graphics2D g){
        g.drawImage(image, x, y, null);
    }

    /**
     * Metoda zwracająca prostokąt będący reprezentacją obiektu dla kolizji
     * @return prostokąt reprezentujący obiekt
     */
    @Override
    public Rectangle2D getCollider(){
        return (Rectangle2D) collider;
    }
}
