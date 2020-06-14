package game.data;

import game.Constant.DefaultGameSettings;
import game.configReader.ConfigReader;

/**
 * Klasa odpowiedzialna za przyznawanie punktów graczowi
 */
public class Points {
    public Points() { }

    /**
     * Metoda licząca, ile punktów za przejście levelu należy dodac graczowi
     * Po wyliczeniu liczby punktow, dodajemy je do aktualnego wyniku gracza
     * @param player gracz, któremu przydzielamy punkty
     * @param fuel pozostałe paliwo statku
     * @param levelNumber numer levelu, za ktory przyznajemy punkty
     */
    public static void addPointsForLevel(Player player, float fuel, int levelNumber) {
        int K = Integer.parseInt(ConfigReader.getValue("level" + levelNumber, "K"));
        float Z = fuel;
        int M = Integer.parseInt(ConfigReader.getValue("level" + levelNumber, "M"));

        int addPoints = (int) (Z*M) + K;
        player.addPoints(addPoints);

        System.out.println("Points added on level " + levelNumber + ": " + player.getScore() + "\n");
    }


    /**
     * Metoda zliczająca bonus punktowy za pozostałe życia
     * Metoda ma zabezpieczenie przed dodaniem bonusa graczowi, który nie ukończyl żadnego poziomu
     * @param player gracz, któremu przydzielamy punkty
     * @param currentLevel aktualny level, dodatek do debuga
     */
    public static void bonusForLeftLifes(Player player, int currentLevel) {
        int L = player.getLifes();
        int S = DefaultGameSettings.S_POINTS;
        if (player.getScore() != 0) {
            player.addPoints((int)((float)L/(float)3 * S));
            System.out.println("added Extra Points For Ships at level "+ currentLevel +" AND exit to Menu: " + player.getScore() + "\n");
        }
    }

    /**
     * Metoda zliczająca chwilową ilość punktów gracza
     * Metoda nie dodaje punktów do wyniku gracza, zwraca tylko liczbę punktów, którą miałby gracz, gdyby w tym momencie ukończył poziom.
     * @param player gracz, ktoremu zliczamy chwilową liczbę punktów
     * @param fuel aktualny poziom paliwa statku gracza
     * @param levelNumber aktualny level, na którym jest gracz
     * @return zwraca chwilową liczbę punktów gracza
     */
    public static int getLiveScore(Player player, float fuel, int levelNumber) {
        int accualScore = player.getScore();
        int liveScore = accualScore;
        int Z = (int) fuel;
        int L = player.getLifes();
        int K = Integer.parseInt(ConfigReader.getValue("level" + levelNumber, "K"));
        int S = DefaultGameSettings.S_POINTS;
        int M = Integer.parseInt(ConfigReader.getValue("level" + levelNumber, "M"));
        liveScore+=  (int) (( (float) L/ (float) 3) * S) + (Z*M) + K;
        return liveScore;
    }
}
