import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.ui.Button;
import java.util.List;

public class GameOfSet {
    CanvasWindow canvas; 
    private GameBoard gameBoard;
    private SetManager setManager;

    private GraphicsText mistakesDisplay;
    private int mistakes;

    private GraphicsText correctSetsDisplay; 
    private int correctSets;                         
          


    public GameOfSet() {
        canvas = new CanvasWindow("Game of Set", 1400, 740);
        gameBoard = new GameBoard(canvas); //pass reference to CameBoard
        setManager = new SetManager(gameBoard); //pass the reference to CardManager
        canvas.onClick(e -> handleCardClick(e.getPosition())); //handle clicking

        //UI objects
        createRefreshButton(); //initialize refresh button
        createMistakesDisplay(); //initialize mistakes display
        createCorrectSetDisplay();
        
        canvas.animate(()->{});
    }

    /*
     * Handle clicking actions 
     * Calls methods needed for selection to process guesses and update graphics
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
                boolean isCorrectSet = setManager.processGuess(selectedCards);
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


    /*
     * Called when Refresh button clicked
     * Initializes game
     * resets mistakes and correct sets to 
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

     /*
      * Initializes refresh buttons 
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

    /*
     * Initialize mistake display
     * below refresh button, same x position 
     */
    public void createMistakesDisplay(){
        mistakesDisplay = new GraphicsText();
        mistakesDisplay.setText("Mistakes made: " + mistakes);
        mistakesDisplay.setPosition(
            GameBoard.CARD_WIDTH * 4 + GameBoard.PADDING * 5,  
            GameBoard.PADDING + 50                             
        );
        canvas.add(mistakesDisplay);
    }

    /*
     * Called when a mistake is made
     */
    public void updateMistakesDisplay(){ 
        mistakesDisplay.setText("Mistakes: " + mistakes);
    }

    /*
     * Initialize correct set display
     * Same x as refresh button
     */
    private void createCorrectSetDisplay(){
        correctSetsDisplay = new GraphicsText();
        correctSetsDisplay.setText("Correct Sets: " + correctSets);
        correctSetsDisplay.setPosition(
        GameBoard.CARD_WIDTH * 4 + GameBoard.PADDING * 5,  
        GameBoard.PADDING + 100);
        canvas.add(correctSetsDisplay);
    }

    /*
     * Called when a correct guess is made
     */
    private void updateCorrectSetsDisplay(){
        correctSetsDisplay.setText("Correct Sets: " + correctSets);
    }

    public static void main(String[] args) {
        new GameOfSet();
    }    

}
