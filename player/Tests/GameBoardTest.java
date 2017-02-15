import org.junit.Test;
import player.GameBoard;
import player.Move;
import java.util.*;


import static org.junit.Assert.assertEquals;


/**
 * Created by Supransh on 2/14/17.
 */
public class GameBoardTest {

    int player = 1;
    int color_black = 0;
    int color_white = 1;


        @Test
        public void Tester1() {


            GameBoard gameBoard_test1 = new GameBoard(color_white);
            gameBoard_test1.add(3,3, player);
            gameBoard_test1.add(3,4, player);
            Move m = new Move(2,5);
            boolean output1 = gameBoard_test1.isValid(m, player);
            assertEquals(true, output1);

        }

        @Test
        public void Tester2(){

            GameBoard gameBoard_test = new GameBoard(color_white);
            gameBoard_test.add(3,3, player);
            gameBoard_test.add(3,4, player);
            boolean output = gameBoard_test.checkAdjacency(color_white,3, 3);
            assertEquals(false, output);


    }

   /* @Test
    public void Tester3() {
        GameBoard gameBoard_test = new GameBoard(color_white);
        gameBoard_test.add(3,3, color_white);
        gameBoard_test.add(3,4, color_white);
        ArrayList<Move> test = new ArrayList<Move>();




    }*/

    }

