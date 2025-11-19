import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GuessTests {
    private Guess guess = new Guess();

    // Helper method to create cards with specific attributes
    private static Card createCard(String shape, String color, String fill, int number) {
        return new Card(shape, color, fill, number);
    }


// VALID SET TESTS:
    //All same
    @Test
    public void isValidSet_returnsTrue_whenAllAttributesAreSame() {
        Card card1 = createCard("Oval", "Red", "Solid", 1);
        Card card2 = createCard("Oval", "Red", "Solid", 1);
        Card card3 = createCard("Oval", "Red", "Solid", 1);
        assertTrue(guess.isValidSet(card1, card2, card3));
    }

    //All different
    @Test
    public void isValidSet_returnsTrue_whenAllAttributesAreDifferent() {
        Card card1 = createCard("Oval", "Red", "Solid", 1);
        Card card2 = createCard("Diamond", "Green", "Striped", 2);
        Card card3 = createCard("Squiggle", "Purple", "Empty", 3);
        assertTrue(guess.isValidSet(card1, card2, card3));
    }

    //Checks all valid cases
    @ParameterizedTest //Run the test multiple times with different inputs
    @MethodSource("validSetProvider")  //Use the invalidSetProvider() method to supply arguments for this test.
    public void isValidSet_returnsTrue_forValidCombinations(Card card1, Card card2, Card card3) {
        assertTrue(guess.isValidSet(card1, card2, card3));
    }
 
    private static Stream<Arguments> validSetProvider() {
        return Stream.of(
            //All attributes the same
            Arguments.of(
            createCard("Oval", "Red", "Solid", 1),
            createCard("Oval", "Red", "Solid", 1),
            createCard("Oval", "Red", "Solid", 1)
            ),

            //All attributes different
            Arguments.of(
                createCard("Oval", "Red", "Solid", 1),
                createCard("Diamond", "Green", "Striped", 2),
                createCard("Squiggle", "Purple", "Empty", 3)
            ),

            //Only color is the same; other attributes are all different
            Arguments.of(
                createCard("Oval", "Red", "Solid", 1),
                createCard("Diamond", "Red", "Striped", 2),
                createCard("Squiggle", "Red", "Empty", 3)
            ),

            //Only shape is the same; rest different
            Arguments.of(
                createCard("Oval", "Red", "Solid", 1),
                createCard("Oval", "Green", "Striped", 2),
                createCard("Oval", "Purple", "Empty", 3)
            ),

            //Two attributes same, two different
            Arguments.of(
            createCard("Oval", "Red", "Solid", 1),
            createCard("Oval", "Green", "Striped", 1),
            createCard("Oval", "Purple", "Empty", 1)
            )
        );
    }


//INVALID SET TESTS
    // Checks all invalid cases
    @ParameterizedTest 
    @MethodSource("invalidSetProvider")
    public void isValidSet_returnsFalse_forInvalidCombinations(Card card1, Card card2, Card card3) {
        assertFalse(guess.isValidSet(card1, card2, card3));
    }

    private static Stream<Arguments> invalidSetProvider() {
        return Stream.of(
            //Invalid color
            Arguments.of(
                createCard("Oval", "Red", "Solid", 1),
                createCard("Diamond", "Red", "Solid", 1),
                createCard("Squiggle", "Green", "Solid", 1)
            ),
            
            //Invalid shape
            Arguments.of(
                createCard("Oval", "Red", "Solid", 1),
                createCard("Oval", "Green", "Solid", 1),
                createCard("Diamond", "Purple", "Solid", 1)
            ),

            //Invalid fill
            Arguments.of(
                createCard("Oval", "Red", "Solid", 1),
                createCard("Diamond", "Green", "Solid", 2),
                createCard("Squiggle", "Purple", "Striped", 3)
            ),

            // Invalid number
            Arguments.of(
                createCard("Oval", "Red", "Solid", 1),
                createCard("Diamond", "Green", "Striped", 1),
                createCard("Squiggle", "Purple", "Empty", 2)
            )
        );
    }

    
//PROCESS GUESS TESTS
    //Tests that processGuess correctly returns true and adds the valid set to the correct guesses list.
    @Test
    public void processGuess_returnsTrueAndAddsToCorrectGuesses_whenValidSet() {
        Deck deck = new Deck();
        Card card1 = createCard("Oval", "Red", "Solid", 1);
        Card card2 = createCard("Oval", "Green", "Striped", 1);
        Card card3 = createCard("Oval", "Purple", "Empty", 1);
        
        assertTrue(guess.processGuess(card1, card2, card3, deck));
        assertEquals(1, guess.getCorrectGuesses().size());
        assertEquals(3, guess.getCorrectGuesses().get(0).size());
    }

    //Tests that processGuess correctly returns false and does not add an invalid set to the correct guesses list.
    @Test
    public void processGuess_returnsFalse_whenInvalidSet() {
        Deck deck = new Deck();
        Card card1 = createCard("Oval", "Red", "Solid", 1);
        Card card2 = createCard("Oval", "Red", "Striped", 1);
        Card card3 = createCard("Oval", "Purple", "Empty", 1);
        
        assertFalse(guess.processGuess(card1, card2, card3, deck));
        assertEquals(0, guess.getCorrectGuesses().size()); // Ensures the invalid set is not added
    }
}
