import chess.*;

import java.util.*;

// A player that will always go for the first move that 
// consumes an opponent's piece (regardless of what piece
// it is)
// If can't get a piece, make a random move
public class FirstOrderPlayer extends Player {

	private static Random random = new Random();

	public FirstOrderPlayer(Color player){
		super(player);
	}

	public Move getMove(BoardState state){
		ArrayList<Move> possibleMoves = state.getAllMoves(this.getColor());

		for (Move m : possibleMoves){
			if (m.getOccupyingPiece() != null)
				return m;
		}

		int choice = random.nextInt(possibleMoves.size());
		return possibleMoves.get(choice);
	}
}