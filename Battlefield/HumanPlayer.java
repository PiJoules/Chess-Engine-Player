import chess.*;

import java.util.*;

// A player that makes random moves
public class HumanPlayer extends Player {

	private Random random = new Random();
	private Scanner scan = new Scanner(System.in);

	public HumanPlayer(Color player){
		super(player);
	}

	public Move getMove(BoardState state){
		ArrayList<Move> possibleMoves = state.getAllMoves(this.getColor());
		//int choice = random.nextInt(possibleMoves.size());
		//return possibleMoves.get(choice);

		// Moves will be in the format "x1,y1,x2,y2"
		while (true){
			System.out.print("Make move: ");
			String moveString = scan.nextLine();
			String[] coordsString = moveString.split(",");
			int[] coords = new int[4];
			for (int i = 0; i < coords.length; i++)
				coords[i] = Integer.parseInt(coordsString[i]);
			/*Move move = new Move(
				state.getSquare(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])),
				state.getSquare(Integer.parseInt(coords[2]), Integer.parseInt(coords[3]))
			);
			if (possibleMoves.contains(move)){
				return move;
			}
			else{
				System.out.println("Move (" + move.toString() + ") is not an available move");
			}*/
			for (Move m : possibleMoves){
				if (m.getInitSquare().getX() == coords[0] && m.getInitSquare().getY() == coords[1] &&
					m.getDestSquare().getX() == coords[2] && m.getDestSquare().getY() == coords[3])
					return m;
			}
			System.out.println("This move is not an available move; please re-enter");
		}

	}
}