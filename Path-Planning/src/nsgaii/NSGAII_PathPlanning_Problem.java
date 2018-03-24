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
			y[i] = Math.floor(gen[i].getValue() / mapColumn_);
			x[i] = gen[i].getValue() % mapColumn_;
		}
		
		double[] f = new double[numberOfObjectives_];
		f[0] = getPathLength(x, y);
		f[1] = getPathAngle(gen, x, y);
		f[2] = getPathSafety(gen, x, y);
		
		solution.setObjective(0, f[2]);
		solution.setObjective(1, f[0]);
		solution.setObjective(2, f[1]);
	} 
	
	/**
	 * calculate the total length of a path
	 * @param x: x coordinate 
	 * @param y: y coordinate
	 * @return: path length
	 * @throws JMException
	 */
	private double getPathLength(double[] x, double[] y) throws JMException {		
		double distance = 0.0;
		
		for(int i = 1; i < numberOfVariables_; ++ i) {
			distance += Math.sqrt((x[i] - x[i - 1]) * (x[i] - x[i - 1])
						+ (y[i] - y[i - 1]) * (y[i] - y[i - 1]));
		}
		
		return distance;
	}
	
	/**
	 * calculate the total angle of a path
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * @return: path angle
	 */
	private double getPathAngle(Variable[] gen, double[] x, double[] y) {
		double pathAngle = 0.0, angle1, angle2;
		
		for(int i = 1; i < numberOfVariables_ - 1; ++ i) {
			//when the start parts of the segment are the same point
			while((i < numberOfVariables_ - 1) && (gen[i] == gen[i - 1])) {
				++ i;
			}
			if(i == numberOfVariables_ - 1) return pathAngle;
			angle1 = getAngleOfVector(x[i - 1] - x[i], y[i - 1] - y[i]);
			
			//when the last parts of the segment are the same point
			while((i < numberOfVariables_ - 1) && (gen[i] == gen[i + 1])) {
				++ i;
			}
			if(i == numberOfVariables_ - 1) return pathAngle;
			angle2 = getAngleOfVector(x[i + 1] - x[i], y[i + 1] - y[i]);
			
			pathAngle += Math.abs(Math.abs(angle1 - angle2) - Math.PI);
		}
		
		return pathAngle;
	}
	
	/**
	 * get the safety of a path
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * @return: path safety
	 */
	private double getPathSafety(Variable gen[], double[] x, double[] y) {
		double pathSafety = 0.0, safetyLast = 0, safetyThis = 0;
		int countLast = 0, countThis = 0;
		
		for(int i = 1; i < numberOfVariables_; ++ i) {
			countThis = 0;
			while((i < numberOfVariables_) && (gen[i] == gen[i - 1])) {
				++ countThis;
				++ i;
			}
			if(i == numberOfVariables_) {
				//if all the points of the path are the same 
				if(countThis == numberOfVariables_ - 2) {
					return Integer.MAX_VALUE;
				}
				return pathSafety;
			}
			
			if(checkSegInObstacle((int)x[i - 1], (int)y[i - 1], (int)x[i], (int)y[i]) == true) {
				return Double.MAX_VALUE;
			}
			safetyThis = getSafetyOfSeg((int)x[i - 1], (int)y[i - 1], (int)x[i], (int)y[i]);

			pathSafety += Math.min(safetyLast, safetyThis) * countLast + safetyThis;
			safetyLast = safetyThis;
			countLast = countThis;
		}
		
		return (double)mapSize_ / pathSafety;
	}
	
	/**
	 * get the angle between vector (x, y) and x-axis
	 * @param x: vector(x, y)
	 * @param y: vector(x, y)
	 * @return: vector angle 
	 */
	private double getAngleOfVector(double x, double y) {
		if(x == 0) {
			if(y == 0) return -1;
			else if(y > 0) return Math.PI * 0.5;
			else return Math.PI * 1.5;
		}
		else if(x > 0) {
			if(y == 0) return 0;
			else if(y > 0) return Math.atan(y / x);
			else return 2.0 * Math.PI + Math.atan(y / x);
		}
		else {
			if(y == 0) return Math.PI;
			else return Math.PI + Math.atan(y / x);
		}
	}
	
	/**
	 * get the safety of the segment (x1, y1) ~ (x2, y2)
	 * @param x1: start point x coordinate
	 * @param y1: start point y coordinate
	 * @param x2: end point x coordinate
	 * @param y2: end point y coordinate
	 * @return: segment safety
	 */
	public double getSafetyOfSeg(int x1, int y1, int x2, int y2) {
		int maxX = Math.max(x1, x2), minX = Math.min(x1, x2);
		int maxY = Math.max(y1, y2), minY = Math.min(y1, y2);
		double dx1 = x1 + 0.5, dx2 = x2 + 0.5, dy1 = y1 + 0.5, dy2 = y2 + 0.5;
		double k, b, segSafety = Double.MAX_VALUE;
		
		//if the x coordinates are the same
		if(minX == maxX) {
			//if the segment is not in an obstacle
			for(int i = 0; i < mapRow_; ++ i) {
				for(int j = 0; j < mapColumn_; ++ j) {
					if(mapMatrix_[i][j] == '1') {
						double temp;
						if((i <= maxY) && (i >= minY)) {
							temp = Math.abs(j - maxX);
						}
						else {
							temp = Math.sqrt(Math.min((j - x1) * (j - x1) + (i - y1) * (i - y1),
									 				  (j - x2) * (j - x2) + (i - y2) * (i - y2)));
						}
														
						segSafety = Math.min(segSafety, temp);
					}
				}
			}
			return segSafety;
		}
	
		//if the y coordinates are the same
		if(minY == maxY) {
			//if the segment is not in an obstacle
			for(int i = 0; i < mapRow_; ++ i) {
				for(int j = 0; j < mapColumn_; ++ j) {
					if(mapMatrix_[i][j] == '1') {
						double temp;
						if((j <= maxX) && (j >= minX)) {
							temp = Math.abs(i - maxY);
						}
						else {
							temp = Math.sqrt(Math.min((j - x1) * (j - x1) + (i - y1) * (i - y1),
									 				  (j - x2) * (j - x2) + (i - y2) * (i - y2)));
						}
														
						segSafety = Math.min(segSafety, temp);
					}
				}
			}
			return segSafety;
		}
		
		//if the x coordinates and the y coordinates are not the same
		k = (dy1 - dy2) / (dx1 - dx2);
		b = (dx1 * dy2 - dx2 * dy1) / (dx1 - dx2);
		
		double b1 = Math.max(dx1 / k + dy1, dx2 / k + dy2);
		double b2 = Math.min(dx1 / k + dy1, dx2 / k + dy2);
		for(int i = 0; i < mapRow_; ++ i) {
			for(int j = 0; j < mapColumn_; ++ j) {
				if(mapMatrix_[i][j] == '1') {
					double temp;
					if((((j + 0.5) / k + i + 0.5) < b2) || (((j + 0.5) / k + i + 0.5) > b1)) {
						temp = Math.sqrt(Math.min((j - x1) * (j - x1) + (i - y1) * (i - y1),
				 				  				  (j - x2) * (j - x2) + (i - y2) * (i - y2)));
					}
					else {
						temp = Math.abs(k * (j + 0.5) - (i + 0.5) + b) / Math.sqrt(k * k + 1);
					}
					
					segSafety = Math.min(segSafety, temp);
				}
			}
		}
		return segSafety;
	}
	
	public boolean checkSegInObstacle(int x1, int y1, int x2, int y2) {
		int maxX = Math.max(x1, x2), minX = Math.min(x1, x2);
		int maxY = Math.max(y1, y2), minY = Math.min(y1, y2);
		double dx1 = x1 + 0.5, dx2 = x2 + 0.5, dy1 = y1 + 0.5, dy2 = y2 + 0.5;
		double k, b;
		
		//if the x coordinates are the same
		if(minX == maxX) {
			//if the segment is in an obstacle
			for(int i = minY; i <= maxY; ++ i) {
				if(mapMatrix_[i][minX] == '1') return true;
			}
		}
	
		//if the y coordinates are the same
		if(minY == maxY) {
			//if the segment is in an obstacle
			for(int i = minX; i <= maxX; ++ i) {
				if(mapMatrix_[minY][i] == '1') return true;
			}
		}
		
		//if the x coordinates and the y coordinates are not the same
		k = (dy1 - dy2) / (dx1 - dx2);
		b = (dx1 * dy2 - dx2 * dy1) / (dx1 - dx2);
		for(int i = minX + 1; i <= maxX; ++ i) {
			double temp = k * i + b;
			if(temp == Math.floor(temp)) {
				temp = Math.floor(temp);
				if(k > 0) {
					if((mapMatrix_[(int)temp][i] == '1') 
							|| (mapMatrix_[(int)temp - 1][i - 1] == '1')) {
						return true;	
					}
				}
				else {
					if((mapMatrix_[(int)temp][i - 1] == '1') 
							|| (mapMatrix_[(int)temp - 1][i] == '1')) {
						return true;	
					}
				}
			}
			else {
				temp = Math.floor(temp);
				if((mapMatrix_[(int)temp][i] == '1') 
					|| (mapMatrix_[(int)temp][i - 1] == '1')) {
					return true;
				}
			}
		}
		
		for(int i = minY + 1; i <= maxY; ++ i) {
			double temp = (i - b) / k;
			if(temp == Math.floor(temp)) {
				temp = Math.floor(temp);
				if(k > 0) {
					if((mapMatrix_[i][(int)temp] == '1') 
							|| (mapMatrix_[i - 1][(int)temp - 1] == '1')) {
						return true;	
					}
				}
				else {
					if((mapMatrix_[i - 1][(int)temp] == '1') 
							|| (mapMatrix_[i][(int)temp - 1] == '1')) {
						return true;	
					}
				}
			}
			else {
				temp = Math.floor(temp);
				if((mapMatrix_[i][(int)temp] == '1') 
					|| (mapMatrix_[i - 1][(int)temp] == '1')) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getMapStartPoint() {
		return mapStartPoint_;
	}

	public int getMapTargetPoint() {
		return mapTargetPoint_;
	}
	
	public int getMapSize() {
		return mapSize_;
	}
	
	public int getMapColumn() {
		return mapColumn_;
	}
}
