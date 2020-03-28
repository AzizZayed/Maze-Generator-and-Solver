package com.zayed.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MazeGenerator extends JPanel{

	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 1010;
	private static final int HEIGHT = 610;
	
	private Maze maze;

	public MazeGenerator() {
		canvasSetup();
		initialize();
	}

	private void canvasSetup() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

		this.setFocusable(true);

		this.addKeyListener(new KeyAdapter( ) {
			
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				
				if (code == KeyEvent.VK_R)
					initialize();
				
				repaint();
			}

			
		});
	}
	
	private void initialize() {
		maze = new Maze(WIDTH, HEIGHT, 10, true);
	}

	@Override
	public void paintComponent(Graphics g) {
		drawBackground(g);
		drawGame(g);
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	private void drawGame(Graphics g) {
		maze.draw(g);
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame("Depth-First Search Maze Generator");
		MazeGenerator game = new MazeGenerator();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.setDoubleBuffered(true);

		game.repaint();
	}

}
