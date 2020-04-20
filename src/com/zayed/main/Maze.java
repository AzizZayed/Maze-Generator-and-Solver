package com.zayed.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Stack;

/**
 * maze class, generate and solve the mass
 * 
 * @author Zayed
 *
 */
public class Maze {

	private int cols, rows; // dimensions in units of cells
	private int cellSize; // size of 1 cell in pixels

	/**
	 * Cell State, as in the state every cell in the maze is
	 * <li>{@link #WALL}</li>
	 * <li>{@link #EMPTY}</li>
	 * <li>{@link #PATH}</li>
	 * <li>{@link #SOLUTION}</li>
	 * 
	 * @author Zayed
	 */
	private enum CellState {
		/**
		 * Wall: defines a wall cell (walls are black)
		 */
		WALL,

		/**
		 * Empty: Not a wall cell, a path nor a solution, placeholder for future paths
		 * (like null)
		 */
		EMPTY,

		/**
		 * Path: Represents a path cell (paths are white)
		 */
		PATH,

		/**
		 * Solution: Represents a solution cell (paths are blue)
		 */
		SOLUTION
	}

	private CellState[][] maze; // main data structure

	// start and stop positions of the maze
	private Point start;
	private Point stop;

	/**
	 * Constructor
	 * 
	 * @param w     -> width of canvas
	 * @param h     -> height of canvas
	 * @param size  -> size in pixels
	 * @param solve -> solve maze or no
	 */
	public Maze(int width, int height, int size) {
		size = Math.abs(size);
		
		cols = Math.abs(width) / size;
		rows = Math.abs(height) / size;

		// make them biggest odd number smaller or equal
		cols = cols - 1 + cols % 2;
		rows = rows - 1 + rows % 2;

		cellSize = size;

		start = new Point(0, 1);
		stop = new Point(cols - 1, rows - 2);

		configure();
		generate();
		solve();
	}

	/**
	 * initialize maze array
	 */
	private void configure() {

		maze = new CellState[cols][rows];

		// walls all around
		int i, j;

		for (j = 0; j < rows; j++) {
			maze[0][j] = CellState.WALL;
			maze[cols - 1][j] = CellState.WALL;
		}
		for (i = 0; i < cols; i++) {
			maze[i][0] = CellState.WALL;
			maze[i][rows - 1] = CellState.WALL;
		}

		// inside walls and empty cells
		for (i = 1; i < cols - 1; i += 2) {
			for (j = 1; j < rows - 1; j += 2) {
				maze[i][j] = CellState.EMPTY;
				maze[i + 1][j] = CellState.WALL;
				maze[i][j + 1] = CellState.WALL;
			}
		}
		for (i = 2; i < cols - 2; i += 2) {
			for (j = 2; j < rows - 2; j += 2) {
				maze[i][j] = CellState.WALL;
			}
		}
	}

	/**
	 * generate the maze
	 */
	private void generate() {
		Point current, next;
		Stack<Point> history = new Stack<Point>();

		int nToVisit = (cols - 1) * (rows - 1) / 4;
		int nVisited = 1;

		current = new Point(start.x + 1, start.y);
		maze[current.x][current.y] = CellState.PATH;

		while (nVisited < nToVisit) {

			next = checkNext(current, CellState.EMPTY, 2);
			if (next != null) {
				int x = (current.x + next.x) / 2;
				int y = (current.y + next.y) / 2;
				maze[x][y] = CellState.PATH;

				history.push(current);
				current = next;
				maze[current.x][current.y] = CellState.PATH;

				nVisited++;
			} else if (!history.empty()) {
				current = history.pop();
			}
		}
	}

	/**
	 * check around for another cell to go on
	 * 
	 * @param current -> current cell
	 * @param target  -> target cell state we're looking for
	 * @param dist    -> distance between current and desired cells
	 * @return
	 */
	private Point checkNext(Point current, CellState target, int dist) {

		final int n = 4; // number of neighbors

		// the options of cells
		Point[] options = { new Point(current.x, current.y + dist), new Point(current.x, current.y - dist),
				new Point(current.x + dist, current.y), new Point(current.x - dist, current.y) };

		boolean[] goodIndices = new boolean[n]; // the options
		int nGood = 0; // number of good

		for (int i = 0; i < n; i++) {
			Point c = options[i];

			boolean good = c.x >= 0 && c.x < cols && c.y >= 0 && c.y < rows && maze[c.x][c.y] == target;
			goodIndices[i] = good;

			if (good)
				nGood++;
		}

		if (nGood == 0)
			return null; // if there are no neighbors

		int rand = (int) (Math.random() * n);
		while (!goodIndices[rand]) {
			rand = (int) (Math.random() * n);
		}

		return options[rand]; // return the random neighbor

	}

	/**
	 * solve the maze
	 */
	private void solve() {
		Point current, next;
		Stack<Point> history = new Stack<Point>();

		current = new Point(start.x + 1, start.y);
		maze[current.x][current.y] = CellState.SOLUTION;

		while (current.x != stop.x - 1 || current.y != stop.y) {

			next = checkNext(current, CellState.PATH, 1);

			if (next != null) {
				history.push(current);
				current = next;
				maze[current.x][current.y] = CellState.SOLUTION;
			} else if (!history.empty()) {
				maze[current.x][current.y] = CellState.EMPTY;
				current = history.pop();
			}

		}
	}

	/**
	 * draw the maze
	 * 
	 * @param g -> tool to draw
	 */
	public void draw(Graphics g) {

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				CellState state = maze[i][j];
				Color color;

				int x = i * cellSize;
				int y = j * cellSize;

				switch (state) {
				case EMPTY:
					color = Color.WHITE;
					break;
				case PATH:
					color = Color.WHITE;
					break;
				case SOLUTION:
					color = new Color(0.0f, 0.0f, 1.0f, 0.3f);
					break;
				default:
					color = Color.BLACK; // wall
					break;
				}

				if (i == start.x && j == start.y)
					color = Color.GREEN;
				else if (i == stop.x && j == stop.y)
					color = Color.RED;

				g.setColor(color);
				g.fillRect(x, y, cellSize, cellSize);
			}
		}
	}
}
