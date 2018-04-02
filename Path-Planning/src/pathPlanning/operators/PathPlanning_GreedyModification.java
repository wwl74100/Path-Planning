package pathPlanning.operators;

import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import pathPlanning.problem.PathPlanning_Problem;

public class PathPlanning_GreedyModification extends Operator {
	public PathPlanning_GreedyModification(HashMap<String, Object> parameters) {
		super(parameters);
	}
	
	public void doModification(Solution solution) throws JMException {
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
		
		boolean canFind = true;
		while(canFind == true) {
			canFind = false;
			int index = 0;
			double maxLength = 0.0;
			for(int i = 0; i < numberOfVariables_ - 2; ++ i) {
				if(problem_.checkSegInObstacle((int)x[i], (int)y[i], (int)x[i + 2], (int)y[i + 2]) == 0) {
					double length = problem_.getLengthOfSeg((int)x[i], (int)y[i], (int)x[i + 1], (int)y[i + 1])
									+ problem_.getLengthOfSeg((int)x[i + 1], (int)y[i + 1], (int)x[i + 2], (int)y[i + 2])
									- problem_.getLengthOfSeg((int)x[i], (int)y[i], (int)x[i + 2], (int)y[i + 2]);
					if(length > maxLength) {
						index = i;
						canFind = true;
						maxLength = length;
					}
				}
			}
			if(canFind == true) {
				int temp = (int)solution.getDecisionVariables()[index].getValue();
				solution.getDecisionVariables()[index + 1].setValue(temp);
				x[index + 1] = x[index];
				y[index + 1] = y[index];
			}
		}
	}
	
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution)object;
		doModification(solution);
		return solution; 
	}
}
