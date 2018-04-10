package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author lucas.burdell
 */
public class Corners extends Heuristic {

    public Corners(double weight) {
        super(weight);
    }

    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        int[][] grid = state.getGameGrid();
        
        long topLeft = 0;
        long topRight = 0;
        long bottomLeft = 0;
        long bottomRight = 0;
        
        topLeft += grid[0][0] + grid[0][1] + grid[1][0];
        topRight += grid[0][3] + grid[0][2] + grid[1][3];
        bottomLeft += grid[3][0] + grid[3][1] + grid[2][0];
        bottomRight += grid[3][3] + grid[3][2] + grid[2][3];
        
        long topHighest = topLeft > topRight ? topLeft : topRight;
        long bottomHighest = bottomLeft > bottomRight ? bottomLeft : bottomRight;
        return topHighest < bottomHighest ? bottomHighest : topHighest;
    }

    
}
