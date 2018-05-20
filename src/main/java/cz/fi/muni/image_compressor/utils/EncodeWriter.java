package cz.fi.muni.image_compressor.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class containing methods helping the encoder to construct the output file.
 */
public class EncodeWriter {
    
    /**
     * Writes the image size (width and height) into output stream.
     * 
     * @param width     inamge width
     * @param height    image height
     * @param outStream output stream
     * @throws IOException if an error occurs during writing
     */
    public static void writeImageSize(int width, int height, FileOutputStream outStream) throws IOException{
        writeInteger(width, outStream);
        writeInteger(height, outStream);
    }
    
    /**
     * Writes the bit length of encoded image into output stream.
     * 
     * @param bitLength bit length of encoded image
     * @param outStream output stream
     * @throws IOException if an error occurs during writing
     */
    public static void writeBitLength(int bitLength, FileOutputStream outStream) throws IOException{
        writeInteger(bitLength, outStream);
    }
    
    /**
     * Writes an integer (four bytes) into output stream.
     * 
     * @param value     value to be writted
     * @param outStream output stream
     * @throws IOException if an error occurs during writing
     */
    public static void writeInteger(int value, FileOutputStream outStream) throws IOException{
        for(int j = 3; j >= 0; j--){
            outStream.write(value >> (j * 8));
        }
    }
    
    /**
     * Writes the chain of bits into data stream byte by byte. Returns the 
     * remaining bits which could not be writted as a whole byte.
     * 
     * @param bits chain of bits to be written
     * @param out  data stream
     * @return the remaining bits
     * @throws IOException if an error occurs during writing
     */
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
    
    /**
     * Writes the specified chain of bits as a whole byte. Performs insering 
     * zeros at the end of this chain if the length of it is less than eight.
     * 
     * @param bits bits to be written
     * @param out data stream
     * @throws IOException if an error occurs during writing
     */
    public static void writeLastByte(String bits, ByteArrayOutputStream out) throws IOException{
        if(!bits.equals("")){
            byte lastByte = Byte.parseByte(bits, 2);
            int shift = 8 - bits.length();
            lastByte <<= shift;
            out.write(lastByte);
        }
    }
}
