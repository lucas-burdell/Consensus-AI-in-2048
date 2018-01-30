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
package cliai;

import aidecision.AIDecider;
import aidecision.RandomVoting;
import aiheuristics.Heuristic;
import aiheuristics.HighestMerges;
import aiheuristics.MostMerges;
import aisearch.Searcher;
import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.Scanner;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class Runner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameController controller = new GameController();
        GameBoard board = controller.createStartingGameboard();
        System.out.print("enter max depth: ");
        int maxDepth = scanner.nextInt();
        System.out.println();
        Searcher searcher = new Searcher(controller, maxDepth);
        AIDecider decider = new RandomVoting();
        Heuristic[] heuristics = new Heuristic[]{
            new HighestMerges(),
            new MostMerges()
        };

        while (!controller.isGameOver(board)) {
            System.out.println(board);
            System.out.println();
            int[] votes = searcher.getVotesOnDirections(board, heuristics);
            Direction decision = decider.evaluateVotes(votes);
            board = controller.doGameMove(board, decision);
            //System.out.println("Current score: " + board.getScore());
            //int move = scanner.nextInt();
            //Direction[] directions = Direction.values();
            //if (move > -1 && move < directions.length) {
            //board = controller.doGameMove(board, directions[move]);
            //board = controller.moveGrid(board, directions[move]).getKey();
            //}
        }
        System.out.println("Game over!");

    }

}