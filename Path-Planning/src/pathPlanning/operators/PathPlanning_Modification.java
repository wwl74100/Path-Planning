package pathPlanning.operators;

import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import pathPlanning.problem.PathPlanning_Problem;

public class PathPlanning_Modification extends Operator {
	public PathPlanning_Modification(HashMap<String, Object> parameters) {
		super(parameters);
	}
	
	public void doMutation(Solution solution) throws JMException { 
		PathPlanning_Problem problem_ = ((PathPlanning_Problem)(solution.getProblem()));
		int numberOfVariables_ = solution.numberOfVariables();
		int mapColumn_ = problem_.getMapColumn();
		Variable[] gen = solution.getDecisionVariables();
		
		double[] x = new double[numberOfVariables_];
		double[] y = new double[numberOfVariables_];
		for(int i = 0; i < numberOfVariables_; ++ i) {
			y[i] = Math.floor(gen[i].getValue() / mapColumn_);
			x[i] = gen[i].getValue() % mapColumn_;
		}
		
		for(int i = 0; i < numberOfVariables_; ++ i) {
			for(int j = i + 2; j < numberOfVariables_; ++ j) {
				if(problem_.checkSegInObstacle((int)x[i], (int)y[i], (int)x[j], (int)y[j]) == false) {
					int temp = (int)solution.getDecisionVariables()[i].getValue();
					for(int k = i + 1; k < j; ++ k) {
						solution.getDecisionVariables()[k].setValue(temp);
						x[k] = x[i];
						y[k] = y[i];
					}
				}
			}
		}
	}
	
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;
		doMutation(solution);
		return solution; 
	}
}