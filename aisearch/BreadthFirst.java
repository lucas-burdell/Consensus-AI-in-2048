package aisearch;

import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import gamemodel.Direction;

/**
 *
 * @author lucas.burdell
 */
public class BreadthFirst implements SearchAlgorithm {

    private static boolean evaluateAfterstates = false;
    private static int maximumDepth = 4;

    /**
     * @return the evaluateAfterstates
     */
    public static boolean isEvaluateAfterstates() {
        return evaluateAfterstates;
    }

    /**
     * @param aEvaluateAfterstates the evaluateAfterstates to set
     */
    public static void setEvaluateAfterstates(boolean aEvaluateAfterstates) {
        evaluateAfterstates = aEvaluateAfterstates;
    }

    /**
     * @return the maximumDepth
     */
    public static int getMaximumDepth() {
        return maximumDepth;
    }

    /**
     * @param aMaximumDepth the maximumDepth to set
     */
    public static void setMaximumDepth(int aMaximumDepth) {
        maximumDepth = aMaximumDepth;
    }
    
    @Override
    public Direction searchForNextMove(GameController controller, GameBoard currentBoard) {
         
        //TODO: build tree for each of the four moves and then search from there.
        // once max depth is reached, total up value of all possible states
        // search should be A* (or at least a directed search) rather than breadth-first.
        return null;
       
        /*
        Deque<GameBoard> openQueue = new ArrayDeque<>();
        openQueue.add(currentBoard);
        ArrayList<GameBoard> closedList = new ArrayList<>();
        
        
        int currentDepth = 0;
        int elementsToDepthIncrease = 1;
        int nextElementsToDepthIncrease = 0;
        GameBoard highestScoringBoard = currentBoard;
        
        
        
        while (!openQueue.isEmpty()) {
            
            // dequeue
            GameBoard board = openQueue.remove();
            
            
            ArrayList<GameBoard> allNewStates = new ArrayList<>();
            for (Direction direction : Direction.values()) {
                GameBoard afterState = controller.moveGrid(board, direction);
                if (afterState.isMoved()) {
                    GameBoard[] newStates = controller.createAllPossibleNewStates(afterState);
                    for (GameBoard newState : newStates) {
                        allNewStates.add(newState);
                    }
                }
            }
            
            openQueue.addAll(allNewStates);
            nextElementsToDepthIncrease += allNewStates.size();
            elementsToDepthIncrease -= 1;
            if (elementsToDepthIncrease <= 0) {
                currentDepth = currentDepth + 1;
                if (currentDepth > maximumDepth) {
                    // do something
                }
                elementsToDepthIncrease = nextElementsToDepthIncrease;
                nextElementsToDepthIncrease = 0;
            }
            
        }
        return null;
        //return highestScoringBoard;
        */
    }
    
}
