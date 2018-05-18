package cz.fi.muni.image_compressor.utils;

import cz.fi.muni.image_compressor.common.CompressionType;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class EncodeWriter {
    
    public static void writeCompressionType(CompressionType type, FileOutputStream outStream) throws IOException{
        if(type.equals(CompressionType.LOSSLESS)){
            outStream.write(0);
        }else{
            outStream.write(1);
        }
    }
    
    public static void writeImageSize(int width, int height, FileOutputStream outStream) throws IOException{
        writeInteger(width, outStream);
        writeInteger(height, outStream);
    }
    
    public static void writeBitLength(int bitLength, FileOutputStream outStream) throws IOException{
        writeInteger(bitLength, outStream);
    }
    
    public static void writeInteger(int value, FileOutputStream outStream) throws IOException{
        for(int j = 3; j >= 0; j--){
            outStream.write(value >> (j * 8));
        }
    }
    
    public static String writeBitsByBytes(String bits, ByteArrayOutputStream out) throws IOException{
        int byteNumber = bits.length() / 8;

        for(int k = 0; k < byteNumber; k++){
            int beginIndex = k * 8;
            int endIndex = (k + 1) * 8;
            String oneByte = bits.substring(beginIndex, endIndex);
            out.write(Integer.parseInt(oneByte, 2));
        }
            
        return bits.substring(8 * byteNumber, bits.length());
    }
    
    public static void writeLastByte(String bitBuffer, ByteArrayOutputStream out) throws IOException{
        if(!bitBuffer.equals("")){
            byte lastByte = Byte.parseByte(bitBuffer, 2);
            int shift = 8 - bitBuffer.length();
            lastByte <<= shift;
            out.write(lastByte);
        }
    }
}
