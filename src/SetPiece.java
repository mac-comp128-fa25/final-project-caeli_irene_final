import java.awt.Color;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Path;

/**
 * Abstract class that creates common methods for the different fills of shapes
 */
public abstract class SetPiece extends GraphicsGroup{
    Path shape;
    Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStroked() {  
        shape.setStroked(true);    
        shape.setStrokeColor(color);  
        shape.setStrokeWidth(2);
    }

    public void setFilled() {
        shape.setStroked(false);
        shape.setFilled(true);
        shape.setFillColor(color);
    } 

    public abstract void setStriped();
    
}
