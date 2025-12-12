import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.macalester.graphics.Point;
import java.util.Random;
import java.util.Iterator;

/**
 * Generate 12 random cards that contain 6 or more sets
 * initialize 2D array to categorize card properties -> eliminating cards you can't pick
 */
public class SetManager {

    public Map<Point, Card> currentCards = new HashMap<>();
    private ArrayList<Card> selectedCards; 
    private GameBoard gameBoard;
    private Guess guess = new Guess();
    public List<Card> board;
    public int[][] graph = new int[12][12];
    public Map<Card, Integer> graphPoint = new HashMap<>();
    public int iterate = 0;
    private Random random = new Random();
    public final boolean[] used = new boolean[81];     

    /**
     * Constructs a new SetManager with a bpard of 12 cards with 6 or more sets
     * @param gameBoard the GameBoard that the cards will be added to
     */
    public SetManager(GameBoard gameBoard) { 
        this.gameBoard = gameBoard;
        this.selectedCards = new ArrayList<>();
        generateBoard();
        assignPositions();
        gameBoard.setUpCards(currentCards);
    }

    /**
     * Generates a list of cards that make a valid board
     */
    public List<Card> generateBoard(){
        guess.clear();
        board = new ArrayList<>();
        iterate = 0;
        Arrays.fill(used, false); // reset used cards so all 81 IDs are used

        long start = System.nanoTime();
        boolean success = boardFill(start); // find first slot at pos=0
        while(!success){
            start = System.nanoTime();
            success = boardFill(start);
        }
        return new ArrayList<>(board);
    }

    /**
     * A recurssive method to randomly builds the board
     */
    private boolean boardFill(long start){
        int ran = random.nextInt(3);
        int sets = checkSets(board);
        int size = board.size();
        if(System.nanoTime() - start > 10000000){
            return false;
        }
        if(size==12 && sets>=6){
            return true;
        }
        if(sets>=6){
            getUnconnected();
            return boardFill(start);
        }
        if((12-size)==(6-sets)){
            ran = 2;
        } 
        if((12-size)==(7-sets)&&(ran==0)){ //1,2,3
            ran = 1; 
        } 
        if((size==10)&&(ran==0)){ //1,2,3
            ran = 1; 
        }
        if((size==11)&&(ran<2)){ //2,3
            ran = ran+2;
        } 
        if((size<2)&&(ran==1 || ran==2)){ //0,3
            if (ran==1){
                ran = 0;
            } else {
                ran = 3;
            }
        }
        if((size<5)&&(ran==2)){
            ran = 3;
        }
        if(ran==0){
            generateSet();
            return boardFill(start);
        }
        if(ran==1){
            int a = random.nextInt(board.size()-1);
            generateSet(board.get(a));
            return boardFill(start);
        }
        if(ran==2){
            int a = random.nextInt(board.size()-1);
            int b = random.nextInt(board.size()-1);
            while(a==b || checkIfSet(board.get(a),board.get(b))){
                b = random.nextInt(board.size()-1);
            }
            getThird(board.get(a), board.get(b));
            return boardFill(start);
        }
        if(ran==3){
            getUnconnected();
            return boardFill(start);
        }
        return false;
    }
    
    /**
     * Calculates the number of valid sets given the current board 
     * Also updates the adjacency matrix based on the current sets
     */
    public int checkSets(List<Card> board){
        int setCount = 0;
        for(int i=0; i<board.size()-2;i++){
            for(int j=i+1; j<board.size()-1; j++){
                for (int k=j+1; k<board.size();k++){
                    if(guess.isValidSet(board.get(i),board.get(j),board.get(k))){
                        setCount++;
                        graph[graphPoint.get(board.get(i))][graphPoint.get(board.get(j))] = 1;
                        graph[graphPoint.get(board.get(i))][graphPoint.get(board.get(k))] = 1;
                        graph[graphPoint.get(board.get(j))][graphPoint.get(board.get(i))] = 1;
                        graph[graphPoint.get(board.get(j))][graphPoint.get(board.get(k))] = 1;
                        graph[graphPoint.get(board.get(k))][graphPoint.get(board.get(i))] = 1;
                        graph[graphPoint.get(board.get(k))][graphPoint.get(board.get(j))] = 1;
                    }
                }
            }
        }
        return setCount;
    
    }

    /**
     * Test method
     * Returns a list of sets
     */
    public List<List<Card>> findSets(List<Card> board){
        List<List<Card>> sets = new ArrayList<>();
        for(int i=0; i<board.size()-2;i++){
            for(int j=i+1; j<board.size()-1; j++){
                for (int k=j+1; k<board.size();k++){
                    if(guess.isValidSet(board.get(i),board.get(j),board.get(k))){
                        sets.add(List.of(board.get(i), board.get(j), board.get(k)));
                    }
                }
            }
        }
        return sets;
    
    }

    /**
     * Adds three random cards that make a set to the board
     */
    public void generateSet(){
        List<Integer> candidates = getUnconnectedId();
        Collections.shuffle(candidates, random);
        Card temp1 = new Card(candidates.get(0));
        board.add(temp1);
        graphPoint.put(temp1, iterate);
        iterate++;
        used[temp1.getId()] = true;
        candidates = getUnconnectedId();
        Collections.shuffle(candidates, random);
        Card temp2 = new Card(candidates.get(0));
        board.add(temp2);
        graphPoint.put(temp2, iterate);
        iterate++;
        used[temp2.getId()] = true;
        getThird(temp1, temp2);
    }

    /**
     * Adds the rest of a new set that contains the input card to the board
     */
    public void generateSet(Card card1){
        List<Integer> candidates = getUnconnectedId();
        Collections.shuffle(candidates, random);
        Card temp = new Card(candidates.get(0));
        getThird(card1, temp);
        board.add(temp);
        graphPoint.put(temp, iterate);
        iterate++;
        used[temp.getId()] = true;
    }

    /**
     * Adds the third card in a set with the two input cards to the board
     */
    public void getThird(Card card1, Card card2){
        String color;
        String shape;
        String fill;
        int num;
        if(card1.getColor().equals(card2.getColor())){
            color = card1.getColor();
        } else{
            color = card1.thirdColor(card2);
        }
        if(card1.getShape().equals(card2.getShape())){
            shape = card1.getShape();
        } else{
            shape = card1.thirdShape(card2);
        }
        if(card1.getFill().equals(card2.getFill())){
            fill = card1.getFill();
        } else{
            fill = card1.thirdFill(card2);
        }
        if(card1.getNumber()==card2.getNumber()){
            num = card1.getNumber();
        } else{
            num = 6-card1.getNumber()-card2.getNumber();
        }
        Card temp = new Card(shape, color, fill, num);
        board.add(temp);
        graphPoint.put(temp, iterate);
        iterate++;
        used[temp.getId()] = true;
    }

    /**
     * Adds a card ot tha board that is not in any sets with cards on the board
     */
    public void getUnconnected(){
        List<Integer> candidates = getUnconnectedId();
        Collections.shuffle(candidates, random);
        Card temp = new Card(candidates.get(0));
        board.add(temp);
        graphPoint.put(temp, iterate);
        iterate++;
        used[temp.getId()] = true;
    }

    /**
     * @return a list of Card Ids that are not on the board or in a sets with two cards on the board
     */
    public List<Integer> getUnconnectedId(){
        List<Integer> candidates = new ArrayList<>(); // build a list of all unused card IDs
        int[] connected = getConnected();
        for (int i = 0; i < 81; i++) {
            if ((!used[i])&&(connected[i]==0)) candidates.add(i);
        }
        return candidates;
    }

    /**
     * @return an array indicating double the number of sets not on the board a card completes 
     */
    public int[] getConnected(){
        int[] connected = new int[81];
        Arrays.fill(connected, 0);
        Iterator<Card> inter = board.iterator();
        while(inter.hasNext()){
            Card a = inter.next();
            Iterator<Card> interB = board.iterator();
            while(interB.hasNext()){
                Card b = interB.next();
                if(graph[graphPoint.get(a)][graphPoint.get(b)]==0){
                    connected[getThirdId(a,b)]=connected[getThirdId(a,b)]+1;
                }
            }
        }
        return connected;
    }

    /**
     * @return a boolean that is true if a card is in less than 2 sets with cards on tha board
     */
    public boolean checkConnections(Card c){
        int[] connected = getConnected();
        if(connected[c.getId()]>2){
            return false;
        }
        return true;
    }

    /**
     * @return the id of the third card in a set with the two input cards
     */
    public int getThirdId(Card card1, Card card2){
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
        Card temp = new Card(shape, color, fill, num);
        return temp.getId();
    }

    /**
     * @return if two cards are in a set that is on the board
     */
    public boolean checkIfSet(Card a, Card b){
        return graph[graphPoint.get(a)][graphPoint.get(b)]==1;
    }

    /**
     * Assigns the cards on the board positions and updates the Map
     */
    public void assignPositions(){
        List<Point> positions = generateGridPositions();
        Iterator<Point> pos = positions.iterator();
        for(Card temp: board){
            Point currentPosition = pos.next();
            temp.setPosition(currentPosition);
            currentCards.put(currentPosition, temp);
        }
    }

    /**
     * @return a list of possible positions cards can go into on the board
     */
    public List<Point> generateGridPositions() { 
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

    /**
     * Passes 3 cards selected in the guess class
     * Un-selects them if the guess is correct
     */
    public boolean processGuess(Card card1, Card card2, Card card3) {
        toggleCardSelection(card1);
        toggleCardSelection(card2);
        toggleCardSelection(card3);
        if (!guess.processGuess(card1, card2, card3)) {
            return false;
        }
        gameBoard.updateCorrectSets(card1, card2, card3);
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
}
