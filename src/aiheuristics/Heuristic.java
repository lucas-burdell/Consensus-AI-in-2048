package aiheuristics;


import gamemodel.GameBoard;
import gamemodel.GameController;


/** Heuristics should be designed to be evaluations of states
 * providing a higher score for a more favorable state.
 *
 * @author lucas.burdell
 */
public interface Heuristic {
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection);
    @Override
    public String toString();
}
