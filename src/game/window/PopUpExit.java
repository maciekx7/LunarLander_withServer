package game.window;

import game.view.AbstractVerticalPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa odpowiedzialna za tworzenie okna popUp komunikujacego o wystąpieniu krytycznego błędu i zamykajacego program
 * Klasa dziedziczy po AbstractVerticalPanel
 * @see AbstractVerticalPanel
 */
public class PopUpExit extends AbstractVerticalPanel {
    /**
     * Konstruktor tworzący okno błędu krytycznego
     * @param exeptionText Text błędu pojawiającego sie w oknie błędu
     */
    public PopUpExit(String exeptionText) {
        super();

        JFrame popUp = new JFrame();
        {
            popUp.setTitle("Error Window");
            popUp.setSize(500, 150);
            popUp.setLocationRelativeTo(null);
            popUp.setResizable(false);
            popUp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            popUp.setLayout(new FlowLayout());
        }

        //adding elements to vertical JPanel from abstract class
        super.verticalPanel.add(new JLabel("Sorry, we've cought an error", SwingConstants.CENTER));
        super.verticalPanel.add(new JLabel(exeptionText, SwingConstants.CENTER));

        JButton exitButton = new JButton("Exit");
        super.verticalPanel.add(exitButton);

        //Adding abstract vertical JPanel to JFrame
        popUp.add(super.verticalPanel);

        //Button AcctionListner which provide exit action onClick
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        popUp.setVisible(true);
    }
}

