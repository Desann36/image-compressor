package cz.fi.muni.image_compressor;

import cz.fi.muni.image_compressor.common.CompressionType;
import cz.fi.muni.image_compressor.common.DecodeOptions;
import cz.fi.muni.image_compressor.common.EncodeOptions;
import cz.fi.muni.image_compressor.common.Options;
import cz.fi.muni.image_compressor.lossy.LossyEncoder;
import cz.fi.muni.image_compressor.lossless.LosslessEncoder;
import cz.fi.muni.image_compressor.utils.CMDParser;
import cz.fi.muni.image_compressor.utils.CustomReader;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
        String[] argss = new String[]{"-help", "e", "lossless", "image_gray.bmp", ""};
        
        try{
            Options options = CMDParser.parseCommandLine(argss);
            BufferedImage image = CustomReader.readImageFile(options.getInputFile());
            String fileNameWithOutExt = options.getInputFile().getName().replaceFirst("[.][^.]+$", "");

            if(options instanceof EncodeOptions){
                EncodeOptions opt = (EncodeOptions) options;
                
                if(opt.getCompressionType().equals(CompressionType.LOSSLESS)){
                    LosslessEncoder encoder = new LosslessEncoder(opt.getOutputDir());
                    encoder.encode(image, fileNameWithOutExt + "_encoded");
                }
                else{
                    LossyEncoder.encode(image, opt.getOutputDir());
                }
            }
            else if(options instanceof DecodeOptions){
                DecodeOptions opt = (DecodeOptions) options;
            }
        }catch(IllegalArgumentException | IOException e){
            System.err.println(e.getMessage());
        }
    }  
}