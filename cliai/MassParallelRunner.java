/*
 * Copyright (C) 2018 Lucas Burdell <lucasburdell@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cliai;

import aidecision.AIDecider;
import aidecision.MajorityVoting;
import aidecision.RandomBagVoting;
import aiheuristics.HeuristicList;
import aisearch.MultiThreadSearchDesign1;
import aisearch.SingleThreadSearch;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Run multiple games and compare results
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class MassParallelRunner {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter number of games to play: ");
        int gamesToPlay = input.nextInt();
        System.out.println();
        System.out.print("Enter max depth of search: ");
        int maxDepth = input.nextInt();
//        System.out.print("Enter number of threads: ");
//        int threadCount = input.nextInt();
//        int maxDepth = 3;
        int threadCount = Runtime.getRuntime().availableProcessors();
        final int[] scoreResults = new int[gamesToPlay];
        final GameBoard[] finalBoards = new GameBoard[gamesToPlay];
        GameController controller = new GameController();
        //MultiThreadSearchDesign1
        //MultiThreadSearchDesign1 searcher = new MultiThreadSearchDesign1(controller);
        SingleThreadSearch searcher = new SingleThreadSearch(controller);
        //searcher.setEvaluateAfterstates(true);
        searcher.setMaximumDepth(maxDepth);
        AIDecider decider = new RandomBagVoting();
        //searcher.setDebugMessagesEnabled(true);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Future[] futures = new Future[gamesToPlay];
        for (int i = 0; i < gamesToPlay; i++) {
            final int gameId = i;
            futures[gameId] = executor.submit(() -> {
                GameBoard currentBoard = controller.createStartingGameboard();
                long moveCount = 0;
                long startTime = System.currentTimeMillis();
                while (!controller.isGameOver(currentBoard)) {
                    int[] votes = searcher.getVotesOnDirections(currentBoard, HeuristicList.getHeuristics());
                    Direction decision = decider.evaluateVotes(votes);
                    currentBoard = controller.doGameMove(currentBoard, decision);
                    //System.out.println(currentBoard.getScore());
                    //System.out.println(currentBoard);
                    //System.out.println("Moved above board " + decision);
                    if (moveCount % 20L == 0) {
                        //System.out.println("moves made this game: " + moveCount);
                    }
                    moveCount++;

                }
                long endTime = System.currentTimeMillis();
                scoreResults[gameId] = currentBoard.getScore();
                finalBoards[gameId] = currentBoard;
                System.out.println("final score: " + currentBoard.getScore());
                System.out.println("game " + gameId + " complete");
                System.out.println("game took: " + (endTime - startTime) / 1000.0 + " seconds");
            });
        }
        for (Future future : futures) {
            try {
                future.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(MassParallelRunner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(MassParallelRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        executor.shutdown();
        System.out.println("Mean: " + getMean(scoreResults));
        System.out.println("Standard Deviation: " + getStandardDeviation(scoreResults));
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
        try (PrintWriter writer = new PrintWriter(boardFile)){
            writer.println("gameid,board");
            for (int i = 0; i < finalBoards.length; i++) {
                GameBoard finalBoard = finalBoards[i];
                writer.println(i + "," + finalBoard.toStorageString());
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace(System.err);
        }
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
}
