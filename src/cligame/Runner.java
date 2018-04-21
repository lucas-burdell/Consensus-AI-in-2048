package cligame;

import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.Scanner;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class Runner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameController controller = new GameController();
        GameBoard board = controller.createStartingGameboard();

        while (!controller.isGameOver(board)) {
            System.out.println(board);
            System.out.println();
            System.out.println("Current score: " + board.getScore());
            System.out.print("Input a move (0 = up, 1 = down, 2 = left, 3 = right): ");
            int move = scanner.nextInt();
            Direction[] directions = Direction.values();
            if (move > -1 && move < directions.length) {
                board = controller.doGameMove(board, directions[move]);
                //board = controller.moveGrid(board, directions[move]).getKey();
            }
        }
        System.out.println("Game over!");

    }
}
