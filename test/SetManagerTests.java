import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import edu.macalester.graphics.CanvasWindow;

public class SetManagerTests {

    private CanvasWindow mockCanvas;
    private GameBoard Gameboard;
    private SetManager manager;
    private List<Card> board;
    private Guess check;
    private static final int EXPECTED_BOARD_SIZE = 12;
    private static final int EXPECTED_NUMBER_OF_SETS = 6;

    @BeforeEach
    public void setUp() {
        mockCanvas = new CanvasWindow(null, 0, 0);
        Gameboard = new GameBoard(mockCanvas);
        manager = new SetManager(Gameboard); 
        check = new Guess();
    }

    @RepeatedTest(10)
    public void testBoardHasCorrectSizeAndSets() {
        board = manager.generateBoard();
        assertEquals(EXPECTED_BOARD_SIZE, board.size(), "Board should have 12 cards");

        int foundSets = countValidSets(board);
        assertTrue(foundSets>=EXPECTED_NUMBER_OF_SETS);
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

    @AfterEach
    public void noDuplicates(){
        Set<Integer> seenIds = new HashSet<>();
        for (Card c : board) {
            int id = c.getId();
            assertTrue(seenIds.add(id), board.toString());
        }
    }
}

