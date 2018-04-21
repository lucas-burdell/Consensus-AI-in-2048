package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public class Stacks extends Heuristic {

    public Stacks(double weight) {
        super(weight);
    }

    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        long totalScore = 0;
        int[][] grid = state.getGameGrid();
        for (int i = 0; i < grid.length; i++) {
            int[] is = grid[i];
            for (int j = 0; j < is.length; j++) {
                int k = is[j];
                if (i < grid.length - 1) {
                    if (k == grid[i + 1][j]) {
                        if (k == 0) {
                            totalScore += 1000;
                        } else {
                            totalScore += k << 2;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < grid.length; i++) {
            totalScore += grid[i][0] << 2;
            totalScore += grid[i][grid.length - 1] << 2;
            totalScore += grid[0][i] << 2;
            totalScore += grid[grid.length - 1][i] << 2;
        }
        return totalScore;
    }
    
}
