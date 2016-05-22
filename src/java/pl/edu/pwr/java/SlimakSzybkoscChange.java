package pl.edu.pwr.java;

/**
 * Created by Michał Bizoń on 22.05.2016.
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa SlimakSzybkoscSlider impementuje klase ChangeListener i umozliwia
 * zmiane wartosci szybkosci slimaka poprzez zmiane polozenia suwaka.
 */
class SlimakSzybkoscChange implements ChangeListener
{
    //utworzenie slimaka
    Slimak[] slimaki;
    //konstruktor domyslny


    SlimakSzybkoscChange(Slimak[] slim)
    {
        slimaki = slim;
    }

    //metoda umozliwiajaca zmiane wartosci szybkosci pod wplywem przesuniecia suwaka
    public void stateChanged(ChangeEvent e)
    {
        for (Slimak s: slimaki) {
            s.szybkosc = ((JSlider)e.getSource()).getValue();
        }
    }
}
