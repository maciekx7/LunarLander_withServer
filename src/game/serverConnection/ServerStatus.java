package game.serverConnection;

import javax.swing.*;
import java.net.Socket;

/**
 * Klasa przechowująca informację o statusie połączenia z serwerem
 */
public class ServerStatus {
    /**
     * FLaga prechpwująca informację o statusie połączenia z serwerem
     */
    private static boolean IS_CONNECTED = false;

    /**
     * Metoda awarjna. Powinna zosiać wywołana tylko w momencie niespodziewanego utracenia połączenia z serwerem.
     * Próbuje zamknąć połączenie ze strony klienta (jeśli takowe jest jeszcze otwarte)
     * Wywołuje komunikat o niespodziwanej utracie połączenia i daje użytkownikowi możliwość używanie programu dalej
     * w trybie offline, lub zamknięcie programu.
     * @param serverSocket socket servera
     */
    public static void connectionLost(Socket serverSocket) {
        int dialogResult = dialogPopUp("Connection issue", "We've lost connection with the server. Do you want to play offline?");

        ServerConnectivity.closeConnection(serverSocket);

        IS_CONNECTED = false;


        if(dialogResult == JOptionPane.NO_OPTION) {
            System.out.println("NO");
            System.exit(1);
        }
    }

    /**
     * Metoda awaryjna. Powinna zostać wywołana tylko, jeśli klient woła niedozwoloną komendę - jeśli żądanie klienta
     * nie zgadza się z protokołem serwerowym. Zamyka połączenie i pozwala zakończyc grę, lub grać offline.
     * Zamyka połączenie z serwerem i prosi serwer o zamknięcie połączenia z klientem.
     * @param serverSocket Socket serwera
     * @param command Żądanie klienta
     * @param response odpowiedź zwrócona przez server
     */
    public static void wrongCommand(Socket serverSocket, String command, String response) {
        String title = "Protocol issue", text;
        if(response == "INVALID_COMMAND") {
            text = "There is no such command in server protocol. Please, read server protocol first.";
            text+= "\n" + "COMMAND: " + command;
            text+= "\n" + "Do you want to play offline?";
        } else {
            text = "FATAL ERROR\n";
            text+= "Server has a problem with your demand. Connection closed\n";
            text+= "Contact with server administration\n";
            text+= "Your demand: " + command;
        }


        int dialogResult = dialogPopUp(title,text);

        ServerConnectivity.talkWithServer(serverSocket,"LOGOUT");
        ServerConnectivity.closeConnection(serverSocket);

        IS_CONNECTED = false;

        if(dialogResult == JOptionPane.NO_OPTION) {
            System.exit(1);
        }

    }

    /**
     * Ustawa flagę połączenia na true, jeśli serwerSocket nie jest pusty
     * @param socket Socket serwera (może przyjmować wartość null, wtedy flaga nie zmienia statusu).
     */
    public static void connect(Socket socket) {
        if(socket!= null) {
            if(socket.isConnected()) {
                IS_CONNECTED = true;
            }
        }
    }

    /**
     * Zwraca aktualny status flagi połączenia.
     * @return Zwraca stan flagi IS_CONNECTED
     */
    public static boolean isConnected() {
        return IS_CONNECTED;
    }


    /**
     * Okno dialogowe PopUp
     * @param title Nazwa panelu
     * @param text treść w panelu
     * @return int, decyzja użytkownika, YES or NO
     */
    private static int dialogPopUp(String title, String text) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(null, text, title, dialogButton);
        return dialogResult;
    }
}
