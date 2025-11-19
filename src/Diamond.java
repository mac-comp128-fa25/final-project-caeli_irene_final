import java.util.List;

import edu.macalester.graphics.Line;
import edu.macalester.graphics.Path;
import edu.macalester.graphics.Point;

/**
 * The Diamond Set Shape, extends SetPiece
 */
public class Diamond extends SetPiece{

    public Diamond(){
        super.shape = new Path(getPoints());
        add(super.shape);
    }
    
    /**
     * @return The points needed to create a diamond
     */
    private static List<Point> getPoints(){
        return List.of(new Point(25, 0), new Point(50, 50), new Point(25, 100), new Point(0, 50), new Point(25, 0));
    }
    
    public void setStriped(){
        super.setStroked();
        for (int b=3; b<25;b+=6){
            double x1 = 25 - b;
            double x2 = 25 + b;
            double y = 50 - 2*x1;
            Line tempLine = new Line(x1,y,x2,y);
            tempLine.setStrokeColor(super.color);
            tempLine.setStrokeWidth(2);
            add(tempLine);
        }
        for (int b=6; b<25;b+=6){
            double x1 = 25 - b;
            double x2 = 25 + b;
            double y = 50 + 2*x1;
            Line tempLine = new Line(x1,y,x2,y);
            tempLine.setStrokeColor(super.color);
            tempLine.setStrokeWidth(2);
            add(tempLine);
        }
    }
    
}
