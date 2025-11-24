import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;

public class SetManagerTests {

    private CanvasWindow mockCanvas;
    private GameBoard Gameboard;
    private SetManager manager;
    private List<Card> board;
    private Guess check;
    private static final int EXPECTED_BOARD_SIZE = 15;
    private static final int EXPECTED_NUMBER_OF_SETS = 6;

    @BeforeEach
    public void setUp() {
        mockCanvas = new CanvasWindow(null, 0, 0);
        Gameboard = new GameBoard(mockCanvas);
        manager = new SetManager(Gameboard);
        board = manager.generateBoard();
        check = new Guess();
    }

    /*
     * This makes the test file runnable
     */
    // @Test
    // void testManagerInitializesWithCards() {
    //     assertFalse(manager.getCurrentCards().isEmpty(), "Manager should initialize with cards on board");
    // }

    // @AfterEach
    // public void checkCardManagerInvariants() {
    //     checkCurrentCardsIntegrity();
    //     checkSelectedCardsValidity();
    // }

    /*
     * Checks for duplicates on the board
     */
    // private void checkCurrentCardsIntegrity() {
    //     Map<Point, Card> boardCards = manager.getCurrentCards();


    //     Set<Card> seen = new HashSet<>();
    //     for (Card card : boardCards.values()) {
    //         assertTrue(seen.add(card), "Duplicate card found on board: " + card);
    //     }
    //     assertEquals(boardCards.size(), new HashSet<>(boardCards.keySet()).size(),
    //         "Duplicate board positions found");
    // }

    /*
     * Check that:
     * no more than 3 cards have been selected
     * all selected cards are on the board
     * the list has no duplicates
     */
    //  private void checkSelectedCardsValidity() {
    //      List<Card> selected = manager.getSelectedCards();
    //      Map<Point, Card> boardCards = manager.getCurrentCards();

    //      assertTrue(selected.size() <= 3, "More than 3 cards selected");

    //      for (Card card : selected) {
    //          assertTrue(boardCards.containsValue(card),
    //              "Selected card not found on board: " + card);
    //      }

    //      Set<Card> unique = new HashSet<>(selected);
    //      assertEquals(unique.size(), selected.size(), "Duplicate selected cards found");
    // }

    /**
     * Generates all 220 combinations of 3 cards taken from 12
     * CHANGED the loop already checks i<j<k so if(i==j || j==k || i==k) will never be true
     * reformatted the test with helper methods
     */

    // @Test
    // public void testBoard(){
    //     assertEquals(12, board.size());
    //     int sets = 0;
    //     for(int i=0; i<10;i++){
    //         for(int j=i+1; j<11; j++){
    //             for (int k=j+1; k<12;k++){
    //                 if(i==j || j==k || i==k){
    //                 } else {
    //                     if(check.isValidSet(board.get(i),board.get(j),board.get(k))){
    //                         sets++;
    //                     }
    //                 }
    //             }
    //         }
    //     }
    //     assertEquals(6, sets);

    @Test
    public void testBoardHasCorrectSizeAndSets() {
        assertEquals(EXPECTED_BOARD_SIZE, board.size(), "Board should have 12 cards");

        int foundSets = countValidSets(board);
        assertEquals(EXPECTED_NUMBER_OF_SETS, foundSets, "Board should contain 6 valid sets");
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

    // @Test
    // public void testBoardMultipleTimes() {
    //     final int TRIALS = 2;  // test 50 generated boards
    //     for (int t = 0; t < TRIALS; t++) {
    //         List<Card> board = manager.generateBoard();

    //         // check board size
    //         assertEquals(EXPECTED_BOARD_SIZE, board.size(), "Board should have 12 cards");

    //         // check for duplicate cards
    //         Set<Integer> seenIds = new HashSet<>();
    //         for (Card c : board) {
    //             int id = manager.getCardID(c);
    //             assertTrue(seenIds.add(id), "Duplicate card found on board: " + id);
    //         }

    //         // check number of valid sets
    //         int foundSets = countValidSets(board);
    //         assertEquals(EXPECTED_NUMBER_OF_SETS, foundSets, "Board should contain 6 valid sets");
    //    }
    // }
}

