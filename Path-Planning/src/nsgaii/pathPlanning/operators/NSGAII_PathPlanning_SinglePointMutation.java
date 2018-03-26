package nsgaii.pathPlanning.operators;

import java.util.HashMap;

import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import nsgaii.pathPlanning.problem.NSGAII_PathPlanning_Problem;

public class NSGAII_PathPlanning_SinglePointMutation extends Mutation {
	private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m_=ETA_M_DEFAULT_;
	
	private Double mutationProbability_ = null;
	private Double distributionIndex_ = eta_m_;
	
	/**
	 * Constructor
	 * Creates a new instance of the polynomial mutation operator
	 */
	public NSGAII_PathPlanning_SinglePointMutation(HashMap<String, Object> parameters) {
		super(parameters);
		if (parameters.get("probability") != null)
	  		mutationProbability_ = (Double) parameters.get("probability") ;  		
	  	if (parameters.get("distributionIndex") != null)
	  		distributionIndex_ = (Double) parameters.get("distributionIndex") ;  
	}
	
	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException 
	 */
	public void doMutation(double probability, Solution solution) throws JMException {
		Variable[] gen = solution.getDecisionVariables();
		try {
			if (PseudoRandom.randDouble() < probability) {
				int crossoverPoint = PseudoRandom.randInt(1, solution.numberOfVariables() - 2);
				int valueX = PseudoRandom.randInt(0, ((NSGAII_PathPlanning_Problem)(solution.getProblem())).getMapSize() - 1);				
		        while((gen[crossoverPoint].getValue() == gen[crossoverPoint + 1].getValue())
		        		&& (crossoverPoint < solution.numberOfVariables() - 2)) {
		        	++ crossoverPoint;
		        }
				solution.getDecisionVariables()[crossoverPoint].setValue(valueX);
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
	 * Executes the operation
	 * @param object An object containing a solution
	 * @return An object containing the mutated solution
	 * @throws JMException 
	 */  
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;
		doMutation(mutationProbability_, solution);
		return solution;
	}
}
