package cz.fi.muni.image_compressor.common;

import java.io.File;
import java.nio.file.Files;
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
     * @param compressionOperation compression operation
     * @param inputFile path to input file
     * @param outputDir directory for output file
     */
    public Options(String compressionOperation, String inputFile, String outputDir){
        if(compressionOperation != null && (compressionOperation.equals("e") || compressionOperation.equals("d"))){
            if(compressionOperation.equals("e")){
                this.operation = CompressionOperation.ENCODING;
            }
            else{
                this.operation = CompressionOperation.DECODING;
            }
        }else{
            throw new IllegalArgumentException("Wrong compression operation!");
        }
        
        if(inputFile != null && this.isValidInputFile(inputFile)){
            this.inputFile = new File(inputFile);
        }
        else{
            throw new IllegalArgumentException("Wrong input file!");
        }

        if(outputDir != null && this.isValidOutputDir(outputDir)){
            if(outputDir.equals("")){
                this.outputDir = Paths.get(System.getProperty("user.dir"));
            }else{
                this.outputDir = Paths.get(outputDir);
            }
        }
        else{
            throw new IllegalArgumentException("Wrong output directory!");
        }
    }
    
    private boolean isValidInputFile(String inputFile) {
        Path file = new File(inputFile).toPath();
        return Files.exists(file);
    }
    
    private boolean isValidOutputDir(String outputDir) {
        Path file = new File(outputDir).toPath();
        return Files.isDirectory(file);
    }
}
