package pathPlanning.solution;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import pathPlanning.problem.PathPlanning_Problem;

/**
 * @author X.K.T
 * @class  PathPlanning_SolutionType
 * @brief  Class to define the SolutionType of path-planning problem
 */
public class PathPlanning_SolutionType extends SolutionType{
	/**
	 * @brief Constructor
	 * @param problem: Problem being solved
	 */
	public PathPlanning_SolutionType(Problem problem) {
		super(problem);
	}
	
	/**
	 * @brief Create a new solution
	 */
	public Variable[] createVariables() {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		
		//the first and last number of the chromosome are fixed
		variables[0] = new Int(((PathPlanning_Problem)problem_).getMapStartPoint(),
				(int)problem_.getLowerLimit(0),
				(int)problem_.getUpperLimit(0));
		
		variables[problem_.getNumberOfVariables() - 1] = new Int(((PathPlanning_Problem)problem_).getMapTargetPoint(),
				(int)problem_.getLowerLimit(problem_.getNumberOfVariables() - 1),
				(int)problem_.getUpperLimit(problem_.getNumberOfVariables() - 1));
		
		//create other numbers of the chromosome
		try {
			for (int var = 1; var < problem_.getNumberOfVariables() - 1; var++) {
				variables[var] = new Int((int)variables[0].getValue() + 1,
						(int)variables[problem_.getNumberOfVariables() - 1].getValue() - 1);
			
				int flag = 1;
				while(flag == 1) {
					for(int i = 0; i < var; ++ i) {
						if(variables[i].getValue() == variables[var].getValue()) {
							flag = 1;
							variables[var] = new Int((int)variables[0].getValue() + 1,
									(int)variables[problem_.getNumberOfVariables() - 1].getValue() - 1);
							break;
						}
						else {
							flag = 0;
						}
					}			
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return variables;
	}
}
