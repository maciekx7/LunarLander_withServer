package game.Constant;

import game.serverConnection.ServerStatus;
import game.configReader.ConfigReader;
import game.serverConnection.ServerConnectivity;

import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa przechoowywująca poziomy gry
 */
public class LoadLevel {
    /** string przechowujący komende do serwera */
    private static final String serverCommand = "LOAD_LEVEL:";
    /** stała przechowywująca przyspieszenie grawitacyjne */
    public static float GRAVITY_SPEED;

    /** stała przechowywująca startowe położenie statku x */
    public static int xStart;
    /** stała przechowywująca startowe położenie statku y */
    public static int yStart;

    /** tablica współrzędnych x mapy */
    public static int[] xVerticies;
    /** tablica współrzędnych y mapy */
    public static int[] yVerticies;
    /** tablica współrzędnych x lądowiska */
    public static int[] xLanding;
    /** tablica współrzędnych y lądowiska */
    public static int[] yLanding;

    /** stała przechuwująca ilość meteorytów */
    public static int numOfMeteors;
    /** tablica wartości prędkości w osi x meteorytów */
    public static int [] speedMeteors;
    /** tablica współrzędnych x meteorytów */
    public static int[] xMeteors;
    /** tablica współrzędnych y meteorytów */
    public static int[] yMeteors;

    /**
     * Metoda pobierająca poziom gry
     * @param serverSocket Socket serwera. Jeśli połączenie jest zestawione, pobiera dane online. Jeśli nie - lokalne.
     * @param Level numer poziomu, który chcemy pobrać
     */
    public static void getLevel(Socket serverSocket, int Level) {
        if(ServerStatus.isConnected()) {
            try {
                Map<String,String> data = ServerConnectivity.getDecodedDataInMap(serverSocket,serverCommand + String.valueOf(Level));
                GRAVITY_SPEED = Float.parseFloat(data.get("gravitySpeed"));
                xStart = Integer.parseInt(data.get("xStart"));
                yStart = Integer.parseInt(data.get("yStart"));
                xVerticies = Arrays.stream(data.get("xVertecies").split(";")).mapToInt(Integer::parseInt).toArray();
                yVerticies = Arrays.stream(data.get("yVertecies").split(";")).mapToInt(Integer::parseInt).toArray();
                xLanding = Arrays.stream(data.get("xLanding").split(";")).mapToInt(Integer::parseInt).toArray();
                yLanding = Arrays.stream(data.get("yLanding").split(";")).mapToInt(Integer::parseInt).toArray();
                numOfMeteors = Integer.parseInt(data.get("numberOfMeteors"));
                speedMeteors = Arrays.stream(data.get("speedMeteors").split(";")).mapToInt(Integer::parseInt).toArray();
                xMeteors = Arrays.stream(data.get("xMeteors").split(";")).mapToInt(Integer::parseInt).toArray();
                yMeteors = Arrays.stream(data.get("yMeteors").split(";")).mapToInt(Integer::parseInt).toArray();
                System.out.println("Level from server");
            } catch(Exception e) {
                ServerStatus.connectionLost(serverSocket);
            }
        }
        if(!ServerStatus.isConnected()) {
            String fileName;
            switch (Level) {
                case 2:
                    fileName = "level2";
                    break;
                case 3:
                    fileName = "level3";
                    break;
                case 4:
                    fileName = "level4";
                    break;
                case 1:
                default:
                    fileName = "level1";
                    break;
            }

            GRAVITY_SPEED = Float.parseFloat(ConfigReader.getValue(fileName, "gravitySpeed"));
            xStart = Integer.parseInt(ConfigReader.getValue(fileName, "xStart"));
            yStart = Integer.parseInt(ConfigReader.getValue(fileName, "yStart"));
            xVerticies = Arrays.stream(ConfigReader.getValue(fileName, "xVertecies").split(";")).mapToInt(Integer::parseInt).toArray();
            yVerticies = Arrays.stream(ConfigReader.getValue(fileName, "yVertecies").split(";")).mapToInt(Integer::parseInt).toArray();
            xLanding = Arrays.stream(ConfigReader.getValue(fileName, "xLanding").split(";")).mapToInt(Integer::parseInt).toArray();
            yLanding = Arrays.stream(ConfigReader.getValue(fileName, "yLanding").split(";")).mapToInt(Integer::parseInt).toArray();

            numOfMeteors = Integer.parseInt(ConfigReader.getValue(fileName, "numberOfMeteors"));
            speedMeteors = Arrays.stream(ConfigReader.getValue(fileName, "speedMeteors").split(";")).mapToInt(Integer::parseInt).toArray();
            xMeteors = Arrays.stream(ConfigReader.getValue(fileName, "xMeteors").split(";")).mapToInt(Integer::parseInt).toArray();
            yMeteors = Arrays.stream(ConfigReader.getValue(fileName, "yMeteors").split(";")).mapToInt(Integer::parseInt).toArray();
        }
        resizeToScreen();
    }

    private LoadLevel() {
        throw new AssertionError();
    }

    /** Metoda dostosowywująca wspołrzędne mapy do rozmiaru ekranu*/
    private static void resizeToScreen(){
        xVerticies = Arrays.stream(xVerticies).map(x -> (int)(DefaultGameSettings.WIDTH*0.01*x)).toArray();
        yVerticies = Arrays.stream(yVerticies).map(y -> (int)(DefaultGameSettings.HEIGHT*0.01*y)).toArray();
        xLanding = Arrays.stream(xLanding).map(x -> (int)(DefaultGameSettings.WIDTH *0.01*x)).toArray();
        yLanding = Arrays.stream(yLanding).map(y -> (int)(DefaultGameSettings.HEIGHT *0.01*y)).toArray();
        xStart = (int)(xStart * 0.01 * DefaultGameSettings.WIDTH);
        yStart = (int)(yStart * 0.01 * DefaultGameSettings.HEIGHT);

        xMeteors = Arrays.stream(xMeteors).map(x -> (int)(DefaultGameSettings.WIDTH *0.01*x)).toArray();
        yMeteors = Arrays.stream(yMeteors).map(y -> (int)(DefaultGameSettings.HEIGHT *0.01*y)).toArray();
    }

}
