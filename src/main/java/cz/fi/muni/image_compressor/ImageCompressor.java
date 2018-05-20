package cz.fi.muni.image_compressor;

import cz.fi.muni.image_compressor.common.CompressionOperation;
import cz.fi.muni.image_compressor.common.Options;
import cz.fi.muni.image_compressor.compression.LosslessEncoder;
import cz.fi.muni.image_compressor.utils.CMDParser;
import cz.fi.muni.image_compressor.utils.CustomReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry class for compressing/decompressing images.
 */
public class ImageCompressor {
    
    /**
     * Main method to start the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        String[] argss = new String[]{"-help", "e", "image_gray.bmp", ""};
        
        try{
            Options options = CMDParser.parseCommandLine(argss);
            BufferedImage image = CustomReader.readImageFile(options.getInputFile());
            String fileNameWithOutExt = options.getInputFile().getName().replaceFirst("[.][^.]+$", "");

            if(options.getOperation().equals(CompressionOperation.ENCODING)){
                LosslessEncoder encoder = new LosslessEncoder(options.getOutputDir());
                encoder.encode(image, fileNameWithOutExt + "_encoded");
            }
            else if(options.getOperation().equals(CompressionOperation.DECODING)){

            }
        }catch(IllegalArgumentException | IOException e){
            Logger.getLogger(LosslessEncoder.class.getName()).log(Level.SEVERE, null, e);
            System.err.println(e.getMessage());
        }
    }  
}