
public class Hex implements BoardGame {

	private int[][] board; // 2D Board. 0 - empty, 1 - Player 1, 2 - Player 2 
	                      
	private int n1, n2; // height and width of board
	
	private WeightedQuickUnionUF wqu; // Union Find data structure to keep track
										// of unions and calculate winner
	
	private int currentPlayer; // Current player in the game, initialised to 1
    private int turnCount; //Keep check of turn count so not checking winner when we don't have to

    //Special sites for checking percolation
    //Player 1 takes EAST -> WEST
    private int EAST;
    private int WEST;
    //Player 2 takes NORTH -> SOUTH
    private int NORTH;
    private int SOUTH;

    public Hex(int n1, int n2) // create N-by-N grid, with all sites blocked
	{
        this.n1 = n1;
        this.n2 = n2;
        turnCount = 0;
        currentPlayer = 1;
        // TODO: Create instance of board
        board = new int[n1][n2];
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board.length; j++)
                board[i][j] = 0;

        // TODO: Create instance WeightedQuickUnionUF class
        // (n1 * n2) + 4 because we need an extra 4 to check who's won
        wqu = new WeightedQuickUnionUF((n1 * n2) + 4);
        EAST = board.length - 1;
        WEST = board.length - 2;
        NORTH = board.length - 3;
        SOUTH = board.length - 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoardGame#takeTurn(int, int)
	 */
	@Override
	public void takeTurn(int x, int y) {

        //Loop to make sure player gets to pick a valid coordinate
        //and that choice is occupied
        while (true) {
            boolean valid = validCoords(x, y);
            boolean open = isOpen(x, y);

            if (valid && open) break;

            if (!valid) {
                StdOut.print("Not a valid coordinate!");
            } else if (!open) {
                StdOut.print("Occupied by Player " + board[x][y]);
            } else {
                StdOut.print("Not valid and occupied by a player.");
                StdOut.print("Specsavers?");
            }
            StdOut.print("Re-enter coordinates: ");
            StdOut.print("x: ");
            x = StdIn.readInt();
            StdOut.print("y: ");
            y = StdIn.readInt();
        }

        //Change choice to current players value
        board[x][y] = currentPlayer;



		// TODO: calculate location and neighbours location in
		// WeightedQuickUnionUF data structure
        int index = changeTo2D(x, y);

        unionise(x, y, index);



		// TODO: create unions to neighbour sites in WeightedQuickUnionUF that
		// also contain current players value

        if (turnCount > 27) isWinner();
        else nextPlayer();
        turnCount++;
	}

    private int changeTo2D(int x, int y) {
        return x * n1 + y;
    }

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoardGame#getCurrentPlayer()
	 */
	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}

    /**
     *
     * @param currentPlayer
     */
	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoardGame#getBoard()
	 */
	@Override
	public int[][] getBoard() {
		return board;
	}


    /**
     *
     */
	private void nextPlayer() {
		if (currentPlayer == 1)
			currentPlayer = 2;
		else
			currentPlayer = 1;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoardGame#isWinner()
	 */
	@Override
	public boolean isWinner() {
		if (currentPlayer == 1) return wqu.connected(EAST, WEST);
        else if (currentPlayer == 2) return wqu.connected(NORTH, SOUTH);
        return false;
	}

    private void unionise(int x, int y, int index) {
        connectHomes(x, y, index);
    }

    private void connectHomes(int x, int y, int index) {
        if (currentPlayer == 1) {
            if (y == 0) wqu.union(EAST, index);
            if (y == board.length - 1) wqu.union(WEST, index);
        }

        if (currentPlayer == 2) {
            if (x == 0) wqu.union(NORTH, index);
            if (x == board.length - 1) wqu.union(SOUTH, index);
        }
    }


    private boolean validCoords(int x, int y) {
        //ANDed so it returns false as soon as an invalid coordinate is seen
        return x >= 0 && y >= 0 && x < board.length && y < board.length;
    }

    private boolean isOpen(int x, int y) {
        return board[x][y] == 0;
    }


	/**
	 * THIS IS OPTIONAL:
	 * Modify the main method if you wish to suit your implementation.
     * This is just an example of a test implementation.
     * For example you may want to display the board after each turn.
     * @param args
	 * 
	 */
	public static void main(String[] args) {

		BoardGame hexGame = new Hex(4, 4);

		while (!hexGame.isWinner()) {
			System.out.println("It's player " + hexGame.getCurrentPlayer()
					+ "'s turn");
			System.out.println("Enter x and y location:");
			int x = StdIn.readInt();
			int y = StdIn.readInt();

			hexGame.takeTurn(x, y);

		}

		System.out.println("It's over. Player " + hexGame.getCurrentPlayer()
				+ " wins!");

	}



}
