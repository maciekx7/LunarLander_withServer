package game.manager;

import game.Constant.DefaultGameSettings;
import game.Constant.GraphicsConstants;
import game.Constant.LoadLevel;
import game.interfaces.Updatable;
import game.data.HighScores;
import game.data.Player;
import game.data.Points;
import game.entities.*;
import game.controller.KeyHandler;
import game.physic.Collision;
import game.data.Points;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * Klasa manadżera gry, który jest odpowiedzialny za całą logikę i połączenie wszystkich elementów samej rozrywki
 */
public class GameManager implements Updatable {

    /** socket serwera do ewentualnej komunikacji z klientem */
    Socket serverSocket;

    /** zmienne przechowujące grafiki */
    BufferedImage gameOverImg, startImg, wonImage, crashedImg, landedImg, shipDestroyedImg, pauseImg;

    /** aktualny poziom oraz maksymalna liczba poziomów */
    public int currentLevel, maxLevels;
    /** flaga określająca przegraną */
    public boolean gameOver = false;
    /** flaga określająca wygraną */
    public boolean won = false;
    /** flaga określająca rozbicie statku */
    public boolean crashed = false;
    /** flaga określająca pomyślne lądowanie */
    public boolean landed = false;
    /** flaga określająca rozpoczęcie gry */
    public boolean started = false;
    /** flaga określająca zapis wyniku */
    public boolean savedScore = false;

    /** wynik zgromadzony do wygranej lub przegranej */
    public int scoreOnWinOrLose = 0;

    /** obiekt statku gracza
     * @see Ship
     **/
    public Ship ship;
    /** obiekt terenu
     * @see Terrain
     */
    private Terrain terrain;
    /** obiekt lądowiska
     * @see LandingSpace
     */
    private LandingSpace landing;
    /** obiekt zarządcy meteorów
     * @see MeteorHandler
     * @see Meteor*/
    private MeteorHandler meteors;

    /** obiekt klasy sprawdzającej kolizje
     * @see Collision
     */
    private Collision detector;

    /** atrybut przechowujący referencję na klasę gracza
     * @see Player
     */
    public Player player = Player.getInstance();
    /** atrubut przechowywujcy referencję na klasę tablicy najlepszych wyników
     * @see  HighScores
     */
    public HighScores highScores = HighScores.getInstance();
    /** atrubut przechowywujcy referencję na klasę punktów
     * @see Points
     */
    public Points points;

    /**
     * Konstruktor klasy manadżera, inicjalizuje wszystkie pola - wywołanie init() oraz ładuje zasoby - wywołanie loadResources()
     * @param server socket servera, może być null
     */
    public GameManager(Socket server){
        serverSocket = server;
        currentLevel = 1;
        loadResources();
        loadLevel();
    }

    /** Metoda ładująca aktualny poziom */
    private void loadLevel(){
        LoadLevel.getLevel(serverSocket,currentLevel);

        meteors = new MeteorHandler(LoadLevel.numOfMeteors,LoadLevel.xMeteors, LoadLevel.yMeteors, 300, LoadLevel.speedMeteors, LoadLevel.GRAVITY_SPEED);

        maxLevels = DefaultGameSettings.NUMBEROFLEVELS;

        terrain = new Terrain(LoadLevel.xVerticies, LoadLevel.yVerticies);
        landing = new LandingSpace(LoadLevel.xLanding, LoadLevel.yLanding);
        ship = new Ship(LoadLevel.xStart, LoadLevel.yStart, LoadLevel.GRAVITY_SPEED, DefaultGameSettings.FUEL);

        detector = new Collision(ship, terrain, landing, meteors.getMeteors());
    }

    /** Metoda ładująca potrzebne zasoby graficzne */
    private void loadResources(){
        try {
            gameOverImg = ImageIO.read(new File(GraphicsConstants.GAME_OVER_IMAGE));
            startImg = ImageIO.read(new File((GraphicsConstants.MENU_TEXT_IMAGE)));
            wonImage = ImageIO.read(new File(GraphicsConstants.YOU_WON_IMAGE));
            landedImg = ImageIO.read(new File(GraphicsConstants.LANDED_IMAGE));
            crashedImg = ImageIO.read(new File(GraphicsConstants.CRASHED_IMAGE));
            shipDestroyedImg = ImageIO.read(new File(GraphicsConstants.SHIP_DESTROYED_IMAGE));
            pauseImg = ImageIO.read((new File(GraphicsConstants.PAUSE_IMAGE)));
        } catch (IOException e) {
            Logger.getLogger(GameManager.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }

    /** Metoda odpowiedzialna za zresetowanie statku oraz załadowanie odpowiedniego poziomu */
    public void reload(){
        scoreOnWinOrLose = 0;
        if(crashed) {
            player.deleteOneLife();
        }
        if(landed) {
            if(currentLevel < maxLevels) {
                currentLevel++;
            }

            else {
                currentLevel = 1;
                player.resetLifes();
            }
        }
        if(gameOver) {
            player.resetLifes();
            currentLevel = 1;
        }
        won = false;
        gameOver = false;
        crashed = false;
        landed = false;
        started = false;
        savedScore = false;
        loadLevel();
    }

    /**
     * Metoda update()
     * Wywolywana ze stalą częstotliwością
     * Odpowiedzialna za całą logikę gry
     * Między innymi detekcja kolizji, poruszanie statkiem, zapisywanie wyników, ustalanie stanu gry, itd.
     */
    @Override
    public void update() {
        if (started) {
            if(player.getLifes() >= 0) {
                if (!crashed && !landed) {
                    ship.update();
                    meteors.update();
                    crashed = detector.detectFatalColission();
                    landed = detector.detectWin();
                }
            }
            else {
                player.setLifesToZero();
                gameOver = true;
            }
        }

        while((landed || gameOver) && !savedScore) {
            savedScore = true;
            if (gameOver) {
                System.out.println("GameManager Class update method first if");
                highScores.checkPlayerScore(player);
                scoreOnWinOrLose = player.getScore();
                player.resetPlayerScores();
            } else {
                if (currentLevel < maxLevels) {
                    Points.addPointsForLevel(player, ship.getFuel(), currentLevel);
                } else {
                    System.out.println("GameManager Class update method second if");
                    Points.addPointsForLevel(player,ship.getFuel(), currentLevel);
                    Points.bonusForLeftLifes(player,currentLevel);
                    highScores.checkPlayerScore(player);
                    scoreOnWinOrLose = player.getScore();
                    player.resetPlayerScores();
                }
            }
        }

        if(landed && currentLevel == maxLevels)
            won = true;

        if(player.getLifes() == 0 && crashed)
            gameOver = true;

    }

    /**
     * Metoda input()
     * Wywolywana ze stałą częstotliwością
     * Odpowiedzialna za obsluge zdarzeń klawiatury
     * @param key obiekt KeyHandler'a
     * @see KeyHandler
     */
    @Override
    public void input(KeyHandler key) {

        if(!gameOver) {
            if (!started) {
                if (key.enter.down())
                    started = true;
            } else {
                if (!crashed && !landed)
                    ship.input(key);
                else {
                    if (key.enter.down()) {
                        reload();
                    }
                }

                if (key.escape.down()) {
                    crashed = false;
                }

                if (key.space.down()) {
                    ship.pause = true;
                    meteors.pause(true);
                }
                else {
                    ship.pause = false;
                    meteors.pause(false);
                }
            }
        }
    }

    /**
     * Metoda render()
     * Wywoływana ze stałą częstotliwościa 30 razy na sekundę
     * Odpowiedzialna za rysowanie wszystkich obiektów gry do obiektu grafiki
     * @param g obiekt grafiki do którego rysujemy wszstkie obiekty
     */
    @Override
    public void render(Graphics2D g) {
        ship.render(g);
        meteors.render(g);
        landing.render(g);
        terrain.render(g);

        if(ship.pause)
            g.drawImage(pauseImg, 400,100,null);

        if(!started)
            g.drawImage(startImg, 400,100, null);

        if(gameOver){
            g.drawImage(gameOverImg, 200, 100, null);
        }

        if(won){
            g.drawImage(wonImage, 200,100, null);
        }

        if(crashed) {
            g.drawImage(shipDestroyedImg, ship.x, ship.y,null);
            g.drawImage(crashedImg, 0, 0, null);
            if(player.getLifes() != 0)
                g.drawImage(startImg, 400,100,null);
        }

        if(landed)
            g.drawImage(landedImg, 0, 0, null);
    }
}
