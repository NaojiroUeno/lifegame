package lifegame;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;

import java.awt.event.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.BasicStroke;
import org.jfree.chart.ChartFrame;



public class Main extends JFrame implements Runnable, ActionListener, BoardListener {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Main());
	}
	
	JButton next;
	JButton undo;
	JButton newgame;
	JButton auto;
	JButton graph;
	BoardModel model = new BoardModel(12, 12);
	private int cols = model.getCols();
	private int rows = model.getRows();
	private boolean flag;
	private int count = 0;
	private int[][] cells;
	
	public void run() {
		JFrame frame = new JFrame(); //ウィンドウ作成
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //ウィンドウを閉じたときの処理
		
		JPanel base = new JPanel(); //ウィンドウの背景
		frame.setContentPane(base);
		frame.setTitle("lifegame");
		base.setPreferredSize(new Dimension(400, 400));
		frame.setMinimumSize(new Dimension(300, 200));
		
		frame.pack(); //レイアウト
		frame.setVisible(true); //表示する	
		
		base.setLayout(new BorderLayout());
		BoardView view = new BoardView(model);
		base.add(view, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		base.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout());
		
		model.addListener(view);  //ここでBoardViewを追加する
		model.addListener(this);
		
		next = new JButton("next");
		next.addActionListener(this);
		buttonPanel.add(next);
		undo = new JButton("undo");
		undo.addActionListener(this);
		buttonPanel.add(undo);
		newgame = new JButton("newgame");
		newgame.addActionListener(this);
		buttonPanel.add(newgame);
		auto = new JButton("auto");
		auto.addActionListener(this);
		buttonPanel.add(auto);
		graph = new JButton("graph");
		graph.addActionListener(this);
		buttonPanel.add(graph);
		
		////////////////////
		 
	    
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == next) {
			//System.out.println("test");
			model.next();
		}else if(e.getSource() == undo) {
			//System.out.println("undo");
			model.undo();
		}else if(e.getSource() == newgame) {
			Main main = new Main();
			main.run();
		}else if(e.getSource() == auto) {
			MultiThread mt = new MultiThread(model);
			Thread thread = new Thread(mt);
			thread.start();
		}else if(e.getSource() == graph) {
			DefaultCategoryDataset ds_cat = new DefaultCategoryDataset();
            for(int i = 0; i < model.get_M(); i++) {
            	ds_cat.setValue(model.graph_num(i), "cells", "i");
            }
            JFreeChart chart=ChartFactory.createLineChart("CELLS NUMBER", "generation", "cells", ds_cat, PlotOrientation.VERTICAL, true, false, false);
            chart.getCategoryPlot().getRenderer().setSeriesStroke(0, new BasicStroke(4));
            chart.getCategoryPlot().getRenderer().setSeriesStroke(1, new BasicStroke(4));
            chart.getCategoryPlot().getRenderer().setSeriesStroke(2, new BasicStroke(4));

            ChartFrame frame1 = new ChartFrame("Rainbow Planet Sample Chart(^o^)", chart);
            frame1.setVisible(true);
            frame1.setSize(800,400);
		}
	}
	
	@Override
	public void updated(BoardModel model) {
		if(model.isUndoable()) {
			undo.setEnabled(true);
		}else {
			undo.setEnabled(false);
		}
		if(model.isNextable()) {
			next.setEnabled(true);
		}else {
			next.setEnabled(false);
		}
	}
}
