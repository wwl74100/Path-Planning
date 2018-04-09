  // NSGAIIPathPlanning_main.java

package nsgaii.pathPlanning.core;

import jmetal.core.Algorithm;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import nsgaii.pathPlanning.comparator.NSGAII_PathPlanning_DoubleObjectiveComparator;
import nsgaii.pathPlanning.comparator.NSGAII_PathPlanning_ThreeObjectiveComparator;
import pathPlanning.demo.DemoPainter;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * 
 * @author X.K.T
 * @class NSGAII_PathPlanning_main
 * @brief Execute NSGAII
 *
 */
public class NSGAII_PathPlanning_main {	
	public static Logger      logger_ ;      // Logger object
	public static FileHandler fileHandler_ ; // FileHandler object	
	
	public static void main(String[] args) throws 
    								JMException, 
    								SecurityException, 
    								IOException, 
    								ClassNotFoundException {
		// Logger object and file to store log messages
	    logger_      = Configuration.logger_ ;
	    fileHandler_ = new FileHandler("Result_NSGAII/NSGAII.log"); 
	    logger_.addHandler(fileHandler_) ;
	    
	    NSGAII_PathPlanning_Settings settings = new NSGAII_PathPlanning_Settings("map/map0.txt");
	    Algorithm algorithm = settings.configure();
	    
	    // Execute the Algorithm
	    long initTime = System.currentTimeMillis();
	    SolutionSet population = algorithm.execute();
	    long estimatedTime = System.currentTimeMillis() - initTime;
	    
	    // Result messages 
	    logger_.info("Total execution time: "+estimatedTime + "ms");
	    logger_.info("Variables values have been writen to file NSGAII_VAR");
	    population.printVariablesToFile("Result_NSGAII/NSGAII_VAR");    
	    logger_.info("Objectives values have been writen to file NSGAII_FUN");
	    population.printObjectivesToFile("Result_NSGAII/NSGAII_FUN");
	    
	    //double objective
	    population.sort(new NSGAII_PathPlanning_DoubleObjectiveComparator());
	    new DemoPainter("NSGAII", algorithm.getProblem(), population.best(new NSGAII_PathPlanning_DoubleObjectiveComparator()));
	
	    //three objective
	    //population.sort(new NSGAII_PathPlanning_ThreeObjectiveComparator());
	    //new DemoPainter("NSGAII", algorithm.getProblem(), population.best(new NSGAII_PathPlanning_ThreeObjectiveComparator()));
	
	}
}
