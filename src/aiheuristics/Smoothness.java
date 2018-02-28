package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public class Smoothness implements Heuristic {

    
    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        byte[][] grid = state.getGameGrid();
        long output = 0;
        for (int y = 0; y < grid.length; y++) {
            byte[] row = grid[y];
            for (int x = 0; x < row.length; x++) {
                int value = row[x];
                if (x + 1 < row.length) {
                    int rightValue = row[x + 1];
                    if (rightValue == value) {
                        output += value;
                    }
                }
                if (y + 1 < grid.length) {
                    int downValue = grid[y + 1][x];
                    if (downValue == value) {
                        output += value;
                    }
                }
            }
        }
        return output;
    }
    
}
