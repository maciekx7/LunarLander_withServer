package game.entities;

import game.Constant.MenuWindowStates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Klasa tworząca jednakowe przyciski
 */
public class Button extends JButton {
    /**
     * @param menuListener Listner do nasłuchiwania przycisku
     * @param buttonName nazwa przycisku
     * @param buttonAction nazwa akcji przycisku
     */
    public Button(ActionListener menuListener, String buttonName, String buttonAction) {
        this.setFocusable(false);
        this.addActionListener(menuListener);
        this.setActionCommand(buttonAction);
        this.setText(buttonName);
        this.setPreferredSize(new Dimension(MenuWindowStates.BUTTON_WIDTH,MenuWindowStates.BUTTON_HEIGHT));
    }
}
