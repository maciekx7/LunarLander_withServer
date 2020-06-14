package server;

import java.io.*;
import java.net.Socket;

/**
 * Klasa odpowiadająca za dzialanie serwera i nasłuchiwanie żądań przysyłanych do serwera
 */
public class ThreadForServer implements Runnable{
    /**
     * Socket, na jakim serwer ma nasłuchiwać
     */
    private Socket socket;

    /**
     * Konstruktor klasy ustawiający nasłuchiwany Socket
     * @param socket Socket, na jakim serwer ma nasluchiwać
     */
    public ThreadForServer(Socket socket) {
        this.socket = socket;
    }

    /**
     * Metoda nasłuchująca żądania plynące do serwera i odsłyająca odpowiedzi do klienta
     */
    @Override
    public void run() {

        try {
            while (true) {
                InputStream inputStream = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                String fromClient = br.readLine();
                if(fromClient!=null) {
                    System.out.println("FROM_CLIENT: " + fromClient);

                    String serverMessage = ServerProtocol.serverAction(fromClient);
                    pw.println(serverMessage);
                    pw.flush();

                    System.out.println("TO_CLIENT: " + serverMessage);
                    if (serverMessage == "LOGOUT") {
                        System.out.println("user LoggedOut");
                        socket.close();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
