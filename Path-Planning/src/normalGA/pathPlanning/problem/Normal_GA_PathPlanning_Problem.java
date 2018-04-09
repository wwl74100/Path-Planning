package normalGA.pathPlanning.problem;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import pathPlanning.problem.PathPlanning_Problem;
import pathPlanning.solution.PathPlanning_SolutionType;

/**
 * 
 * @author X.K.T
 * @class Normal_GA_PathPlanning_Problem
 * @brief Class representing the Path-Planning problem for Normal GA
 *
 */
public class Normal_GA_PathPlanning_Problem extends PathPlanning_Problem {	
	/**
	 * @brief Constructor
	 * @param fileName: Map file name
	 */
	public Normal_GA_PathPlanning_Problem(String fileName) {
		super(fileName);
		getMapInfo(fileName);
		
		numberOfVariables_ = 10;
		numberOfObjectives_ = 5;
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
		double wd = 50, ws = 0, wk = 0, C = 5;
		//double wd = 50, ws = 10, wk = 5, C = 5;
		double wl = 5.0, wc = 50.0;
		
		Variable[] gen = solution.getDecisionVariables();
		
		double[] x = new double[numberOfVariables_];
		double[] y = new double[numberOfVariables_];
		for(int i = 0; i < numberOfVariables_; ++ i) {
			y[i] = Math.floor(gen[i].getValue() / mapColumn_);
			x[i] = gen[i].getValue() % mapColumn_;
		}
		
		double f = 0;
		double pathLength = getPathLength(x, y);
		double pathSafety, pathAngle;
		double[] pathInfeasiblePercent;
		
		pathSafety = getPathSafety(gen, x, y);
		pathAngle = getPathAngle(gen, x, y);
		pathInfeasiblePercent = getPathInfeasiblePercent(x, y, pathLength);
		
		if(checkPathFeasible(x, y) == true) {	
			//f = C +  (1 / (wd * pathLength + ws * pathSafety + wk * pathAngle));
			f = C +  wd / pathLength + ws / pathSafety + wk / pathAngle;
		}
		else {
			//f = 1 / (wd * pathLength + wl * pathInfeasiblePercent[0] + wc * pathInfeasiblePercent[1]);
			f = wd / pathLength + 1 / (wl * pathInfeasiblePercent[0] + wc * pathInfeasiblePercent[1]);
		}
		solution.setFitness(f);
		
		solution.setObjective(0, pathLength);
		solution.setObjective(1, pathSafety);
		solution.setObjective(2, pathAngle);
		solution.setObjective(3, pathInfeasiblePercent[0]);
		solution.setObjective(4, pathInfeasiblePercent[1]);
	} 
}
