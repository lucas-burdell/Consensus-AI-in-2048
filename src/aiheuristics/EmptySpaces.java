/*
 * Copyright (C) 2018 Lucas Burdell <lucasburdell@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class EmptySpaces implements Heuristic {

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
