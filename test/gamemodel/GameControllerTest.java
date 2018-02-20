/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamemodel;

import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lucas.burdell
 */
public class GameControllerTest {
    
    public GameControllerTest() {
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
     * Test of createAllPossibleNewStates method, of class GameController.
     */
    @Test
    public void testCreateAllPossibleNewStates() {
        System.out.println("createAllPossibleNewStates");
        GameBoard board = null;
        GameController instance = new GameController();
        GameBoard[] expResult = null;
        GameBoard[] result = instance.createAllPossibleNewStates(board);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of placeRandomTile method, of class GameController.
     */
    @Test
    public void testPlaceRandomTile_GameBoard() {
        System.out.println("placeRandomTile");
        GameBoard board = null;
        GameController instance = new GameController();
        GameBoard expResult = null;
        GameBoard result = instance.placeRandomTile(board);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of placeRandomTile method, of class GameController.
     */
    @Test
    public void testPlaceRandomTile_GameBoard_intArr() {
        System.out.println("placeRandomTile");
        GameBoard board = null;
        int[] position = null;
        GameController instance = new GameController();
        GameBoard expResult = null;
        GameBoard result = instance.placeRandomTile(board, position);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRandom method, of class GameController.
     */
    @Test
    public void testGetRandom() {
        System.out.println("getRandom");
        GameController instance = new GameController();
        Random expResult = null;
        Random result = instance.getRandom();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRandom method, of class GameController.
     */
    @Test
    public void testSetRandom() {
        System.out.println("setRandom");
        Random random = null;
        GameController instance = new GameController();
        instance.setRandom(random);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doGameMove method, of class GameController.
     */
    @Test
    public void testDoGameMove() {
        System.out.println("doGameMove");
        GameBoard board = null;
        Direction direction = null;
        GameController instance = new GameController();
        GameBoard expResult = null;
        GameBoard result = instance.doGameMove(board, direction);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of moveGrid method, of class GameController.
     */
    @Test
    public void testMoveGrid() {
        System.out.println("moveGrid");
        GameBoard board = null;
        Direction direction = null;
        GameController instance = new GameController();
        GameBoard expResult = null;
        GameBoard result = instance.moveGrid(board, direction);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createStartingGameboard method, of class GameController.
     */
    @Test
    public void testCreateStartingGameboard() {
        System.out.println("createStartingGameboard");
        GameController instance = new GameController();
        GameBoard expResult = null;
        GameBoard result = instance.createStartingGameboard();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isMatchesAvailable method, of class GameController.
     */
    @Test
    public void testIsMatchesAvailable() {
        System.out.println("isMatchesAvailable");
        GameBoard board = null;
        GameController instance = new GameController();
        boolean expResult = false;
        boolean result = instance.isMatchesAvailable(board);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isEmptySpace method, of class GameController.
     */
    @Test
    public void testIsEmptySpace() {
        System.out.println("isEmptySpace");
        GameBoard board = null;
        GameController instance = new GameController();
        boolean expResult = false;
        boolean result = instance.isEmptySpace(board);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isGameOver method, of class GameController.
     */
    @Test
    public void testIsGameOver() {
        System.out.println("isGameOver");
        GameBoard board = null;
        GameController instance = new GameController();
        boolean expResult = false;
        boolean result = instance.isGameOver(board);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
