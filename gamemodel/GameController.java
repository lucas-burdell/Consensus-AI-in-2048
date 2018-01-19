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
package gamemodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javafx.util.Pair;
/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class GameController {

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    public static final Random RANDOM = new Random();
    public static final int NUMBER_OF_STARTING_TILES = 2;
    public static final double CHANCE_OF_A_FOUR = .9;
    public static final int GRID_SIZE = 4;

    private int score = 0;

    private final int[] selectRandomEmptyPosition(GameBoard board) {
        ArrayList<Integer[]> positions = board.getEmptyPositions();
        Integer[] output = positions.get(RANDOM.nextInt(positions.size()));
        return new int[]{output[0], output[1]};
    }

    public final GameBoard[] createAllPossibleNewStates(GameBoard board) {
        ArrayList<Integer[]> positions = board.getEmptyPositions();
        GameBoard[] states = new GameBoard[positions.size() * 2];
        for (int i = 0; i < positions.size(); i++) {
            Integer[] position = positions.get(i);
            GameBoard board2 = new GameBoard(board);
            board2.getGameGrid()[position[0]][position[1]].setValue(1);
            GameBoard board4 = new GameBoard(board);
            board4.getGameGrid()[position[0]][position[1]].setValue(2);
            states[i * 2] = board2;
            states[(i * 2) + 1] = board4;
        }
        return states;
    }

    public final GameBoard placeRandomTile(GameBoard board) {
        int[] position = selectRandomEmptyPosition(board);
        return placeRandomTile(board, position);
    }

    public final GameBoard placeRandomTile(GameBoard board, int[] position) {
        GameBoard newBoard = new GameBoard(board);
        GameNode node = newBoard.getGameGrid()[position[0]][position[1]];
        if (RANDOM.nextDouble() < CHANCE_OF_A_FOUR) {
            // is a 2
            node.setValue(1);
        } else {
            // is a 4
            node.setValue(2);
        }
        return newBoard;
    }

    private static class TileMove {

        /**
         * @return the position
         */
        public int[] getPosition() {
            return position;
        }

        /**
         * @return the next
         */
        public GameNode getNext() {
            return next;
        }
        private int[] position;
        private GameNode next;

        public TileMove(int[] position, GameNode next) {
            this.position = position;
            this.next = next;
        }
    }

    private final TileMove getFarthestTile(GameBoard board, int[] position, Direction direction) {
        GameNode[][] grid = board.getGameGrid();
        int posX = position[0];
        int posY = position[1];
        switch (direction) {
            case UP:
                //System.out.println("getting farthest up from " + posX + ","+ posY);
                for (int y = posY - 1; y > -1; y--) {
                    if (grid[posX][y].getValue() != 0) {

                        return new TileMove(new int[]{posX, y + 1}, grid[posX][y]);
                    }
                }
                return new TileMove(new int[]{posX, 0}, grid[posX][0]);
            //return new int[]{posX, 0};
            case DOWN:
                //System.out.println("getting farthest down from " + posX + ","+ posY);
                for (int y = posY + 1; y < GRID_SIZE; y++) {
                    if (grid[posX][y].getValue() != 0) {
                        //return new int[]{posX, i};
                        return new TileMove(new int[]{posX, y - 1}, grid[posX][y]);
                    }
                }
                return new TileMove(new int[]{posX, GRID_SIZE - 1}, grid[posX][GRID_SIZE - 1]);
            case LEFT:
                //System.out.println("getting farthest left from " + posX + ","+ posY);
                for (int x = posX - 1; x > -1; x--) {
                    if (grid[x][posY].getValue() != 0) {
                        return new TileMove(new int[]{x + 1, posY}, grid[x][posY]);
                    }
                }
                //return new int[]{0, posY};
                return new TileMove(new int[]{0, posY}, grid[0][posY]);
            case RIGHT:
                //System.out.println("getting farthest right from " + posX + ","+ posY);
                for (int x = posX + 1; x < GRID_SIZE; x++) {
                    if (grid[x][posY].getValue() != 0) {
                        //return new int[]{i, posY};
                        return new TileMove(new int[]{x - 1, posY}, grid[x][posY]);
                    }
                }
                //return new int[]{GRID_SIZE, posY};
                return new TileMove(new int[]{GRID_SIZE - 1, posY}, grid[GRID_SIZE - 1][posY]);
            default:
                throw new RuntimeException("Unexpected direction: " + direction);
        }
    }

    // only board-mutating method
    private final boolean doMerge(GameBoard board, int[] position, Direction direction) {
        GameNode node = board.getGameGrid()[position[0]][position[1]];
        //System.out.println("Evaluating " + position[0] + "," + position[1] + " for merge");
        if (node.getValue() != 0) {
            //System.out.println("moving " + position[0] + "," + position[1]);
            TileMove farthestPosition = getFarthestTile(board, position, direction);
            //System.out.println("furthest point: " + farthestPosition.getPosition()[0] + "," + farthestPosition.getPosition()[1]);
            GameNode farthestNode = farthestPosition.getNext();
            if (farthestNode != node && farthestNode.getValue() != 0
                    && farthestNode.getValue() == node.getValue() && !farthestNode.isMerged()) {
                farthestNode.setValue(node.getValue() + 1);
                //System.out.println("Set furthest to " + farthestNode.getValue());
                farthestNode.setMerged(true);
                node.setValue(0);
                //System.out.println("Set node to " + node.getValue());
                score += farthestNode.getValue();
                return true;
            } else {
                int[] newPos = farthestPosition.getPosition();
                if (newPos[0] == position[0] && newPos[1] == position[1]) {
                    return false;
                } else {
                    board.getGameGrid()[newPos[0]][newPos[1]].setValue(node.getValue());
                    node.setValue(0);
                    return true;
                }
            }
        }
        //System.out.println("skipped because 0: " + position[0] + "," + position[1]);
        return false;
    }

    public final GameBoard doGameMove(GameBoard board, Direction direction) {
        Pair<GameBoard, Boolean> move = moveGrid(board, direction);
        GameBoard newBoard = move.getKey();
        if (move.getValue()) {
            newBoard = placeRandomTile(newBoard);
        }
        return newBoard;
    }

    public final Pair<GameBoard, Boolean> moveGrid(GameBoard board, Direction direction) {
        // if positive vector move backwards
        GameBoard newBoard = new GameBoard(board);
        boolean moved = false;
        if (direction == Direction.RIGHT) {
            for (int x = GRID_SIZE - 1; x > -1; x--) {
                for (int y = 0; y < GRID_SIZE; y++) {
                    moved = doMerge(newBoard, new int[]{x, y}, direction) || moved;
                }
            }
        } else {
            if (direction == Direction.DOWN) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    for (int y = GRID_SIZE - 1; y > -1; y--) {
                        moved = doMerge(newBoard, new int[]{x, y}, direction) || moved;
                    }
                }
            } else {
                for (int x = 0; x < GRID_SIZE; x++) {
                    //System.out.println("Moving row " + x + " for " + direction);
                    for (int y = 0; y < GRID_SIZE; y++) {
                        moved = doMerge(newBoard, new int[]{x, y}, direction) || moved;
                        //System.out.println("Moving column " + y + " for " + direction);
                    }
                }
            }
        }
        return new Pair<>(newBoard, moved);
    }

    public GameBoard createStartingGameboard() {
        GameBoard board = new GameBoard(GRID_SIZE);
        for (int i = 0; i < NUMBER_OF_STARTING_TILES; i++) {
            board = placeRandomTile(board);
        }
        return board;
    }
    
    public boolean isMatchesAvailable(GameBoard board) {
        GameNode[][] grid = board.getGameGrid();
        for (int x = 0; x < GRID_SIZE - 1; x++) {
            for (int y = 0; y < GRID_SIZE - 1; y++) {
                if (grid[x][y].getValue() == grid[x + 1][y].getValue()) {
                    return true;
                } else if (grid[x][y].getValue() == grid[x][y + 1].getValue()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isEmptySpace(GameBoard board) {
        GameNode[][] grid = board.getGameGrid();
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (grid[x][y].getValue() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver(GameBoard board) {
        return !(isEmptySpace(board) || isMatchesAvailable(board));
    }
}
