import java.util.ArrayList;
import java.util.List;

/**
 * The Guess class handles the logic for evaluating guesses in the Game of Set.
 * It verifies whether three selected cards form a valid set and stores all correctly
 * identified sets for later reference.
 */
public class Guess {

    /** A list of all correct guesses, each represented as a list of three cards. */
    private List<List<Card>> correctGuesses;

    /**
     * Constructs a new Guess object with an empty list of correct guesses.
     */
    public Guess (){
        correctGuesses=new ArrayList<>();
    }

    /**
     * Processes a guess containing three cards.
     *
     * @param card1 the first selected card
     * @param card2 the second selected card
     * @param card3 the third selected card
     * @return true if the three cards form a valid set,
     *         false otherwise
     */
    public boolean processGuess(Card card1, Card card2, Card card3) {
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