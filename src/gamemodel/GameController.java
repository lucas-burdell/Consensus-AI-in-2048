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

    private Random random = new Random();
    private boolean debugMessagesEnabled = false;

    public static final int NUMBER_OF_STARTING_TILES = 2;
    public static final double CHANCE_OF_A_FOUR = .9;
    public static final int ROW_SIZE = 4;
    public static final int GRID_SIZE = ROW_SIZE * ROW_SIZE;



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

    private final int[] selectRandomEmptyPosition(GameBoard board) {
        ArrayList<Integer[]> positions = board.getEmptyPositions();
        Integer[] output = positions.get(getRandom().nextInt(positions.size()));
        return new int[]{output[0], output[1]};
    }

    public final GameBoard placeRandomTile(GameBoard board) {
        int[] position = selectRandomEmptyPosition(board);
        return placeRandomTile(board, position);
    }

    public final GameBoard placeRandomTile(GameBoard board, int[] position) {
        GameBoard newBoard = new GameBoard(board);
        int[] row = newBoard.getGameGrid()[position[0]];//[position[1]];
        if (getRandom().nextDouble() < CHANCE_OF_A_FOUR) {
            // is a 2
            row[position[1]] = (1);
        } else {
            // is a 4
            row[position[1]] = (2);
        }
        return newBoard;
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

    private static class TileMove {

        /**
         * @return the position
         */
        public int[] getEmptyPosition() {
            return position;
        }

        /**
         * @return the next
         */
        public int[] getNextPosition() {
            return next;
        }
        private int[] position;
        private int[] next;

        public TileMove(int[] position, int[] next) {
            this.position = position;
            this.next = next;
        }
    }

    private final TileMove getFarthestTile(GameBoard board, int[] position, Direction direction) {
        int[][] grid = board.getGameGrid();
        int posX = position[0];
        int posY = position[1];
        switch (direction) {
            case UP:
                //println("getting farthest up from " + posX + ","+ posY);
                for (int y = posY - 1; y > -1; y--) {
                    if (grid[posX][y] != 0) {

                        return new TileMove(new int[]{posX, (int) (y + 1)}, new int[]{posX, (int) y});
                    }
                }
                return new TileMove(new int[]{posX, 0}, new int[]{posX, 0});
            //return new int[]{posX, 0};
            case DOWN:
                //println("getting farthest down from " + posX + ","+ posY);
                for (int y = posY + 1; y < ROW_SIZE; y++) {
                    if (grid[posX][y] != 0) {
                        //return new int[]{posX, i};
                        return new TileMove(new int[]{posX, (int)(y - 1)}, new int[]{posX,(int) y});
                    }
                }
                return new TileMove(new int[]{posX, ROW_SIZE - 1}, new int[]{posX, ROW_SIZE - 1});
            case LEFT:
                //println("getting farthest left from " + posX + ","+ posY);
                for (int x = posX - 1; x > -1; x--) {
                    if (grid[x][posY] != 0) {
                        return new TileMove(new int[]{(int) (x + 1), posY}, new int[]{(int) x, posY});
                    }
                }
                //return new int[]{0, posY};
                return new TileMove(new int[]{0, posY}, new int[]{0, posY});
            case RIGHT:
                //println("getting farthest right from " + posX + ","+ posY);
                for (int x = posX + 1; x < ROW_SIZE; x++) {
                    if (grid[x][posY] != 0) {
                        //return new int[]{i, posY};
                        return new TileMove(new int[]{(int) (x - 1), posY}, new int[]{(int) x, posY});
                    }
                }
                //return new int[]{GRID_SIZE, posY};
                return new TileMove(new int[]{ROW_SIZE - 1, posY}, new int[]{ROW_SIZE - 1, posY});
            default:
                throw new RuntimeException("Unexpected direction: " + direction);
        }
    }

    // only board-mutating method
    private final void doMerge(GameBoard board, int[] position, Direction direction) {
        int nodeValue = board.getGameGrid()[position[0]][position[1]];
        //println("Evaluating " + position[0] + "," + position[1] + " for merge");
        if (nodeValue != 0) {
            println("moving " + position[0] + "," + position[1]);
            TileMove farthestTile = getFarthestTile(board, position, direction);
            println("furthest point: " + farthestTile.getEmptyPosition()[0] + "," + farthestTile.getEmptyPosition()[1]);
            int[] farthestNodePosition = farthestTile.getNextPosition();
            int farthestX = farthestNodePosition[0];
            int farthestY = farthestNodePosition[1];
            int[] farthestEmptyPosition = farthestTile.getEmptyPosition();
            int farthestValue = board.getGameGrid()[farthestNodePosition[0]][farthestNodePosition[1]];
            if (!(farthestNodePosition[0] == position[0]
                    && farthestNodePosition[1] == position[1])
                    && farthestValue != 0 && farthestValue == nodeValue
                    && !board.getMergeGridPosition(farthestX, farthestY)) {
                int value = (int) (nodeValue + 1);
                board.getGameGrid()[farthestNodePosition[0]][farthestNodePosition[1]] = (value);
                println("Set furthest to " + value);
                board.setMergeGridPosition(farthestNodePosition[0], farthestNodePosition[1], true);
                board.getGameGrid()[position[0]][position[1]] = 0;
                //println("Set node to " + node.getValue());
                board.setScore(board.getScore() + (1 << value));//(int) Math.pow(2, value));
                //score += farthestNode.getValue();
                board.setMoved(true);
                board.setNumberOfMerges(board.getNumberOfMerges() + 1);
            } else {
                int[] newPos = farthestEmptyPosition;
                if (newPos[0] == position[0] && newPos[1] == position[1]) {
                } else {
                    board.getGameGrid()[newPos[0]][newPos[1]] = nodeValue;
                    board.getGameGrid()[position[0]][position[1]] = (0);
                    board.setMoved(true);
                }
            }
        }
        //println("skipped because 0: " + position[0] + "," + position[1]);
    }

    public final GameBoard doGameMove(GameBoard board, Direction direction) {
        GameBoard newBoard = moveGrid(board, direction);
        if (newBoard.isMoved()) {
            newBoard = placeRandomTile(newBoard);
        }
        return newBoard;
    }

    public final GameBoard moveGrid(GameBoard board, Direction direction) {
        // if positive vector move backwards
        GameBoard newBoard = new GameBoard(board);

        if (direction == Direction.RIGHT) {
            for (int x = ROW_SIZE - 1; x > -1; x--) {
                for (int y = 0; y < ROW_SIZE; y++) {
                    doMerge(newBoard, new int[]{x, y}, direction);
                }
            }
        } else if (direction == Direction.DOWN) {
            for (int x = 0; x < ROW_SIZE; x++) {
                for (int y = ROW_SIZE - 1; y > -1; y--) {
                    doMerge(newBoard, new int[]{x, y}, direction);
                }
            }
        } else {
            for (int x = 0; x < ROW_SIZE; x++) {
                //println("Moving row " + x + " for " + direction);
                for (int y = 0; y < ROW_SIZE; y++) {
                    doMerge(newBoard, new int[]{x, y}, direction);
                    //println("Moving column " + y + " for " + direction);
                }
            }
        }
        newBoard.setPreviousMove(direction);
        return newBoard;
    }

    public GameBoard createStartingGameboard() {
        GameBoard board = new GameBoard(ROW_SIZE);
        for (int i = 0; i < NUMBER_OF_STARTING_TILES; i++) {
            board = placeRandomTile(board);
        }

        // set a default value for movement;
        board.setPreviousMove(Direction.values()[0]);
        return board;
    }

    public boolean isMatchesAvailable(GameBoard board) {
        int[][] grid = board.getGameGrid();
        Direction[] directions = Direction.values();
        for (int x = 0; x < ROW_SIZE; x++) {
            for (int y = 0; y < ROW_SIZE; y++) {
                for (Direction direction : directions) {
                    int otherX = x;
                    int otherY = y;
                    switch (direction) {
                        case UP:
                            otherY -= 1;
                            break;
                        case DOWN:
                            otherY += 1;
                            break;
                        case LEFT:
                            otherX -= 1;
                            break;
                        case RIGHT:
                            otherX += 1;
                            break;
                        default:
                            throw new AssertionError(direction.name());
                    }
                    if (otherX >= ROW_SIZE || otherX < 0) {
                        continue;
                    }
                    if (otherY >= ROW_SIZE || otherY < 0) {
                        continue;
                    }
                    if (grid[x][y] == grid[otherX][otherY]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isEmptySpace(GameBoard board) {
        int[][] grid = board.getGameGrid();
        for (int x = 0; x < ROW_SIZE; x++) {
            for (int y = 0; y < ROW_SIZE; y++) {
                if (grid[x][y] == 0) {
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
