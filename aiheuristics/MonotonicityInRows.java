package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public class MonotonicityInRows implements Heuristic {

    @Override
    public long getValueOfState(GameController controller, GameBoard state) {
        int[][] grid = state.getGameGrid();
        long leftOutput = 0;
        long rightOutput = 0;
        for (int x = 0; x < grid.length; x++) {
            int[] row = grid[x];
            for (int y = 1; y < row.length; y++) {
                int value = row[y];
                
                // if row is treated as it is in descending order
                if (row[y - 1] > row[y]) {
                    // Add the difference to the output.
                    // This does not reward same-value tiles.
                    leftOutput += row[y - 1] - row[y];
                } else { // row might be in ascending order (or anything else)
                    rightOutput += row[y] - row[y - 1];
                }
                
            }
        }
        // return the highest score
        return Math.max(leftOutput, leftOutput);
    }
    
}
