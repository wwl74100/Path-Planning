// NSGAII_Settings.java

package nsgaii;

import java.util.HashMap;

import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;

public class NSGAII_PathPlanning_Settings extends Settings {
	public int populationSize_                 ;
	public int maxEvaluations_                 ;
	public double mutationProbability_         ;
	public double crossoverProbability_        ;
	public double mutationDistributionIndex_   ;
	public double crossoverDistributionIndex_  ;

	public NSGAII_PathPlanning_Settings() {
		super();
		
	    // Default experiments.settings
	    populationSize_              = 100   ;
	    maxEvaluations_              = 25000 ;
	    mutationProbability_         = 1.0/problem_.getNumberOfVariables() ;
	    crossoverProbability_        = 0.9   ;
	    mutationDistributionIndex_   = 20.0  ;
	    crossoverDistributionIndex_  = 20.0  ;
	} // NSGAII_Settings

	public Algorithm configure() throws JMException {
		Algorithm algorithm ;
		Selection selection ;
		Crossover crossover ;
		Mutation  mutation  ;

	    HashMap  parameters ; // Operator parameters

	    // Creating the algorithm. There are two choices: NSGAII and its steady-
	    // state variant ssNSGAII
	    algorithm = new NSGAII(problem_) ;

	    // Algorithm parameters
	    algorithm.setInputParameter("populationSize", populationSize_);
	    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

	    // Mutation and Crossover for Real codification
	    parameters = new HashMap() ;
	    parameters.put("probability", crossoverProbability_) ;
	    parameters.put("distributionIndex", crossoverDistributionIndex_) ;
	    crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);

	    parameters = new HashMap() ;
	    parameters.put("probability", mutationProbability_) ;
	    parameters.put("distributionIndex", mutationDistributionIndex_) ;
	    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);

	    // Selection Operator
	    parameters = null ;
	    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;

	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover",crossover);
	    algorithm.addOperator("mutation",mutation);
	    algorithm.addOperator("selection",selection);

	    return algorithm ;
	}
}
