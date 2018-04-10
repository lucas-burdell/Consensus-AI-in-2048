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
import java.util.Random;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class GameBoard {
    
    private static long creations = 0;

    /**
     * @return the creations
     */
    public static long getCreations() {
        return creations;
    }

    /**
     * @param aCreations the creations to set
     */
    public static  void incCreations() {
        //creations++;
    }
    
    

    /**
     * @return the numberOfMerges
     */
    public int getNumberOfMerges() {
        return numberOfMerges;
    }

    /**
     * @param numberOfMerges the numberOfMerges to set
     */
    public void setNumberOfMerges(int numberOfMerges) {
        this.numberOfMerges = numberOfMerges;
    }

    /**
     * @param x
     * @param y
     * @return the mergeGrid
     */
    public boolean getMergeGridPosition(int x, int y) {
        return mergeGrid[y][x];
    }

    /**
     * @param x
     * @param value
     * @param y
     */
    public void setMergeGridPosition(int x, int y, boolean value) {
        this.mergeGrid[y][x] = value;
    }
    
    private int score = 0;
    private boolean moved = false;
    private Direction previousMove = null;
    private int numberOfMerges = 0;

    /**
     * @return the gameGrid
     */
    public int[][] getGameGrid() {
        return gameGrid;
    }

    /**
     * @param gameGrid the gameGrid to set
     */
    public void setGameGrid(int[][] gameGrid) {
        this.gameGrid = gameGrid;
    }
 
    private int[][] gameGrid;
    private boolean[][] mergeGrid;
    
    
    public GameBoard(int gridSize){
        gameGrid =  new int[gridSize][gridSize];
        mergeGrid = new boolean[gridSize][gridSize];
        incCreations();
    }
    
    public ArrayList<Integer[]> getEmptyPositions() {
        ArrayList<Integer[]> output = new ArrayList<>(16);
        for (int i = 0; i < gameGrid.length; i++) {
            int[] gameNodes = gameGrid[i];
            for (int j = 0; j < gameNodes.length; j++) {
                int gameNode = gameNodes[j];
                if (gameNode == 0) {
                    output.add(new Integer[]{i, j});
                }
            }
        }
        return output;
    }
    
    public GameBoard(GameBoard board) {
        int gridSize = board.getGameGrid().length;
        int[][] otherGrid = board.getGameGrid();
        this.gameGrid = new int[gridSize][gridSize];
        for (int i = 0; i < gameGrid.length; i++) {
            int[] gameNodes = otherGrid[i];
            for (int j = 0; j < gameNodes.length; j++) {
                int gameNode = gameNodes[j];
                gameGrid[i][j] = gameNode;
            }
        }
        this.score = board.getScore();
        this.mergeGrid = new boolean[gridSize][gridSize];
        //incCreations();
    }
    
    public GameBoard(int[][] grid) {
        int gridSize = grid.length;
        this.gameGrid = grid;
//        this.gameGrid = new int[gridSize][gridSize];
//        for (int y = 0; y < gameGrid.length; y++) {
//            int[] row = gameGrid[y];
//            for (int x = 0; x < row.length; x++) {
//                gameGrid[y][x] = grid[y][x];
//            }
//        }
        this.score = 0;
        this.mergeGrid = new boolean[gridSize][gridSize];
        incCreations();
    }
    
    public GameBoard(String storage) {
        String[] numbers = storage.split(",");
        this.gameGrid = new int[GameController.ROW_SIZE][GameController.ROW_SIZE];
        this.mergeGrid = new boolean[GameController.ROW_SIZE][GameController.ROW_SIZE];
        int stringIndex = 0;
        for (int row = 0; row < GameController.ROW_SIZE; row++) {
            for (int col = 0; col < GameController.ROW_SIZE; col++) {
                gameGrid[row][col] = Byte.parseByte(numbers[stringIndex]);
            }
        }
        incCreations();
    }
    
    
    public String toStorageString(){
        StringBuilder output = new StringBuilder();
        for (int[] row : gameGrid) {
            for (int value : row) {
                output.append(value).append(",");
            }
        }
        output.deleteCharAt(output.length() - 1);
        return output.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder[] output = new StringBuilder[gameGrid.length];
        for (int y = 0; y < gameGrid.length; y++) {
            output[y] = new StringBuilder();
            for (int x = 0; x < gameGrid.length; x++) {
                if (gameGrid[x][y] == 0) {
                    output[y] = output[y].append("\t\t[").append(0).append("]");
                } else {
                    output[y] = output[y].append("\t\t[").append(
                            (int) Math.pow(2, gameGrid[x][y])).append("]");
                }
            }
        }
        StringBuilder outputLine = new StringBuilder();
        for (int i = 0; i < output.length; i++) {
            outputLine = outputLine.append(output[i]).append("\n");
        }
        return outputLine.toString();
//        for (int x = 0; x < gameGrid.length; x++) {
//            if (x < gameGrid.length) {
//                output = output + "\n";
//            }
//            for (int y = 0; y < gameGrid[x].length; y++) {
//                output = output + "[" + gameGrid[x][y] + "]";
//                if (y < gameGrid[x].length - 1) {
//                    output = output + " ";
//                }
//            }
//        }
//        return output;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the moved
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * @param moved the moved to set
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    /**
     * @return the previousMove
     */
    public Direction getPreviousMove() {
        return previousMove;
    }

    /**
     * @param previousMove the previousMove to set
     */
    public void setPreviousMove(Direction previousMove) {
        this.previousMove = previousMove;
    }
    
    
}
