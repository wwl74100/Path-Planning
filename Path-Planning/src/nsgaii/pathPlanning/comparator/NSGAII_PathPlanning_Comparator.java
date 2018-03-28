package nsgaii.pathPlanning.comparator;

import java.util.Comparator;

import jmetal.core.Solution;
import jmetal.util.comparators.IConstraintViolationComparator;
import jmetal.util.comparators.OverallConstraintViolationComparator;

public class NSGAII_PathPlanning_Comparator implements Comparator {
	IConstraintViolationComparator violationConstraintComparator_ ;
	 
	  /**
	   * Constructor
	   */
	  public NSGAII_PathPlanning_Comparator() {
	    violationConstraintComparator_ = new OverallConstraintViolationComparator(); 
	    //violationConstraintComparator_ = new NumberOfViolatedConstraintComparator(); 
	  }

	  /**
	   * Constructor
	   * @param comparator
	   */
	  public NSGAII_PathPlanning_Comparator(IConstraintViolationComparator comparator) {
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

	    int dominate1 ; // dominate1 indicates if some objective of solution1 
	                    // dominates the same objective in solution2. dominate2
	    int dominate2 ; // is the complementary of dominate1.

	    dominate1 = 0 ; 
	    dominate2 = 0 ;
	    
	    int flag; //stores the result of the comparison

	    // Test to determine whether at least a solution violates some constraint
	    if (violationConstraintComparator_.needToCompare(solution1, solution2))
	      return violationConstraintComparator_.compare(solution1, solution2) ;
	    /*
	    if (solution1.getOverallConstraintViolation()!= 
	        solution2.getOverallConstraintViolation() &&
	       (solution1.getOverallConstraintViolation() < 0) ||         
	       (solution2.getOverallConstraintViolation() < 0)){            
	      return (overallConstraintViolationComparator_.compare(solution1,solution2));
	    }
	   */
	    
	    // Equal number of violated constraints. Applying a dominance Test then
	    double[] value1 = new double[solution1.getNumberOfObjectives()];
	    double[] value2 = new double[solution1.getNumberOfObjectives()];
	    for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
	      value1[i] = solution1.getObjective(i);
	      value2[i] = solution2.getObjective(i);
	    }
	    
	    //if path 1 is not feasible
	    if(value1[0] > Double.MAX_VALUE / 2) {
	    	//if path 2 is not feasible
	    	if(value2[0] > Double.MAX_VALUE / 2) {
	    		return 0;
	    	}
	    	//if path 2 if feasible
	    	else {
	    		return 1;
	    	}
	    }
	    //if path 1 is feasible
	    else {
	    	//if path 2 is not feasible
	    	if(value2[0] > Double.MAX_VALUE / 2) {
	    		return -1;
	    	}
	    	//if path 2 is feasible
	    	else {
	    		//if length 1 < length 2
	    		if(value1[1] < value2[1]) {
	    			return -1;
	    		}
	    		//if length 1 > length 2
	    		else if(value1[1] > value2[1]) {
	    			return 1;
	    		}
	    		
	    		// angle first
	    		//if length 1 = length 2
	    		else {
	    			//if safety 1 < safety 2
	    			if(value1[2] < value2[2]) {
		    			return -1;
		    		}
		    		//if safety 1 > safety 2
		    		else if(value1[2] > value2[2]) {
		    			return 1;
		    		}
	    			//if safety 1 = safety 2
		    		else {
		    			//if angle 1 < angle 2
		    			if(value1[0] < value2[0]) {
			    			return -1;
			    		}
			    		//if angle 1 > angle 2
			    		else if(value1[0] > value2[0]) {
			    			return 1;
			    		}
		    			//if angle 1 = angle 2
			    		else {
			    			return 0;
			    		}
		    		}
	    		}
	    	}
	    }
	}
}
