import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;

public class SetManagerTests {

    private CanvasWindow mockCanvas;
    private GameBoard Gameboard;
    private SetManager manager;
    private List<Card> board;
    private Random random = new Random();
    private Guess check;
    private static final int EXPECTED_BOARD_SIZE = 12;
    private static final int EXPECTED_NUMBER_OF_SETS = 6;

    @BeforeEach
    public void setUp() {
        mockCanvas = new CanvasWindow(null, 0, 0);
        Gameboard = new GameBoard(mockCanvas);
        manager = new SetManager(Gameboard); 
        board = manager.generateBoard();
        check = new Guess();
    }

    @RepeatedTest(10)
    public void testBoardHasCorrectSizeAndSets() {
        assertEquals(EXPECTED_BOARD_SIZE, board.size(), "Board should have 12 cards");

        int foundSets = countValidSets(board);
        assertTrue(foundSets>=EXPECTED_NUMBER_OF_SETS);
        //assertEquals(EXPECTED_NUMBER_OF_SETS, foundSets, "Board should contain 6 valid sets");
    }

    // Counts all valid sets of 3 cards in the board using the Guess checker. */
    private int countValidSets(List<Card> cards) {
        int sets = 0;
        int n = cards.size();
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (check.isValidSet(cards.get(i), cards.get(j), cards.get(k))) {
                        sets++;
                    }
                }
            }
        }
        return sets;
    }

    @Test
    public void testGetThird(){
        manager.board = new ArrayList<>();
        manager.getUnconnected();
        manager.getUnconnected();
        manager.getUnconnected();
        manager.getUnconnected();
        assertTrue(manager.board.size()==4);
        int a = random.nextInt(manager.board.size()-1);
        int b = random.nextInt(manager.board.size()-1);
        while(a==b || manager.checkIfSet(manager.board.get(a),manager.board.get(b))){
            b = random.nextInt(manager.board.size()-1);
        }
        manager.getThird(manager.board.get(a), manager.board.get(b));
        assertEquals(5,manager.board.size());
        assertEquals(1,manager.checkSets(manager.board));
        assertTrue(manager.board.size()==5&&manager.checkSets(manager.board)==1);
        manager.getUnconnected();
    }

    @Test
    public void checkConnections(){
        manager.board = new ArrayList<>();
        for(int i=0; i<2;i++){
            Card temp = new Card(i);
            manager.board.add(temp);
            manager.graphPoint.put(temp, manager.iterate);
            manager.iterate++;
            manager.used[temp.getId()] = true;
        }
        assertEquals(0,manager.checkSets(manager.board));
        assertEquals(2,manager.getConnected()[2]);
    }

    @AfterEach
    public void noDuplicates(){
        Set<Integer> seenIds = new HashSet<>();
        for (Card c : board) {
            int id = c.getId();
            assertTrue(seenIds.add(id), board.toString());
        }
    }
}

