package cz.fi.muni.image_compressor.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class for reading image files.
 */
public class CustomReader {

    /**
     * Reads image file and returns the read data stored in BufferedImage.
     * 
     * @param inputFile file to read from
     * @return a BufferedImage containing the contents of input file
     * @throws IOException if an error occurs during reading
     */
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
