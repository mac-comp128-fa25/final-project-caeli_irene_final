import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.macalester.graphics.Point;
import java.util.Random;
import java.util.Set;
import java.util.Iterator;

public class SetManager {

    private final Map<Point, Card> currentCards = new HashMap<>();
    private ArrayList<Card> selectedCards; 
    private GameBoard gameBoard;
    private static Guess guess = new Guess();
    //private Deck cards = new Deck(); stopped using a deck
    //private List<List<String>> sets = new ArrayList<>(); stopped tracking sets -> used in checkSets
    public static List<Card> board;
    private static int[][] graph = new int[12][12];
    private static Map<Card, Integer> graphPoint = new HashMap<>();
    private static int iterate = 0;
    private static Random random = new Random();
    private static final boolean[] used = new boolean[81]; //CHANGED add used card
    //Visualizer viz = null; // CHANGED to feed data into vizualizer, initialized in the
    

    public SetManager(GameBoard gameBoard) { 
        this.gameBoard = gameBoard;
        this.selectedCards = new ArrayList<>();
    }

    //CHANGED VIZUALIZER
    // public void setVisualizer(Visualizer viz) {
    //     this.viz = viz;
    // }

    public static List<Card> generateBoard(){
        
        board = new ArrayList<>();
        Arrays.fill(used, false); // reset used cards so all 81 IDs are used

        boolean success = boardFill(); // find first slot at pos=0
        if (!success) { // if recusion fails throw an exception
            throw new RuntimeException("Failed to generate a valid board");
        }

        return new ArrayList<>(board);


        //ORIGINAL
        // int set = 0;
        // int stop = 0;
        // addCard(board);
        // addCard(board);
        // addCard(board);
        // addCard(board);
        // while(set<6 && stop < 100){
        //     if(board.size()>12){
        //         board.remove(random.nextInt(11));
        //         addSet(board);
        //     }
        //     int ran = random.nextInt(10);
        //     if(ran<3){
        //         addCard(board);
        //     } else{
        //         addSet(board);
        //     }
        //     set = checkSets(board);
        //     stop++;
        // }
        // stop = 0;
        // while(board.size()<12 && stop < 100){
        //     List<String> card = cards.getNextCard();
        //     board.add(new Card(card.get(0), card.get(1), card.get(2), Integer.valueOf(card.get(3))));
        //     set = checkSets(board);
        //     if(set > 6){
        //         board.remove(board.size()-1);
        //     }
        //     stop++;
        // }
        // return board;
    }

    /**
     * Recursive function to fill the board
     * pos: current position in the board (0-11)
     */
    private boolean fillBoard(int pos) {
        /**
         * RECURSIVE
         * Base case: pos == boardSize. The board is full, we check if it has 6 valid sets
         * if yes -> success!
         * if no -> go back a step and try again
         *  create an array list of possible unused cards
         *  Collections.shuffle them randomly
         *  go through each card and place it on the board
         *  check number of sets currently on board
         *      less than 6 sets
         *          call fill board again to add an extra card
         *      more than 6 sets
         *          remove card we added
         *  */ 
        if (pos == 15) { //checks board size
            // Board full, must have exactly 6 sets
            return checkSets(board) == 6;
        }

        List<Integer> candidates = new ArrayList<>(); // build a list of all unused card IDs
        for (int i = 0; i < 81; i++) {
            if (!used[i]) candidates.add(i);
        }
        Collections.shuffle(candidates, random); // shuffle unused card ids -> randomness

        for (int id : candidates) { // try each candidate card
            Card c = new Card(id); //convert id to card object
            board.add(c); // place it on board
            used[id] = true; // mark it as used
            int setsNow = checkSets(board); // how many sets currently present
            if (setsNow <= 6) { // don't allow more than 6 sets
                if (fillBoard(pos + 1)) { // call fill board again to add an extra card
                    return true; // solution found
                }
            }
            // if we have more than 6 sets -> backtrack
            board.remove(board.size() - 1);
            used[id] = false;
        }

        return false; // this path leads to no valid 6 set arrangements -> throws an exception in generate board
    }

    // //CHANGED replaced by fillBoard
    // add get next valid card to handle edge cases and make sure we never have null cards
    // private Card getNextValidCard() {
    //     // Try random IDs first
    //     int tries = 0;
    //     while (tries < 81) {
    //         int id = random.nextInt(81);
    //         if (!used[id]) {
    //             used[id] = true;
    //             return new Card(id);
    //         }
    //         tries++;
    //     }
    //     // if that takes too long -> go through all IDs
    //     for (int i = 0; i < 81; i++) {
    //         if (!used[i]) {
    //             used[i] = true;
    //             return new Card(i);
    //         }
    //     }
    //     // Absolute fallback (should never happen)
    //     return new Card(0);
    // }

    //CHANGED add set method -> with recursion we don't need it
    // private void addSet(List<Card> board){
    //     int ran1 = random.nextInt(board.size()-1);
    //     int ran2 = random.nextInt(board.size()-1);
    //     while(ran2==ran1){
    //         ran2 = random.nextInt(board.size()-1);
    //     }
    //     Card test = getThird(board.get(ran1),board.get(ran2));
    //     if(!checkCard(board, test)){
    //         board.add(test);
    //         cards.removeCard(List.of(test.getShape(), test.getColor(), test.getFill(), test.getNumber().toString()));
    //     } 
    // }
    // private void generateSet(List<Card> board){
    //     int i = random.nextInt(board.size());
    //     int j = random.nextInt(board.size());
    //     while(i == j) j = random.nextInt(board.size());

    //     Card c1 = board.get(i);
    //     Card c2 = board.get(j);

    //     Card c3 = getThird(c1, c2);

    //     // compute the ID of c3 so we prevent duplicates
    //     int id = getCardID(c3);   // You will add this helper below

    //     if(!used[id]) {
    //         used[id] = true;
    //         board.add(new Card(id));
    //     }

    //     if (board.size() >= 15) return;  // CHANGED 12 to 15 cards

    //     if (board.size() < 2) return;

    //     Card c1 = board.get(random.nextInt(board.size()));
    //     Card c2 = board.get(random.nextInt(board.size()));
    //     while (c1.equals(c2)) {
    //         c2 = board.get(random.nextInt(board.size()));
    //     }

    //     Card c3 = getThird(c1, c2);

    //     if (c3 == null || used[c3.getId()]) {
    //         c3 = getNextValidCard();
    //     } else {
    //         used[getCardID(c3)] = true;
    //     }

    //     board.add(c3);
    // }


    //CHANGED add card method -> replaced by fillBoard
    // private void addCard(List<Card> board){
    //     Card test = new Card();
    //     if(!checkCard(board, test)){
    //         board.add(test);
    //         cards.removeCard(List.of(test.getShape(), test.getColor(), test.getFill(), test.getNumber().toString()));
    //     }
    // }
    // private void addCard(List<Card> board){
    //     // // choose an unused id
    //     // int id = random.nextInt(81);
    //     // while(used[id]) {
    //     //     id = random.nextInt(81);
    //     // }
    //     // used[id] = true;
    //     // board.add(new Card(id));
        
    //     board.add(getNextValidCard());
    // }

    // addCard, addSet helper -> replaced by fill board
    // private boolean checkCard(List<Card> board, Card test){
    //     Iterator<Card> iter = board.iterator();
    //     while(iter.hasNext()){
    //         Card old = iter.next();
    //         if(old.isEqual(test)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    private static boolean boardFill(){
        int ran = random.nextInt(3);
        int sets = checkSets(board);
        int size = board.size();
        if(size==12 && sets==6){
            System.out.println("Hit final, actual sets:"+sets+" size:"+size);
            return true;
        }
        if(sets==6){
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
                ran = 3; //CHANGE
            }
        }
        if(ran==0){
            generateSet();
            return boardFill();
        }
        if(ran==1){
            int a = random.nextInt(board.size()-1);
            generateSet(board.get(a));
            return boardFill();
        }
        if(ran==2){
            int a = random.nextInt(board.size()-1);
            int b = random.nextInt(board.size()-1);
            while(a==b || checkIfSet(board.get(a),board.get(b))){
                b = random.nextInt(board.size()-1);
            }
            getThird(board.get(a), board.get(b));
            return boardFill();
        }
        if(ran==3){
            getUnconnected();
            return boardFill();
        }
        return false;
    }
    
    public static int checkSets(List<Card> board){
        int setCount = 0;
        System.out.println(board.size());
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

    public static void generateSet(){
        Card[] tempCards = new Card[2];
        tempCards[0] = new Card();
        tempCards[1] = new Card();
        getThird(tempCards[0], tempCards[1]);
        for(Card temp:tempCards){
            board.add(temp);
            graphPoint.put(temp, iterate);
            iterate++;
            used[temp.getId()] = true;
        }
    }

    public static void generateSet(Card card1){
        Card temp = new Card();
        getThird(card1, temp);
        board.add(temp);
        graphPoint.put(temp, iterate);
        iterate++;
        used[temp.getId()] = true;
    }

    public static void getThird(Card card1, Card card2){
        System.out.println("Get third");
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
        if(checkConnections(temp)){
            board.add(temp);
            graphPoint.put(temp, iterate);
            iterate++;
            used[temp.getId()] = true;
        } else{
            System.out.println("Removed");
            for(int i=0; i<3;i++){
                int ran = random.nextInt(board.size()-1);
                board.remove(ran);
            }
            reset();
        }
    }

    public static void reset(){
        iterate = 0;
        graphPoint.clear();
        Arrays.fill(used, false);
        for(Card temp:board){
            graphPoint.put(temp, iterate);
            iterate++;
            used[temp.getId()] = true;
        }
    }

    public static void getUnconnected(){
        List<Integer> candidates = new ArrayList<>(); // build a list of all unused card IDs
        int[] connected = getConnected();
        for (int i = 0; i < 81; i++) {
            if ((!used[i])&&(connected[i]==0)) candidates.add(i);
        }
        Collections.shuffle(candidates, random);
        Card temp = new Card(candidates.get(0));
        board.add(temp);
        graphPoint.put(temp, iterate);
        iterate++;
        used[temp.getId()] = true;
    }

    public static int[] getConnected(){
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

    public static boolean checkConnections(Card c){
        int[] connected = getConnected();
        if(connected[c.getId()]>1){
            return false;
        }
        return true;
    }

    public static int getThirdId(Card card1, Card card2){
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

    public static boolean checkIfSet(Card a, Card b){
        return graph[graphPoint.get(a)][graphPoint.get(b)]==1;
    }

    /**
     * UI
     * Places cards on the Gameboard object
     */
    public List<Point> generateGridPositions() { //CHANGED made public so vizualizer can access it
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

        Point pos1 = card1.getPosition();
        Point pos2 = card2.getPosition();
        Point pos3 = card3.getPosition();
    
        if (pos1 != null) currentCards.remove(pos1);
        if (pos2 != null) currentCards.remove(pos2);
        if (pos3 != null) currentCards.remove(pos3);

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

        // Still need to be added

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

    
    // public List<Integer> generateBoardIDsForViz() {
    //     List<Integer> result = new ArrayList<>();
    //     boolean[] usedLocal = new boolean[81];
    //     backtrackViz(result, usedLocal);
    //     return result;
    // }

    


    // private boolean backtrackViz(List<Integer> result, boolean[] usedLocal) {
    //     // target size 12 (standard Set board)
    //     if (result.size() == 15) return true;

    //     for (int c = 0; c < 81; c++) {
    //         if (usedLocal[c]) continue;

    //         usedLocal[c] = true;
    //         result.add(c);

    //         // VISUALIZE: prefer passing Card object if you have one; otherwise pass string
    //         if (viz != null) {
    //             // create a temporary Card to show full toString() if needed, otherwise pass id string
    //             try {
    //                 Card tmp = new Card(c);           // you already have Card(int) in code
    //                 viz.onAddCard(tmp);              // Visualizer has onAddCard(Card)
    //             } catch (Exception e) {
    //                 viz.onAddCard(String.valueOf(c));
    //             }
    //         }

    //         if (backtrackViz(result, usedLocal)) return true;

    //         // BACKTRACK
    //         result.remove(result.size() - 1);
    //         usedLocal[c] = false;

    //         if (viz != null) {
    //             try {
    //                 Card tmp = new Card(c);
    //                 viz.onRemoveCard(tmp);
    //             } catch (Exception e) {
    //                 viz.onRemoveCard(String.valueOf(c));
    //             }
    //         }
    //     }
    //     return false;
    // }

    public static void main(String[] args){
        // Card card1 = new Card();
        // Card card2 = new Card();
        // Card card3 = getThird(card1, card2);
        // for(Card test : List.of(card1, card2, card3)){
        //     System.out.println(test.getShape());
        //     System.out.println(test.getColor());
        //     System.out.println(test.getFill());
        //     System.out.println(test.getNumber());
        // }

        // for testing
        List<Card> board = generateBoard();
        Arrays.fill(used, false);
        for(Card test : board){
            System.out.println(test.getShape()+" "+test.getColor()+" "+test.getFill()+" "+test.getNumber());
        }   
    }
}
