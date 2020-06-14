package game.launcher;

import game.Constant.*;
import game.serverConnection.ServerConnectivity;
import game.serverConnection.ServerStatus;
import game.data.HighScores;
import game.data.Player;
import game.window.GameWindow;
import game.configReader.ConfigReader;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

/**
 * Klasa okna launchera, rozszerza po JFrame
 * Odpowiada za stworzenie ramki i kontentu w niej
 * Obsługuje zdarzenia przychodzące z interfejsu graficznego użytkownika
 */
public class LauncherWindow extends JFrame implements ActionListener {
    /**
     * Nazwa pliku konfiguracyjnego z danymi serwera
     */
    String serverFileName = "ip";

    /** Domyślna wartosc pola IP */
    private String ipDefault = ConfigReader.getValue(serverFileName,"ip");
    /** Domyślna wartosc pola port */
    private String portDefault = ConfigReader.getValue(serverFileName,"port");

    /** Przycisk menu */
    private JButton online;
    /** Przycisk menu */
    private JButton offline;
    /** Pola tekstowe menu */
    private JTextField ip, port, nick;
    /** Zawartość pól tekstowych */
    private String ipText, portText;

    /** Wywolanie singletona Player, czyli odwołanie się do obiektu player */
    private Player player = Player.getInstance();

    private HighScores scoreBoard = HighScores.getInstance();

    /** Konstruktor klasy okna, ustawia domyślne rozmiary i układa elementy UI w oknie
     * Korystamy w nim z DocumentListnera, aby nasłuchiwać zmiany w polu tekstowym
     * Jest to zabezpieczenie przed przejściem przez użytkownika dalej w momencie wpisania błędnego nicku
     */
    public LauncherWindow(){
        //Window setting
        setTitle(LauncherConst.LAUNCHER_TITLE);
        {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(LauncherConst.WIDTH, LauncherConst.HEIGHT);
            setLocationRelativeTo(null);
            setResizable(false);
        }
        //layout setting - buttons, textfields
        {
            setLayout(new FlowLayout(FlowLayout.CENTER, 60, 50));

            //--------------------NICK---------------------
            add(new JLabel(LauncherConst.NICK_LABEL));
            nick = (JTextField) add(new JTextField(11));
            //--------------------NICK----------------------

            //------------------BUTTONS---------------------
            online = (JButton) add(new JButton(LauncherConst.ONLINE_LABEL));
            online.setEnabled(false);
            offline = (JButton) add(new JButton(LauncherConst.OFFLINE_LABEL));
            offline.setEnabled(false);
            //------------------BUTTONS---------------------

            //-----------------IP LABEL---------------------
            add(new JLabel(LauncherConst.IP_LABEL));
            ip = (JTextField) add(new JTextField(11));
            ip.setText(ipDefault);
            //-----------------IP LABEL---------------------

            //----------------PORT LABEL--------------------
            add(new JLabel(LauncherConst.PORT_LABEL));
            port = (JTextField) add(new JTextField(11));
            port.setText(portDefault);
            //----------------PORT LABEL--------------------

            offline.addActionListener(this);
            online.addActionListener(this);

            nick.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    changed(documentEvent);
                }

                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    changed(documentEvent);
                }

                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    changed(documentEvent);
                }

                /**
                 * Metoda sprawdzająca, czy pole tekstowe nicku nie jest puste
                 */
                public void changed(DocumentEvent documentEvent) {
                    String currText = nick.getText();

                    if (currText.trim().isEmpty()) {
                        online.setEnabled(false);
                        offline.setEnabled(false);
                    } else {
                        online.setEnabled(true);
                        offline.setEnabled(true);
                    }
                }
            });
        }
        setVisible(true);
    }

    /** Metoda ustawiającą nick aktualnego gracza */
    private void setPlayerNick() {
        var Nick = nick.getText();
        if (!isTextFieldNickEmpty()) {
            player.setNick(Nick);
            player.resetLifes();
        }
    }

    /**
     * Metoda sprawdzająca czy pole tekstowe Nick'u gracza jest puste
     * @return zwraca czy pole jest puste
     */
    private boolean isTextFieldNickEmpty() {
        return nick.getText().trim().isEmpty();
    }

    /** Metoda z interfejsu ActionListener nasłuchująca nadchodzące zdarzenia
     * @param actionEvent zdarzenie pochodzące z interfejsu graficznego
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var source = actionEvent.getSource();
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * Metoda anonimowa definiująca działanie launchera
             * Jeśli użytkownik wcisnął przysik "Online" to gra uruchamia się w trybie online
             * Jeśli użytkownik wcisnął przysik "Offline" to gra uruchamia się w trybie offline
             * Tworzy nowe okno gry
             */
            @Override
            public void run() {
                ipText = ip.getText();
                portText = port.getText();

                if(source == online){
                    Socket serverSocket = ServerConnectivity.connectToServer(ipText, Integer.parseInt(portText));
                    if(serverSocket!=null) {
                        ServerStatus.connect(serverSocket);
                        downloadConfigData(serverSocket);
                        System.out.println("We're playing online!");
                        setPlayerNick();
                        dispose();
                        new GameWindow(serverSocket);
                    } else {
                        JOptionPane.showMessageDialog(null, "We can't connect to the server, sorry.", "No connection", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(source == offline){
                    ServerStatus.connect(null);
                    downloadConfigData(null);
                    System.out.println("Offline game");
                    setPlayerNick();
                    dispose();
                    new GameWindow(null);
                 }
            }
        });
    }
/** metoda pobierająca dane konfiguracyjne z serwera */
    private void downloadConfigData(Socket serverSocket) {
        DefaultGameSettings.donwloanGameSettings(serverSocket);
        MenuWindowStates.downloadMenu(serverSocket);
        GraphicsConstants.donwloanGraphics();
        scoreBoard.setSocket(serverSocket);
    }

}

