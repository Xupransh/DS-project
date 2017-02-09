package player;
import java.util.*;
import java.lang.*;

public class Tester{
	private static void perform(MachinePlayer player, Move curr){
		System.out.print(curr);
		if (player.forceMove(curr))
			System.out.println(" executed!"); 
		else
			System.out.println(" not executed!"); 
	}

	// private static void readFile(String file) {
	// 	ArrayList<Move> moves = new ArrayList<Move>();
	// 	try(BufferedReader br = new BufferedReader(new FileReader(file))) {
	// 	    StringBuilder sb = new StringBuilder();
	// 	    String line = br.readLine();

	// 	    while (line != null) {
	// 	        sb.append(line);
	// 	        sb.append(System.lineSeparator());
	// 	        line = br.readLine();
	// 	    }
	// 	    String everything = sb.toString();
	// 	}
	// }

	public static void main(String[] args) {
		MachinePlayer player = new MachinePlayer(1);
		int[] xAll = {1,2};
		int[] yAll = {1,2};
		// for (int x: xAll) {
		// 	for (int y: yAll) {
		// 		Move curr = new Move(x, y);
		// 		System.out.print(curr);
		// 		if (player.forceMove(curr))
		// 			System.out.println(" executed!"); 
		// 		else
		// 			System.out.println(" not executed!"); 
		// 	}
		// }

		Move c1 = new Move(1,2);	
		Move c2 = new Move(1,3);
		Move c3 = new Move(2,4);
		perform(player, c1);
		perform(player, c2);
		perform(player, c3);


	}
}