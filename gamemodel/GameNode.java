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

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class GameNode {

    /**
     * @return the merged
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     * @param merged the merged to set
     */
    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
    private int value;
    private boolean merged;
    
    public GameNode(){
        this(0);
    }
    
    public GameNode(int value) {
        this.value = value;
    }
    
    public GameNode(GameNode node) {
        this.value = node.getValue();
    }
    
    public String toString() {
        if (value > 0) {
            return Integer.toString((int) Math.pow(2, value));
        } else {
            return "0";
        }
    }
}
