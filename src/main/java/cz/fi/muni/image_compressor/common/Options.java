package cz.fi.muni.image_compressor.common;

import java.io.File;
import java.nio.file.Path;
import lombok.Data;
import lombok.Getter;

/**
 *
 */
@Data
public class Options {
    @Getter private CompressionType compressionType;
    @Getter private int lossyQuality = 40;
    @Getter private File inputFile;
    @Getter private Path outputDir;

    public Options(String type, File inputFile, Path outputDir){
        type = type.toUpperCase();

        if(type.equals("LOSSY") || type.equals("LOSSLESS")){
            this.compressionType = CompressionType.valueOf(type);
        }else{
            throw new IllegalArgumentException("Compression type must be either lossy or lossless!");
        }

        if(inputFile != null){
            this.inputFile = inputFile;
        }else{
            throw new IllegalArgumentException("Wrong input file!");
        }

        if(outputDir != null){
            this.outputDir = outputDir;
        }else{
            throw new IllegalArgumentException("Wrong output directory!");
        }
    }

    public void setLossyQuality(int quality){
        if(quality > 0 && quality < 100){
            this.lossyQuality = quality;
        }else{
            throw new IllegalArgumentException("Quality must be > 0 and < 100!");
        }
    }
}
