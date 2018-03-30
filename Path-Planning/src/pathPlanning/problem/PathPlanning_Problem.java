package pathPlanning.problem;

import dataHandler.DataHandler;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import pathPlanning.solution.PathPlanning_SolutionType;

public abstract class PathPlanning_Problem extends Problem {
	private double bias = 0.00001;
	
	protected int mapSize_;         // map size
	protected int mapRow_;          // map row number
	protected int mapColumn_;       // map column number
	private int mapStartPoint_;   // map start point
	private int mapTargetPoint_;  // map target point
	private char[] mapVector_;    // map info vector
	private char[][] mapMatrix_;  // map info matrix
	
	protected void getMapInfo(String fileName) {
		DataHandler dataHandler = new DataHandler(fileName);
		mapSize_        = dataHandler.getMapSize();
		mapRow_         = dataHandler.getMapRow();
		mapColumn_      = dataHandler.getMapColumn();
		mapStartPoint_  = dataHandler.getStartPoint();
		mapTargetPoint_ = dataHandler.getTargetPoint();
		mapVector_      = dataHandler.getMapVector();
		mapMatrix_      = dataHandler.getMapMatrix();
	}
	
	public PathPlanning_Problem(String fileName) {
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
	
	public abstract void evaluate(Solution solution) throws JMException; 
	
	/**
	 * check the path is feasible or not
	 * @param x
	 * @param y
	 * @return : true : path is feasible
	 * 			 false : path is infeasible
	 * @throws JMException
	 */
	protected boolean checkPathFeasible(double[] x, double[] y) throws JMException {
		double temp;
		for(int i = 1; i < numberOfVariables_; ++ i) {
			temp = checkSegInObstacle((int)x[i - 1], (int)y[i - 1], (int)x[i], (int)y[i]);
			if(temp > 0) return false;
		}
		return true;
	}
	
	/**
	 * get the infeasible percent of the path
	 * @param x
	 * @param y
	 * @param pathLength : the total length of the path
	 * @return : double[0] is the percent of infeasible segment number
	 * 			 double[1] is the percent of infeasible segment length
	 * @throws JMException
	 */
	protected double[] getPathInfeasiblePercent(double[] x, double[] y, double pathLength) throws JMException {	
		double[] iSegPercent = new double[2];
		double iSegNumber = 0, iSegLength = 0;
		double temp;
		
		for(int i = 1; i < numberOfVariables_; ++ i) {
			temp = checkSegInObstacle((int)x[i - 1], (int)y[i - 1], (int)x[i], (int)y[i]);
			//if the segment is infeasible
			if(temp > 0) {
				if(temp > Double.MAX_VALUE / 2) {
					iSegNumber += 1.0;
				}
				else {
					iSegNumber += 1.0;
					iSegLength += temp;
				}
			}
		}
		iSegPercent[0] = iSegNumber / (numberOfVariables_ - 1);
		iSegPercent[1] = iSegLength / pathLength;
		
		return iSegPercent;
	}
	
	/**
	 * get the total length of a path
	 * @param x : the x coordinates of the points on the path
	 * @param y : the y coordinates of the points on the path
	 * @return : the total path length
	 * @throws JMException
	 */
	protected double getPathLength(double[] x, double[] y) throws JMException {		
		double distance = 0.0;
		
		for(int i = 1; i < numberOfVariables_; ++ i) {
			distance += getLengthOfSeg(x[i], y[i], x[i - 1], y[i - 1]);
		}
		
		return distance;
	}
	
	/**
	 * get the total angle of a path
	 * @param gen : the path
	 * @param x : the x coordinates of the points on the path
	 * @param y : the y coordinates of the points on the path
	 * @return : the total path angle
	 * @throws JMException
	 */
	protected double getPathAngle(Variable[] gen, double[] x, double[] y) throws JMException {
		double pathAngle = 0.0, angle1, angle2;
		
		for(int i = 1; i < numberOfVariables_ - 1; ++ i) {
			//when the start parts of the segment are the same point
			while((i < numberOfVariables_ - 1) && (gen[i].getValue() == gen[i - 1].getValue())) {
				++ i;
			}
			if(i == numberOfVariables_ - 1) return pathAngle;
			angle1 = getAngleOfVector(x[i - 1] - x[i], y[i - 1] - y[i]);
			
			//when the last parts of the segment are the same point
			while((i < numberOfVariables_ - 1) && (gen[i].getValue() == gen[i + 1].getValue())) {
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
	 * @param gen : the path
	 * @param x : the x coordinates of the points on the path
	 * @param y : the y coordinates of the points on the path
	 * @return : the total path safety
	 * @throws JMException
	 */
	protected double getPathSafety(Variable gen[], double[] x, double[] y) throws JMException {
		double pathSafety = 0.0, safetyThis = 0;
		
		for(int i = 1; i < numberOfVariables_; ++ i) {
			double temp = checkSegInObstacle((int)x[i - 1], (int)y[i - 1], (int)x[i], (int)y[i]);
			//if the segment is infeasible
			if(temp > 0) {
				return Double.MAX_VALUE;
			}
			
			//if the segment is feasible
			safetyThis = getSafetyOfSeg((int)x[i - 1], (int)y[i - 1], (int)x[i], (int)y[i]);

			pathSafety += safetyThis;
		}
		
		return (double)mapSize_ / pathSafety;
	}
	
	/**
	 * get the length of a segment (x1, y1) ~ (x2, y2)
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return : the segment length
	 */
	public double getLengthOfSeg(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	/**
	 * get the angle between vector (x, y) and the x axis
	 * @param x
	 * @param y
	 * @return
	 */
	private double getAngleOfVector(double x, double y) {
		if(x == 0) {
			if(y == 0) return 0;
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
	 * if the segment is feasible, get its safety
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return : the safety of the segment
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
	
	/**
	 * check if the segment is in an obstacle
	 * @param x1 : segment point x1
	 * @param y1 : segment point y1
	 * @param x2 : segment point x2
	 * @param y2 : segment point y2
	 * @return : the length of the segment crossing the obstacle
	 */
	public double checkSegInObstacle(int x1, int y1, int x2, int y2) {
		int maxX = Math.max(x1, x2), minX = Math.min(x1, x2);
		int maxY = Math.max(y1, y2), minY = Math.min(y1, y2);
		double dx1 = x1 + 0.5, dx2 = x2 + 0.5, dy1 = y1 + 0.5, dy2 = y2 + 0.5;
		double k, b;
		double len = 0.0;
		
		//if the two points of the segment are the sane
		if((minX == maxX) && (minY == maxY)) {
			//if the points are in an obstacle
			if(mapMatrix_[minY][minX] == '1') {
				return Double.MAX_VALUE;
			}
			//if the points are not in an obstacle
			else {
				return 0;
			}
		}
		
		//if the x coordinates are the same
		if(minX == maxX) {
			//if the segment is in an obstacle
			if(mapMatrix_[minY][minX] == '1') {
				len += 0.5;
			}
			if(mapMatrix_[maxY][minX] == '1') {
				len += 0.5;
			}
			for(int i = minY + 1; i <= maxY - 1; ++ i) {
				if(mapMatrix_[i][minX] == '1') {
					len += 1.0;
				}
			}
			return len;
		}
	
		//if the y coordinates are the same
		if(minY == maxY) {
			if(mapMatrix_[minY][minX] == '1') {
				len += 0.5;
			}
			if(mapMatrix_[minY][maxX] == '1') {
				len += 0.5;
			}
			//if the segment is in an obstacle
			for(int i = minX + 1; i <= maxX - 1; ++ i) {
				if(mapMatrix_[minY][i] == '1') {
					len += 1.0;
				}
			}
			return len;
		}
		
		//if the x coordinates and the y coordinates are not the same
		k = (dy1 - dy2) / (dx1 - dx2);
		b = (dx1 * dy2 - dx2 * dy1) / (dx1 - dx2);
		//System.out.println("k= " + k + " b= " + b);
		
		for(int i = minX; i <= maxX; ++ i) {
			for(int j = minY; j <= maxY; ++ j) {
				//if this is an obstacle
				if(mapMatrix_[j][i] == '1') {
					//if this point is the start or end point of the segment
					if(((i == x1) && (j == y1)) || ((i == x2) && (j == y2))) {
						if((k * i + b >= Math.max(minY + 0.5, j) - bias) 
								&& (k * i + b <= Math.min(maxY + 0.5, j + 1) + bias)) {
							len += getLengthOfSeg(i + 0.5, j + 0.5, i, k * i + b);
							continue;
						}
						if((k * (i + 1) + b >= Math.max(minY + 0.5, j) - bias)
								&& (k * (i + 1) + b <= Math.min(maxY + 0.5, j + 1) + bias)) {
							len += getLengthOfSeg(i + 0.5, j + 0.5, i + 1, k * (i + 1) + b);
							continue;
						}
						if(((j - b) / k >= Math.max(i, minX + 0.5) - bias) 
								&& ((j - b) / k <= Math.min(i + 1, maxX + 0.5) + bias)) {
							len += getLengthOfSeg(i + 0.5, j + 0.5, (j - b) / k, j);
							continue;
						}
						if(((j + 1 - b) / k >= Math.max(i, minX + 0.5) - bias) 
								&& ((j + 1 - b) / k <= Math.min(i + 1, maxX + 0.5) + bias)) {
							len += getLengthOfSeg(i + 0.5, j + 0.5, (j + 1 - b) / k, j + 1);
							continue;
						}
					}
					//if this point is not the start or end point of the segment
					else {
						//System.out.println("len= " +  len);
						//System.out.println("j= " + j + " i= " + i);
						//System.out.println((k * i + b) + " " + (k * (i + 1) + b));
						//System.out.println(((j - b) / k) + " " + ((j + 1 - b) / k));
						int flag1 = 0, flag2 = 0;
						double a1 = 0.0, b1 = 0.0, a2 = 0.0, b2 = 0.0;
						if((k * i + b >= Math.max(minY + 0.5, j) - bias) 
								&& (k * i + b <= Math.min(maxY + 0.5, j + 1) + bias)) {
							if(flag1 == 1) {
								if((i < a1 - bias) || (i > a1 + bias)) {
									a2 = i;
									b2 = k * i + b;
									flag2 = 1;
								}	
							}
							else {
								a1 = i;
								b1 = k * i + b;
								flag1 = 1;
							}
						}
						if((k * (i + 1) + b >= Math.max(minY + 0.5, j) - bias) 
								&& (k * (i + 1) + b <= Math.min(maxY + 0.5, j + 1) + bias)) {
							if(flag1 == 1) {
								if((i + 1 < a1 - bias) || (i + 1 > a1 + bias)) {
									a2 = i + 1;
									b2 = k * (i + 1) + b;
									flag2 = 1;
								}	
							}
							else {
								a1 = i + 1;
								b1 = k * (i + 1) + b;
								flag1 = 1;
							}
						}
						
						if(((j - b) / k >= Math.max(i, minX + 0.5) - bias) 
								&& ((j - b) / k <= Math.min(i + 1, maxX + 0.5) + bias)) {
							if(flag1 == 1) {
								if((j < b1 - bias) || (j > b1 + bias)) {
									a2 = (j - b) / k;
									b2 = j;
									flag2 = 1;
								}								
							}
							else {
								a1 = (j - b) / k;
								b1 = j;
								flag1 = 1;
							}
						}
						
						if(((j + 1 - b) / k >= Math.max(i, minX + 0.5) - bias) 
								&& ((j + 1 - b) / k <= Math.min(i + 1, maxX + 0.5) + bias)) {
							if(flag1 == 1) {
								if((j + 1 < b1 - bias) || (j + 1 > b1 + bias)) {
									a2 = (j + 1 - b) / k;
									b2 = j + 1;
									flag2 = 1;
								}	
							}
							else {
								a1 = (j + 1 - b) / k;
								b1 = j + 1;
								flag1 = 1;
							}
						}
						if((flag1 == 1) && (flag2 == 1)) {
							//System.out.println("a1 " + a1 + " b1 " + b1 + " a2 " + a2 + " b2 " + b2);
							len += getLengthOfSeg(a1, b1, a2, b2);
						}
					}
				}			
			}
		}
		return len;
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
	
	public int getMapRow() {
		return mapRow_;
	}
	
	public int getMapColumn() {
		return mapColumn_;
	}
	
	public char[][] getMapMatrix() {
		return mapMatrix_;
	}
}
