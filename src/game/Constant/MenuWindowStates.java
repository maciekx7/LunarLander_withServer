package game.Constant;

import game.serverConnection.ServerStatus;
import game.configReader.ConfigReader;
import game.serverConnection.ServerConnectivity;

import java.net.Socket;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Klasa przechoowywująca stałe menu gry
 */
public final class MenuWindowStates {
    /** string przechowujący komende do serwera */
    private static final String serverCommand = "GET_MENU_SETTINGS";
    /** string prechowywujący nazwę pliku ze ścieżkami do stałych menu gry. BEZ ROZSZERZENIA */
    private static final String fileName = "menu";

    /** Stała przechowująca tytuł gry*/
    public static String GAME_TITLE;

    /** stała przechowywująca nazwę akcji przycisku menu */
    public static final String MENU = "MainMenu";
    /** stała przechowywująca nazwę akcji przycisku help */
    public static final String HELP = "Help";
    /** stała przechowywująca nazwę akcji przycisku nowej gry */
    public static final String NEW_GAME = "NewGame";
    /** stała przechowywująca nazwę akcji przycisku tablicy najlepszych wyników */
    public static final String HIGH_SCORES = "HighScores";
    /** stała przechowywująca nazwę akcji wyjdź */
    public static final String EXIT = "Exit";

    /** stała przechowywująca nazwę przycisku menu */
    public static String MENU_BUTTON;
    /** stała przechowywująca nazwę przycisku help */
    public static String HELP_BUTTON;
    /** stała przechowywująca nazwę przycisku nowej gry */
    public static String NEW_GAME_BUTTON;
    /** stała przechowywująca nazwę przycisku tablicy najlepszych wyników */
    public static String HIGH_SCORES_BUTTON;
    /** stała przechowywująca nazwę wyjdź */
    public static String EXIT_BUTTON;

    /** stała przechowywująca domyślna szerokość okna gry */
    public static int WIDTH;
    /** stała przechowywująca domyślna wysokość okna gry */
    public static int HEIGHT;

    /** stała przechowywująca domyślna szerokość przycisku  */
    public static int BUTTON_WIDTH;
    /** stała przechowywująca domyślna wysokość przycisku  */
    public static int BUTTON_HEIGHT;

    private MenuWindowStates() {
        throw new AssertionError();
    }

    /**
     * Metoda pobierająca menu gry
     * @param serverSocket Socket serwera. Jeśli połączenie jest zestawione, pobiera dane online. Jeśli nie - lokalne.
     */
    public static void downloadMenu(Socket serverSocket) {
        if(ServerStatus.isConnected()) {
            try {
                Map<String,String> data = ServerConnectivity.getDecodedDataInMap(serverSocket,serverCommand);
                GAME_TITLE = data.get("gameTitle");
                MENU_BUTTON = data.get("backToMain");
                HELP_BUTTON = data.get("help");
                NEW_GAME_BUTTON = data.get("newGame");
                HIGH_SCORES_BUTTON = data.get("highScores");
                EXIT_BUTTON = data.get("exit");
                WIDTH = parseInt(data.get("width"));
                HEIGHT = parseInt(data.get("height"));
                BUTTON_WIDTH = parseInt(data.get("buttonWidth"));
                BUTTON_HEIGHT = parseInt(data.get("buttonHeight"));
            } catch (Exception e) {
                ServerStatus.connectionLost(serverSocket);
            }
        }
        if(!ServerStatus.isConnected()) {
            GAME_TITLE = ConfigReader.getValue(fileName,"gameTitle");
            MENU_BUTTON = ConfigReader.getValue(fileName, "backToMain");
            HELP_BUTTON = ConfigReader.getValue(fileName, "help");
            NEW_GAME_BUTTON = ConfigReader.getValue(fileName, "newGame");
            HIGH_SCORES_BUTTON = ConfigReader.getValue(fileName, "highScores");
            EXIT_BUTTON = ConfigReader.getValue(fileName, "exit");
            WIDTH = parseInt(ConfigReader.getValue(fileName, "width"));
            HEIGHT = parseInt(ConfigReader.getValue(fileName, "height"));
            BUTTON_WIDTH = parseInt(ConfigReader.getValue(fileName, "buttonWidth"));
            BUTTON_HEIGHT = parseInt(ConfigReader.getValue(fileName, "buttonHeight"));
        }
    }
}
