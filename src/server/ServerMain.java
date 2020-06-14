package server;

import server.configReader.ConfigReader;

import java.lang.management.ManagementFactory;

/**
 * Klasa main serwera
 */
public class ServerMain {

    public static void main(String[] args) {
        String[] agent = ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);
        try {
            if(agent[0].contains("IDEA")) {
                ConfigReader.useIDE();
            }
        } catch(Exception e) {
            ConfigReader.useTerminal();
        }
        new Server().runServer();
    }

}
