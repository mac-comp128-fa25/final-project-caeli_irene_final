import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.macalester.graphics.Point;

public class CardManager {
    
    private final Map<Point, Card> currentCards;
    private Deck deck;
    private ArrayList<Guess> correctGuesses;
    private ArrayList<Card> selectedCards; 
    private GameBoard gameBoard;

    public CardManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.deck = new Deck();
        this.currentCards = new HashMap<>();
        this.correctGuesses = new ArrayList<>();
        this.selectedCards = new ArrayList<>();
        initializeBoard();
    }
  
    /*
    * Return defensive copy of current cards
    */
    public Map<Point, Card> getCurrentCards() {
        return new HashMap<>(currentCards);
    }

    /*
    * initialize board by resetting the list of current cards in deck
    */
    public void initializeBoard() {
        currentCards.clear();
        deck.clearList();
        fillBoardToCapacity();  //fill to 12 cards initially
    }

    /*
     * Resets board to new card spread
     */
   public void replenishBoard() {
        if (currentCards.size() >= 12) {
            return; //already full
        }
        fillBoardToCapacity();
    }

    /*
     * Fills each empty position with the next card in the deck
     * If all cards in the deck have been used resets the board from 0
     */
    private void fillBoardToCapacity() {
        List<Point> emptyPositions = getEmptyPositions();
        for (Point pos : emptyPositions) {
            Card nextCard = deck.getNextCard();
            if (deck.getUsedCardSize()==81){
                initializeBoard();
            }
            currentCards.put(pos, nextCard);
        }
        gameBoard.setUpCards(currentCards);
    }

    /*
     * Iterates through all positions to check which one does not have a card object
     */
    private List<Point> getEmptyPositions() {
        List<Point> allPositions = generateGridPositions();
        List<Point> emptyPositions = new ArrayList<>();
        for (Point pos : allPositions) {
            if (!currentCards.containsKey(pos)) {
                emptyPositions.add(pos);
            }
        }
        return emptyPositions;
    }
    
    /*
     * Places cards on the Gameboard object
     */
    private List<Point> generateGridPositions() {
        List<Point> positions = new ArrayList<>();
        int cols = 4;
        int rows = 3;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                positions.add(new Point(
                    col * (GameBoard.CARD_WIDTH + GameBoard.PADDING),
                    row * (GameBoard.CARD_HEIGHT + GameBoard.PADDING)
                ));
            }
        }
        return positions;
    }

    /*
     * Passes 3 cards selected in the guess class
     * Remove and replace if the guess is correct
     */
    public boolean processGuess(Card card1, Card card2, Card card3) {
        Guess guess = new Guess();
        if (!guess.isValidSet(card1, card2, card3)) {
            return false;
        }

        Point pos1 = findCardPosition(card1);
        Point pos2 = findCardPosition(card2);
        Point pos3 = findCardPosition(card3);
    
        if (pos1 != null) currentCards.remove(pos1);
        if (pos2 != null) currentCards.remove(pos2);
        if (pos3 != null) currentCards.remove(pos3);

        correctGuesses.add(guess);
        replenishBoard();
        return true;
    }

    /*
     * Takes in clicked card from GameOfSet
     * Returns the corresponding position in the currentCards HashMap
     */

    private Point findCardPosition(Card card) {
        for (Map.Entry<Point, Card> entry : currentCards.entrySet()) {
            if (entry.getValue().equals(card)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /*
     * Takes in clicked card from GameOfSet
     * Checks no more than 3 cards are selected at the same time
     * Passes selected card onto Card class to highlight it
     */
    public boolean toggleCardSelection(Card card) {
        boolean newSelectedState = !card.isSelected();
        card.setSelected(newSelectedState);

        if (newSelectedState) {
            if (selectedCards.size() < 3) {
                selectedCards.add(card);
            } else {
                card.setSelected(false);
                return false;
            }
        } else {
            selectedCards.remove(card);
        }

        return true;
    }

    /*
     * Takes in set of selected cards to check if it is a correct guess
     * Passes the 3 selected cards into the guess class
     * If the guess is valid the selected cards are removed from the board
     */
    public boolean processCurrentSelection() {
        if (selectedCards.size() != 3) {
            return false;
        }

        Guess guess = new Guess();
        boolean isValid = guess.isValidSet(
            selectedCards.get(0),
            selectedCards.get(1), 
            selectedCards.get(2));

        if (isValid) {
            removeCardsFromBoard(selectedCards);
            correctGuesses.add(guess);
            replenishBoard();
        } 

        clearSelection();
        return isValid;
    }
    

    /**
     * Removes cards from the current board positions
     */
    private void removeCardsFromBoard(List<Card> cardsToRemove) {
        List<Point> positionsToRemove = new ArrayList<>();
        
        for (Map.Entry<Point, Card> entry : currentCards.entrySet()) {
            if (cardsToRemove.contains(entry.getValue())) {
                positionsToRemove.add(entry.getKey());
            }
        }
        
        for (Point pos : positionsToRemove) {
            currentCards.remove(pos);
        }
    }

    /**
    * Clears current selection and visual state
    */
    public void clearSelection() {
        for (Card card : selectedCards) {
            if (card.isSelected()) {  
                card.setSelected(card.isSelected()); 
            }
        }
        selectedCards.clear();

    }

    public List<Card> getSelectedCards() {
        return new ArrayList<>(selectedCards);
    }

    public int getCorrectSetsCount() {
        return correctGuesses.size();
    }
}


    
