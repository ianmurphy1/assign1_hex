import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;

/**
 * Class that contains the logic for the Hex game.
 */
public class Hex implements BoardGame {

    private int[][] board; // 2D Board. 0 - empty, 1 - Player 1, 2 - Player 2

    private int n1, n2; // height and width of board

    private WeightedQuickUnionUF wqu; // Union Find data structure to keep track
    // of unions and calculate winner

    private int currentPlayer; // Current player in the game, initialised to 1
    private int turnCount; //Keep check of turn count so not checking winner when we don't have to

    //Special sites for checking percolation
    //Player 1 takes EAST -> WEST
    private final int EAST;
    private final int WEST;
    //Player 2 takes NORTH -> SOUTH
    private final int NORTH;
    private final int SOUTH;

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
     * Method that allows the completion of a turn for a player.
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
            int index = oneDify(x, y);
            //Form unions with any neighbours that are valid and free.
            unionise(x, y, index);
            turnCount++;
            if ((turnCount >= (n1 + n2) - 1) && isWinner()) return;
            nextPlayer();
            //Get the next player and jump out of method.
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
    private int oneDify(int x, int y) {
        return (x * n1) + y;
    }


    /**
     * Method that returns the current player.
     *
     * @return The current player.
     */
    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method that allows access to the private variable 'currentPlayer' so that it
     * can be changed.
     *
     * @param currentPlayer
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Method that returns the 2D array belonging to the Hex class.
     *
     * @return The 2D array of Hex
     */
    @Override
    public int[][] getBoard() {
        return board;
    }


    /**
     * Method to change the current player to the next one.
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
     * those of the same player will union them together in wqu's data structure. The method treats the border
     * areas of the grid separately and uses these places for the union to the home indexes for the player.
     *
     * @param x Row coordinate
     * @param y Column coordinate
     * @param index The index of co-ordinates in wqu's data structure
     */
    private void unionise(int x, int y, int index) {
        // if co-ordinate is located around edge then call method to connect virtual sites
        if (x == 0 || y == 0 || x == n1 - 1 || y == n2 - 1) connectHomes(x, y, index);

        if (y < n2 - 1 && board[x][y + 1] == currentPlayer) wqu.union(oneDify(x, y + 1), index);

        if (y > 0 && board[x][y - 1] == currentPlayer) wqu.union(oneDify(x, y - 1), index);

        if (x > 0) {
            if (board[x - 1][y] == currentPlayer) wqu.union(oneDify(x - 1, y), index);

            if (y < n2 - 1 && board[x - 1][y + 1] == currentPlayer) wqu.union(oneDify(x - 1, y + 1), index);
        }

        if (x < n1 - 1) {
            if (x < n1 - 1 && board[x + 1][y] == currentPlayer) wqu.union(oneDify(x + 1, y), index);

            if (x < n1 - 1 && y > 0 && board[x + 1][y - 1] == currentPlayer) wqu.union(oneDify(x + 1, y - 1), index);
        }

    }

    /**
     * This method will check around the borders and if the player is connected to
     * their corresponding home base then its connected.
     *
     * @param x The chosen x coordinate
     * @param y The players chosen y coordinate
     * @param index The x and y coordinates corresponding index in wqu's data structure
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
        return x >= 0 && y >= 0 && x < n1 && y < n2;
    }

    /**
     * This method will return whether or not a grid spot is open.
     *
     * @param x The player's x coordinate
     * @param y The player's y coordinate
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

        BoardGame hexGame = new Hex(14, 14);

        while (!hexGame.isWinner()) {
            System.out.println("It's player " + hexGame.getCurrentPlayer()
                    + "'s turn");
            printGrid(hexGame.getBoard());
            System.out.println("Enter Row: ");
            int x = StdIn.readInt();
            System.out.println("Enter Column: ");
            int y = StdIn.readInt();
            hexGame.takeTurn(x, y);
        }
        System.out.println("It's over. Player " + hexGame.getCurrentPlayer()
                + " wins!");
    }

    /**
     * Method that prints out a grid that is passed into it in hex grid form... kinda.
     *
     * @param arr Board to printed.
     */
    public static void printGrid(int[][] arr) {
        StdOut.println("     [0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [10] [11] [12] [13]");
        for (int i = 0; i < arr.length; i++) {

            StdOut.print(" [" + i + "] ");
            for (int j = 0; j < i; j++) {
                StdOut.print("  ");
            }

            for (int j = 0; j < arr[i].length; j++) {
                StdOut.print("<" + arr[i][j] + "> ");
            }
            StdOut.println(" ");
        }
    }
}
