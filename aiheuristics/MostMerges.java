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
import static gamemodel.GameController.GRID_SIZE;
import gamemodel.GameNode;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class MostMerges implements Heuristic {

    @Override
    public long getValueOfState(GameController controller, GameBoard state) {
        GameNode[][] grid = state.getGameGrid();
        long output = 0;
        for (int x = 0; x < GRID_SIZE - 1; x++) {
            for (int y = 0; y < GRID_SIZE - 1; y++) {
                if (grid[x][y].getValue() == grid[x + 1][y].getValue()) {
                    output++;
                } else if (grid[x][y].getValue() == grid[x][y + 1].getValue()) {
                    output++;
                }
            }
        }
        return output;
    }
    
}
