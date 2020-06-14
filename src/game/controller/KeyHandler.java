package game.controller;


import game.view.GameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa KeyHandler implementująca interfes KeyListener
 * Odpowiedzialna za obsługe zdarzeń klawiatury w samej grze
 */
public class KeyHandler implements KeyListener {
    /**
     * Wewnętrzna klasa Key reprezentująca pojedyńczy klawisz
     */
    public class Key {
        /** zmienna reprezentująca stan klawisza, jeśli true to wciśnięty */
        private boolean down;

        /**
         * Konstruktor klasy Key, dodaje klawisz do listy wszystkich klawiszy z klasy KeyHandler
         */
        public Key(){
            keys.add(this);
        }

        /**
         * Metoda zmieniająca stan klawisza na taki jak w parametrze
         * @param pressed stan w jaki chcemy ustawić klawisz
         */
        public void toggle(boolean pressed){
            this.down = pressed;
        }

        /**
         * Metoda zwracająca aktualny stan przyisku
         * @return stan przycisku
         */
        public boolean down(){
            return this.down;
        }
    }

    /** Lista wszystkich klawiszy */
    private List<Key> keys = new ArrayList<Key>();

    /** zmienna reprezentująca przysik "up" */
    public Key up = new Key();
    /** zmienna reprezentująca przysik "down" */
    public Key down = new Key();
    /** zmienna reprezentująca przysik "left" */
    public Key left = new Key();
    /** zmienna reprezentująca przysik "right" */
    public Key right = new Key();
    /** zmienna reprezentująca przysik "space" */
    public Key space = new Key();
    /** zmienna reprezentująca przysik "enter" */
    public Key enter = new Key();
    /** zmienna reprezentująca przysik "escape" */
    public Key escape = new Key();

    /**
     * Konstruktor klasy, dodaje KeyListener do gry
     * @param game panel gry do którego wprowadzamy obsługe zdarzeń tym Hanlderem
     */
    public KeyHandler(GameView game){
        game.addKeyListener(this);
        game.setFocusable(true);
    }

    /**
     * Metoda zmieniająca stan wszystkic przycisków na nie wciśnięte
     */
    public void releaseAll(){
        for(Key key : keys){
            key.toggle(false);
        }
    }

    /**
     * Metoda zmieniająca stan przycisku
     * @param e zdarzenie pochodzące z klawiatury - przycisk
     * @param pressed stan w jaki ustawić dany przycisk
     */
    public void toggle(KeyEvent e, boolean pressed){
        if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) up.toggle(pressed);
        if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(pressed);
        if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(pressed);
        if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(pressed);
        if(e.getKeyCode() == KeyEvent.VK_SPACE) space.toggle(pressed);
        if(e.getKeyCode() == KeyEvent.VK_ENTER) enter.toggle(pressed);
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) escape.toggle(pressed);

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    /**
     * Metoda z interfejsu KeyListener'a, obsluguje zdarzenie wcisnięcia przycisku
     * Pod wplywem zdarzenia wywołujemy metode która zmienia stan klawisza na wciśnięty
     * W przypadku spacji jedynie zmieniamy jej stan na przeciwny - implementacja pauzy
     * @param keyEvent zdarzenie pochodzące z klawiatury - przycisk
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() != KeyEvent.VK_SPACE)
            toggle(keyEvent, true);
        else
            toggle(keyEvent, !space.down);
    }

    /**
     * Metoda z interfejsu KeyListener'a, obsluguje zdarzenie puszczenia przycisku
     * Pod wplywem zdarzenia wywołujemy metode która zmienia stan klawisza na nie wciśnięty
     * W przypadku spacji tego nie robimy - implementacja pauzy
     * @param keyEvent zdarzenie pochodzące z klawiatury - przycisk
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() != KeyEvent.VK_SPACE)
            toggle(keyEvent, false);
    }
}
