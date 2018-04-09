package pathPlanning;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
 * @class Test_PathPlanning_AllMap
 * @brief Test CGA and NSGAII on all maps
 *
 */
public class Test_PathPlanning_AllMap {
	public static Logger      logger_ ;      // Logger object
	public static FileHandler fileHandler_ ; // FileHandler object	
	public static int Iteration = 100;
	
	/**
	 * @brief test CGA
	 * @param fileName: map file name
	 * @throws JMException 
	 * @throws ClassNotFoundException 
	 */
	public static void testCGA(String fileName) throws ClassNotFoundException, JMException, IOException {
		//Test Conventional GA
		FileOutputStream fos   = new FileOutputStream("Result_CGA/CGA_all_map_result.txt", true);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw      = new BufferedWriter(osw);
		
		int infeasibleNumber = 0;
		long initTime = 0, totalTime = 0;
		double shortestLength = Double.MAX_VALUE;
		double totalLength = 0;
		
		Normal_GA_PathPlanning_Settings CGA_Settings = new Normal_GA_PathPlanning_Settings(fileName);
		Algorithm CGA_Algorithm = CGA_Settings.configure();
		SolutionSet CGA_Best = new SolutionSet(Iteration);
		CGA_Best.clear();
		
		for(int i = 0; i < Iteration; ++ i) {
			//System.out.println(i);
			initTime = System.currentTimeMillis();
			SolutionSet population = CGA_Algorithm.execute();
			totalTime += System.currentTimeMillis() - initTime;	
			Solution best_solution = population.best(new Normal_GA_PathPlanning_Comparator());
			
			if(best_solution.getObjective(4) > 0) {
				++ infeasibleNumber;
			}
			else {
				shortestLength = Math.min(shortestLength, best_solution.getObjective(0));
				totalLength += best_solution.getObjective(0);
			}
			
			CGA_Best.add(best_solution);
		}
		
		bw.newLine();
		bw.write(fileName);
		bw.newLine();
		bw.write("    CGA average excution time: " + (totalTime / Iteration) + "ms");
		bw.newLine();
		bw.write("    CGA shortest path length : " + shortestLength);
		bw.newLine();
		bw.write("    CGA average path length  : " + (totalLength / ((Iteration == infeasibleNumber)? 1 : (Iteration - infeasibleNumber))));
		bw.newLine();
		bw.write("    CGA infeasible rate      : " + ((double)infeasibleNumber / Iteration));
		bw.close();
	}
	
	/**
	 * @brief test NSGAII
	 * @param fileName
	 * @param mapOptimal
	 * @throws ClassNotFoundException
	 * @throws JMException
	 */
	public static void testNSGAII(String fileName) throws ClassNotFoundException, JMException, IOException {
		FileOutputStream fos   = new FileOutputStream("Result_NSGAII/NSGAII_all_map_result.txt", true);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw      = new BufferedWriter(osw);
		
		int infeasibleNumber = 0;
		long initTime = 0, totalTime = 0;
		double shortestLength = Double.MAX_VALUE;
		double totalLength = 0;
		
		NSGAII_PathPlanning_Settings NSGAII_Settings = new NSGAII_PathPlanning_Settings(fileName);
		Algorithm NSGAII_Algorithm = NSGAII_Settings.configure();
		SolutionSet NSGAII_Best = new SolutionSet(Iteration);
		NSGAII_Best.clear();
		
		for(int i = 0; i < Iteration; ++ i) {
			//System.out.println(i);
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
				
				shortestLength = Math.min(shortestLength, best_solution.getFitness());
				totalLength += best_solution.getFitness();
			}
			
			NSGAII_Best.add(best_solution);
		}
		
		bw.newLine();
		bw.write(fileName);
		bw.newLine();
		bw.write("    NSGAII average excution time: " + (totalTime / Iteration) + "ms");
		bw.newLine();
		bw.write("    NSGAII shortest path length : " + shortestLength);
		bw.newLine();
		bw.write("    NSGAII average path length  : " + (totalLength / ((Iteration == infeasibleNumber)? 1 : (Iteration - infeasibleNumber))));
		bw.newLine();
		bw.write("    NSGAII infeasible rate      : " + ((double)infeasibleNumber / Iteration));
		bw.close();
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

		String fileName;
		
		for(int k = 0; k < 100; ++ k) {
			fileName = "map/map" + String.valueOf(k) + ".txt";
			System.out.println(fileName);
			
			System.out.println("CGA:");
			testCGA(fileName);
			
			System.out.println("NSGAII:");
			testNSGAII(fileName);
	
			System.out.println();
		}	
	}
}
