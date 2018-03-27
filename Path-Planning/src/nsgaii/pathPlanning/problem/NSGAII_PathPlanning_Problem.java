// NSGAII_PathPlanning_Problem.java

package nsgaii.pathPlanning.problem;

import dataHandler.DataHandler;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import pathPlanning.problem.PathPlanning_Problem;
import pathPlanning.solution.PathPlanning_SolutionType;

/**
 * Class representing the Path-Planning problem for NSGA-II
 * @author X.K.T
 *
 */
public class NSGAII_PathPlanning_Problem extends PathPlanning_Problem {
	public NSGAII_PathPlanning_Problem(String fileName) {
		super(fileName);
		
		numberOfVariables_ = 10;
		//numberOfVariables_ = 3;
		numberOfObjectives_ = 3;
		numberOfConstraints_ = 0;
		problemName_ = "Path Planning";
		
		upperLimit_ = new double[numberOfVariables_];
	    lowerLimit_ = new double[numberOfVariables_];

	    // Establishes upper and lower limits for the variables
	    for (int var = 0; var < numberOfVariables_; var++) {
	      lowerLimit_[var] = 0.0;
	      upperLimit_[var] = mapSize_ - 1;
	    }
	    
	    // Solution type is ArrayInt
	    solutionType_ = new PathPlanning_SolutionType(this);
	}
	
	/** 
	 * Evaluates a solution.
	 * @param solution The solution to evaluate.
	 * @throws JMException 
	 */
	public void evaluate(Solution solution) throws JMException {
		Variable[] gen = solution.getDecisionVariables();
		
		double[] x = new double[numberOfVariables_];
		double[] y = new double[numberOfVariables_];
		for(int i = 0; i < numberOfVariables_; ++ i) {
			y[i] = Math.floor(gen[i].getValue() / mapColumn_);
			x[i] = gen[i].getValue() % mapColumn_;
		}
		
		double[] f = new double[numberOfObjectives_];
		
		f[0] = getPathSafety(gen, x, y);
		f[1] = getPathLength(x, y);
		f[2] = getPathAngle(gen, x, y);
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
		solution.setObjective(2, f[2]);
	} 
}