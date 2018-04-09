package normalGA.pathPlanning.core;

import jmetal.core.Algorithm;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import normalGA.pathPlanning.comparator.Normal_GA_PathPlanning_Comparator;
import nsgaii.pathPlanning.comparator.NSGAII_PathPlanning_ThreeObjectiveComparator;
import pathPlanning.demo.DemoPainter;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * 
 * @author X.K.T
 * @class Normal_GA_PathPlanning_main
 * @brief Execute normal GA
 *
 */
public class Normal_GA_PathPlanning_main {
	public static Logger      logger_ ;      // Logger object
	public static FileHandler fileHandler_ ; // FileHandler object	
	
	public static void main(String[] args) throws 
									JMException, 
									SecurityException, 
									IOException, 
									ClassNotFoundException {
		// Logger object and file to store log messages
	    logger_      = Configuration.logger_ ;
	    fileHandler_ = new FileHandler("Result_CGA/CGA_main.log"); 
	    logger_.addHandler(fileHandler_) ;
	    
	    Normal_GA_PathPlanning_Settings settings = new Normal_GA_PathPlanning_Settings("map/map0.txt");
	    Algorithm algorithm = settings.configure();
	    
	    // Execute the Algorithm
	    long initTime = System.currentTimeMillis();
	    SolutionSet population = algorithm.execute();
	    long estimatedTime = System.currentTimeMillis() - initTime;
	    
	    // Result messages 
	    logger_.info("Total execution time: "+estimatedTime + "ms");
	    logger_.info("Variables values have been writen to file VAR");
	    population.printVariablesToFile("Result_CGA/Normal_GA_VAR");    
	    logger_.info("Fitness values have been writen to file FIT");
	    population.printFitnessToFile("Result_CGA/Normal_GA_FIT");
	    logger_.info("Objectives values have been writen to file FUN");
	    population.printObjectivesToFile("Result_CGA/Normal_GA_FUN");
	    
	    new DemoPainter("CGA", algorithm.getProblem(), population.best(new Normal_GA_PathPlanning_Comparator()));
	}
}
