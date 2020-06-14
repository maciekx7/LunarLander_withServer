package server;
import server.configReader.ConfigReader;

import java.net.ServerSocket;
import java.net.Socket;


/**
 * Klasa inicjująca serwer
 */
public class Server {
    /**
     * Nazwa pliku, w którym znajduje się port, na którym ma działać serwer
     */
    private static String filename = "ip";
    /**
     * Port, na jakim ma działać serwer
     */
    private int port;

    /**
     * Metoda, której zadaniem jest próba włączenia serwera
     */
    public void runServer() {
        Socket socket = null;
        try {
            port = Integer.parseInt(ConfigReader.getValue(filename,"port"));
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is working now and waiting for clients");
            new ServerWindow(serverSocket);
            while (true) {
                socket = serverSocket.accept();
                new Thread(new ThreadForServer(socket)).start();
            }

        } catch (Exception e){
            System.out.println("Sorry, server not working");
            System.out.println(e);
        }
    }
}
