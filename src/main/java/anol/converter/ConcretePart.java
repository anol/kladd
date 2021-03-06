package anol.converter;

import anol.awg.AwtHelper;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.floor;

public class ConcretePart {

    static double mm2pt(double mm) {
        double points = mm * 72.0 / 25.4;
        return points;
    }

    public static String mm2pti(double mm) {
        Double mmpoints = mm2pt(mm);
        Integer immipoints = mmpoints.intValue();
        return immipoints.toString();
    }

    static List<String> getListAttribute(String attribute) {
        List<String> list = new ArrayList<String>();
        for (String subValue : attribute.split(" ")) {
            list.add(subValue);
        }
        return list;
    }

    // The AWT coordinate system has origo in the top-left corner

    private String name;
    private Area mainArea;
    private MajorPoints pointList;
    private MajorPoints helpLines;
    private boolean flip_y = false;
    private boolean flip_x = false;
    private boolean swap_xy = false;
    private final int sheet;

    public ConcretePart(String name, int sheet) {
        this.name = name;
        this.sheet = sheet;
        this.mainArea = new Area();
        this.pointList = new MajorPoints();
        this.helpLines = new MajorPoints();
    }

    public void specialFunction(String special){
        List<String> list = getListAttribute(special);
        for (String func : list) {
            switch (func) {
                case "":
                    break;
                case "swap_xy":
                    swap_xy = true;
                    break;
                case "flip_y":
                    flip_y = true;
                    break;
                case "flip_x":
                    flip_x = true;
                    break;
                default:
                    System.out.println("Ukjent funksjon: \"" + func + "\"");
                    break;
            }
        }
    }

    public String getName() {
        return name;
    }

    private void addHelpLine(double x1, double y1, double x2, double y2) {
        Point2D.Double point1 = new Point2D.Double(x1, y1);
        pointList.add(point1);
        Point2D.Double point2 = new Point2D.Double(x2, y2);
        pointList.add(point2);
    }

    private void addMajorPoint(double x, double y) {
        Point2D.Double point = new Point2D.Double(x, y);
        pointList.add(point);
    }

    public void subtractCircle(double x, double y, double radius) {
        if (swap_xy) {
            double temp = x;
            x = y;
            y = temp;
        }
        if (flip_y) y = -y;
        if (flip_x) x = -x;
        addMajorPoint(x, y);
        x -= radius;
        y -= radius;
        Area sirk = new Area(new Ellipse2D.Double(x, y, radius * 2, radius * 2));
        mainArea.subtract(sirk);
    }

    public void subtractRect(double x, double y, double width, double height, double radius, double rotate) {
        if (swap_xy) {
            double temp = x;
            x = y;
            y = temp;
            temp = width;
            width = height;
            height = temp;
        }
        if (flip_y) {
            y = -y;
            rotate = -rotate;
        }
        if (flip_x) {
            x = -x;
            rotate = -rotate;
        }
        double anchorx = x;
        double anchory = y;
        addMajorPoint(x, y);
        x -= width / 2;
        y -= height / 2;
        Area rekt = new Area(new RoundRectangle2D.Double(x, y, width, height, radius, radius));
        if ((-1.0 > rotate) || (1.0 < rotate)) {
            double theta = Math.toRadians(rotate);
            AffineTransform t = new AffineTransform();
            t.rotate(theta, anchorx, anchory);
            rekt.transform(t);
        }
        mainArea.subtract(rekt);
    }

    public void addRect(double x, double y, double width, double height, double radius, double rotate) {
        if (swap_xy) {
            double temp = x;
            x = y;
            y = temp;
            temp = width;
            width = height;
            height = temp;
        }
        if (flip_y) {
            y = -y;
            rotate = -rotate;
        }
        if (flip_x) {
            x = -x;
            rotate = -rotate;
        }
        double anchorx = x;
        double anchory = y;
        addMajorPoint(x, y);
        x -= width / 2;
        y -= height / 2;
        addMajorPoint(x, y);
        addMajorPoint(x, y + height);
        addMajorPoint(x + width, y + height);
        addMajorPoint(x + width, y);
        Area rekt = new Area(new RoundRectangle2D.Double(x, y, width, height, radius, radius));
        if ((-1.0 > rotate) || (1.0 < rotate)) {
            double theta = Math.toRadians(rotate);
            AffineTransform t = new AffineTransform();
            t.rotate(theta, anchorx, anchory);
            rekt.transform(t);
        }
        mainArea.add(rekt);
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

    public double getWeight(double thickness) {
        final double weightOfSteel = 7.8;
        double areal = floor(AwtHelper.approxArea(mainArea, 1.0, 1) / 100.0);
        double volum = floor(areal * thickness / 10);
        double masse = floor(volum * weightOfSteel) / 1000.0;
        System.out.println("\"" + name + "\": T=" + thickness / 10 + "cm, A=" + areal + "cm2, V=" + volum + "cm3, M=" + masse + "kg");
        return masse;
    }

    public boolean isOnSheet(int sheet) {
        return this.sheet == sheet;
    }
}
