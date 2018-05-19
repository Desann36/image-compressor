package cz.fi.muni.image_compressor.common;

/**
 * Stores user-specified information regarding decoding.
 */
public class DecodeOptions extends Options{
    
    /**
     * Creates a new instance of DecodeOptions.
     * 
     * @param inputFile path to input file
     * @param outputDir directory for output file
     */
    public DecodeOptions(String inputFile, String outputDir) {
        super(inputFile, outputDir);
    }
}
