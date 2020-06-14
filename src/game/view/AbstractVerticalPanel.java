package game.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Klasa abstracyjna dziedzicząca po JPanelu, tworząca widok wertykalny komponentów,
 * które zostaną dodane do paneu vetricalPanel.
 */
public abstract class AbstractVerticalPanel extends JPanel {
    /** Panel wertykalny. Po dodaniu do niego komponentów metodą verticalPanel.add(), pojawią się one jeden pod drugim. */
    protected JPanel verticalPanel = new JPanel(new GridLayout(10, 1, 10, 1));

    /**
     * Konstruktor klasy tworzący szkielet widoku vertykalnego
     * Należy wywołać go w swoim konstruktorze metodą super().
     */
    public AbstractVerticalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 6, 5, 6));

        JPanel layout = new JPanel(new GridBagLayout());
        layout.setBorder(new EmptyBorder(10, 10, 10, 10));

        layout.add(verticalPanel);

        panel.add(layout, BorderLayout.CENTER);
        setLayout(new GridLayout(5, 1, 20, 20));
        this.add(panel);
    }
}
