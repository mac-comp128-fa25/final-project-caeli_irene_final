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
    //@Test
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
    // private void checkSelectedCardsValidity() {
    //     List<Card> selected = manager.getSelectedCards();
    //     Map<Point, Card> boardCards = manager.getCurrentCards();

    //     assertTrue(selected.size() <= 3, "More than 3 cards selected");

    //     for (Card card : selected) {
    //         assertTrue(boardCards.containsValue(card),
    //             "Selected card not found on board: " + card);
    //     }

    //     Set<Card> unique = new HashSet<>(selected);
    //     assertEquals(unique.size(), selected.size(), "Duplicate selected cards found");
    // }

    @Test
    void testBoard(){
        assertEquals(12, board.size());
        int sets = 0;
        for(int i=0; i<10;i++){
            for(int j=i+1; j<11; j++){
                for (int k=j+1; k<12;k++){
                    if(i==j || j==k || i==k){
                    } else {
                        if(check.isValidSet(board.get(i),board.get(j),board.get(k))){
                            sets++;
                        }
                    }
                }
            }
        }
        assertEquals(6, sets);
    }
}
