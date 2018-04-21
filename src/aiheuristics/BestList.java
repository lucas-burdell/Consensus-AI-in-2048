package aiheuristics;

/**
 * literally just a class with a single static array of the heuristics
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class BestList {

    /**
     * @return the heuristics
     */
    public static Heuristic[] getHeuristics() {
        return heuristics;
    }

    private static final Heuristic[] heuristics = new Heuristic[]{
        new Stacks(1)
        //new HighestMerges(1),
        //new MostMerges(1),
        //new EmptySpaces(1), 
        //new BiggestNumberNeighbors(1),
        //new MonotonicityInRows(1),
        //new Smoothness(1),
        //new TwoDirections(1),
        //new Corners(1), 
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
