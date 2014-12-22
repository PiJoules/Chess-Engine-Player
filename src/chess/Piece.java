package chess;

import chess.Player.Color;

/*
Piece will contain the type of piece it is, and it's owner
(based off its color)
*/

public class Piece {
	public enum Type {
		Rook, Knight, Bishop, Queen, King, Pawn
	}

	private Type type;
	private Color color;

	// Pawn properties
	// Can only move 2 spaces if haven't moved before
	private boolean moved = false;

	public Piece(Type type, Color color){
		this.type = type;
		this.color = color;
	}
	// Used once a pieces moves to set moved to true
	public Piece(Type type, Color color, boolean moved){
		this.type = type;
		this.color = color;
		this.moved = moved;
	}
	public Piece(Piece piece){
		this(piece.getType(), piece.getColor(), true);
	}

	public Type getType(){
		return this.type;
	}

	public Color getColor(){
		return this.color;
	}

	public boolean hasMoved(){
		return this.moved;
	}

	public char getCharacter(){
		// R,N (Knight),B,Q,K,P (Uppercase letters) - white
		// r,n,b,q,k,p (Lowercase letters) - black
		char[] pieces = {'R','N','B','Q','K','P'};
		if (color == Color.Black){
			for (int i = 0; i < pieces.length; i++){
				pieces[i] = Character.toLowerCase(pieces[i]);
			}
		}
		if (type == Type.Rook)
			return pieces[0];
		else if (type == Type.Knight)
			return pieces[1];
		else if (type == Type.Bishop)
			return pieces[2];
		else if (type == Type.Queen)
			return pieces[3];
		else if (type == Type.King)
			return pieces[4];
		else if (type == Type.Pawn)
			return pieces[5];

		return ' ';
	}

	@Override
	// Custom .equals method for testing equal pieces
	// Pieces are equal if their types are equal
	public boolean equals(Object piece){
		return this.type == ((Piece) piece).getType();
	}

	@Override
	public String toString(){
		return this.getCharacter() + "";
	}
}