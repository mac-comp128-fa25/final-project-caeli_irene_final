import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

public class GameBoard {
    private CanvasWindow canvas;
    public Map<Point, Card> cardPositions;

    public static final int CARD_WIDTH = 250;
    public static final int CARD_HEIGHT = 120;
    public static final int PADDING = 20;
    public static final int CHANGE = 170;
    
    public GameBoard(CanvasWindow canvas) {
        this.canvas = canvas;
        this.cardPositions = new HashMap<>();
    }

    
    /**
     * Initial full board setup
     */
    public void setUpCards(Map<Point, Card> cardsOnBoard) {
        clear();
        
        for (Map.Entry<Point, Card> entry : cardsOnBoard.entrySet()) {
            Point position = entry.getKey();
            Card card = entry.getValue();
            
            card.setSelected(false);

            // Set and add to canvas
            card.setPosition(position.getX()+CHANGE, position.getY()+CHANGE);
            canvas.add(card.getGraphic(), position.getX()+CHANGE, position.getY()+CHANGE);
        }
        
        this.cardPositions.putAll(cardsOnBoard);
    }

    /**
     * Partial update after guesses
     */
   public void updateBoard(Map<Point, Card> newCards) {
        cardPositions.putAll(newCards);
        renderAllCards();
    }

    /**
     * Returns position if clicked point contains a card
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
    
    /*
     * Find position of a card given a point
     */
    public Card getCardAt(Point pos1) {
        return cardPositions.get(pos1);
    }


    /*
     * Places cards from Map to canvas
     * Mainains selection state during redraws
     * Adds position to the canvas
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

    /*
     * Removes graphic object from each position of the Gameboard
     */
    public void clear() {
        for (Card card : cardPositions.values()) {
            canvas.remove(card.getGraphic());
        }
        cardPositions.clear();
    }

    public Collection<Card> getCurrentCards() {
        return cardPositions.values();
    }


}
