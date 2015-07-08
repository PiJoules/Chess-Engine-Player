import chess.*;

import java.lang.reflect.Constructor;
import java.io.*;

public class Board {

    private static int whiteWins = 0;
    private static int blackWins = 0;
    private static int ties = 0;

	public static void main(String[] args) throws Exception {
        System.out.println("");

        int plays = Integer.parseInt(args[2]);
        System.out.println("Player 1: " + args[0]);
        System.out.println("Player 2: " + args[1]);

        int play = 0;
        while (play++ < plays){
            System.out.println("\nGame " + play + ":");

            BoardState board = new BoardState();
            board.showPositions();

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

                board.display();
                playerMove= p1.getMove(board);
                System.out.println(playerMove.toString() + "\n");
                board = board.makeMove(playerMove);
                if (isGameOver(board)){
                    break;
                }

                System.out.println("______________\n");
                System.out.println("Turn: " + (board.getMoveCount()+1));

                board.display();
                playerMove = p2.getMove(board);
                System.out.println(playerMove.toString() + "\n");
                board = board.makeMove(playerMove);
                if (isGameOver(board)){
                    break;
                }

                System.out.println("______________\n");
            }
        }

        System.out.println("______________\n");
        System.out.println("Results after " + plays + " plays:");
        System.out.println(args[0] + " wins: " + whiteWins);
        System.out.println(args[1] + " wins: " + blackWins);
        System.out.println("Ties: " + ties);

        System.out.println("\nClosing engine ... ");
	}

    private static boolean isGameOver(BoardState board){
        if (board.gameOver()){
            if (board.getWinner() == Player.Color.White){
                System.out.println("\nResult: White wins");
                whiteWins++;
            }
            else if (board.getWinner() == Player.Color.Black){
                blackWins++;
                System.out.println("\nResult: Black wins");
            }
            else if (board.getWinner() == null){
                ties++;
                System.out.println("\nResult: tie");
            }
            return true;
        }

        return false;
    }
}