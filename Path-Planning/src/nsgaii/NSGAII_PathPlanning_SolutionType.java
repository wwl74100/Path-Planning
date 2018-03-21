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
	
	public Variable[] createVariables() {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];

		for (int var = 0; var < problem_.getNumberOfVariables(); var++)
			variables[var] = new Int((int)problem_.getLowerLimit(var),
					(int)problem_.getUpperLimit(var));    

		return variables ;
	}
}
