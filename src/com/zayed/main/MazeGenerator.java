package com.zayed.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Zayed
 *
 */
public class MazeGenerator extends JPanel{

	private static final long serialVersionUID = 1L;

	// window dimensions 
	private static final int WIDTH = 1010;
	private static final int HEIGHT = 610;
	
	private Maze maze; // maze

	/**
	 * Constructor
	 */
	public MazeGenerator() {
		canvasSetup();
		initialize();
	}

	/**
	 * just to setup the canvas to our desired settings and sizes
	 */
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
	
	/**
	 * initialize all our game objects
	 */
	private void initialize() {
		maze = new Maze(WIDTH, HEIGHT, 10, true);
	}

	@Override
	public void paintComponent(Graphics g) {
		drawBackground(g);
		drawGame(g);
	}

	/**
	 * Draw the background
	 * @param g -> tool to draw
	 */
	private void drawBackground(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * draw the game
	 * @param g -> tool to draw
	 */
	private void drawGame(Graphics g) {
		maze.draw(g);
	}

	/**
	 * main method
	 * @param args
	 */
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
