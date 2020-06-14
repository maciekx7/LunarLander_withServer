package game.view;

import game.Constant.DefaultGameSettings;
import game.interfaces.Updatable;
import game.controller.KeyHandler;
import game.manager.GameManager;
import game.window.GameWindow;
import game.window.PopUpWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.net.Socket;

/**
 * Głowna klasa panelu gry
 * Odpowiedzialna za renderowanie oraz update'owanie gry ze stałą częstotliwością
 */
public class GameView extends JPanel implements Runnable, Updatable {
    /** socket serwera do komunikacji z klientem */
    Socket serverSocket;

    /** zmienna określająca szerokość okna */
    public static int width;
    /** zmienna określająca wysokość okna */
    public static int height;

    /** flaga określająca działanie bądź przerwanie pętli gry */
    public boolean running = false;
    /** flaga określająca wyświetlenie końcowego menu */
    public boolean hasPopUp = false;
    /** flaga określająca wciśnięcie przycisku escape */
    private boolean escClicked = false;

    /** obiiekt okna gry
     * @see  GameWindow*/
    private GameWindow frame;
    /** obiekt managera gry - odpowiedzialny za logikę gry
     * @see GameManager */
    private GameManager manager;
    /** obiekt KeyHandlera - odpowiedzialny za czytanie zdarzeń klawiatury
     * @see KeyHandler*/
    private KeyHandler key;

    /** wątek gry */
    private Thread gameThread;

    /** zmienna określająca aktualną ilość klatek na sekundę */
    private int fps = 30;
    public int frameCount = 0;

    /**
     * Konstruktor klasy Game
     * Ustawia początkowe rozmiary okna oraz inicjuje potrzebne elementy
     * @param width początkowa szerokość okna
     * @param height początkowa wysokość okna
     * @param frame referencja do okna gry
     * @param server socket serwera, może być null
     */
    public GameView(int width, int height, GameWindow frame, Socket server) {
        this.serverSocket = server;
        this.width = width;
        this.height = height;
        this.frame = frame;

        init();
    }

    /**
     * Metoda inicjująca niezbędne elementy
     * Tworzy obiekt managera oraz KeyHandlera
     * @see GameManager
     * @see KeyHandler
     */
    public void init(){
        running = true;
        key = new KeyHandler(this);
        manager = new GameManager(serverSocket);
    }


    /**
     * Metoda pozwalająca zapisać wynik gracza i bezpiecznie wrócić do menu
     */
    private void saveAndGoToMenu(){
        System.out.println("GameViewClass saveAndGoToMenu method");
        manager.points.bonusForLeftLifes(manager.player, manager.currentLevel);
        manager.highScores.checkPlayerScore(manager.player);
        manager.player.resetPlayerScores();
        manager.player.resetLifes();

        frame.goToMenu();
    }

    /**
     * Metoda update()
     * Wywolywana ze stalą częstotliwością
     * Odpowiedzialna za update wszystkich elementów gry - wywołanie manager.update()
     * Na bieżąco ustala rozmiar okna gry
     * @see GameManager
     */
    public void update(){
        manager.update();

        width = getWidth();
        height = getHeight();

        if((manager.won || manager.gameOver) && !hasPopUp){
            hasPopUp = true;
            new PopUpWindow(frame, manager, key, this);
        }
    }

    /**
     * Metoda input()
     * Wywolywana ze stałą częstotliwością
     * Odpowiedzialna za obsluge zdarzeń klawiatury w grze - wywolanie manager.inpu()
     * @see GameManager
     * @param key obiekt KeyHandler'a
     * @see KeyHandler
     */
    public void input(KeyHandler key){
        manager.input(key);

        if(key.escape.down())
            if(!escClicked) {
                saveAndGoToMenu();
                escClicked = true;
            }

    }

    /**
     * Metoda render()
     * Wywoływana ze stałą częstotliwościa 30 razy na sekundę
     * Odpowiedzialna za rysowanie wszystkich obiektów gry do obiektu grafiki - wywołanie manager.render()
     * Dopasowuje rozmiar wszystkich obiektów do rozmiaru okna dynamicznie za pomoca przekształcenia affinicznego
     * @see GameManager
     * @param g2d obiekt grafiki do którego rysujemy wszstkie obiekty
     */
    public void render(Graphics2D g2d){
        g2d.clearRect(0,0, getWidth(), getHeight());

        g2d.setColor(new Color(60, 60, 80));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.white);
        AffineTransform saveTransform = g2d.getTransform();
        AffineTransform scaleMatrix = new AffineTransform();
        float sx =(1f+(getSize().width-DefaultGameSettings.WIDTH)/(float)DefaultGameSettings.WIDTH);
        float sy =(1f+(getSize().height-DefaultGameSettings.HEIGHT)/(float)DefaultGameSettings.HEIGHT);
        scaleMatrix.scale(sx, sy);
        g2d.setTransform(scaleMatrix);

        Font bigFont = new Font("Monospaced", Font.BOLD, 15);
        g2d.setFont(bigFont);

        g2d.drawString("FPS: " + fps, 5, 20);
        g2d.drawString("Current Level: " + manager.currentLevel, 5, 40);
        g2d.drawString("Lifes: " + manager.player.getLifes(), 5, 60);
        g2d.drawString("Fuel tank: " + manager.ship.getFuel(), 5, 80);
        g2d.drawString("Max Landing Speed: " + manager.ship.getMaxLandingSpeed(), 5, 100);
        g2d.drawString("Speed (X: " +  String.format("%.1f", manager.ship.getSpeedX()) + " Y: " + String.format("%.1f", manager.ship.getSpeedY()) + ")", 5, 120);

        if (!manager.crashed && !manager.landed) {
            g2d.drawString("Points:" + String.valueOf(manager.points.getLiveScore(manager.player, manager.ship.getFuel(), manager.currentLevel)), 5,140);
        } else {
            if(manager.landed && (manager.currentLevel != manager.maxLevels) || (manager.crashed && manager.player.getLifes() != 0)) {
                g2d.drawString("Points:" + String.valueOf(manager.player.getScore()), 5, 140);
            } else {
               g2d.drawString("Points:" + String.valueOf(manager.scoreOnWinOrLose),5,140);
            }
        }

        manager.render(g2d);

        g2d.setTransform(saveTransform);
        frameCount++;
    }

    /**
     * Metoda biblioteki swing, odpowiedzialna za rysowanie componentów graficznych
     * Wywolywana 30 razy na sekunde za pomoca wywolania repaint()
     * Wywoluje metode render(Graphics g)
     * @param g obiekt grafiki (kontekst graficzny) do którego rysowane są componenty
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D) g);
        g.dispose();
    }

    /**
     * Metoda biblioteki AWT
     * Tworzy nowy wątek gry
     */
    @Override
    public void addNotify(){
        super.addNotify();

        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }

    /**
     * Metoda run z interfejsu Runnable
     * Główna pętla gry starająca utrzymać stałą częstotliwość odświeżania i renderowania gry
     * Wywołuje metody update, input, repaint ze stałą częstotliwością
     */
    @Override
    public void run() {
        final double GAME_HERTZ = 30.0;
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        final int MAX_UPDATES_BEFORE_RENDER = 5;
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = System.nanoTime();

        final double TARGET_FPS = 30;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (running)
        {
            double now = System.nanoTime();
            int updateCount = 0;

            while( now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER )
            {
                update();
                input(key);
                lastUpdateTime += TIME_BETWEEN_UPDATES;
                updateCount++;
            }

            if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
                lastUpdateTime = now - TIME_BETWEEN_UPDATES;

            input(key);
            repaint();

            lastRenderTime = now;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime)
            {
                fps = frameCount;
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
            {
                Thread.yield();
                try {Thread.sleep(1);} catch(Exception e) {}

                now = System.nanoTime();
            }
        }
    }
}
