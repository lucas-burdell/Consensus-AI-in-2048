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
package aidecision;

import gamemodel.Direction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lucas Burdell <lucasburdell@gmail.com>
 */
public class RandomBagVotingTest {
    
    public RandomBagVotingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    /**
     * Test of evaluateVotes method, of class MajorityVoting.
     */
    @Test
    public void testEvaluateVotes() {
        System.out.println("evaluateVotes");
        int[] votes = new int[]{1, 0, 0, 0};
        RandomBagVoting instance = new RandomBagVoting();
        Direction expResult = Direction.values()[0];
        Direction result = instance.evaluateVotes(votes);
        assertEquals(expResult, result);

        votes = new int[]{1, 1, 0, 0};
        result = instance.evaluateVotes(votes);
        if (!(result == Direction.values()[0] || result == Direction.values()[1])) {
            fail("EvaluateVotes failed to select between two same values.");
        }
    } 
}
