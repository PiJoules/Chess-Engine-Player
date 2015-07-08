package chess;

import chess.Player.Color;

/*
A move will contain the square containing the piece to be moved
(initSquare) and the square that the piece will move to (destSquare).
*/

public class Move {

	private Square initSquare, destSquare;

	public Move(Square initSquare, Square destSquare){
		//this.initSquare = initSquare.fastCopy();
		//this.destSquare = destSquare.fastCopy();
		this.initSquare = initSquare;
		this.destSquare = destSquare;
	}

	public Square getInitSquare(){
		return this.initSquare;
	}
	public Square getDestSquare(){
		return this.destSquare;
	}
	public Piece getMovingPiece(){
		return this.initSquare.getPiece();
	}
	// The piece on the destSquare (if any)
	public Piece getOccupyingPiece(){
		return this.destSquare.getPiece();
	}

	public Move fastCopy(){
		return new Move(initSquare.fastCopy(), destSquare.fastCopy());
	}

	@Override
	public String toString(){
		return initSquare.toString() + " -> " + destSquare.toString();
	}

	@Override
	public boolean equals(Object moveObject){
		Move move = (Move) moveObject;
		return initSquare.equals(move.getInitSquare()) && destSquare.equals(move.getDestSquare());
	}

}