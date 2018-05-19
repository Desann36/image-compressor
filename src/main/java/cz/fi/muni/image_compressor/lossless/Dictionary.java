package cz.fi.muni.image_compressor.lossless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of dictionary designed for LZW encoding and decoding. The keys 
 * of this dictionary are lists of values of image pixel intensities (0 - 255), 
 * the values are corresponding bit codes of specified length.
 */
public class Dictionary {
    private Map<List<Byte>, Integer> dictionary;
    private int codeLength;
    
    /**
     * Creates a new instance of Dictionary with an initial code length of 
     * eight.
     */
    public Dictionary(){
        this.initDictionary();
        this.codeLength = 8;
    }
    
    /**
     * Returns the value to which the specified key is mapped, or null if this 
     * dictionary contains no mapping for the key.
     * 
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this 
     * dictionary contains no mapping for the key
     */
    public int getValue(List<Byte> key){
        return dictionary.get(key);
    }
    
    /**
     * Returns the bit length of values in this dictionary.
     * 
     * @return the bit length of values in this dictionary
     */
    public int getCodeLength(){
        return this.codeLength;
    }
    
    /**
     * Returns true if this dictionary contains a mapping for the specified key.
     * 
     * @param key the key whose presence in this dictionary is to be tested
     * @return true if this dictionary contains a mapping for the specified key
     */
    public boolean containsKey(List<Byte> key){
        return dictionary.containsKey(key);
    }
    
    /**
     * Associates the authomatically generated value (bit code) with the 
     * specified key.
     * 
     * @param newKey key with which the generated value is to be associated
     */
    public void addKeyToDictionary(List<Byte> newKey){
        if(!dictionary.containsKey(newKey)) {
            if(dictionary.size() == Math.pow(2, this.codeLength)){
                this.codeLength++;
            }
            dictionary.put(newKey, dictionary.size());
        }
    }
    
    private void initDictionary() {
        this.dictionary = new HashMap<>();
        
        for(int i = 0; i < 256; i++)
        {
            List<Byte> list = new ArrayList<>();
            list.add((byte) i);
            dictionary.put(list, i);
        }
    }
}
