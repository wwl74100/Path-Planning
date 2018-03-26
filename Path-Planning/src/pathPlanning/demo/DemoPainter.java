package pathPlanning.demo;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import nsgaii.pathPlanning.problem.NSGAII_PathPlanning_Problem;

public class DemoPainter extends JFrame {	
	public Problem problem_;
	public Solution solution_;
	public int mapRow_;
	public int mapColumn_;
	public char[][] mapMatrix_;
	
	public DemoPainter(Problem problem, Solution solution) { 
		this.setSize(1050, 1050); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setVisible(true); 
		
		problem_ = problem;
		solution_ = solution;
		mapRow_ = ((NSGAII_PathPlanning_Problem)problem_).getMapRow();
		mapColumn_ = ((NSGAII_PathPlanning_Problem)problem_).getMapColumn();
		mapMatrix_ = ((NSGAII_PathPlanning_Problem)problem_).getMapMatrix();
	} 
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);		
		paintMap(g);
		paintPath(g);
	}
	
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
		
		for(int i = 0; i < mapRow_; ++ i) {
			for(int j = 0; j < mapColumn_; ++ j) {
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
	
	public void paintPath(Graphics g) {
		int sizeX = mapColumn_ * 40 + 40, sizeY = mapRow_ * 40 + 80; 
		Variable[] gen = solution_.getDecisionVariables();
		int numberOfVariables_ = solution_.getProblem().getNumberOfVariables();
		
		int[] x = new int[numberOfVariables_];
		int[] y = new int[numberOfVariables_];
		try {
			g.setColor(Color.BLACK);
			
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