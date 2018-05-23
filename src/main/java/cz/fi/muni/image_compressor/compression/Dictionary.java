package cz.fi.muni.image_compressor.compression;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * Implementation of dictionary designed for LZW encoding and decoding. The 
 * values of this dictionary are lists of values of image pixel intensities 
 * (0 - 255), the keys are corresponding bit codes of specified length. 
 * 
 * Initially, the bit length of code is set 8 and the first 256 (key, value)
 * pairs are initialized to (0..255, List(0)..List(255)). The maximum bit length 
 * of codes is 16; at that point it is necessary to perform the flush of the 
 * dictionary in order to continue encoding/decoding.
 */
public class Dictionary {
    public static final int MAX_LENGTH = 16;
    private DualHashBidiMap<Integer, List<Byte>> dictionary;
    private int codeLength;
    
    /**
     * Creates a new instance of Dictionary with an initial code length of 
     * eight.
     */
    public Dictionary(){
        this.initDictionary();
    }
    
    
    /**
     * Empties the dictionary, sets the code length to 8 and initializes the 
     * first 256 key-value pairs.
     */
    public void flushDictionary(){
        this.initDictionary();
    }
    
    
    /**
     * Returns the size of this dictionary.
     * 
     * @return the size of this dictionary
     */
    public int getSize(){
        return this.dictionary.size();
    }
    
    /**
     * Returns the bit length of codes in this dictionary.
     * 
     * @return the bit length of codes in this dictionary
     */
    public int getCodeLength(){
        return this.codeLength;
    }
    
    /**
     * Returns the code to which the specified pixels are mapped, or null if 
     * this dictionary contains no mapping for the code.
     * 
     * @param pixels the pixels whose associated code is to be returned
     * @return the code to which the specified pixels are mapped, or null if 
     * this dictionary contains no mapping for the code
     */
    public int getCode(List<Byte> pixels){
        return dictionary.getKey(pixels);
    }
    
    /**
     * Returns the pixels to which the specified code is mapped, or null if 
     * this dictionary contains no mapping for the pixels.
     * 
     * @param code the code whose associated pixels are to be returned
     * @return the pixels to which the specified code is mapped, or null if 
     * this dictionary contains no mapping for the pixels
     */
    public List<Byte> getPixels(int code){
        return dictionary.get(code);
    }
    
    /**
     * Returns true if this dictionary contains a mapping for the specified 
     * pixels.
     * 
     * @param pixels the pixels whose presence in this dictionary is to be 
     * tested
     * @return true if this dictionary contains a mapping for the specified 
     * pixels
     */
    public boolean containsPixels(List<Byte> pixels){
        return dictionary.containsValue(pixels);
    }
    
    /**
     * Returns true if this dictionary contains a mapping for the specified 
     * code.
     * 
     * @param code the code whose presence in this dictionary is to be tested
     * @return true if this dictionary contains a mapping for the specified code
     */
    public boolean containsCode(int code){
        return dictionary.containsKey(code);
    }
    
    /**
     * Associates the authomatically generated code with the specified pixels.
     * 
     * @param pixels pixels with which the generated code is to be associated
     */
    public void addNewPixels(List<Byte> pixels){
        if(!dictionary.containsValue(pixels)) {
            if(dictionary.size() == Math.pow(2, MAX_LENGTH)){
                throw new IndexOutOfBoundsException("Dictionary overflow");
            }
            
            if(dictionary.size() == Math.pow(2, this.codeLength)){
                this.codeLength++;
            }
            
            dictionary.put(dictionary.size(), pixels);
        }
    }
    
    private void initDictionary() {
        this.codeLength = 8;
        this.dictionary = new DualHashBidiMap<>();
        
        for(int i = 0; i < 256; i++)
        {
            List<Byte> list = new ArrayList<>();
            list.add((byte) i);
            dictionary.put(i, list);
        }
    }
}
