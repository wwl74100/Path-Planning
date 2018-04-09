package pathPlanning.operators;

import java.util.HashMap;

import jmetal.core.Solution;
import jmetal.encodings.variable.Int;
import jmetal.operators.crossover.Crossover;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * 
 * @author X.K.T
 * @class PathPlanning_TwoPointCrossover
 * @brief Two point crossover
 *
 */

public class PathPlanning_TwoPointCrossover extends Crossover {
	private Double crossoverProbability_ = null;
	
	/**
	 * @brief Constructor
	 * @param parameters: Crossover probability
	 */
	public PathPlanning_TwoPointCrossover(HashMap<String, Object> parameters) {
		super(parameters);
		if(parameters.get("probability") != null) {
			crossoverProbability_ = (Double) parameters.get("probability") ;
		}
	}
	
	/*public Solution[] doCrossover(double probability,
			  					  Solution parent1,
			  					  Solution parent2) throws JMException {
		Solution[] offSpring = new Solution[2];
		offSpring[0] = new Solution(parent1);
		offSpring[1] = new Solution(parent2);
		try {
			if (PseudoRandom.randDouble() < probability) {
				int crossoverPoint1 = PseudoRandom.randInt(1, parent1.numberOfVariables() - 2);
				int crossoverPoint2 = PseudoRandom.randInt(crossoverPoint1, parent1.numberOfVariables() - 2);
				
				//create offSpring1
				int valueX, flag = 1;
				for (int i = crossoverPoint1; i <= crossoverPoint2; ++ i) {
					valueX = (int) parent1.getDecisionVariables()[i].getValue();
					for(int j = 0; j < parent1.numberOfVariables(); ++ j) {
						if((j >= crossoverPoint1) && (j <= crossoverPoint2)) {
							continue;
						}
					
						if(valueX == (int) offSpring[1].getDecisionVariables()[j].getValue()) {
							flag = 0;
							break;
						}						
					}
					
					if(flag == 1) {
						offSpring[1].getDecisionVariables()[i].setValue(valueX);
					}
					else {
						break;
					}					
				}
				
				if(flag == 0) {
					int ulimit = (int)offSpring[1].getDecisionVariables()[crossoverPoint2 + 1].getValue();
					int blimit = (int)offSpring[1].getDecisionVariables()[crossoverPoint1 - 1].getValue();
					for (int i = crossoverPoint1; i <= crossoverPoint2; ++ i) {
						valueX = PseudoRandom.randInt(blimit + 1, ulimit - 1);
						int temp = 1;
						while(temp == 1) {
							for(int j = crossoverPoint1; j < i; ++ j) {
								if(valueX == (int)offSpring[1].getDecisionVariables()[j].getValue()) {
									temp = 1;
									valueX = PseudoRandom.randInt(blimit + 1, ulimit - 1);
									break;
								}
								else {
									temp = 0;
								}
							}			
						}
						offSpring[1].getDecisionVariables()[i].setValue(valueX);
					}
				}
				
				//create offspring0
				flag = 1;
				for (int i = crossoverPoint1; i <= crossoverPoint2; ++ i) {
					valueX = (int) parent2.getDecisionVariables()[i].getValue();
					for(int j = 0; j < parent1.numberOfVariables(); ++ j) {
						if((j >= crossoverPoint1) && (j <= crossoverPoint2)) {
							continue;
						}
					
						if(valueX == (int) offSpring[0].getDecisionVariables()[j].getValue()) {
							flag = 0;
							break;
						}						
					}
					
					if(flag == 1) {
						offSpring[0].getDecisionVariables()[i].setValue(valueX);
					}
					else {
						break;
					}					
				}
				
				if(flag == 0) {
					int ulimit = (int)offSpring[0].getDecisionVariables()[crossoverPoint2 + 1].getValue();
					int blimit = (int)offSpring[0].getDecisionVariables()[crossoverPoint1 - 1].getValue();
					for (int i = crossoverPoint1; i <= crossoverPoint2; ++ i) {
						valueX = PseudoRandom.randInt(blimit + 1, ulimit - 1);
						int temp = 1;
						while(temp == 1) {
							for(int j = crossoverPoint1; j < i; ++ j) {
								if(valueX == (int)offSpring[0].getDecisionVariables()[j].getValue()) {
									temp = 1;
									valueX = PseudoRandom.randInt(blimit + 1, ulimit - 1);
									break;
								}
								else {
									temp = 0;
								}
							}			
						}
						offSpring[0].getDecisionVariables()[i].setValue(valueX);
					}
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
	}*/
	
	/**
	 * @brief Do crossover
	 * @param probability: Crossover probability
	 * @param parent1
	 * @param parent2
	 * @return
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
				int crossoverPoint1 = PseudoRandom.randInt(1, parent1.numberOfVariables() - 2);
				int crossoverPoint2 = PseudoRandom.randInt(crossoverPoint1, parent1.numberOfVariables() - 2);
				int valueX1;
				int valueX2;
				for (int i = crossoverPoint1; i <= crossoverPoint2; i++) {
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
	 * @brief Execute the crossover operator
	 * @param object: Solution to crossover
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
