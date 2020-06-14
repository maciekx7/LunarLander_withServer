package server;

import server.configReader.ConfigReader;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Klasa przetrzymująca aktualną tablicę wyników gry.
 * Singletone.
 * Najlepsze wyniki gry pobierane są z plików konfiguracyjnych gry i na bierząco aktualizowane
 */
public class ScoreBoard {
    private Socket serverSocket = null;

    /** atrybut statyczny przechowywujący nazwę pliku konfiguracyjnego z najlepszymi wynikami gry. Bez rozszerzenia.*/
    private static final String fileName = "scoreBoard";

    /** ArrayList przechowywująca rekordy w postaci klasy Record, allokujemy jej rozmiar na top+1*/
    private ArrayList<Record> records = new ArrayList<Record>(numberOfRecords+1);

    /** atrybut, który przechowywuje liczbę najlepszych wyników pobieranych z configa */
    private static int numberOfRecords;

    /** flaga sprawdzająca, czy dane zostały już pobrane z pliku konfiguracyjnego */
    private boolean isDataDonwloaded = false;

    /**
     * Konstruktor prywatny zabezpieczający przed multi-wywolaniem klasy HighScores
     */
    private ScoreBoard() {
        if (ScoreBoard.Holder.INSTANCE != null) {
            throw new IllegalStateException("Singleton already constructed");
        }
    }

    /**
     * Metoda statyczna, dzięki której dostajemy się do instancji HighScores
     * @return zwraca instancję najlepszych wyników
     */
    public static ScoreBoard getInstance() {
        return ScoreBoard.Holder.INSTANCE;
    }

    /** Klasa statyczna tworząca jedyną instancję tablicy najlepszych wyników */
    private static class Holder {
        private static final ScoreBoard INSTANCE = new ScoreBoard();
    }

    //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
    /**
     * Klasa przechowywująca pojedynczy rekord najlepszych wyników
     * Implementuje interfejs Comparable, aby móc sortować wyniki.
     */
    public class Record implements Comparable<Record>{
        /** nick */
        private String nick;
        /** wynik punktowy*/
        private int score;
        public Record(String nick, int score) {
            this.nick = nick;
            this.score = score;
        }

        /**
         * metoda zwracająca wynik
         * @return zwraca wynik punktowy
         */
        public Integer getScore() {
            return this.score;
        }

        /**
         * Metoda zwracająca nick
         * @return zwraca nick
         */
        public String getNick() {
            return this.nick;
        }

        /**
         * Komparator, infoemuje, że podczas sortowania interesują nas porówywania wyników punktowych
         * @param record instancja klasy Record
         * @return zwraca wynik porownania dwóch rekordów
         */
        @Override
        public int compareTo(Record record) {
            return this.getScore().compareTo(record.getScore());
        }
    }
    //-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-

    public void setSocket(Socket server) {
        serverSocket = server;
    }


    /**
     * Zwraca najlepsze rekordy
     * @return zwraca listę najlepszych rekordów
     */
    public ArrayList<Record> getRecords() {
        return records;
    }

    /**
     * Zwraca liczbę najlepszych wyników
     * @return zwraca liczbę najlepszych wyników
     */
    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    /**
     * Pobiera najlepsze wyniki z pliku konfiguracyjnego i ładuje je do ArrayList w kolejności od najlepszego do najgorszego.
     * Posiada zabezpieczenie sprawdzające, czy dane nie zostały już pobrane (flaga isDataDonwloaded)
     */
    public void downloadData() {
        if (!isDataDonwloaded) {
            String[] nicks;
            int[] scores;

            System.out.println("ScoreBoard downloaded");
            nicks = (ConfigReader.getValue(fileName, "nicks")).split(",");
            scores = Arrays.stream(ConfigReader.getValue(fileName,"scores").split(",")).mapToInt(Integer::parseInt).toArray();
            numberOfRecords = Integer.parseInt(ConfigReader.getValue(fileName, "numerOfRecords"));
            for(int i=0; i<numberOfRecords; i++) {
                records.add(new Record(nicks[i], scores[i]));
            }
            records.sort(Record::compareTo);
            Collections.reverse(records);
            isDataDonwloaded = true;
        }
    }

    /**
     * Metoda aktualizujaca wyniki w pliku konfiguracyjnym, jeśli wynik gracza powinien się na niej znaleźć.
     * Metoda najpierw pobiera dane z pliku config (jeśli nie zostały jeszcze pobrane)
     * Następnie sprawdza, czy wynik gracza jest lepszy lub równy najgorszemu wynikowi z tablicy wynikow
     * Jeśli tak się stanie, dodaje wynik gracza do ArrayList records, sortuje listę w kolejności od największego do najmniejszego
     * i usuwa ostatni Record.
     * Następnie zapisuje nową listę najlepszych wyników w pliku konfiguracyjnym.
     * @param data Tablica zawierajaca nick i wynik gracza
     * @return zwraca informacje o tym, czy przekazany wynik gracza zostal zapisany do tablicy wynikow, czy nie
     */
    public String checkPlayerScore(String[] data) {
        this.downloadData();
        String nicks = "";
        String scores = "";

        int playerScore = Integer.parseInt(data[1]);
        String playesNick = data[0];

        if(playerScore >= records.get(getNumberOfRecords() - 1).getScore()) {

            //Adding player result (if better than worst result in array
            //then sorting from max to minimum and deleting worst score
            records.add(new Record(playesNick,playerScore));
            records.sort(Record::compareTo);
            Collections.reverse(records);
            records.remove(numberOfRecords);

            //Adding first record to string
            if (!records.isEmpty()) {
                nicks = nicks + records.get(0).getNick();
                scores = scores + records.get(0).getScore();
            }

            //adding rest of records to string splitted by ','
            for(int i=1; i<numberOfRecords; i++) {
                nicks = nicks + "," + records.get(i).getNick();
                scores = scores + "," + records.get(i).getScore();
            }

            ConfigReader.setValue(fileName, "nicks", nicks);
            ConfigReader.setValue(fileName, "scores", scores);
            System.out.println("Ranging reloaded + saved in online file");
            return "SCORE_SAVED";
        }
        return "SCORE_REJECTED";
    }
}
