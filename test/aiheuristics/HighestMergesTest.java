package aiheuristics;

import gamemodel.GameBoard;
import gamemodel.GameController;
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
public class HighestMergesTest {
    private final GameController controller = new GameController();
    private GameBoard board;
    
    public HighestMergesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        board = new GameBoard(GameController.GRID_SIZE);
    }
    
    @After
    public void tearDown() {
        board = null;
    }

    /**
     * Test of getValueOfState method, of class HighestMerges.
     */
    @Test
    public void testGetValueOfState() {
        System.out.println("getValueOfState");
        GameController controller = new GameController();
        GameBoard state = new GameBoard(GameController.GRID_SIZE);
        int currentDirection = 0;
        HighestMerges instance = new HighestMerges();
        long expResult = 0L;
        long result = instance.getValueOfState(controller, state, currentDirection);
        assertEquals(expResult, result);
        
    }
    
}
