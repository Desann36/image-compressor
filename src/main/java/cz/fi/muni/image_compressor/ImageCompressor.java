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
 *
 */
public class ImageCompressor {
    
    public static void main(String[] args)
    {
        String[] argss = new String[]{"-help", "e", "lossless", "image_gray.bmp", ""};
        
        try{
            Options options = CMDParser.parseCommandLine(argss);
            BufferedImage image = CustomReader.readImageFile(options.getInputFile());
            
            if(options instanceof EncodeOptions){
                EncodeOptions opt = (EncodeOptions) options;
                
                if(opt.getCompressionType().equals(CompressionType.LOSSLESS)){
                    LosslessEncoder.encode(image, opt.getOutputDir());
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