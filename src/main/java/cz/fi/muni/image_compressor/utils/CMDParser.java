package cz.fi.muni.image_compressor.utils;

import cz.fi.muni.image_compressor.common.CompressionType;
import cz.fi.muni.image_compressor.common.Options;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 */
public class CMDParser {
    public static Options parseCommandLine(String[] args){
        String type = null;
        int quality = -1;
        File inputFile = null;
        Path outputDir = null;
        String arg;
        int i = 0;
        
        //optional arguments
        while(i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            if(arg.equals("-help")){
                System.out.println("Usage: ImageCompressor [-help] {lossless | lossy quality} input_file output_dir");
            }
            else{
                throw new IllegalArgumentException("Unknown optional argument!");
            }
        }
        
        //mandatory arguments
        for(int j = 0; j < 3; j++){
            arg = args[i++];
            
            switch (j) {
                case 0:
                    type = arg.toUpperCase();
                    
                    if(type.equals("LOSSY")){
                        try{
                            quality = Integer.parseInt(args[i]);
                        }catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Quality must be a number!", e);
                        }
                        i++;
                    }
                    
                    break;
                case 1:
                    inputFile = new File(arg);
                    break;
                case 2:
                    outputDir = Paths.get(arg);
                    break;
                default:
                    break;
            }
        }
        
        Options opt = new Options(type, inputFile, outputDir);
        
        if(opt.getCompressionType().equals(CompressionType.LOSSY)){
            opt.setLossyQuality(quality);
        }
        
        return opt;
    }
}
