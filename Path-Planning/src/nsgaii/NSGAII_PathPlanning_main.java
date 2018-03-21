// NSGAIIPathPlanning_main.java

package nsgaii;

import jmetal.core.Algorithm;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

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
	    fileHandler_ = new FileHandler("NSGAII_main.log"); 
	    logger_.addHandler(fileHandler_) ;
	    
	    NSGAII_PathPlanning_Settings settings = new NSGAII_PathPlanning_Settings();
	    Algorithm algorithm = settings.configure();
	    
	    // Execute the Algorithm
	    long initTime = System.currentTimeMillis();
	    SolutionSet population = algorithm.execute();
	    long estimatedTime = System.currentTimeMillis() - initTime;
	    
	    // Result messages 
	    logger_.info("Total execution time: "+estimatedTime + "ms");
	    logger_.info("Variables values have been writen to file VAR");
	    population.printVariablesToFile("VAR");    
	    logger_.info("Objectives values have been writen to file FUN");
	    population.printObjectivesToFile("FUN");
	}
}
