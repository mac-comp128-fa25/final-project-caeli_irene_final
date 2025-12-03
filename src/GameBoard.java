import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;


/**
 * Manages the visual layout and rendering of Set cards on the canvas.
 * Tracks card positions, detects clicks, and updates the display when
 * cards are added, removed, or replaced.
 */
public class GameBoard {
    /** Window where all cards are drawn. */
    private CanvasWindow canvas;

    /** Maps top-left board positions to the corresponding Card object. */
    public Map<Point, Card> cardPositions;

    /** Width of each element in pixels. */
    public static final int CARD_WIDTH = 250;
    public static final int CARD_HEIGHT = 120;
    public static final int PADDING = 20;
    public static final int CHANGE = 170;
    
    /**
     * Creates a new GameBoard responsible for placing and rendering cards.
     *
     * @param canvas The CanvasWindow where the board will be drawn.
     */
    public GameBoard(CanvasWindow canvas) {
        this.canvas = canvas;
        this.cardPositions = new HashMap<>();
    }

    
    /**
     * Clears the board and places a full set of cards onto the canvas.
     * Each entry in the map represents the position where a card should appear.
     *
     * @param cardsOnBoard A map from board positions to Card objects.
     */
    public void setUpCards(Map<Point, Card> cardsOnBoard) {
        clear();
        
        for (Map.Entry<Point, Card> entry : cardsOnBoard.entrySet()) {
            Point position = entry.getKey();
            Card card = entry.getValue();

            card.setPosition(position.getX()+CHANGE, position.getY()+CHANGE);
            canvas.add(card.getGraphic(), position.getX()+CHANGE, position.getY()+CHANGE);
        }
        
        this.cardPositions.putAll(cardsOnBoard);
    }

    /**
     * Updates part of the board by inserting new cards into existing positions,
     * then rerenders the full board.
     *
     * @param newCards Map of positions and cards to insert or replace.
     */
   public void updateBoard(Map<Point, Card> newCards) {
        cardPositions.putAll(newCards);
        renderAllCards();
    }

    /**
     * Returns the top-left position of the card containing the given click point.
     *
     * @param x The x coordinate of the mouse click.
     * @param y The y coordinate of the mouse click.
     * @return The Point representing the card's position, or null if none found.
     */
    public Point getCardPositionAt(int x, int y) {
        for (Point pos : cardPositions.keySet()) {
            if (x >= pos.getX()+CHANGE && x <= pos.getX() + CARD_WIDTH +CHANGE&&
                y >= pos.getY()+CHANGE && y <= pos.getY() + CARD_HEIGHT+CHANGE) {
                return pos;
            }
        }
        return null;
    }
    
    /**
     * Returns the Card object located at the given board position.
     *
     * @param pos1 The position to retrieve a card from.
     * @return The card at that position, or null if none exists.
     */
    public Card getCardAt(Point pos1) {
        return cardPositions.get(pos1);
    }


    /**
     * Clears and redraws all cards currently on the board.
     * Maintains each card's selection visual state.
     */
    private void renderAllCards() {
        canvas.removeAll();
        
        for (Map.Entry<Point, Card> entry : cardPositions.entrySet()) {
            Point pos = entry.getKey();
            Card card = entry.getValue();

            GraphicsGroup graphics = card.getGraphic();
            
            if (card.isSelected()) {
                card.setSelected(false);;
            } else {
                card.setSelected(true);
            }
            
            graphics.setPosition(pos.getX(), pos.getY());
            canvas.add(graphics);
        }
    }

    /**
     * Removes all card graphics from the canvas and clears the board map.
     */
    public void clear() {
        for (Card card : cardPositions.values()) {
            canvas.remove(card.getGraphic());
        }
        cardPositions.clear();
    }
    /**
     * Returns a collection of all cards currently displayed on the board.
     *
     * @return A collection of Card objects.
     */
    public Collection<Card> getCurrentCards() {
        return cardPositions.values();
    }


}
