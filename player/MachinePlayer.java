/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  private int myChipsLeft = 10; 
  private int opponentChipsLeft = 10;
  private int color; // color of my player
  /* boolean value that represents whose move it is */
  private final static int MYPLAYER = 1; 
  private final static int OPPONENT = 2;
  private int board[8][8]; /* 0 if unoccupied, -1 for corners, 1 if occupied by me and 2 if occupied by opponet */


  public MachinePlayer(int color) {
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    return new Move();
  } 


  private boolean wrongGoal(int player, int x, int y) {
    boolean player_color = player == MYPLAYER ? color : !color; 
    int coord_examine = player_color == 0 ? x : y;
    return (coord_examine == 0 || coord_examine == 7);
  }

  // returhs true if m is a valid move else returns false
  private boolean isValid(Move m, int player) {
    int chips_curr = player == MYPLAYER ? myChipsLeft : opponentChipsLeft;
    if (m.moveKind == QUIT) return true;
    else if (m.moveKind == ADD && chips_curr == 0 || m.moveKind == STEP && chips_curr != 0) return false;
    else if (m.moveKind == ADD) {
      int x = m.x1, y = m.y1;
      if (board[x][y] == -1 || board[x][y] != 0) return false; // can't place in the corners or if already occupied
      else if (wrongGoal(player, x, y)) return false; // can't place in the opponent's goal
      else checkAdjacency(player, x, y); // check the adjacency constraint. 
    }

    /* invalid set of moves if STEP move */
    else {

    }
  }

  // modifies a board given a move
  private void modify(Move m) {

  }

  // undoes the action of move m on the board
  private void unModify(Move m) {

  }

  // performs a move if it is legal else returns false. Opponent is 0 
  private boolean performMove(Move m, int player) {
    if (!isValid(m, player)) return false;
    else {
        modify(m); return true;
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
