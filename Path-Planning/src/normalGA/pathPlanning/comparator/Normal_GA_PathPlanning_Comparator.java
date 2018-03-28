package normalGA.pathPlanning.comparator;

import java.util.Comparator;
import jmetal.core.Solution;
import jmetal.util.comparators.IConstraintViolationComparator;
import jmetal.util.comparators.OverallConstraintViolationComparator;

public class Normal_GA_PathPlanning_Comparator implements Comparator {
	IConstraintViolationComparator violationConstraintComparator_ ;
	 
	  /**
	   * Constructor
	   */
	  public Normal_GA_PathPlanning_Comparator() {
	    violationConstraintComparator_ = new OverallConstraintViolationComparator(); 
	    //violationConstraintComparator_ = new NumberOfViolatedConstraintComparator(); 
	  }

	  /**
	   * Constructor
	   * @param comparator
	   */
	  public Normal_GA_PathPlanning_Comparator(IConstraintViolationComparator comparator) {
	    violationConstraintComparator_ = comparator ;
	  }
	 
	 /**
	  * Compares two solutions.
	  * @param object1 Object representing the first <code>Solution</code>.
	  * @param object2 Object representing the second <code>Solution</code>.
	  * @return -1, or 0, or 1 if solution1 dominates solution2, both are 
	  * non-dominated, or solution1  is dominated by solution22, respectively.
	  */
	  public int compare(Object object1, Object object2) {
	    if (object1==null)
	      return 1;
	    else if (object2 == null)
	      return -1;
	    
	    Solution solution1 = (Solution)object1;
	    Solution solution2 = (Solution)object2;

	    if(solution1.getFitness() > solution2.getFitness()) {
	    	return -1;
	    }
	    else if(solution1.getFitness() == solution2.getFitness()) {
	    	return 0;
	    }
	    else {
	    	return 1;
	    }
	}
}
