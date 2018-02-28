package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public class MonotonicityInRows implements Heuristic {

    
    
    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        byte[][] grid = state.getGameGrid();
        long leftOutput = 0;
        long rightOutput = 0;
        for (byte[] row : grid) {
            for (int x = 1; x < row.length; x++) {
                // row is treated as if it is in descending order
                if (row[x - 1] > row[x]) {
                    // Add the difference to the output.
                    // This does not reward same-value tiles.
                    leftOutput += row[x - 1] - row[x];
                } else { // row might be in ascending order (or anything else)
                    rightOutput += row[x] - row[x - 1];
                }
                
            }
        }
        // reward the highest score
        return Math.max(leftOutput, leftOutput);
    }
    
}
