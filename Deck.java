import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates and manages a full deck of set cards
 */
public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>();
    private Random random = new Random();
    private ArrayList<Integer> usedCards = new ArrayList<>();

    public Deck(){
        createDeck();
    }

    /**
     * @return a random new card that is not already on the board
     */
    public Card getNextCard(){
        int i = random.nextInt(81);
        while (usedCards.contains(i)){
            i = random.nextInt(81);
        }
        usedCards.add(i);
        return cards.get(i);
    }

    /**
     * Clears the internal list of cards on board
     */
    public void clearList(){
        usedCards.clear();
    }

    /**
     * Creates a full deck of set cards
     */
    private void createDeck(){
        List<String> shapes = List.of("Oval", "Diamond", "Squiggle");
        List<String> colors = List.of("Red", "Green", "Purple");
        List<String> fills = List.of("Empty", "Solid", "Striped");
        for (int a=0;a<3;a++){
            for (int b=0;b<3;b++){
                for (int c=0;c<3;c++){
                    for (int number=1;number<4;number++){
                        cards.add(new Card(shapes.get(a), colors.get(b), fills.get(c), number));
                    }
                }
            }
        }
    }

    /**
     * @return the current size of internal list of cards on board
     */
    public int getUsedCardSize(){
        return usedCards.size();
    }

    /**
     * adds majority of cards to internal list to allow easier testing of end of deck
     */
    public void testEdgeCase(){
        for (int i=0;i<68;i++){
            usedCards.add(i);
        }
    }

}
