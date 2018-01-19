package heuristics;

import gamemodel.GameBoard;

/**
 *
 * @author lucas.burdell
 */
public class HighestMerge implements Heuristic {

    @Override
    public int evaluate(GameBoard board, GameBoard previous) {
        return board.getScore() - previous.getScore();
    }
    
}
