// Test.java

package pathPlanning;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import nsgaii.pathPlanning.comparator.NSGAII_PathPlanning_Comparator;
import nsgaii.pathPlanning.problem.NSGAII_PathPlanning_Problem;
import pathPlanning.demo.DemoPainter;
import pathPlanning.operators.PathPlanning_SinglePointMutation;
import pathPlanning.operators.PathPlanning_Sort;
import pathPlanning.operators.PathPlanning_TwoPointCrossover;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jdk.internal.org.objectweb.asm.util.CheckAnnotationAdapter;

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
	    
	    // Execute the Algorithm
	    long initTime = System.currentTimeMillis();
	    NSGAII_PathPlanning_Problem problem_ = new NSGAII_PathPlanning_Problem("map/map0.txt");
	    
	    double temp = problem_.getSafetyOfSeg(0, 0, 0, 0);
	    System.out.println(temp);

	    /*
	    HashMap parameters = new HashMap() ;
	    parameters.put("probability", 0.8) ;
	    parameters.put("distributionIndex", 20.0) ;
	    PathPlanning_Sort sort = new PathPlanning_Sort(parameters);
	    
	    parameters = new HashMap() ;
	    parameters.put("probability", 0.8) ;
	    parameters.put("distributionIndex", 20.0) ;
	    PathPlanning_TwoPointCrossover crossover = new PathPlanning_TwoPointCrossover(parameters);
	    
	    parameters = new HashMap() ;
	    parameters.put("probability", 1.0) ;
	    parameters.put("distributionIndex", 20.0) ;
	    PathPlanning_SinglePointMutation mutation = new PathPlanning_SinglePointMutation(parameters);
	    
	    SolutionSet population = new SolutionSet(5);
	    Solution newSolution;
	    for (int i = 0; i < 5; i++) {
	      newSolution = new Solution(problem_);
	      sort.execute(newSolution);
	      problem_.evaluate(newSolution);
	      problem_.evaluateConstraints(newSolution);
	      population.add(newSolution);
	    }
	    long estimatedTime = System.currentTimeMillis() - initTime;
	    
	    // Result messages 
	    logger_.info("Variables values have been writen to file VAR");
	    population.printVariablesToFile("TestSortVAR");    
	    logger_.info("Objectives values have been writen to file FUN");
	    population.printObjectivesToFile("TestSortFUN");
	    
	    for (int i = 0; i < 5; i++) {
		    newSolution = population.get(i);
		    mutation.execute(newSolution);
		    sort.execute(newSolution);
		    problem_.evaluate(newSolution);
		    problem_.evaluateConstraints(newSolution);
	    }
	    
	    logger_.info("Variables values have been writen to file VAR");
	    population.printVariablesToFile("TestMutationVAR");    
	    logger_.info("Objectives values have been writen to file FUN");
	    population.printObjectivesToFile("TestMutationFUN");
	    
	    new DemoPainter(problem_, population.best(new NSGAII_PathPlanning_Comparator()));
	    */
	}
}