package pl.edu.pwr.java;

/**
 * Created by Michał Bizoń on 22.05.2016.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Klasa rysunek rozszerza klase JPanel oraz implementuje metody rysujace
 * 'iloscSlimakow' oraz plansze 10x10
 */
class Rysunek extends JPanel
{
    private static BufferedImage imageToBufferedImage(Image image) {

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return bufferedImage;

    }

    private static Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }


    public BufferedImage skalujObraz(BufferedImage img, int width, int height,
                                    Color background) {

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        if (imgWidth*height < imgHeight*width) {
            width = imgWidth*height/imgHeight;
        } else {
            height = imgHeight*width/imgWidth;
        }
        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setBackground(background);
            g.clearRect(0, 0, width, height);
            g.drawImage(img, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        Image transpImg1 = makeColorTransparent(newImage,Color.GRAY);
        BufferedImage resultImage1 = imageToBufferedImage(transpImg1);

        return resultImage1;
    }

    /**
     * Metoda rysujaca komponenty
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);


        BufferedImage img = null;
//        ClassLoader classLoader = ProgramGlowny.class.getClassLoader();
        try {
            URL defaultImage = ProgramGlowny.class.getResource("../../../..//slimak.png");
//            File imageFile = new File("slimaki/slimak.png");
            img = ImageIO.read(defaultImage);
            img = skalujObraz(img,30,30,Color.GRAY);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
//            g.setColor(new Color(240,0,0));
            for(int j = 0; j < Slimaki.ileZrobicSlimakow; j++)
            {
                g.drawImage(img, 3 + 30 * Slimaki.iloscSlimakow[j].pozW,3 + 30 * Slimaki.iloscSlimakow[j].pozH,null);
                //g.fillOval(3 + 30 * Slimaki.iloscSlimakow[j].pozW, 3 + 30 * Slimaki.iloscSlimakow[j].pozH, 23, 23);
            }
        }
    }
}
