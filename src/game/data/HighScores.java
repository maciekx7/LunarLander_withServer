package game.data;

import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import game.serverConnection.ServerStatus;
import game.configReader.ConfigReader;
import game.serverConnection.ServerConnectivity;

/**
 * Klasa przetrzymująca aktualną tablicę wyników gry.
 * Singletone.
 * Najlepsze wyniki gry pobierane są z plików konfiguracyjnych gry i na bierząco aktualizowane
 */
public class HighScores {

    /** socket serwera do ewentualnej komunikacji z klientem */
    private Socket serverSocket = null;

    /** atrybut statyczny przechowywujący nazwę pliku konfiguracyjnego z najlepszymi wynikami gry. Bez rozszerzenia.*/
    private static final String fileName = "highScores";

    /** ArrayList przechowywująca rekordy w postaci klasy Record, allokujemy jej rozmiar na top+1*/
    private ArrayList<Record> records = new ArrayList<Record>(numberOfRecords+1);

    /** atrybut, który przechowywuje liczbę najlepszych wyników pobieranych z configa */
    private static int numberOfRecords;

    /** flaga sprawdzająca, czy dane zostały już pobrane z pliku konfiguracyjnego */
    private boolean isDataDonwloaded = false;

    /**
     * Konstruktor prywatny zabezpieczający przed multi-wywolaniem klasy HighScores
     */
    private HighScores() {
        if (HighScores.Holder.INSTANCE != null) {
            throw new IllegalStateException("Singleton already constructed");
        }
    }

    /**
     * Metoda statyczna, dzięki której dostajemy się do instancji HighScores
     * @return zwraca instancję najlepszych wyników
     */
    public static HighScores getInstance() {
        return HighScores.Holder.INSTANCE;
    }

    /** Klasa statyczna tworząca jedyną instancję tablicy najlepszych wyników */
    private static class Holder {
        private static final HighScores INSTANCE = new HighScores();
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

    /**
     * Metoda inicjująca socket serwera
     * @param server socket serwera z którym się łączymy
     */
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

            if(ServerStatus.isConnected()) {
                try {
                    System.out.println("ScoreBoard online");
                    Map<String,String> data = ServerConnectivity.getDecodedDataInMap(serverSocket,"GET_SCOREBOARD");
                    nicks = (data.get("nicks")).split(",");
                    scores = Arrays.stream(data.get("scores").split(",")).mapToInt(Integer::parseInt).toArray();
                    numberOfRecords = Integer.parseInt(ServerConnectivity.getDecodedDataInMap(serverSocket, "GET:scoreBoard@numerOfRecords").get("numerOfRecords"));
                    for(int i=0; i<numberOfRecords; i++) {
                        records.add(new Record(nicks[i], scores[i]));
                    }
                    records.sort(Record::compareTo);
                    Collections.reverse(records);
                    isDataDonwloaded = true;

                } catch(Exception e) {
                    System.out.println(e);
                    ServerStatus.connectionLost(serverSocket);
                }
            }
            if(!ServerStatus.isConnected()) {
                try {
                    System.out.println("ScoreBoard Offline");
                    nicks = (ConfigReader.getValue(fileName, "nicks")).split(",");
                    scores = Arrays.stream(ConfigReader.getValue(fileName,"scores").split(",")).mapToInt(Integer::parseInt).toArray();
                    numberOfRecords = Integer.parseInt(ConfigReader.getValue(fileName, "numerOfRecords"));
                    for(int i=0; i<numberOfRecords; i++) {
                        records.add(new Record(nicks[i], scores[i]));
                    }
                    records.sort(Record::compareTo);
                    Collections.reverse(records);
                    isDataDonwloaded = true;
                } catch(Exception e) {
                    System.out.println(e);
                }

            }
        }
    }

    /**
     * Metoda aktualizująca wyniki w pliku konfiguracyjnym, jeśli wynik gracza powinien się na niej znaleźć.
     * Metoda ta najpierw pobiera dane z pliku config (jeśli nie zostały jeszcze pobrane)
     * Następnie sprawdza, czy wynik gracza jest lepszy lub równy najgorszemu wynikowi z tablicy wyników
     * Jeśli tak się stanie, dodaje wynik gracza do ArrayList records, sortuje listę w kolejności od największego do najmniejszego
     * i usuwa ostatni Record.
     * Następnie zapisuje nową listę najlepszych wyników w pliku konfiguracyjnym.
     * @param player gracz, ktorego wynik sprawdzamy, czy powinien znaleźć się na tablicy wyników
     */
    public void checkPlayerScore(Player player) {
        System.out.println("checkPlayerScore Method");
        this.downloadData();
        String nicks = "";
        String scores = "";
        String response = null;

        if (ServerStatus.isConnected()) {
            try {
                System.out.println("wlazlem w zapisywanie wyniku gracza");
                String command = "SAVE_SCORES:" + player.getNick() + "@" + player.getScore();
                response = ServerConnectivity.talkWithServer(serverSocket, command);
                System.out.println(response);
                if(response.contains("SCORE_SAVED")) {
                    System.out.println("ranking reloading");
                    records = new ArrayList<>();
                    isDataDonwloaded = false;
                    this.downloadData();
                    saveScoreBoardToFile("by server");
                }
            } catch (Exception e) {
                System.out.println(e);
                ServerStatus.connectionLost(serverSocket);
            }
        }
        if(!ServerStatus.isConnected()) {
            try {
                if (player.getScore() >= records.get(numberOfRecords - 1).getScore()) {

                    records.add(new Record(player.getNick(), player.getScore()));
                    records.sort(Record::compareTo);
                    Collections.reverse(records);
                    records.remove(numberOfRecords);
                    saveScoreBoardToFile("by game");
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    private void saveScoreBoardToFile(String byWho) {
        String nicks = "";
        String scores = "";
        //Adding first record to string
        if (!records.isEmpty()) {
            nicks = nicks + records.get(0).getNick();
            scores = scores + records.get(0).getScore();
        }

        //adding rest of records to string splitted by ','
        for (int i = 1; i < numberOfRecords; i++) {
            nicks = nicks + "," + records.get(i).getNick();
            scores = scores + "," + records.get(i).getScore();
        }
        ConfigReader.setValue(fileName, "nicks", nicks);
        ConfigReader.setValue(fileName, "scores", scores);
        System.out.println("Ranging reloaded " + byWho + " and saved in local file");
    }
}
