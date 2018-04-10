package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public class BiggestNumberCornerDistance extends Heuristic {

    public BiggestNumberCornerDistance(double weight) {
        super(weight);
    }
    
    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        int[][] grid = state.getGameGrid();
        int[] highestPos = getHighestValuePosition(grid);
        int[] distances = new int[]{
            chebyshevDistance(highestPos[1], highestPos[0], 0, 0), // top left
            chebyshevDistance(highestPos[1], highestPos[0], 3, 0), // top right
            chebyshevDistance(highestPos[1], highestPos[0], 0, 3), // bottom left
            chebyshevDistance(highestPos[1], highestPos[0], 3, 3), // bottom right
        };
        int min = distances[0];
        for (int i = 1; i < distances.length; i++) {
            int distance = distances[i];
            if (distance < min) {
                min = distance;
            }
        }
        return (grid.length * grid.length) - min;
    }
    
    private int chebyshevDistance(int x, int y, int x2, int y2) {
        return Math.max(Math.abs(x2 - x), Math.abs(y2 - y));
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
