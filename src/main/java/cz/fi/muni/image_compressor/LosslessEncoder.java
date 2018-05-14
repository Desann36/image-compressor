/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.image_compressor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DL
 */
public class LosslessEncoder {
    private String outputDir;
    
    public LosslessEncoder(String inputFile, String outputDir){
        this.outputDir = outputDir;
    }
    
    public void encode(BufferedImage image) {
        byte[] values = this.makeArray(image);
        Dictionary dctr = new Dictionary();
        
        try (ByteArrayOutputStream dataStream = new ByteArrayOutputStream()) {
            String remainingBits = "";
            List<Byte> prev = new ArrayList<>();
            int bitLength = 0;
            int i = 0;
            while(i < values.length){
                List<Byte> curr = this.getNextSymbolsToEncode(dctr, values, i);
                
                bitLength += dctr.getCodeLength();
                
                String bits = this.getSymbolsInBits(dctr, curr, remainingBits);
                remainingBits = this.writeBitsByBytes(bits, dataStream);
                
                prev.add(values[i]);
                dctr.addKeyToDictionary(prev);
                
                i += curr.size();
                prev = new ArrayList<>(curr);
            }   
            
            this.writeLastByte(remainingBits, dataStream);
            
            try(FileOutputStream outStream = new FileOutputStream(outputDir + "encoded_image.sav")) {
                this.writeBitLength(bitLength, outStream);
                dataStream.writeTo(outStream);
            } catch (IOException ex) {
                Logger.getLogger(LosslessEncoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LosslessEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeBitLength(int bitLength, FileOutputStream outStream) throws IOException{
        for(int j = 3; j >= 0; j--){
            outStream.write(bitLength >> (j * 8));
        }
    }
    
    private String getSymbolsInBits(Dictionary dctr, List<Byte> curr, String bitBuffer){
        return bitBuffer + String.format("%" + dctr.getCodeLength() + "s", 
            Integer.toBinaryString(dctr.getValue(curr))).replace(' ', '0');
    }
    
    private List<Byte> getNextSymbolsToEncode(Dictionary dctr, byte[] values, int i){
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
    
    private String writeBitsByBytes(String bits, ByteArrayOutputStream out) throws IOException{
        int byteNumber = bits.length() / 8;

        for(int k = 0; k < byteNumber; k++){
            int beginIndex = k * 8;
            int endIndex = (k + 1) * 8;
            String oneByte = bits.substring(beginIndex, endIndex);
            out.write(Integer.parseInt(oneByte, 2));
        }
            
        return bits.substring(8 * byteNumber, bits.length());
    }
    
    private void writeLastByte(String bitBuffer, ByteArrayOutputStream out) throws IOException{
        if(!bitBuffer.equals("")){
            byte lastByte = Byte.parseByte(bitBuffer, 2);
            int shift = 8 - bitBuffer.length();
            lastByte <<= shift;
            out.write(lastByte);
        }
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
