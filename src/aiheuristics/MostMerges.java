package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class MostMerges extends Heuristic {

    public MostMerges(double weight) {
        super(weight);
    }
    
    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {

        /*
        Direction[] directions = Direction.values();
        long output = 0;
        int numberOfEmptyTiles = state.getEmptyPositions().size();
        for (int i = 0; i < directions.length; i++) {
            GameBoard result = controller.moveGrid(state, directions[i]);
            output += result.getEmptyPositions().size() - numberOfEmptyTiles;
        }
        return output;
        */
        return state.getNumberOfMerges();

    }

}
