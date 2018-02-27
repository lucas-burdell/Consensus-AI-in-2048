package aidecision;

import aiheuristics.Heuristic;
import gamemodel.Direction;
import java.util.Random;

/** Pull random decision from a "bag". Like putting a slip of paper in a bag for each vote,
 * and then pulling one out at random.
 *
 * @author lucas.burdell
 */
public class RandomBagVoting extends AIDecider {
    
    private Random random = new Random();
    
    @Override
    public Direction evaluateVotes(int[] heuristicVotes) {
        // determine bag size
        /*
        int bagSize = 0;
        for (int i = 0; i < votes.length; i++) {
            int vote = votes[i];
            bagSize += vote;
        }
        int[] bag = new int[bagSize];
        int index = 0;
        for (int i = 0; i < votes.length; i++) {
            int vote = votes[i];
            for (int j = 0; j < vote; j++) {
                bag[index] = i;
                index++;
            }
        }
        int choiceIndex = random.nextInt(bagSize);
        int choice = bag[choiceIndex];
        Direction[] directions = Direction.values();
        return directions[choice];
        */
        throw new RuntimeException("RandomBagVoting not implemented yet.");
    }
    
}
