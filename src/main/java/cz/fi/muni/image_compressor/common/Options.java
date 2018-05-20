package cz.fi.muni.image_compressor.common;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Data;
import lombok.Getter;

/**
 * Stores user-specified information regarding image compression.
 */
@Data
public class Options {
    @Getter private File inputFile;
    @Getter private Path outputDir;
    @Getter private CompressionOperation operation;
    
    /**
     * Constructs a new instance of Options.
     * 
     * @param inputFile path to input file
     * @param outputDir directory for output file
     */
    public Options(String operation, String inputFile, String outputDir){
        if(operation != null && (operation.equals("e") || operation.equals("d"))){
            if(operation.equals("e")){
                this.operation = CompressionOperation.ENCODING;
            }
            else{
                this.operation = CompressionOperation.DECODING;
            }
        }else{
            throw new IllegalArgumentException("Wrong compression operation!");
        }
        
        if(inputFile != null){
            this.inputFile = new File(inputFile);
        }
        else{
            throw new IllegalArgumentException("Wrong input file!");
        }

        if(outputDir != null){
            this.outputDir = Paths.get(outputDir);
        }
        else{
            throw new IllegalArgumentException("Wrong output directory!");
        }
    }
}
