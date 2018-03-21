package nsgaii;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.ArrayInt;
import jmetal.encodings.variable.Int;

public class NSGAII_PathPlanning_SolutionType extends SolutionType {
	
	/**
	 * Constructor
	 * @param problem: Problem being solved
	 */
	public NSGAII_PathPlanning_SolutionType(Problem problem) {
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
		
		for (int var = 1; var < problem_.getNumberOfVariables() - 1; var++)
			variables[var] = new Int((int)problem_.getLowerLimit(var),
					(int)problem_.getUpperLimit(var));    

		return variables ;
	}
}
