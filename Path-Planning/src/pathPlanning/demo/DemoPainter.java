package pathPlanning.demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import pathPlanning.problem.PathPlanning_Problem;

/**
 * 
 * @author X.K.T
 * @class DemoPainter
 * @brief Paint the map and the path
 *
 */
public class DemoPainter extends JFrame {	
	public Problem problem_;
	public Solution solution1_ = null;
	public Solution solution2_ = null;
	public int mapRow_;
	public int mapColumn_;
	public char[][] mapMatrix_;
	
	/**
	 * @brief Constructor
	 * @param title: Frame title
	 * @param problem: Path-planning problem
	 */
	public DemoPainter(String title, Problem problem) {
		this.setSize(1050, 1050); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setVisible(true); 
		setTitle(title);
		
		problem_ = problem;
		mapRow_ = ((PathPlanning_Problem)problem_).getMapRow();
		mapColumn_ = ((PathPlanning_Problem)problem_).getMapColumn();
		mapMatrix_ = ((PathPlanning_Problem)problem_).getMapMatrix();
	}
	
	/**
	 * @brief Constructor
	 * @param title: Frame title
	 * @param problem: Path-planning problem
	 * @param solution: Path to paint
	 */
	public DemoPainter(String title, Problem problem, Solution solution) { 
		this.setSize(1050, 1050); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setVisible(true); 
		setTitle(title);
		
		problem_ = problem;
		solution1_ = solution;
		mapRow_ = ((PathPlanning_Problem)problem_).getMapRow();
		mapColumn_ = ((PathPlanning_Problem)problem_).getMapColumn();
		mapMatrix_ = ((PathPlanning_Problem)problem_).getMapMatrix();
	} 
	
	/**
	 * @brief Constructor
	 * @param title
	 * @param problem
	 * @param solution1
	 * @param solution2
	 */
	public DemoPainter(String title, Problem problem, Solution solution1, Solution solution2) {
		this.setSize(1050, 1050); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setVisible(true); 
		setTitle(title);
		
		problem_ = problem;
		solution1_ = solution1;
		solution2_ = solution2;
		mapRow_ = ((PathPlanning_Problem)problem_).getMapRow();
		mapColumn_ = ((PathPlanning_Problem)problem_).getMapColumn();
		mapMatrix_ = ((PathPlanning_Problem)problem_).getMapMatrix();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);	
		paintMap(g);
		if(solution1_ != null) {
			g.setColor(Color.RED);
			paintPath(g, solution1_);
		}
		if(solution2_ != null) {
			g.setColor(Color.BLUE);
			paintPath(g, solution2_);
		}
	}
	
	/**
	 * @brief Paint the map
	 * @param g
	 */
	public void paintMap(Graphics g) {	
		int sizeX = mapColumn_ * 40 + 40, sizeY = mapRow_ * 40 + 80; 
		this.setSize(sizeX, sizeY);
		
		g.setColor(Color.BLACK);
		for(int i = 0; i <= mapColumn_; ++ i) {
			g.drawLine(20 + i * 40, 60, 20 + i * 40, sizeY - 20);
		}
		
		for(int i = 0; i <= mapRow_; ++ i) {
			g.drawLine(20, 60 + i * 40, sizeX - 20, 60 + i * 40);
		}
		
		Font font = new Font("ËÎÌו", Font.BOLD, 15);
		for(int i = 0; i < mapRow_; ++ i) {
			for(int j = 0; j < mapColumn_; ++ j) {
				g.setColor(Color.black);
				g.setFont(font);
				g.drawString(String.valueOf(i * mapRow_ + j), j * 40 + 30, sizeY - 40 - i * 40);
				if(mapMatrix_[i][j] == '1') {
					g.setColor(Color.DARK_GRAY);
					g.fillRect(j * 40 + 20, sizeY - 20 - (i + 1) * 40, 40, 40);
				}
				
				if(mapMatrix_[i][j] == '8') {
					g.setColor(Color.YELLOW);
					g.fillRect(j * 40 + 20, sizeY - 20 - (i + 1) * 40, 40, 40);
				}
				
				if(mapMatrix_[i][j] == '9') {
					g.setColor(Color.GREEN);
					g.fillRect(j * 40 + 20, sizeY - 20 - (i + 1) * 40, 40, 40);
				}
			}
		}
	}
	
	/**
	 * @brief Paint the path
	 * @param g
	 * @param solution: Path to paint
	 */
	public void paintPath(Graphics g, Solution solution) {
		Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2.5f));
        
		int sizeX = mapColumn_ * 40 + 40, sizeY = mapRow_ * 40 + 80; 
		Variable[] gen = solution.getDecisionVariables();
		int numberOfVariables_ = solution.getProblem().getNumberOfVariables();
		
		int[] x = new int[numberOfVariables_];
		int[] y = new int[numberOfVariables_];
		try {
			y[0] = (int)Math.floor(gen[0].getValue() / mapColumn_);
			x[0] = (int)gen[0].getValue() % mapColumn_;			
			g.fillOval(x[0] * 40 + 35, sizeY - 45 - y[0] * 40, 10, 10);
			
			for(int i = 1; i < numberOfVariables_; ++ i) {
				y[i] = (int)Math.floor(gen[i].getValue() / mapColumn_);
				x[i] = (int)gen[i].getValue() % mapColumn_;
				
				g.fillOval(x[i] * 40 + 35, sizeY - 45 - y[i] * 40, 10, 10);			
				g.drawLine(x[i - 1] * 40 + 40, sizeY - 40 - y[i - 1] * 40,
							x[i] * 40 + 40, sizeY - 40 - y[i] * 40);
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
}