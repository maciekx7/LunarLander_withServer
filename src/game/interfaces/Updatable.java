package game.interfaces;

import game.controller.KeyHandler;

import java.awt.*;

/**
 * Interfejs Updatable, każda klasa która chce być wywoływana w update powinna posiadac te 3 metody:
 * update() - tu dziala logika gry,
 * input() - tu działa obsługa zdarzeń klawiatury,
 * render() - tu działa rysowanie grafiki
 */
public interface Updatable {

    /**
     * Metoda update()
     * Wywolywana ze stalą częstotliwością
     * Odpowiedzialna za update wszystkich elementów gry
     */
     void update();
    /**
     * Metoda odpowiedzialna za obsługe zdarzeń z klawiatury, wywoływana razem z update()
     * @param key KeyHandler - obiekt obsługujący zdarzenia klawiatury
     */
     void input(KeyHandler key);
    /**
     * Metoda render()
     * Wywoływana ze stałą częstotliwościa 30 razy na sekundę
     * Odpowiedzialna za rysowanie wszystkich obiektów gry do obiektu grafiki
     * @param g obiekt grafiki do którego rysujemy wszstkie obiekty
     */
     void render(Graphics2D g);
}
