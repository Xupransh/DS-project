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
  private int myChipsLeft = 10; 
  private int opponentChipsLeft = 10;
  private int color; // color of my player
  /* boolean value that represents whose move it is */
  private final static int MYPLAYER = 1; 
  private final static int OPPONENT = 2;
  private final static int EMPTY = -2;
  private final static int CORNER = -1;

  private int[][] board; /* 0 if unoccupied, -1 for corners, 1 if occupied by me and 2 if occupied by opponet */
  private int searchDepth;

  static final int[] LEFT = {-1,0};
  static final int[] RIGHT = {1,0};
  static final int[] LEFT_DOWN = {-1,-1};
  static final int[] RIGHT_UP = {1,1};
  static final int[] UP= {0,1};
  static final int[] LEFT_UP = {-1,1};
  static final int[] DOWN = {0,-1};
  static final int[] RIGHT_DOWN = {1,-1};
  static final int[][] DIRECTIONS = {RIGHT_UP,RIGHT_DOWN,UP,DOWN,LEFT_UP,RIGHT,LEFT_DOWN,LEFT};

  public MachinePlayer(int color) {
    this.color = color;
    board = new int[8][8];
    initializeBoard();
  }


  private void initializeBoard() {
    for (int i=0; i < 8; i++) {
      for (int j=0; j < 8; j++) {
        board[i][j] = EMPTY;
      }
    }

    board[0][0] = board[7][7] = board[7][0] = board[0][7] = CORNER;
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    this.color = color; this.searchDepth = searchDepth;
    board = new int[8][8];
    initializeBoard();
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

  private boolean wrongGoal(int player, int x, int y) {
    int player_color = player == MYPLAYER ? color : 1-color; 
    int coord_examine = player_color == 0 ? x : y;
    return (coord_examine == 0 || coord_examine == 7);
  }
  
  public int adjacent(int x, int y, int[] dir){
    int curr_x = x + dir[0];
    int curr_y = y + dir[1];

    if (curr_x < 0 || curr_x > 7) return -1;
    else if (curr_y < 0 || curr_y > 7) return -1;
    else return board[curr_x][curr_y];  
  }  
  

  private int countNeighbors(int color, int x, int y) {
    int count = 0;
    for (int[] dir: DIRECTIONS) {
        if (adjacent(x, y, dir) == color) 
          count++;
    }

    return count;
  }

  // For adding, checks if the addition of a third chip will create three adjacent chips
  private boolean checkAdjacency(int color, int x, int y)
  {
    //check in a maximum 3X3 sub array around x,y
    int count = 0;
    for(int[] dir : DIRECTIONS)
    {
      /* if x+dir[0], y + dir[1] has a chip of same color */
      if(adjacent(x, y, dir)==color)
      {
        count++;
        /* if the new position has more than 1 neighbor */
        if (countNeighbors(color, x+dir[0], y + dir[1]) >= 1) return false;
      }

    }

    /* if this position has more than 2 adjacent positions filled with chips of same color */
    return (count < 2);

  }
  
  private boolean checkAddMove(Move m, int player) {
    int x = m.x1, y = m.y1;
    int chips_curr = player == MYPLAYER ? myChipsLeft : opponentChipsLeft;
    int player_color = player == MYPLAYER ? color : 1-color;
    if (chips_curr == 0) return false;
    else if (board[x][y] == CORNER || board[x][y] != EMPTY) return false; // can't place in the corners or if already occupied
    else if (wrongGoal(player, x, y)) return false;
    else return checkAdjacency(player_color, x, y);
  }

  private boolean checkStepMove(Move m, int player) {
    int x1 = m.x1, x2 = m.x2, y1 = m.y1, y2 = m.y2;
    int chips_curr = player == MYPLAYER ? myChipsLeft : opponentChipsLeft;
    int player_color = player == MYPLAYER ? color : 1-color;
    if (chips_curr != 0) return false;
    else if (board[x2][y2] != player_color) return false; // to ensure that x1, y1 is a legal position
    else if (board[x1][y1] == CORNER|| board[x1][y1] != EMPTY) return false; // can't place in the corners or if already occupied
    else if (wrongGoal(player, x1, y1)) return false;
    else {
      board[x2][y2] = EMPTY;
      boolean adj_check = checkAdjacency(player_color, x1, y1);
      board[x2][y2] = player_color;
      return adj_check;
    }
  }


   // returhs true if m is a valid move else returns false
  private boolean isValid(Move m, int player) {
    if (m.moveKind == Move.QUIT) return true;

    int chips_curr = player == MYPLAYER ? myChipsLeft : opponentChipsLeft;
    if (m.moveKind == Move.ADD) return checkAddMove(m, player);
    else return checkStepMove(m, player);
  }

  

  // modifies a board given a move
  private void modify(Move m, int player) {
    int myColor = player == MYPLAYER ? color : 1-color;

    if (m.moveKind == Move.STEP) {
      int x1 = m.x1, x2 = m.x2, y1 = m.y1, y2 = m.y2;
      board[x1][y1] = myColor;
      board[x2][y2] = EMPTY;
    }

    else if (m.moveKind == Move.ADD) {
      int x = m.x1, y = m.y1;
      if (player == MYPLAYER) myChipsLeft--;
      else opponentChipsLeft--;
     //System.out.println("here. filling " + x + " " + y + "with " + myColor);
      board[x][y] = myColor;
    }
  }

  // undoes the action of move m on the board
  private void unModify(Move m, int player) {
    int myColor = player == MYPLAYER ? color : 1-color;

    if (m.moveKind == Move.STEP) {
      int x1 = m.x1, x2 = m.x2, y1 = m.y1, y2 = m.y2;
      board[x1][y1] = EMPTY;
      board[x2][y2] = myColor;
    }

    else if (m.moveKind == Move.ADD) {
      int x = m.x1, y = m.y1;
      if (player == MYPLAYER) myChipsLeft++;
      else opponentChipsLeft++;
      board[x][y] = EMPTY;
    }
  }

  //to get the positions of all chips on board
  private Move[] chipsOnBoard(int player)
  {
    int myColor = player == MYPLAYER ? color : 1-color;
    Move[] holder = new Move[10];
    int counter = 0;
    for (int i = 0; i < 8; i++) 
    {
      for (int j = 0; j < 8; j++) 
      {
        if (board[i][j] == myColor) 
        {
          Move newMove = new Move(i, j);
          holder[counter] = newMove;
          counter++;
        }
      }
    }

    System.out.println(counter);
    return holder;
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
    if (!isValid(m, player)) return false;
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

}
