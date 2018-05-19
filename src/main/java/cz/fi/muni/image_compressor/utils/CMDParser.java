package cz.fi.muni.image_compressor.utils;

import cz.fi.muni.image_compressor.common.CompressionType;
import cz.fi.muni.image_compressor.common.DecodeOptions;
import cz.fi.muni.image_compressor.common.EncodeOptions;
import cz.fi.muni.image_compressor.common.Options;

/**
 * Class for parsing the command line arguments. 
 */
public class CMDParser {
    
    /**
     * Parses the command line arguments and stores them in the instance of
     * class Options.
     * 
     * @param args command line arguments
     * @return instance of class Options containing user-specified information
     * about image compression
     */
    public static Options parseCommandLine(String[] args){
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
        
        String codingType = getArgument(args, i++);

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
        }
        else{
            return args[i];
        }
    }
}
