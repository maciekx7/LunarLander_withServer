package game.view;

import javax.swing.*;
import java.awt.event.ActionListener;

import game.Constant.MenuWindowStates;
import game.entities.Button;


/**
 * Klasa zajmujaca się wyświetlaniem widoku Menu głównego
 * Rozszerza klasę JPanel
 */
public class MenuPanel extends AbstractVerticalPanel {

    /** Lista nazw przycisków w menu */
    private String[] buttonNames = {MenuWindowStates.NEW_GAME_BUTTON, MenuWindowStates.HIGH_SCORES_BUTTON, MenuWindowStates.HELP_BUTTON, MenuWindowStates.EXIT_BUTTON};
    /** Lista nazw akcji wywolywanych przez pryciski*/
    private String[] buttonActionNames = {MenuWindowStates.NEW_GAME, MenuWindowStates.HIGH_SCORES, MenuWindowStates.HELP, MenuWindowStates.EXIT};
    /** Liczba przycisków */
    private int howManyButtons = buttonActionNames.length;

    /**
     * Konstruktor tworzący komponenty do wyświetlenia w głównym menu
     * Przyciski tworzone są dynamicznie w zależności od dlugości tablicy przycisków
     * Przyciski tworzone są z pomocą klasy Button
     * @param menuListner Listner nasłuchujący wciśnięcia przycisków
     */
    public MenuPanel(ActionListener menuListner) {
        super();
        super.verticalPanel.add(new JLabel(MenuWindowStates.GAME_TITLE, SwingConstants.CENTER));
        for (var i = 0; i < howManyButtons; i++) {
            super.verticalPanel.add(new Button(menuListner, buttonNames[i], buttonActionNames[i]));
        }
    }
}
