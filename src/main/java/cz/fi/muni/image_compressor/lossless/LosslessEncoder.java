package cz.fi.muni.image_compressor.lossless;

import cz.fi.muni.image_compressor.common.CompressionType;
import cz.fi.muni.image_compressor.utils.EncodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class LosslessEncoder {
    
    public static void encode(BufferedImage image, Path outputDir) {
        byte[] values = makeArray(image);
        Dictionary dctr = new Dictionary();
        
        try (ByteArrayOutputStream dataStream = new ByteArrayOutputStream()) {
            String remainingBits = "";
            List<Byte> prev = new ArrayList<>();
            int bitLength = 0;
            int i = 0;
            while(i < values.length){
                List<Byte> curr = getNextSymbolsToEncode(dctr, values, i);
                
                bitLength += dctr.getCodeLength();
                
                String bits = getSymbolsInBits(dctr, curr, remainingBits);
                remainingBits = EncodeWriter.writeBitsByBytes(bits, dataStream);
                
                prev.add(values[i]);
                dctr.addKeyToDictionary(prev);
                
                i += curr.size();
                prev = new ArrayList<>(curr);
            }   
            
            EncodeWriter.writeLastByte(remainingBits, dataStream);
            
            try(FileOutputStream outStream = new FileOutputStream(outputDir + "encoded_image.enc")) {
                EncodeWriter.writeCompressionType(CompressionType.LOSSLESS, outStream);
                EncodeWriter.writeImageSize(image.getWidth(), image.getHeight(), outStream);
                EncodeWriter.writeBitLength(bitLength, outStream);
                dataStream.writeTo(outStream);
            } catch (IOException ex) {
                Logger.getLogger(LosslessEncoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LosslessEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String getSymbolsInBits(Dictionary dctr, List<Byte> curr, String bitBuffer){
        return bitBuffer + String.format("%" + dctr.getCodeLength() + "s", 
            Integer.toBinaryString(dctr.getValue(curr))).replace(' ', '0');
    }
    
    private static List<Byte> getNextSymbolsToEncode(Dictionary dctr, byte[] values, int i){
        List<Byte> curr = new ArrayList<>();
        List<Byte> aux = new ArrayList<>();
        curr.add(values[i]);
        aux.add(values[i]);
        
        while(dctr.containsKey(aux) && (++i < values.length)) { 
            curr = new ArrayList<>(aux);
            aux.add(values[i]);
        }
        
        return curr;
    }
    
    private static byte[] makeArray(BufferedImage image){
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
