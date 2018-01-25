package aiheuristics;


import gamemodel.GameBoard;
import gamemodel.GameController;


/**
 *
 * @author lucas.burdell
 */
public interface Heuristic {
    public int getValueOfState(GameController controller, GameBoard state);
}
