import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    // CHANGED add static arrays so constructors can reference reliably
    private static final String[] SHAPES = {"Oval", "Diamond", "Squiggle"};
    private static final String[] COLORS = {"Red", "Green", "Purple"};
    private static final String[] FILLS= {"Empty", "Solid", "Striped"};
    // List<String> shapes = List.of("Oval", "Diamond", "Squiggle"); 
    // List<String> colors = List.of("Red", "Green", "Purple"); 
    // List<String> fills = List.of("Empty", "Solid", "Striped");

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

    // CHANGED random card constructor
    public Card(){
        // random.nextInt(3) since there are 3 options (0,1,2)
        int r = random.nextInt(3);
        this.shape = SHAPES[r];
        r = random.nextInt(3);
        this.color = COLORS[r];
        r = random.nextInt(3);
        this.fill = FILLS[r];
        this.number = random.nextInt(3) + 1;
        this.graphic = new CardGraphics(shape, color, fill, number);
        this.isSelected = false;
    }

    //CHANGED generate card based on ID not randomly (so we can have more control without using a full deck)
    /**
     * Create a card from an id in [0,80] using an implicit graph / indexing scheme:
     *  id % 3 -> shape (0..2)
     *  (id / 3) % 3 -> color
     *  (id / 9) % 3 -> fill
     *  (id / 27) % 3 -> number index (0..2) -> +1
     */
    public Card(int id) {
        // assume id in [0,80]
        this.shape = SHAPES[id % 3];
        this.color = COLORS[(id / 3) % 3];
        this.fill = FILLS[(id / 9) % 3];
        this.number = ((id / 27) % 3) + 1;  // 1,2,3
        this.graphic = new CardGraphics(shape, color, fill, number);
        this.isSelected = false;
    }

    public String thirdShape(Card other){
        //List<String> tempShapes = new ArrayList<>(shapes);
        List<String> tempShapes = new ArrayList<>(List.of(SHAPES));
        tempShapes.remove(this.shape);
        tempShapes.remove(other.getShape());
        return tempShapes.get(0);
    }

    public String thirdColor(Card other){
        //List<String> tempColor = new ArrayList<>(colors);
        List<String> tempColor = new ArrayList<>(List.of(COLORS));
        tempColor.remove(this.color);
        tempColor.remove(other.getColor());
        return tempColor.get(0);
    }

    public String thirdFill(Card other){
        //List<String> tempFill = new ArrayList<>(fills);
        List<String> tempFill = new ArrayList<>(List.of(FILLS));
        tempFill.remove(this.fill);
        tempFill.remove(other.getFill());
        return tempFill.get(0);
    }

    //CHANGED use string equals instead of ==
    public boolean isEqual(Card other){
        // boolean shape = (this.shape==other.getShape()); 
        // boolean color = (this.color==other.getColor());
        // boolean fill = (this.fill==other.getFill());
        // boolean num = (this.number==other.getNumber());
        // return shape && color && fill && num;
        if (other == null) return false;
        boolean shapeEq = this.shape.equals(other.getShape());
        boolean colorEq = this.color.equals(other.getColor());
        boolean fillEq  = this.fill.equals(other.getFill());
        boolean numEq   = this.number.equals(other.getNumber());
        return shapeEq && colorEq && fillEq && numEq;
    }

    //CHANGED override equals/hashCode so collections work reliably:
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card other = (Card) o;
        return Objects.equals(shape, other.shape)
            && Objects.equals(color, other.color)
            && Objects.equals(fill, other.fill)
            && Objects.equals(number, other.number);
    }
    @Override
    public int hashCode() {
        return Objects.hash(shape, color, fill, number);
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
        System.out.println(test.getNumber());;
        //CHANGED add card id to main
        Card byId = new Card(35);
        System.out.println("id35 -> " + byId);
    }

    
}


