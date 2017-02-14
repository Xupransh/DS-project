import org.junit.Test;
import player.GameBoard;
import player.Move;


import static org.junit.Assert.assertEquals;


/**
 * Created by supransh on 2/14/17.
 */
public class GameBoardTest {

    int player = 2;
    int color_black = 0;
    int color_white = 1;


        @Test
        public void Tester1() {

            Move m = new Move(0,0);
            GameBoard gameBoard_test = new GameBoard(color_white);
            boolean output = gameBoard_test.isValid(m, player);
            assertEquals(false, output);

        }

        @Test
        public void Tester2(){

            GameBoard gameBoard_test = new GameBoard(color_white);
            gameBoard_test.add(3,3, color_white);
            gameBoard_test.add(3,4, color_white);
            boolean output = gameBoard_test.checkAdjacency(color_white,3, 3);
            assertEquals(true, output);


    }

    /*@Test
    public void allValid() throws Exception {

    }*/

    }

