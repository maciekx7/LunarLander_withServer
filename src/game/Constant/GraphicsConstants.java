package game.Constant;

import game.serverConnection.ServerStatus;
import game.configReader.ConfigReader;
import game.serverConnection.ServerConnectivity;

import java.net.Socket;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Klasa przechoowywująca ścieżki do elementów graficznych gry
 */
public class GraphicsConstants {
    /** string prechowywujący nazwę pliku ze ścieżkami do elementów graficznych gry. BEZ ROZSZERZENIA */
    private static String fileName = "gameGraphics";

    /** ścieżka do obrazka statku */
    public static String SHIP_IMAGE;
    /** ścieżka do obrazku ognia w góre */
    public static String FIRE_UP_IMAGE;
    /** ścieżka do ognia w dół */
    public static String FIRE_DOWN_IMAGE;
    /** ścieżka do ognia w lewo */
    public static String FIRE_LEFT_IMAGE;
    /** ścieżka do ognia w prawo */
    public static String FIRE_RIGHT_IMAGE;
    /** ścieżka do obrazka koniec gry */
    public static String GAME_OVER_IMAGE;
    /** ścieżka do obrazka wciśnij enter */
    public static String MENU_TEXT_IMAGE;
    /** ścieżka do obrazka Wygrałeś */
    public static String YOU_WON_IMAGE;
    /** ścieżka do obrazka wylądowałeś */
    public static String LANDED_IMAGE;
    /** ścieżka do obrazka rozbiłeś się */
    public static String CRASHED_IMAGE;
    /** ścieżka do obrazka statek został zniszczony */
    public static String SHIP_DESTROYED_IMAGE;
    /** ścieżka do obrazka pausa */
    public static String PAUSE_IMAGE;
    /** ścieżka do obrazka meteorytu */
    public static String METEOR_IMAGE;

    private GraphicsConstants() {
        throw new AssertionError();
    }


    /**
     * Metoda pobierająca ścieżki do plików graficznych
     */
    public static void donwloanGraphics() {
        if(ConfigReader.IDE) {
            fileName = "gameGraphicsIDE";
        } else {
            fileName = "gameGraphics";
        }
            SHIP_IMAGE = ConfigReader.getValue(fileName, "ship");
            FIRE_UP_IMAGE = ConfigReader.getValue(fileName, "fireUp");
            FIRE_DOWN_IMAGE = ConfigReader.getValue(fileName, "fireDown");
            FIRE_LEFT_IMAGE = ConfigReader.getValue(fileName, "fireLeft");
            FIRE_RIGHT_IMAGE = ConfigReader.getValue(fileName, "fireRight");
            GAME_OVER_IMAGE = ConfigReader.getValue(fileName, "gameOver");
            MENU_TEXT_IMAGE = ConfigReader.getValue(fileName, "menuText");
            YOU_WON_IMAGE = ConfigReader.getValue(fileName, "wonText");
            LANDED_IMAGE = ConfigReader.getValue(fileName, "landed");
            CRASHED_IMAGE = ConfigReader.getValue(fileName, "crashed");
            SHIP_DESTROYED_IMAGE = ConfigReader.getValue(fileName, "destroyed");
            PAUSE_IMAGE = ConfigReader.getValue(fileName, "paused");
            METEOR_IMAGE = ConfigReader.getValue(fileName, "meteor");
    }
}
