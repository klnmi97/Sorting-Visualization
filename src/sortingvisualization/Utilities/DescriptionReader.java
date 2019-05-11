
package sortingvisualization.Utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import sortingvisualization.Enums.Algorithm;

/**
 * Reader of *.txt description files from the resource folder based on the
 * algorithm type
 * @author Mykhailo Klunko
 */
public class DescriptionReader {
    
    private static String path = "desc/";
    
    /**
     * Read description for the exact algorithm type if exists
     * @param algInstance type of the algorithm
     * @return String with description text
     */
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
