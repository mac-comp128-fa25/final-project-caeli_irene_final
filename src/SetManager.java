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
 * Generate 12 random cards
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

    public SetManager(GameBoard gameBoard) { 
        this.gameBoard = gameBoard;
        this.selectedCards = new ArrayList<>();
        generateBoard();
        assignPositions();
        gameBoard.setUpCards(currentCards);
    }

    public List<Card> generateBoard(){
        board = new ArrayList<>();
        iterate = 0;
        Arrays.fill(used, false); // reset used cards so all 81 IDs are used

        boolean success = boardFill(); // find first slot at pos=0
        if (!success) { // if recusion fails throw an exception
            throw new RuntimeException("Failed to generate a valid board");
        }

        return new ArrayList<>(board);
    }

    private boolean boardFill(){
        int ran = random.nextInt(3);
        System.out.println("Random number: "+ ran);
        int sets = checkSets(board);
        int size = board.size();
        System.out.println("Sets: "+sets+" Size: "+size);
        if(size==12 && sets>=6){
            System.out.println("Hit final, actual sets:"+sets+" size:"+size);
            return true;
        }
        if(sets>=6){
            System.out.println("Hit full sets, actual sets:"+sets+" size:"+size);
            getUnconnected();
            return boardFill();
        }
        if((12-size)==(6-sets)){
            System.out.println("Hit only get third, actual sets:"+sets+" size:"+size);
            ran = 2;
        } 
        if((12-size)==(7-sets)){
            //1,2,3
            System.out.println("Hit everything but get set, actual sets:"+sets+" size:"+size);
            if(ran==0){
                ran = 1; //CHANGE
            }
        } 
        if(size==10){
            //1,2,3
            System.out.println("Hit 10, actual sets:"+sets+" size:"+size);
            if(ran==0){
                ran = 1; //CHANGE
            }
        }
        if(size==11){
            //2,3
            System.out.println("Hit 11, actual sets:"+sets+" size:"+size);
            if(ran<2){
                ran = ran+2;
            }
        } 
        if(size<2){
            // 0,3
            System.out.println("Hit too small, actual sets:"+sets+" size:"+size);
            if(ran==1 || ran==2){
                ran = 0; //CHANGE
            }
        }
        if(size<5){
            System.out.println("Hit almost too small, actual sets:"+sets+" size:"+size);
            if(ran==2){
                ran = 3;
            }
        }
        /** Size between 3 and  */
        if(ran==0){
            generateSet();
            System.out.println("Get set, plus 3");
            return boardFill();
        }
        if(ran==1){
            int a = random.nextInt(board.size()-1);
            generateSet(board.get(a));
            System.out.println("Get set, plus 2");
            return boardFill();
        }
        if(ran==2){
            int a = random.nextInt(board.size()-1);
            int b = random.nextInt(board.size()-1);
            while(a==b || checkIfSet(board.get(a),board.get(b))){
                b = random.nextInt(board.size()-1);
            }
            getThird(board.get(a), board.get(b));
            System.out.println("Get third");
            return boardFill();
        }
        if(ran==3){
            getUnconnected();
            System.out.println("Get unconnected");
            return boardFill();
        }
        return false;
    }
    
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

    public void generateSet(){
        Card[] tempCards = new Card[2];
        List<Integer> candidates = getUnconnectedId();
        Collections.shuffle(candidates, random);
        tempCards[0] = new Card(candidates.get(0));
        candidates = getUnconnectedId();
        Collections.shuffle(candidates, random);
        tempCards[1] = new Card(candidates.get(0));
        getThird(tempCards[0], tempCards[1]);
        for(Card temp:tempCards){
            board.add(temp);
            graphPoint.put(temp, iterate);
            iterate++;
            used[temp.getId()] = true;
        }
    }

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

    public void getThird(Card card1, Card card2){
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
        board.add(temp);
        graphPoint.put(temp, iterate);
        iterate++;
        used[temp.getId()] = true;
    }

    public void reset(){
        iterate = 0;
        graphPoint.clear();
        Arrays.fill(used, false);
        for(Card temp:board){
            graphPoint.put(temp, iterate);
            iterate++;
            used[temp.getId()] = true;
        }
    }

    public void getUnconnected(){
        List<Integer> candidates = getUnconnectedId();
        Collections.shuffle(candidates, random);
        Card temp = new Card(candidates.get(0));
        board.add(temp);
        graphPoint.put(temp, iterate);
        iterate++;
        used[temp.getId()] = true;
    }

    public List<Integer> getUnconnectedId(){
        List<Integer> candidates = new ArrayList<>(); // build a list of all unused card IDs
        int[] connected = getConnected();
        for (int i = 0; i < 81; i++) {
            if ((!used[i])&&(connected[i]==0)) candidates.add(i);
        }
        return candidates;
    }

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

    public boolean checkConnections(Card c){
        int[] connected = getConnected();
        if(connected[c.getId()]>2){
            return false;
        }
        return true;
    }

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

    public boolean checkIfSet(Card a, Card b){
        return graph[graphPoint.get(a)][graphPoint.get(b)]==1;
    }

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
     * UI
     * Places cards on the Gameboard object
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
     * UI
     * Passes 3 cards selected in the guess class
     * Remove and replace if the guess is correct
     */
    public boolean processGuess(Card card1, Card card2, Card card3) {
        if (!guess.isValidSet(card1, card2, card3)) {
            return false;
        }
        toggleCardSelection(card1);
        toggleCardSelection(card2);
        toggleCardSelection(card3);
        return true;
    }

    /**
     * UI
     * Passes 3 cards selected in the guess class
     * Remove and replace if the guess is correct
     */
    public boolean processGuess(List<Card> cards) {
        if (!guess.isValidSet(cards.get(0), cards.get(1), cards.get(2))) {
            return false;
        }
        for(Card temp:cards){
            toggleCardSelection(temp);
        }
        return true;
    }

    /*
     * UI
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
     * UI
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

        if(isValid){
            for(Card temp : selectedCards){
                toggleCardSelection(temp);
            }
        }

        clearSelection();
        return isValid;
    }

    /**
     * UI
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
    /**
     * UI
     */
    public List<Card> getSelectedCards() {
        return new ArrayList<>(selectedCards);
    }

    /**
     * UI
     */
    public List<Card> getBoard(){
        return new ArrayList<>(board);
    }
}
