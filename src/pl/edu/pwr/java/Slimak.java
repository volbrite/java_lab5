package pl.edu.pwr.java;

import java.util.Random;

/**
 * Created by Michał Bizoń on 22.05.2016.
 */

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
