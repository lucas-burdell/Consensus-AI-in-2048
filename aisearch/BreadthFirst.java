package aisearch;

import aiheuristics.Heuristic;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import gamemodel.Direction;
import searchtree.Tree;
import searchtree.Tree.Node;

/**
 *
 * @author lucas.burdell
 */
public class BreadthFirst {

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

    public Direction searchForNextMove(GameController controller, GameBoard currentBoard, Heuristic heuristic) {

        //TODO: build tree for each of the four moves and then search from there.
        // once max depth is reached, total up value of all possible states according to heuristic
        //return null;
        Deque<Node<GameBoard>> openQueue = new ArrayDeque<>();
        Direction[] directions = Direction.values();
        Tree<GameBoard>[] treeList = new Tree[directions.length];
        int currentDepth = 1;
        int elementsToDepthIncrease = directions.length;
        int nextElementsToDepthIncrease = 0;
        for (int i = 0; i < treeList.length; i++) {
            GameBoard result = controller.moveGrid(currentBoard, directions[i]);
            treeList[i] = new Tree<>(result);
            openQueue.add(treeList[i].getRoot());
        }



        
        while (!openQueue.isEmpty()) {
            // dequeue (dequeue the deque. Queue? Que?)
            Node<GameBoard> node = openQueue.remove();
            ArrayList<Node<GameBoard>> allNewStates = new ArrayList<>();
            for (Direction direction : directions) {
                GameBoard afterState = controller.moveGrid(node.getValue(), direction);
                if (afterState.isMoved()) {
                    GameBoard[] newStates = controller.createAllPossibleNewStates(afterState);
                    for (GameBoard newState : newStates) {
                        Node<GameBoard> newNode = new Node<>(newState, node);
                        allNewStates.add(newNode);
                    }
                }
            }
            openQueue.addAll(allNewStates);
            nextElementsToDepthIncrease += allNewStates.size();
            elementsToDepthIncrease -= 1;
            
            if (elementsToDepthIncrease <= 0) {
                currentDepth = currentDepth + 1;
                elementsToDepthIncrease = nextElementsToDepthIncrease;
                nextElementsToDepthIncrease = 0;
                
            }
        }
        
            
            /*
            
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
