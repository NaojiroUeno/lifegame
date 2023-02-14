package lifegame;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseEvent;

public class BoardView extends JPanel implements BoardListener, MouseListener, MouseMotionListener{
	private int cols;
	private int rows;
	public int tmp1;
	public int tmp2;
	private int[][] flag;
	private boolean[][] cells;
	BoardModel model;
	
	public BoardView(BoardModel m){
		cols = m.getCols();
		rows = m.getRows();
		this.model = m;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		flag = new int[cols][rows];
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				flag[i][j] = 0;
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		
		cells = new boolean[cols][rows];
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				if(model.isAlive(i, j)) {
					cells[i][j] = true;
				}
			}
		}

		tmp1 = this.getHeight() / cols;
		tmp2 = this.getHeight() / rows;
		for(int i = 0; i < cols; i++) {
			g.drawLine(tmp1 + i * tmp1, tmp2, tmp1 + i * tmp1, tmp2 * 12);
		}
		for(int i = 0; i < rows; i++) {
			g.drawLine(tmp1, tmp2 + i * tmp2, tmp1 * 12, tmp2 + i * tmp2);
		}
		
		for(int i = 0; i < cols - 1; i++) {
			for(int j = 0; j < rows - 1; j++) {
				if(cells[i][j]) {
					g.fillRect(tmp1 * (i + 1), tmp2 * (j + 1), tmp1, tmp2);
				}
			}
		}
	}
	
	@Override
	synchronized public void updated(BoardModel model) {
		this.repaint();
	}
	
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	synchronized public void mousePressed(MouseEvent e) {
		tmp1 = this.getHeight() / (cols + 1);
		tmp2 = this.getHeight() / (rows + 1);
		//System.out.println("Pressed: " + e.getX() + ", " + e.getY());
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				if((e.getX() > tmp1 + tmp1 * i ) && (e.getX() < tmp1 + tmp1 * (i + 1) ) ){
						 if((e.getY() > tmp2 + tmp2 * j) && (e.getY() < tmp2 + tmp2 * (j + 1))) {
							 model.changeCellState(i, j);
						 }
				}
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
	}
	synchronized public void mouseDragged(MouseEvent e) {
		//System.out.println("Dragged: " + e.getX() + ", " + e.getY());
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				if((e.getX() > tmp1 + tmp1 * i ) && (e.getX() < tmp1 + tmp1 * (i + 1) ) ){
					 if((e.getY() > tmp2 + tmp2 * j) && (e.getY() < tmp2 + tmp2 * (j + 1))) {
						 if(flag[i][j] == 0) {
							 model.changeCellState(i, j);
							 flag[i][j] = 1;
						 }
					 }
				}
			}
		}
	}
	public void mouseMoved(MouseEvent e) {
	}
}
