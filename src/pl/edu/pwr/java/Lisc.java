package pl.edu.pwr.java;

/**
 * Created by Michał Bizoń on 22.05.2016.
 */

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
