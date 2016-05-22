package pl.edu.pwr.java;

import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Klasa Lisc rozszerza klase Thread (Watek) oraz implementuje odrastajace liscie
 * na planszy 10x10
 */
class Lisc extends Thread
{
    //zmienna pomocnicza okreslajaca szybkosc odrastania trawy
    int szybkosc;

    //konstruktor domyslny
    Lisc()
    {
        szybkosc = 40;
    }

    /**
     * Metoda okresla dzialanie po uruchomieniu watku Trawa
     */
    public void run()
    {
        while(true)
        {
            //odstep miedzy odrastaniem
            while(szybkosc == 0);
            {
                try
                {
                    sleep((100-szybkosc)*10);
                }
                catch(InterruptedException e) { }
            }
            //symulacja odrastania trawy dla kazdego pola
            synchronized(Slimaki.liscieKolor)
            {
                for(int i = 0; i < 10; i++)
                {
                    for(int j = 0; j < 10; j++)
                    {
                        if(Slimaki.liscieKolor[i][j] < 255)
                        {
                            //zwiekszenie 'nasycenia' koloru
                            Slimaki.liscieKolor[i][j]++;
                        }
                    }
                }
            }
            //odswiezenie widoku planszy
            Slimaki.salata.repaint();
        }
    }
}

/**
 * Klasa Slimak rozszerza klase Thread (Watek) oraz implementuje slimaka
 * poruszajacego sie po planszy 10x10
 */
class Slimak extends Thread
{
    //zmienne pomocnicze okreslajace polozenie szybkosc poruszania sie oraz 'zarlocznosc' slimaka
    int pozW, pozH, szybkosc, zarlocznosc;

    //konstruktor domyslny
    Slimak(int pw, int ph)
    {
        pozW = pw;
        pozH = ph;
        szybkosc = 50;
        zarlocznosc = 60;
    }

    /**
     * Metoda okresla dzialanie po uruchomieniu watku Slimak
     */
    public void run()
    {
        //zmienna losowa
        Random random = new Random();
        while(true)
        {
            //odstep miedzy kolejnymi ruchami
            try
            {
                sleep((100 - szybkosc) * 10);
            }
            catch(InterruptedException e) { }

            //jesli ustawiona szybkosc jest wieksza od 0
            if(szybkosc > 0)
            {
                synchronized(Slimaki.liscieKolor)
                {
                    //zmienna przechowujaca kolor
                    int i = Slimaki.liscieKolor[pozW][pozH];
                    //zmienne przechowujace polozenie
                    int j = pozW;
                    int k = pozH;
                    int l = 1;
                    //zmiana koloru lisci na skutek zarlocznosci
                    Slimaki.liscieKolor[pozW][pozH] -= zarlocznosc;

                    //kontrola czy kolor liscia jest wiekszy od 0 (zakres 0-255), jesli nie ustawiamy 0
                    if(Slimaki.liscieKolor[pozW][pozH] < 0)
                    {
                        Slimaki.liscieKolor[pozW][pozH] = 0;
                    }

                    //zmiana polozenia slimaka
                    for(int p = 0; p < 10; p++)
                    {
                        int x;
                        int y;
                        if(p==0)
                        {
                            x = pozW - 1;
                            y = pozH;
                        }
                        else if(p==1)
                        {
                            x = pozW + 1;
                            y = pozH;
                        }
                        else if(p==2)
                        {
                            x = pozW;
                            y = pozH - 1;
                        }
                        else
                        {
                            x = pozW;
                            y = pozH + 1;
                        }

                        //jesli w wyniku ruchu znajdziemy sie poza obszarem lub na polu zajmowanym przez innego slimaka
                        if(x < 0 || x >= 10 || y < 0 || y >= 10 || Slimaki.czyJestSlimak[x][y] != false)
                        {
                            continue;
                        }

                        //wybor najbardziej zielonego pola
                        if(Slimaki.liscieKolor[x][y] > i)
                        {
                            i = Slimaki.liscieKolor[x][y];
                            j = x;
                            k = y;
                            l = 1;
                            continue;
                        }
                        if(Slimaki.liscieKolor[x][y] != i)
                        {
                            continue;
                        }
                        l++;

                        if(random.nextInt(l) == 0)
                        {
                            j = x;
                            k = y;
                        }
                    }

                    //opuszczenie (przez slimaka) poprzedniego pola
                    Slimaki.czyJestSlimak[pozW][pozH] = false;
                    //zmiana pozycji
                    pozW = j;
                    pozH = k;
                    //zajecie nowego pola
                    Slimaki.czyJestSlimak[pozW][pozH] = true;
                }
            }
            //odswiezenie rysunku
            Slimaki.salata.repaint();
        }
    }
}

/**
 * Klasa rysunek rozszerza klase JPanel oraz implementuje metody rysujace
 * 'slimaki' oraz plansze 10x10
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
            for(int i = 0; i < 10; i++)
            {
                for(int j = 0; j < 10; j++)
                {
                    g.setColor(new Color(0, Slimaki.liscieKolor[i][j], 0));
                    g.fillRect(i*30, j*30, 30, 30);
                }
            }
            //rysowanie slimakow
            g.setColor(new Color(240,0,0));
            for(int j = 0; j < 4; j++)
            {
                g.fillOval(3 + 30 * Slimaki.slimaki[j].pozW, 3 + 30 * Slimaki.slimaki[j].pozH, 23, 23);
            }
        }
    }
}

/**
 * Klasa SlimakSzybkoscSlider impementuje klase ChangeListener i umozliwia
 * zmiane wartosci szybkosci slimaka poprzez zmiane polozenia suwaka.
 */
class SlimakSzybkoscChange implements ChangeListener
{
    //utworzenie slimaka
    Slimak slimak;
    //konstruktor domyslny
    SlimakSzybkoscChange(Slimak slim)
    {
        slimak = slim;
    }

    //metoda umozliwiajaca zmiane wartosci szybkosci pod wplywem przesuniecia suwaka
    public void stateChanged(ChangeEvent e)
    {
        slimak.szybkosc = ((JSlider)e.getSource()).getValue();
    }
}

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
    static Slimak slimaki[];
    static Lisc lisc;
    static Rysunek salata;

    //konstruktor
    Slimaki()
    {
        //opis okienka dialogowego
        super("Slimaki");
        //rozmiar okienka
        setSize(500,400);
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
        przebiegSymulacjiLabel.setBounds(10, 10, 140, 20);
        salata.setBounds(10, 40, 301, 301);
        start.setBounds(330, 40, 130, 30);
        odrastanieTrawyLabel.setBounds(330, 80, 140, 20);
        szybkoscSlimakowLabel.setBounds(330, 130, 140, 20);
        szybkoscOdrastaniaSlider.setBounds(325,100,140,20);
        zarlocznoscSlimakowLabel.setBounds(330, 240, 140, 20);

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

        //petla dodajaca suwaki zmieniajace szybkosc poszczegolnych slimakow
        for(int i = 0; i < 4; i++)
        {
            JSlider szybkoscSlider = new JSlider(0, 0, 100, slimaki[i].szybkosc);
            szybkoscSlider.setBounds(325, 150+i*20 ,140 ,20);
            szybkoscSlider.addChangeListener(new SlimakSzybkoscChange(slimaki[i]));
            panel.add(szybkoscSlider);
        }

        //petla dodajaca suwaki zmieniajace zarlocznosc poszczegolnych slimakow
        panel.add(zarlocznoscSlimakowLabel);
        for(int j = 0; j < 4; j++)
        {
            JSlider zarlocznoscSlider = new JSlider(0, 0, 100, slimaki[j].zarlocznosc);
            zarlocznoscSlider.setBounds(325, 260+j*20, 140, 20);
            zarlocznoscSlider.addChangeListener(new SlimakZarlocznoscChange(slimaki[j]));
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
            if(slimaki[0].isAlive())
            {
                JOptionPane.showMessageDialog(null, "Watki zostaly juz uruchomione");
            }
            //w przeciwnym razie uruchamiamy watki poruszajacych sie slimakow oraz watek liscia
            else
            {
                lisc.start();
                for(int i = 0; i < 4; i++)
                {
                    slimaki[i].start();
                }
            }
        }
    }

    /**
     * Metoda main wypelniajaca losowym kolorem pola planszy oraz
     * rozmieszczajaca na niej slimaki
     */
    static  public void main(String arg[])
    {
        //tablica dwuwymiarowa na przechowywanie kolorow pol
        liscieKolor = new int[10][10];
        //tablica dwywymiarowa do zaznaczania obecnosci slimaka
        czyJestSlimak = new boolean[10][10];
        //zmienna losowa
        Random random = new Random();
        //wypelnienie pol planszy losowym kolorem zielonym
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                liscieKolor[i][j] = random.nextInt(256);
                czyJestSlimak[i][j] = false;
            }
        }
        //utworzenie 4 slimakow
        slimaki = new Slimak[4];
        //rozmieszczenie slimakow na planszy
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
            slimaki[i] = new Slimak(pozW, pozH);
        }
        Slimaki slimaki = new  Slimaki();
    }
}


