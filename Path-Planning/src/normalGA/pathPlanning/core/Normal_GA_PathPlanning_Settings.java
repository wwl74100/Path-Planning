package normalGA.pathPlanning.core;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import nsgaii.pathPlanning.core.NSGAII_PathPlanning;
import nsgaii.pathPlanning.operators.NSGAII_PathPlanning_Modification;
import nsgaii.pathPlanning.operators.NSGAII_PathPlanning_SinglePointCrossover;
import nsgaii.pathPlanning.operators.NSGAII_PathPlanning_SinglePointMutation;
import nsgaii.pathPlanning.problem.NSGAII_PathPlanning_Problem;

public class Normal_GA_PathPlanning_Settings extends Settings{
	public int populationSize_                 ;
	public int maxEvaluations_                 ;
	public double mutationProbability_         ;
	public double crossoverProbability_        ;
	public double mutationDistributionIndex_   ;
	public double crossoverDistributionIndex_  ;
	
	public Normal_GA_PathPlanning_Settings(String fileName) {
		super();
		problem_ = new NSGAII_PathPlanning_Problem(fileName);
	    // Default experiments.settings
	    populationSize_              = 100   ;
	    maxEvaluations_              = 200000 ;
	    mutationProbability_         = 1.0/problem_.getNumberOfVariables() ;
	    crossoverProbability_        = 0.9   ;
	    mutationDistributionIndex_   = 20.0  ;
	    crossoverDistributionIndex_  = 20.0  ;
	} // Normal_GA_Settings
	
	public Algorithm configure() throws JMException {
		Algorithm algorithm ;
		Selection selection ;
		Crossover crossover ;
		Mutation  mutation  ;
		NSGAII_PathPlanning_Modification modification;

	    HashMap  parameters ; // Operator parameters

	    algorithm = new Normal_GA_PathPlanning(problem_) ;

	    // Algorithm parameters
	    algorithm.setInputParameter("populationSize", populationSize_);
	    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

	    // Mutation and Crossover for Real codification
	    parameters = new HashMap() ;
	    parameters.put("probability", crossoverProbability_) ;
	    parameters.put("distributionIndex", crossoverDistributionIndex_) ;
	    crossover = new NSGAII_PathPlanning_SinglePointCrossover(parameters);

	    parameters = new HashMap() ;
	    parameters.put("probability", mutationProbability_) ;
	    parameters.put("distributionIndex", mutationDistributionIndex_) ;
	    mutation = new NSGAII_PathPlanning_SinglePointMutation(parameters);

	    //modification
	    parameters = new HashMap() ;
	    parameters.put("probability", mutationProbability_) ;
	    parameters.put("distributionIndex", mutationDistributionIndex_) ;
	    modification = new NSGAII_PathPlanning_Modification(parameters);
	    
	    // Selection Operator
	    parameters = null ;
	    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;

	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover",crossover);
	    algorithm.addOperator("mutation",mutation);
	    algorithm.addOperator("selection",selection);
	    algorithm.addOperator("modification", modification);

	    return algorithm ;
	}
}
