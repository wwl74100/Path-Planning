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

public class PathPlanning_SinglePointMutation extends Mutation {
	private static final double ETA_M_DEFAULT_ = 20.0;
	private final double eta_m_=ETA_M_DEFAULT_;
	
	private Double mutationProbability_ = null;
	private Double distributionIndex_ = eta_m_;

	public PathPlanning_SinglePointMutation(HashMap<String, Object> parameters) {
		super(parameters);
		if (parameters.get("probability") != null)
	  		mutationProbability_ = (Double) parameters.get("probability") ;  		
	  	if (parameters.get("distributionIndex") != null)
	  		distributionIndex_ = (Double) parameters.get("distributionIndex") ;  
	}
	
	public void doMutation(double probability, Solution solution) throws JMException {
		try {
			if (PseudoRandom.randDouble() < probability) {
				int mutationPoint = PseudoRandom.randInt(1, solution.numberOfVariables() - 2);
				int ulimit = (int)solution.getDecisionVariables()[0].getValue();
				int blimit = (int)solution.getDecisionVariables()[solution.numberOfVariables() - 1].getValue();
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
	
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;
		doMutation(mutationProbability_, solution);
		return solution;
	}
}
