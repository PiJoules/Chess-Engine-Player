package chess;

public abstract class Player {
	public enum Color {
		White, Black
	}
	private Color color;
	// 0 for white, 1 for black
	public Player(Color color){
		this.color = color;
	}
	public Color getColor(){
		return this.color;
	}
	public abstract Move getMove(BoardState state);
}