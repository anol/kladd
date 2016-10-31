package anol;

import org.w3c.dom.Element;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

import static java.awt.geom.PathIterator.*;

public class ToPs {

    // The PostScript coordinate system has origo in the bottom-left corner

    public Area inputArea;
    private double oldX = 0.111111111;
    private double oldY = 0.111111111;

    public ToPs(Area inputArea) {
        this.inputArea = inputArea;
    }

    static double mm2pt(double mm) {
        double points = mm * 72.0 / 25.4;
        return points;
    }

    static String mm2pti(double mm) {
        Double mmpoints = mm2pt(mm);
        Integer immipoints = mmpoints.intValue();
        return immipoints.toString();
    }

    String getBoundingBox() {
        Rectangle2D bounds = inputArea.getBounds2D();
        String boundingBox = mm2pti(-bounds.getMinX()) + " " +
                mm2pti(bounds.getMinY()) + " " +
                mm2pti(-bounds.getMaxX()) + " " +
                mm2pti(bounds.getMaxY());
        return boundingBox;
    }

    String getCreationDate() {
        String today = (new Date()).toString();
        return today;
    }

    public String getDocumentHeader(String title, String pageSize) {
        String header = "%!PS-Adobe-2.0\n" +
                "%%Creator: kladd\n" +
                "%%CreationDate: " + getCreationDate() + "\n" +
                "%%Title: " + title + "\n" +
                "%%Pages: 1\n" +
                "%%PageOrder: Ascend\n" +
                "%%BoundingBox: " + getBoundingBox() + "\n" +
                "%%DocumentPaperSizes: " + pageSize + "\n" +
                "%%Orientation: Landscape\n" +
                "%%EndComments\n";
        return header;
    }

    public String getDocumentTrailer() {
        String trailer = "%%EOF\n";
        return trailer;
    }

/*
    A0 = 2384 x 3370
    A1 = 1684 x 2384
    A2 = 1191 x 1684
    A3 = 842 x 1191
    A4 = 595 x 842
*/

    public String getPageHeader(String title, String pageSize) {
        String header = "%%Page: 1 1\n" +
                "%%BeginPageSetup\n" +
                "/pagelevel save def\n" +
                "90 rotate\n";
        header += "%%EndPageSetup\n";
        return header;
    }

    public String getPageTrailer() {
        String trailer = "pagelevel restore\n" + "showpage\n";
        return trailer;
    }

    public String convertArea(Point2D.Double globalOrigo) {
        double origoX = globalOrigo.getX();
        double origoY = globalOrigo.getY();
        String outputString = "";
        for (PathIterator pi = inputArea.getPathIterator(null); !pi.isDone(); pi.next()) {
            double[] coords = new double[6];
            int type = pi.currentSegment(coords);
            switch (type) {
                case SEG_MOVETO: // 1 point
                    outputString += "newpath\n";
                    outputString += mm2pt(-coords[0] + origoX) + " " + mm2pt(coords[1] + origoY) + " moveto\n";
                    break;
                case SEG_LINETO: // 1 point
                    outputString += mm2pt(-coords[0] + origoX) + " " + mm2pt(coords[1] + origoY) + " lineto\n";
                    break;
                case SEG_QUADTO: // 2 point
                    outputString += mm2pt(-coords[0] + origoX) + " " + mm2pt(coords[1] + origoY) + " lineto\n";
                    outputString += mm2pt(-coords[2] + origoX) + " " + mm2pt(coords[3] + origoY) + " lineto\n";
                    break;
                case SEG_CUBICTO: // 3 points
                    outputString += mm2pt(-coords[0] + origoX) + " " + mm2pt(coords[1] + origoY) + " ";
                    outputString += mm2pt(-coords[2] + origoX) + " " + mm2pt(coords[3] + origoY) + " ";
                    outputString += mm2pt(-coords[4] + origoX) + " " + mm2pt(coords[5] + origoY) + " curveto\n";
                    break;
                case SEG_CLOSE: // 0 points
                    outputString += "closepath stroke\n";
                    break;
                default:
                    System.out.print("?");
            }
        }
        return outputString;
    }

    private String drawMarker(Point2D.Double point, Point2D.Double localOrigo, Point2D.Double globalOrigo) {
        double x = mm2pt(-point.getX() + globalOrigo.getX());
        double y = mm2pt(point.getY() + globalOrigo.getY());
        double newX = localOrigo.getX() - point.getX();
        double newY = -localOrigo.getY() + point.getY();
        String outputString = "";
        outputString += "newpath\n";
        outputString += (x - 8) + " " + (y) + " moveto\n";
        outputString += (x + 8) + " " + (y) + " lineto\n";
        outputString += "closepath stroke\n";
        outputString += "newpath\n";
        outputString += (x) + " " + (y - 8) + " moveto\n";
        outputString += (x) + " " + (y + 7) + " lineto\n";
        outputString += "closepath stroke\n";
        if (oldY != newY) {
            outputString += "newpath\n";
            outputString += (x + 9) + " " + (y - 2) + " moveto\n";
            outputString += "(" + newY + ") show\n";
            outputString += "stroke\n";
            oldY = newY;
        }
        if (oldX != newX) {
            outputString += "newpath\n";
            outputString += (x - 8) + " " + (y + 8) + " moveto\n";
            outputString += "(" + newX + ") show\n";
            outputString += "stroke\n";
            oldX = newX;
        }
        return outputString;
    }

    public String convertPoints(MajorPoints pointList) {
        Rectangle2D bounds = inputArea.getBounds2D();
        Point2D.Double localOrigo = new Point2D.Double(bounds.getCenterX(), bounds.getCenterY());
        Point2D.Double globalOrigo = pointList.getOrigo();
        String outputString = "0.25 setlinewidth 1 setlinecap 1 0.2 0.2 setrgbcolor\n";
        outputString += "/Times-Roman findfont 7 scalefont setfont\n";
        Iterator<Point2D.Double> iterator = pointList.getIterator();
        while (iterator.hasNext()) {
            Point2D.Double point = iterator.next();
            outputString += drawMarker(point, localOrigo, globalOrigo);
        }
        return outputString;
    }
}
