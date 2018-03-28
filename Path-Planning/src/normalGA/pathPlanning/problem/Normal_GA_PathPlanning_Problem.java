package normalGA.pathPlanning.problem;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import pathPlanning.problem.PathPlanning_Problem;
import pathPlanning.solution.PathPlanning_SolutionType;

public class Normal_GA_PathPlanning_Problem extends PathPlanning_Problem {	
	public Normal_GA_PathPlanning_Problem(String fileName) {
		super(fileName);
		getMapInfo(fileName);
		
		numberOfVariables_ = 10;
		numberOfObjectives_ = 1;
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

	public void evaluate(Solution solution) throws JMException {
		double wd = 1.0, ws = 1.0, wk = 1.0, C = 10.0;
		double wl = 1.0, wc = 1.0;
		
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
		
		if(checkPathFeasible(x, y) == true) {
			pathSafety = getPathSafety(gen, x, y);
			pathAngle = getPathAngle(gen, x, y);
			
			f = 10 * C + wk * pathAngle +  (1 / (wd * pathLength + ws * pathSafety));
		}
		else {
			pathInfeasiblePercent = getPathInfeasiblePercent(x, y, pathLength);		
			f = 1 / (wd * pathLength + wl * pathInfeasiblePercent[0] + wc * pathInfeasiblePercent[1]);
		}
		solution.setFitness(f);
	} 
}
