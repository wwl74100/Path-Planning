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
		
		numberOfVariables_ = mapRow_ + mapColumn_;
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
		
	} 
}
