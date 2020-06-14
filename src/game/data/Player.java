package game.data;


import game.Constant.DefaultGameSettings;

/**
 * Klasa przechowująca informacje o aktualnie grającym graczu.
 * Singletone. Gracz może być tylko jeden.
 */
public class Player {
    /**
     * Nick gracza
     */
    private String  nick = "Maciej";
    /**
     * Aktualny wynik punktowy gracza
     */
    private int score = 0;
    /**
     * Aktualna liczba żyć gracza, wartość startowa pobierana z pliku konfiguracyjnego
     */
    private int lifes;

    /**
     * Prywatny konstruktor zapobiegający stworzeniu więcej niż jednej instancji gracza
     */
    private Player() {
        if (Holder.INSTANCE != null) {
            throw new IllegalStateException("Singleton already constructed");
        }
    }

    /**
     * Metoda dająca dostęp do instancji gracza
     * @return zwraca instancję gracza
     */
    public static Player getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Klasa tworząca jedyną instancję gracza w całym programie
     */
    private static class Holder {
        private static final Player INSTANCE = new Player();
    }

    /**
     * Metoda pozwalajca ustawić nick gracza
     * @param nick to nick, jaki chcemy nadać graczowi
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Metoda pobierająca aktualny nick gracza
     * @return zwraca aktualny nick gracza
     */
    public String getNick() {
        return this.nick;
    }

    /**
     * Metoda pozwalająca ustawić aktualny wynik punktowy gracza
     * @param score wartość punktowa, jaką chcemy ustawić
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Metoda pobierająca aktualny wynik gracza
     * @return zwraca akrualny wynik gracza
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Metoda resetująca aktualny wynik gracza do 0
     */
    public void resetPlayerScores() { this.score = 0; }

    /**
     * Metod pozwalająca na dodanie punktów do aktaulnego wyniku gracza
     * @param points liczba punktów, która zosatnie dodana do wyniku gracza
     */
    public void addPoints(int points) { this.score+=points; }

    /**
     * Metoda pobierająca aktualną liczbę żyć gracza
     * @return zwraca liczbę żyć
     */
    public int getLifes() { return this.lifes; }

    /**
     * Metoda resetująca liczbę żyć gracza do wartości domyślnej, pobieranej z pliku konfiguracyjnego
     */
    public void resetLifes() {
        this.lifes = DefaultGameSettings.LIFES;
    }

    /**
     * Metoda ustawiająca liczbę żyć gracza na 0
     */
    public void setLifesToZero() {
        this.lifes = 0;
    }

    /**
     * Metoda zmniejszająca liczbę żyć gracza o jeden, jeśli wartość ta nie osiągnęła jeszcze wartości 0
     */
    public void deleteOneLife() {
        if(lifes != 0) {
           lifes--;
        }
    }


}
