package game.window;

import game.Constant.DefaultGameSettings;
import game.Constant.MenuWindowStates;
import game.serverConnection.ServerConnectivity;
import game.view.GameView;
import game.view.HelpPanel;
import game.view.HighScoresPanel;
import game.view.MenuPanel;

import game.serverConnection.ServerStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

/**
 * Klasa okna gry. Jest odpowiedzialna za wyświetlanie menu, wszystkich podmenu, a także samej gry
 * Rozszerza po JFrame oraz implementuje interfejs ActionListener do nasłuchiwania nadchodzących zdarzeń
 * Działa na zasadzie zamieniania JPanel'i zgodnie z aktualnym stanem gry
 */
public class GameWindow extends JFrame implements ActionListener{

    /** domyślna szerokość okna menu */
    private int defaultWidth = MenuWindowStates.WIDTH;
    /** domyślna wysokość okna menu */
    private int defaultHeight = MenuWindowStates.HEIGHT;
    /** domyślna szerokość okna gry */
    private int gameWidth = DefaultGameSettings.WIDTH;
    /** domyślna wysokość okna gry */
    private int gameHeight = DefaultGameSettings.HEIGHT;

    /** panel menu gry, zawiera przyciski do przechodzenia pomiędzy poszczególnymi submenu */
    private MenuPanel menuPanel = null;
    /** panel najlepszych wyników, zawiera listę 5 najlepszych wyników */
    private HighScoresPanel highScoresPanel = null;
    /** panel pomocy, zawiera krótką instrukcję gry */
    private HelpPanel helpPanel = null;
    /** panel gry, odpowiedzialny za gameplay */
    private GameView gamePanel = null;
    /** socket serwera, z którym ewentualnie klient się komunikuje */
    private Socket serverSocket;

    /** Konstruktor głównego okna gry, ustala jego rozmiar oraz układa elementy graficzne
     * @param socket socket servera, może być null gdy nie chcemy łączyć się z serwerem
     */
    public GameWindow(Socket socket) {
        setWindowSizeAndFocus();
        MakeUI();
        if(socket!=null) {
            serverSocket = socket;
        }
    }

    /**
     * Metoda ustawiająca podstawowe parametry okna, t.j.
     * tytuł, rozmiar, layout, położenie, focus
     */
    private void setWindowSizeAndFocus() {

        this.setFocusable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(MenuWindowStates.GAME_TITLE);
        setWindowSize(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     * Metoda ustawiająca domyślny rozmiar okna
     * @param game flaga czy jest to okno gry,
     *             jeśli tak to ustawia domyslny rozmiar dla gry,
     *             jeśli nie to ustawia domyślny rozmiar dla menu
     */
    private void setWindowSize(boolean game) {

        if (!game) {
            setSize(defaultWidth, defaultHeight);
            this.setResizable(false);
        } else {
            setSize(gameWidth, gameHeight);
            this.setResizable(true);
            setLocationRelativeTo(null);
        }
    }

    /**
     * Metoda odpowiedzialna za stworzenie domyślnego panelu jakim jest menu
     */
    private void MakeUI() {
        menuPanel = new MenuPanel(this);
        this.add(menuPanel);
    }

    /**
     * Metoda usuwająca aktualny panel w oknie
     * Dopuszaczmy możliwość istnienia tylko jednego panelu na raz,
     * zatem wywoływana za każdym razem gdy zmieniamy panel
     */
    private void removeAllPanels() {
        this.getContentPane().removeAll();
         if (menuPanel != null) {
            menuPanel = null;
        } if (highScoresPanel != null) {
            highScoresPanel = null;
        } if (helpPanel != null) {
            helpPanel = null;
        } if (gamePanel != null){
            gamePanel.running = false;
            gamePanel = null;
            setSize(defaultWidth,defaultHeight);
            setLocationRelativeTo(null);
        }
    }

    /**
     * Metoda rozróżniająca panel gry od reszty
     * Ustawia layout dla panelu oraz wymusza focus na panelu gry
     * @param game flaga czy aktualny panel to panel gry
     * @param panel referencja do aktualnego panelu
     */
    private void setPanelOptions(boolean game, JPanel panel) {
        setWindowSize(game);
        if(game)
            setLayout(new BorderLayout());
        else
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        this.add(panel);
        if(game) {
            panel.setFocusable(true);
            panel.requestFocus();
            panel.requestFocusInWindow();
        }
        this.revalidate();
        this.repaint();
    }

    /**
     * Metoda przywracająca domyślny panel menu
     */
    public void goToMenu(){
        removeAllPanels();
        menuPanel = new MenuPanel(this);
        setPanelOptions(false, menuPanel);
    }

    /**
     * Meotda z interfejsu ActionListener, nasłuchująca nadchodzące zdarzenia
     * Zmienia panele gry zgodnie z wykonaną akcją
     * Jeśli tym zdarzeniem było naciśnięcie przycisku:
     * EXIT - wychodzi z aplikacji
     * MENU - ustawia panel menu
     * NEW GAME - uruchamia panel gry
     * HIGH SCORES - ustawia panel najlepszych wyników
     * HELP - ustawia panel pomocy
     * @param actionEvent zdarzenie przychodzące z UI
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        switch (action) {
            case MenuWindowStates.EXIT:
                if(ServerStatus.isConnected()) {
                    ServerConnectivity.talkWithServer(serverSocket,"LOGOUT");
                    ServerConnectivity.closeConnection(serverSocket);
                    System.out.println("We've closed server connection");
                }
                removeAllPanels();
                System.exit(0);
                break;

            case MenuWindowStates.MENU:
                removeAllPanels();
                menuPanel = new MenuPanel(this);
                setPanelOptions(false,menuPanel);
                break;


            case MenuWindowStates.NEW_GAME:
                removeAllPanels();
                gamePanel = new GameView(gameWidth, gameHeight, this, serverSocket);
                setPanelOptions(true,gamePanel);
                break;

            case MenuWindowStates.HIGH_SCORES:
                removeAllPanels();
                highScoresPanel = new HighScoresPanel(this);
                setPanelOptions(false,highScoresPanel);
                break;

            case MenuWindowStates.HELP:
                removeAllPanels();
                helpPanel = new HelpPanel(this, serverSocket);
                setPanelOptions(false,helpPanel);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
    }

}
