// NSGAII_Settings.java

package nsgaii.pathPlanning.core;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import nsgaii.pathPlanning.problem.NSGAII_PathPlanning_Problem;
import pathPlanning.operators.PathPlanning_Modification;
import pathPlanning.operators.PathPlanning_SinglePointMutation;
import pathPlanning.operators.PathPlanning_Sort;
import pathPlanning.operators.PathPlanning_TwoPointCrossover;

public class NSGAII_PathPlanning_Settings extends Settings {
	public int populationSize_                 ;
	public int maxEvaluations_                 ;
	public double mutationProbability_         ;
	public double crossoverProbability_        ;
	public double mutationDistributionIndex_   ;
	public double crossoverDistributionIndex_  ;

	public NSGAII_PathPlanning_Settings(String fileName) {
		super();
		problem_ = new NSGAII_PathPlanning_Problem(fileName);
	    // Default experiments.settings
	    populationSize_              = 80   ;
	    maxEvaluations_              = 40000 ;
	    mutationProbability_         = 0.15 ;
	    crossoverProbability_        = 0.8   ;
	    mutationDistributionIndex_   = 20.0  ;
	    crossoverDistributionIndex_  = 20.0  ;
	} // NSGAII_Settings

	public Algorithm configure() throws JMException {
		Algorithm algorithm ;
		Selection selection ;
		Crossover crossover ;
		Mutation  mutation  ;
		PathPlanning_Modification modification;
		PathPlanning_Sort sort;

	    HashMap  parameters ; // Operator parameters

	    // Creating the algorithm. There are two choices: NSGAII and its steady-
	    // state variant ssNSGAII
	    algorithm = new NSGAII_PathPlanning(problem_) ;

	    // Algorithm parameters
	    algorithm.setInputParameter("populationSize", populationSize_);
	    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

	    // Mutation and Crossover for Real codification
	    parameters = new HashMap() ;
	    parameters.put("probability", crossoverProbability_) ;
	    parameters.put("distributionIndex", crossoverDistributionIndex_) ;
	    crossover = new PathPlanning_TwoPointCrossover(parameters);

	    parameters = new HashMap() ;
	    parameters.put("probability", mutationProbability_) ;
	    parameters.put("distributionIndex", mutationDistributionIndex_) ;
	    mutation = new PathPlanning_SinglePointMutation(parameters);

	    //modification
	    parameters = new HashMap() ;
	    parameters.put("probability", mutationProbability_) ;
	    parameters.put("distributionIndex", mutationDistributionIndex_) ;
	    modification = new PathPlanning_Modification(parameters);
	    
	    parameters = new HashMap() ;
	    parameters.put("probability", mutationProbability_) ;
	    parameters.put("distributionIndex", mutationDistributionIndex_) ;
	    sort = new PathPlanning_Sort(parameters);
	    
	    // Selection Operator
	    parameters = null ;
	    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;

	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover",crossover);
	    algorithm.addOperator("mutation",mutation);
	    algorithm.addOperator("selection",selection);
	    algorithm.addOperator("modification", modification);
	    algorithm.addOperator("sort", sort);

	    return algorithm ;
	}
}
