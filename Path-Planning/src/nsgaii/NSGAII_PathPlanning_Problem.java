// NSGAII_PathPlanning_Problem.java

package nsgaii;

import dataHandler.DataHandler;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;

/**
 * Class representing the Path-Planning problem for NSGA-II
 * @author X.K.T
 *
 */
public class NSGAII_PathPlanning_Problem extends Problem {
	private int mapSize_;         // map size
	private int mapRow_;          // map row number
	private int mapColumn_;       // map column number
	private int mapStartPoint_;   // map start point
	private int mapTargetPoint_;  // map target point
	private char[] mapVector_;    // map info vector
	private char[][] mapMatrix_;  // map info matrix
	
	/**
	 * get the map file info 
	 * @param fileName: the name of the map file
	 */
	private void getMapInfo(String fileName) {
		DataHandler dataHandler = new DataHandler(fileName);
		mapSize_        = dataHandler.getMapSize();
		mapRow_         = dataHandler.getMapRow();
		mapColumn_      = dataHandler.getMapColumn();
		mapStartPoint_  = dataHandler.getStartPoint();
		mapTargetPoint_ = dataHandler.getTargetPoint();
		mapVector_      = dataHandler.getMapVector();
		mapMatrix_      = dataHandler.getMapMatrix();
	}
	
	/**
	 * Constructor
	 * @param numberOfVariables: Number of variables.
	 */	
	public NSGAII_PathPlanning_Problem(String fileName) {
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
	    solutionType_ = new NSGAII_PathPlanning_SolutionType(this);
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
			x[i] = Math.floor(gen[i].getValue() / mapColumn_);
			y[i] = gen[i].getValue() % mapColumn_;
		}
		
		double[] f = new double[numberOfObjectives_];
		f[0] = getPathLength(x, y);
		f[1] = getPathAngle(x, y);
		f[2] = getPathSafety(x, y);
		
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
		solution.setObjective(2, f[2]);
	} 
	
	private double getPathLength(double[] x, double[] y) throws JMException {		
		double distance = 0.0;
		
		for(int i = 1; i < numberOfVariables_; ++ i) {
			distance += Math.sqrt((x[i] - x[i - 1]) * (x[i] - x[i - 1])
						+ (y[i] - y[i - 1]) * (y[i] - y[i - 1]));
		}
		
		return distance;
	}
	
	private double getPathAngle(double[] x, double[] y) {
		return 0;
	}
	
	private double getPathSafety(double[] x, double[] y) {
		return 0;
	}

	public int getMapStartPoint() {
		return mapStartPoint_;
	}

	public int getMapTargetPoint() {
		return mapTargetPoint_;
	}
}
