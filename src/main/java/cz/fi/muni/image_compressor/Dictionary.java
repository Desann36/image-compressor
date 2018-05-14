/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.image_compressor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DL
 */
public class Dictionary {
    private Map<List<Byte>, Integer> dictionary;
    private int codeLength;
    
    public Dictionary(){
        this.initDictionary();
        this.codeLength = 8;
    }
    
    public int getValue(List<Byte> key){
        return dictionary.get(key);
    }
    
    public int getCodeLength(){
        return this.codeLength;
    }
    
    public boolean containsKey(List<Byte> key){
        return dictionary.containsKey(key);
    }
    
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
