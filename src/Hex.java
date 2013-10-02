
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

        board = new int[n1][n2];
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board.length; j++)
                board[i][j] = 0;

        // (n1 * n2) + 4 because we need an extra 4 to check who's won
        wqu = new WeightedQuickUnionUF((n1 * n2) + 4);
        // Setting virtual site places for checking winner in wqu data structure
        EAST = wqu.getId().length - 1;
        WEST = wqu.getId().length - 2;
        NORTH = wqu.getId().length - 3;
        SOUTH = wqu.getId().length - 4;
    }

    /**
     *
     *
     * @param x Row number on board
     * @param y Column number on board
     * @see BoardGame#takeTurn(int, int)
     */
    @Override
    public void takeTurn(int x, int y) {
        // Check if valid and free space
        if (validCoords(x, y) && isOpen(x, y)) {
            // Set grid to be the current player
            board[x][y] = currentPlayer;
            // Change the x and y coords to the index of the quick union data structure
            int index = changeTo2D(x, y);
            unionise(x, y, index);
            turnCount++;
            if ((turnCount >= (n1 + n2) - 1) && isWinner()) return;
            nextPlayer();
            return;
        }

        if (!validCoords(x, y)) StdOut.println("Invalid Co-Ordinates");
        else if (!isOpen(x, y)) StdOut.println("Already Occupied.");

        StdOut.println("Re-Enter Co-Ordinates: ");

    }

    /**
     * This method takes the co-ordinates of a player's choice of grid position and converts into the index
     * number of the data structure that quick union uses to keep track of what is connected.
     *
     * @param x Row number
     * @param y Column number
     * @return Index of grid position in flat array
     */
    private int changeTo2D(int x, int y) {
        return (x * n1) + y;
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
        return wqu.connected(EAST, WEST) || wqu.connected(NORTH, SOUTH);
    }

    /**
     * This method checks the neighbours of the chosen grid location and if it finds the neighbours to be
     * those of the same player will union them together in wqu's data structure.
     *
     * @param x Row coordinate
     * @param y Column coordinate
     * @param index The index of co-ordinates in wqu's data structure
     */
    private void unionise(int x, int y, int index) {
        // if co-ordinate is located around edge then call method to connect virtual sites
        if (x == 0 || y == 0 || x == n1 - 1 || y == n2 - 1) connectHomes(x, y, index);

        if (y < n2 - 1 && board[x][y + 1] == currentPlayer) wqu.union(changeTo2D(x, y + 1), index);

        if (y > 0 && board[x][y - 1] == currentPlayer) wqu.union(changeTo2D(x, y - 1), index);

        if (x > 0 && board[x - 1][y] == currentPlayer) wqu.union(changeTo2D(x - 1, y), index);

        if (x > 0 && y < n2 - 1 && board[x - 1][y + 1] == currentPlayer) wqu.union(changeTo2D(x - 1, y + 1), index);

        if (x < n1 - 1 && board[x + 1][y] == currentPlayer) wqu.union(changeTo2D(x + 1, y), index);

        if (x < n1 - 1 && y > 0 && board[x + 1][y - 1] == currentPlayer) wqu.union(changeTo2D(x + 1, y - 1), index);
    }

    /**
     *
     * @param x
     * @param y
     * @param index
     */
    private void connectHomes(int x, int y, int index) {
        if (currentPlayer == 1) {
            if (y == 0) wqu.union(EAST, index);
            if (y == n2 - 1) wqu.union(WEST, index);
        } else if (currentPlayer == 2) {
            if (x == 0) wqu.union(NORTH, index);
            if (x == n1 - 1) wqu.union(SOUTH, index);
        }
    }

    /**
     * @param x Row Number (0 - (n1 - 1))
     * @param y Column Number (0 - (n2 - 1))
     * @return Validity of co-ordinates
     */
    private boolean validCoords(int x, int y) {
        //ANDed so it returns false as soon as an invalid coordinate is seen
        return (x >= 0 && y >= 0 && x < n1 && y < n2);
    }

    /**
     *
     * @param x
     * @param y
     * @return Whether board space is open
     */
    private boolean isOpen(int x, int y) {
        return board[x][y] == 0;
    }


    /**
     * THIS IS OPTIONAL:
     * Modify the main method if you wish to suit your implementation.
     * This is just an example of a test implementation.
     * For example you may want to display the board after each turn.
     *
     * @param args
     */
    public static void main(String[] args) {

        BoardGame hexGame = new Hex(4, 4);

        while (!hexGame.isWinner()) {
            System.out.println("It's player " + hexGame.getCurrentPlayer()
                    + "'s turn");
            printGrid(hexGame.getBoard());
            System.out.println("Enter x and y location:");
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            hexGame.takeTurn(x, y);
        }
        System.out.println("It's over. Player " + hexGame.getCurrentPlayer()
                + " wins!");

    }

    /**
     *
     * @param arr
     */
    public static void printGrid(int[][] arr) {

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < i; j++) {
                StdOut.print("  ");
            }
            for (int j = 0; j < arr.length; j++) {
                StdOut.print("<" + arr[i][j] + ">");
            }
            StdOut.println(" ");
        }

    }


}
