package sepehr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sepehr on 10/17/2017.
 */
public class ImageProcessing {

    // Print text
    public BufferedImage printText (String text, int startPixelX, int startPixelY, BufferedImage image) throws IOException {

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.drawString(text, startPixelX, startPixelY);

        return image;
    }

    // Print text with custom color
    public BufferedImage printText (String text, int startPixelX, int startPixelY, BufferedImage image, Color textColor) throws IOException {

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(textColor);
        graphics.drawString(text, startPixelX, startPixelY);

        File string = new File("image/string.png");
        ImageIO.write(image, "png", string);

        return image;
    }

    // Print text with custom color and font
    public BufferedImage printText (String text, int startPixelX, int startPixelY, BufferedImage image, Color textColor, Font font) throws IOException {

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(textColor);
        graphics.setFont(font);
        graphics.drawString(text, startPixelX, startPixelY);

        File string = new File("image/string.png");
        ImageIO.write(image, "png", string);

        return image;
    }

    // Map two image
    public BufferedImage mapImage (BufferedImage templateImage, int startPixelX, int startPixelY, BufferedImage contentImage) {

        System.out.println("Image width " + templateImage.getWidth());
        System.out.println("Image height " + templateImage.getHeight());

        for (int i = 0; i < contentImage.getWidth(); i++) {
            for (int j = 0; j < contentImage.getHeight(); j++) {
                Color color = new Color(contentImage.getRGB(i,j));
                /*System.out.println("Pixel --- " + i + "," + j);
                System.out.println("Red   - " + color.getRed());
                System.out.println("Green - " + color.getGreen());
                System.out.println("Blue  - " + color.getBlue());
                System.out.println("Alpha - " + color.getAlpha());
                System.out.println("Transparency - " + color.getTransparency());*/
                templateImage.setRGB(startPixelX + i, startPixelY + j, color.getRGB());
            }
        }

        return templateImage;
    }

    public BufferedImage resolveTransparency (BufferedImage image) {

        BufferedImage bi = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_ARGB);
        for (int x=0;x<image.getWidth();x++){
            for (int y=0;y<image.getHeight();y++){
                int rgba = image.getRGB(x,y);
                boolean isTrans = (rgba & 0xff000000) == 0;
                if (isTrans){
                    bi.setRGB(x,y, (Color.gray.getRGB()&0x00ffffff));
                } else {
                    bi.setRGB(x,y,rgba);
                }
            }
        }

        return bi;
    }
}
