package aisearch;

import aiheuristics.Heuristic;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.ArrayList;
import gamemodel.Direction;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lucas.burdell
 */
public class MultiThreadSearchDesign1 {

    /**
     * @return the weightOnDepths
     */
    public boolean isWeightOnDepths() {
        return weightOnDepths;
    }

    /**
     * @param weightOnDepths the weightOnDepths to set
     */
    public void setWeightOnDepths(boolean weightOnDepths) {
        this.weightOnDepths = weightOnDepths;
    }

    /**
     * @return the depthWeight
     */
    public double getDepthWeight() {
        return depthWeight;
    }

    /**
     * @param depthWeight the depthWeight to set
     */
    public void setDepthWeight(double depthWeight) {
        this.depthWeight = depthWeight;
    }

    /**
     * @return the debugMessagesEnabled
     */
    public boolean isDebugMessagesEnabled() {
        return debugMessagesEnabled;
    }

    /**
     * @param debugMessagesEnabled the debugMessagesEnabled to set
     */
    public void setDebugMessagesEnabled(boolean debugMessagesEnabled) {
        this.debugMessagesEnabled = debugMessagesEnabled;
    }

    private boolean evaluateAfterstates = false;
    private int maxDepth = 4;
    private GameController controller;
    private Random random = new Random(); // random for choosing between winners
    private boolean debugMessagesEnabled = false;
    private double depthWeight = 1;
    private boolean weightOnDepths = true;
    private int numberOfThreads = 16;

    private void println(Object message) {
        if (isDebugMessagesEnabled()) {
            System.out.println(message);
        }
    }

    /**
     * @return the evaluateAfterstates
     */
    public boolean isEvaluateAfterstates() {
        return evaluateAfterstates;
    }

    /**
     * @param aEvaluateAfterstates the evaluateAfterstates to set
     */
    public void setEvaluateAfterstates(boolean aEvaluateAfterstates) {
        evaluateAfterstates = aEvaluateAfterstates;
    }

    /**
     * @return the maximumDepth
     */
    public int getMaximumDepth() {
        return maxDepth;
    }

    /**
     * @param aMaximumDepth the maximumDepth to set
     */
    public void setMaximumDepth(int aMaximumDepth) {
        maxDepth = aMaximumDepth;
    }

    
    public int[] getVotesOnDirections(GameBoard currentBoard, Heuristic[] heuristics) {
        final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        final Direction[] directions = Direction.values();
        final Queue<GameBoard>[] directionQueues = new ConcurrentLinkedQueue[directions.length];
        final long[][] heuristicSums = new long[directions.length][heuristics.length];
        final GameController gameController = this.controller;
        
        for (int i = 0; i < directionQueues.length; i++) {
            directionQueues[i] = new ConcurrentLinkedQueue<>();
        }

        int currentDepth = 1;
        Object elementsLock = new Object();
        long elementsToDepthIncrease = directions.length;
        long nextElementsToDepthIncrease = 0;

        for (int i = 0; i < directions.length; i++) {
            final GameBoard result = controller.moveGrid(currentBoard, directions[i]);
            if (result.isMoved()) {
                for (int j = 0; j < heuristics.length; j++) {
                    final Heuristic heuristic = heuristics[j];
                    final int x = i;
                    final int y = j;
                    // evaluate state of board
                    executor.submit(() -> {
                        heuristicSums[x][y] += heuristic.getValueOfState(gameController, result, x);
                    });

                }
            }
            if (result.isMoved() && currentDepth <= maxDepth) {
                if (!this.evaluateAfterstates) {
                    GameBoard[] newStates = controller.createAllPossibleNewStates(result);

                    for (GameBoard newState : newStates) {
                        directionQueues[i].add(newState);
                        nextElementsToDepthIncrease++;
                    }
                } else {
                    directionQueues[i].add(result);
                    nextElementsToDepthIncrease++;
                }
            }
        }

        while (!queuesAreEmpty(directionQueues) && currentDepth <= maxDepth) {
            for (int i = 0; i < directions.length; i++) {
                Direction direction = directions[i];
                Queue<GameBoard> queue = directionQueues[i];
                if (queue.isEmpty()) {
                    continue; // skip to next iteration, this queue's empty!
                }
                GameBoard nextBoard = queue.poll();
                elementsToDepthIncrease--;
                GameBoard afterState = controller.moveGrid(nextBoard, direction);

                if (afterState.isMoved() && currentDepth <= maxDepth) {
                    if (!this.evaluateAfterstates) {
                        GameBoard[] newStates = controller.createAllPossibleNewStates(afterState);

                        for (GameBoard newState : newStates) {
                            queue.add(newState);
                            nextElementsToDepthIncrease++;
                        }
                    } else {
                        queue.add(afterState);
                        nextElementsToDepthIncrease++;
                    }
                }
                if (afterState.isMoved()) {
                    for (int j = 0; j < heuristics.length; j++) {
                        final Heuristic heuristic = heuristics[j];
                        final int x = i;
                        final int y = j;
                        final int curDepth = currentDepth;
                        // evaluate state of board
                        executor.submit(() -> {
                            if (this.isWeightOnDepths()) {
                                //heuristicSums[i][j] += Math.pow(heuristic.getValueOfState(controller, nextBoard), getDepthWeight() / currentDepth) ;
                                double x2 = 1 / curDepth;
                                double y2 = Math.pow(0.51457317283, x2);
                                heuristicSums[x][y] += heuristic.getValueOfState(controller, nextBoard, x) * y2;
                            } else {
                                heuristicSums[x][y] += heuristic.getValueOfState(controller, nextBoard, x);
                            }
                        });
                    }
                }

                if (elementsToDepthIncrease <= 0) {
                    currentDepth++;
                    elementsToDepthIncrease = nextElementsToDepthIncrease;
                    nextElementsToDepthIncrease = 0;

                }
            }
        }

        int[] votes = new int[directions.length];
        for (int i = 0; i < heuristics.length; i++) {

            ArrayList<Integer> sameList = new ArrayList<>();
            long highestSum = 0;

            for (int j = 0; j < directions.length; j++) {
                println(heuristics[i] + " on " + directions[j] + " "
                        + "scored: " + heuristicSums[j][i]);
                if (heuristicSums[j][i] > highestSum) {
                    highestSum = heuristicSums[j][i];
                    sameList.clear();
                    sameList.add(j);
                } else if (heuristicSums[j][i] == highestSum) {
                    sameList.add(j);
                }
            }
            int choice = 0;
            if (sameList.isEmpty()) {
                throw new RuntimeException("SameList was empty for "
                        + heuristics[i]);
            } else if (sameList.size() > 1) {
                println(heuristics[i] + " scored the same for "
                        + "these directions: ");
                for (Integer index : sameList) {
                    println(directions[index]);
                }
                choice = sameList.get(random.nextInt(sameList.size()));
                println("Randomly chose "
                        + directions[(choice)] + " for " + heuristics[i]);

            } else {
                choice = sameList.get(0);
            }
            votes[choice]++;
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            System.err.println("Executor failed to terminate in time: " + executor);
        }
        //System.out.println(executor);
        return votes;
    }

    private boolean queuesAreEmpty(Queue[] queues) {
        for (int i = 0; i < queues.length; i++) {
            Queue queue = queues[i];
            if (!queue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public MultiThreadSearchDesign1(GameController controller) {
        this.controller = controller;
    }

    public MultiThreadSearchDesign1(GameController controller, int maxDepth) {
        this.controller = controller;
        this.maxDepth = maxDepth;
    }

    /**
     * @return the numberOfThreads
     */
    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    /**
     * @param numberOfThreads the numberOfThreads to set
     */
    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }
}
