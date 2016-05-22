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
    JSlider iloscSlimakowSlider;

    //stworzenie nowego kontenera
    Container panel = getContentPane();

    //zmienne pomocnicze
    static int wielkoscX = 30;
    static int wielkoscY = 10;
    static int liscieKolor[][];
    static boolean czyJestSlimak[][];
    static Slimak iloscSlimakow[];
    static Lisc lisc;
    static Rysunek salata;
    static int ileZrobicSlimakow;
    static int initSzybkosc = 50;
    static int initZarlocznosc = 50;




    //konstruktor
    Slimaki()
    {
        //opis okienka dialogowego
        super("Slimaki");

        try {
            // Set System L&F
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }

        ileZrobicSlimakow = 1;

        liscieKolor = new int[10][10];
        //tablica dwywymiarowa do zaznaczania obecnosci slimaka
        czyJestSlimak = new boolean[10][10];

        //rozmiar okienka
        setSize(800,600);
        //opis dzialania w momencie wcisniecia przycisku zamkniecia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lisc = new Lisc();
        //rozmieszczenie elementow w kontenerze
        panel.setLayout(null);

        //dodanie suwaka zmieniajacego szybkosc odrastania trawy


        ileSlimakowSliderMet();


//==========================================================

        setContentPane(panel);
        setVisible(true);
    }

    public void ileSlimakowSliderMet() {
        JLabel iloscSlimakowLabel = new JLabel("Ilość ślimaków:");
        iloscSlimakowLabel.setBounds(100,10,140,20);

        iloscSlimakowSlider = new JSlider(0,0,10,ileZrobicSlimakow);
        iloscSlimakowSlider.setBounds(250,10,200,20);
//        iloscSlimakowSlider.addChangeListener(e -> {
//            ileZrobicSlimakow = iloscSlimakowSlider.getValue();
//        });

        JTextField iloscSlimakowField = new JTextField();
        iloscSlimakowField.setBounds(450,10,20,20);

        iloscSlimakowField.setText(Integer.toString(ileZrobicSlimakow));

        iloscSlimakowSlider.addChangeListener(e -> {
            iloscSlimakowField.setText(Integer.toString(iloscSlimakowSlider.getValue()));
        });

        JButton okButton = new JButton("OK");
        okButton.setBounds(500,10,50,50);

        okButton.addActionListener(e -> {
            iloscSlimakowSlider.disable();
            iloscSlimakowField.disable();
            ileZrobicSlimakow = iloscSlimakowSlider.getValue();
            namalujPlansze();
        });

        panel.add(iloscSlimakowLabel);
        panel.add(iloscSlimakowSlider);
        panel.add(iloscSlimakowField);
        panel.add(okButton);
    }

    void namalujPlansze() {
        czyJestSlimak = new boolean[wielkoscX][wielkoscY];

        iloscSlimakowSlider.disable();

        iloscSlimakow = new Slimak[ileZrobicSlimakow];
        Random random = new Random();

        for(int i = 0; i < ileZrobicSlimakow; i++)
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
        for (int i = 0; i < wielkoscX; i++) {
            for (int j = 0; j < wielkoscY; j++) {
                liscieKolor[i][j] = random.nextInt(256);
                czyJestSlimak[i][j] = false;
            }
        }
        salata = new Rysunek();
        salata.setBounds(10, 50, 761, 401);
        panel.add(salata);

        narysujDolneSlidery();

        panel.repaint();
    }

    public void narysujDolneSlidery() {
        JSlider szybkoscOdrastaniaSlider = new JSlider(0, 0, 100, lisc.szybkosc);

        //okreslenie rozmiaru i lokalizaji poszczegolnych elementow
        //przebiegSymulacjiLabel.setBounds(370, 10, 140, 20);
        start.setBounds(600, 460, 140, 90);

        odrastanieTrawyLabel.setBounds(10, 460, 140, 20);
        szybkoscOdrastaniaSlider.setBounds(10,485,140,20);

        szybkoscSlimakowLabel.setBounds(200, 460, 140, 20);

        zarlocznoscSlimakowLabel.setBounds(400, 460, 140, 20);

        //dodanie poszczegolnych elementow do panelu
        //panel.add(przebiegSymulacjiLabel);

        panel.add(start);
        panel.add(odrastanieTrawyLabel);
        panel.add(szybkoscOdrastaniaSlider);
        panel.add(szybkoscSlimakowLabel);

        //dodanie sluchaczy zdarzen
        start.addActionListener(this);
        szybkoscOdrastaniaSlider.addChangeListener(new LiscSzybkoscChange());

        JTextField szybkoscOdrastaniaField = new JTextField();
        szybkoscOdrastaniaField.setBounds(10,520,140,20);

        szybkoscOdrastaniaSlider.addChangeListener(e -> {
            szybkoscOdrastaniaField.setText(Integer.toString(szybkoscOdrastaniaSlider.getValue()));
        });

        panel.add(szybkoscOdrastaniaField);

        JSlider szybkoscSlimakowSlider = new JSlider(0, 0,100,initSzybkosc);
        szybkoscSlimakowSlider.setBounds(200,485, 140, 20);
        szybkoscSlimakowSlider.addChangeListener(new SlimakSzybkoscChange(iloscSlimakow));

        JTextField szybkoscSlimakowField = new JTextField();
        szybkoscSlimakowField.setBounds(200,520,140,20);

        szybkoscSlimakowSlider.addChangeListener(e -> {
            szybkoscSlimakowField.setText(Integer.toString(szybkoscSlimakowSlider.getValue()));
        });

        panel.add(szybkoscSlimakowField);
        panel.add(szybkoscSlimakowSlider);

        //petla dodajaca suwaki zmieniajace zarlocznosc poszczegolnych slimakow

        JSlider zarlocznoscWszystkichSlider = new JSlider(0,0,100,initZarlocznosc);
        zarlocznoscWszystkichSlider.setBounds(400,485,140,20);
        zarlocznoscWszystkichSlider.addChangeListener(new SlimakZarlocznoscChange((iloscSlimakow)));

        JTextField zarlocznoscWszystkichField = new JTextField();
        zarlocznoscWszystkichField.setBounds(400,520,140,20);

        zarlocznoscWszystkichSlider.addChangeListener(e -> {
            zarlocznoscWszystkichField.setText(Integer.toString(zarlocznoscWszystkichSlider.getValue()));
        });

        panel.add(zarlocznoscWszystkichField);
        panel.add(zarlocznoscWszystkichSlider);
        panel.add(zarlocznoscSlimakowLabel);
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
                for(int i = 0; i < ileZrobicSlimakow; i++)
                {
                    iloscSlimakow[i].start();
                }
            }

            //utworzenie obiektow klasy Lisc oraz Rysunek



        }
    }
}
