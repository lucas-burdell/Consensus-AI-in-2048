package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class BiggestNumberNeighbors extends Heuristic {

    public BiggestNumberNeighbors(double weight) {
        super(weight);
    }

    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        // find biggest 4 numbers
        int[][] grid = state.getGameGrid();
        // sort grid into 1d array
        int[] biggestNumbers = new int[grid.length * grid.length];
        int currentIndex = 0;
        long output = 0;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                int value = grid[x][y];
                biggestNumbers[currentIndex] = value;
                currentIndex++;
            }
        }
        Arrays.sort(biggestNumbers);
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                int value = grid[x][y];
                if (arrayContains(biggestNumbers, value)){
                    output += scoreNeighbors(grid, biggestNumbers, new int[]{x, y});
                }
            }
        }
        
        return output;
    }
    
    private boolean arrayContains(int[] array, int value) {
        int index = Arrays.binarySearch(array, value);
        return (array[index] == value);
    }
    
    private int scoreNeighbors(int[][] grid, int[] numbers, int[] position) {
        int value = grid[position[0]][position[1]];
        int right = position[0] + 1;
        int down = position[1] + 1;
        int output = 0;
        if (right < grid.length) {
            if (Math.abs(value - grid[right][position[1]]) <= 1) {
                output += Math.abs(value - grid[right][position[1]]);
            }
        }
        if (down < grid.length) {
            if (Math.abs(value - grid[position[0]][down]) <= 1) {
                output += Math.abs(value - grid[position[0]][down]);
            }
        }
        
        return output;
    }

    
}
