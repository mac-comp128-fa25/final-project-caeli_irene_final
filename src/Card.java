import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

/**
 * The Card class represents a single card in the Set game with attributes like shape, color, fill, and number.
 * It also includes methods for setting position, selecting the card, and managing its graphics representation.
 */
public class Card {
    private String shape;
    private String color; 
    private String fill; 
    private Integer number; 
    private CardGraphics graphic;
    private Point position; // this is the position in the HashMap
    private boolean isSelected; // whether the card is selected by the player

    /**
     * Constructs a new Card with the given attributes.
     * 
     * @param shape The shape of the card ("Oval", "Diamond", "Squiggle").
     * @param color The color of the card ("Red", "Green", "Purple").
     * @param fill The fill pattern of the card ("Solid", "Striped", "Empty").
     * @param number The number of shapes on the card (1, 2, or 3).
     */
    public Card(String shape, String color, String fill, int number){
        this.shape = shape;
        this.color = color;
        this.fill = fill;
        this.number = number;
        this.graphic = new CardGraphics(shape, color, fill, number);
        this.isSelected = false; // default not selected
    }

    public String getShape() {
        return shape;
    }

    public String getColor() {
        return color;
    }

    public String getFill() {
        return fill;
    }

    public Integer getNumber() {
        return number;
    }

    //returns a graphicsGroup
    public GraphicsGroup getGraphic() {
        return graphic.getGraphics();
    }

    //returns a card graphics
    public CardGraphics getCardGraphics() {
        return graphic;
    }
    
    public void setPosition(double x, double y){
        this.position = new Point(x,y);
    }

    public Point getPosition(){
        return position;
    }

    //Sets the selected state of the card and updates its graphic accordingly.
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected) {
            graphic.beenSelected(); //Update graphic for selected state
        } else {
            graphic.unSelect(); //Update graphic for unselected state
        }
    }

    public boolean isSelected() {
        return isSelected;
    }
    
}


