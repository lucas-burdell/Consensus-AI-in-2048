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
    private GameBoard emptyState;
    private HighestMerges instance;
    
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
        instance = new HighestMerges();
        emptyState = new GameBoard(GameController.ROW_SIZE);
    }
    
    @After
    public void tearDown() {
        emptyState = null;
        instance = null;
    }

    /**
     * Test of getValueOfState method, of class HighestMerges.
     */
    @Test
    public void testGetValueOfState() {
        System.out.println("getValueOfState");
        int currentDirection = 0;
        long expResult = 0L;
        long result = instance.getValueOfState(controller, emptyState, currentDirection);
        assertEquals(expResult, result);
        
    }
    
}
