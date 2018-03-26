package nsgaii.pathPlanning.operators;

import java.util.HashMap;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.operators.crossover.Crossover;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class NSGAII_PathPlanning_SinglePointCrossover extends Crossover {
	private Double crossoverProbability_ = null;
	
	public NSGAII_PathPlanning_SinglePointCrossover(HashMap<String, Object> parameters) {
		super(parameters);
		if(parameters.get("probability") != null) {
			crossoverProbability_ = (Double) parameters.get("probability") ;
		}
	}
	
	/**
	 * Perform the crossover operation.
	 * @param probability Crossover probability
	 * @param parent1 The first parent
	 * @param parent2 The second parent   
	 * @return An array containig the two offsprings
	 * @throws JMException
	 */
	public Solution[] doCrossover(double probability,
								  Solution parent1,
								  Solution parent2) throws JMException {
		Solution[] offSpring = new Solution[2];
		offSpring[0] = new Solution(parent1);
		offSpring[1] = new Solution(parent2);
		try {
			if (PseudoRandom.randDouble() < probability) {
				int crossoverPoint = PseudoRandom.randInt(0, parent1.numberOfVariables() - 1);
				int valueX1;
				int valueX2;
				for (int i = crossoverPoint; i < parent1.numberOfVariables(); i++) {
		            valueX1 = (int) parent1.getDecisionVariables()[i].getValue();
		            valueX2 = (int) parent2.getDecisionVariables()[i].getValue();
		            offSpring[0].getDecisionVariables()[i].setValue(valueX2);
		            offSpring[1].getDecisionVariables()[i].setValue(valueX1);
		        } 
			}
		} catch (ClassCastException e1) {
			Configuration.logger_.severe("SinglePointCrossover.doCrossover: Cannot perfom " +
		              					 "SinglePointCrossover");
		    Class cls = java.lang.String.class;
		    String name = cls.getName();
		    throw new JMException("Exception in " + name + ".doCrossover()");
		}
		return offSpring;
	}
	
	/**
	 * Executes the operation
	 * @param object An object containing an array of two solutions
	 * @return An object containing an array with the offSprings
	 * @throws JMException
	 */
	public Object execute(Object object) throws JMException {
		Solution[] parents = (Solution[]) object;
		Solution[] offSpring;
	    offSpring = doCrossover(crossoverProbability_,
	            parents[0],
	            parents[1]);

	    //-> Update the offSpring solutions
	    for (int i = 0; i < offSpring.length; i++) {
	      offSpring[i].setCrowdingDistance(0.0);
	      offSpring[i].setRank(0);
	    }
	    return offSpring;
	}
}
