import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private Random random = new Random();
    List<String> shapes = List.of("Oval", "Diamond", "Squiggle");
    List<String> colors = List.of("Red", "Green", "Purple");
    List<String> fills = List.of("Empty", "Solid", "Striped");

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

    public Card(){
        int r = random.nextInt(2);
        this.shape = shapes.get(r);
        r = random.nextInt(3);
        this.color = colors.get(r);
        r = random.nextInt(3);
        this.fill = fills.get(r);
        this.number = r+1;
    }

    public String thirdShape(Card other){
        List<String> tempShapes = new ArrayList<>(shapes);
        tempShapes.remove(this.shape);
        tempShapes.remove(other.getShape());
        return tempShapes.get(0);
    }

    public String thirdColor(Card other){
        List<String> tempColor = new ArrayList<>(colors);
        tempColor.remove(this.color);
        tempColor.remove(other.getColor());
        return tempColor.get(0);
    }

    public String thirdFill(Card other){
        List<String> tempFill = new ArrayList<>(fills);
        tempFill.remove(this.fill);
        tempFill.remove(other.getFill());
        return tempFill.get(0);
    }

    public boolean isEqual(Card other){
        boolean shape = (this.shape==other.getShape());
        boolean color = (this.color==other.getColor());
        boolean fill = (this.fill==other.getFill());
        boolean num = (this.number==other.getNumber());
        return shape && color && fill && num;
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

    public String toString(){
        return "["+shape+" "+fill+" "+color+" "+number+"]";
    }

    public static void main(String arg[]){
        Card test = new Card();
        System.out.println(test.getShape());
        System.out.println(test.getColor());
        System.out.println(test.getFill());
        System.out.println(test.getNumber());
    }
    
}


