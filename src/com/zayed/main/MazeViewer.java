package com.zayed.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * main class, handles input and drawing
 * 
 * @author Zayed
 *
 */
public class MazeViewer extends JPanel {

	private static final long serialVersionUID = 1L;

	// window dimensions
	private static final int WIDTH = 510; // width in pixels
	private static final int HEIGHT = 510; // height in pixels

	private Maze maze; // maze

	/**
	 * Constructor
	 */
	public MazeViewer() {
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

		this.addKeyListener(new KeyAdapter() {

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
		maze = new Maze(WIDTH, HEIGHT, 10);
	}

	@Override
	public void paintComponent(Graphics g) {
		// Draw the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		maze.draw(g); // draw the game
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		JFrame frame = new JFrame("Backtracker Maze Generator");
		MazeViewer game = new MazeViewer();

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
