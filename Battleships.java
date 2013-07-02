/*
  battleships.java

  date: 07/06/13
  desc: simple implementation of battleships
  author: michael murphy
 */

import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 This is the main class which places the ships on the grid,
 takes commands from the player, and deals with the rest of
 the game.  I decided to make it fire back because it's more fun
 that way
 */
public class Battleships {
	public static int xsize = 10;
	public static int ysize = 10;
	public static Random random = new Random();

	/*
	 * Entry to the program is here
	 */
	public static void main(String args[]) {
		int battleshipsize = 5;
		int destroyersize = 4;
		int[][] aigrid = new int[xsize][ysize]; // the AI's grid
		int[][] ugrid = new int[xsize][ysize]; // the user's grid

		// I declare the destroyers as separate vars instead of
		// using an array because with only 2 instances, it seems
		// much clearer this way
		Ship aibattleship = new Ship(battleshipsize, xsize, ysize);
		Ship aidestroyera = new Ship(destroyersize, xsize, ysize);
		Ship aidestroyerb = new Ship(destroyersize, xsize, ysize);

		// it's more fun if the AI fires back
		Ship ubattleship = new Ship(battleshipsize, xsize, ysize);
		Ship udestroyera = new Ship(destroyersize, xsize, ysize);
		Ship udestroyerb = new Ship(destroyersize, xsize, ysize);

		// User intro and instructions:
		System.out.println("*************************************************************");
		System.out.println("            Battleships v0.1: Michael Murphy");
		System.out.println(" To take a shot: use coordinates in the format, A0,B1 ... J9");
		System.out.println("         There are three enemy ships to destroy...");
		System.out.println("  You don't get to choose where your ships go... maybe later");
		System.out.println("                  To quit, type 'quit'");
		System.out.println("*************************************************************");
		System.out.println();

		// This will modify the global grid
		// and also create an individual grid
		// for each ship for the AI and for the user
		System.out.println("Placing ships on a " + xsize + " x " + ysize
				+ " grid");

		System.out.println("Placing AI Ship 1... ");
		placeShip(aibattleship, aigrid);
		System.out.println("Placing AI Ship 2... ");
		placeShip(aidestroyera, aigrid);
		System.out.println("Placing AI Ship 3... ");
		placeShip(aidestroyerb, aigrid);

		System.out.println("Placing User Ship 1... ");
		placeShip(ubattleship, ugrid);
		System.out.println("Placing User Ship 2... ");
		placeShip(udestroyera, ugrid);
		System.out.println("Placing User Ship 3... ");
		placeShip(udestroyerb, ugrid);
		System.out.println();

		// output the users grid
		System.out.println("The open seas...");
		printGrid(ugrid);

		System.out.println();

		// the game will run in another method
		// it will terminate on 'quit' or when the user wins
		runGame(aibattleship, aidestroyera, aidestroyerb, ubattleship,
				udestroyera, udestroyerb);
	}

	/*
	 * The game mechanics run within the while loop in this method if/else tree
	 * could be cleaned up but prevents unwanted input still susceptible to
	 * @&£@^ throwing SystemFormatException
	 */
	static void runGame(Ship aibattleship, Ship aidestroyera,
			Ship aidestroyerb, Ship ubattleship, Ship udestroyera,
			Ship udestroyerb) {
		boolean playing = true; // this will be false when the user wins... yay
		String userinput = "";
		int aix;
		int aiy;
		int userx;
		int usery;

		while (playing) {
			System.out.print("Take a shot: ");

			try {
				BufferedReader bufferRead = new BufferedReader(
						new InputStreamReader(System.in));
				userinput = bufferRead.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// allow user to quit cleanly if they've had enough
			if (userinput != "quit") {
				// deal with bad input cleanly
				if (userinput.length() == 2 || userinput.length() == 3) {

					// User input is in the form A0-J9
					// A = 65 so I can just split the string
					// and -65 to get a 0-10 value for x
					userx = (byte) (userinput.charAt(0)) - 65; // should be the
																// ASCII val
					usery = Integer.parseInt(userinput.substring(1));

					// AI chooses random coords... randomly...
					aix = random.nextInt(xsize);
					aiy = random.nextInt(ysize);

					// no ArrayOutOfBounds exceptions please
					if (userx >= 0 && userx < xsize && usery >= 0
							&& usery < ysize) {
						/*
						 * This part could be split up into methods but it seems
						 * small enough to leave it as it is.
						 */

						System.out.println("You shot at " + userx + ", "
								+ usery);

						// Check to see if any AI ships have been shot
						aibattleship.checkShot(userx, usery);
						aidestroyera.checkShot(userx, usery);
						aidestroyerb.checkShot(userx, usery);

						if (aibattleship.sunk && aidestroyera.sunk
								&& aidestroyerb.sunk) {
							System.out.println("Papaparapapaa, yev wen... ");
							playing = false;
						}

						System.out.println("AI shot at " + aix + ", " + aiy);

						// Check to see if any User ships have been shot
						ubattleship.checkShot(aix, aiy);
						udestroyera.checkShot(aix, aiy);
						udestroyerb.checkShot(aix, aiy);

						System.out.println();

						if (ubattleship.sunk && udestroyera.sunk
								&& udestroyerb.sunk) {
							System.out
									.println("You lost to a random number generator.");
							playing = false;
						}
					} else {
						System.out
								.println("Please enter a value between A0 and J9");
					}
				} else {
					System.out
							.println("Please enter your value with the form A0, B1, ... J9 etc.");
				}
			} else {
				System.out.println("Thanks for playing... ");
				playing = false;
			}
		}
	}

	/*
	 * Debug method to print the grid out Cheat mode...
	 */
	static void printGrid(int[][] grid) {
		for (int i = 0; i < xsize; ++i) {
			for (int j = 0; j < ysize; ++j) {
				System.out.print(grid[j][i]);
			}
			System.out.println();
		}
	}

	/*
	 * Pick a random number and determine whether we can fit in a ship
	 * horizontally, or vertically. Ships can't overlap.
	 */
	static void placeShip(Ship ship, int[][] grid) {
		int orientation = random.nextInt(2);
		int xrandomcoord = random.nextInt(xsize);
		int yrandomcoord = random.nextInt(ysize);

		int shipsize = ship.size;

		// do this until we've placed a ship
		for (;;) {
			// first decide whether we check horizontally or vertically and
			// choose the point
			orientation = random.nextInt(2);
			xrandomcoord = random.nextInt(xsize);
			yrandomcoord = random.nextInt(ysize);

			if (orientation == 0) {
				// drawShip returns true when it's done
				if (drawShip(ship, orientation, xrandomcoord, xsize, shipsize,
						grid))
					break;
			} else {
				if (drawShip(ship, orientation, yrandomcoord, ysize, shipsize,
						grid))
					break;
			}
		}
	}

	/*
	 * Draws a ship on the map This method is here to clean up some code-reuse.
	 * It is called by placeShip.
	 * 
	 * returns true when a ship may be placed succesfully or if a collision
	 * happens and the loop must be restarted.
	 * 
	 * returns false if a ship may not be placed at the random coordinate with
	 * this orientation.
	 */
	static boolean drawShip(Ship ship, int orientation, int randomcoord,
			int size, int shipsize, int[][] grid) {
		int i = 0;
		int j = 0;
		int k = 0;

		// the same thing with the vertical dimension
		if (randomcoord > shipsize || randomcoord < size - shipsize) {
			for (i = 0; i < shipsize; i++) {
				// check for orientation and draw accordingly
				if (orientation == 0) {
					// check for a collision
					if (grid[i][randomcoord] != 1) {
						grid[i][randomcoord] = 1;
						ship.coords[i][randomcoord] = 1;
						j++;
					} else {
						// have to mark the cells 0-j back to 0 now
						for (k = 0; k < j; k++) {
							grid[k][randomcoord] = 0;
							ship.coords[k][randomcoord] = 0;
						}

						// try again somewhere else
						placeShip(ship, grid);
						return true;
					}
				} else {
					// see above for comments
					if (grid[randomcoord][i] != 1) {
						grid[randomcoord][i] = 1;
						ship.coords[randomcoord][i] = 1;
						j++;
					} else {
						for (k = 0; k < j; k++) {
							grid[randomcoord][k] = 0;
							ship.coords[randomcoord][k] = 0;
						}

						placeShip(ship, grid);
						return true;
					}
				}
			}
			return true;
		}
		return false;
	}
}
