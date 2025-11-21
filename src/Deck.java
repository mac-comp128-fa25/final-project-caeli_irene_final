import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates and manages a full deck of set cards
 */
public class Deck {
    private List<List<String>> cards = new ArrayList<>();
    private Random random = new Random();

    public Deck(){
        createDeck();
    }

    /**
     * @return a random new card that is not already on the board
     */
    public List<String> getNextCard(){
        int i = random.nextInt(cards.size());
        List<String> card = cards.get(i);
        cards.remove(i);
        return card;
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
                    for (Integer number=1;number<4;number++){
                        cards.add(List.of(shapes.get(a),colors.get(b), fills.get(c), number.toString()));
                    }
                }
            }
        }
    }

    public void removeCard(List<String> card){
        cards.remove(card);
    }

}
