
public class Hex implements BoardGame {

	private int[][] board; // 2D Board. 0 - empty, 1 - Player 1, 2 - Player 2 
	                      
	private int n1, n2; // height and width of board
	
	private WeightedQuickUnionUF wqu; // Union Find data structure to keep track
										// of unions and calculate winner
	
	private int currentPlayer; // Current player in the game, initialised to 1

	public Hex(int n1, int n2) // create N-by-N grid, with all sites blocked
	{
        this.n1 = n1;
        this.n2 = n2;
        currentPlayer = 1;

        // TODO: Create instance of board
        board = new int[n1][n2];
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board.length; j++)
                board[i][j] = 0;

        // TODO: Create instance WeightedQuickUnionUF class
        // (n1 * n2) + 4 because we need an extra 4 to check who's won
        wqu = new WeightedQuickUnionUF((n1 * n2) + 4);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see BoardGame#takeTurn(int, int)
	 */
	@Override
	public void takeTurn(int x, int y) {
        x--; //Decrement to take into account player starting index at 1.
        y--;

		// TODO: check coords are valid
        if (validCoords(x, y)) {
            throw new IndexOutOfBoundsException("Invalid Coordinates");
        }



		// TODO: check if location is free and set to player's value(1 or 2).
        if (isOpen);

		// TODO: calculate location and neighbours location in
		// WeightedQuickUnionUF data structure

		// TODO: create unions to neighbour sites in WeightedQuickUnionUF that
		// also contain current players value

		// TODO: if no winner get the next player

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

		// TODO:check if there is a connection between either side of the board.
		// You can do this by using the 'virtual site' approach in the
		// percolation test.
		return false;
	}


    private boolean validCoords(int x, int y) {
        return (x < 0 || y < 0 || x > board.length || y > board.length);
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
