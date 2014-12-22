import chess.*;

public class Board {
	public static void main(String[] args){
        BoardState board = new BoardState();
        //board.makeVerbose();
        board.showPositions();

        Player p1 = new RandomPlayer(Player.Color.White);
        Player p2 = new RandomPlayer(Player.Color.Black);

        Move playerMove;

        System.out.println("______________\n");
        while(true){
            System.out.println("Turn: " + (board.getMoveCount()+1));

            playerMove= p1.getMove(board);
            System.out.println(playerMove.toString());
            board = board.makeMove(playerMove);
            board.display();
            if (board.gameOver()){
                System.out.println("White wins");
                break;
            }

            System.out.println("______________\n");
            System.out.println("Turn: " + (board.getMoveCount()+1));

            playerMove = p2.getMove(board);
            System.out.println(playerMove.toString());
            board = board.makeMove(playerMove);
            board.display();
            if (board.gameOver()){
                System.out.println("Black wins");
                break;
            }

            System.out.println("______________\n");
        }
	}
}