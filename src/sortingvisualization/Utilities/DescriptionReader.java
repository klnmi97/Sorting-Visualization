/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sortingvisualization.Utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import sortingvisualization.Enums.Algorithm;

/**
 *
 * @author Mykhailo Klunko
 */
public class DescriptionReader {
    
    private static String path = "desc/";
    
    public String readDescription(Algorithm algInstance) {
        String currentfile = path + algInstance.getName().replaceAll("\\s","") + ".txt";
        String content = "";
        
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(currentfile);
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br
              = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }
            content = resultStringBuilder.toString();
        } catch(Exception e) {
            content = "Sorry, description was not found :(";
        }
        
        return content;
    }
}
