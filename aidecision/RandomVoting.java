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

import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class RandomVoting extends AIDecider {

    /**
     * @return the debugMessagesEnabled
     */
    public boolean isDebugMessagesEnabled() {
        return debugMessagesEnabled;
    }

    private void println(Object message) {
        if (isDebugMessagesEnabled()) {
            System.out.println(message);
        }
    }

    /**
     * @param debugMessagesEnabled the debugMessagesEnabled to set
     */
    public void setDebugMessagesEnabled(boolean debugMessagesEnabled) {
        this.debugMessagesEnabled = debugMessagesEnabled;
    }

    private Random random = new Random();
    private boolean debugMessagesEnabled = false;

    @Override
    public Direction evaluateVotes(int[] votes) {
        // always choose highest vote
        // randomly choose between equal choices
        int highest = 0;
        ArrayList<Integer> sameList = new ArrayList<>();
        for (int i = 0; i < votes.length; i++) {
            int vote = votes[i];
            if (vote > highest) {
                highest = vote;
                sameList.clear();
                sameList.add(i);
            } else if (vote == highest) {
                sameList.add(i);
            }
        }
        int choice = sameList.get(random.nextInt(sameList.size()));
        Direction[] directions = Direction.values();
        println(directions[choice] + " chosen!");
        return directions[choice];
    }

}
