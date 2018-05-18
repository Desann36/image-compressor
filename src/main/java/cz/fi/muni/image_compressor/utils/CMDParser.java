package cz.fi.muni.image_compressor.utils;

import cz.fi.muni.image_compressor.common.CompressionType;
import cz.fi.muni.image_compressor.common.DecodeOptions;
import cz.fi.muni.image_compressor.common.EncodeOptions;
import cz.fi.muni.image_compressor.common.Options;

/**
 *
 */
public class CMDParser {
    
    public static Options parseCommandLine(String[] args){
        String codingType = null;
        String arg;
        int i = 0;
        
        //optional arguments
        while(args[i].startsWith("-")) {
            arg = getArgument(args, i++);

            if(arg.equals("-help")){
                System.out.println("Usage: ImageCompressor [-help] {e {lossless | lossy quality} | d} input_file output_dir");
            }
            else{
                throw new IllegalArgumentException("Unknown optional argument!");
            }
        }
        
        codingType = getArgument(args, i++);

        if(codingType.equals("e")){
            return parseEncoding(args, i);
        }
        else if(codingType.equals("d")){
            return parseDecoding(args, i);
        }
        else{
            throw new IllegalArgumentException("Wrong coding type!");
        }
    }
    
    private static Options parseEncoding(String[] args, int i){
        String quality = null;
        
        String compressionType = getArgument(args, i++);

        if(compressionType.equals("lossy")){
            quality = getArgument(args, i++);
        }

        String inputFile = getArgument(args, i++);
        String outputDir = getArgument(args, i++);
        
        EncodeOptions options = new EncodeOptions(compressionType, inputFile, outputDir); 
            
        if(options.getCompressionType().equals(CompressionType.LOSSY)){
            options.setLossyQuality(quality);
        }
        
        allArgumentsRead(args, i);
        return options;
    }
    
    private static Options parseDecoding(String[] args, int i){
        String inputFile = getArgument(args, i++);
        String outputDir = getArgument(args, i++);
        
        DecodeOptions options = new DecodeOptions(inputFile, outputDir);
        
        allArgumentsRead(args, i);
        return options;
    }
    
    private static void allArgumentsRead(String[] args, int i){
        if(i != args.length){
            throw new IllegalArgumentException("Too many arguments!");
        }
    }
    
    private static String getArgument(String[] args, int i){
        if(i >= args.length){
            throw new IllegalArgumentException("Some arguments are missing!");
        }else{
            return args[i];
        }
    }
}
