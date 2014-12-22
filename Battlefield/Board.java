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
            if (isGameOver(board)){
                break;
            }

            System.out.println("______________\n");
            System.out.println("Turn: " + (board.getMoveCount()+1));

            playerMove = p2.getMove(board);
            System.out.println(playerMove.toString());
            board = board.makeMove(playerMove);
            board.display();
            if (isGameOver(board)){
                break;
            }

            System.out.println("______________\n");
        }
	}

    private static boolean isGameOver(BoardState board){
        if (board.gameOver()){
            if (board.getWinner() == Player.Color.White)
                System.out.println("White wins");
            else if (board.getWinner() == Player.Color.Black)
                System.out.println("Black wins");
            else if (board.getWinner() == null)
                System.out.println("Is tie");
            return true;
        }

        return false;
    }
}