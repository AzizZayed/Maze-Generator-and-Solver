package com.zayed.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Stack;

/**
 * 
 * @author Zayed
 *
 */
public class Maze {

	private int cols, rows; // dimensions in units of cells
	private int cellSize; // size of 1 cell in pixels
	private boolean solved = false; // solve the maze or not

	/**
	 * Cell State, as in the state every cell in the array is
	 * 
	 * @author Zayed
	 *
	 */
	private enum CellState {
		WALL, EMPTY, PATH, START, STOP, SOLUTION
	}

	private CellState[][] maze; // main data structure

	/**
	 * a cell in the maze
	 * 
	 * @author Zayed
	 *
	 */
	private class Cell {
		public int x, y; // position

		/**
		 * Constructor
		 * 
		 * @param x -> column position
		 * @param y -> row position
		 */
		public Cell(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	// start and stop positions of the maze
	private Cell startPosition;
	private Cell stopPosition;

	/**
	 * Constructor
	 * 
	 * @param w     -> width of canvas
	 * @param h     -> height of canvas
	 * @param size  -> size in pixels
	 * @param solve -> solve maze or no
	 */
	public Maze(int w, int h, int size, boolean solve) {
		cols = w / size;
		rows = h / size;

		// make them biggest odd number smaller or equal
		cols = cols - 1 + cols % 2;
		rows = rows - 1 + rows % 2;

		cellSize = size;
		solved = solve;

		startPosition = new Cell(0, 1);
		stopPosition = new Cell(cols - 1, rows - 2);

		configure();
		generate();
	}

	/**
	 * Constructor
	 * 
	 * @param w    -> width of canvas
	 * @param h    -> height of canvas
	 * @param size -> size in pixels
	 */
	public Maze(int w, int h, int size) {
		this(w, h, size, false);
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

		// start and stop
		maze[startPosition.x][startPosition.y] = CellState.START;
		maze[stopPosition.x][stopPosition.y] = CellState.STOP;

	}

	/**
	 * generate the maze
	 */
	private void generate() {
		Cell current, next;
		Stack<Cell> history = new Stack<Cell>();

		int nToVisit = (cols - 1) * (rows - 1) / 4;
		int nVisited = 1;

		current = new Cell(startPosition.x + 1, startPosition.y);
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

		if (solved)
			solve();

	}

	/**
	 * check around for another cell to go on
	 * 
	 * @param current -> current cell
	 * @param target  -> target cell state we're looking for
	 * @param dist    -> distance between current and desired cells
	 * @return
	 */
	private Cell checkNext(Cell current, CellState target, int dist) {

		final int n = 4; // number of neighbors

		// the options of cells
		Cell[] options = { new Cell(current.x, current.y + dist), new Cell(current.x, current.y - dist),
				new Cell(current.x + dist, current.y), new Cell(current.x - dist, current.y) };

		boolean[] goodIndices = new boolean[n]; // the options
		int nGood = 0; // number of good 

		for (int i = 0; i < n; i++) {
			Cell c = options[i];

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
		Cell current, next;
		Stack<Cell> history = new Stack<Cell>();

		current = new Cell(startPosition.x + 1, startPosition.y);
		maze[current.x][current.y] = CellState.SOLUTION;

		// while 
		while (current.x != stopPosition.x - 1 || current.y != stopPosition.y) {

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
	 * @param g -> tool to draw
	 */
	public void draw(Graphics g) {

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				Color color;
				CellState state = maze[i][j];

				int x = i * cellSize;
				int y = j * cellSize;

				switch (state) {
				case EMPTY:
					color = Color.WHITE;
					break;
				case PATH:
					color = Color.WHITE;
					break;
				case START:
					color = Color.GREEN;
					break;
				case STOP:
					color = Color.RED;
					break;
				case SOLUTION:
					color = new Color(0.0f, 0.0f, 1.0f, 0.5f);
					break;
				case WALL:
					color = Color.BLACK;
					break;
				default:
					color = Color.BLUE;
					break;
				}

				g.setColor(color);
				g.fillRect(x, y, cellSize, cellSize);

			}
		}

	}

}
