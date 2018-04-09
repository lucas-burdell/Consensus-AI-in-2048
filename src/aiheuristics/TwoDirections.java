package aiheuristics;

import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public class TwoDirections extends Heuristic {

    public TwoDirections(double weight) {
        super(weight);
    }
    
    
    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        long multiplier = (currentDirection - state.getPreviousMove().ordinal()) % (Direction.values().length);
        if (multiplier < 0) multiplier += Direction.values().length;
        if (Math.abs(multiplier) <= 1L) {
            multiplier = 5;
        } else {
            multiplier = 1;
        }
        return state.getScore() * multiplier;

    }

    
}
