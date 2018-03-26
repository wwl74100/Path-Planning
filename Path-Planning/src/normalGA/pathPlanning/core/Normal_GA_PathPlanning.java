package normalGA.pathPlanning.core;

import jmetal.core.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class Normal_GA_PathPlanning extends Algorithm {
	public Normal_GA_PathPlanning(Problem problem) {
		super(problem);
	}
	
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
	    int maxEvaluations;
	    int evaluations;
	    
	    QualityIndicator indicators; // QualityIndicator object
	    int requiredEvaluations; // Use in the example of use of the
	    // indicators object (see below)
	    
	    SolutionSet population;
	    SolutionSet offspringPopulation;
	    SolutionSet union;
	    
	    Operator mutationOperator;
	    Operator crossoverOperator;
	    Operator selectionOperator;
	    Operator modificationOperator;
	    Operator sortOperator;
	    
	    populationSize = ((Integer) getInputParameter("populationSize")).intValue();
	    maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
	    indicators = (QualityIndicator) getInputParameter("indicators");
	    
	    population = new SolutionSet(populationSize);
	    evaluations = 0;
	    
	    requiredEvaluations = 0;
	    
	    mutationOperator = operators_.get("mutation");
	    crossoverOperator = operators_.get("crossover");
	    selectionOperator = operators_.get("selection");
	    modificationOperator = operators_.get("modification");
	    sortOperator = operators_.get("sort");
	    
	    // Create the initial solutionSet
	    Solution newSolution;
	    for (int i = 0; i < populationSize; i++) {
	      newSolution = new Solution(problem_);
	      sortOperator.execute(newSolution);
	      problem_.evaluate(newSolution);
	      problem_.evaluateConstraints(newSolution);
	      evaluations++;
	      population.add(newSolution);
	    } //for 
	    
	    return population;
	}
}
