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
        String outputDir = "";
        int i = 0;
        
        try {
            String arg = getArgument(args, i++);

            //optional arguments
            while(arg.startsWith("-")) {
                switch (arg) {
                    case "-help":
                        printUsageStatement();
                        break;
                    case "-outdir":
                        arg = getArgument(args, i++);
                        outputDir = arg;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown optional argument!");
                }

                arg = getArgument(args, i++);
            }

            //mandatory arguments
            String compressionOperation = arg;
            String inputFile = getArgument(args, i++);
        
            Options options = new Options(compressionOperation, inputFile, outputDir); 
            
            allArgumentsRead(args, i);
            return options;
        } catch(IllegalArgumentException e) {
            printUsageStatement();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    private static void allArgumentsRead(String[] args, int i){
        if(i < args.length){
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
    
    private static void printUsageStatement(){
        System.out.println("Usage: [-help] [-outdir outputDir] {e | d} input_file");
    }
}
