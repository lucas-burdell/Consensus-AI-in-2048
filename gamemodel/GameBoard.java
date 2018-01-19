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

    /**
     * @return the gameGrid
     */
    public GameNode[][] getGameGrid() {
        return gameGrid;
    }

    /**
     * @param gameGrid the gameGrid to set
     */
    public void setGameGrid(GameNode[][] gameGrid) {
        this.gameGrid = gameGrid;
    }
 
    private GameNode[][] gameGrid;
    
    public GameBoard(int gridSize){
        gameGrid =  new GameNode[gridSize][gridSize];
        for (int i = 0; i < gameGrid.length; i++) {
            GameNode[] nodeRow = gameGrid[i];
            for (int j = 0; j < nodeRow.length; j++) {
                nodeRow[j] = new GameNode();
            }
        }
    }
    
    public ArrayList<Integer[]> getEmptyPositions() {
        ArrayList<Integer[]> output = new ArrayList<>();
        for (int i = 0; i < gameGrid.length; i++) {
            GameNode[] gameNodes = gameGrid[i];
            for (int j = 0; j < gameNodes.length; j++) {
                GameNode gameNode = gameNodes[j];
                if (gameNode.getValue() == 0) {
                    output.add(new Integer[]{i, j});
                }
            }
        }
        return output;
    }
    
    public GameBoard(GameBoard board) {
        int gridSize = board.getGameGrid().length;
        gameGrid = new GameNode[gridSize][gridSize];
        for (int i = 0; i < board.getGameGrid().length; i++) {
            GameNode[] gameNodes = board.getGameGrid()[i];
            for (int j = 0; j < gameNodes.length; j++) {
                GameNode gameNode = gameNodes[j];
                gameGrid[i][j] = new GameNode(gameNode.getValue());
            }
        }
    }
    
    @Override
    public String toString(){
        StringBuilder[] output = new StringBuilder[gameGrid.length];
        for (int y = 0; y < gameGrid.length; y++) {
            output[y] = new StringBuilder();
            for (int x = 0; x < gameGrid.length; x++) {
                output[y] = output[y].append(" [").append(gameGrid[x][y].toString()).append("]");
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
    
    
}
