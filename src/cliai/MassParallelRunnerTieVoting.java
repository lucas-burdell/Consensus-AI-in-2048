package cliai;

import aidecision.MajorityTieVoting;
import aidecision.MajorityVoting;
import aiheuristics.Heuristic;
import aiheuristics.HeuristicList;
import aisearch.DepthWeighting;
import aisearch.SingleThreadSearch;
import aisearch.StateEvaluationType;
import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Run multiple games and compare results
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class MassParallelRunnerTieVoting {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter number of games to play: ");
        int gamesToPlay = input.nextInt();
        System.out.print("Enter max depth of search: ");
        int maxDepth = input.nextInt();
        System.out.print("Enter progress report iteration:");
        int progressReportIteration = input.nextInt();
        int threadCount = Runtime.getRuntime().availableProcessors();
        final int[] scoreResults = new int[gamesToPlay];
        final GameBoard[] finalBoards = new GameBoard[gamesToPlay];
        final boolean[] winResults = new boolean[gamesToPlay];

        //searcher.setDebugMessagesEnabled(true);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Future[] futures = new Future[gamesToPlay];

        GameController controller = new GameController();

        SingleThreadSearch searcher = new SingleThreadSearch(controller);
        searcher.setMaximumDepth(maxDepth);
        searcher.setDepthWeightingType(DepthWeighting.NONE);
        searcher.setEvaluationType(StateEvaluationType.NEXT_STATES);
        searcher.setDepthScaling(false);
        searcher.setConsiderFoursForPossibleStates(false);           

        //searcher.setDebugMessagesEnabled(true);
        Heuristic[] heuristics = HeuristicList.getHeuristics();
        MajorityTieVoting decider = new MajorityTieVoting(heuristics, 4, 6);
        System.out.println("decider primaryA: " + heuristics[4]);
        System.out.println("decider primaryB: " + heuristics[6]);
        decider.setLearning(false);
        //decider.setDebugMessagesEnabled(true);

        long programStartTime = System.currentTimeMillis();
        for (int i = 0; i < gamesToPlay; i++) {
            final int gameId = i;
            futures[gameId] = executor.submit(() -> {

                GameBoard currentBoard = controller.createStartingGameboard();

                long moveCount = 0;
                //long startTime = System.currentTimeMillis();
                //System.out.println("start playing for " + gameId);
                while (!controller.isGameOver(currentBoard)) {
                    int[] votes = searcher.getVotesOnDirections(currentBoard, heuristics);
                    Direction decision = decider.evaluateVotes(votes);
                    currentBoard = controller.doGameMove(currentBoard, decision);
                    //System.out.println("gameid " + gameId + " score: " + currentBoard.getScore());
                    //System.out.println(currentBoard.getScore());
                    //System.out.println(currentBoard);
                    //System.out.println("Moved above board " + decision);
                    if (moveCount % 20L == 0) {
                        //System.out.println("moves made this game: " + moveCount);
                    }
                    moveCount++;

                }
                //long endTime = System.currentTimeMillis();
                scoreResults[gameId] = currentBoard.getScore();
                finalBoards[gameId] = currentBoard;
                winResults[gameId] = getHighestTile(currentBoard.getGameGrid()) >= 11;
                
//                System.out.println("final score: " + currentBoard.getScore());
//                System.out.println("game " + gameId + " complete");
                //System.out.println("game took: " + (endTime - startTime) / 1000.0 + " seconds");
            });
        }
        for (int i = 0; i < futures.length; i++) {
            Future future = futures[i];
            try {
                future.get();
                if (i % progressReportIteration == 0) {
                    System.out.println("completed " + i + " games");
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace(System.err);
            }
        }
        executor.shutdown();
        long programEndTime = System.currentTimeMillis();
        System.out.println("----------------");
        System.out.println("OUTPUT");
        System.out.println("Games played: " + gamesToPlay);
        System.out.println("maximum depth: " + maxDepth);
        System.out.println("Eval type: " + searcher.getEvaluationType());
        System.out.println("Heuristics used: ");
        for (Heuristic heuristic : heuristics) {
            System.out.println("\t" + heuristic.getClass().getCanonicalName());
        }
        System.out.println("Total AI computation time: " + (programEndTime - programStartTime) / 1000.0 + " seconds");
        System.out.println("Mean: " + getMean(scoreResults));
        System.out.println("Standard Deviation: " + getStandardDeviation(scoreResults));
        System.out.println("Max: " + getMaxNumber(scoreResults));
        System.out.println("Min: " + getMinNumber(scoreResults));
        //System.out.println("Primary Agreements: " + decider.getAgreementCount());
        System.out.println("Total Decisions: " + decider.getDecisionCount());
        printWinPercentage(winResults);
        //System.out.println("Agreement percentage: " + ((double) decider.getAgreementCount() / (double) decider.getDecisionCount()));
        //System.out.println("Number of boards: " + GameBoard.getCreations());

        File scoreFile = new File("scoreOutput.csv");
        File boardFile = new File("boardOutput.txt");
        try (PrintWriter writer = new PrintWriter(scoreFile)) {
            writer.println("gameid,gamescore");
            for (int i = 0; i < scoreResults.length; i++) {
                int scoreResult = scoreResults[i];
                writer.println(i + "," + scoreResult);
            }

            scoreFile.setWritable(true);
            scoreFile.setReadable(true);
            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.err);
        }
        try (PrintWriter writer = new PrintWriter(boardFile)) {
            writer.println("gameid,board");
            for (int i = 0; i < finalBoards.length; i++) {
                GameBoard finalBoard = finalBoards[i];
                writer.println(i + "," + finalBoard.toStorageString());
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.err);
        }

//        System.out.println("Calculated weights:");
//        System.out.println(decider.getWeightsReport());
    }

    private static long getMean(int[] scoreResults) {
        long output = 0;
        for (int i = 0; i < scoreResults.length; i++) {
            int scoreResult = scoreResults[i];
            output += scoreResult;
        }
        return output / (long) scoreResults.length;
    }

    private static double getStandardDeviation(int[] scoreResults) {
        long mean = getMean(scoreResults);
        double[] standardResults = new double[scoreResults.length];
        for (int i = 0; i < scoreResults.length; i++) {
            standardResults[i] = Math.pow((scoreResults[i] - mean), 2);
        }
        double output = 0;
        for (int i = 0; i < standardResults.length; i++) {
            double standardResult = standardResults[i];
            output += standardResult;
        }
        return Math.sqrt(output / standardResults.length);
    }

    private static int getMaxNumber(int[] array) {
        int max = 0;
        for (int i = 1; i < array.length; i++) {
            int j = array[i];
            if (j > array[max]) {
                max = i;
            }
        }
        return array[max];
    }

    private static int getMinNumber(int[] array) {
        int min = 0;
        for (int i = 1; i < array.length; i++) {
            int j = array[i];
            if (j < array[min]) {
                min = i;
            }
        }
        return array[min];
    }
    
    private static int getHighestTile(int[][] grid) {
        int highest = 0;
        for (int i = 0; i < grid.length; i++) {
            int[] is = grid[i];
            for (int j = 0; j < is.length; j++) {
                int k = is[j];
                if (k >= highest) {
                    highest = k;
                }
            }
        }
        return highest;
    }
    
    private static void printWinPercentage(boolean[] wins) {
        int winCount = 0;
        int totalCount = wins.length;
        for (int i = 0; i < wins.length; i++) {
            boolean win = wins[i];
            if (win) {
                winCount++;
            }
        }
        System.out.println("Win percentage: " + ((double) winCount / totalCount) + " ("+winCount+"/"+totalCount+")");
    }
}
