package anol;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MajorPoints {

    private List<Point2D.Double> pointList = new ArrayList<Point2D.Double>();
    private Point2D.Double origo = new Point2D.Double(0.0, 0.0);

    public void add(Point2D.Double point) {
        pointList.add(point);
    }

    public ListIterator<Point2D.Double> getIterator() {
        return pointList.listIterator();
    }

    public void setOrigo(Point2D.Double origo) {
        this.origo = origo;
    }

    public Point2D.Double getOrigo() {
        return origo;
    }
}
