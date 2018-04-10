package aisearch;

import aiheuristics.Heuristic;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.ArrayList;
import gamemodel.Direction;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author lucas.burdell
 */
public class SingleThreadSearch {

    private boolean evaluateAfterstates = true;
    private boolean evaluateStates = false;
    private StateEvaluationType evaluationType = StateEvaluationType.AFTERSTATES;
    private int maxDepth = 3;
    private GameController controller;
    private Random random = new Random(); // random for choosing between winners
    private boolean debugMessagesEnabled = false;
    //private double depthWeight = 1;
    //private boolean weightOnDepths = true;
    private DepthWeighting depthWeightingType;
    private double logarithmicDepthWeightPower = .5;
    private boolean depthScaling = false;
    private static final ConcurrentHashMap<String, byte[][][]> stateMap = new ConcurrentHashMap<>();
    

    private boolean considerFoursForPossibleStates = false;

    /**
     * @return the considerFoursForPossibleStates
     */
    public boolean isConsiderFoursForPossibleStates() {
        return considerFoursForPossibleStates;
    }

    /**
     * @param considerFoursForPossibleStates the considerFoursForPossibleStates
     * to set
     */
    public void setConsiderFoursForPossibleStates(boolean considerFoursForPossibleStates) {
        this.considerFoursForPossibleStates = considerFoursForPossibleStates;
    }

    /**
     * @return the depthScaling
     */
    public boolean isDepthScaling() {
        return depthScaling;
    }

    /**
     * @param depthScaling the depthScaling to set
     */
    public void setDepthScaling(boolean depthScaling) {
        this.depthScaling = depthScaling;
    }

    /**
     * @return the evaluationType
     */
    public StateEvaluationType getEvaluationType() {
        return evaluationType;
    }

    public LinkedList<GameBoard> createAllPossibleNewStates(GameBoard board) {
        LinkedList states = new LinkedList();
        
        /*
        // have I done this before?
        byte[][][] oldStates = SingleThreadSearch.stateMap.get(board.toStorageString());
        if (oldStates != null) {
            // yes, rebuild states from existing grids
            for (byte[][] oldState : oldStates) {
                GameBoard newBoard = new GameBoard(oldState);
                newBoard.setPreviousMove(board.getPreviousMove());
                newBoard.setNumberOfMerges(board.getNumberOfMerges());
                states.add(newBoard);
            }
            return states;
        }
        
        // nope, gotta build all the state grids 
         */
        ArrayList<Integer[]> positions = board.getEmptyPositions();
        //byte[][][] gridsToAdd = new byte[positions.size() * 2][][];
        for (int i = 0; i < positions.size(); i++) {
            Integer[] position = positions.get(i);
            GameBoard board2 = new GameBoard(board);
            board2.getGameGrid()[position[0]][position[1]] = (1);
            board2.setPreviousMove(board.getPreviousMove());
            board2.setNumberOfMerges(board.getNumberOfMerges());
            states.add(board2);
            if (this.considerFoursForPossibleStates) {
                GameBoard board4 = new GameBoard(board);
                board4.getGameGrid()[position[0]][position[1]] = (2);
                board4.setPreviousMove(board.getPreviousMove());
                board4.setNumberOfMerges(board.getNumberOfMerges());
                states.add(board4);
            }

            //gridsToAdd[i * 2] = board2.getGameGrid();
            // gridsToAdd[i * 2 + 1] = board4.getGameGrid();
        }

        // add these to the hashmap
        // SingleThreadSearch.stateMap.put(board.toStorageString(), gridsToAdd);
        return states;
    }

    /**
     * @param evaluationType the evaluationType to set
     */
    public void setEvaluationType(StateEvaluationType evaluationType) {
        this.evaluationType = evaluationType;
        switch (evaluationType) {
            case NEXT_STATES:
                this.evaluateStates = true;
                this.evaluateAfterstates = false;
                return;
            case AFTERSTATES:
                this.evaluateAfterstates = true;
                this.evaluateStates = false;
                return;
            case BOTH:
                this.evaluateAfterstates = true;
                this.evaluateStates = true;
        }
    }

    /**
     * @return the depthWeightingType
     */
    public DepthWeighting getDepthWeightingType() {
        return depthWeightingType;
    }

    /**
     * @param depthWeightingType the depthWeightingType to set
     */
    public void setDepthWeightingType(DepthWeighting depthWeightingType) {
        this.depthWeightingType = depthWeightingType;
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

    private long addNewStates(GameBoard afterState, LinkedList queue) {
        long nextElementsToDepthIncrease = 0;
        if (this.evaluateStates) {
            LinkedList newStates = createAllPossibleNewStates(afterState);
            queue.addAll(newStates);
            nextElementsToDepthIncrease += newStates.size();
//            for (GameBoard newState : newStates) {
//                queue.add(newState);
//                nextElementsToDepthIncrease++;
//            }
        }

        if (this.evaluateAfterstates) {
            queue.add(afterState);
            nextElementsToDepthIncrease++;
        }
        return nextElementsToDepthIncrease;
    }

    private long evaluateState(GameBoard state, Heuristic heuristic, int currentDirection, int currentDepth, int maxDepth) {
        // if depth is 1 override with linear
        if (maxDepth == 1 && this.depthWeightingType == DepthWeighting.LOGARITHMIC) {
            double scale = (maxDepth - currentDepth + 1) / maxDepth;
            return (long) (heuristic.getValueOfState(controller, state, currentDirection) * scale);
        }
        switch (this.depthWeightingType) {
            default:
            case NONE:
                return heuristic.getValueOfState(controller, state, currentDirection);
            case LINEAR:
                double scale = (maxDepth - currentDepth + 1) / maxDepth;
                return (long) (heuristic.getValueOfState(controller, state, currentDirection) * scale);
            case LOGARITHMIC:
                double x = (currentDepth - 1) / (maxDepth - 1);
                double y = (Math.pow(this.logarithmicDepthWeightPower, x));
                return (long) (heuristic.getValueOfState(controller, state, currentDirection) * y);
        }
    }

    private int[] getHeuristicVotes(long[][] heuristicSums, Direction[] directions, Heuristic[] heuristics) {
        int[] votes = new int[heuristics.length];
        for (int i = 0; i < heuristics.length; i++) {
            ArrayList<Integer> sameList = new ArrayList<>();
            long highestSum = Integer.MIN_VALUE;
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
                choice = sameList.get(getRandom().nextInt(sameList.size()));
                println("Randomly chose "
                        + directions[(choice)] + " for " + heuristics[i]);

            } else {
                choice = sameList.get(0);
            }
            votes[i] = choice;

        }

        return votes;
    }

    private int[] tallyVotes(long[][] heuristicSums, Direction[] directions, Heuristic[] heuristics) {
        int[] votes = new int[directions.length];
        for (int i = 0; i < heuristics.length; i++) {

            ArrayList<Integer> sameList = new ArrayList<>();
            long highestSum = Integer.MIN_VALUE;

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
                choice = sameList.get(getRandom().nextInt(sameList.size()));
                println("Randomly chose "
                        + directions[(choice)] + " for " + heuristics[i]);

            } else {
                choice = sameList.get(0);
            }
            votes[choice]++;
        }

        //System.out.println(executor);
        return votes;
    }

    // less empty positions = search more depths
    // this increase is linear (for now)
    public int getDepthScale(GameBoard currentBoard) {

        double emptyThreshold = (GameController.GRID_SIZE) * .75;
        double percentEmpty = (double) currentBoard.getEmptyPositions().size()
                / (emptyThreshold);
        percentEmpty = Math.max(0, Math.min(percentEmpty, 1));
        double depthScale = -.8 * percentEmpty + 1;
        return Math.max(1, (int) Math.ceil(this.maxDepth * depthScale));

        /*
        int emptySpaces = currentBoard.getEmptyPositions().size();
        double depthScale = ((double) GameController.GRID_SIZE - (double) emptySpaces)
                / (double) GameController.GRID_SIZE;
        return Math.max(1, (int) Math.ceil(this.maxDepth * depthScale));
         */
    }

    public int[] getVotesOnDirections(GameBoard currentBoard, Heuristic[] heuristics) {
        Direction[] directions = Direction.values();
        LinkedList<GameBoard>[] directionQueues = new LinkedList[directions.length];
        long[][] heuristicSums = new long[directions.length][heuristics.length];
        GameController gameController = this.controller;

        for (int i = 0; i < directionQueues.length; i++) {
            directionQueues[i] = new LinkedList();
        }

        int currentDepth = 1;
        int maxDepth = this.depthScaling ? getDepthScale(currentBoard) : this.maxDepth;
        long elementsToDepthIncrease = 0;
        long nextElementsToDepthIncrease = 0;

        println("Begin queue init");
        for (int directionNum = 0; directionNum < directions.length; directionNum++) {
            GameBoard afterState = controller.moveGrid(currentBoard, directions[directionNum]);
            if ((afterState.isMoved()) && this.evaluateAfterstates) {
                for (int heuristicNum = 0; heuristicNum < heuristics.length; heuristicNum++) {
                    final Heuristic heuristic = heuristics[heuristicNum];
                    // evaluate state of board
                    heuristicSums[directionNum][heuristicNum] += evaluateState(afterState, heuristic, directionNum, currentDepth, maxDepth);
                }
            }
            if ((afterState.isMoved())) {
                elementsToDepthIncrease += addNewStates(afterState, directionQueues[directionNum]);
            }
        }

        println("Begin queues while loop");
        while (!queuesAreEmpty(directionQueues)) {
            for (int directionQueueNum = 0; directionQueueNum < directionQueues.length; directionQueueNum++) {
                println("Begin queue " + directionQueueNum + " loop");
                LinkedList<GameBoard> queue = directionQueues[directionQueueNum];
                if (queue.isEmpty()) {
                    continue; // skip to next queue
                }
                GameBoard nextBoard = queue.poll();
                elementsToDepthIncrease--;
                for (int i = 0; i < directions.length; i++) {
                    Direction direction = directions[i];
                    GameBoard afterState = controller.moveGrid(nextBoard, direction);
                    if (currentDepth <= maxDepth && afterState.isMoved()) { //(afterState.isMoved() || this.ignoreMovement) && currentDepth <= maxDepth) {
                        nextElementsToDepthIncrease += addNewStates(afterState, queue);
                    }

                    if (this.evaluateAfterstates && afterState.isMoved()) { //&& (afterState.isMoved() || this.ignoreMovement)) {
                        for (int heuristicNum = 0; heuristicNum < heuristics.length; heuristicNum++) {
                            final Heuristic heuristic = heuristics[heuristicNum];
                            // evaluate state of board
                            heuristicSums[directionQueueNum][heuristicNum] += evaluateState(afterState, heuristic, i, currentDepth, maxDepth);
                        }
                    }

                    if (this.evaluateStates) {
                        for (int heuristicNum = 0; heuristicNum < heuristics.length; heuristicNum++) {
                            final Heuristic heuristic = heuristics[heuristicNum];
                            // evaluate state of board
                            heuristicSums[directionQueueNum][heuristicNum] += evaluateState(nextBoard, heuristic, i, currentDepth, maxDepth);
                        }
                    }
                }

                if (elementsToDepthIncrease <= 0) {
                    currentDepth++;
                    elementsToDepthIncrease = nextElementsToDepthIncrease;
                    nextElementsToDepthIncrease = 0;
                }
            }
        }

        return getHeuristicVotes(heuristicSums, directions, heuristics);
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

    public SingleThreadSearch(GameController controller) {
        this.controller = controller;
    }

    public SingleThreadSearch(GameController controller, int maxDepth) {
        this.controller = controller;
        this.maxDepth = maxDepth;
    }

    /**
     * @return the random
     */
    public Random getRandom() {
        return random;
    }

    /**
     * @param random the random to set
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * @return the logarithmicDepthWeightPower
     */
    public double getLogarithmicDepthWeightPower() {
        return logarithmicDepthWeightPower;
    }

    /**
     * @param logarithmicDepthWeightPower the logarithmicDepthWeightPower to set
     */
    public void setLogarithmicDepthWeightPower(double logarithmicDepthWeightPower) {
        this.logarithmicDepthWeightPower = logarithmicDepthWeightPower;
    }

    /**
     * @return the evaluateStates
     */
    public boolean isEvaluateStates() {
        return evaluateStates;
    }
}