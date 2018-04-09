package normalGA.pathPlanning.core;

import jmetal.core.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import normalGA.pathPlanning.comparator.Normal_GA_PathPlanning_Comparator;

/**
 * 
 * @author X.K.T
 * @class Normal_GA_PathPlanning 
 * @brief Implement normal GA 
 *
 */
public class Normal_GA_PathPlanning extends Algorithm {
	private int eliteOffSpringNumber_ = 10;
	
	/**
	 * @brief Constructor 
	 * @param problem
	 */
	public Normal_GA_PathPlanning(Problem problem) {
		super(problem);
	}
	
	/**
	 * @brief Execute normal GA
	 */
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
	    
	    // Generations 
	    while (evaluations < maxEvaluations) {
	    	// Create the offSpring solutionSet      
	    	offspringPopulation = new SolutionSet(populationSize);
	    	Solution[] parents = new Solution[2];
	    	for (int i = 0; i < (populationSize / 2); i++) {
	    		if (evaluations < maxEvaluations) {
	            //obtain parents
	    			parents[0] = (Solution) selectionOperator.execute(population);
	    			parents[1] = (Solution) selectionOperator.execute(population);

	    			Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);

	    			sortOperator.execute(offSpring[0]);
	    			mutationOperator.execute(offSpring[0]);
	    			sortOperator.execute(offSpring[0]);
	    			modificationOperator.execute(offSpring[0]);
	          
	    			sortOperator.execute(offSpring[1]);
	    			mutationOperator.execute(offSpring[1]);
	    			sortOperator.execute(offSpring[1]);
	    			modificationOperator.execute(offSpring[1]);
	          
	    			problem_.evaluate(offSpring[0]);
	    			problem_.evaluateConstraints(offSpring[0]);
	    			problem_.evaluate(offSpring[1]);
	    			problem_.evaluateConstraints(offSpring[1]);
	    			offspringPopulation.add(offSpring[0]);
	    			offspringPopulation.add(offSpring[1]);
	    			evaluations += 2;
	    		} // if                            
	    	} // for
	      
	    	union = ((SolutionSet) population).union(offspringPopulation);
	    	union.sort(new Normal_GA_PathPlanning_Comparator());
	    	
	        int remain = populationSize;
	        int unionSize = union.size();
	        population.clear();
	        
	        for(int i = 0; i < eliteOffSpringNumber_; ++ i) {
	        	population.add(union.get(i));
	        }
	        remain -= eliteOffSpringNumber_;

	        while(remain > 0) {
	        	int r = PseudoRandom.randInt(0, unionSize - 1);
	        	population.add(union.get(r));
	        	-- remain;
	        }       
	    } // while
	    
	    return population;
	}
}
