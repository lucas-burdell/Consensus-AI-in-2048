package heuristics;

import gamemodel.GameBoard;
/**
 *
 * @author lucas.burdell
 */
public interface Heuristic {
    public int evaluate(GameBoard board, GameBoard previous);
}
