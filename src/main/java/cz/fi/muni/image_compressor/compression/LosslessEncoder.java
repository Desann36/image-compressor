package cz.fi.muni.image_compressor.compression;

import cz.fi.muni.image_compressor.utils.EncodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class performing lossless encoding of images using LZW compression algorithm.
 */
public class LosslessEncoder {
    private Path outputDir;

    /**
     * Constructs a new instance of LosslessEncoder.
     * 
     * @param outputDir directory for output file
     */
    public LosslessEncoder(Path outputDir) {
        this.outputDir = outputDir;
    }
    
    /**
     * Performs lossless encoding of the specified image using LZW compression 
     * algorithm. The output of this encoder is stored in file with specified
     * name with the extension .enc. 
     * 
     * @param image          image to be encoded
     * @param outputFileName name of the output file
     */
    public void encode(BufferedImage image, String outputFileName) throws IOException {
        byte[] values = makeArray(image);
        Dictionary dictionary = new Dictionary();
        
        try (ByteArrayOutputStream dataStream = new ByteArrayOutputStream()) {
            String remainingBits = "";
            List<Byte> prev = new ArrayList<>();
            int bitLength = 0;
            int i = 0;
            
            while(i < values.length){
                List<Byte> curr = getNextSymbolsToEncode(dictionary, values, i);
                
                bitLength += dictionary.getCodeLength();
                
                String bits = getSymbolsInBits(dictionary, curr, remainingBits);
                remainingBits = EncodeWriter.writeBitsByBytes(bits, dataStream);
                
                prev.add(values[i]);
                dictionary.addKeyToDictionary(prev);
                
                i += curr.size();
                prev = new ArrayList<>(curr);
            }   
            
            EncodeWriter.writeLastByte(remainingBits, dataStream);
            this.createOutputFile(dataStream, image.getWidth(), image.getHeight(), bitLength, outputFileName);
        } catch (IOException e) {
            throw new IOException("Error encoding the image!", e);
        }
    }
    
    private void createOutputFile(ByteArrayOutputStream dataStream, int width, int height, int bitLength, 
            String outputFileName) throws IOException{
        try(FileOutputStream outStream = new FileOutputStream(outputDir + outputFileName + ".enc")) {
            EncodeWriter.writeImageSize(width, height, outStream);
            EncodeWriter.writeBitLength(bitLength, outStream);
            dataStream.writeTo(outStream);
        } catch (IOException e) {
            throw new IOException("Error creating the output file!", e);
        }
    }
    
    private String getSymbolsInBits(Dictionary dictionary, List<Byte> curr, String bitBuffer){
        return bitBuffer + String.format("%" + dictionary.getCodeLength() + "s", 
            Integer.toBinaryString(dictionary.getValue(curr))).replace(' ', '0');
    }
    
    private List<Byte> getNextSymbolsToEncode(Dictionary dictionary, byte[] values, int i){
        List<Byte> curr = new ArrayList<>();
        List<Byte> aux = new ArrayList<>();
        curr.add(values[i]);
        aux.add(values[i]);
        
        while(dictionary.containsKey(aux) && (++i < values.length)) { 
            curr = new ArrayList<>(aux);
            aux.add(values[i]);
        }
        
        return curr;
    }
    
    private byte[] makeArray(BufferedImage image){
        int height = image.getHeight();
        int width = image.getWidth();
        int index = 0;
        
        byte[] values = new byte[height * width];
        
        for(int row = 0; row < height; row++) {
            for(int column = 0; column < width; column++) {
                values[index++] = (byte) (image.getRGB(column, row) & 0xFF);
            }
        }
        
        return values;
    }
}
