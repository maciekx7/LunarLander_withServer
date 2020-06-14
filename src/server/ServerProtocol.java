package server;
import server.configReader.ConfigReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa protokołu sieciowego, zajmuje się przetwarzaniem komend do serwera i zwracaniem odpowiedzi.
 */
public class ServerProtocol {
    private static boolean acceptingClients = true;
    private static int clientNumber = 0;


    /**
     * Protokół sieciowy, funkcja przyjmuje żądania użytkownika i zwraca mu
     * oczekiwane (lub nie - jeśli komenda jest niepoprawna) żądanie.
     * @param command komenda przysłana przez użytkownika w postaci Stringa
     * @return po przetworzeniu komendy, zwraca odpowiedź w postaci Stringa
     */
    public static String serverAction(String command){
        String serverCommand = command;
        String originalCommand= command;
        System.out.println(command);
        if(command.contains("SAVE_SCORES:")){
            originalCommand=command;
            serverCommand="SAVE_SCORES";
        }
        if(command.contains("LOAD_LEVEL:")) {
            originalCommand=command;
            serverCommand="LOAD_LEVEL";
        }
        if(command.contains("GET:")) {
            originalCommand=command;
            serverCommand="GET";
        }
        if(command.contains("PUT:")) {
            originalCommand=command;
            serverCommand = "PUT";
        }

        String filename;
        String[] keys;
        String serverMessage;
        switch (serverCommand){
            case "GAME_SETTINGS":
                keys = new String[]{"width", "height", "lifes", "numberOfLevels", "fuelLevel", "maxLandingSpeed", "speedAccelerating", "startSpeedX", "startSpeedY", "S"};
                filename = "gameSettings";
                serverMessage=getCodedContent(filename,keys);
                break;

            case "GET_MENU_SETTINGS":
                keys = new String[]{"gameTitle", "newGame", "help", "highScores", "exit", "backToMain", "width", "height","buttonWidth","buttonHeight"};
                filename = "menu";
                serverMessage=getCodedContent(filename,keys);
                break;

            case "LOAD_LEVEL":
                String text[] = originalCommand.split(":");
                keys = new String[]{"gravitySpeed", "xStart","yStart","xVertecies","yVertecies","xLanding", "yLanding", "numberOfMeteors", "xMeteors", "yMeteors", "speedMeteors", "K","M"};
                filename = "level" + text[1];
                serverMessage=getCodedContent(filename,keys);
                break;

            case "GET_HELPTEXT":
                filename = "help";
                int lines = Integer.parseInt(ConfigReader.getValue(filename, "lines"));
                keys = new String[lines];
                for(int i=1; i<=lines; i++) {
                    keys[i-1] = String.valueOf(i);
                }
                serverMessage=getCodedContent(filename,keys);
                break;

            case "GET":
                filename = getFileNameFormCommand(originalCommand);
                keys = getKeysFromCommand(originalCommand);
                serverMessage=getCodedContent(filename,keys);
                break;

            case "PUT":
                filename = getFileNameFormCommand(originalCommand);
                String data = removeFileNameFromCommand(originalCommand);
                serverMessage=saveDecodedValue(filename,data);
                break;

            case "LOGIN":
                serverMessage=login();
                break;

            case "GET_SCOREBOARD":
                filename = "scoreBoard";
                keys = new String[]{"nicks","scores"};
                ScoreBoard scoreBoard = ScoreBoard.getInstance();
                scoreBoard.downloadData();
                serverMessage=getCodedContent(filename,keys);
                break;


            case "SAVE_SCORES":
                serverMessage=updateScoreBoard(originalCommand);
                break;

            case "LOGOUT":
                serverMessage=logout();
                break;

            default:
                serverMessage="INVALID_COMMAND\n";
        }
        return serverMessage;
    }


    /**
     * Metoda loguje użytkownika do serwera
     * @return zwraca wiadomość zwrotną w postaci Stringa
     */
    private static String login(){
        String serverMessage;
        if(acceptingClients) {
            clientNumber++;
            serverMessage="LOG_IN "+clientNumber+"\n";
        } else {
            serverMessage="CONNECTION_REJECTED\n";
        }
        return serverMessage;
    }


    /**
     * Wylogowywuje użytkownika
     * @return zwraca komunikat "LOGOUT"
     */
    private static String logout(){
        //String serverMessage = "LOGGED OUT " + clientNumber + "\n";
        clientNumber--;
        return "LOGOUT";
    }

    /**
     * Metoda zwracająca komunikat w momencie zakończenia połączenia
     * @return zwraca komunikat "CLOSE_CONNECTION_NOW"
     */
    private static String connectionClosed(){
        return "CLOSE_CONNECTION_NOW";
    }


    /**
     * Prywatna metoda statyczna zapisująca zakodowane informacje z komendy przysłanej do serwera do plików konfiguracyjnych
     * serwera.
     * @param filename String. Nazwa pliku na serwerze, w jakim należy zapisać informacje
     * @param command Komenda przysłana do serwera, w sklad ktorej wchodzi polecenie główne (np. "PUT:")
     *                i informacje o danych do zapisu w odpowiedniej postacji (klucz#wartość).
     *                Istnieje możliwość zapisania więcej niż jednej wartości jednocześnie do tego samego plik
     *                należy wtedy oddzielać krotki klucza i wartości znakiem "@"
     * @return
     */
    private static String saveDecodedValue(String filename, String command) {
        try {
            String[] trash = command.split(":");    // -> trahs[0] smieci, trash[1] -> dobry text
            String[] newData = trash[1].split("@"); // -> newData[0] -> lista nickow, newData[1] -> lista scorow
            for(String bytes: newData) {
                String[] finalData = bytes.split("#");
                boolean controlFlag = ConfigReader.setValue(filename, finalData[0],finalData[1]);
                if(!controlFlag) {
                    return "FATAL_ERROR\n";
                }
            }
        } catch(Exception e) {
            return "FATAL_ERROR\n";
        }

        return ":  VALUE_SAVED" + "\n";
    }

    /**
     * Prywatna metoda statyczna pobierająca żądane informacje z plików konfiguracyjnych i kodująca je w sposób zgodny
     * z protokolem sieciowym serwera
     * @param filename String. Nazwa pliku, z którego serwer ma pobrać dane
     * @param keys Tablica Stringów. Lista kluczy, które mają zostać pobrane z pliku konfiguracyjnego.
     *             Może być to zarówno jeden, jak i kilka kluczy.
     * @return
     */
    private static String getCodedContent(String filename, String[] keys) {
        boolean controllString = false;
        StringBuilder command = new StringBuilder();
        try {
            String keyNames[] = keys;
            for(String key:keyNames) {
                String info = ConfigReader.getValue(filename, key);
                if(info == "FATAL_ERROR") {
                    controllString = true;
                    System.out.println("FATAL_ERROR on '" + key + "' key");
                }
                if(command.toString().equals("")) {
                    command.append(key).append("#").append(info);
                } else {
                    command.append("@").append(key).append("#").append(info);
                }
            }

        } catch(Exception e) {
            command = new StringBuilder("FATAL_ERROR");
        }
        if(controllString) {
            command = new StringBuilder("FATAL_ERROR");
        }

        return command.toString() +  "\n";
    }

    /**
     * Metoda zajmująca się aktualizowaniem tablicy wyników
     * @param command Komenda nadesłana do serwera
     * @return Zwraca informację o tym, czy tablica wynikow została zaktualizowana, czy nie
     */
    private static String updateScoreBoard(String command) {
        String[] data = getNickAndScoreFromString(command);
        ScoreBoard scoreBoard = ScoreBoard.getInstance();

        String response = scoreBoard.checkPlayerScore(data);

        if(response == "SCORE_SAVED") {
            return "SCORE_SAVED\n";
        } else {
            return "SCORE_TOO_LOW\n";
        }

    }







    //METODY POMOCNICZE


    /**
     * Prywayna metoda pomocnicza protokołu sieciowego, pobierająca z żądania przysłanego do serwera
     * tylko informację o nazwie pliku, z którego serwer ma skorzystać
     * @param command Komenda przysłana do serwera
     * @return String z nazwą pliku
     */
    private static String getFileNameFormCommand(String command) {
        String trash[] = command.split(":");
        String putData[] = trash[1].split("@");
        return putData[0];
    }

    /**
     * Prywatna meoda pomocnicza protokołu sieciowego usuwająca z komendy przysłanej do serwera nazwę  pliku,
     * ktorego dotyczy żądanie
     * @param command komenda przysłana do serwera
     * @return String zawierający komendę bez nazwy pliku
     */
    private static String removeFileNameFromCommand(String command) {
        String returnComamand = "";
        String[] trash = command.split(":");
        String[] newTrash = trash[1].split("@");
        for(int i=1; i<newTrash.length; i++) {
            if(returnComamand == "") {
                returnComamand+= trash[0] + ":" + newTrash[i];
            } else {
                returnComamand+= "@" + newTrash[i];
            }

        }
        return returnComamand;
    }


    /**
     * Prywatna metoda pomocnicza protokołu sieciowego pobierająca z komendy przysłanej do serwera tylko klucze
     * informacji, których dotyczy żądanie
     * @param command Komenda przyslana do serwera
     * @return Tablica Stringów zawierjąca listę kluczy poszukiwanych przez klienta
     */
    private static String[] getKeysFromCommand(String command) {
        command = removeFileNameFromCommand(command);
        String[] x = command.split(":");
        String[] xx = x[1].split("@");
        List<String> keys = new ArrayList<>();
        for(String xxx: xx) {
            String[] y = xxx.split("#");
            keys.add(y[0]);
        }
        return keys.toArray(new String[keys.size()]);

    }

    /**
     * Priwatna metoda pomocnicza przetwarzająca otrzymane na tablicę zawierającą wynik i punkty gracza
     * @param string komenda nadeslana do serwera
     * @return tablica przechowywująca na 1 miejscu nick, a na drugim miejscu punkty gracza
     */
    private static String[] getNickAndScoreFromString(String string) {
        String[] trash = string.split(":");
        String[] data = trash[1].split("@");
        return data;
    }

}
