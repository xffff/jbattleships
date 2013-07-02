/*
  Ship.java

  date: 07/06/13
  desc: Ship object records important data about each ship:
        ship coordinates: coords
        numhits: number of hits taken
        issunk: whether the ship is still floating
  author: michael murphy
 */

public class Ship {
	private int numhits; // any hits it might have taken from the player
	private int xsize; // xsize of grid
	private int ysize; // ysize of grid
	public boolean sunk; // gotta be public so we can tell whether this is sunk
							// or not
	public int[][] coords; // array of valid coords that would result in a hit
	public int size; // size of ship (dependant on type)

	public Ship(int insize, int xgridsize, int ygridsize) {
		xsize = xgridsize;
		ysize = ygridsize;
		size = insize;
		numhits = 0;
		coords = new int[xgridsize][ygridsize];
		sunk = false;
	}

	public int getHits() {
		System.out.println("Num hits on ship: ");
		return (numhits);
	}

	public void printCoords() {
		for (int i = 0; i < xsize; ++i) {
			for (int j = 0; j < ysize; ++j) {
				System.out.print(coords[j][i]);
			}

			System.out.println();
		}
	}

	public void checkShot(int x, int y) {
		if (coords[x][y] == 1) {
			System.out.println("Ship hit at " + x + ", " + y);
			numhits++;

			// Check if the Ship was sunk!
			if (numhits == size) {
				System.out.println("Ship has been sunk!");
				sunk = true;
			}
			System.out.println();
		}
	}
}
