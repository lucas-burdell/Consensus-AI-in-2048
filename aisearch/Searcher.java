package aisearch;

import aiheuristics.Heuristic;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import gamemodel.Direction;
import java.io.PrintStream;
import java.util.Random;
import searchtree.Tree;
import searchtree.Tree.Node;

/**
 *
 * @author lucas.burdell
 */
public class Searcher {

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
    
    
    private void println(Object message){
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
        Direction[] directions = Direction.values();
        Deque<GameBoard>[] directionQueues = new ArrayDeque[directions.length];
        long[][] heuristicSums = new long[directions.length][heuristics.length];
        for (int i = 0; i < directionQueues.length; i++) {
            directionQueues[i] = new ArrayDeque<>();
        }

        int currentDepth = 1;
        long elementsToDepthIncrease = directions.length;
        long nextElementsToDepthIncrease = 0;

        for (int i = 0; i < directions.length; i++) {
            GameBoard result = controller.moveGrid(currentBoard, directions[i]);
            for (int j = 0; j < heuristics.length; j++) {
                Heuristic heuristic = heuristics[j];
                // evaluate state of board
                heuristicSums[i][j] += heuristic.getValueOfState(controller, result);
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
                Deque<GameBoard> queue = directionQueues[i];
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
                        Heuristic heuristic = heuristics[j];
                        // evaluate state of board
                        heuristicSums[i][j] += heuristic.getValueOfState(controller, nextBoard);
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
                choice = random.nextInt(sameList.size());
                println("Randomly chose "
                        + directions[sameList.get(choice)] + " for " + heuristics[i]);
            } else {
                choice = sameList.get(0);
            }
            votes[choice]++;
        }
        return votes;
    }

    private boolean queuesAreEmpty(Deque[] queues) {
        for (int i = 0; i < queues.length; i++) {
            Deque queue = queues[i];
            if (!queue.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Searcher(GameController controller) {
        this.controller = controller;
    }

    public Searcher(GameController controller, int maxDepth) {
        this.controller = controller;
        this.maxDepth = maxDepth;
    }
}
