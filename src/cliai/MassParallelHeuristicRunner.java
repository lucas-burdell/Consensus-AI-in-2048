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
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Run multiple games and compare results
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class MassParallelHeuristicRunner {

    //Combinatoric solver from
    // https://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
    // far more elegant than anything I could dream up
    /* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
    static LinkedList combinationUtil(Object arr[], Object data[], int start, int index, int r) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            LinkedList output = new LinkedList();
            for (int i = 0; i < r; i++) {
                Object object = data[i];
                output.add(object);
            }
            return output;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        LinkedList output = new LinkedList();
        for (int i = start; i <= arr.length - 1 && arr.length - i >= r - index; i++) {
            data[index] = arr[i];
            output.addAll(combinationUtil(arr, data, i + 1, index + 1, r));
        }
        return output;
    }

    static Heuristic[][] combinator(Object arr[], int size) {
        LinkedList result = combinationUtil(arr, new Heuristic[size], 0, 0, size);
        Heuristic[][] output = new Heuristic[result.size() / size][size];

        for (int i = 0; i < result.size(); i++) {
            Heuristic object = (Heuristic) result.get(i);
            output[i / size][i % size] = object;
        }
        return output;
    }

    public static void main(String[] args) {
        int gamesToPlay = 100;
        int maxDepth = 2;
        int progressReportIteration = 1;
        int threadCount = Runtime.getRuntime().availableProcessors();
        Heuristic[] heuristics = HeuristicList.getHeuristics();
        int[] combosToDo = new int[]{1, 2, 8, 9};
        for (int k : combosToDo) {
            System.out.println("generating size " + k + " combos");
            Heuristic[][] combos = (Heuristic[][]) combinator(heuristics, k);
            for (int j = 0; j < combos.length; j++) {
                Heuristic[] combo = combos[j];
                System.out.println("Current combo:");
                for (Heuristic heuristic : combo) {
                    System.out.println("\t" + heuristic.getClass().getCanonicalName());
                }
                final int[] scoreResults = new int[gamesToPlay];
                final GameBoard[] finalBoards = new GameBoard[gamesToPlay];

                //searcher.setDebugMessagesEnabled(true);
                ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                Future[] futures = new Future[gamesToPlay];

                GameController controller = new GameController();
                

                SingleThreadSearch searcher = new SingleThreadSearch(controller);
                searcher.setMaximumDepth(maxDepth);
                searcher.setDepthWeightingType(DepthWeighting.NONE);
                searcher.setEvaluationType(StateEvaluationType.NEXT_STATES);
                searcher.setDepthScaling(true);
                searcher.setConsiderFoursForPossibleStates(false);

                //searcher.setDebugMessagesEnabled(true);
                MajorityVoting decider = new MajorityVoting();

                long programStartTime = System.currentTimeMillis();
                for (int i = 0; i < gamesToPlay; i++) {
                    final int gameId = i;
                    futures[gameId] = executor.submit(() -> {

                        GameBoard currentBoard = controller.createStartingGameboard();

                        long moveCount = 0;
                        //long startTime = System.currentTimeMillis();
                        //System.out.println("start playing for " + gameId);
                        while (!controller.isGameOver(currentBoard)) {
                            int[] votes = searcher.getVotesOnDirections(currentBoard, combo);
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
                            //System.out.println("completed " + i + " games");
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace(System.err);
                    }
                }
                executor.shutdown();
                long programEndTime = System.currentTimeMillis();
                System.out.println("Total AI computation time: " + (programEndTime - programStartTime) / 1000.0 + " seconds");
                System.out.println("Mean: " + getMean(scoreResults));
                System.out.println("Standard Deviation: " + getStandardDeviation(scoreResults));
                System.out.println("Max: " + getMaxNumber(scoreResults));
                System.out.println("Min: " + getMinNumber(scoreResults));
                System.out.println("Heuristics used: ");
                for (Heuristic heuristic : combo) {
                    System.out.println("\t" + heuristic.getClass().getCanonicalName());
                }
                File scoreFile = new File("scoreOutput" + k + "-" + j + ".csv");
                File boardFile = new File("boardOutput" + k + "-" + j + ".txt");
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
                    
                    boardFile.setWritable(true);
                    boardFile.setReadable(true);
                    writer.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace(System.err);
                }
            }
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
}
