package game;

import game.configReader.ConfigReader;
import game.launcher.LauncherWindow;

import javax.swing.*;
import java.lang.management.ManagementFactory;

/**
 * Klasa główna launchera gry. Wywołuje konstruktor okna launchera. Zawiera main()!
 */
public class GameLauncher {

    public GameLauncher(){
        new LauncherWindow();
    }

    public static void main (String[] args) {
        String[] agent = ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);
        try {
            if(agent[0].contains("IDEA")) {
                ConfigReader.useIDE();
            }
        } catch(Exception e) {
                ConfigReader.useTerminal();
        }
        SwingUtilities.invokeLater(new Runnable() { // Run new Thread
            @Override
            public void run() {
                new GameLauncher();
            }
        });
    }

}
