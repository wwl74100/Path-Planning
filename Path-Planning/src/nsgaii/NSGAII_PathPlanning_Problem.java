// NSGAII_PathPlanning_Problem.java

package nsgaii;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayIntSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XInt;

/**
 * Class representing the Path-Planning problem for NSGA-II
 * @author X.K.T
 *
 */
public class NSGAII_PathPlanning_Problem extends Problem {
	
	/**
	 * Constructor
	 * @param numberOfVariables: Number of variables.
	 */	
	public NSGAII_PathPlanning_Problem(Integer numberOfVariables) {
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 3;
		numberOfConstraints_ = 0;
		problemName_ = "Path Planning";
		
		upperLimit_ = new double[numberOfVariables_];
	    lowerLimit_ = new double[numberOfVariables_];

	    // Establishes upper and lower limits for the variables
	    for (int var = 0; var < numberOfVariables_; var++) {
	      lowerLimit_[var] = 0.0;
	      upperLimit_[var] = 1.0;
	    }
	    
	    // Solution type is ArrayInt
	    solutionType_ = new ArrayIntSolutionType(this);
	}
	
	/** 
	 * Evaluates a solution.
	 * @param solution The solution to evaluate.
	 * @throws JMException 
	 */
	public void evaluate(Solution solution) throws JMException {
		XInt x = new XInt(solution);
		double[] f = new double[numberOfObjectives_];
		f[0] = getPathLength(x);
		f[1] = getPathAngle(x);
		f[2] = getPathSafety(x);
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
		solution.setObjective(2, f[2]);
	} 
	
	private double getPathLength(XInt xInt) {
		return 0;
	}
	
	private double getPathAngle(XInt xInt) {
		return 0;
	}
	
	private double getPathSafety(XInt xInt) {
		return 0;
	}
}
