package pathPlanning.solution;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import nsgaii.pathPlanning.problem.NSGAII_PathPlanning_Problem;

public class PathPlanning_SolutionType extends SolutionType{
	/**
	 * Constructor
	 * @param problem: Problem being solved
	 */
	public PathPlanning_SolutionType(Problem problem) {
		super(problem);
	}
	
	/**
	 * create a new solution
	 */
	public Variable[] createVariables() {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		
		variables[0] = new Int(((NSGAII_PathPlanning_Problem)problem_).getMapStartPoint(),
				(int)problem_.getLowerLimit(0),
				(int)problem_.getUpperLimit(0));
		
		variables[problem_.getNumberOfVariables() - 1] = new Int(((NSGAII_PathPlanning_Problem)problem_).getMapTargetPoint(),
				(int)problem_.getLowerLimit(problem_.getNumberOfVariables() - 1),
				(int)problem_.getUpperLimit(problem_.getNumberOfVariables() - 1));
		
		for (int var = 1; var < problem_.getNumberOfVariables() - 1; var++) {
			variables[var] = new Int((int)problem_.getLowerLimit(var),
					(int)problem_.getUpperLimit(var));
			
			int flag = 1;
			while(flag == 1) {
				for(int i = 0; i < var; ++ i) {
					try {
						if(variables[i].getValue() == variables[var].getValue()) {
							flag = 1;
							variables[var] = new Int((int)problem_.getLowerLimit(var),
									(int)problem_.getUpperLimit(var));
							break;
						}
						else {
							flag = 0;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}			
			}
		}	
		return variables;
	}
}
