package anol;

import java.awt.geom.*;
import java.util.Iterator;

public class ConcretePart {

    static double mm2pt(double mm) {
        double points = mm * 72.0 / 25.4;
        return points;
    }

    static String mm2pti(double mm) {
        Double mmpoints = mm2pt(mm);
        Integer immipoints = mmpoints.intValue();
        return immipoints.toString();
    }

    // The AWT coordinate system has origo in the top-left corner

    private String name;
    private Area mainArea;
    private MajorPoints pointList;

    public ConcretePart(String name) {
        this.name = name;
        this.mainArea = new Area();
        this.pointList = new MajorPoints();
    }

    public String getName() {
        return name;
    }

    public void addMjorPoint(double x, double y) {
        Point2D.Double point = new Point2D.Double(x, y);
        pointList.add(point);
    }

    public void subtractRect(double x, double y, double width, double height, double radius, double radius1) {
        Area rekt = new Area(new RoundRectangle2D.Double(x, y, width, height, radius, radius));
        mainArea.subtract(rekt);
    }

    public void subtractCircle(double x, double y, double width, double height) {
        Area sirk = new Area(new Ellipse2D.Double(x, y, width, height));
        mainArea.subtract(sirk);
    }

    public void addRect(double x, double y, double width, double height, double radius, double radius1) {
        mainArea.add(new Area(new Rectangle2D.Double(x, y, width, height)));
    }

    public void setOrigo(double xOffset, double yOffset) {
        pointList.setOrigo(new Point2D.Double(xOffset, yOffset));
    }

    public double getOrigoX() {
        return pointList.getOrigo().getX();
    }

    public double getOrigoY() {
        return pointList.getOrigo().getY();
    }

    public Rectangle2D getBounds() {
        return mainArea.getBounds2D();
    }

    public Point2D.Double getOrigo() {
        return pointList.getOrigo();
    }

    public Iterator<Point2D.Double> getMajorPointIterator() {
        return pointList.getIterator();
    }

    public PathIterator getPathIterator() {
        return mainArea.getPathIterator(null);
    }

    public String getBoundingBox() {
        Rectangle2D bounds = mainArea.getBounds();
        String boundingBox = mm2pti(-bounds.getMinX()) + " " +
                mm2pti(bounds.getMinY()) + " " +
                mm2pti(-bounds.getMaxX()) + " " +
                mm2pti(bounds.getMaxY());
        return boundingBox;
    }

}
