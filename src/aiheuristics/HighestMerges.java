package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class HighestMerges extends Heuristic {

    public HighestMerges(double weight) {
        super(weight);
    }

    
    @Override
    public long getValueOfState(GameController controller, GameBoard state, int currentDirection) {
        /*
        Direction[] directions = Direction.values();
        long output = 0;
        for (int i = 0; i < directions.length; i++) {
            GameBoard result = controller.moveGrid(state, directions[i]);
            output += result.getScore() - state.getScore();
        }
        return output;
        */
        /*
        int[][] grid = state.getGameGrid();
        long output = 0;
        for (int x = 0; x < GRID_SIZE - 1; x++) {
            for (int y = 0; y < GRID_SIZE - 1; y++) {
                if (grid[x][y] == grid[x + 1][y]) {
                    output += grid[x][y];
                } else if (grid[x][y] == grid[x][y + 1]) {
                    output += grid[x][y];
                }
            }
        }

        return output;*/
        return state.getScore();
    }


}
