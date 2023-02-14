package lifegame;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

public class BoardModel {
	private int cols;
	private int rows;
	private int m = 0;
	private boolean[][] cells;
	private boolean[][] clones;
	private boolean[][][] history;
	private int[] graph;
	private int count;
	private ArrayList<BoardListener> listeners; //BoardListener型のオブジェクトを格納できる可変長配列
	
	public BoardModel(int c, int r) {
		cols = c;
		rows = r;
		cells = new boolean[cols][rows];
		history = new boolean[cols][rows][100];
		listeners = new ArrayList<BoardListener>();
		graph = new int[100];
	}
	
	synchronized public void addListener(BoardListener listener) {
		listeners.add(listener);
	}
	
	synchronized private void fireUpdate() {
		for(BoardListener listener: listeners) {
			listener.updated(this);
		}
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				if(this.isAlive(i, j)) {
					count++;
				}
			}
		}
		graph[m] = count;
	}
	
	public int graph_num(int m) {
		return graph[m];
	}
	
	synchronized private void addHistory(int num) {
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				history[i][j][num] = cells[i][j];
			}
		}
	}
	
	synchronized public int getCols() {
		return cols;
	}
	
	synchronized public int getRows() {
		return rows;
	}
	
	synchronized public boolean isAlive(int x, int y) {
		if(cells[y][x]) {
			return true;
		}else {
			return false;
		}
	}
	
	synchronized public void printForDebug() {
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				if(cells[i][j] == true) {
					System.out.print("*");
				}else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	synchronized public void changeCellState(int x, int y) {
		if(cells[y][x] == true) {
			cells[y][x] = false;
		} else {
			cells[y][x] = true;
		}
		this.fireUpdate();
		addHistory(m);
		m++;
	}
	
	synchronized public void next() {
		int count = 0;
		clones = new boolean[cols + 2][rows + 2];
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				clones[i + 1][j + 1] = cells[i][j];
			}
		}
		
		for(int i = 1; i < cols + 1; i++) {
			for(int j = 1; j < rows + 1; j++) {
				for(int k = -1; k <= 1; k++) {
					for(int l = -1; l <= 1; l++) {
						if(clones[i + k][j + l] == true) {
							++count;
						}
					}
				}
				if(clones[i][j] == true) {
					if((count != 3) && (count != 4)) {
						cells[i-1][j-1] = !clones[i][j];
					}else {
						cells[i-1][j-1] = clones[i][j];
					}
				}else{
					if(count == 3) {
						cells[i-1][j-1] = !clones[i][j];
					}else {
						cells[i-1][j-1] = clones[i][j];
					}
				}
				count = 0;
			}
		}
		addHistory(m);
		m++;
		this.fireUpdate();
	}
	
	synchronized public boolean isUndoable() {
		if(m != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	synchronized public boolean isNextable() {
		if(m != 100) {
			return true;
		}else {
			return false;
		}
	}
	
	synchronized public void undo() {
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				cells[i][j] = history[i][j][m-1];
			}
		}
		m--;
		this.fireUpdate();
	}
	
	public int get_M() {
		return m;
	}
}