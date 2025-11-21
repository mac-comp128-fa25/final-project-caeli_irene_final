import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.macalester.graphics.Point;
import java.util.Random;
import java.util.Iterator;

public class SetManager {

    private final Map<Point, Card> currentCards = new HashMap();
    private ArrayList<Card> selectedCards; 
    private GameBoard gameBoard;
    private Guess guess = new Guess();
    private Deck cards = new Deck();
    private List<List<String>> sets = new ArrayList<>();
    private List<Card> board;
    private Random random = new Random();

    public SetManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.selectedCards = new ArrayList<>();
    }

    public List<Card> generateBoard(){
        board = new ArrayList<>();
        int set = 0;
        int stop = 0;
        addCard(board);
        addCard(board);
        addCard(board);
        addCard(board);
        while(set<6 && stop < 100){
            if(board.size()>=12){
                board.remove(random.nextInt(11));
                addSet(board);
            }
            int ran = random.nextInt(10);
            if(ran<3){
                addCard(board);
            } else{
                addSet(board);
            }
            set = checkSets(board);
            stop++;
        }
        stop = 0;
        while(board.size()<12 && stop < 100){
            List<String> card = cards.getNextCard();
            board.add(new Card(card.get(0), card.get(1), card.get(2), Integer.valueOf(card.get(3))));
            set = checkSets(board);
            if(set > 6){
                board.remove(board.size()-1);
            }
            stop++;
        }
        return board;
    }

    private void addSet(List<Card> board){
        int ran1 = random.nextInt(board.size()-1);
        int ran2 = random.nextInt(board.size()-1);
        while(ran2==ran1){
            ran2 = random.nextInt(board.size()-1);
        }
        Card test = getThird(board.get(ran1),board.get(ran2));
        if(!checkCard(board, test)){
            board.add(test);
            cards.removeCard(List.of(test.getShape(), test.getColor(), test.getFill(), test.getNumber().toString()));
        } 
    }

    private void addCard(List<Card> board){
        Card test = new Card();
        if(!checkCard(board, test)){
            board.add(test);
            cards.removeCard(List.of(test.getShape(), test.getColor(), test.getFill(), test.getNumber().toString()));
        }
    }

    private boolean checkCard(List<Card> board, Card test){
        Iterator<Card> iter = board.iterator();
        while(iter.hasNext()){
            Card old = iter.next();
            if(old.isEqual(test)){
                return true;
            }
        }
        return false;
    }

    private int checkSets(List<Card> board){
        int sets = 0;
        this.sets.clear();
        for(int i=0; i<board.size()-2;i++){
            for(int j=i+1; j<board.size()-1; j++){
                for (int k=j+1; k<board.size();k++){
                    if(i==j || j==k || i==k){
                    } else {
                        if(guess.isValidSet(board.get(i),board.get(j),board.get(k))){
                            sets++;
                            this.sets.add(List.of(board.get(i).toString(),board.get(j).toString(),board.get(k).toString()));
                        }
                    }
                }
            }
        }
        return sets;
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

    public Card getThird(Card card1, Card card2){
        String color;
        String shape;
        String fill;
        int num;
        if(card1.getColor()==card2.getColor()){
            color = card1.getColor();
        } else{
            color = card1.thirdColor(card2);
        }
        if(card1.getShape()==card2.getShape()){
            shape = card1.getShape();
        } else{
            shape = card1.thirdShape(card2);
        }
        if(card1.getFill()==card2.getFill()){
            fill = card1.getFill();
        } else{
            fill = card1.thirdFill(card2);
        }
        if(card1.getNumber()==card2.getNumber()){
            num = card1.getNumber();
        } else{
            num = 6-card1.getNumber()-card2.getNumber();
        }
        return new Card(shape, color, fill, num);
    }

    /*
     * Passes 3 cards selected in the guess class
     * Remove and replace if the guess is correct
     */
    public boolean processGuess(Card card1, Card card2, Card card3) {
        if (!guess.isValidSet(card1, card2, card3)) {
            return false;
        }

        Point pos1 = card1.getPosition();
        Point pos2 = card2.getPosition();
        Point pos3 = card3.getPosition();
    
        if (pos1 != null) currentCards.remove(pos1);
        if (pos2 != null) currentCards.remove(pos2);
        if (pos3 != null) currentCards.remove(pos3);

        return true;
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

        // Still need to be added

        clearSelection();
        return isValid;
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

    public List<Card> getBoard(){
        return new ArrayList<>(board);
    }

    public void main(String arg[]){
        // Card card1 = new Card();
        // Card card2 = new Card();
        // Card card3 = getThird(card1, card2);
        // for(Card test : List.of(card1, card2, card3)){
        //     System.out.println(test.getShape());
        //     System.out.println(test.getColor());
        //     System.out.println(test.getFill());
        //     System.out.println(test.getNumber());
        // }
        List<Card> board = generateBoard();
        for(Card test : board){
            System.out.println(test.getShape()+" "+test.getColor()+" "+test.getFill()+" "+test.getNumber());
        }
    }
}
