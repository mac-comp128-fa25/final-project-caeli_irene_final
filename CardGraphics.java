import java.util.ArrayList;
import java.awt.Color;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Rectangle;

/**
 * Controls all the graphics of each card
 */
public class CardGraphics {
    private static final Color GREEN = new Color(0,200,0);
    private static final Color RED = new Color(220,0,0);
    private static final Color PURPLE = new Color(100,0,100);

    private GraphicsGroup graphics; 
    private ArrayList<SetPiece> shapes = new ArrayList<SetPiece>();
    private Rectangle card;

    public CardGraphics(String shape, String color, String fill, int number){
        setUp();
        for (int i=0; i<number; i++){
            chooseShape(shape);
        }
        setPlaces(number);
        setColor(color);
        setFill(fill);
    }

    /**
     * Creates the background of the card
     */
    private void setUp(){
        graphics = new GraphicsGroup();
        card = new Rectangle(0,0,250, 120);
        graphics.add(card);
    }

    /**
     * Selects the shape the card is using 
     */
    private void chooseShape(String shape){
        if (shape.equals("Oval")){
            shapes.add(new Oval());
        }
        if (shape.equals("Diamond")){
            shapes.add(new Diamond());
        }
        if (shape.equals("Squiggle")){
            shapes.add(new Squiggle());
        }
    }

    /**
     * Adds the selected number of shapes to the card 
     */
    private void setPlaces(int number){
        if (number==1){
            graphics.add(shapes.get(0),100,10);
        }
        if (number==2){
            graphics.add(shapes.get(0), 70, 10);
            graphics.add(shapes.get(1), 130, 10);
        }
        if (number==3){
            graphics.add(shapes.get(0),40, 10);
            graphics.add(shapes.get(1),100,10);
            graphics.add(shapes.get(2),160,10);
        }
    }

    /**
     * Sets the color of each shape
     */
    private void setColor(String color){
        for (SetPiece piece : shapes){
            if (color.equals("Red")){
                piece.setColor(RED);
            }
            if (color.equals("Green")){
                piece.setColor(GREEN);
            }
            if (color.equals("Purple")){
                piece.setColor(PURPLE);
            }
        }
    }

    /**
     * Sets the fill of each shape
     */
    private void setFill(String fill){
        for (SetPiece piece : shapes){
            if (fill.equals("Empty")){
                piece.setStroked();
            }
            if (fill.equals("Solid")){
                piece.setFilled();
            }
            if (fill.equals("Striped")){
                piece.setStriped();
            }
        }
    }

    /**
     * Shows selected card graphics
     */
    public void beenSelected(){
        card.setStrokeWidth(3);
        card.setStrokeColor(new Color(0,0,200));
    }

    /**
     * Reverts selected card to normal
     */
    public void unSelect(){
        card.setStrokeWidth(1);
        card.setStrokeColor(new Color(0,0,0));
    }

    public GraphicsGroup getGraphics(){
        return graphics;
    }

}
