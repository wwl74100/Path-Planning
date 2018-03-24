// Test.java

package nsgaii;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Test {	
	public static Logger      logger_ ;      // Logger object
	public static FileHandler fileHandler_ ; // FileHandler object	
	
	public static void main(String[] args) throws 
    								JMException, 
    								SecurityException, 
    								IOException, 
    								ClassNotFoundException {
		// Logger object and file to store log messages
	    logger_      = Configuration.logger_ ;
	    fileHandler_ = new FileHandler("NSGAII_main.log"); 
	    logger_.addHandler(fileHandler_) ;
	    
	    //NSGAII_PathPlanning_Settings settings = new NSGAII_PathPlanning_Settings();
	    //Algorithm algorithm = settings.configure();
	    
	    System.out.println(Math.atan(1));
	    
	    // Execute the Algorithm
	    long initTime = System.currentTimeMillis();
	    NSGAII_PathPlanning_Problem problem_ = new NSGAII_PathPlanning_Problem("map/map0.txt");
	    System.out.println(problem_.getMapStartPoint());
	    System.out.println(problem_.getMapTargetPoint());
	    System.out.println(problem_.getSafetyOfSeg(0, 0, 2, 6));
	    
	    SolutionSet population = new SolutionSet(1);
	    Solution newSolution;
	    for (int i = 0; i < 5; i++) {
	      newSolution = new Solution(problem_);
	      problem_.evaluate(newSolution);
	      problem_.evaluateConstraints(newSolution);
	      population.add(newSolution);
	    }
	    long estimatedTime = System.currentTimeMillis() - initTime;
	    
	    // Result messages 
	    logger_.info("Total execution time: "+estimatedTime + "ms");
	    logger_.info("Variables values have been writen to file VAR");
	    population.printVariablesToFile("VAR");    
	    logger_.info("Objectives values have been writen to file FUN");
	    population.printObjectivesToFile("FUN");
	}
}
