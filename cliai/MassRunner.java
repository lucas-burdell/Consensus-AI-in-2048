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
import aiheuristics.HeuristicList;
import aisearch.Searcher;
import gamemodel.Direction;
import gamemodel.GameBoard;
import gamemodel.GameController;
import java.util.Scanner;

/**
 * Run multiple games and compare results
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class MassRunner {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter number of games to play: ");
        int gamesToPlay = input.nextInt();
        System.out.println();
        System.out.print("Enter max depth of search: ");
        int maxDepth = input.nextInt();
        int[] scoreResults = new int[gamesToPlay];
        GameController controller = new GameController();
        Searcher searcher = new Searcher(controller);
        AIDecider decider = new RandomVoting();
        GameBoard currentBoard = controller.createStartingGameboard();
        for (int i = 0; i < gamesToPlay; i++) {
            while (!controller.isGameOver(currentBoard)) {
                int[] votes = searcher.getVotesOnDirections(currentBoard, HeuristicList.getHeuristics());
                Direction decision = decider.evaluateVotes(votes);
                currentBoard = controller.doGameMove(currentBoard, decision);
            }
            scoreResults[i] = currentBoard.getScore();
            currentBoard = controller.createStartingGameboard();
            System.out.println("game " + i + " complete");
        }
        System.out.println("Mean: " + getMean(scoreResults));

    }

    private static long getMean(int[] scoreResults) {
        long output = 0;
        for (int i = 0; i < scoreResults.length; i++) {
            int scoreResult = scoreResults[i];
            output += scoreResult;
        }
        return output / (long) scoreResults.length;
    }
}
