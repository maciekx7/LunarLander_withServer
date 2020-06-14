package game.view;
import game.data.HighScores;
import game.Constant.MenuWindowStates;
import game.entities.Button;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Klasa zajmujaca się wyświetlaniem widoku Najlepszych wyników
 * Rozszerza klasę JPanel
 */
public class HighScoresPanel extends AbstractVerticalPanel {
    /** atrubut przechowywujcy referencję na klasę tablicy najlepszych wyników*/
    private HighScores highScore = HighScores.getInstance();
    /** Najlepsze wyniki gry w formie ArrayList pobierane z klasy HighScores, na początku puste*/
    private ArrayList<HighScores.Record> records;

    /**
     * Konstruktor tworzący komponenty do wyświetlenia w widoku najlepszych wyników
     * Lista rekordow jest dynamiczna, zależna od liczby rekordów załadowanej do instancji HighScore
     * @param menuListner Listner nasłuchujący wciśnięcia przycisków
     */
    public HighScoresPanel(ActionListener menuListner)  {
        super();
        getData();

        for(int i=0; i<highScore.getNumberOfRecords(); i++) {
            JPanel recordPanel = new JPanel();
            recordPanel.add(new JLabel((i+1) + "."));
            recordPanel.add(new JLabel(records.get(i).getNick()));
            recordPanel.add(new JLabel(String.valueOf(records.get(i).getScore()),SwingConstants.RIGHT));
            super.verticalPanel.add(recordPanel);
        }

        super.verticalPanel.add(new Button(menuListner, MenuWindowStates.MENU_BUTTON, MenuWindowStates.MENU));
    }

    /** Metoda pobierające najlepsze wyniki*/
    void getData() {
        highScore.downloadData();
        records = highScore.getRecords();
    }
}
