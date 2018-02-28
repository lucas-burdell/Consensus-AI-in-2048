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
package aidecision;

import aiheuristics.Heuristic;
import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.io.File;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public abstract class AIDecider {

    /**
     * @return the learn
     */
    public boolean isLearning() {
        return learning;
    }

    /**
     * @param learn the learn to set
     */
    public void setLearning(boolean learn) {
        this.learning = learn;
    }
    private boolean learning = false;

    // very likely to refactor this later, because I don't know what information 
    // I'll need to facilitate learning 
    public abstract Direction evaluateVotes(int[] heuristicVotes);
}
