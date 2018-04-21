package cliai;

import aidecision.AIDecider;
import aidecision.MajorityTieVoting;
import aidecision.MajorityVoting;
import aiheuristics.BestList;
import aiheuristics.Heuristic;
import aiheuristics.HeuristicList;
import aisearch.DepthWeighting;
import aisearch.SingleThreadSearch;
import aisearch.StateEvaluationType;
import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.Scanner;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class Runner {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        GameController controller = new GameController();
        GameBoard board = controller.createStartingGameboard();
        System.out.print("enter max depth: ");
        int maxDepth = scanner.nextInt();
        System.out.println();
        SingleThreadSearch searcher = new SingleThreadSearch(controller, maxDepth);
        searcher.setEvaluationType(StateEvaluationType.NEXT_STATES);
        searcher.setDepthWeightingType(DepthWeighting.NONE);
        searcher.setDepthScaling(true);
        //searcher.setDebugMessagesEnabled(true);
        Heuristic[] heuristics = BestList.getHeuristics();//HeuristicList.getHeuristics();
        //AIDecider decider = new MajorityTieVoting(heuristics, 4, 6);
        AIDecider decider = new MajorityVoting(heuristics);
        

        while (!controller.isGameOver(board)) {
            System.out.println(board);
            System.out.println();
            System.out.println("score: " + board.getScore());
            int[] votes = searcher.getVotesOnDirections(board, heuristics);
            Direction decision = decider.evaluateVotes(votes);
            System.out.println("Board is moving " + decision);
            board = controller.doGameMove(board, decision);
            System.out.println("Current score: " + board.getScore());
            //int move = scanner.nextInt();
            //Direction[] directions = Direction.values();
            //if (move > -1 && move < directions.length) {
            //board = controller.doGameMove(board, directions[move]);
            //board = controller.moveGrid(board, directions[move]).getKey();
            //}
        }
        System.out.println(board);
        System.out.println("Game over!");

    }

}
