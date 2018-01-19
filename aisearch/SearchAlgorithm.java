package aisearch;

import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public interface SearchAlgorithm {
    public Direction searchForNextMove(GameController controller, GameBoard currentBoard);
}
