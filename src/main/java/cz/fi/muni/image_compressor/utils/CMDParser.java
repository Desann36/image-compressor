package cz.fi.muni.image_compressor.utils;

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
                System.out.println("Usage: ImageCompressor [-help] {e | d} input_file output_dir");
            }
            else{
                throw new IllegalArgumentException("Unknown optional argument!");
            }
        }
        
        String compressionOperation = getArgument(args, i++);
        String inputFile = getArgument(args, i++);
        String outputDir = getArgument(args, i++);
        
        Options options = new Options(compressionOperation, inputFile, outputDir); 
        
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
