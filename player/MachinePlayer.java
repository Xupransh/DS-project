/* MachinePlayer.java */

package player;
import java.util.*;
import java.lang.*;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
 
  private int color; // color of my player
  /* boolean value that represents whose move it is */
  private final static int MYPLAYER = 1; 
  private final static int OPPONENT = 2;
  private GameBoard currBoard;
  final static int EMPTY = -2;
  final static int CORNER = -1;


  private int searchDepth;


  public MachinePlayer(int color) {
    this.color = color;
    currBoard = new GameBoard(color);
  }



  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    this.color = color; this.searchDepth = searchDepth;
    currBoard = new GameBoard(color);
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    Move chosen =  chooseRandomMove();
    performMove(chosen, MYPLAYER);
    return chosen;
  } 

  private Move chooseRandomMove() {
    ArrayList<Move> allLegalMoves = currBoard.allValid(MYPLAYER);
    return allLegalMoves.get((new Random()).nextInt(allLegalMoves.size()));
  }




 
  /*function to score a position, the parameters used are following (can be updated)
  
  -> It’s bad to have more than 2 pieces in goal areas - goalFeature
  -> It’s bad to have 3 piece in a straight line - straightFeature
  -> It’s bad to have an enemy piece in the middle of 2 of your straight line pieces - enemyFeature

  -> Make tree of chips from left to right (or up to down depending on side), maintain count of no. of chips going from l to r. 
  If enemy has higher count, that’s bad, else good.

  The score will be computed based on 2 multipliers, 1 for enemy, and one for me, with the 4 features added and multiplied to the 
  multiplier, and the 2 products added to get a final score. The enemy multiplier will be negative obviously.
  networkTree function needed, function to count no. of chips connected from l to r needed, takes array
  positions of chips on board

  private int evaluate(int player)
  {

    int score; //this will be the value returned

    int mgoalFeature;
    int mstraightFeature;
    int menemyFeature;

    int egoalFeature;
    int estraightFeature;
    int eenemyFeature;

    int myColor = player == MYPLAYER ? color : 1-color;
    int enemyColor = 1-myColor;
    Move [] enemyPosition = chipsOnBoard(int enemyColor);
    Move [] myPosition = chipsOnBoard(int myColor);

    int enemyCount = connectedChips(enemyPosition); //function needed
    int myCount = connectedChips(myPosition);



  }
  */


  // performs a move if it is legal else returns false. Opponent is 0 
  private boolean performMove(Move m, int player) {
    if (!currBoard.isValid(m, player)) return false;
    else {
        modify(m, player); 
        return true;
    }
  }

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    return performMove(m, OPPONENT);
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this" 
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    return performMove(m, MYPLAYER);
  }

    private void modify(Move m, int player) {

    int my_color = player == MYPLAYER ? color : 1-color;
     
    if (m.moveKind == Move.STEP) {
      int x1 = m.x1, x2 = m.x2, y1 = m.y1, y2 = m.y2;
      currBoard.board[x1][y1] = my_color;
      currBoard.board[x2][y2] = EMPTY;
    }

    else if (m.moveKind == Move.ADD) {
      int x = m.x1, y = m.y1;
      if (player == MYPLAYER) currBoard.myChipsLeft--;
      else currBoard.opponentChipsLeft--;
      currBoard.board[x][y] = my_color;
    }
  }

  // undoes the action of move m on the board
  private void unModify(Move m, int player) {

  int my_color = player == MYPLAYER ? color : 1-color;
    if (m.moveKind == Move.STEP) {
      int x1 = m.x1, x2 = m.x2, y1 = m.y1, y2 = m.y2;
      currBoard.board[x1][y1] = EMPTY;
      currBoard.board[x2][y2] = my_color;
    }

    else if (m.moveKind == Move.ADD) {
      int x = m.x1, y = m.y1;
      if (player == MYPLAYER) currBoard.myChipsLeft++;
      else currBoard.opponentChipsLeft++;
      currBoard.board[x][y] = EMPTY;
    }
  }


}
