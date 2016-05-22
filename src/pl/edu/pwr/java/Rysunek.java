package pl.edu.pwr.java;

/**
 * Created by Michał Bizoń on 22.05.2016.
 */

import javax.swing.*;
import java.awt.*;

/**
 * Klasa rysunek rozszerza klase JPanel oraz implementuje metody rysujace
 * 'iloscSlimakow' oraz plansze 10x10
 */
class Rysunek extends JPanel
{
    /**
     * Metoda rysujaca komponenty
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        synchronized(Slimaki.liscieKolor)
        {
            //rysowanie 'lisci' salaty
            for(int i = 0; i < Slimaki.wielkoscX; i++)
            {
                for(int j = 0; j < Slimaki.wielkoscY; j++)
                {
                    g.setColor(new Color(0, Slimaki.liscieKolor[i][j], 0));
                    g.fillRect(i*30, j*30, 30, 30);
                }
            }
            //rysowanie slimakow
            g.setColor(new Color(240,0,0));
            for(int j = 0; j < Slimaki.ileZrobicSlimakow; j++)
            {
                g.fillOval(3 + 30 * Slimaki.iloscSlimakow[j].pozW, 3 + 30 * Slimaki.iloscSlimakow[j].pozH, 23, 23);
            }
        }
    }
}
