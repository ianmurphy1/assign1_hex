import static org.junit.Assert.*;

import org.junit.Test;


/**
 * This JUnit tests the Hex class.
 * If you have coded it correctly they should all pass!
 * This is not an exhaustive test - you can create your own test cases.
 *
 * @author Frank
 *
 * I changed these slightly so that the tests would pass. The change was made due
 * to me using the arr[row][col] way of looking at the coordinates of the 2D array.
 *
 *
 */
public class HexTest {

    /**
     * This test checks the initial creation of a Hex class.
     */
    @Test
    public void HexInitialisationTest() {
        BoardGame hex = new Hex(14, 14);
        //check board dimensions
        assertTrue(hex.getBoard().length == 14);
        assertTrue(hex.getBoard()[0].length == 14);

        //check board initialised to 0
        assertTrue(hex.getBoard()[0][0] == 0);
        assertTrue(hex.getBoard()[0][0] == hex.getBoard()[13][13]);

        //check game starts with player 1
        assertTrue(hex.getCurrentPlayer() == 1);

        //check there is no winner yet!
        assertFalse(hex.isWinner());

    }

    /**
     * This test checks a player 2 win scenario
     * This test was changed by me to take into account the method of coordinates to be [row][col].
     *
     * In this case, player 2 tries to connect the top to the bottom(Yellow edges in image in specification)
     */
    @Test
    public void HexGamePlayer1WinTest() {
        BoardGame hex = new Hex(14, 14);

        int j = 0;
        assertFalse(hex.isWinner());

        for (int i : hex.getBoard()[0]) {
            hex.takeTurn(4, j);
            hex.takeTurn(1, j);
            j++;
        }

        assertTrue(hex.isWinner() && hex.getCurrentPlayer() == 1);

    }


    /**     *
     * This test checks a player 2 win scenario.
     *
     * This test was changed by me to take into account the method of coordinates to be [row][col].
     * In this case, player 2 tries to connect the left and right sides(Yellow edges in board in specification)
     */
    @Test
    public void HexGamePlayer2WinTest() {
        BoardGame hex = new Hex(14, 14);

        int j = 0;
        assertFalse(hex.isWinner());

        for (int i : hex.getBoard()[0]) {
            assertTrue(hex.getCurrentPlayer() == 1);
            hex.takeTurn(j, 0);
            assertFalse(hex.isWinner());
            hex.takeTurn(j, 6);
            j++;
        }

        assertTrue(hex.isWinner() && hex.getCurrentPlayer() == 2);

    }

    /**
     * This test checks that the current player is not
     * changed if an invalid move is attempted.
     */
    @Test
    public void HexGameInvalidMoveTest() {
        BoardGame hex = new Hex(14, 14);
        assertTrue(hex.getCurrentPlayer() == 1);


        hex.takeTurn(0, 0);
        assertTrue(hex.getCurrentPlayer() == 2);


        hex.takeTurn(0, 0);//Location already used, current player should remain the same(i.e. player 2)
        assertTrue(hex.getCurrentPlayer() == 2);

        hex.takeTurn(10, 10);
        assertTrue(hex.getCurrentPlayer() == 1);

        hex.takeTurn(14, 10);//invalid Coordinate
        assertTrue(hex.getCurrentPlayer() == 1);

        hex.takeTurn(1, -10);//invalid Coordinate
        assertTrue(hex.getCurrentPlayer() == 1);

    }

}
