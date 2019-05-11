
package sortingvisualization.algorithms;

import java.util.List;
import javafx.animation.Animation;

/**
 * Interface for algorithm classes
 * @author Mykhailo Klunko
 */
public interface AbstractAlgorithm {
    
    /**
     * Create sorting visualization
     * @return list of animations
     */
    public List<Animation> sort();
    
}
