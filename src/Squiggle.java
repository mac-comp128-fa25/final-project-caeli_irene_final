import java.util.ArrayList;
import java.util.List;

import edu.macalester.graphics.Line;
import edu.macalester.graphics.Path;
import edu.macalester.graphics.Point;

/**
 * The Squiggle Set Shape, extends SetPiece
 */
public class Squiggle extends SetPiece{

    public Squiggle(){
        super.shape = new Path(getPoints());
        add(super.shape);
    }

    /**
     * @return the points needed to create the Squiggle shape
     */
    private static List<Point> getPoints(){
        List<Point> points = new ArrayList<Point>();
        for (double a=0;a<4.72;a+=0.01){
            double x = 7 + 7*Math.sin(a);
            double y = 20 + 12.1*a;
            points.add(new Point(x,y));
        }
        for (double b=3.14;b<4.72;b+=0.01){
            double x = 23 + 23*Math.cos(b);
            double y = 77 - 23*Math.sin(b);
            points.add(new Point(x,y));
        }
        for (double c=4.71;c<6.29;c+=0.01){
            double x = 23 + 20*Math.cos(c);
            double y = 80 - 20*Math.sin(c);
            points.add(new Point(x,y));
        }
        for (double d=6.28;d>1.56;d-=0.01){
            double x = 43 + 7*Math.sin(d);
            double y = 80 - 12.1*(6.28-d);
            points.add(new Point(x,y));
        }
        for (double e=0;e<1.58;e+=0.01){
            double x = 27 + 23*Math.cos(e);
            double y = 23 - 23*Math.sin(e);
            points.add(new Point(x,y));
        }
        for (double f=1.57;f<3.15;f+=0.01){
            double x = 27 + 20*Math.cos(f);
            double y = 20 - 20*Math.sin(f);
            points.add(new Point(x,y));
        }
        return points;
    }

    public void setStriped(){
        super.setStroked();
        List<Line> lineList = new ArrayList<>();
        lineList.add(new Line(27+20*Math.cos(2.68), 20-20*Math.sin(2.68), 27+23*Math.cos(0.54),23-23*Math.sin(0.54)));
        lineList.add(new Line(7+7*Math.sin(0.18),20+(12.1*0.18),27+23*Math.cos(0.035),23-23*Math.sin(0.035)));
        lineList.add(new Line(7+7*Math.sin(1.1),20+(12.1*1.1),43+7*Math.sin(2.42),80-12.1*(6.28-2.42)));
        lineList.add(new Line(7+7*Math.sin(2.02),20+(12.1*2.02),43+7*Math.sin(3.34),80-12.1*(6.28-3.34)));
        lineList.add(new Line(7+7*Math.sin(2.93),20+(12.1*2.93),43+7*Math.sin(4.26),80-12.1*(6.28-4.26)));
        lineList.add(new Line(7+7*Math.sin(3.85),20+(12.1*3.85),43+7*Math.sin(5.17),80-12.1*(6.28-5.17)));
        lineList.add(new Line(23+23*Math.cos(3.17),77-23*Math.sin(3.17),43+7*Math.sin(6.09),80-12.1*(6.28-6.09)));
        lineList.add(new Line(23+23*Math.cos(3.68),77-23*Math.sin(3.68),23+20*Math.cos(-0.46),80-20*Math.sin(-0.46)));
        for (Line tempLine : lineList){
            tempLine.setStrokeColor(super.color);
            tempLine.setStrokeWidth(2);
            add(tempLine);
        }
    }

}
