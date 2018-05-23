package cz.fi.muni.image_compressor.compression;

import cz.fi.muni.image_compressor.utils.EncodeWriter;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
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
     * @throws java.io.IOException if an error occurs during writing in the
     * output file
     */
    public void encode(BufferedImage image, String outputFileName) throws IOException {
        byte[] values = makeArray(image);
        Dictionary dictionary = new Dictionary();
        
        try (ByteArrayOutputStream dataStream = new ByteArrayOutputStream()) {
            String remainingBitsOfCode = "";
            List<Byte> prevPixelsToEncode = new ArrayList<>();
            int i = 0;
            
            while(i < values.length){
                List<Byte> pixelsToEncode = getNextPixelsToEncode(dictionary, values, i);
                
                String code = getCodeOfPixels(dictionary, pixelsToEncode, remainingBitsOfCode);
                remainingBitsOfCode = EncodeWriter.writeBitsByBytes(code, dataStream);
                
                prevPixelsToEncode.add(values[i]);
                dictionary.addNewPixels(prevPixelsToEncode);
                
                i += pixelsToEncode.size();
                
                prevPixelsToEncode = new ArrayList<>(pixelsToEncode);
                
                if(dictionary.getSize() == Math.pow(2, Dictionary.MAX_LENGTH)){
                    dictionary.flushDictionary();
                    prevPixelsToEncode = new ArrayList<>();
                }
            }   
            
            EncodeWriter.writeLastByte(remainingBitsOfCode, dataStream);
            this.createOutputFile(dataStream, image.getWidth(), image.getHeight(), outputFileName);
        } catch (IOException e) {
            throw new IOException("Error encoding the image!", e);
        }
    }
    
    private void createOutputFile(ByteArrayOutputStream dataStream, int width, int height, String outputFileName) 
            throws IOException{
        String outputFile = outputDir + "\\" + outputFileName + ".enc";
        
        try(FileOutputStream outStream = new FileOutputStream(outputFile)) {
            EncodeWriter.writeImageSize(width, height, outStream);
            dataStream.writeTo(outStream);
        } catch (IOException e) {
            throw new IOException("Error creating the output file!", e);
        }
    }
    
    private String getCodeOfPixels(Dictionary dictionary, List<Byte> curr, String bitBuffer){
        int i = dictionary.getCode(curr);
        return bitBuffer + String.format("%" + dictionary.getCodeLength() + "s", 
            Integer.toBinaryString(dictionary.getCode(curr))).replace(' ', '0');
    }
    
    //get the longest possible sequence of pixels in dictionary
    private List<Byte> getNextPixelsToEncode(Dictionary dictionary, byte[] values, int i){
        List<Byte> curr = new ArrayList<>();
        List<Byte> aux = new ArrayList<>();
        curr.add(values[i]);
        aux.add(values[i]);
        
        while(dictionary.containsPixels(aux) && (++i < values.length)) { 
            curr = new ArrayList<>(aux);
            aux.add(values[i]);
        }
        
        return curr;
    }
    
    private byte[] makeArray(BufferedImage image){
        int height = image.getHeight();
        int width = image.getWidth();
        
        byte[] values = new byte[height * width];
        Raster raster = image.getData();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();

        for(int i = 0; i < height * width; i++) {
            values[i] = (byte) dataBuffer.getElem(i);
        }
        
        return values;
    }
}
