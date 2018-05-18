package cz.fi.muni.image_compressor.common;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Data;
import lombok.Getter;

/**
 *
 */
@Data
public abstract class Options {
    @Getter private File inputFile;
    @Getter private Path outputDir;
    
    public Options(String inputFile, String outputDir){
        if(inputFile != null){
            this.inputFile = new File(inputFile);
        }else{
            throw new IllegalArgumentException("Wrong input file!");
        }

        if(outputDir != null){
            this.outputDir = Paths.get(outputDir);
        }else{
            throw new IllegalArgumentException("Wrong output directory!");
        }
    }
}
