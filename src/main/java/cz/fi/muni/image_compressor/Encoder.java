/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.image_compressor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author DL
 */
public class Encoder {
    
    public enum CompressionType {
        LOSSLESS, LOSSY
    }
    
    private class Options {
        private CompressionType compressionType;
        private int lossyQuality = 40;
        private String inputFile;
        private String outputDir;
        
        public Options(String type, String inputFile, String outputDir){
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
    
    private Options parseCommandLine(String[] args){
        String type = null;
        int quality = -1;
        String inputFile = null;
        String outputDir = null;
        String arg;
        int i = 0;
        
        //optional arguments
        while(i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            if(arg.equals("-help")){
                System.out.println("Usage: Encoder [-help] {lossless | lossy quality} input_file output_dir");
            }
            else{
                throw new IllegalArgumentException("Unknown optional argument!");
            }
        }
        
        //mandatory arguments
        for(int j = 0; j < 3; j++){
            arg = args[i++];
            
            switch (j) {
                case 0:
                    type = arg.toUpperCase();
                    
                    if(type.equals("LOSSY")){
                        try{
                            quality = Integer.parseInt(args[i]);
                        }catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Quality must be a number!", e);
                        }
                        i++;
                    }
                    
                    break;
                case 1:
                    inputFile = arg;
                    break;
                case 2:
                    outputDir = arg;
                    break;
                default:
                    break;
            }
        }
        
        Options opt = new Options(type, inputFile, outputDir);
        
        if(opt.compressionType.equals(CompressionType.LOSSY)){
            opt.setLossyQuality(quality);
        }
        
        return opt;
    }
    
    private BufferedImage readImageFile(String inputFile) throws IOException{
        BufferedImage image = null;
        
        try
        {
            image = ImageIO.read(new File(inputFile));
        }
        catch(IOException e)
        {
            throw new IOException("Error reading input file!", e);
        }

        return image;
    }
    
    public static void main(String[] args)
    {
        Encoder encoder = new Encoder();
        String[] argss = new String[]{"-help", "lossless", "image_gray.bmp", ""};
        
        try{
            Options opt = encoder.parseCommandLine(argss);
            BufferedImage image = encoder.readImageFile(opt.inputFile);
        
            if(opt.compressionType.equals(CompressionType.LOSSLESS)){
                LosslessEncoder llessEncoder = new LosslessEncoder(opt.inputFile, opt.outputDir);
                llessEncoder.encode(image);
            }else{
                LossyEncoder lyEncoder = new LossyEncoder();
                lyEncoder.encode();
            }
        }catch(IllegalArgumentException | IOException e){
            System.err.println(e.getMessage());
        }
    }  
}