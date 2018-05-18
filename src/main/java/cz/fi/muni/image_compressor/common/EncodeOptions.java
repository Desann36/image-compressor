package cz.fi.muni.image_compressor.common;

import lombok.Data;
import lombok.Getter;

/**
 *
 */
@Data
public class EncodeOptions extends Options{
    @Getter private CompressionType compressionType;
    @Getter private int lossyQuality = 40;

    public EncodeOptions(String type, String inputFile, String outputDir){
        super(inputFile, outputDir);
        type = type.toUpperCase();

        if(type.equals("LOSSY") || type.equals("LOSSLESS")){
            this.compressionType = CompressionType.valueOf(type);
        }else{
            throw new IllegalArgumentException("Compression type must be either lossy or lossless!");
        }
    }

    public void setLossyQuality(String lossyQuality){
        int quality = -1;
        
        if(lossyQuality != null){
            try{
                quality = Integer.parseInt(lossyQuality);
            }catch (NumberFormatException e) {
                throw new IllegalArgumentException("Quality must be a number!", e);
            }
            
            if(quality > 0 && quality < 100){
                this.lossyQuality = quality;
            }else{
                throw new IllegalArgumentException("Quality must be > 0 and < 100!");
            }
        }else{
            throw new IllegalArgumentException("Wrong quality!");
        }
    }
}
