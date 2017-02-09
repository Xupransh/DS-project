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
  private Gameboard currBoard;

  private int searchDepth;


  public MachinePlayer(int color) {
    this.color = color;
    currBoard = new Gameboard(color);
  }



  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    this.color = color; this.searchDepth = searchDepth;
    currBoard = new Gameboard(color);
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    Move chosen =  chooseRandomMove();
    performMove(chosen, MYPLAYER);
    return chosen;
  } 

  private Move chooseRandomMove() {
    ArrayList<Move> allLegalMoves = allValid(MYPLAYER);
    return allLegalMoves.get((new Random()).nextInt(allLegalMoves.size()));
  }



  //generates array list of all valid moves
  private ArrayList<Move> allValid(int player)
  {

    ArrayList<Move> legalMoves = new ArrayList<Move>();
    //generate legal moves for ADD type moves
    if (myChipsLeft > 0) 
    {
      for (int i = 0; i < 8; i++) 
      {
          for (int j = 0; j < 8; j++) 
          {
              if (board[i][j] == EMPTY) 
              {
                Move temp = new Move(i, j);
                if (isValid(temp, player)) legalMoves.add(temp);
              }
          }
      }
    }

    //generate legal moves for STEP moves type
    else
    {
      Move[] chips_initial = chipsOnBoard(player); 
      for(int i = 0; i<chips_initial.length ; i++)
      {
        Move curr = chips_initial[i];
        for (int j = 0; j<8 ; j++ ) 
        {
          for (int k = 0; k <8 ; k++ ) 
          {
              if (board[j][k] == EMPTY) 
              {
                  Move temp = new Move(j, k, curr.x1,curr.y1);
                  if (isValid(temp, player)) legalMoves.add(temp);    
              }
          }
        }
      }
    }

    return legalMoves;
  }


  // performs a move if it is legal else returns false. Opponent is 0 
  private boolean performMove(Move m, int player) {
    if (!currBoard.isValid(m, player)) return false;
    else {
        currBoard.modify(m, player); 
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

}
