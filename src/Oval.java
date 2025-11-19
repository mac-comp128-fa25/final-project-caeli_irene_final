import java.util.List;
import java.util.ArrayList;

import edu.macalester.graphics.Point;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Path;

/**
 * The Oval Set Shape, extends SetPiece
 */
public class Oval extends SetPiece{

    public Oval(){
        super.shape = new Path(getPoints());
        add(super.shape);
    }

    /**
     * @return the points needed to create an oval
     */
    private static List<Point> getPoints(){
        List<Point> points = new ArrayList<Point>();
        for(double a=0;a<6.29;a+=0.01){
            double x = 25 + 25*Math.cos(a);
            double y = 50 - 50*Math.sin(a);
            points.add(new Point(x,y));
        }
        return points;
    }

    public void setStriped(){
        super.setStroked();
        for (double a=0.3925;a<3.15;a+=0.3925){
            double x1 = 25 + 25*Math.cos(1.57+a);
            double x2 = 25 + 25*Math.cos(1.57-a);
            double y1 = 50 - 50*Math.sin(1.57+a);
            double y2 = 50 - 50*Math.sin(1.57-a);
            Line tempLine = new Line(x1,y1,x2,y2);
            tempLine.setStrokeColor(super.color);
            tempLine.setStrokeWidth(2);
            add(tempLine);
        }
    }

}
