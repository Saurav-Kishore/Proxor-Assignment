import java.io.*;
import java.util.*;

/*
 * The class pertaining to Canvas and
 * the functionalities to draw on it.
 */
class Drawing
{
	// 2D Matrix variable storing our Canvas
	private char[][] canvas;

	// Starting point for the class
	public void start()
	{
		Scanner sc = new Scanner(System.in);
		int x1, y1, x2, y2;
		
		/*
		 * The while loop takes input and
		 * displays the output based on input 
		 * type until the Quit command is issued.
		 */ 
		label:
		while(true)
		{
			System.out.print("enter command: ");
			String input[] = sc.nextLine().split(" ");
			char typeOfInput = input[0].charAt(0);
			switch(typeOfInput)
			{
				// Case for creating a new canvas
				case 'C':
					int row = Integer.parseInt(input[2]);
					int col = Integer.parseInt(input[1]);
					createCanvas(row, col);
					break;
				
				// Case for drawing a horizontal
				// or vertical line on the canvas
				case 'L':
					x1 = Integer.parseInt(input[1]);
					y1 = Integer.parseInt(input[2]);
					x2 = Integer.parseInt(input[3]);
					y2 = Integer.parseInt(input[4]);
					drawLine(x1, y1, x2, y2);
					break;
				
				// Case for drawing a Rectangle around
				// the bounding box coordinates
				case 'R':
					x1 = Integer.parseInt(input[1]);
					y1 = Integer.parseInt(input[2]);
					x2 = Integer.parseInt(input[3]);
					y2 = Integer.parseInt(input[4]);
					drawRect(x1, y1, x2, y2);
					break;

				// Case to fill the bounded space
				// staring at point x, y with color c
				case 'B':
					int x = Integer.parseInt(input[1]);
					int y = Integer.parseInt(input[2]);
					char c = input[3].charAt(0);
					colourFill(x, y, c);
					break;

				// Quits the Program
				case 'Q':
					break label;

				// Default Case: Retake the input
				default:
					break;
			}

			// Display the current view of the Canvas
			displayCanvas();
		}
	}

	/*
	 * Creates a new Canvas with specified
	 * boundaries.
	 */
	private void createCanvas(int row, int col)
	{
		canvas = new char[row + 2][col + 2];
		for(int i = 0; i < canvas.length; i++)
		{
			for(int j = 0; j < canvas[i].length; j++)
			{
				canvas[i][j] = ' ';
			}
		}

		for(int i = 0; i < col + 2; i++)
		{
			canvas[0][i] = '-';
			canvas[row + 1][i] = '-';
		}

		for(int j = 1; j < row + 1; j++)
		{
			canvas[j][0] = '|';
			canvas[j][col + 1] = '|';
		}
	}

	/*
	 * Draws a vertical or horizontal line based
	 * on the two points (x1,y1) & (x2,y2). If the points 
	 * are not situated horizontally or vertically
	 * then do nothing on the canvas.
	 */
	private void drawLine(int x1, int y1, int x2, int y2)
	{
		if(y1 == y2)
		{
			if(x1 > x2)
			{
				int temp = x1;
				x1 = x2;
				x2 = temp;
			}

			for(int col = x1; col <= x2; col++)
			{
				canvas[y1][col] = 'x';
			}
		}
		else if(x1 == x2)
		{
			if(y1 > y2)
			{
				int temp = y1;
				y1 = y2;
				y2 = temp;
			}

			for(int row = y1; row <= y2; row++)
			{
				canvas[row][x1] = 'x';
			}
		}
	}

	/*
	 * Draws a rectangle based on the two points
	 * (x1,y1) & (x2,y2) as boinding box coordinates.
	 */
	private void drawRect(int x1, int y1, int x2, int y2)
	{
		int row = canvas.length - 2;
		int col = canvas[0].length - 2;
		if(x1 > x2)
		{
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}

		if(y1 > y2)
		{
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}

		if(x1 < 1 || x2 > col || y1 < 1 || y2 > row)
			return;

		for(int i = x1; i <= x2; i++)
		{
			canvas[y1][i] = 'x';
			canvas[y2][i] = 'x';
		}

		for(int j = y1; j <= y2; j++)
		{
			canvas[j][x1] = 'x';
			canvas[j][x2] = 'x';
		}

	}

	/*
	 * Fills the entire area connected to
	 * (x, y) with "colour" c. Uses Breadth
	 * First Search strategy for filling
	 * the colour.
	 */
	private void colourFill(int x, int y, char c)
	{
		int row = canvas.length - 2;
		int col = canvas[0].length - 2;
		if(x < 1 || x > col || y < 1 || y > row || canvas[y][x] == 'x')
			return;

		boolean[][] visited = new boolean[row+2][col+2];

		Queue<Point> q = new LinkedList<>();
		q.add(new Point(x, y));
		visited[y][x] = true;
		canvas[y][x] = c;
		while(!q.isEmpty())
		{
			int x_off[] = {1, -1, 0, 0, 1, 1, -1, -1};
			int y_off[] = {0, 0, 1, -1, 1, -1, 1, -1};
			Point p = q.remove();
			int x1 = p.x;
			int y1 = p.y;
			for(int i = 0; i < 8; i++)
			{
				int new_x = x1 + x_off[i];
				int new_y = y1 + y_off[i];
				if(isSafe(new_x, new_y, visited))
				{
					q.add(new Point(new_x, new_y));
					visited[new_y][new_x] = true;
					canvas[new_y][new_x] = c;
				}
			}
		}
	}

	/*
	 * Utility method for the colourFill method.
	 */
	private boolean isSafe(int x, int y, boolean visited[][])
	{
		int row = canvas.length - 2;
		int col = canvas[0].length - 2;
		if(x < 1 || x > col || y < 1 || y > row || canvas[y][x] == 'x' || visited[y][x] == true)
			return false;

		return true;
	}

	/*
	 * Prints the entire Canvas on the console
	 * output in the desired format.
	 */
	private void displayCanvas()
	{
		for(int i = 0; i < canvas.length; i++)
		{
			for(int j = 0; j < canvas[i].length; j++)
			{
				System.out.print(canvas[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
}