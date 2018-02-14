package aiheuristics;

import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public class TwoDirections implements Heuristic {
    
    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        long multiplier = (currentDirection - state.getPreviousMove().ordinal());
        if (Math.abs(multiplier) <= 1L) {
            multiplier = 5;
        } else {
            multiplier = 1;
        }
        return state.getScore() * multiplier;

    }
    
}
