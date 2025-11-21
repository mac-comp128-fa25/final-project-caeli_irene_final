import java.util.ArrayList;
import java.util.List;

/**
 * The Guess class is responsible for handling the logic of processing guesses in the game.
 * It validates if a set of three cards forms a valid set and keeps track of correct guesses.
 * The correct guesses are stored as lists of cards.
 */
public class Guess {
    private List<List<Card>> correctGuesses;

    /**
     * Constructor that accepts the current setof cards
     */
    public Guess (){
        correctGuesses=new ArrayList<>();
    }

    /**
     * Handles a guess attempt
     * @return true if valid set
     */
    public boolean processGuess(Card card1, Card card2, Card card3, Deck deck) {
        if (!isValidSet(card1, card2, card3)) {
            return false;
        }
        
        correctGuesses.add(List.of(card1,card2,card3));
        return true;
    }

    /**
     * Checks if the three cards form a valid set
     */
    public boolean isValidSet(Card card1, Card card2, Card card3) { //CHECK access modifier
        boolean colorCheck= checkAttribute(card1.getColor(), card2.getColor(), card3.getColor());
        boolean shapeCheck= checkAttribute(card1.getShape(), card2.getShape(), card3.getShape());
        boolean fillCheck= checkAttribute(card1.getFill(), card2.getFill(), card3.getFill());
        boolean numberCheck= checkAttribute(card1.getNumber(), card2.getNumber(), card3.getNumber());
        System.out.println(card1.toString()+card2.toString()+card3.toString());
        return colorCheck && shapeCheck && fillCheck && numberCheck;
    }

    /**
     * Checks whether the three given values are either all the same or all different.
     * This is used to validate each attribute (color, shape, fill, number) in a set.
     * Returns true if the values form a valid attribute set condition; false otherwise.
     */

    private boolean checkAttribute(Object first, Object second, Object third) {
        boolean allSame = first.equals(second) && second.equals(third);
        boolean allDifferent = !first.equals(second) && !first.equals(third) && !second.equals(third);

        return allSame || allDifferent;
    }

    /**
     * Returns a copy of all correctly identified sets (each set is 3 Cards).
     * Prevents modification of internal state by returning a new list.
     */
    public List<List<Card>> getCorrectGuesses() {
        return new ArrayList<>(correctGuesses);
    }
}