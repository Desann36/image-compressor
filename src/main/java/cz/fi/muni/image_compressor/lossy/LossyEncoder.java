package cz.fi.muni.image_compressor.lossy;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import org.jtransforms.fft.FloatFFT_2D;

/**
 *
 */
public class LossyEncoder {

    public static void encode(BufferedImage image, Path outputDir) {
        int rows = image.getHeight();
        int columns = image.getWidth();
        //float[][] values = make2DArray(image);
        float[][] values = new float[][]{
          {5, 7, 8, 0, 0, 0},
          {0, 1, 9, 0, 0, 0},
          {4, 3, 6, 0, 0, 0}
        };
        
        rows = 3;
        columns = 3;
        
        FloatFFT_2D fourierCoefs = new FloatFFT_2D(rows, columns);
        fourierCoefs.realForwardFull(values);
        
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                String sign = values[i][2*j + 1] < 0 ? " - " : " + ";
                //System.out.println(values[i][2*j] + sign + Math.abs(values[i][2*j + 1]) + "i");
            }
        } 
    }
    
    private static float[][] make2DArray(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        float[][] array = new float[height][2*width];
        
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                array[i][j] = image.getRGB(j, i);
            }
        }     
        
        return array;
    }
}
