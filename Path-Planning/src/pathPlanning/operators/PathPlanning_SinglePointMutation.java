package pathPlanning.operators;

import java.util.HashMap;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import nsgaii.pathPlanning.problem.NSGAII_PathPlanning_Problem;

/**
 * 
 * @author X.K.T
 * @class PathPlanning_SinglePointMutation
 * @brief Single point mutation
 *
 */
public class PathPlanning_SinglePointMutation extends Mutation {
	private static final double ETA_M_DEFAULT_ = 20.0; //useless in path-planning problem
	private final double eta_m_=ETA_M_DEFAULT_; //useless in path-planning problem
	
	private Double mutationProbability_ = null; //mutation probability
	private Double distributionIndex_ = eta_m_; //useless in path-planning problem
	
	/**
	 * @brief Constructor
	 * @param parameters: useless in path-planning problem except probability
	 */
	public PathPlanning_SinglePointMutation(HashMap<String, Object> parameters) {
		super(parameters);
		if (parameters.get("probability") != null)
	  		mutationProbability_ = (Double) parameters.get("probability") ;  		
	  	if (parameters.get("distributionIndex") != null)
	  		distributionIndex_ = (Double) parameters.get("distributionIndex") ;  
	}
	
	/**
	 * @brief Do mutation
	 * @param probability: Mutation probability
	 * @param solution: Solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability, Solution solution) throws JMException {
		try {
			if (PseudoRandom.randDouble() < probability) {
				int mutationPoint = PseudoRandom.randInt(1, solution.numberOfVariables() - 2);
				int ulimit = (int)solution.getDecisionVariables()[solution.numberOfVariables() - 1].getValue();
				int blimit = (int)solution.getDecisionVariables()[0].getValue();
				int valueX = PseudoRandom.randInt(blimit + 1, ulimit - 1);				
		        
				int flag = 1;
				while(flag == 1) {
					for(int i = 1; i < solution.numberOfVariables(); ++ i) {
						if(valueX == (int)solution.getDecisionVariables()[i].getValue()) {
							flag = 1;
							valueX = PseudoRandom.randInt(blimit + 1, ulimit - 1);
							break;
						}
						else {
							flag = 0;
						}
					}	
				}
				solution.getDecisionVariables()[mutationPoint].setValue(valueX);
			}
		} catch (ClassCastException e1) {
			Configuration.logger_.severe("SinglePointCrossover.doCrossover: Cannot perfom " +
		              					 "SinglePointCrossover");
		    Class cls = java.lang.String.class;
		    String name = cls.getName();
		    throw new JMException("Exception in " + name + ".doCrossover()");
		}
	}
	
	/**
	 * @brief Execute the modification operator
	 * @param object: Solution to mutate
	 * @return mutated solution
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;
		doMutation(mutationProbability_, solution);
		return solution;
	}
}
