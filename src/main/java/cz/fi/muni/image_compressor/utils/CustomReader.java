package cz.fi.muni.image_compressor.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 */
public class CustomReader {
    public static BufferedImage readImageFile(File inputFile) throws IOException{
        BufferedImage image = null;
        
        try
        {
            image = ImageIO.read(inputFile);
        }
        catch(IOException e)
        {
            throw new IOException("Error reading input file!", e);
        }

        return image;
    }
}
