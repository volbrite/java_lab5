package pl.edu.pwr.java;

/**
 * Created by Michał Bizoń on 22.05.2016.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Klasa Slimaki rozszerza klase JFrame (tworzenie okna programu wraz z komponentami - suwaki etc)
 * oraz implementuje ActionListener co w nastepstwie umozliwia obsluge zdarzen generowanych
 * przez przyciski i suwaki.
 */
class Slimaki extends JFrame implements ActionListener
{
    //utworzenie zmiennych opisow pol, przycisku oraz suwakow
    JLabel przebiegSymulacjiLabel = new JLabel ("Przebieg symulacji:");
    private JButton start  = new JButton ("start");
    JLabel odrastanieTrawyLabel = new JLabel ("Odrastanie lisci:");
    JLabel szybkoscSlimakowLabel = new JLabel ("Szybkosc slimakow:");
    JLabel zarlocznoscSlimakowLabel = new JLabel ("Zarlocznosc slimakow:");

    //zmienne pomocnicze
    static int liscieKolor[][];
    static boolean czyJestSlimak[][];
    static Slimak iloscSlimakow[];
    static Lisc lisc;
    static Rysunek salata;

    //konstruktor
    Slimaki()
    {
        //opis okienka dialogowego
        super("Slimaki");
        liscieKolor = new int[10][10];
        //tablica dwywymiarowa do zaznaczania obecnosci slimaka
        czyJestSlimak = new boolean[10][10];

        //rozmiar okienka
        setSize(800,600);
        //opis dzialania w momencie wcisniecia przycisku zamkniecia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //utworzenie obiektow klasy Lisc oraz Rysunek
        lisc = new Lisc();
        salata = new Rysunek();

        //stworzenie nowego kontenera
        Container panel = getContentPane();
        //rozmieszczenie elementow w kontenerze
        panel.setLayout(null);

        //dodanie suwaka zmieniajacego szybkosc odrastania trawy
        JSlider szybkoscOdrastaniaSlider = new JSlider(0, 0, 100, lisc.szybkosc);

        //okreslenie rozmiaru i lokalizaji poszczegolnych elementow
        przebiegSymulacjiLabel.setBounds(370, 10, 140, 20);
        salata.setBounds(10, 40, 301, 301);
        start.setBounds(330, 40, 130, 30);
        odrastanieTrawyLabel.setBounds(630, 80, 140, 20);
        szybkoscSlimakowLabel.setBounds(630, 130, 140, 20);
        szybkoscOdrastaniaSlider.setBounds(625,100,140,20);
        zarlocznoscSlimakowLabel.setBounds(630, 240, 140, 20);

        //dodanie poszczegolnych elementow do panelu
        panel.add(przebiegSymulacjiLabel);
        panel.add(salata);
        panel.add(start);
        panel.add(odrastanieTrawyLabel);
        panel.add(szybkoscOdrastaniaSlider);
        panel.add(szybkoscSlimakowLabel);

        //dodanie sluchaczy zdarzen
        start.addActionListener(this);
        szybkoscOdrastaniaSlider.addChangeListener(new LiscSzybkoscChange());

        iloscSlimakow = new Slimak[4];
        Random random = new Random();

        for(int i = 0; i < 4; i++)
        {
            int pozW = random.nextInt(10);
            int pozH = random.nextInt(10);
            //jesli na danym polu znajduje sie juz slimak losujemy inne miejsce

            while(czyJestSlimak[pozW][pozH])
            {
                pozW = random.nextInt(10);
                pozH = random.nextInt(10);
            }
            //zaznaczamy obecnosc slimaka
            czyJestSlimak[pozW][pozH] = true;
            //dodanie slimaka do tablicy
            iloscSlimakow[i] = new Slimak(pozW, pozH);
        }

        //wypelnienie pol planszy losowym kolorem zielonym
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                liscieKolor[i][j] = random.nextInt(256);
                czyJestSlimak[i][j] = false;
            }
        }



        //petla dodajaca suwaki zmieniajace szybkosc poszczegolnych slimakow
        for(int i = 0; i < 4; i++)
        {
            JSlider szybkoscSlider = new JSlider(0, 0, 100, iloscSlimakow[i].szybkosc);
            szybkoscSlider.setBounds(325, 150+i*20 ,140 ,20);
            szybkoscSlider.addChangeListener(new SlimakSzybkoscChange(iloscSlimakow[i]));
            panel.add(szybkoscSlider);
        }

        //petla dodajaca suwaki zmieniajace zarlocznosc poszczegolnych slimakow
        panel.add(zarlocznoscSlimakowLabel);
        for(int j = 0; j < 4; j++)
        {
            JSlider zarlocznoscSlider = new JSlider(0, 0, 100, iloscSlimakow[j].zarlocznosc);
            zarlocznoscSlider.setBounds(325, 260+j*20, 140, 20);
            zarlocznoscSlider.addChangeListener(new SlimakZarlocznoscChange(iloscSlimakow[j]));
            panel.add(zarlocznoscSlider);
        }

        setContentPane(panel);
        setVisible(true);
    }

    /**
     * Metoda umozliwia obsluge klikniecia przycisku 'start'
     */
    public void actionPerformed (ActionEvent evt)
    {
        //zmienna obiektowa zrodlo - do obslugi klikniecia przyciskow
        Object zrodlo = evt.getSource();

        //jesli kliknelismy przycisk start
        if(zrodlo==start)
        {

            //w momencie, gdy uruchomilismy juz watki otrzymujemy stosowny komunikat
            if(iloscSlimakow[0].isAlive())
            {
                JOptionPane.showMessageDialog(null, "Watki zostaly juz uruchomione");
            }
            //w przeciwnym razie uruchamiamy watki poruszajacych sie slimakow oraz watek liscia
            else
            {
                lisc.start();
                for(int i = 0; i < 4; i++)
                {
                    iloscSlimakow[i].start();
                }
            }
        }
    }
}
