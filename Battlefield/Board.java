import chess.*;

import java.lang.reflect.Constructor;
import java.io.*;

public class Board {
	public static void main(String[] args) throws Exception {
        BoardState board = new BoardState();
        board.showPositions();

        System.out.println("Player 1: " + args[0]);
        System.out.println("Player 2: " + args[1]);

        Class<?> clazz = Class.forName(args[0]);
        Class<?> clazz2 = Class.forName(args[1]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Constructor<?> ctor2 = clazz2.getConstructors()[0];
        Player p1 = (Player) ctor.newInstance(new Object[]{Player.Color.White});
        Player p2 = (Player) ctor2.newInstance(new Object[]{Player.Color.Black});

        Move playerMove;

        System.out.println("______________\n");
        while(true){
            System.out.println("Turn: " + (board.getMoveCount()+1));

            playerMove= p1.getMove(board);
            System.out.println(playerMove.toString() + "\n");
            board = board.makeMove(playerMove);
            board.display();
            if (isGameOver(board)){
                break;
            }

            System.out.println("______________\n");
            System.out.println("Turn: " + (board.getMoveCount()+1));

            playerMove = p2.getMove(board);
            System.out.println(playerMove.toString() + "\n");
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