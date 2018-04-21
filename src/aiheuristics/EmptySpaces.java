package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class EmptySpaces extends Heuristic {

    public EmptySpaces(double weight) {
        super(weight);
    }

    
    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        int[][] grid = state.getGameGrid();
        int[] highestPos = getHighestValuePosition(grid);
        int highestValue = grid[highestPos[0]][highestPos[1]];
        long output = 0;
        for (int[] row : grid) {
            for (int value : row) {
                if (value == 0) {
                    output += (highestValue / 2);
                }
            }
        }

        return output;
    }

// returned as y, x
    private int[] getHighestValuePosition(int[][] grid) {
        int[] highest = new int[2];
        int highestValue = 0;
        for (int y = 0; y < grid.length; y++) {
            int[] row = grid[y];
            for (int x = 0; x < row.length; x++) {
                int value = row[x];
                if (value > highestValue) {
                    highest[0] = y;
                    highest[1] = x;
                    highestValue = value;
                }
            }
        }
        return highest;
    }

}
