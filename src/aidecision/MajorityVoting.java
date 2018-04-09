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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class MajorityVoting extends AIDecider {

    private long totalMoves = 0;
    private final long[] majorityAgrees;
    private final Heuristic[] heuristics;
    private boolean adjustWeights = false;
    
    public MajorityVoting(Heuristic[] heuristics) {
        majorityAgrees = new long[heuristics.length];
        this.heuristics = heuristics;
    }

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
    public synchronized Direction evaluateVotes(int[] heuristicVotes) {
        double[] votes = new double[Direction.values().length];
        for (int i = 0; i < heuristicVotes.length; i++) {
            int heuristicVote = heuristicVotes[i];
            println(heuristics[i] + " chose " + heuristicVote);
            votes[heuristicVote] += heuristics[i].getWeight();
        }

        // always choose highest vote
        // randomly choose between equal choices
        double highest = 0;
        LinkedList<Integer> sameList = new LinkedList<>();
        for (int i = 0; i < votes.length; i++) {
            double vote = votes[i];
            if (vote > highest) {
                highest = vote;
                sameList.clear();
                sameList.add(i);
            } else if (vote == highest) {
                sameList.add(i);
            }
        }
        int choice = sameList.get(getRandom().nextInt(sameList.size()));
        Direction[] directions = Direction.values();
        Direction decision = directions[choice];

        println("majority chose " + choice);
        
        if (this.isLearning()) {
            calculateWeights(choice, heuristicVotes);
        }

        return decision;
    }

    private void calculateWeights(int majorityDecision, int[] heuristicVotes) {
        this.totalMoves++;
        for (int i = 0; i < heuristics.length; i++) {
            Heuristic heuristic = heuristics[i];
            int decision = heuristicVotes[i];
            if (decision == majorityDecision) {
                majorityAgrees[i]++;
            }
            heuristic.setWeight(majorityAgrees[i] / (double) (this.totalMoves));
            if (this.debugMessagesEnabled) {
                println(getWeightsReport());
            }
        }
    }

    public String getWeightsReport() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < heuristics.length; i++) {
            Heuristic heuristic = heuristics[i];
            output.append(heuristic.toString()).append(" now has weight: ").append(heuristic.getWeight()).append("\n");
        }
        return output.toString();
    }

    /**
     * @return the random
     */
    public Random getRandom() {
        return random;
    }

    /**
     * @param random the random to set
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * @return the adjustWeights
     */
    public boolean isAdjustWeights() {
        return adjustWeights;
    }

    /**
     * @param adjustWeights the adjustWeights to set
     */
    public void setAdjustWeights(boolean adjustWeights) {
        this.adjustWeights = adjustWeights;
    }

}
