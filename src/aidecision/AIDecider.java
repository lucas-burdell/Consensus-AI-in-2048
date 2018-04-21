package aidecision;

import gamemodel.Direction;

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
