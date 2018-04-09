package pathPlanning;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.core.Algorithm;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import normalGA.pathPlanning.comparator.Normal_GA_PathPlanning_Comparator;
import normalGA.pathPlanning.comparator.Normal_GA_PathPlanning_TestComparator;
import normalGA.pathPlanning.core.Normal_GA_PathPlanning_Settings;
import nsgaii.pathPlanning.comparator.NSGAII_PathPlanning_DoubleObjectiveComparator;
import nsgaii.pathPlanning.comparator.NSGAII_PathPlanning_ThreeObjectiveComparator;
import nsgaii.pathPlanning.core.NSGAII_PathPlanning_Settings;
import pathPlanning.demo.DemoPainter;
import pathPlanning.problem.PathPlanning_Problem;

/**
 * 
 * @author X.K.T
 * @class Test_PathPlanning_TypicalMap
 * @brief Test CGA and NSGAII on four typical maps
 *
 */
public class Test_PathPlanning_TypicalMap {
	public static Logger      logger_ ;      // Logger object
	public static FileHandler fileHandler_ ; // FileHandler object	
	public static int Iteration = 1000;
	
	/**
	 * test CGA
	 * @param fileName: map file name
	 * @throws JMException 
	 * @throws ClassNotFoundException 
	 */
	public static Solution testCGA(String fileName, double mapOptimal) throws ClassNotFoundException, JMException {
		//Test Conventional GA
		int optimalNumber = 0, infeasibleNumber = 0;
		long initTime = 0, totalTime = 0;
		Normal_GA_PathPlanning_Settings CGA_Settings;
		Algorithm CGA_Algorithm;
		SolutionSet CGA_Best = new SolutionSet(Iteration);
		double CGA_Shortest_Length = Double.MAX_VALUE;
		
		CGA_Settings = new Normal_GA_PathPlanning_Settings(fileName);
		CGA_Algorithm = CGA_Settings.configure();
		CGA_Best.clear();
		for(int i = 0; i < Iteration; ++ i) {
			//System.out.println(i);
			if(i % 100 == 0) {
				System.out.println(i);
			}
			initTime = System.currentTimeMillis();
			SolutionSet population = CGA_Algorithm.execute();
			totalTime += System.currentTimeMillis() - initTime;	
			Solution best_solution = population.best(new Normal_GA_PathPlanning_Comparator());
			
			if(best_solution.getObjective(4) > 0) {
				++ infeasibleNumber;
			}
			else {
				CGA_Shortest_Length = Math.min(CGA_Shortest_Length, best_solution.getObjective(0));
				if(best_solution.getObjective(0) < mapOptimal) {
					++ optimalNumber;
				}
			}
			
			CGA_Best.add(best_solution);
		}
		CGA_Best.sort(new Normal_GA_PathPlanning_TestComparator());
		
		System.out.println("Conventional GA average excution time " + (totalTime / Iteration) + "ms");
		System.out.println("Conventional GA shortest path length " + CGA_Shortest_Length);
		System.out.println("Conventional GA optimal rate " + ((double)optimalNumber / Iteration));
		System.out.println("Conventional GA infeasible rate " + ((double)infeasibleNumber / Iteration));
		CGA_Best.printVariablesToFile("Result_CGA/" + fileName + "VAR"); 
		CGA_Best.printFitnessToFile("Result_CGA/" + fileName + "FIT");
		CGA_Best.printObjectivesToFile("Result_CGA/" + fileName + "FUN");
		
		return CGA_Best.best(new Normal_GA_PathPlanning_Comparator());
	}
	
	/**
	 * test NSGAII
	 * @param fileName
	 * @param mapOptimal
	 * @throws ClassNotFoundException
	 * @throws JMException
	 */
	public static Solution testNSGAII(String fileName, double mapOptimal) throws ClassNotFoundException, JMException {
		int optimalNumber = 0, infeasibleNumber = 0;
		long initTime = 0, totalTime = 0;
		NSGAII_PathPlanning_Settings NSGAII_Settings;
		Algorithm NSGAII_Algorithm;
		SolutionSet NSGAII_Best = new SolutionSet(Iteration);
		double NSGAII_Shortest_Length = Double.MAX_VALUE;
		
		NSGAII_Settings = new NSGAII_PathPlanning_Settings(fileName);
		NSGAII_Algorithm = NSGAII_Settings.configure();
		
		NSGAII_Best.clear();
		for(int i = 0; i < Iteration; ++ i) {
			//System.out.println(i);
			if(i % 100 == 0) {
				System.out.println(i);
			}
			initTime = System.currentTimeMillis();
			SolutionSet population = NSGAII_Algorithm.execute();
			totalTime += System.currentTimeMillis() - initTime;	
			//Solution best_solution = population.best(new NSGAII_PathPlanning_ThreeObjectiveComparator());
			Solution best_solution = population.best(new NSGAII_PathPlanning_DoubleObjectiveComparator());
			
			
			//System.out.println(best_solution.getObjective(1));
			if(best_solution.getObjective(1) > 0) {
				++ infeasibleNumber;
			}
			else {
				/*NSGAII_Shortest_Length = Math.min(NSGAII_Shortest_Length, best_solution.getObjective(0));
				if(best_solution.getObjective(0) < mapOptimal) {
					++ optimalNumber;
				}*/
				
				NSGAII_Shortest_Length = Math.min(NSGAII_Shortest_Length, best_solution.getFitness());
				if(best_solution.getFitness() < mapOptimal) {
					++ optimalNumber;
				}	
			}
			
			NSGAII_Best.add(best_solution);
		}
		//NSGAII_Best.sort(new NSGAII_PathPlanning_ThreeObjectiveComparator());
		NSGAII_Best.sort(new NSGAII_PathPlanning_DoubleObjectiveComparator());
		
		System.out.println("NSGAII average excution time " + (totalTime / Iteration) + "ms");
		System.out.println("NSGAII shortest path length " + NSGAII_Shortest_Length);
		System.out.println("NSGAII optimal rate " + ((double)optimalNumber / Iteration));
		System.out.println("NSGAII infeasible rate " + ((double)infeasibleNumber / Iteration));
		NSGAII_Best.printVariablesToFile("Result_NSGAII/" + fileName + "VAR"); 
		NSGAII_Best.printObjectivesToFile("Result_NSGAII/"+ fileName + "FUN");	
		
		//return NSGAII_Best.best(new NSGAII_PathPlanning_ThreeObjectiveComparator());
		return NSGAII_Best.best(new NSGAII_PathPlanning_DoubleObjectiveComparator());
	}
	
	public static void main(String[] args) throws 
									JMException, 
									SecurityException, 
									IOException, 
									ClassNotFoundException {
		// Logger object and file to store log messages
		logger_      = Configuration.logger_ ;
		fileHandler_ = new FileHandler("Test_main.log"); 
		logger_.addHandler(fileHandler_) ;

		double[] mapOptimal = {29.50, 27.50, 29.50, 23.0};
		String[] fileName = {"map/map0.txt", "map/map1.txt", "map/map2.txt", "map/map3.txt"};
		Solution solution1, solution2;
		
		for(int k = 0; k < 2; ++ k) {
			System.out.println(fileName[k]);
			
			System.out.println("CGA:");
			solution1 = testCGA(fileName[k], mapOptimal[k]);
			
			System.out.println("NSGAII:");
			solution2 = testNSGAII(fileName[k], mapOptimal[k]);
			
			new DemoPainter(fileName[k], (PathPlanning_Problem)solution1.getProblem(), solution1, solution2);
			//new DemoPainter(fileName[k], (PathPlanning_Problem)solution1.getProblem(), solution1);
			//new DemoPainter(fileName[k], (PathPlanning_Problem)solution2.getProblem(), solution2);
			
			System.out.println();
		}	
	}
}
