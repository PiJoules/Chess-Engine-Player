import chess.*;

import java.util.*;

// A player that makes random moves
public class RandomPlayer extends Player {

	private static Random random = new Random();

	public RandomPlayer(Color player){
		super(player);
	}

	public Move getMove(BoardState state){
		ArrayList<Move> possibleMoves = state.getAllMoves(this.getColor());
		int choice = random.nextInt(possibleMoves.size());
		return possibleMoves.get(choice);
	}
}