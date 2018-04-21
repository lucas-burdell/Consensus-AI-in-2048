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
public class MajorityTieVoting extends AIDecider {
    
    private final Heuristic[] heuristics;
    private boolean adjustWeights = false;
    
    private final int primaryAIndex;
    private final int primaryBIndex;
    
    public MajorityTieVoting(Heuristic[] heuristics, int primaryA, int primaryB) {
        this.heuristics = heuristics;
        this.primaryAIndex = primaryA;
        this.primaryBIndex = primaryB;
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
    
    private int agreementCount = 0;
    private int decisionCount = 0;

    @Override
    public Direction evaluateVotes(int[] heuristicVotes) {
        synchronized (this){
            setDecisionCount(getDecisionCount() + 1);
        }
        double[] majorityVotes = new double[Direction.values().length];
        int primaryA = -1;
        int primaryB = -1;
        
        for (int i = 0; i < heuristicVotes.length; i++) {
            int heuristicVote = heuristicVotes[i];
            if (i == primaryAIndex) {
                primaryA = heuristicVote;
            } else if (i == primaryBIndex) {
                primaryB = heuristicVote;
            } else {
                majorityVotes[heuristicVote] += heuristics[i].getWeight();
            }
        }
        
        
        if (primaryA == primaryB) {
            return Direction.values()[primaryA];
        }

        // use majority as tie-breaker
        
        // always choose highest vote
        // randomly choose between equal choices
        double highest = 0;
        LinkedList<Integer> sameList = new LinkedList<>();
        for (int i = 0; i < majorityVotes.length; i++) {
            double vote = majorityVotes[i];
            if (vote > highest) {
                highest = vote;
                sameList.clear();
                sameList.add(i);
            } else if (vote == highest) {
                sameList.add(i);
            }
        }
        int majorityChoice = sameList.get(getRandom().nextInt(sameList.size()));
        if (majorityChoice == primaryA) {
            return Direction.values()[primaryB];
        } else if (majorityChoice == primaryB) {
            return Direction.values()[primaryA];
        } else {
            return Direction.values()[majorityChoice];
        }
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

    /**
     * @return the decisionCount
     */
    public int getDecisionCount() {
        return decisionCount;
    }

    /**
     * @param decisionCount the decisionCount to set
     */
    public void setDecisionCount(int decisionCount) {
        this.decisionCount = decisionCount;
    }

}
