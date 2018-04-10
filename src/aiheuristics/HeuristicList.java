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

/**
 * literally just a class with a single static array of the heuristics
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class HeuristicList {

    /**
     * @return the heuristics
     */
    public static Heuristic[] getHeuristics() {
        return heuristics;
    }

    private static final Heuristic[] heuristics = new Heuristic[]{
        new HighestMerges(1),
        new MostMerges(1),
        new EmptySpaces(1), 
        //new BiggestNumberNeighbors(1),
        new MonotonicityInRows(1),
        new Smoothness(1),
        new TwoDirections(1),
        new Corners(1), 
        //new BiggestNumberCornerDistance(1),
    };
    /*
            new HighestMerges(.5),
        new MostMerges(.25),
        new EmptySpaces(.25), 
        new BiggestNumberNeighbors(.5),
        new MonotonicityInRows(.5),
        new Smoothness(1),
        new TwoDirections(.75),
        new Corners(1), 
        new BiggestNumberCornerDistance(.5),
    */

}
