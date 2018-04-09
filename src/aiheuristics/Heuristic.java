package aiheuristics;


import gamemodel.GameBoard;
import gamemodel.GameController;


/** Heuristics should be designed to be evaluations of states
 * providing a higher score for a more favorable state.
 *
 * @author lucas.burdell
 */
public abstract class Heuristic {
    public abstract long getValueOfState(GameController controller, GameBoard state, int currentDirection);
    private double weight;
    public Heuristic(double weight){
        this.weight = weight;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
