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
 * 
 * @author X.K.T
 * @class NSGAII_PathPlanning_Problem
 * @brief Class representing the Path-Planning problem for NSGA-II
 *
 */
public class NSGAII_PathPlanning_Problem extends PathPlanning_Problem {
	/**
	 * @brief Constructor
	 * @param fileName: Map file name
	 */
	public NSGAII_PathPlanning_Problem(String fileName) {
		super(fileName);
		
		numberOfVariables_ = 10;
		
		//numberOfObjectives_ = 3;
		numberOfObjectives_ = 2;
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
	 * @brief Evaluates a solution.
	 * @param solution: The solution to evaluate.
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
		
		double pathLength = getPathLength(x, y);
		double pathSafety, pathAngle;
		double[] pathInfeasiblePercent;
		
		pathSafety = getPathSafety(gen, x, y);
		pathAngle = getPathAngle(gen, x, y);
		pathInfeasiblePercent = getPathInfeasiblePercent(x, y, pathLength);
		
		/**
		 * three objective: (1)length
		 * 					(2)safety
		 * 					(3)angle
		 */
		/*f[0] = pathLength;
		f[1] = pathSafety;
		f[2] = pathAngle;
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
		solution.setObjective(2, f[2]);*/
		
		/**
		 * three objective: (1)length
		 * 					(2)infeasible segment percent & infeasible length percent
		 * 					(3)angle
		 */
		/*f[0] = pathLength;
		f[1] = 10 * pathInfeasiblePercent[0] + 100 * pathInfeasiblePercent[1];
		f[2] = pathAngle;
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
		solution.setObjective(2, f[2]);*/
		
		/**
		 * double objective: (1)length
		 * 					 (2)feasible: safety & angle
		 * 						infeasible: infeasible segment percent & infeasible length percent
		 */
		/*f[0] = pathLength;
		double ws = 5.0; //weight of path safety
		double wa = 5.0; //weight of path angle
		double wps = 100.0;//weight of infeasible segment percent
		double wpl = 100.0;//weight of infeasible length percent
		if(checkPathFeasible(x, y) == true) {
			f[1] = ws * pathSafety + wa * pathAngle;
		}
		else {
			f[1] = wps * pathInfeasiblePercent[0] + wpl * pathInfeasiblePercent[1];
		}
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);*/
		
		/**
		 * double objective: (1) length & angle
		 * 					 (2) infeasible segment percent & infeasible length percent
		 */
		//f[0] = 4 * pathLength + pathAngle;
		f[0] = pathLength;
		f[1] = 10 * pathInfeasiblePercent[0] + 100 * pathInfeasiblePercent[1];
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
		solution.setFitness(pathLength);
	} 
}