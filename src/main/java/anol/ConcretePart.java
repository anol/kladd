package anol;

import java.awt.geom.*;

public class ConcretePart {

    // The AWT coordinate system has origo in the top-left corner

    private Area mainArea;
    private MajorPoints pointList;

    public ConcretePart(Area mainArea, MajorPoints pointList) {
        this.mainArea = mainArea;
        this.pointList = pointList;
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
}
