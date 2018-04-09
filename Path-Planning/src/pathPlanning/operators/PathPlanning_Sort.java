package pathPlanning.operators;

import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.util.JMException;

/**
 * 
 * @author X.K.T
 * @class PathPlanning_Sort
 * @brief Sort the chromosome from small to large
 *
 */
public class PathPlanning_Sort extends Operator {
	/**
	 * @brief Constructor
	 * @param parameters: useless
	 */
	public PathPlanning_Sort(HashMap<String, Object> parameters) {
		super(parameters);
	}
	
	/**
	 * @brief Do sort operator
	 * @param solution
	 * @throws JMException
	 */
	public void doSort(Solution solution) throws JMException {
		int numberOfVariables_ = solution.numberOfVariables();
		
		try {
			for(int i = 1; i < numberOfVariables_ - 1; ++ i) {
				int value1 = (int)solution.getDecisionVariables()[i].getValue();
				for(int j = i; j < numberOfVariables_ - 1; ++ j) {
					int value2 = (int)solution.getDecisionVariables()[j].getValue();
					if(value1 > value2) {
						solution.getDecisionVariables()[i].setValue(value2);
						solution.getDecisionVariables()[j].setValue(value1);
						value1 = value2;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief Execute the sort operator
	 * @param object: Solution to sort
	 * @return Sorted solution
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;
		doSort(solution);
		return solution; 
	}
}
