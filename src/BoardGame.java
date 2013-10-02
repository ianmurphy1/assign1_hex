/**
 * Generic 2D Board Game interface.
 * This should NOT be altered.
 * @author Frank
 *
 */
public interface BoardGame {

	/**
	 * Allows the current player to take their turn 
	 * @param x - Row number on board
	 * @param y - Column number on board
	 */
	public void takeTurn(int x, int y);

	/**
	 * Return current player in control of the game
	 * @return int indicating current player
	 */
	public int getCurrentPlayer();

	
	/**
	 * Returns 2D array representing the current state of the board
	 * @return Board's 2D array
	 */
	public int[][] getBoard();

	/**
	 * Returns boolean indicating if there's a winner.
	 * @return Whether player has won or not
	 */
	public boolean isWinner();

}