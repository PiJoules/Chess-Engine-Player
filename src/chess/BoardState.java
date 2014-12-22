package chess;


import java.util.*;

import chess.Player.Color;
import chess.Piece.Type;

// The current state of the board
public class BoardState {

	private Square[][] board;
	private int moveCount;
	private int whiteMovesLeft = 50;
	private int blackMovesLeft = 50;
	private Move lastMove; // record the last move made for display
	private boolean verbose = false, showingPositions = false;
	private Color winner = null; // will stay null in event of a tie

	// The current player making the move
	// The first player is white
	private Color nextPlayer = Color.White;


	public BoardState(){
		board = new Square[8][8];
		
		Type[] types = {Type.Rook, Type.Knight, Type.Bishop, Type.Queen, Type.King, Type.Bishop, Type.Knight, Type.Rook};

		// White always starts on top row (0)
		for (int y = 0; y < 8; y++){
			for (int x = 0; x < 8; x++){
				if (y == 0){
					board[y][x] = new Square(new Piece(types[x],Color.White), x, 0);
				}
				else if (y == 7){
					board[y][x] = new Square(new Piece(types[x],Color.Black), x, 7);
				}
				else if (y == 1){
					board[y][x] = new Square(new Piece(Type.Pawn,Color.White), x, 1);
				}
				else if (y == 6){
					board[y][x] = new Square(new Piece(Type.Pawn,Color.Black), x, 6);
				}
				else{
					board[y][x] = new Square(null, x, y);
				}
			}
		}
		
		//Initialize the move count
		moveCount = 0;
	}

	// Copy constructor
	private BoardState(Square[][] board, int moveCount){
		this.board = board;
		this.moveCount = moveCount;
	}

	public void makeVerbose(){
		verbose = true;
	}
	public void showPositions(){
		showingPositions = true;
	}

	// Return all moves that can be made ()
	// Possible moves are dependant on what moves are legal for each piece
	// with the player color
	public ArrayList<Move> getAllMoves(Color playerColor){
		ArrayList<Move> availableMoves = new ArrayList<>();

		for (int y = 0; y < 8; y++){
			for (int x = 0; x < 8; x++){
				Square s = getSquare(x,y);
				Piece p = s.getPiece();
				if (p == null)
					continue;
				if (p.getColor() == playerColor){
					// Pawn can move forward 1 space, 2 spaces, or diagnolly to consume pieces
					if (p.getType() == Type.Pawn){
						if (playerColor == Color.White){
							// Can only move 1 space forward if square not occupied
							if (y <= 6){
								Square destSquare = getSquare(x,y+1);
								if (destSquare.getPiece() == null)
									availableMoves.add(new Move(s, destSquare));
							}

							// Can only move 2 spaces forward if both squares aren't occupied
							// and the pawn has not moved before
							if (y <= 5 && !p.hasMoved()){
								Square destSquare = getSquare(x,y+1);
								Square destSquare2 = getSquare(x,y+2);
								if (destSquare.getPiece() == null && destSquare2.getPiece() == null)
									availableMoves.add(new Move(s, destSquare2));
							}

							// Can only move diagnally if both diagnol squares are occupied by
							// an enemy piece
							if (x >= 1 && y <= 6){
								Square destSquare = getSquare(x-1,y+1);
								if (destSquare.getPiece() != null)
									if (destSquare.getPiece().getColor() != playerColor)
										availableMoves.add(new Move(s, destSquare));
							}
							if (x <= 6 && y <= 6){
								Square destSquare = getSquare(x+1,y+1);
								if (destSquare.getPiece() != null)
									if (destSquare.getPiece().getColor() != playerColor)
										availableMoves.add(new Move(s, destSquare));
							}
						}
						else if (playerColor == Color.Black){
							// Can only move 1 space forward if square not occupied
							if (y >= 1){
								Square destSquare = getSquare(x,y-1);
								if (destSquare.getPiece() == null)
									availableMoves.add(new Move(s, destSquare));
							}

							// Can only move 2 spaces forward if both squares aren't occupied
							// and the pawn has not moved before
							if (y >= 2 && !p.hasMoved()){
								Square destSquare = getSquare(x,y-1);
								Square destSquare2 = getSquare(x,y-2);
								if (destSquare.getPiece() == null && destSquare2.getPiece() == null)
									availableMoves.add(new Move(s, destSquare2));
							}

							// Can only move diagnolly if both diagnol squares are occupied by
							// an enemy piece
							if (x >= 1 && y >= 1){
								Square destSquare = getSquare(x-1,y-1);
								if (destSquare.getPiece() != null)
									if (destSquare.getPiece().getColor() != playerColor)
										availableMoves.add(new Move(s, destSquare));
							}
							if (x <= 6 && y >= 1){
								Square destSquare = getSquare(x+1,y-1);
								if (destSquare.getPiece() != null)
									if (destSquare.getPiece().getColor() != playerColor)
										availableMoves.add(new Move(s, destSquare));
							}
						}
					}

					// Rook can move up to 7 spaces up, down, left, or right depending on what
					// pieces are in the way if any
					if (p.getType() == Type.Rook){
						availableMoves.addAll(getMovesInCrossSection(s, p));
					}

					// Bishop can move up to 7 spaces in any diagnol depending on what
					// pieces are in the way if any
					if (p.getType() == Type.Bishop){
						availableMoves.addAll(getMovesInDiagnol(s, p));
					}

					// Queen is just a combination of the previous 2 getMoves...
					if (p.getType() == Type.Queen){
						availableMoves.addAll(getMovesInCrossSection(s, p));
						availableMoves.addAll(getMovesInDiagnol(s, p));
					}

					// Knight moves in L shapes starting with 2 spaces either up, down,
					// left, or right followed by 1 space in a direction perpendicular to
					// the original one
					// Can only move onto that space also if not occupied by a piece of the
					// same color
					// 
					// Probably should check each of the 8 squares individually
					if (p.getType() == Type.Knight){
						if (s.getY()-2 >= 0){
							if (s.getX()-1 >= 0){
								Piece p2 = getSquare(x-1,y-2).getPiece();
								if (p2 == null)
									availableMoves.add(new Move(s, getSquare(s.getX()-1, s.getY()-2)));
								else if (p2.getColor() != playerColor)
									availableMoves.add(new Move(s, getSquare(s.getX()-1, s.getY()-2)));
							}
							if (s.getX()+1 < 8){
								Piece p2 = getSquare(x+1,y-2).getPiece();
								if (p2 == null)
									availableMoves.add(new Move(s, getSquare(s.getX()+1, s.getY()-2)));
								else if (p2.getColor() != playerColor)
									availableMoves.add(new Move(s, getSquare(s.getX()+1, s.getY()-2)));
							}
						}
						if (s.getY()+2 < 8){
							if (s.getX()-1 >= 0){
								Piece p2 = getSquare(x-1,y+2).getPiece();
								if (p2 == null)
									availableMoves.add(new Move(s, getSquare(s.getX()-1, s.getY()+2)));
								else if (p2.getColor() != playerColor)
									availableMoves.add(new Move(s, getSquare(s.getX()-1, s.getY()+2)));
							}
							if (s.getX()+1 < 8){
								Piece p2 = getSquare(x+1,y+2).getPiece();
								if (p2 == null)
									availableMoves.add(new Move(s, getSquare(s.getX()+1, s.getY()+2)));
								else if (p2.getColor() != playerColor)
									availableMoves.add(new Move(s, getSquare(s.getX()+1, s.getY()+2)));
							}
						}
						if (s.getX()-2 >= 0){
							if (s.getY()-1 >= 0){
								Piece p2 = getSquare(x-2,y-1).getPiece();
								if (p2 == null)
									availableMoves.add(new Move(s, getSquare(s.getX()-2, s.getY()-1)));
								else if (p2.getColor() != playerColor)
									availableMoves.add(new Move(s, getSquare(s.getX()-2, s.getY()-1)));
							}
							if (s.getY()+1 < 8){
								Piece p2 = getSquare(x-2,y+1).getPiece();
								if (p2 == null)
									availableMoves.add(new Move(s, getSquare(s.getX()-2, s.getY()+1)));
								else if (p2.getColor() != playerColor)
									availableMoves.add(new Move(s, getSquare(s.getX()-2, s.getY()+1)));
							}
						}
						if (s.getX()+2 < 8){
							if (s.getY()-1 >= 0){
								Piece p2 = getSquare(x+2,y-1).getPiece();
								if (p2 == null)
									availableMoves.add(new Move(s, getSquare(s.getX()+2, s.getY()-1)));
								else if (p2.getColor() != playerColor)
									availableMoves.add(new Move(s, getSquare(s.getX()+2, s.getY()-1)));
							}
							if (s.getY()+1 < 8){
								Piece p2 = getSquare(x+2,y+1).getPiece();
								if (p2 == null)
									availableMoves.add(new Move(s, getSquare(s.getX()+2, s.getY()+1)));
								else if (p2.getColor() != playerColor)
									availableMoves.add(new Move(s, getSquare(s.getX()+2, s.getY()+1)));
							}
						}
					}

					// King moves 1 space on any direction as long as it doesn't have 
					// a piece of the same color and it doesn't walk into an enemy check
					if (p.getType() == Type.King){
						for (int y2 = Math.max(0,y-1); y2 < Math.min(8,y+2); y2++){
							for (int x2 = Math.max(0,x-1); x2 < Math.min(8,x+2); x2++){
								if (!(x2 == x && y2 == y)){

									if (!opponentKingIsInAdjascentSquareTo(getSquare(x2,y2), playerColor)){
										// Test if king will be in check from a move
										Piece p2 = getSquare(x2,y2).getPiece();
										if (p2 == null){
											Move m = new Move(s, getSquare(x2,y2));
											BoardState clonedState = this.makeMoveCloning(m);
											if (!clonedState.playerIsInCheck(playerColor)){
												availableMoves.add(m);
											}
										}
										else if (p2.getColor() != playerColor){
											Move m = new Move(s, getSquare(x2,y2));
											BoardState clonedState = this.makeMoveCloning(m);
											if (!clonedState.playerIsInCheck(playerColor)){
												availableMoves.add(m);
											}
										}
									}

								}
							}
						}
					}

				}
			}
		}


		// If in check, remove all moves that don't lead to an out of check move
		if (this.playerIsInCheck(playerColor)){
			for (int i = availableMoves.size()-1; i >= 0; i--){
				BoardState clonedState = this.makeMoveCloning(availableMoves.get(i));
				if (clonedState.playerIsInCheck(playerColor)){
					availableMoves.remove(i);
				}
			}
		}

		return availableMoves;
	}

	private boolean opponentKingIsInAdjascentSquareTo(Square mySquare, Color myColor){
		for (int y = Math.max(0,mySquare.getY()-1); y < Math.min(8,mySquare.getY()+2); y++){
			for (int x = Math.max(0,mySquare.getX()-1); x < Math.min(8,mySquare.getX()+2); x++){
				if (x == mySquare.getX() && y == mySquare.getY())
					continue;
				Piece p = getSquare(x,y).getPiece();
				if (p != null){
					if (p.getType() == Type.King && p.getColor() != myColor){
						return true;
					}
				}
			}
		}
		return false;
	}

	// Utility for getAllMoves() method but only for those available
	// directly up, down, left, or right of the piece on the given square
	//
	// Available moves include the last piece on any direction if that piece
	// is the opponent's and exlude the piece if it's the player's.
	// 
	// This also assumes the piece is either a Rook or Queen
	private ArrayList<Move> getMovesInCrossSection(Square s, Piece p){
		ArrayList<Move> availableMoves = new ArrayList<>();
		Color pieceColor = p.getColor();

		// Check up
		for (int y = s.getY()-1; y >= 0; y--){
			Square destSquare = getSquare(s.getX(), y);
			Piece occupyingPiece = destSquare.getPiece();
			if (occupyingPiece == null)
				availableMoves.add(new Move(s, destSquare));
			else if (occupyingPiece.getColor() != p.getColor()){
				availableMoves.add(new Move(s, destSquare));
				break;
			}
			else
				break;
		}

		// Check down
		for (int y = s.getY()+1; y < 8; y++){
			Square destSquare = getSquare(s.getX(), y);
			Piece occupyingPiece = destSquare.getPiece();
			if (occupyingPiece == null)
				availableMoves.add(new Move(s, destSquare));
			else if (occupyingPiece.getColor() != p.getColor()){
				availableMoves.add(new Move(s, destSquare));
				break;
			}
			else
				break;
		}

		// Check left
		for (int x = s.getX()-1; x >= 0; x--){
			Square destSquare = getSquare(x, s.getY());
			Piece occupyingPiece = destSquare.getPiece();
			if (occupyingPiece == null)
				availableMoves.add(new Move(s, destSquare));
			else if (occupyingPiece.getColor() != p.getColor()){
				availableMoves.add(new Move(s, destSquare));
				break;
			}
			else
				break;
		}

		// Check right
		for (int x = s.getX()+1; x < 8; x++){
			Square destSquare = getSquare(x, s.getY());
			Piece occupyingPiece = destSquare.getPiece();
			if (occupyingPiece == null)
				availableMoves.add(new Move(s, destSquare));
			else if (occupyingPiece.getColor() != p.getColor()){
				availableMoves.add(new Move(s, destSquare));
				break;
			}
			else
				break;
		}

		return availableMoves;
	}

	// Utility for getAllMoves() method but only for those available
	// in the diagnol of the piece on the given square
	//
	// Available moves include the last piece on any direction if that piece
	// is the opponent's and exlude the piece if it's the player's.
	// 
	// This also assumes the piece is either a Bishop or Queen
	private ArrayList<Move> getMovesInDiagnol(Square s, Piece p){
		ArrayList<Move> availableMoves = new ArrayList<>();
		Color pieceColor = p.getColor();

		// Up and to the left
		for (int i = 1, x = s.getX(), y = s.getY(); x-i >= 0 && y-i >= 0; i++){
			Square destSquare = getSquare(x-i, y-i);
			Piece occupyingPiece = destSquare.getPiece();
			if (occupyingPiece == null)
				availableMoves.add(new Move(s, destSquare));
			else if (occupyingPiece.getColor() != p.getColor()){
				availableMoves.add(new Move(s, destSquare));
				break;
			}
			else
				break;
		}

		// Up and to the right
		for (int i = 1, x = s.getX(), y = s.getY(); x+i < 8 && y-i >= 0; i++){
			Square destSquare = getSquare(x+i, y-i);
			Piece occupyingPiece = destSquare.getPiece();
			if (occupyingPiece == null)
				availableMoves.add(new Move(s, destSquare));
			else if (occupyingPiece.getColor() != p.getColor()){
				availableMoves.add(new Move(s, destSquare));
				break;
			}
			else
				break;
		}

		// Down and to the right
		for (int i = 1, x = s.getX(), y = s.getY(); x+i < 8 && y+i < 8; i++){
			Square destSquare = getSquare(x+i, y+i);
			Piece occupyingPiece = destSquare.getPiece();
			if (occupyingPiece == null)
				availableMoves.add(new Move(s, destSquare));
			else if (occupyingPiece.getColor() != p.getColor()){
				availableMoves.add(new Move(s, destSquare));
				break;
			}
			else
				break;
		}

		// Down and to the left
		for (int i = 1, x = s.getX(), y = s.getY(); x-i >= 0 && y+i < 8; i++){
			Square destSquare = getSquare(x-i, y+i);
			Piece occupyingPiece = destSquare.getPiece();
			if (occupyingPiece == null)
				availableMoves.add(new Move(s, destSquare));
			else if (occupyingPiece.getColor() != p.getColor()){
				availableMoves.add(new Move(s, destSquare));
				break;
			}
			else
				break;
		}

		return availableMoves;
	}

	// Get a square based on coordinates
	private Square getSquare(int x, int y){
		return board[y][x];
	}


	// Update the state
	// A move will be when one piece is removed from one another square
	// and copied onto another
	public BoardState makeMove(Move move){
		lastMove = move;
		moveCount++;

		Piece movingPiece = move.getMovingPiece();
		Square initSquare = move.getInitSquare();
		Square destSquare = move.getDestSquare();
		if (movingPiece.getType() == Type.Pawn){
			// Will automatically be a queen for now
			if (movingPiece.getColor() == Color.White && destSquare.getY() == 7)
				destSquare.setPiece(new Piece(Type.Queen, Color.White, true));
			else if (movingPiece.getColor() == Color.Black && destSquare.getY() == 0)
				destSquare.setPiece(new Piece(Type.Queen, Color.Black, true));
			else
				destSquare.setPiece(new Piece(movingPiece));
		}
		else
			destSquare.setPiece(new Piece(movingPiece));
		initSquare.setPiece(null);

		// Swicth players after move is made
		if (nextPlayer == Color.White)
			nextPlayer = Color.Black;
		else if (nextPlayer == Color.White)
			nextPlayer = Color.Black;
		return this;
	}

	//Display the board
	public void display(){
		// Mark the previous square of the moving
		// piece with an x
		int lastX = lastMove.getInitSquare().getX();
		int lastY = lastMove.getInitSquare().getY();
		char lastPosChar = 'X'; // for last move of white
		if (lastMove.getDestSquare().getPiece().getColor() == Color.Black) // the piece is now on the destSquare instead of the init square
			lastPosChar = 'x';

		if (showingPositions){
			System.out.print("   ");
			for (int i = 0; i < 8; i++)
				System.out.print(i + " ");
			System.out.println("\n");
		}

		for(int y = 0; y < 8; y ++){
			if (showingPositions){
				System.out.print(y + "  ");
			}
			for(int x = 0; x < 8; x ++){
				Piece p = board[y][x].getPiece();
				if (p != null)
					System.out.print(p.getCharacter());
				else if (x == lastX && y == lastY)
					System.out.print(lastPosChar);
				else
					System.out.print(' ');
				if (x < 7)
					System.out.print("|");
			}
			if (y < 7){
				System.out.println("");
				if (showingPositions)
					System.out.print("   ");
				for (int i = 0; i < 15; i++){
					System.out.print("-");
				}
			}
			System.out.println("");
		}
	}


	// Clone the state then make move
	public BoardState makeMoveCloning(Move move){
		Square[][] clonedBoard = new Square[8][8];
		//Clone the board
		for(int i = 0; i < 8; i ++){
			for(int j = 0; j < 8; j ++){
				clonedBoard[i][j] = board[i][j].fastCopy();
			}
		}
		//Clone the state
		BoardState clone = new BoardState(clonedBoard, this.moveCount);
		int initSquareX = move.getInitSquare().getX();
		int initSquareY = move.getInitSquare().getY();
		int destSquareX = move.getDestSquare().getX();
		int destSquareY = move.getDestSquare().getY();
		Move moveOnClonedBoard = new Move(clone.getSquare(initSquareX, initSquareY), clone.getSquare(destSquareX, destSquareY));
		clone = clone.makeMove(moveOnClonedBoard);
		return clone;
	}


	public int getMoveCount(){
		return this.moveCount;
	}

	public boolean playerIsInCheck(Color color){
		Color opponentColor = Color.White;
		if (color == Color.White)
			opponentColor = Color.Black;
		for (int y = 0; y < 8; y++){
			for (int x = 0; x < 8; x++){
				Square s = getSquare(x,y);
				Piece p = s.getPiece();
				if (p != null){
					if (p.getColor() == color && p.getType() == Type.King){
						// Check if Rook or queen is in the same cross-section as this king
						// Check up
						for (int y2 = y-1; y2 >= 0; y2--){
							Piece p2 = getSquare(x, y2).getPiece();
							if (p2 != null){
								if (p2.getColor() == opponentColor){
									if (p2.getType() == Type.Rook || p2.getType() == Type.Queen)
										return true;
									else
										break;
								}
								else if (p2.getColor() == color)
									break;
							}
						}

						// Check down
						for (int y2 = y+1; y2 < 8; y2++){
							Piece p2 = getSquare(x, y2).getPiece();
							if (p2 != null){
								if (p2.getColor() == opponentColor){
									if (p2.getType() == Type.Rook || p2.getType() == Type.Queen)
										return true;
									else
										break;
								}
								else if (p2.getColor() == color)
									break;
							}
						}

						// Check left
						for (int x2 = x-1; x2 >= 0; x2--){
							Piece p2 = getSquare(x2, y).getPiece();
							if (p2 != null){
								if (p2.getColor() == opponentColor){
									if (p2.getType() == Type.Rook || p2.getType() == Type.Queen)
										return true;
									else
										break;
								}
								else if (p2.getColor() == color)
									break;
							}
						}

						// Check right
						for (int x2 = x+1; x2 < 8; x2++){
							Piece p2 = getSquare(x2, y).getPiece();
							if (p2 != null){
								if (p2.getColor() == opponentColor){
									if (p2.getType() == Type.Rook || p2.getType() == Type.Queen)
										return true;
									else
										break;
								}
								else if (p2.getColor() == color)
									break;
							}
						}

						// Check if Bishop or queen is in the same diagnol as this king
						// Check upper left
						for (int i = 1, x2 = x, y2 = y; x2-i >= 0 && y2-i >= 0; i++){
							Piece p2 = getSquare(x2-i, y2-i).getPiece();
							if (p2 != null){
								if (p2.getColor() == opponentColor){
									if (p2.getType() == Type.Bishop || p2.getType() == Type.Queen)
										return true;
									else
										break;
								}
								else if (p2.getColor() == color)
									break;
							}
						}

						// Check upper right
						for (int i = 1, x2 = x, y2 = y; x2+i < 8 && y2-i >= 0; i++){
							Piece p2 = getSquare(x2+i, y2-i).getPiece();
							if (p2 != null){
								if (p2.getColor() == opponentColor){
									if (p2.getType() == Type.Bishop || p2.getType() == Type.Queen)
										return true;
									else
										break;
								}
								else if (p2.getColor() == color)
									break;
							}
						}

						// Check bottom right
						for (int i = 1, x2 = x, y2 = y; x2+i < 8 && y2+i < 8; i++){
							Piece p2 = getSquare(x2+i, y2+i).getPiece();
							if (p2 != null){
								if (p2.getColor() == opponentColor){
									if (p2.getType() == Type.Bishop || p2.getType() == Type.Queen)
										return true;
									else
										break;
								}
								else if (p2.getColor() == color)
									break;
							}
						}

						// Check bottom left
						for (int i = 1, x2 = x, y2 = y; x2-i >= 0 && y+i < 8; i++){
							Piece p2 = getSquare(x2-i, y2+i).getPiece();
							if (p2 != null){
								if (p2.getColor() == opponentColor){
									if (p2.getType() == Type.Bishop || p2.getType() == Type.Queen)
										return true;
									else
										break;
								}
								else if (p2.getColor() == color)
									break;
							}
						}

						// Check if pawn is in one of the diagnols
						if (color == Color.White){
							// Check lower corners
							if (x >= 1 && y <= 6){
								Piece p2 = getSquare(x-1,y+1).getPiece();
								if (p2 != null){
									if (p2.getColor() == opponentColor && p2.getType() == Type.Pawn){
										return true;
									}
								}
							}
							if (x <= 6 && y <= 6){
								Piece p2 = getSquare(x+1,y+1).getPiece();
								if (p2 != null){
									if (p2.getColor() == opponentColor && p2.getType() == Type.Pawn){
										return true;
									}
								}
							}
						}
						else if (color == Color.Black){
							// Check upper corners
							if (x >= 1 && y >= 1){
								Piece p2 = getSquare(x-1,y-1).getPiece();
								if (p2 != null){
									if (p2.getColor() == opponentColor && p2.getType() == Type.Pawn){
										return true;
									}
								}
							}
							if (x <= 6 && y >= 1){
								Piece p2 = getSquare(x+1,y-1).getPiece();
								if (p2 != null){
									if (p2.getColor() == opponentColor && p2.getType() == Type.Pawn){
										return true;
									}
								}
							}
						}

						// Check if an opponent knight can get this king
						if (s.getY()-2 >= 0){
							if (s.getX()-1 >= 0){
								Piece p2 = getSquare(x-1,y-2).getPiece();
								if (p2 != null)
									if (p2.getColor() != color && p2.getType() == Type.Knight)
										return true;
							}
							if (s.getX()+1 < 8){
								Piece p2 = getSquare(x+1,y-2).getPiece();
								if (p2 != null)
									if (p2.getColor() != color && p2.getType() == Type.Knight)
										return true;
							}
						}
						if (s.getY()+2 < 8){
							if (s.getX()-1 >= 0){
								Piece p2 = getSquare(x-1,y+2).getPiece();
								if (p2 != null)
									if (p2.getColor() != color && p2.getType() == Type.Knight)
										return true;
							}
							if (s.getX()+1 < 8){
								Piece p2 = getSquare(x+1,y+2).getPiece();
								if (p2 != null)
									if (p2.getColor() != color && p2.getType() == Type.Knight)
										return true;
							}
						}
						if (s.getX()-2 >= 0){
							if (s.getY()-1 >= 0){
								Piece p2 = getSquare(x-2,y-1).getPiece();
								if (p2 != null)
									if (p2.getColor() != color && p2.getType() == Type.Knight)
										return true;
							}
							if (s.getY()+1 < 8){
								Piece p2 = getSquare(x-2,y+1).getPiece();
								if (p2 != null)
									if (p2.getColor() != color && p2.getType() == Type.Knight)
										return true;
							}
						}
						if (s.getX()+2 < 8){
							if (s.getY()-1 >= 0){
								Piece p2 = getSquare(x+2,y-1).getPiece();
								if (p2 != null)
									if (p2.getColor() != color && p2.getType() == Type.Knight)
										return true;
							}
							if (s.getY()+1 < 8){
								Piece p2 = getSquare(x+2,y+1).getPiece();
								if (p2 != null)
									if (p2.getColor() != color && p2.getType() == Type.Knight)
										return true;
							}
						}

						// Check if a this king is in the perimeter of an opponent's king
						// Though this is illegal, this will be used to determine where this
						// king can move when creating the list of possible moves
						for (int y2 = Math.max(0,y-1); y < Math.min(8,y+2); y++){
							for (int x2 = Math.max(0,x-1); x < Math.min(8,x+2); x++){
								Piece p2 = getSquare(x2,y2).getPiece();
								if (p2 != null)
									if (p2.getColor() == opponentColor && p2.getType() == Type.King)
										return true;
							}
						}

					}
				}
			}
		}

		return false;
	}

	private int getPiecesCountForPlayer(Color playerColor){
		int count = 0;
		for (int y = 0; y < 8; y++){
			for (int x = 0; x < 8; x++){
				Piece p = getSquare(x,y).getPiece();
				if (p != null){
					if (p.getColor() == playerColor)
						count++;
				}
			}
		}
		return count;
	}

	private boolean kingIsInPlay(Color playerColor){
		for (int y = 0; y < 8; y++){
			for (int x = 0; x < 8; x++){
				Piece p = getSquare(x,y).getPiece();
				if (p != null){
					if (p.getColor() == playerColor && p.getType() == Type.King)
						return true;
				}
			}
		}
		return false;
	}

	public Color getWinner(){
		return winner;
	}

	// Game over if a player has zero available moves that will get them out of check
	public boolean gameOver(){

		ArrayList<Move> availableWhiteMoves = this.getAllMoves(Color.White);
		ArrayList<Move> availableBlackMoves = this.getAllMoves(Color.Black);

		if (!kingIsInPlay(Color.White) || !kingIsInPlay(Color.Black)){
			System.out.println("King is mossing from one player. Something is wrong.");
			return true;
		}

		if (getPiecesCountForPlayer(Color.White) <= 1){
			whiteMovesLeft--;
			if (whiteMovesLeft <= 0)
				return true; // tie
		}
		if (getPiecesCountForPlayer(Color.Black) <= 1){
			blackMovesLeft--;
			if (blackMovesLeft <= 0)
				return true; // tie
		}

		if (availableWhiteMoves.size() == 0){
			winner = Color.Black;
			return true;
		}

		if (availableBlackMoves.size() == 0){
			winner = Color.White;
			return true;
		}

		return false;
	}
}