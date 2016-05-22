package pl.edu.pwr.java;

/**
 * Created by Michał Bizoń on 22.05.2016.
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Klasa SlimakZarlocznoscSlider impementuje klase ChangeListener i umozliwia
 * zmiane wartosci szybkosci slimaka poprzez zmiane polozenia suwaka.
 */
class SlimakZarlocznoscChange implements ChangeListener
{
    //utworzenie slimaka
    Slimak slimak;
    //konstruktor domyslny
    SlimakZarlocznoscChange(Slimak slim)
    {
        slimak = slim;
    }

    //metoda umozliwiajaca zmiane wartosci zarlocznosci pod wplywem przesuniecia suwaka
    public void stateChanged(ChangeEvent e)
    {
        slimak.zarlocznosc = ((JSlider)e.getSource()).getValue();
    }
}
