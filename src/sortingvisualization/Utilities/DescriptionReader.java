/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import sortingvisualization.Enums.Algorithm;

/**
 *
 * @author Mykhailo Klunko
 */
public class DescriptionReader {
    
    private static String path = "/res/desc/";
    
    public static String readDescription(Algorithm algInstance) {
        String currentfile = System.getProperty("user.dir") + path + algInstance.getName().replaceAll("\\s","") + ".txt";
        String content = "";
        
        try {
            content = new String(Files.readAllBytes(Paths.get(currentfile)));
        } catch(Exception e) {
            content = "Description not found :'(";
        }
        
        return content;
    }
}
