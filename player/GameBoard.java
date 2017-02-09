
package player;
import java.util.*;
import java.lang.*;


public class GameBoard{
	final static int EMPTY = -2;
	final static int CORNER = -1;

	private final static int MYPLAYER = 1; 
	private final static int OPPONENT = 2;

	private int myChipsLeft = 10; 
	private int opponentChipsLeft = 10;
	private int color; /* 0 for black and 1 for white */
	private int[][] board; /* 0 if unoccupied, -1 for corners, 1 if occupied by me and 2 if occupied by opponet */

	
	static final int[] LEFT = {-1,0};
	static final int[] RIGHT = {1,0};
	static final int[] LEFT_DOWN = {-1,-1};
	static final int[] RIGHT_UP = {1,1};
	static final int[] UP= {0,1};
	static final int[] LEFT_UP = {-1,1};
	static final int[] DOWN = {0,-1};
	static final int[] RIGHT_DOWN = {1,-1};
	static final int[][] DIRECTIONS = {RIGHT_UP,RIGHT_DOWN,UP,DOWN,LEFT_UP,RIGHT,LEFT_DOWN,LEFT};

	public GameBoard(int color) {
	  board = new int[8][8];
	  this.color = color;
	  for (int i=0; i < 8; i++) {
	    for (int j=0; j < 8; j++) {
	      board[i][j] = EMPTY;
	    }
	  }

	  board[0][0] = board[7][7] = board[7][0] = board[0][7] = CORNER;
	}


	public int pos(int i, int j) {
		return board[i][j];
	}

	public void set(int i, int j, int color) {
		board[i][j] = color;
	}


	// modifies a board given a move
	private void modify(Move m, int color) {
	  if (m.moveKind == Move.STEP) {
	    int x1 = m.x1, x2 = m.x2, y1 = m.y1, y2 = m.y2;
	    board[x1][y1] = color;
	    board[x2][y2] = EMPTY;
	  }

	  else if (m.moveKind == Move.ADD) {
	    int x = m.x1, y = m.y1;
	    if (player == MYPLAYER) myChipsLeft--;
	    else opponentChipsLeft--;
	    board[x][y] = color;
	  }
	}

	// undoes the action of move m on the board
	private void unModify(Move m, int color) {

	  if (m.moveKind == Move.STEP) {
	    int x1 = m.x1, x2 = m.x2, y1 = m.y1, y2 = m.y2;
	    board[x1][y1] = EMPTY;
	    board[x2][y2] = color;
	  }

	  else if (m.moveKind == Move.ADD) {
	    int x = m.x1, y = m.y1;
	    if (player == MYPLAYER) myChipsLeft++;
	    else opponentChipsLeft++;
	    board[x][y] = EMPTY;
	  }
	}



	//to get the positions of all chips on board
	private Move[] chipsOnBoard(int color)
	{
	  Move[] holder = new Move[10];
	  int counter = 0;
	  for (int i = 0; i < 8; i++) 
	  {
	    for (int j = 0; j < 8; j++) 
	    {
	      if (board[i][j] == color) 
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
	public boolean isValid(Move m, int player) {
	  if (m.moveKind == Move.QUIT) return true;

	  int chips_curr = player == MYPLAYER ? myChipsLeft : opponentChipsLeft;
	  if (m.moveKind == Move.ADD) return checkAddMove(m, player);
	  else return checkStepMove(m, player);
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

}