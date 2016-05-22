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
class LiscSzybkoscChange implements ChangeListener
{
    //metoda umozliwiajaca zmiane wartosci szybkosci pod wplywem przesuniecia suwaka
    public void stateChanged(ChangeEvent e)
    {
        Slimaki.lisc.szybkosc = ((JSlider)e.getSource()).getValue();
    }
}