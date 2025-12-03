import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.ui.Button;
import java.util.List;

/**
 * Main controller class for the Game of Set application.
 * 
 * This class handles:
 * 
 *   Initializing the UI and game components</li>
 *   Managing user input (card clicking)</li>
 *   Tracking game statistics such as mistakes and correct sets</li>
 *   Refreshing the board</li>
 * 
 */
public class GameOfSet {
    CanvasWindow canvas; 
    private GameBoard gameBoard;
    private SetManager setManager;

    private GraphicsText mistakesDisplay;
    private int mistakes;

    private GraphicsText correctSetsDisplay; 
    private int correctSets;                         
          

    /**
     * Constructs a new GameOfSet instance, initializes the UI,
     * sets up event handlers, and begins the first animation frame.
     */
    public GameOfSet() {
        canvas = new CanvasWindow("Game of Set", 1400, 840);

        // Core game components
        gameBoard = new GameBoard(canvas); 
        setManager = new SetManager(gameBoard); 

        // Input handling
        canvas.onClick(e -> handleCardClick(e.getPosition())); 

        //UI elements
        createRefreshButton(); //initialize refresh button
        createMistakesDisplay(); //initialize mistakes display
        createCorrectSetDisplay();
        
        canvas.animate(()->{});
    }

    /**
     * Handles a click on the canvas. Determines whether a card was clicked,
     * updates card selection state, and processes guesses once three cards
     * have been selected.
     *
     * @param clickPosition The position of the mouse click
     */
    private void handleCardClick(Point clickPosition) {
        Point cardPosition = gameBoard.getCardPositionAt(
            (int)clickPosition.getX(), 
            (int)clickPosition.getY());
    
        if (cardPosition != null) {
            Card clickedCard = gameBoard.getCardAt(cardPosition);
            boolean isValidSelection = setManager.toggleCardSelection(clickedCard);
            List<Card> selectedCards = setManager.getSelectedCards();
            if (isValidSelection && selectedCards.size() == 3) {
                boolean isCorrectSet = setManager.processGuess(selectedCards.get(0), selectedCards.get(1), selectedCards.get(2));
                if (!isCorrectSet) {
                    mistakes++;
                    updateMistakesDisplay();
                } else {
                    correctSets++;
                    updateCorrectSetsDisplay();
                }
            }
        }
    }


    /**
     * Resets the game board by regenerating the cards, resetting statistics,
     * and updating the display components.
     */
    public void refreshGame() {
        setManager.generateBoard();
        setManager.assignPositions();
        gameBoard.setUpCards(setManager.currentCards);
        mistakes = 0; 
        correctSets = 0;
        updateMistakesDisplay();
        updateCorrectSetsDisplay();
       
    }

     /**
     * Creates and positions the Refresh button.
     */
     private void createRefreshButton() {
        Button refreshButton = new Button("Refresh");
        refreshButton.setPosition(
            GameBoard.CARD_WIDTH * 4 + GameBoard.PADDING * 5, 
            GameBoard.PADDING                                  
        );
        
        refreshButton.onClick(() -> refreshGame());
        canvas.add(refreshButton);
    }

    /**
     * Initializes the text display that shows the number of mistakes made.
     */
    public void createMistakesDisplay(){
        mistakesDisplay = new GraphicsText();
        mistakesDisplay.setText("Mistakes: " + mistakes);
        mistakesDisplay.setPosition(
            GameBoard.CARD_WIDTH * 4 + GameBoard.PADDING * 5,  
            GameBoard.PADDING + 50                             
        );
        canvas.add(mistakesDisplay);
    }

    /**
     * Updates the visible mistakes counter.
     */
    public void updateMistakesDisplay(){ 
        mistakesDisplay.setText("Mistakes: " + mistakes);
    }

    /**
     * Initializes the text display that shows the number of correct sets found.
     */
    private void createCorrectSetDisplay(){
        correctSetsDisplay = new GraphicsText();
        correctSetsDisplay.setText("Correct Sets: " + correctSets);
        correctSetsDisplay.setPosition(
        GameBoard.CARD_WIDTH * 4 + GameBoard.PADDING * 5,  
        GameBoard.PADDING + 80);
        canvas.add(correctSetsDisplay);
    }

    /**
     * Updates the displayed number of correct sets.
     */
    private void updateCorrectSetsDisplay(){
        correctSetsDisplay.setText("Correct Sets: " + correctSets);
    }
    
    /**
     * Main entry point for launching the game.
     */
    public static void main(String[] args) {
        new GameOfSet();
    }    

}
