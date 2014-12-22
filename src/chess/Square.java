package chess;

import chess.Player.Color;

/*
Squares will just contain the Piece that's on it
and its coordinates.
*/

public class Square {
	private Piece piece; // the piece that is currently on the square; null if no piece
	private int x, y; // goes from 0-7

	public Square(Piece piece, int x, int y){
		this.piece = piece;
		this.x = x;
		this.y = y;
	}
	public Square fastCopy(){
		if (piece == null){
			//System.out.println("FastCopy piece: (null)");
			return new Square(null, x, y);
		}
		//System.out.println("FastCopy piece: " + piece.toString());
		return new Square(new Piece(piece), x, y);
	}
	public Piece getPiece(){
		return this.piece;
	}
	public void setPiece(Piece piece){
		this.piece = piece;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}

	@Override
	// Squares are equal if their coordinates are equal
	public boolean equals(Object square){
		Square s = (Square) square;
		return this.x == s.getX() && this.y == s.getY();
	}

	@Override
	public String toString(){
		if (piece == null)
			return "Piece:(none) " + "(" + x + "," + y + ")";
		return "Piece:'" + piece.toString()  + "' (" + x + "," + y + ")";
	}
}